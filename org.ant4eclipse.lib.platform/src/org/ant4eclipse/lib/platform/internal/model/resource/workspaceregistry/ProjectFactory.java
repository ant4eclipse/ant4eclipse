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
package org.ant4eclipse.lib.platform.internal.model.resource.workspaceregistry;

import org.ant4eclipse.core.logging.A4ELogging;


import org.ant4eclipse.lib.core.Assert;
import org.ant4eclipse.lib.platform.internal.model.resource.EclipseProjectImpl;
import org.ant4eclipse.lib.platform.internal.model.resource.WorkspaceImpl;
import org.ant4eclipse.lib.platform.internal.model.resource.role.ProjectRoleIdentifierRegistry;
import org.ant4eclipse.lib.platform.model.resource.EclipseProject;
import org.ant4eclipse.lib.platform.model.resource.validator.ValidatorRegistry;

import java.io.File;

/**
 * A Factory that builds EclipseProjects
 * 
 * @author Nils Hartmann (nils@nilshartmann.net)
 */
public class ProjectFactory {

  private ProjectRoleIdentifierRegistry _projectRoleIdentifierRegistry;

  private ValidatorRegistry             _validatorRegistry;

  public ProjectFactory() {
    this._projectRoleIdentifierRegistry = new ProjectRoleIdentifierRegistry();
    this._validatorRegistry = new ValidatorRegistry();
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
  public EclipseProject readProjectFromWorkspace(WorkspaceImpl workspace, File projectDirectory) {

    A4ELogging.trace("ProjectFactory: readProjectFromWorkspace(%s, %s)", workspace, projectDirectory.getAbsolutePath());

    Assert.notNull(workspace);
    Assert.isDirectory(projectDirectory);

    EclipseProjectImpl project = new EclipseProjectImpl(workspace, projectDirectory);

    // parses the project description
    ProjectFileParser.parseProject(project);

    // apply role specific information
    this._projectRoleIdentifierRegistry.applyRoles(project);

    A4ELogging.trace("ProjectFactory: return '%s'", project);
    return project;

  }

  /**
   * Performs a postprocessing for each registere project role. The project roles already have been setup but operations
   * that might require to have access to other projects can be performed now.
   * 
   * @param project
   *          The project which roles should be postprocessed. Not <code>null</code>.
   */
  public void postProcessRoleSetup(EclipseProject project) {
    A4ELogging.trace("ProjectFactory: postProcessRoleSetup(%s)", project.getSpecifiedName());
    this._projectRoleIdentifierRegistry.postProcessRoles(project);
    this._validatorRegistry.validate(project);
  }

}
