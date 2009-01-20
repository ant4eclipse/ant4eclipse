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
package org.ant4eclipse.platform.ant.core.delegate;

import org.ant4eclipse.platform.ant.core.EclipseProjectComponent;
import org.ant4eclipse.platform.model.resource.EclipseProject;
import org.ant4eclipse.platform.model.resource.role.ProjectRole;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.ProjectComponent;

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
  public EclipseProjectDelegate(final ProjectComponent component) {
    super(component);
  }

  /**
   * <p>
   * Sets the project name.
   * </p>
   * 
   * @param projectName
   *          the project name.
   */
  public final void setProjectName(final String projectName) {
    this._projectName = projectName;
  }

  /**
   * <p>
   * Returns <code>true</code> if the project name has been set.
   * </p>
   * 
   * @return <code>true</code> if the project name has been set.
   */
  public final boolean isProjectNameSet() {
    return this._projectName != null;
  }

  /**
   * <p>
   * Throws an {@link BuildException} if the workspace directory or the project name is not set.
   * </p>
   */
  public final void requireWorkspaceAndProjectNameSet() {
    if (!isWorkspaceSet() || !isProjectNameSet()) {
      throw new BuildException("You have to specify the workspace and projectName attributes!");
    }
  }

  /**
   * <p>
   * Returns the associated eclipse project.
   * </p>
   * 
   * @return the associated eclipse project.
   * 
   * @throws BuildException
   *           if the eclipse project with the given name does not exist in the given workspace.
   */
  public EclipseProject getEclipseProject() throws BuildException {

    // get eclipse project if it is not already set
    if (this._eclipseProject == null) {
      if (getWorkspace().hasProject(this._projectName)) {
        this._eclipseProject = getWorkspace().getProject(this._projectName);
      } else {
        // TODO
        throw new BuildException("The specified project '" + this._projectName + "' does not exists in the workspace.");
      }
    }

    // return the eclipse project
    return this._eclipseProject;
  }

  /**
   * <p>
   * Ensures that the associated project has the specified project role.
   * </p>
   * 
   * @param projectRoleClass
   *          the project role class
   */
  public void ensureRole(Class<? extends ProjectRole> projectRoleClass) {
    if (!getEclipseProject().hasRole(projectRoleClass)) {

      // TODO
      final StringBuffer buffer = new StringBuffer();
      buffer.append("Project ");
      buffer.append(this._projectName);
      buffer.append(" must have role");
      buffer.append(projectRoleClass.getName());
      buffer.append("!");

      throw new BuildException(buffer.toString());
    }
  }
}