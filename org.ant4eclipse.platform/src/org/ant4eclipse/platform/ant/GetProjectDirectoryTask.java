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

import org.ant4eclipse.platform.ant.base.AbstractProjectBasedTask;
import org.apache.tools.ant.BuildException;

/**
 * <p>
 * Can be used to resolve the root diretory fo a given project.
 * </p>
 * 
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 */
public class GetProjectDirectoryTask extends AbstractProjectBasedTask {
  /** the pathId */
  private String _pathId   = null;

  /** the property */
  private String _property = null;

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
   * {@inheritDoc}
   */
  @Override
  public void doExecute() {

    requireWorkspaceAndProjectNameSet();
    requirePathIdOrPropertySet();

    final String path = getEclipseProject().getFolder().getAbsolutePath();

    if (isPathIdSet()) {
      getProject().addReference(getPathId(), path);
    }

    if (isPropertySet()) {
      getProject().setProperty(this._property, path);
    }
  }
}