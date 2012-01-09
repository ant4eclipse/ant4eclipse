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

import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.jar.JarEntry;
import java.util.jar.JarOutputStream;
import java.util.jar.Manifest;

public class JarBundleBuilder {

  public static int      BUFFER_SIZE = 10240;

  /** - */
  private BundleManifest _manifest;

  /** - */
  private String         _name;

  private String         _embeddedJarName;

  /**
   * <p>
   * Creates a new instance of type {@link JarBundleBuilder}.
   * </p>
   * 
   * @param name
   */
  // Assure.nonEmpty( "name", name );
  public JarBundleBuilder( String name ) {
    _name = name;
    _manifest = new BundleManifest( _name );
  }

  /**
   * <p>
   * </p>
   * 
   * @return
   */
  public BundleManifest withBundleManifest() {
    return _manifest;
  }

  public JarBundleBuilder withEmbeddedJar( String name ) {
    _embeddedJarName = name;
    _manifest.withClassPath( ".," + name + ".jar" );
    return this;
  }

  /**
   * <p>
   * </p>
   * 
   * @param destinationDirectory
   * @return
   */
  public File createIn( File destinationDirectory ) {

    if( !destinationDirectory.isDirectory() ) {
      throw new RuntimeException( "Directory '" + destinationDirectory + "' must be a directory." );
    }

    File jarFile = new File( destinationDirectory, _name + ".jar" );

    if( _embeddedJarName == null ) {
      createJarArchive( jarFile, _manifest.getManifest() );
    } else {
      File jarFile2 = new File( destinationDirectory, _embeddedJarName + ".jar" );
      createJarArchive( jarFile, _manifest.getManifest(), new File[] { jarFile2 } );
      jarFile2.delete();
    }
    return jarFile;
  }

  /**
   * <p>
   * </p>
   * 
   * @param archiveFile
   */
  private void createJarArchive( File archiveFile, Manifest manifest ) {
    createJarArchive( archiveFile, manifest, new File[0] );
  }

  /**
   * <p>
   * </p>
   * 
   * @param archiveFile
   * @param tobeJared
   */
  private void createJarArchive( File archiveFile, Manifest manifest, File[] tobeJared ) {
    try {
      byte buffer[] = new byte[BUFFER_SIZE];
      // Open archive file
      FileOutputStream stream = new FileOutputStream( archiveFile );
      JarOutputStream out = new JarOutputStream( stream, manifest );

      for( int i = 0; i < tobeJared.length; i++ ) {
        if( tobeJared[i] == null || !tobeJared[i].exists() || tobeJared[i].isDirectory() ) {
          continue; // Just in case...
        }
        System.out.println( "Adding " + tobeJared[i].getName() );

        // Add archive entry
        JarEntry jarAdd = new JarEntry( tobeJared[i].getName() );
        jarAdd.setTime( tobeJared[i].lastModified() );
        out.putNextEntry( jarAdd );

        // Write file to archive
        FileInputStream in = new FileInputStream( tobeJared[i] );
        Utilities.copy( in, out, buffer );
        Utilities.close( (Closeable) in );
      }
      Utilities.close( (Closeable) out );
      Utilities.close( (Closeable) stream );
      System.out.println( "Adding completed OK" );
    } catch( Exception ex ) {
      ex.printStackTrace();
      System.out.println( "Error: " + ex.getMessage() );
    }
  }
  
} /* ENDCLASS */
