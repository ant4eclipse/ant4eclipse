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
package org.ant4eclipse.pde.test.builder;

import org.ant4eclipse.jdt.test.builder.JdtProjectBuilder;

import org.ant4eclipse.pde.model.pluginproject.PluginProjectRole;

import org.ant4eclipse.lib.core.util.Utilities;
import org.ant4eclipse.testframework.FileHelper;

import java.io.File;

/**
 *
 */
public class PdeProjectBuilder extends JdtProjectBuilder {

  /** - */
  private BundleManifest        _manifest;

  /** - */
  private PluginBuildProperties _pluginBuildProperties;

  /**
   * @param projectName
   */
  public PdeProjectBuilder(String projectName) {
    super(projectName);

    withDefaultBundleManifest();
    withPdeNature();
  }

  public static PdeProjectBuilder getPreConfiguredPdeProjectBuilder(String projectName) {
    return (PdeProjectBuilder) new PdeProjectBuilder(projectName).withDefaultBundleManifest()
        .withJreContainerClasspathEntry().withSrcClasspathEntry("src", false).withOutputClasspathEntry("bin");
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected void createArtefacts(File projectDir) {
    super.createArtefacts(projectDir);

    createBundleManifestFile(projectDir);
    createPluginBuildPropertiesFile(projectDir);
  }

  protected PdeProjectBuilder withPdeNature() {
    withContainerClasspathEntry("org.eclipse.pde.core.requiredPlugins");
    return (PdeProjectBuilder) withNature(PluginProjectRole.PLUGIN_NATURE);
  }

  protected PdeProjectBuilder withDefaultBundleManifest() {
    this._manifest = new BundleManifest(getProjectName());
    return this;
  }

  public BundleManifest withBundleManifest() {
    this._manifest = new BundleManifest(getProjectName());
    return this._manifest;
  }

  protected void createBundleManifestFile(File projectDir) {
    Utilities.mkdirs(new File(projectDir, "META-INF"));
    File manifestFile = new File(new File(projectDir, "META-INF"), "MANIFEST.MF");
    FileHelper.createFile(manifestFile);
    this._manifest.write(manifestFile);
  }

  public PluginBuildProperties withDefaultBuildProperties() {
    this._pluginBuildProperties = new PluginBuildProperties();
    this._pluginBuildProperties.withLibrary(".").withSource("src").withOutput("bin");
    return this._pluginBuildProperties;
  }

  public PluginBuildProperties withBuildProperties() {
    this._pluginBuildProperties = new PluginBuildProperties();
    return this._pluginBuildProperties;
  }

  protected void createPluginBuildPropertiesFile(File projectDir) {
    if (this._pluginBuildProperties != null) {
      File buildPropertiesFile = new File(projectDir, "build.properties");
      Utilities.writeFile(buildPropertiesFile, this._pluginBuildProperties.toString(), Utilities.ENCODING);
    }
  }
}
