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
package org.ant4eclipse.lib.platform.internal.model.resource.role;

import org.ant4eclipse.lib.core.A4ECore;
import org.ant4eclipse.lib.platform.internal.model.resource.EclipseProjectImpl;
import org.ant4eclipse.lib.platform.model.resource.EclipseProject;
import org.ant4eclipse.lib.platform.model.resource.role.ProjectRoleIdentifier;

import java.util.ArrayList;
import java.util.List;

/**
 * The ProjectRoleIdentifierRegistry holds all known {@link ProjectRoleIdentifier}s. It can be used to apply roles to
 * {@link EclipseProjectImpl}s
 * 
 * @author Nils Hartmann (nils@nilshartmann.net)
 */
public class ProjectRoleIdentifierRegistry {

  /**
   * All known {@link ProjectRoleIdentifier}
   */
  private List<ProjectRoleIdentifier> roleidentifiers;

  public ProjectRoleIdentifierRegistry() {
    roleidentifiers = new ArrayList<ProjectRoleIdentifier>();
    roleidentifiers.addAll( A4ECore.instance().getServices( ProjectRoleIdentifier.class ) );
  }

  /**
   * Modifies the supplied project according to all currently registered RoleIdentifier instances.
   * 
   * @param project
   *          The project that shall be modified. Not <code>null</code>.
   */
  public void applyRoles( EclipseProjectImpl project ) {
    for( ProjectRoleIdentifier roleidentifier : roleidentifiers ) {
      if( roleidentifier.isRoleSupported( project ) ) {
        project.addRole( roleidentifier.createRole( project ) );
      }
    }
  }

  /**
   * Performs a post processing step for the roles. This might be necessary in situations where the applied role does
   * not have all information needed (especially when the information must be provided by other projects which have not
   * been processed at role application time).
   * 
   * @param project
   *          The project used for the post processing step. Not <code>null</code>.
   */
  public void postProcessRoles(EclipseProject project) {
    for( ProjectRoleIdentifier roleidentifier : roleidentifiers ) {
      if( roleidentifier.isRoleSupported( project ) ) {
        roleidentifier.postProcess( project );
      }
    }
  }

  /**
   * Provides an {@link Iterable} which can be used to run through all currently registered
   * {@link ProjectRoleIdentifier} instances.
   * 
   * @return   An {@link Iterable} which can be used to run through all currently registered
   *           {@link ProjectRoleIdentifier} instances. Not <code>null</code>.
   */
  public Iterable<ProjectRoleIdentifier> getProjectRoleIdentifiers() {
    return this.roleidentifiers;
  }

} /* ENDCLASS */
