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
package org.ant4eclipse.platform.ant.core.condition;

import java.io.File;

import org.ant4eclipse.core.ant.AbstractAnt4EclipseCondition;
import org.ant4eclipse.platform.ant.core.EclipseProjectComponent;
import org.ant4eclipse.platform.ant.core.WorkspaceComponent;
import org.ant4eclipse.platform.ant.core.delegate.EclipseProjectDelegate;
import org.ant4eclipse.platform.model.resource.EclipseProject;
import org.ant4eclipse.platform.model.resource.Workspace;
import org.ant4eclipse.platform.model.resource.role.ProjectRole;
import org.apache.tools.ant.BuildException;

/**
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 */
public abstract class AbstractProjectBasedCondition extends AbstractAnt4EclipseCondition implements WorkspaceComponent,
    EclipseProjectComponent {

  /** the project delegate */
  private final EclipseProjectDelegate _projectDelegate;

  /**
   * Creates a new instance of type {@link AbstractProjectBasedCondition}.
   */
  public AbstractProjectBasedCondition() {
    this._projectDelegate = new EclipseProjectDelegate(this);
  }

  /**
   * Sets the name of the project.
   * 
   * @param project
   *          the name of the project.
   */
  public void setProjectName(final String project) {
    this._projectDelegate.setProjectName(project);
  }

  /**
   * Sets the workspace.
   * 
   * @param workspace
   *          the workspace.
   * @deprecated
   */
  @Deprecated
  public void setWorkspace(final File workspace) {
    this._projectDelegate.setWorkspaceDirectory(workspace);
  }

  public final void setWorkspaceDirectory(File workspaceDirectory) {
    this._projectDelegate.setWorkspaceDirectory(workspaceDirectory);
  }

  /**
   * @param projectRoleClass
   * @see org.ant4eclipse.platform.ant.core.delegate.EclipseProjectDelegate#ensureRole(java.lang.Class)
   */
  public void ensureRole(Class<? extends ProjectRole> projectRoleClass) {
    this._projectDelegate.ensureRole(projectRoleClass);
  }

  /**
   * @return
   * @throws BuildException
   * @see org.ant4eclipse.platform.ant.core.delegate.EclipseProjectDelegate#getEclipseProject()
   */
  public EclipseProject getEclipseProject() throws BuildException {
    return this._projectDelegate.getEclipseProject();
  }

  /**
   * @return
   * @see org.ant4eclipse.platform.ant.core.delegate.WorkspaceDelegate#getWorkspace()
   */
  public final Workspace getWorkspace() {
    return this._projectDelegate.getWorkspace();
  }

  /**
   * @return
   * @see org.ant4eclipse.platform.ant.core.delegate.WorkspaceDelegate#getWorkspaceDirectory()
   */
  public final File getWorkspaceDirectory() {
    return this._projectDelegate.getWorkspaceDirectory();
  }

  /**
   * @return
   * @see org.ant4eclipse.platform.ant.core.delegate.EclipseProjectDelegate#isProjectNameSet()
   */
  public final boolean isProjectNameSet() {
    return this._projectDelegate.isProjectNameSet();
  }

  /**
   * @return
   * @see org.ant4eclipse.platform.ant.core.delegate.WorkspaceDelegate#isWorkspaceDirectorySet()
   */
  public final boolean isWorkspaceDirectorySet() {
    return this._projectDelegate.isWorkspaceDirectorySet();
  }

  /**
   * 
   * @see org.ant4eclipse.platform.ant.core.delegate.EclipseProjectDelegate#requireWorkspaceAndProjectNameSet()
   */
  public final void requireWorkspaceAndProjectNameSet() {
    this._projectDelegate.requireWorkspaceAndProjectNameSet();
  }

  /**
   * 
   * @see org.ant4eclipse.platform.ant.core.delegate.WorkspaceDelegate#requireWorkspaceDirectorySet()
   */
  public final void requireWorkspaceDirectorySet() {
    this._projectDelegate.requireWorkspaceDirectorySet();
  }

  /**
   * @param projectPath
   * @see org.ant4eclipse.platform.ant.core.delegate.EclipseProjectDelegate#setProject(java.io.File)
   */
  public void setProject(File projectPath) {
    this._projectDelegate.setProject(projectPath);
  }

}