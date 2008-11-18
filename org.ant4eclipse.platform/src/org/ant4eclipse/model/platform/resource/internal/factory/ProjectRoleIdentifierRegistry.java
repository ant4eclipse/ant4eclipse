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
package org.ant4eclipse.model.platform.resource.internal.factory;

import java.util.Vector;

import org.ant4eclipse.model.platform.resource.EclipseProject;
import org.ant4eclipse.model.platform.resource.internal.EclipseProjectImpl;
import org.ant4eclipse.model.platform.resource.role.ProjectRole;
import org.ant4eclipse.model.platform.resource.role.ProjectRoleIdentifier;


/**
 * A simple registry used for project role identifiers.
 * 
 * @author Daniel Kasmeroglu (daniel.kasmeroglu@kasisoft.net)
 */
public class ProjectRoleIdentifierRegistry {

  private static final Vector<ProjectRoleIdentifier> _projectroles = new Vector<ProjectRoleIdentifier>();

  /**
   * Prevent instantiation.
   */
  private ProjectRoleIdentifierRegistry() {
    // Prevent instantiation.
  }

  /**
   * Adds the supplied role identifier to this registry.
   * 
   * @param newrole
   *          The role identifier that shall be added.
   */
  public static final void addRoleIdentifier(final ProjectRoleIdentifier newrole) {
    _projectroles.add(newrole);
  }

  /**
   * Modifies the supplied project according to all currently registered RoleIdentifier instances.
   * 
   * @param project
   *          The project that shall be modified.
   */
  public static final void applyRoles(final EclipseProject project) {
    for (int i = 0; i < _projectroles.size(); i++) {
      final ProjectRoleIdentifier role = _projectroles.get(i);
      if (role.isRoleSupported(project)) {
        final ProjectRole projectRole = role.createRole(project);
        ((EclipseProjectImpl) project).addRole(projectRole);
      }
    }
  }

} /* ENDCLASS */
