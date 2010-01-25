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
package org.ant4eclipse.ant.pde;

import org.ant4eclipse.core.exception.Ant4EclipseException;

import org.ant4eclipse.pde.PdeExceptionCode;

/**
 * <p>
 * Default implementation of the interface {@link TargetPlatformAwareComponent}.
 * </p>
 * 
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 */
public class TargetPlatformAwareDelegate implements TargetPlatformAwareComponent {

  /** the target platform id */
  private String _targetPlatformId;

  /**
   * <p>
   * Creates a new instance of type {@link TargetPlatformAwareDelegate}.
   * </p>
   */
  public TargetPlatformAwareDelegate() {
    super();
  }

  /**
   * {@inheritDoc}
   */
  public final void setTargetPlatformId(String targetPlatformId) {
    this._targetPlatformId = targetPlatformId;
  }

  /**
   * {@inheritDoc}
   */
  public final boolean isTargetPlatformIdSet() {
    return this._targetPlatformId != null;
  }

  /**
   * {@inheritDoc}
   */
  public final String getTargetPlatformId() {
    return this._targetPlatformId;
  }

  /**
   * {@inheritDoc}
   */
  public final void requireTargetPlatformIdSet() {
    if (!isTargetPlatformIdSet()) {
      throw new Ant4EclipseException(PdeExceptionCode.ANT_ATTRIBUTE_NOT_SET, "targetPlatformId");
    }
  }
}
