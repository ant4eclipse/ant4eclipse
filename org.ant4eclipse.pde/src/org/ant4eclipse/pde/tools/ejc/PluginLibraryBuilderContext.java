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
package org.ant4eclipse.pde.tools.ejc;

import java.io.File;

import org.ant4eclipse.core.Assert;
import org.ant4eclipse.pde.model.buildproperties.PluginBuildProperties;
import org.ant4eclipse.pde.model.buildproperties.PluginBuildProperties.Library;
import org.ant4eclipse.pde.tools.target.TargetPlatform;
import org.eclipse.osgi.service.resolver.BundleDescription;

/**
 * The PluginLibraryBuilderContext carries several information that are needed to build a plugin, i.e. resolved classes
 * and sourcefolders.
 * 
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 * @author Nils Hartmann (nils@nilshartmann.net)
 */
public class PluginLibraryBuilderContext {
  /** */
  private final Library               library;

  /** */
  private final BundleDescription     bundleDescription;

  private final PluginBuildProperties _pluginBuildProperties;

  /** */
  private final File[]                sourceFolder;

  /** */
  private final File                  classesFolder;

  /** */
  private final File                  pluginOutputPath;

  /** The target platform */
  private final TargetPlatform        _targetPlatform;

  /**
   * <p>
   * Creates a new instance of type PluginLibraryBuilderContext.
   * </p>
   * 
   * @param library
   *          The library that is currently to be built
   * @param bundleDescription
   *          The description of the library's owning bundle
   * @param sourceFolder
   *          The sourcefolder of this library as absolute pathes
   * @param targetPlatform
   *          The TargetPlatform that should be used to resolved required plugins etc
   * @param classesFolder
   *          The outputfolder of this library as absolute pathes
   * @param pluginOutputPath
   *          The root destination folder of the plugin
   */
  public PluginLibraryBuilderContext(final Library library, final BundleDescription bundleDescription,
      final PluginBuildProperties pluginBuildProperties, final TargetPlatform targetPlatform,
      final File[] sourceFolder, final File classesFolder, final File pluginOutputPath) {

    Assert.notNull(library);
    Assert.notNull(bundleDescription);
    Assert.notNull(pluginBuildProperties);
    Assert.notNull(targetPlatform);
    Assert.notNull(sourceFolder);
    Assert.notNull(classesFolder);
    Assert.notNull(pluginOutputPath);

    this.library = library;
    this.bundleDescription = bundleDescription;
    this._pluginBuildProperties = pluginBuildProperties;
    this._targetPlatform = targetPlatform;
    this.sourceFolder = sourceFolder;
    this.classesFolder = classesFolder;
    this.pluginOutputPath = pluginOutputPath;
  }

  /**
   * Returns the absolute path where the class files should be compiled to
   */
  public File getClassesFolder() {
    return this.classesFolder;
  }

  /**
   * <p>
   * Returns the descrption of the library that should be built.
   * </p>
   * 
   * @return the descrption of the library that should be built.
   */
  public Library getLibrary() {
    return this.library;
  }

  public BundleDescription getBundleDescription() {
    return this.bundleDescription;
  }

  /**
   * <p>
   * Returns the output path for the plugin that should be built.
   * </p>
   * 
   * @return The output path for the plugin that should be built.
   */
  public File getPluginOutputPath() {
    return this.pluginOutputPath;
  }

  public File[] getSourceFolder() {
    return this.sourceFolder;
  }

  public PluginBuildProperties getPluginBuildProperties() {
    return this._pluginBuildProperties;
  }

  public TargetPlatform getTargetPlatform() {
    return this._targetPlatform;
  }

}
