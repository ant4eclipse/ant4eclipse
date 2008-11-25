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
package org.ant4eclipse.platform.model.internal.resource.workspaceregistry;

import java.io.File;

import org.ant4eclipse.core.Assert;
import org.ant4eclipse.core.logging.A4ELogging;
import org.ant4eclipse.platform.model.internal.resource.EclipseProjectImpl;
import org.ant4eclipse.platform.model.internal.resource.WorkspaceImpl;
import org.ant4eclipse.platform.model.internal.resource.role.ProjectRoleIdentifierRegistry;
import org.ant4eclipse.platform.model.resource.EclipseProject;

/**
 * A Factory that builds EclipseProjects
 * 
 * @author Nils Hartmann (nils@nilshartmann.net)
 */
public class ProjectFactory {

  private ProjectRoleIdentifierRegistry _projectRoleIdentifierRegistry;

  public ProjectFactory() {
    _projectRoleIdentifierRegistry = new ProjectRoleIdentifierRegistry();
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
    _projectRoleIdentifierRegistry.applyRoles(project);

    A4ELogging.trace("ProjectFactory: return '%s'", project);
    return project;

  }

}
