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
package org.ant4eclipse.lib.pde.tools;

import org.ant4eclipse.lib.pde.internal.tools.FeatureDescription;
import org.ant4eclipse.lib.pde.model.featureproject.FeatureManifest;
import org.eclipse.osgi.service.resolver.BundleDescription;
import org.osgi.framework.Version;

import java.io.File;

/**
 * <p>
 * A {@link TargetPlatform} defines the platform against which bundles projects (and features projects) contained in a
 * workspace can be built. The platform contains different plug-in sets.
 * </p>
 * 
 * @author Nils Hartmann
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 */
public interface TargetPlatform {

  /**
   * <p>
   * Returns the {@link PlatformConfiguration} for this {@link TargetPlatform}.
   * </p>
   * 
   * @return the {@link PlatformConfiguration} for this {@link TargetPlatform}.
   */
  PlatformConfiguration getTargetPlatformConfiguration();

  /**
   * <p>
   * Returns the bundle descriptor for the bundle with the given name and version. A null value is returned if no such
   * bundle is found. If multiple bundles with the same resolution state are available, the bundle with the highest
   * version number is returned if the <code>version<code> is null.
   * </p>
   * 
   * @param symbolicName
   *          symbolic name of the bundle to query
   * @param version
   *          version of the bundle to query. null matches any bundle
   * @return the descriptor for the identified bundle or <code>null</code> if no such bundle is found.
   */
  BundleDescription getResolvedBundle(String symbolicName, Version version);

  /**
   * <p>
   * Returns all the unresolved bundles.
   * </p>
   * 
   * @return all the unresolved bundles
   */
  BundleDescription[] getBundlesWithResolverErrors();

  /**
   * <p>
   * Returns the feature description for the feature with the given id and version. A null value is returned if no such
   * feature is found.
   * </p>
   * 
   * @param id
   *          the id of the requested feature (must not be null)
   * @param version
   *          the version of the requested feature (maybe null)
   * @return the feature description for the feature with the given id and version or <code>null</code> value is
   *         returned if no such feature is found.
   */
  FeatureDescription getFeatureDescription(String id, Version version);

  /**
   * <p>
   * Returns <code>true</code> if this target platform contains a feature description with the given id and version.
   * </p>
   * 
   * @param id
   *          the id of the requested feature (must not be null)
   * @param version
   *          the version of the requested feature (maybe null)
   * @return <code>true</code> if this target platform contains a feature description with the given id and version.
   */
  boolean hasFeatureDescription(String id, Version version);

  /**
   * <p>
   * Returns the feature description for the feature with the given id. A null value is returned if no such feature is
   * found.
   * </p>
   * 
   * @param id
   *          the id of the requested feature (must not be null)
   * @return the feature description for the feature with the given id or <code>null</code> value is returned if no such
   *         feature is found.
   */
  FeatureDescription getFeatureDescription(String id);

  /**
   * <p>
   * Returns <code>true</code> if this target platform contains a feature description with the given id.
   * </p>
   * 
   * @param id
   *          the id of the requested feature (must not be null)
   * @return <code>true</code> if this target platform contains a feature description with the given id.
   */
  boolean hasFeatureDescription(String id);

  /**
   * <p>
   * Returns the bundle description for the bundle with the given id. A null value is returned if no such bundle is
   * found.
   * </p>
   * 
   * @param id
   *          the id of the requested bundle (must not be null)
   * @return the bundle description for the bundle with the given id or <code>null</code> value is returned if no such
   *         feature is found.
   */
  BundleDescription getBundleDescription(String id);

  /**
   * <p>
   * Returns <code>true</code> if this target platform contains a bundle description with the given id.
   * </p>
   * 
   * @param id
   *          the id of the requested bundle (must not be null)
   * @return <code>true</code> if this target platform contains a bundle description with the given id.
   */
  boolean hasBundleDescription(String id);

  /**
   * <p>
   * </p>
   * 
   * @param bundleDescriptionId
   * @return
   */
  boolean matchesPlatformFilter(String bundleDescriptionId);

  /**
   * <p>
   * Returns the {@link ResolvedFeature}
   * </p>
   * 
   * @param source
   * @param manifest
   * @return returns the resolved feature
   */
  ResolvedFeature resolveFeature(Object source, FeatureManifest manifest);

  /**
   * Returns a list of all locations used by this TargetPlatform.
   * 
   * @return A list of all locations used by this TargetPlatform. Neither <code>null</code> nor empty.
   */
  File[] getLocations();

  /**
   * <p>
   * Refreshes this target platform.
   * </p>
   * 
   */
  void refresh();
}