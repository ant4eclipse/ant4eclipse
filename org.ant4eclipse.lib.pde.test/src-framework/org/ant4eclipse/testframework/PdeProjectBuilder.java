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
package org.ant4eclipse.testframework;

import org.ant4eclipse.lib.core.util.Utilities;
import org.ant4eclipse.lib.pde.model.pluginproject.PluginProjectRole;

import java.io.File;

/**
 *
 */
public class PdeProjectBuilder extends JdtProjectBuilder {

  private BundleManifest        _manifest;

  private PluginBuildProperties _pluginBuildProperties;

  /**
   * @param projectName
   */
  public PdeProjectBuilder( String projectName ) {
    super( projectName );

    withDefaultBundleManifest();
    withPdeNature();
  }

  public static PdeProjectBuilder getPreConfiguredPdeProjectBuilder( String projectName ) {
    PdeProjectBuilder result = new PdeProjectBuilder( projectName );
    result.withDefaultBundleManifest();
    result.withJreContainerClasspathEntry();
    result.withSrcClasspathEntry( "src", false );
    result.withOutputClasspathEntry( "bin" );
    return result;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected void createArtefacts( File projectDir ) {
    super.createArtefacts( projectDir );
    createBundleManifestFile( projectDir );
    createPluginBuildPropertiesFile( projectDir );
  }

  protected PdeProjectBuilder withPdeNature() {
    withContainerClasspathEntry( "org.eclipse.pde.core.requiredPlugins" );
    withNature( PluginProjectRole.PLUGIN_NATURE );
    return this;
  }

  protected PdeProjectBuilder withDefaultBundleManifest() {
    _manifest = new BundleManifest( getProjectName() );
    return this;
  }

  public BundleManifest withBundleManifest() {
    _manifest = new BundleManifest( getProjectName() );
    return _manifest;
  }

  protected void createBundleManifestFile( File projectDir ) {
    Utilities.mkdirs( new File( projectDir, "META-INF" ) );
    File manifestFile = new File( new File( projectDir, "META-INF" ), "MANIFEST.MF" );
    _manifest.write( manifestFile );
  }

  public PluginBuildProperties withDefaultBuildProperties() {
    _pluginBuildProperties = new PluginBuildProperties();
    _pluginBuildProperties.withLibrary( "." ).withSource( "src" ).withOutput( "bin" );
    return _pluginBuildProperties;
  }

  public PluginBuildProperties withBuildProperties() {
    _pluginBuildProperties = new PluginBuildProperties();
    return _pluginBuildProperties;
  }

  protected void createPluginBuildPropertiesFile( File projectDir ) {
    if( _pluginBuildProperties != null ) {
      File buildPropertiesFile = new File( projectDir, "build.properties" );
      Utilities.writeFile( buildPropertiesFile, _pluginBuildProperties.toString(), Utilities.ENCODING );
    }
  }
  
} /* ENDCLASS */
