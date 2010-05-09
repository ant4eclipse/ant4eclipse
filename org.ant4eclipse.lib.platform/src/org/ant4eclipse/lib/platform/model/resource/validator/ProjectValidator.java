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
package org.ant4eclipse.lib.platform.model.resource.validator;

import org.ant4eclipse.lib.platform.model.resource.role.ProjectRole;

/**
 * <p>
 * Each implementation is capable to validate a certain aspect of a project configuration since a configuration isn't
 * necessarily correct.
 * </p>
 * 
 * @author Daniel Kasmeroglu (daniel.kasmeroglu@kasisoft.net)
 */
public interface ProjectValidator {

  /**
   * <p>
   * Returns <code>true</code> if this validator is capable to handle the supplied role.
   * 
   * @param role
   *          The role used for the test. Not <code>null</code>.
   * 
   * @return <code>true</code> <=> This validator is capable to handle the supplied role.
   */
  boolean canValidate(ProjectRole role);

  /**
   * <p>
   * Validates the content of a specific project from the perspective of a specific role.
   * </p>
   * 
   * @param role
   *          The role used to provide the necessary information. Not <code>null</code> and
   *          {@link #canValidate(ProjectRole)} evaluates to <code>true</code>.
   */
  void validate(ProjectRole role);

} /* ENDINTERFACE */
