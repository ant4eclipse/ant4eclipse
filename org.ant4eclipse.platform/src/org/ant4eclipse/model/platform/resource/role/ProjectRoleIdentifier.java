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
package org.ant4eclipse.model.platform.resource.role;

import org.ant4eclipse.model.platform.resource.EclipseProject;

/**
 * <p>
 * The {@link ProjectRoleIdentifier} defines the common interface for project role identifiers. A project role
 * identifier can be used to assign a specific role to an eclipse project when the project is reed from the underlying
 * workspace.
 * </p>
 * 
 * @author Daniel Kasmeroglu (daniel.kasmeroglu@kasisoft.net)
 */
public interface ProjectRoleIdentifier {

  /**
   * <p>
   * Returns <code>true</code> if the supplied project provides support for a specific role.
   * </p>
   * 
   * @param project
   *          the eclipse project that should be tested.
   * 
   * @return <code>true</code> if the role is applicable for the eclipse project.
   */
  public boolean isRoleSupported(EclipseProject project);

  /**
   * <p>
   * Creates the specific {@link ProjectRole} for the given {@link EclipseProject}.
   * </p>
   * 
   * @param project
   *          the eclipse project.
   */
  public ProjectRole createRole(final EclipseProject project);
} /* ENDCLASS */