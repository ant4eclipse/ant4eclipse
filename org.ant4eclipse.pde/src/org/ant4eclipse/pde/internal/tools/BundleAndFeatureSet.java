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
package org.ant4eclipse.pde.internal.tools;

import org.eclipse.osgi.service.resolver.BundleDescription;
import org.osgi.framework.Version;

import java.util.List;

/**
 * <p>
 * A {@link BundleAndFeatureSet} represents a set of bundles and features that are stored in a specific location. A
 * bundle can be a plug-in project or a binary bundle that is packed as a OSGi bundle or a exploded directory. A feature
 * can be a feature project or a built feature that is packed as jar file or a exploded directory..
 * </p>
 * <p>
 * A {@link BundleAndFeatureSet} provides several methods for retrieving bundles and features from their location.
 * </p>
 * 
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 * @author Nils Hartmann (nils@nilshartmann.net)
 */
public interface BundleAndFeatureSet {

  /**
   * <p>
   * Initializes the {@link BundleAndFeatureSet} (which means that the bundles and features will be reed from the
   * underlying location).
   * </p>
   * <p>
   * If {@link BundleAndFeatureSet} already has been initialized, the method must return immediately. The initialization
   * may a time consuming operation since all bundles in the bundle set have to be parsed.
   * </p>
   */
  void initialize();

  /**
   * <p>
   * Returns all {@link BundleDescription BundleDescriptions} that are contained in this {@link BundleAndFeatureSet}.
   * </p>
   * 
   * @return all {@link BundleDescription BundleDescriptions} that are contained in this {@link BundleAndFeatureSet}.
   */
  List<BundleDescription> getAllBundleDescriptions();

  /**
   * <p>
   * Returns <code>true</code> if the {@link BundleAndFeatureSet} contains a Bundle with the given symbolic name.
   * </p>
   * 
   * @param symbolicName
   *          the symbolic name.
   * @return <code>true</code> if the {@link BundleAndFeatureSet} contains a Bundle with the given symbolic name.
   */
  boolean containsBundle(String symbolicName);

  /**
   * <p>
   * Returns the {@link FeatureDescription} with the given feature id and the given version. If no such feature exists,
   * <code>null</code> will be returned instead.
   * </p>
   * 
   * @param featureId
   *          the feature id
   * @param version
   *          the version of the feature
   * @return the {@link FeatureDescription}
   */
  FeatureDescription getFeatureDescription(String featureId, Version version);

  /**
   * <p>
   * Returns the {@link FeatureDescription} with the given feature id. If no such feature exists, <code>null</code> will
   * be returned instead. If multiple features exits with the specified feature id, the feature with the highest version
   * will be returned.
   * </p>
   * 
   * @param featureId
   *          the feature id
   * @return the {@link FeatureDescription}
   */
  FeatureDescription getFeatureDescription(String featureId);
}