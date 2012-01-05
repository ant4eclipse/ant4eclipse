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
package org.ant4eclipse.lib.platform.model.resource.variable;

import org.ant4eclipse.lib.core.A4EService;
import org.ant4eclipse.lib.core.util.StringMap;
import org.ant4eclipse.lib.platform.model.resource.EclipseProject;

/**
 * <p>
 * </p>
 * 
 * @author Nils Hartmann (nils@nilshartmann.net)
 * @author Daniel Kasmeroglu (Daniel.Kasmeroglu@kasisoft.net)
 */
public interface EclipseStringSubstitutionService extends A4EService {

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
  String substituteEclipseVariables(String string, EclipseProject project, StringMap otherProperties);

} /* ENDINTERFACE */
