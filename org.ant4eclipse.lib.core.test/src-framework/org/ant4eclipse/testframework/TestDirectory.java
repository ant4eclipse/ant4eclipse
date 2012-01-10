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
import org.junit.Assert;

import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

/**
 * The Test Environment contains a set of folder that are created before and removed after a test case.
 * 
 * @todo [17-Dec-2009:KASI] Make use of the Utilities methods !!!
 * 
 * @author Nils Hartmann (nils@nilshartmann.net)
 */
public class TestDirectory {

  /**
   * should {@link #dispose()} remove the created directories?
   * 
   * can be set to <tt>false</tt> to avoid removing the directories (can be useful for debug purposes)
   */
  private boolean _removeOnDispose = true;

  /**
   * The root directory of the test environment
   * 
   * <p>
   * <b>NOTE!</b> this directory will be deleted recursivley!
   */
  private File    _rootDir;

  public TestDirectory() {
    init();
  }

  public TestDirectory( boolean removeOnDispose ) {
    _removeOnDispose = removeOnDispose;
    init();
  }

  protected void init() {
    _rootDir = new File( System.getProperty( "java.io.tmpdir" ), "a4etest" );
    if( _rootDir.exists() ) {
      if( !Utilities.delete( _rootDir ) ) {
        throw new RuntimeException( String.format( "Failed to delete directory '%s'.", _rootDir ) );
      }
    }
    System.out.println( "Create test dir: " + _rootDir );
    Utilities.mkdirs( _rootDir );
  }

  public void dispose() {
    try {
      if( _rootDir != null && _removeOnDispose ) {
        System.out.println( "Remove test dir: " + _rootDir );
        if( !Utilities.delete( _rootDir ) ) {
          throw new RuntimeException( String.format( "Failed to delete directory '%s'.", _rootDir ) );
        }
      }
    } catch( Exception ex ) {
      System.err.println( "WARN! Could not remove test directory " + _rootDir + ": " + ex );
    }
  }

  /**
   * Creates the file fileName with the given content in the root folder of the test environment
   * 
   * @param fileName
   * @param content
   * @throws IOException
   */
  public File createFile( String fileName, String content ) {
    File outFile = new File( _rootDir, fileName );
    Utilities.writeFile( outFile, content, Utilities.ENCODING );
    return outFile;
  }

  /**
   * Copies the content from the given input stream to the file.
   * 
   * <p>
   * This method closes the input stream after copying it
   * 
   * @param fileName
   *          The filename that is relative to the root of the test environment
   * @param inputStream
   *          The inputStream to read from
   * @return The file that has been createad
   * @throws IOException
   */
  public File createFile( String fileName, InputStream inputStream ) throws IOException {
    ByteArrayOutputStream output = new ByteArrayOutputStream();
    byte[] buffer = new byte[1024];
    int bytesRead = -1;
    while( (bytesRead = inputStream.read( buffer, 0, buffer.length )) != -1 ) {
      output.write( buffer, 0, bytesRead );
    }
    Utilities.close( (Closeable) inputStream );
    return createFile( fileName, output.toString() );
  }

  public File createSubDirectory( String name ) {
    Assert.assertNotNull( name );

    File subdir = new File( _rootDir, name );
    Utilities.mkdirs( subdir );
    return subdir;
  }

  public File getRootDir() {
    return _rootDir;
  }

} /* ENDCLASS */
