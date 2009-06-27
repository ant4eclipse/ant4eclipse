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

import java.io.File;

import org.ant4eclipse.core.Assert;
import org.ant4eclipse.core.logging.A4ELogging;
import org.ant4eclipse.pde.internal.model.pluginproject.BundleDescriptionLoader;
import org.ant4eclipse.pde.model.link.LinkFile;
import org.ant4eclipse.pde.model.link.LinkFileFactory;
import org.eclipse.osgi.service.resolver.BundleDescription;

/**
 * A {@link BundleSet} implementation that represent an Eclipse "Target Platform" containing binary plugins.
 * 
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 * @author Nils Hartmann (nils@nilshartmann.net)
 */
public class BinaryBundleSet extends AbstractBundleSet {

  /** the constant that defines the default plugin directory */
  public static final String DEFAULT_PLUGIN_DIRECTORY = "plugins";

  /** the location of the platform against which the workspace plugins will be compiled and tested */
  private final File         _targetPlatformLocation;

  /**
   * <p>
   * Creates a new instance of type TargetPlatform with a specific workspace and an optional target platform location.
   * </p>
   * 
   * @param workspace
   *          the workspace that will be used.
   * @param targetPlatformLocation
   *          the optional target platform location.
   */
  public BinaryBundleSet(final File targetPlatformLocation) {
    super(targetPlatformLocation);
    A4ELogging.trace("BinaryPluginSet<init>(%s)", targetPlatformLocation);
    Assert.isDirectory(targetPlatformLocation);

    this._targetPlatformLocation = targetPlatformLocation;
  }

  /**
   * @see net.sf.ant4eclipse.tools.pde.internal.target.AbstractBundleSet#readBundles()
   */
  protected void readBundles() {

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
  }
}
