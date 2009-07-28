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
package org.ant4eclipse.pde.internal.tools;

import org.ant4eclipse.core.Assert;

import org.ant4eclipse.pde.internal.model.pluginproject.BundleDescriptionLoader;
import org.ant4eclipse.pde.internal.model.pluginproject.FeatureDescriptionLoader;
import org.ant4eclipse.pde.model.link.LinkFile;
import org.ant4eclipse.pde.model.link.LinkFileFactory;

import org.eclipse.osgi.service.resolver.BundleDescription;

import java.io.File;

/**
 * <p>
 * A {@link BundleAndFeatureSet} implementation that represent an eclipse target platform containing binary bundles and
 * features.
 * </p>
 * 
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 * @author Nils Hartmann (nils@nilshartmann.net)
 */
public class BinaryBundleAndFeatureSet extends AbstractBundleAndFeatureSet {

  /** the constant that defines the default plug-in directory */
  public static final String DEFAULT_PLUGIN_DIRECTORY  = "plugins";

  /** the constant that defines the default feature directory */
  public static final String DEFAULT_FEATURE_DIRECTORY = "features";

  /** the location of the platform against which the workspace plug-ins will be compiled and tested */
  private final File         _targetPlatformLocation;

  /**
   * <p>
   * Creates a new instance of type BinaryBundleAndFeatureSet.
   * </p>
   * 
   * @param targetPlatformLocation
   *          the target platform location.
   */
  public BinaryBundleAndFeatureSet(final File targetPlatformLocation) {
    Assert.isDirectory(targetPlatformLocation);

    this._targetPlatformLocation = targetPlatformLocation;
  }

  /**
   * {@inheritDoc}
   */
  protected void readBundlesAndFeatures() {

    // 1. read plugin from target location
    // TODO: ERROR-HANDLING...
    File pluginsDirectory = new File(this._targetPlatformLocation, DEFAULT_PLUGIN_DIRECTORY);
    if (!pluginsDirectory.exists()) {
      pluginsDirectory = this._targetPlatformLocation;
    }
    if ((pluginsDirectory != null) && pluginsDirectory.exists()) {
      final File[] plugins = pluginsDirectory.listFiles();
      for (int i1 = 0; i1 < plugins.length; i1++) {
        final File plugin = plugins[i1];
        final BundleDescription bundleDescription = BundleDescriptionLoader.parsePlugin(plugin);
        if (bundleDescription != null) {
          addBundleDescription(bundleDescription);
        }
      }
    }

    // 2. read plugins from linked directories in target location
    if (this._targetPlatformLocation != null) {
      final LinkFile[] linkFiles = LinkFileFactory.getLinkFiles(this._targetPlatformLocation);
      for (int i = 0; i < linkFiles.length; i++) {
        final LinkFile file = linkFiles[i];
        if (file.isValidDestination()) {
          final File pluginsDirectory1 = file.getPluginsDirectory();
          if ((pluginsDirectory1 != null) && pluginsDirectory1.exists()) {
            final File[] plugins = pluginsDirectory1.listFiles();
            for (int i2 = 0; i2 < plugins.length; i2++) {
              final File plugin = plugins[i2];
              final BundleDescription bundleDescription = BundleDescriptionLoader.parsePlugin(plugin);
              if (bundleDescription != null) {
                addBundleDescription(bundleDescription);
              }
            }
          }
        }
      }
    }

    // 1. read features from target location
    // TODO: ERROR-HANDLING...

    // try to search features in the 'features' directory
    File featuresDirectory = new File(this._targetPlatformLocation, DEFAULT_FEATURE_DIRECTORY);

    // if the 'features' directory doesn't exist, use the target platform location
    if (!featuresDirectory.exists()) {
      featuresDirectory = this._targetPlatformLocation;
    }

    // 
    readFeature(featuresDirectory);

    // 2. read plugins from linked directories in target location
    if (this._targetPlatformLocation != null) {

      final LinkFile[] linkFiles = LinkFileFactory.getLinkFiles(this._targetPlatformLocation);

      for (LinkFile linkFile : linkFiles) {

        if (linkFile.isValidDestination()) {
          readFeature(linkFile.getFeaturesDirectory());
        }
      }
    }
  }

  /**
   * <p>
   * </p>
   * 
   * @param directory
   */
  private void readFeature(File directory) {

    if (directory == null || !directory.exists()) {
      return;
    }

    for (File feature : directory.listFiles()) {
      final FeatureDescription featureDescription = FeatureDescriptionLoader.parseFeature(feature);

      if (featureDescription != null) {
        addFeaturesDescription(featureDescription);
      }
    }
  }
}
