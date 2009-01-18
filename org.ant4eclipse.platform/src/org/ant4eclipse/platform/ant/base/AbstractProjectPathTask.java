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
import org.ant4eclipse.platform.ant.delegate.ProjectPathDelegate;
import org.ant4eclipse.platform.model.resource.EclipseProject;
import org.ant4eclipse.platform.model.resource.Workspace;
import org.ant4eclipse.platform.model.resource.role.ProjectRole;
import org.apache.tools.ant.types.Path;

/**
 * <p>
 * Abstract base class for all tasks that resolve paths from an eclipse project.
 * </p>
 * 
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 */
public abstract class AbstractProjectPathTask extends AbstractAnt4EclipseTask {

  private final ProjectPathDelegate _projectPathDelegate;

  public AbstractProjectPathTask() {
    super();

    this._projectPathDelegate = new ProjectPathDelegate(this);
  }

  public final String getDirSeparator() {
    return this._projectPathDelegate.getDirSeparator();
  }

  public final String getPathSeparator() {
    return this._projectPathDelegate.getPathSeparator();
  }

  public final File getWorkspaceDirectory() {
    return this._projectPathDelegate.getWorkspaceDirectory();
  }

  public final boolean isDirSeparatorset() {
    return this._projectPathDelegate.isDirSeparatorset();
  }

  public final boolean isPathSeparatorSet() {
    return this._projectPathDelegate.isPathSeparatorSet();
  }

  public final boolean isProjectNameSet() {
    return this._projectPathDelegate.isProjectNameSet();
  }

  public final boolean isWorkspaceSet() {
    return this._projectPathDelegate.isWorkspaceSet();
  }

  public final void requireWorkspaceAndProjectNameSet() {
    this._projectPathDelegate.requireWorkspaceAndProjectNameSet();
  }

  public final void requireWorkspaceSet() {
    this._projectPathDelegate.requireWorkspaceSet();
  }

  public final void setDirSeparator(String newdirseparator) {
    this._projectPathDelegate.setDirSeparator(newdirseparator);
  }

  public final void setPathSeparator(String newpathseparator) {
    this._projectPathDelegate.setPathSeparator(newpathseparator);
  }

  public final void setProjectName(String projectName) {
    this._projectPathDelegate.setProjectName(projectName);
  }

  public final void setWorkspace(File workspace) {
    this._projectPathDelegate.setWorkspace(workspace);
  }

  public final void setWorkspaceDirectory(File workspaceDirectory) {
    this._projectPathDelegate.setWorkspaceDirectory(workspaceDirectory);
  }

  /**
   * @return
   */
  protected final Workspace getWorkspace() {
    return this._projectPathDelegate.getWorkspace();
  }

  /**
   * @return
   */
  protected final EclipseProject getEclipseProject() {
    return this._projectPathDelegate.getEclipseProject();
  }

  protected final Path convertToPath(File[] entries) {
    return this._projectPathDelegate.convertToPath(entries);
  }

  protected final String convertToString(File[] entries) {
    return this._projectPathDelegate.convertToString(entries);
  }

  protected final Path convertToPath(File entry) {
    return this._projectPathDelegate.convertToPath(entry);
  }

  protected final String convertToString(File entry) {
    return this._projectPathDelegate.convertToString(entry);
  }

  protected void ensureRole(Class<? extends ProjectRole> projectRoleClass) {
    this._projectPathDelegate.ensureRole(projectRoleClass);
  }

}
