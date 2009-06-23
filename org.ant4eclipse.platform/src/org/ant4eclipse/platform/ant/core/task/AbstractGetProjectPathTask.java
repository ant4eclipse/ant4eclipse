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

  /** the project delegate */
  private final EclipseProjectDelegate _projectDelegate;

  /** the get path delegate */
  private final GetPathDelegate        _getPathDelegate;

  /**
   * <p>
   * Create a new instance of type {@link AbstractGetProjectPathTask}.
   * </p>
   */
  public AbstractGetProjectPathTask() {
    super();

    // create the delegates
    this._projectDelegate = new EclipseProjectDelegate(this);
    this._getPathDelegate = new GetPathDelegate(this);
  }

  /**
   * <p>
   * Resolves the current path.
   * </p>
   * 
   * @return A list of resolved pathes.
   */
  protected abstract File[] resolvePath();

  /**
   * {@inheritDoc}
   */
  @Override
  public void doExecute() {

    // check requires attributes
    requireWorkspaceAndProjectNameSet();
    requirePathIdOrPropertySet();

    // resolve path
    final File[] resolvedPath = resolvePath();
    setResolvedPath(resolvedPath);

    // set path
    if (isPathIdSet()) {
      populatePathId();
    }

    // set property
    if (isPropertySet()) {
      populateProperty();
    }
  }

  /**
   * {@inheritDoc}
   */
  public final String getDirSeparator() {
    return this._getPathDelegate.getDirSeparator();
  }

  /**
   * {@inheritDoc}
   */
  public EclipseProject getEclipseProject() throws BuildException {
    return this._projectDelegate.getEclipseProject();
  }

  /**
   * {@inheritDoc}
   */
  public final String getPathId() {
    return this._getPathDelegate.getPathId();
  }

  /**
   * {@inheritDoc}
   */
  public final String getPathSeparator() {
    return this._getPathDelegate.getPathSeparator();
  }

  /**
   * {@inheritDoc}
   */
  public final String getProperty() {
    return this._getPathDelegate.getProperty();
  }

  /**
   * {@inheritDoc}
   */
  public final File[] getResolvedPath() {
    return this._getPathDelegate.getResolvedPath();
  }

  /**
   * {@inheritDoc}
   */
  public final Workspace getWorkspace() {
    return this._projectDelegate.getWorkspace();
  }

  /**
   * {@inheritDoc}
   */
  public final File getWorkspaceDirectory() {
    return this._projectDelegate.getWorkspaceDirectory();
  }

  /**
   * {@inheritDoc}
   */
  public final boolean isDirSeparatorSet() {
    return this._getPathDelegate.isDirSeparatorSet();
  }

  /**
   * {@inheritDoc}
   */
  public final boolean isPathIdSet() {
    return this._getPathDelegate.isPathIdSet();
  }

  /**
   * {@inheritDoc}
   */
  public final boolean isPathSeparatorSet() {
    return this._getPathDelegate.isPathSeparatorSet();
  }

  /**
   * {@inheritDoc}
   */
  public final boolean isProjectNameSet() {
    return this._projectDelegate.isProjectNameSet();
  }

  /**
   * {@inheritDoc}
   */
  public final boolean isPropertySet() {
    return this._getPathDelegate.isPropertySet();
  }

  /**
   * {@inheritDoc}
   */
  public final boolean isRelative() {
    return this._getPathDelegate.isRelative();
  }

  /**
   * {@inheritDoc}
   */
  public final boolean isWorkspaceDirectorySet() {
    return this._projectDelegate.isWorkspaceDirectorySet();
  }

  /**
   * {@inheritDoc}
   */
  public final void populatePathId() {
    this._getPathDelegate.populatePathId();
  }

  /**
   * {@inheritDoc}
   */
  public final void populateProperty() {
    this._getPathDelegate.populateProperty();
  }

  /**
   * {@inheritDoc}
   */
  public final void requirePathIdOrPropertySet() {
    this._getPathDelegate.requirePathIdOrPropertySet();
  }

  /**
   * {@inheritDoc}
   */
  public final void requireWorkspaceAndProjectNameSet() {
    this._projectDelegate.requireWorkspaceAndProjectNameSet();
  }

  /**
   * {@inheritDoc}
   */
  public final void requireWorkspaceDirectorySet() {
    this._projectDelegate.requireWorkspaceDirectorySet();
  }

  /**
   * {@inheritDoc}
   */
  public final void setDirSeparator(String newdirseparator) {
    this._getPathDelegate.setDirSeparator(newdirseparator);
  }

  /**
   * {@inheritDoc}
   */
  public final void setPathId(String id) {
    this._getPathDelegate.setPathId(id);
  }

  /**
   * {@inheritDoc}
   */
  public final void setPathSeparator(String newpathseparator) {
    this._getPathDelegate.setPathSeparator(newpathseparator);
  }

  /**
   * {@inheritDoc}
   */
  public final void setProjectName(String projectName) {
    this._projectDelegate.setProjectName(projectName);
  }

  /**
   * {@inheritDoc}
   */
  public void setProject(File projectPath) {
    this._projectDelegate.setProject(projectPath);
  }

  /**
   * {@inheritDoc}
   */
  public final void setProperty(String property) {
    this._getPathDelegate.setProperty(property);
  }

  /**
   * {@inheritDoc}
   */
  public final void setRelative(boolean relative) {
    this._getPathDelegate.setRelative(relative);
  }

  /**
   * {@inheritDoc}
   */
  public final void setResolvedPath(File[] resolvedPath) {
    this._getPathDelegate.setResolvedPath(resolvedPath);
  }

  /**
   * {@inheritDoc}
   */
  @SuppressWarnings("deprecation")
  public final void setWorkspace(File workspace) {
    this._projectDelegate.setWorkspace(workspace);
  }

  /**
   * {@inheritDoc}
   */
  public final void setWorkspaceDirectory(File workspaceDirectory) {
    this._projectDelegate.setWorkspaceDirectory(workspaceDirectory);
  }

  /**
   * {@inheritDoc}
   */
  public final Path convertToPath(File entry) {
    return this._getPathDelegate.convertToPath(entry);
  }

  /**
   * {@inheritDoc}
   */
  public final String convertToString(File entry) {
    return this._getPathDelegate.convertToString(entry);
  }

  /**
   * {@inheritDoc}
   */
  public final Path convertToPath(File[] entries) {
    return this._getPathDelegate.convertToPath(entries);
  }

  /**
   * {@inheritDoc}
   */
  public final String convertToString(File[] entries) {
    return this._getPathDelegate.convertToString(entries);
  }

  /**
   * {@inheritDoc}
   */
  public final void ensureRole(Class<? extends ProjectRole> projectRoleClass) {
    this._projectDelegate.ensureRole(projectRoleClass);
  }
}
