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
import org.ant4eclipse.platform.ant.core.GetPathComponent;
import org.ant4eclipse.platform.ant.core.delegate.EclipseProjectDelegate;
import org.ant4eclipse.platform.ant.core.delegate.GetPathDelegate;
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
public abstract class AbstractGetProjectPathTask extends AbstractAnt4EclipseTask implements GetPathComponent,
    EclipseProjectComponent {

  private final EclipseProjectDelegate _projectDelegate;

  private final GetPathDelegate        _getPathDelegate;

  public AbstractGetProjectPathTask() {
    super();
    this._projectDelegate = new EclipseProjectDelegate(this);
    this._getPathDelegate = new GetPathDelegate(this);
  }

  public final String getDirSeparator() {
    return this._getPathDelegate.getDirSeparator();
  }

  public EclipseProject getEclipseProject() throws BuildException {
    return this._projectDelegate.getEclipseProject();
  }

  public final String getPathId() {
    return this._getPathDelegate.getPathId();
  }

  public final String getPathSeparator() {
    return this._getPathDelegate.getPathSeparator();
  }

  public final String getProperty() {
    return this._getPathDelegate.getProperty();
  }

  public final File[] getResolvedPath() {
    return this._getPathDelegate.getResolvedPath();
  }

  public final Workspace getWorkspace() {
    return this._projectDelegate.getWorkspace();
  }

  public final File getWorkspaceDirectory() {
    return this._projectDelegate.getWorkspaceDirectory();
  }

  public final boolean isDirSeparatorSet() {
    return this._getPathDelegate.isDirSeparatorSet();
  }

  public final boolean isPathIdSet() {
    return this._getPathDelegate.isPathIdSet();
  }

  public final boolean isPathSeparatorSet() {
    return this._getPathDelegate.isPathSeparatorSet();
  }

  public final boolean isProjectNameSet() {
    return this._projectDelegate.isProjectNameSet();
  }

  public final boolean isPropertySet() {
    return this._getPathDelegate.isPropertySet();
  }

  public final boolean isRelative() {
    return this._getPathDelegate.isRelative();
  }

  public final boolean isWorkspaceSet() {
    return this._projectDelegate.isWorkspaceSet();
  }

  public final void populatePathId() {
    this._getPathDelegate.populatePathId();
  }

  public final void populateProperty() {
    this._getPathDelegate.populateProperty();
  }

  public final void requirePathIdOrPropertySet() {
    this._getPathDelegate.requirePathIdOrPropertySet();
  }

  public final void requireWorkspaceAndProjectNameSet() {
    this._projectDelegate.requireWorkspaceAndProjectNameSet();
  }

  public final void requireWorkspaceSet() {
    this._projectDelegate.requireWorkspaceSet();
  }

  public final void setDirSeparator(String newdirseparator) {
    this._getPathDelegate.setDirSeparator(newdirseparator);
  }

  public final void setPathId(String id) {
    this._getPathDelegate.setPathId(id);
  }

  public final void setPathSeparator(String newpathseparator) {
    this._getPathDelegate.setPathSeparator(newpathseparator);
  }

  public final void setProjectName(String projectName) {
    this._projectDelegate.setProjectName(projectName);
  }

  public final void setProperty(String property) {
    this._getPathDelegate.setProperty(property);
  }

  public final void setRelative(boolean relative) {
    this._getPathDelegate.setRelative(relative);
  }

  public final void setResolvedPath(File[] resolvedPath) {
    this._getPathDelegate.setResolvedPath(resolvedPath);
  }

  @SuppressWarnings("deprecation")
  public final void setWorkspace(File workspace) {
    this._projectDelegate.setWorkspace(workspace);
  }

  public final void setWorkspaceDirectory(File workspaceDirectory) {
    this._projectDelegate.setWorkspaceDirectory(workspaceDirectory);
  }

  public final Path convertToPath(File entry) {
    return this._getPathDelegate.convertToPath(entry);
  }

  public final String convertToString(File entry) {
    return this._getPathDelegate.convertToString(entry);
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

  public final Path convertToPath(File[] entries) {
    return this._getPathDelegate.convertToPath(entries);
  }

  public final String convertToString(File[] entries) {
    return this._getPathDelegate.convertToString(entries);
  }

  public final void ensureRole(Class<? extends ProjectRole> projectRoleClass) {
    this._projectDelegate.ensureRole(projectRoleClass);
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
