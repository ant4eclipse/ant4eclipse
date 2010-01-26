/**********************************************************************
 * Copyright (c) 2005-2009 ant4eclipse project team.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Nils Hartmann, Daniel Kasmeroglu, Gerd Wuetherich
 **********************************************************************/
package org.ant4eclipse.platform.internal.model.resource.variable;

import org.ant4eclipse.core.util.StringMap;

import org.ant4eclipse.platform.model.resource.EclipseProject;
import org.ant4eclipse.platform.model.resource.variable.EclipseVariableResolver;

import org.ant4eclipse.lib.core.Assert;

import java.io.File;
import java.util.Enumeration;
import java.util.Vector;

/**
 * <p>
 * 
 * </p>
 * 
 * @author Nils Hartmann (nils@nilshartmann.net)
 */
public class EclipseVariableResolverImpl implements EclipseVariableResolver {

  /** a mapping for the eclipse variables */
  private StringMap _eclipsevariables = new StringMap();

  /**
   * {@inheritDoc}
   */
  public void dispose() {
    //
  }

  /**
   * {@inheritDoc}
   */
  public void initialize() {
    //
  }

  /**
   * {@inheritDoc}
   */
  public boolean isInitialized() {
    return false;
  }

  /**
   * {@inheritDoc}
   */
  public final void setEclipseVariable(String key, String value) {
    if (value == null) {
      this._eclipsevariables.remove(key);
    } else {
      this._eclipsevariables.put(key, value);
    }
  }

  /**
   * {@inheritDoc}
   */
  public final String resolveEclipseVariables(String string, EclipseProject project, StringMap otherProperties) {
    Assert.notNull(string);
    // Assert.notNull(project);
    // resolve Eclipse variables
    StringMap eclipseVariables = getEclipseVariables(project);

    // overwrite "default" values for eclipse variables with values as specified in otherProperties
    if (otherProperties != null) {
      eclipseVariables.putAll(otherProperties);
    }

    // resolve string
    String resolvedString = resolveProperties(string, eclipseVariables);
    return resolvedString;
  }

  /**
   * {@inheritDoc}
   */
  public final StringMap getEclipseVariables() {
    return (getEclipseVariables(null));
  }

  /**
   * {@inheritDoc}
   */
  public final StringMap getEclipseVariables(EclipseProject project) {
    StringMap eclipseVariables = new StringMap();
    eclipseVariables.putAll(this._eclipsevariables);
    if (project != null) {
      eclipseVariables.put("build_project", project.getFolder().getAbsolutePath());
      eclipseVariables.put("build_type", "full");
      eclipseVariables.put("project_loc", project.getFolder().getAbsolutePath());
      eclipseVariables.put("project_name", project.getFolderName());
      // TODO: is project_path always project_name ?
      eclipseVariables.put("project_path", project.getFolderName());
      // TODO: replace
      // eclipseVariables.put("workspace_loc", project.getWorkspace().getAbsolutePath());
    }
    return eclipseVariables;
  }

  /**
   * <p>
   * </p>
   * 
   * @param value
   * @param properties
   * @return
   */
  private final String resolveProperties(String value, StringMap properties) {

    Vector<String> fragments = new Vector<String>();
    Vector<String> propertyRefs = new Vector<String>();
    Vector<String> propertyArgs = new Vector<String>();
    parsePropertyString(value, fragments, propertyRefs, propertyArgs);

    StringBuffer sb = new StringBuffer();
    Enumeration<String> i = fragments.elements();
    Enumeration<String> j = propertyRefs.elements();
    Enumeration<String> k = propertyArgs.elements();

    while (i.hasMoreElements()) {
      String fragment = i.nextElement();
      if (fragment == null) {
        String propertyName = j.nextElement();
        String propertyArg = k.nextElement();
        Object replacement = null;
        if (properties != null) {
          if ("workspace_loc".equals(propertyName)) {
            replacement = properties.get(propertyName);
            if ((propertyArg != null) && (propertyArg.length() > 0)) {
              replacement = replacement + File.separator + propertyArg;
            }
          } else if ("env_var".equals(propertyName)) {
            if ((propertyArg != null) && (propertyArg.length() > 0)) {
              replacement = System.getProperty(propertyArg);
            }
          } else {
            replacement = properties.get(propertyName);
          }
        }
        String arg = propertyArg != null ? ":" + propertyArg : "";
        fragment = (replacement != null) ? replacement.toString() : "${" + propertyName + arg + "}";
      }
      sb.append(fragment);
    }

    return sb.toString();
  }

  /**
   * based on org.apache.tools.ant.PropertyHelper#parsePropertyString
   */
  private final void parsePropertyString(String value, Vector<String> fragments, Vector<String> propertyRefs,
      Vector<String> propertyArgs) {
    int prev = 0;
    int pos;
    // search for the next instance of $ from the 'prev' position
    while ((pos = value.indexOf("$", prev)) >= 0) {

      // if there was any text before this, add it as a fragment
      // TODO, this check could be modified to go if pos>prev;
      // seems like this current version could stick empty strings
      // into the list
      if (pos > 0) {
        fragments.addElement(value.substring(prev, pos));
      }
      // if we are at the end of the string, we tack on a $
      // then move past it
      if (pos == (value.length() - 1)) {
        fragments.addElement("$");
        prev = pos + 1;
      } else if (value.charAt(pos + 1) != '{') {
        // peek ahead to see if the next char is a property or not
        // not a property: insert the char as a literal
        /*
         * fragments.addElement(value.substring(pos + 1, pos + 2)); prev = pos + 2;
         */
        if (value.charAt(pos + 1) == '$') {
          // backwards compatibility two $ map to one mode
          fragments.addElement("$");
          prev = pos + 2;
        } else {
          // new behaviour: $X maps to $X for all values of X!='$'
          fragments.addElement(value.substring(pos, pos + 2));
          prev = pos + 2;
        }

      } else {
        // property found, extract its name or bail on a typo
        int endName = value.indexOf('}', pos);
        if (endName < 0) {
          throw new RuntimeException("Syntax error in property: " + value);
        }
        String propertyName = value.substring(pos + 2, endName);
        // cut off eclipse arguments, since they are not supported
        // by ant4eclipse
        int v = propertyName.indexOf(':');
        String propertyArg = null;
        if (v != -1) {
          propertyArg = propertyName.substring(v + 1);
          propertyName = propertyName.substring(0, v);
        }
        fragments.addElement(null);
        propertyRefs.addElement(propertyName);
        propertyArgs.addElement(propertyArg);
        prev = endName + 1;
      }
    }
    // no more $ signs found
    // if there is any tail to the file, append it
    if (prev < value.length()) {
      fragments.addElement(value.substring(prev));
    }
  }
}
