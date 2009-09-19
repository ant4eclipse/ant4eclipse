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
package org.ant4eclipse.platform.model.resource.variable;

import org.ant4eclipse.core.Lifecycle;
import org.ant4eclipse.core.util.StringMap;

import org.ant4eclipse.platform.model.resource.EclipseProject;

/**
 * <p>
 * </p>
 * 
 * @author Nils Hartmann (nils@nilshartmann.net)
 */
public interface EclipseVariableResolver extends Lifecycle {

  /**
   * Changes a variable for this instance.
   * 
   * @param key
   *          The name of the variable.
   * @param value
   *          The value of the variable. A value of null causes the variable to be removed.
   */
  void setEclipseVariable(String key, String value);

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
  String resolveEclipseVariables(String string, EclipseProject project, StringMap otherProperties);

  /**
   * Returns a map with the eclipse variables where each key corresponds to a key allowing to access it's value.
   * 
   * @return The map providing the necessary (key, value) pairs.
   */
  StringMap getEclipseVariables();

  /**
   * Returns a map with the eclipse variables where each key corresponds to a key allowing to access it's value.
   * 
   * @param project
   *          The Eclipse project allowing to produce some project specific variables.
   * 
   * @return The map providing the necessary (key, value) pairs.
   */
  StringMap getEclipseVariables(EclipseProject project);

} /* ENDINTERFACE */
