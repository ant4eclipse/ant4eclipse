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
import org.ant4eclipse.lib.core.service.ServiceRegistryAccess;
import org.ant4eclipse.lib.pde.PdeExceptionCode;
import org.ant4eclipse.lib.pde.tools.PlatformConfiguration;
import org.ant4eclipse.lib.pde.tools.TargetPlatform;
import org.ant4eclipse.lib.pde.tools.TargetPlatformRegistry;
import org.ant4eclipse.lib.platform.model.resource.Workspace;

/**
 * <p>
 * Default implementation of the interface {@link TargetPlatformAwareComponent}.
 * </p>
 * 
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 */
public class TargetPlatformAwareDelegate implements TargetPlatformAwareComponent {

  /** the target platform id */
  private String         _targetPlatformId;

  /** the platform configuration id */
  private String         _platformConfigurationId;

  /** - */
  private TargetPlatform _targetPlatform;

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
  @Override
  public final void setTargetPlatformId(String targetPlatformId) {
    this._targetPlatformId = targetPlatformId;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public final boolean isTargetPlatformIdSet() {
    return this._targetPlatformId != null;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public final String getTargetPlatformId() {
    return this._targetPlatformId;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public final void requireTargetPlatformIdSet() {
    if (!isTargetPlatformIdSet()) {
      throw new Ant4EclipseException(PdeExceptionCode.ANT_ATTRIBUTE_NOT_SET, "targetPlatformId");
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getPlatformConfigurationId() {
    return this._platformConfigurationId;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isPlatformConfigurationIdSet() {
    return this._platformConfigurationId != null;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setPlatformConfigurationId(String platformConfigurationId) {
    this._platformConfigurationId = platformConfigurationId;
  }

  /**
   * <p>
   * Returns the target platform.
   * </p>
   * 
   * @param workspace
   *          the workspace
   * @return the target platform.
   */
  public TargetPlatform getTargetPlatform(Workspace workspace) {

    // create the target platform if necessary
    if (this._targetPlatform == null) {

      // get the target platform registry
      TargetPlatformRegistry targetPlatformRegistry = ServiceRegistryAccess.instance().getService(
          TargetPlatformRegistry.class);

      // define the platform configuration
      PlatformConfiguration configuration = null;
      if (isPlatformConfigurationIdSet()
          && targetPlatformRegistry.hasPlatformConfiguration(getPlatformConfigurationId())) {
        configuration = targetPlatformRegistry.getPlatformConfiguration(getPlatformConfigurationId());
      } else {
        configuration = new PlatformConfiguration();
        configuration.setPreferProjects(true);
      }

      // return the target platform
      this._targetPlatform = targetPlatformRegistry.getInstance(workspace, getTargetPlatformId(), configuration);
    }

    // return the target platform
    return this._targetPlatform;
  }
}
