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
import org.ant4eclipse.platform.ant.delegate.GetProjectPathDelegate;
import org.ant4eclipse.platform.model.resource.EclipseProject;
import org.ant4eclipse.platform.model.resource.Workspace;
import org.ant4eclipse.platform.model.resource.role.ProjectRole;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.types.Path;

/**
 * <p>
 * Abstract base class for all tasks that resolve paths from an eclipse project.
 * </p>
 * 
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 */
public abstract class AbstractGetProjectPathTask extends AbstractAnt4EclipseTask {

  private final GetProjectPathDelegate _getProjectPathDelegate;

  public AbstractGetProjectPathTask() {
    super();
    this._getProjectPathDelegate = new GetProjectPathDelegate(this);
  }

  public final String getDirSeparator() {
    return this._getProjectPathDelegate.getDirSeparator();
  }

  public EclipseProject getEclipseProject() throws BuildException {
    return this._getProjectPathDelegate.getEclipseProject();
  }

  public final String getPathId() {
    return this._getProjectPathDelegate.getPathId();
  }

  public final String getPathSeparator() {
    return this._getProjectPathDelegate.getPathSeparator();
  }

  public final String getProperty() {
    return this._getProjectPathDelegate.getProperty();
  }

  public final File[] getResolvedPath() {
    return this._getProjectPathDelegate.getResolvedPath();
  }

  public final Workspace getWorkspace() {
    return this._getProjectPathDelegate.getWorkspace();
  }

  public final File getWorkspaceDirectory() {
    return this._getProjectPathDelegate.getWorkspaceDirectory();
  }

  public final boolean isDirSeparatorset() {
    return this._getProjectPathDelegate.isDirSeparatorset();
  }

  public final boolean isPathIdSet() {
    return this._getProjectPathDelegate.isPathIdSet();
  }

  public final boolean isPathSeparatorSet() {
    return this._getProjectPathDelegate.isPathSeparatorSet();
  }

  public final boolean isProjectNameSet() {
    return this._getProjectPathDelegate.isProjectNameSet();
  }

  public final boolean isPropertySet() {
    return this._getProjectPathDelegate.isPropertySet();
  }

  public final boolean isRelative() {
    return this._getProjectPathDelegate.isRelative();
  }

  public final boolean isWorkspaceSet() {
    return this._getProjectPathDelegate.isWorkspaceSet();
  }

  public final void populatePathId() {
    this._getProjectPathDelegate.populatePathId();
  }

  public final void populateProperty() {
    this._getProjectPathDelegate.populateProperty();
  }

  public final void requirePathIdOrPropertySet() {
    this._getProjectPathDelegate.requirePathIdOrPropertySet();
  }

  public final void requireWorkspaceAndProjectNameSet() {
    this._getProjectPathDelegate.requireWorkspaceAndProjectNameSet();
  }

  public final void requireWorkspaceSet() {
    this._getProjectPathDelegate.requireWorkspaceSet();
  }

  public final void setDirSeparator(String newdirseparator) {
    this._getProjectPathDelegate.setDirSeparator(newdirseparator);
  }

  public final void setPathId(String id) {
    this._getProjectPathDelegate.setPathId(id);
  }

  public final void setPathSeparator(String newpathseparator) {
    this._getProjectPathDelegate.setPathSeparator(newpathseparator);
  }

  public final void setProjectName(String projectName) {
    this._getProjectPathDelegate.setProjectName(projectName);
  }

  public final void setProperty(String property) {
    this._getProjectPathDelegate.setProperty(property);
  }

  public final void setRelative(boolean relative) {
    this._getProjectPathDelegate.setRelative(relative);
  }

  public final void setResolvedPath(File[] resolvedPath) {
    this._getProjectPathDelegate.setResolvedPath(resolvedPath);
  }

  public final void setWorkspace(File workspace) {
    this._getProjectPathDelegate.setWorkspace(workspace);
  }

  public final void setWorkspaceDirectory(File workspaceDirectory) {
    this._getProjectPathDelegate.setWorkspaceDirectory(workspaceDirectory);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void doExecute() {

    requireWorkspaceAndProjectNameSet();
    requirePathIdOrPropertySet();

    final File[] resolvedPath = resolvePath();
    setResolvedPath(resolvedPath);

    if (isPathIdSet()) {
      populatePathId();
    }

    if (isPropertySet()) {
      populateProperty();
    }
  }

  protected final Path convertToPath(File[] entries) {
    return this._getProjectPathDelegate.convertToPath(entries);
  }

  protected final String convertToString(File[] entries) {
    return this._getProjectPathDelegate.convertToString(entries);
  }

  protected final void ensureRole(Class<? extends ProjectRole> projectRoleClass) {
    this._getProjectPathDelegate.ensureRole(projectRoleClass);
  }

  /**
   * <p>
   * Resolves the current path.
   * </p>
   * 
   * @return A list of resolved pathes.
   * 
   * @throws Exception
   *           The resolving process failed for some reason.
   */
  protected abstract File[] resolvePath();
}
