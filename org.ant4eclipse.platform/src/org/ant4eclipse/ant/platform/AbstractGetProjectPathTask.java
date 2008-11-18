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
package org.ant4eclipse.ant.platform;

import java.io.File;

import net.sf.ant4eclipse.ant.TaskHelper;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.types.Path;

/**
 * <p>
 * Abstract base class for all tasks that resolve paths from an eclipse project.
 * </p>
 * 
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 */
public abstract class AbstractGetProjectPathTask extends AbstractProjectBasedTask {
  /** the pathId */
  private String  _pathId   = null;

  /** the property */
  private String  _property = null;

  /** indicates whether the class path should be resolved relative or not */
  private boolean _relative = false;

  /** the resolved path entries */
  private File[]  _resolvedPath;

  /**
   * <p>
   * Sets the path ID. The resolved path can be references via this ID in the ant build file.
   * </p>
   * 
   * @param id
   *          the path ID
   */
  public final void setPathId(final String id) {
    if (this._pathId == null) {
      this._pathId = id;
    }
  }

  /**
   * <p>
   * Returns the path ID for this task.
   * </p>
   * 
   * @return The path ID for this task.
   */
  public final String getPathId() {
    return this._pathId;
  }

  /**
   * <p>
   * Returns true if the path ID has been set.
   * </p>
   * 
   * @return true <=> The path ID has been set.
   */
  protected final boolean isPathIdSet() {
    return this._pathId != null;
  }

  /**
   * <p>
   * Returns whether or not the path should be resolved relative to the workspace.
   * </p>
   * 
   * @return <code>true</code> if the path should be resolved relative to the workspace.
   */
  public final boolean isRelative() {
    return this._relative;
  }

  /**
   * <p>
   * Sets whether the path should be resolved relative to the workspace.
   * </p>
   * 
   * @param relative
   *          whether the path should be resolved relative to the workspace.
   */
  public final void setRelative(final boolean relative) {
    this._relative = relative;
  }

  /**
   * <p>
   * Sets the name of the property that should hold the resolved path.
   * </p>
   * 
   * @param property
   *          the name of the property that should hold the resolved path.
   */
  public final void setProperty(final String property) {
    this._property = property;
  }

  /**
   * <p>
   * Returns the name of the property that should hold the resolved path.
   * <p>
   * 
   * @return the name of the property that should hold the resolved path.
   */
  public final String getProperty() {
    return this._property;
  }

  /**
   * <p>
   * Returns true if the name of the property has been set.
   * </p>
   * 
   * @return <code>true</code> if the name of the property has been set.
   */
  protected final boolean isPropertySet() {
    return this._property != null;
  }

  /**
   * <p>
   * Sets the path separator.
   * </p>
   * 
   * @param pathSeparator
   *          the path separator.
   */
  public final void setPathSeparator(final String pathSeparator) {
    getProjectBase().setPathSeparator(pathSeparator);
  }

  /**
   * <p>
   * Returns <code>true</code> if the path separator has been set.
   * </p>
   * 
   * @return <code>true</code> if the path separator has been set.
   */
  protected final boolean isPathSeparatorSet() {
    return getProjectBase().getPathSeparator() != null;
  }

  /**
   * <p>
   * Ensures that either a path id or a property (or both) is set. If none of both is set, a BuildException will be
   * thrown.
   * </p>
   */
  protected final void requirePathIdOrPropertySet() {
    if (!isPathIdSet() && !isPropertySet()) {
      throw new BuildException("At least one of 'pathId' or 'property' has to be set!");
    }
  }

  /**
   * <p>
   * Returns the a list of resolved pathes.
   * </p>
   * 
   * @return A list of resolved pathes.
   */
  protected final File[] getResolvedPath() {
    return this._resolvedPath;
  }

  /**
   * <p>
   * Sets the resolved path entries.
   * </p>
   * 
   * @param resolvedPath
   *          the resolved path entries.
   */
  protected final void setResolvedPath(final File[] resolvedPath) {
    this._resolvedPath = resolvedPath;
  }

  /**
   * {@inheritDoc}
   */
  public void execute() throws BuildException {

    requireWorkspaceAndProjectNameOrProjectSet();
    requirePathIdOrPropertySet();

    try {
      final File[] resolvedPath = resolvePath();
      setResolvedPath(resolvedPath);
    } catch (final BuildException ex) {
      throw ex;
    } catch (final Exception e) {
      throw new BuildException(e.getMessage(), e);
    }

    if (isPathIdSet()) {
      populatePathId();
    }

    if (isPropertySet()) {
      populateProperty();
    }
  }

  /**
   * <p>
   * Populates the property if specified.
   * </p>
   */
  protected void populateProperty() {
    getProjectBase().setPathProperty(getProperty(), getResolvedPath());
  }

  /**
   * <p>
   * Populates the path id if specified.
   * </p>
   */
  protected void populatePathId() {
    final Path resolvedPath = TaskHelper.convertToPath(getResolvedPath(), getProject());
    getProject().addReference(getPathId(), resolvedPath);
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
  protected abstract File[] resolvePath() throws Exception;
}
