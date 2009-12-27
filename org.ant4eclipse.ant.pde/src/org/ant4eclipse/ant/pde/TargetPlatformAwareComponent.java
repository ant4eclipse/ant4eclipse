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

import org.ant4eclipse.lib.core.exception.Ant4EclipseException;

/**
 * <p>
 * Defines the common interface for all target platform aware components.
 * </p>
 * 
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 */
public interface TargetPlatformAwareComponent {

  /**
   * <p>
   * Sets the target platform against which the workspace plug-ins will be compiled and tested.
   * </p>
   * 
   * @param targetPlatformId
   *          the id of the target platform against which the workspace plug-ins will be compiled and tested.
   */
  void setTargetPlatformId(String targetPlatformId);

  /**
   * <p>
   * Returns whether the target platform location is set.
   * </p>
   * 
   * @return whether the target platform location is set.
   */
  boolean isTargetPlatformIdSet();

  /**
   * <p>
   * Returns the target platform id or <code>null</code>, if no target platform id has been set.
   * </p>
   * 
   * @return the target platform id or <code>null</code>, if no target platform id has been set.
   */
  String getTargetPlatformId();

  /**
   * <p>
   * Throws an {@link Ant4EclipseException} if the field '<code>_targetPlatformId</code>' is not set
   * </p>
   */
  void requireTargetPlatformIdSet();
}
