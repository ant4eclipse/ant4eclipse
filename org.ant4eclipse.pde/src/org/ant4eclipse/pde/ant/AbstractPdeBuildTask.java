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
package org.ant4eclipse.pde.ant;

import org.ant4eclipse.core.exception.Ant4EclipseException;

import org.ant4eclipse.pde.PdeExceptionCode;

import org.ant4eclipse.platform.ant.core.task.AbstractProjectPathTask;

/**
 * <p>
 * Abstract base class for PDE build tasks.
 * </p>
 * 
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 */
public abstract class AbstractPdeBuildTask extends AbstractProjectPathTask {

  /** the target platform id */
  private String _targetPlatformId;

  /**
   * <p>
   * Creates a new instance of type AbstractPdeBuildTask.
   * </p>
   * 
   */
  public AbstractPdeBuildTask() {
    super();
  }

  /**
   * <p>
   * Sets the target platform against which the workspace plug-ins will be compiled and tested.
   * </p>
   * 
   * @param targetPlatformId
   *          the target platform against which the workspace plug-ins will be compiled and tested.
   */
  public final void setTargetPlatformId(final String targetPlatformId) {
    _targetPlatformId = targetPlatformId;
  }

  /**
   * <p>
   * Returns whether the target platform location is set.
   * </p>
   * 
   * @return whether the target platform location is set.
   */
  public final boolean isTargetPlatformId() {
    return this._targetPlatformId != null;
  }

  /**
   * <p>
   * Returns the id of the target platform.
   * </p>
   * 
   * @return id of the target platform.
   */
  public final String getTargetPlatformId() {
    return _targetPlatformId;
  }

  /**
   * <p>
   * Throws an {@link Ant4EclipseException} if the field '<code>_targetPlatformId</code>' is not set
   * </p>
   */
  public final void requireTargetPlatformIdSet() {
    if (!isTargetPlatformId()) {
      throw new Ant4EclipseException(PdeExceptionCode.ANT_ATTRIBUTE_NOT_SET, "targetPlatformId");
    }
  }
}
