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
package org.ant4eclipse.platform.ant.base;

import java.io.File;

import org.ant4eclipse.core.ant.AbstractAnt4EclipseTask;
import org.ant4eclipse.platform.ant.delegate.ProjectDelegate;
import org.ant4eclipse.platform.model.resource.EclipseProject;
import org.ant4eclipse.platform.model.resource.Workspace;
import org.ant4eclipse.platform.model.resource.role.ProjectRole;
import org.apache.tools.ant.BuildException;

/**
 * <p>
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

  public void ensureRole(Class<? extends ProjectRole> projectRoleClass) {
    this._projectBase.ensureRole(projectRoleClass);
  }

  public EclipseProject getEclipseProject() throws BuildException {
    return this._projectBase.getEclipseProject();
  }

  public final Workspace getWorkspace() {
    return this._projectBase.getWorkspace();
  }

  public final File getWorkspaceDirectory() {
    return this._projectBase.getWorkspaceDirectory();
  }

  public final boolean isProjectNameSet() {
    return this._projectBase.isProjectNameSet();
  }

  public final boolean isWorkspaceDirectorySet() {
    return this._projectBase.isWorkspaceSet();
  }

  public final void requireWorkspaceAndProjectNameSet() {
    this._projectBase.requireWorkspaceAndProjectNameSet();
  }

  public final void requireWorkspaceDirectorySet() {
    this._projectBase.requireWorkspaceSet();
  }

  public final void setProjectName(String projectName) {
    this._projectBase.setProjectName(projectName);
  }

  @SuppressWarnings("deprecation")
  public final void setWorkspace(File workspace) {
    this._projectBase.setWorkspace(workspace);
  }

  public final void setWorkspaceDirectory(File workspaceDirectory) {
    this._projectBase.setWorkspaceDirectory(workspaceDirectory);
  }

}