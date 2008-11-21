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
package org.ant4eclipse.platform.model.resource.internal.factory;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

import org.ant4eclipse.core.Ant4EclipseConfigurationProperties;
import org.ant4eclipse.core.Assert;
import org.ant4eclipse.core.logging.A4ELogging;
import org.ant4eclipse.core.util.Utilities;
import org.ant4eclipse.platform.model.resource.EclipseProject;
import org.ant4eclipse.platform.model.resource.internal.EclipseProjectImpl;
import org.ant4eclipse.platform.model.resource.internal.WorkspaceImpl;
import org.ant4eclipse.platform.model.resource.role.ProjectRole;
import org.ant4eclipse.platform.model.resource.role.ProjectRoleIdentifier;

/**
 * A Factory that builds EclipseProjects
 * 
 * @author Nils Hartmann (nils@nilshartmann.net)
 */
public class ProjectFactory {

  /**
   * The prefix of properties that holds a RoleIdentifier class name
   */
  public final static String              ROLEIDENTIFIER_PREFIX = "roleidentifier";

  /**
   * All known {@link ProjectRoleIdentifier}
   */
  private Iterable<ProjectRoleIdentifier> _projectRoleIdentifiers;

  public ProjectFactory() {
    init();
  }

  /**
   * Loads the configured RoleIdentifiers
   */
  protected void init() {
    // get all properties that defines a ProjectRoleIdentifier
    Iterable<String[]> roleidentifierEntries = Ant4EclipseConfigurationProperties.getInstance().getAllProperties(
        ROLEIDENTIFIER_PREFIX);

    final List<ProjectRoleIdentifier> roleIdentifiers = new LinkedList<ProjectRoleIdentifier>();

    // Instantiate all ProjectRoleIdentifiers
    for (String[] roleidentifierEntry : roleidentifierEntries) {
      // we're not interested in the key of a roleidentifier. only the classname (value of the entry) is relevant
      String roleidentiferClassName = roleidentifierEntry[1];
      ProjectRoleIdentifier roleIdentifier = Utilities.newInstance(roleidentiferClassName);
      A4ELogging.trace("Register ProjectRoleIdentifier '%s'", new Object[] { roleIdentifier });
      roleIdentifiers.add(roleIdentifier);
    }

    this._projectRoleIdentifiers = roleIdentifiers;
  }

  /**
   * Reads the configuration for the given project and sets up a new EclipseProject for it
   * 
   * @param workspace
   *          The workspace that contains the project
   * @param projectDirectory
   *          The root directory of the project
   * @return a configured EclipseProject instance
   */
  public EclipseProject readProjectFromWorkspace(final WorkspaceImpl workspace, final File projectDirectory) {

    A4ELogging.trace("ProjectFactory: readProjectFromWorkspace(%s, %s)", new Object[] { workspace,
        projectDirectory.getAbsolutePath() });

    Assert.notNull(workspace);
    Assert.isDirectory(projectDirectory);

    final EclipseProjectImpl project = new EclipseProjectImpl(workspace, projectDirectory);

    // parses the project description
    ProjectFileParser.parseProject(project);

    // apply role specific information
    applyRoles(project);

    A4ELogging.trace("ProjectFactory: return '%s'", project);
    return project;

  }

  /**
   * Modifies the supplied project according to all currently registered RoleIdentifier instances.
   * 
   * @param project
   *          The project that shall be modified.
   */
  protected void applyRoles(final EclipseProjectImpl project) {
    for (ProjectRoleIdentifier projectRoleIdentifier : _projectRoleIdentifiers) {
      if (projectRoleIdentifier.isRoleSupported(project)) {
        final ProjectRole projectRole = projectRoleIdentifier.createRole(project);
        project.addRole(projectRole);
      }
    }
  }
}
