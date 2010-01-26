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

import org.ant4eclipse.core.configuration.Ant4EclipseConfiguration;
import org.ant4eclipse.core.logging.A4ELogging;
import org.ant4eclipse.core.util.Pair;
import org.ant4eclipse.core.util.Utilities;

import org.ant4eclipse.lib.platform.internal.model.resource.EclipseProjectImpl;
import org.ant4eclipse.lib.platform.model.resource.EclipseProject;
import org.ant4eclipse.lib.platform.model.resource.role.ProjectRole;
import org.ant4eclipse.lib.platform.model.resource.role.ProjectRoleIdentifier;

import java.util.LinkedList;
import java.util.List;

/**
 * The ProjectRoleIdentifierRegistry holds all known {@link ProjectRoleIdentifier}s. It can be used to apply roles to
 * {@link EclipseProjectImpl}s
 * 
 * @author Nils Hartmann (nils@nilshartmann.net)
 */
public class ProjectRoleIdentifierRegistry {

  /**
   * The prefix of properties that holds a RoleIdentifier class name
   */
  public static final String              ROLEIDENTIFIER_PREFIX = "roleidentifier";

  /**
   * All known {@link ProjectRoleIdentifier}
   */
  private Iterable<ProjectRoleIdentifier> _projectRoleIdentifiers;

  public ProjectRoleIdentifierRegistry() {
    init();
  }

  /**
   * Modifies the supplied project according to all currently registered RoleIdentifier instances.
   * 
   * @param project
   *          The project that shall be modified. Not <code>null</code>.
   */
  public void applyRoles(EclipseProjectImpl project) {
    for (ProjectRoleIdentifier projectRoleIdentifier : this._projectRoleIdentifiers) {
      if (projectRoleIdentifier.isRoleSupported(project)) {
        ProjectRole projectRole = projectRoleIdentifier.createRole(project);
        project.addRole(projectRole);
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
    for (ProjectRoleIdentifier projectRoleIdentifier : this._projectRoleIdentifiers) {
      if (projectRoleIdentifier.isRoleSupported(project)) {
        projectRoleIdentifier.postProcess(project);
      }
    }
  }

  public Iterable<ProjectRoleIdentifier> getProjectRoleIdentifiers() {
    return this._projectRoleIdentifiers;
  }

  /**
   * Loads the configured RoleIdentifiers
   */
  protected void init() {
    // get all properties that defines a ProjectRoleIdentifier
    Iterable<Pair<String, String>> roleidentifierEntries = Ant4EclipseConfiguration.Helper
        .getAnt4EclipseConfiguration().getAllProperties(ROLEIDENTIFIER_PREFIX);

    List<ProjectRoleIdentifier> roleIdentifiers = new LinkedList<ProjectRoleIdentifier>();

    // Instantiate all ProjectRoleIdentifiers
    for (Pair<String, String> roleidentifierEntry : roleidentifierEntries) {
      // we're not interested in the key of a roleidentifier. only the classname (value of the entry) is relevant
      ProjectRoleIdentifier roleIdentifier = Utilities.newInstance(roleidentifierEntry.getSecond());
      A4ELogging.trace("Register ProjectRoleIdentifier '%s'", roleIdentifier);
      roleIdentifiers.add(roleIdentifier);
    }

    this._projectRoleIdentifiers = roleIdentifiers;
  }

}
