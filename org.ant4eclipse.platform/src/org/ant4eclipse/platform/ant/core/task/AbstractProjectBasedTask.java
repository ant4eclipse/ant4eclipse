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
package org.ant4eclipse.platform.ant.core.task;

import java.io.File;

import org.ant4eclipse.core.ant.AbstractAnt4EclipseTask;
import org.ant4eclipse.platform.ant.core.EclipseProjectComponent;
import org.ant4eclipse.platform.ant.core.delegate.EclipseProjectDelegate;
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
public abstract class AbstractProjectBasedTask extends AbstractAnt4EclipseTask implements EclipseProjectComponent {

  /** the project delegate */
  private final EclipseProjectDelegate _eclipseProjectDelegate;

  /**
   * <p>
   * Creates a new instance of type AbstractProjectBasedTask.
   * </p>
   */
  public AbstractProjectBasedTask() {
    super();

    // create delegate
    this._eclipseProjectDelegate = new EclipseProjectDelegate(this);
  }

  /**
   * {@inheritDoc}
   */
  public final void ensureRole(Class<? extends ProjectRole> projectRoleClass) {
    this._eclipseProjectDelegate.ensureRole(projectRoleClass);
  }

  /**
   * {@inheritDoc}
   */
  public final EclipseProject getEclipseProject() throws BuildException {
    return this._eclipseProjectDelegate.getEclipseProject();
  }

  /**
   * {@inheritDoc}
   */
  public final Workspace getWorkspace() {
    return this._eclipseProjectDelegate.getWorkspace();
  }

  /**
   * {@inheritDoc}
   */
  public final File getWorkspaceDirectory() {
    return this._eclipseProjectDelegate.getWorkspaceDirectory();
  }

  /**
   * {@inheritDoc}
   */
  public final boolean isProjectNameSet() {
    return this._eclipseProjectDelegate.isProjectNameSet();
  }

  /**
   * {@inheritDoc}
   */
  public final boolean isWorkspaceDirectorySet() {
    return this._eclipseProjectDelegate.isWorkspaceDirectorySet();
  }

  /**
   * {@inheritDoc}
   */
  public final void requireWorkspaceAndProjectNameSet() {
    this._eclipseProjectDelegate.requireWorkspaceAndProjectNameSet();
  }

  /**
   * {@inheritDoc}
   */
  public final void requireWorkspaceDirectorySet() {
    this._eclipseProjectDelegate.requireWorkspaceDirectorySet();
  }

  /**
   * {@inheritDoc}
   */
  public final void setProjectName(String projectName) {
    this._eclipseProjectDelegate.setProjectName(projectName);
  }

  /**
   * {@inheritDoc}
   */
  @SuppressWarnings("deprecation")
  public final void setWorkspace(File workspace) {
    this._eclipseProjectDelegate.setWorkspace(workspace);
  }

  /**
   * {@inheritDoc}
   */
  public final void setWorkspaceDirectory(File workspaceDirectory) {
    this._eclipseProjectDelegate.setWorkspaceDirectory(workspaceDirectory);
  }

  /**
   * {@inheritDoc}
   */
  @Deprecated
  public final void setProject(File projectPath) {
    this._eclipseProjectDelegate.setProject(projectPath);
  }
}