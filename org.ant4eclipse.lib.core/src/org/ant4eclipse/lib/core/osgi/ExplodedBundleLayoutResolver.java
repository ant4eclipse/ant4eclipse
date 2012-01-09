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
package org.ant4eclipse.lib.core.osgi;

import org.ant4eclipse.lib.core.util.ManifestHelper;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.jar.Manifest;

/**
 * <p>
 * Implements a {@link BundleLayoutResolver} for exploded bundles.
 * </p>
 * 
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 */
public class ExplodedBundleLayoutResolver implements BundleLayoutResolver {

  /** the location */
  private File     _location;

  /** the manifest */
  private Manifest _manifest;

  /**
   * <p>
   * Creates a new instances of type {@link ExplodedBundleLayoutResolver}.
   * </p>
   * 
   * @param location
   *          the root directory of the exploded bundle.
   */
  // Assure.isDirectory( "location", location );
  public ExplodedBundleLayoutResolver( File location ) {
    _location = location;
    File manifestFile = new File( location, "META-INF/MANIFEST.MF" );
    try {
      _manifest = new Manifest( new FileInputStream( manifestFile ) );
    } catch( Exception e ) {
      throw new RuntimeException( e );
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public byte getType() {
    return LIBRARY;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public File getLocation() {
    return _location;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Manifest getManifest() {
    return _manifest;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public List<File> resolveBundleClasspathEntries() {

    // prepare results
    List<File> result = new ArrayList<File>();

    // get bundle class path
    String[] bundleClasspathEntries = ManifestHelper.getBundleClasspath( _manifest );

    // add class path entries to the result
    for( String bundleClasspathEntrie : bundleClasspathEntries ) {

      // add 'self'
      if( ".".equals( bundleClasspathEntrie ) ) {
        result.add( _location );
      }
      // add entry
      else {
        File classpathEntry = new File( _location, bundleClasspathEntrie );
        if( classpathEntry.exists() ) {
          result.add( classpathEntry );
        }
      }
    }

    return result;
  }
  
} /* ENDCLASS */
