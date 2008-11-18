/**********************************************************************
 * Copyright (c) 2005-2008 ant4eclipse project team.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Nils Hartmann, Daniel Kasmeroglu, Gerd Wuetherich
 **********************************************************************/
package org.ant4eclipse.platform.model.resource.variable.internal;

import java.io.File;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Map;
import java.util.Vector;

import org.ant4eclipse.platform.model.resource.EclipseProject;
import org.ant4eclipse.platform.model.resource.variable.EclipseVariableResolver;

import net.sf.ant4eclipse.core.Assert;

/**
 * <p>
 * </p>
 * 
 * @author Nils Hartmann (nils@nilshartmann.net)
 */
public class EclipseVariableResolverImpl implements EclipseVariableResolver {

  /** a mapping for the eclipse variables */
  private final Map<String, String> _eclipsevariables = new Hashtable<String, String>();

  public void clear() {
    this._eclipsevariables.clear();
  }

  public void dispose() {
    //
  }

  public void initialize() {
    //
  }

  public boolean isInitialized() {
    return false;
  }

  /**
   * Changes a variable for this instance.
   * 
   * @param key
   *          The name of the variable.
   * @param value
   *          The value of the variable. A value of null causes the variable to be removed.
   */
  public final void setEclipseVariable(final String key, final String value) {
    if (value == null) {
      this._eclipsevariables.remove(key);
    } else {
      this._eclipsevariables.put(key, value);
    }
  }

  /**
   * Substitutes all occurences of an eclipse <b>variable</b> (aka as <b>property</b> in ant)in the given string.
   * 
   * The value for a variable in <code>string</code> is first searched in <code>otherProperties</code>. If the value
   * cannot be found there it will be resolved as eclipse would do it.
   * 
   * If a variable contains an <b>argument</b> (<code>${workspace_loc:/path/to/myWorkspace}</code>) the <b>argument</b>
   * is ignored.
   * 
   * @param string
   *          The string with variables
   * @param project
   *          The project that should be used for resolving variables like <code>project_loc</code>
   * @param otherProperties
   *          Table with variable names as keys and their values as values. Might be null.
   */
  public final String resolveEclipseVariables(final String string, final EclipseProject project,
      final Map<String, String> otherProperties) {
    Assert.notNull(string);
    // Assert.notNull(project);
    // resolve Eclipse variables
    final Map<String, String> eclipseVariables = getEclipseVariables(project);

    // overwrite "default" values for eclipse variables with values as specified in otherProperties
    if (otherProperties != null) {
      eclipseVariables.putAll(otherProperties);
    }

    // resolve string
    final String resolvedString = resolveProperties(string, eclipseVariables);
    return resolvedString;
  }

  /**
   * Returns a map with the eclipse variables where each key corresponds to a key allowing to access it's value.
   * 
   * @return The map providing the necessary (key, value) pairs.
   */
  public final Map<String, String> getEclipseVariables() {
    return (getEclipseVariables(null));
  }

  /**
   * Returns a map with the eclipse variables where each key corresponds to a key allowing to access it's value.
   * 
   * @param project
   *          The Eclipse project allowing to produce some project specific variables.
   * 
   * @return The map providing the necessary (key, value) pairs.
   */
  public final Map<String, String> getEclipseVariables(final EclipseProject project) {
    final Map<String, String> eclipseVariables = new Hashtable<String, String>();
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
   * from org.apache.tools.ant.PropertyHelper
   */
  private final String resolveProperties(final String value, final Map<String, String> properties) {

    final Vector<String> fragments = new Vector<String>();
    final Vector<String> propertyRefs = new Vector<String>();
    final Vector<String> propertyArgs = new Vector<String>();
    parsePropertyString(value, fragments, propertyRefs, propertyArgs);

    final StringBuffer sb = new StringBuffer();
    final Enumeration<String> i = fragments.elements();
    final Enumeration<String> j = propertyRefs.elements();
    final Enumeration<String> k = propertyArgs.elements();

    while (i.hasMoreElements()) {
      String fragment = i.nextElement();
      if (fragment == null) {
        final String propertyName = j.nextElement();
        final String propertyArg = k.nextElement();
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
        final String arg = propertyArg != null ? ":" + propertyArg : "";
        fragment = (replacement != null) ? replacement.toString() : "${" + propertyName + arg + "}";
      }
      sb.append(fragment);
    }

    return sb.toString();
  }

  /**
   * based on org.apache.tools.ant.PropertyHelper#parsePropertyString
   */
  private final void parsePropertyString(final String value, final Vector<String> fragments,
      final Vector<String> propertyRefs, final Vector<String> propertyArgs) {
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
        final int endName = value.indexOf('}', pos);
        if (endName < 0) {
          throw new RuntimeException("Syntax error in property: " + value);
        }
        String propertyName = value.substring(pos + 2, endName);
        // cut off eclipse arguments, since they are not supported
        // by ant4eclipse
        final int v = propertyName.indexOf(':');
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
