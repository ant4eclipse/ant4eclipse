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
package org.ant4eclipse.ant.platform.core.delegate;

import org.ant4eclipse.ant.platform.core.EclipseProjectComponent;
import org.ant4eclipse.lib.core.exception.Ant4EclipseException;
import org.ant4eclipse.lib.platform.PlatformExceptionCode;
import org.ant4eclipse.lib.platform.model.resource.EclipseProject;
import org.ant4eclipse.lib.platform.model.resource.role.ProjectRole;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.ProjectComponent;

import java.io.File;

/**
 * <p>
 * Delegate class for ant4eclipse tasks, conditions and types that require a eclipse project.
 * </p>
 * 
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 */
public class EclipseProjectDelegate extends WorkspaceDelegate implements EclipseProjectComponent {

  /** the project name */
  private String         _projectName;

  /** the eclipse project */
  private EclipseProject _eclipseProject;

  /**
   * <p>
   * Creates a new instance of type {@link EclipseProjectDelegate}.
   * </p>
   * 
   * @param component
   *          the ProjectComponent
   */
  public EclipseProjectDelegate(ProjectComponent component) {
    super(component);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  @Deprecated
  public void setProject(File projectPath) {
    throw new Ant4EclipseException(PlatformExceptionCode.DEPRECATED_USAGE_OF_SET_PROJECT);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public final void setProjectName(String projectName) {
    this._projectName = projectName;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public final boolean isProjectNameSet() {
    return this._projectName != null;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public final void requireWorkspaceAndProjectNameSet() {

    if (!(isWorkspaceDirectorySet() || isWorkspaceIdSet()) || !isProjectNameSet()) {
      throw new Ant4EclipseException(PlatformExceptionCode.MISSING_WORKSPACE_AND_PROJECT_NAME);
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public EclipseProject getEclipseProject() throws BuildException {

    // get eclipse project if it is not already set
    if (this._eclipseProject == null) {
      if (getWorkspace().hasProject(this._projectName)) {
        this._eclipseProject = getWorkspace().getProject(this._projectName);
      } else {
        throw new Ant4EclipseException(PlatformExceptionCode.SPECIFIED_PROJECT_DOES_NOT_EXIST, this._projectName);
      }
    }

    // return the eclipse project
    return this._eclipseProject;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void ensureRole(Class<? extends ProjectRole> projectRoleClass) {

    // 
    if (!getEclipseProject().hasRole(projectRoleClass)) {

      // throw exception
      throw new Ant4EclipseException(PlatformExceptionCode.MISSING_PROJECT_ROLE, this._projectName, projectRoleClass
          .getName());
    }
  }
}