/**********************************************************************
 * Copyright (c) 2005-2009 ant4eclipse project team.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Nils Hartmann, Daniel Kasmeroglu, Gerd Wuetherich
 **********************************************************************/
package org.ant4eclipse.ant.platform.ant.core;

import java.io.File;

/**
 * <p>
 * Delegate class for all tasks, types and conditions that deal with project pathes.
 * </p>
 * 
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 */
public interface GetPathComponent extends PathComponent {

  /**
   * <p>
   * Sets the path ID. The resolved path can be references via this ID in the ant build file.
   * </p>
   * 
   * @param id
   *          the path ID
   */
  void setPathId(String id);

  /**
   * <p>
   * Returns the path ID for this task.
   * </p>
   * 
   * @return The path ID for this task.
   */
  String getPathId();

  /**
   * <p>
   * Returns true if the path ID has been set.
   * </p>
   * 
   * @return true <=> The path ID has been set.
   */
  boolean isPathIdSet();

  /**
   * <p>
   * Returns whether or not the path should be resolved relative to the workspace.
   * </p>
   * 
   * @return <code>true</code> if the path should be resolved relative to the workspace.
   */
  boolean isRelative();

  /**
   * <p>
   * Sets whether the path should be resolved relative to the workspace.
   * </p>
   * 
   * @param relative
   *          whether the path should be resolved relative to the workspace.
   */
  void setRelative(boolean relative);

  /**
   * <p>
   * Sets the name of the property that should hold the resolved path.
   * </p>
   * 
   * @param property
   *          the name of the property that should hold the resolved path.
   */
  void setProperty(String property);

  /**
   * <p>
   * Returns the name of the property that should hold the resolved path.
   * <p>
   * 
   * @return the name of the property that should hold the resolved path.
   */
  String getProperty();

  /**
   * <p>
   * Returns true if the name of the property has been set.
   * </p>
   * 
   * @return <code>true</code> if the name of the property has been set.
   */
  boolean isPropertySet();

  /**
   * <p>
   * Ensures that either a path id or a property (or both) is set. If none of both is set, a BuildException will be
   * thrown.
   * </p>
   */
  void requirePathIdOrPropertySet();

  /**
   * <p>
   * Returns the a list of resolved pathes.
   * </p>
   * 
   * @return A list of resolved pathes.
   */
  File[] getResolvedPath();

  /**
   * <p>
   * Sets the resolved path entries.
   * </p>
   * 
   * @param resolvedPath
   *          the resolved path entries.
   */
  void setResolvedPath(File[] resolvedPath);

  /**
   * <p>
   * Populates the property if specified.
   * </p>
   */
  void populateProperty();

  /**
   * <p>
   * Populates the path id if specified.
   * </p>
   */
  void populatePathId();
}
