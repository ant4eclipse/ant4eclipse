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
package org.ant4eclipse.pde.tools;

import org.ant4eclipse.core.util.Pair;

import org.ant4eclipse.pde.internal.tools.FeatureDescription;
import org.ant4eclipse.pde.model.featureproject.FeatureManifest;
import org.ant4eclipse.pde.model.featureproject.FeatureManifest.Plugin;

import org.eclipse.osgi.service.resolver.BundleDescription;
import org.eclipse.osgi.service.resolver.State;

import java.util.List;

/**
 * <p>
 * A target platform contains different plug-in sets. It defines the target, against which plug-ins can be compiled and
 * tested.
 * </p>
 * 
 * @author Nils Hartmann
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 */
public interface TargetPlatform {

  /**
   * <p>
   * Returns the {@link TargetPlatformConfiguration} for this {@link TargetPlatform}.
   * </p>
   * 
   * @return
   */
  TargetPlatformConfiguration getTargetPlatformConfiguration();

  /**
   * <p>
   * Returns the resolved state.
   * </p>
   * 
   * @return
   */
  State getState();

  /**
   * <p>
   * </p>
   * 
   * @param id
   * @param version
   * @return
   */
  FeatureDescription getFeatureDescription(String id, String version);

  /**
   * <p>
   * </p>
   * 
   * @param id
   * @param version
   * @return
   */
  boolean hasFeatureDescription(String id, String version);

  /**
   * <p>
   * </p>
   * 
   * @param id
   * @return the {@link FeatureDescription} for a feature with the given id.
   */
  FeatureDescription getFeatureDescription(String id);

  /**
   * <p>
   * </p>
   * 
   * @param id
   * @return <code>true</code> if a {@link FeatureDescription} for a feature with the given id exists.
   */
  boolean hasFeatureDescription(String id);

  /**
   * <p>
   * </p>
   *
   * @param manifest
   * @return
   */
  ResolvedFeature resolveFeature(FeatureManifest manifest);
}