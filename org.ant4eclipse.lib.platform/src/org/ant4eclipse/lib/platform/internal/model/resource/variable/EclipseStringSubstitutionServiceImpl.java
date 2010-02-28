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
package org.ant4eclipse.lib.platform.internal.model.resource.variable;

import java.io.File;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Vector;

import org.ant4eclipse.lib.core.Assure;
import org.ant4eclipse.lib.core.configuration.Ant4EclipseConfiguration;
import org.ant4eclipse.lib.core.logging.A4ELogging;
import org.ant4eclipse.lib.core.service.ServiceRegistry;
import org.ant4eclipse.lib.core.util.Pair;
import org.ant4eclipse.lib.core.util.StringMap;
import org.ant4eclipse.lib.core.util.Utilities;
import org.ant4eclipse.lib.platform.model.resource.EclipseProject;
import org.ant4eclipse.lib.platform.model.resource.variable.EclipseStringSubstitutionService;

/**
 * <p>
 * 
 * </p>
 * 
 * @author Nils Hartmann (nils@nilshartmann.net)
 */
public class EclipseStringSubstitutionServiceImpl implements EclipseStringSubstitutionService {

  private EclipseVariableResolver[] _eclipseVariableResolvers;

  /**
   * The prefix of properties that holds class names of EclipseVariableResolvers
   */
  public static final String        PREFIX_VARIABLE_RESOLVER = "eclipseVariableResolver";

  /** Parser used to parse a String with properties */
  private PropertyParser            _propertyParser          = new PropertyParser();

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
    Ant4EclipseConfiguration config = ServiceRegistry.instance().getService(Ant4EclipseConfiguration.class);
    Iterable<Pair<String, String>> entries = config.getAllProperties(PREFIX_VARIABLE_RESOLVER);

    List<EclipseVariableResolver> resolvers = new ArrayList<EclipseVariableResolver>();

    // Instantiate all ProjectRoleIdentifiers
    for (Pair<String, String> types : entries) {
      // we're not interested in the key of a project validator. only the classname (value of the entry) is relevant
      EclipseVariableResolver eclipseVariableResolver = Utilities.newInstance(types.getSecond(), types.getFirst());
      A4ELogging.trace("Register EclipseVariableResolver '%s'", eclipseVariableResolver);
      resolvers.add(eclipseVariableResolver);
    }

    this._eclipseVariableResolvers = resolvers.toArray(new EclipseVariableResolver[resolvers.size()]);
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
  public final String substituteEclipseVariables(String string, EclipseProject project, StringMap otherProperties) {
    Assure.notNull("string", string);
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

  protected StringMap getEclipseVariables(EclipseProject eclipseProject) {
    StringMap eclipseVariables = new StringMap();

    for (EclipseVariableResolver resolver : this._eclipseVariableResolvers) {
      resolver.getResolvedVariables(eclipseVariables, eclipseProject);
    }

    // if (project != null) {
    // eclipseVariables.put("build_project", project.getFolder().getAbsolutePath());
    // eclipseVariables.put("build_type", "full");
    // eclipseVariables.put("project_loc", project.getFolder().getAbsolutePath());
    // eclipseVariables.put("project_name", project.getFolderName());
    // // TODO: is project_path always project_name ?
    // eclipseVariables.put("project_path", project.getFolderName());
    // // TODO: replace
    // // eclipseVariables.put("workspace_loc", project.getWorkspace().getAbsolutePath());
    // }
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
    this._propertyParser.parsePropertyString(value, fragments, propertyRefs, propertyArgs);

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

}
