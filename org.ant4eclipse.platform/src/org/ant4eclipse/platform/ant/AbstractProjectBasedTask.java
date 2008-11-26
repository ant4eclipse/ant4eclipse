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
package org.ant4eclipse.platform.ant;

import java.io.File;

import org.ant4eclipse.core.ant.AbstractAnt4EclipseTask;
import org.ant4eclipse.platform.ant.delegate.ProjectDelegate;
import org.ant4eclipse.platform.model.resource.EclipseProject;
import org.ant4eclipse.platform.model.resource.Workspace;
import org.ant4eclipse.platform.model.resource.role.ProjectRole;
import org.apache.tools.ant.BuildException;

/**
 * <p>
 * Abstract base class for all project based tasks.
 * </p>
 * 
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 */
public abstract class AbstractProjectBasedTask extends AbstractAnt4EclipseTask {

  /** Comment for <code>_projectSetBase</code> */
  private final ProjectDelegate _projectBase;

  /**
   * <p>
   * Creates a new instance of type AbstractProjectBasedTask.
   * </p>
   */
  public AbstractProjectBasedTask() {
    super();
    this._projectBase = new ProjectDelegate(this);
  }

  /**
   * Sets a reference to the property set which allows to resolve Eclipse variables using an ANT property set.
   * 
   * @param ref
   *          Name of the property set that will be used.
   */
  public void setVariablesRef(final String ref) {
    this._projectBase.setVariablesRef(ref);
  }

  /**
   * Returns the ProjectBase which allows to do project related configurations.
   * 
   * @return The ProjectBase instance.
   */
  protected ProjectDelegate getProjectDelegate() {
    return (this._projectBase);
  }

  public void ensureRole(Class<? extends ProjectRole> projectRoleClass) {
    if (!getEclipseProject().hasRole(projectRoleClass)) {
      final StringBuffer buffer = new StringBuffer();
      buffer.append("Project ");
      buffer.append(getProject().getName());
      buffer.append(" must have role");
      buffer.append(projectRoleClass.getName());
      buffer.append("!");

      throw new BuildException(buffer.toString());
    }
  }

  /**
   * <p>
   * Returns the EclipseProject instance associated with this task.
   * </p>
   * 
   * @return The EclipseProject instance associated with this task.
   * 
   * @see ProjectDelegate#getEclipseProject()
   */
  public EclipseProject getEclipseProject() {
    return this._projectBase.getEclipseProject();
  }

  /**
   * <p>
   * Sets the name of the project.
   * </p>
   * 
   * @param projectName
   *          the name of the project.
   * 
   * @see ProjectDelegate#getEclipseProject()
   */
  public final void setProjectName(final String projectName) {
    this._projectBase.setProjectName(projectName);
  }

  /**
   * <p>
   * Returns true if the project name has been set.
   * </p>
   * 
   * @return <code>true</code> if the project name has been set.
   * 
   * @see ProjectDelegate#getEclipseProject()
   */
  public final boolean isProjectNameSet() {
    return this._projectBase.isProjectNameSet();
  }

  /**
   * <p>
   * Sets the project.
   * </p>
   * 
   * @param projectDirectory
   *          the project
   * 
   * @see ProjectDelegate#getEclipseProject()
   */
  public final void setProject(final File projectDirectory) {
    this._projectBase.setProject(projectDirectory);
  }

  /**
   * <p>
   * Returns true if the project has been set.
   * </p>
   * 
   * @return <code>true</code> if the project has been set.
   * 
   * @see ProjectDelegate#getEclipseProject()
   */
  public final boolean isProjectSet() {
    return this._projectBase.isProjectSet();
  }

  /**
   * <p>
   * Returns the currently associated workspace .
   * </p>
   * 
   * @return currently associated workspace with this task.
   * 
   * @see ProjectDelegate#getEclipseProject()
   */
  public final Workspace getWorkspace() {
    return this._projectBase.getWorkspace();
  }

  /**
   * <p>
   * Returns whether a workspace has been set to this task.
   * </p>
   * 
   * @return whether a workspace has been set.
   * 
   * @see ProjectDelegate#getEclipseProject()
   */
  public final boolean isWorkspaceSet() {
    return this._projectBase.isWorkspaceSet();
  }

  /**
   * <p>
   * Requires that either the workspace and a project name <b>or</b> a project directory has been set.
   * </p>
   * 
   * @see ProjectDelegate#getEclipseProject()
   */
  public final void requireWorkspaceAndProjectNameOrProjectSet() {
    this._projectBase.requireWorkspaceAndProjectNameOrProjectSet();
  }

  /**
   * <p>
   * Requires that the workspace has been set.
   * </p>
   */
  public final void requireWorkspaceSet() {
    this._projectBase.requireWorkspaceSet();
  }

  /**
   * Sets the workspace for this task
   * 
   * @param workspace
   *          Path to workspace that should be associated with this Task
   * 
   * @see ProjectDelegate#getEclipseProject()
   * @deprecated
   */
  public final void setWorkspace(final File workspace) {
    this._projectBase.setWorkspace(workspace);
  }
  //
  // /**
  // * <p>
  // * Enables/disables the initial workspace initialisation.
  // * </p>
  // *
  // * @param enable
  // * <code>true</code> if the workspace should be initialised.
  // *
  // * @see ProjectBase#getEclipseProject()
  // */
  // public void setInitialiseWorkspace(final boolean enable) {
  // this._projectBase.setInitialiseWorkspace(enable);
  // }
}