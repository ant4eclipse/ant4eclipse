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

import org.junit.Assert;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

/**
 * <p>
 * Collection of utilities used in conjunction with the JUnit testsuite.
 * </p>
 * 
 * @author Daniel Kasmeroglu (Daniel.Kasmeroglu@Kasisoft.net)
 */
public class JUnitUtilities {

  /**
   * Reads the content of the supplied file.
   * 
   * @param file
   *          The file which content has to be read.
   * 
   * @return The data from the file.
   */
  public static final byte[] loadFile( File file ) {
    byte[] buffer = new byte[1024];
    ByteArrayOutputStream byteout = new ByteArrayOutputStream();
    FileInputStream filein = null;
    try {
      filein = new FileInputStream( file );
      int read = filein.read( buffer );
      while( read != -1 ) {
        if( read > 0 ) {
          byteout.write( buffer, 0, read );
        }
        read = filein.read( buffer );
      }
      filein.close();
    } catch( IOException ex ) {
      Assert.fail( ex.getMessage() );
    }
    return byteout.toByteArray();
  }

  /**
   * <p>
   * Creates a temporary directory.
   * </p>
   * 
   * @return A temporary directory. Not <code>null</code>.
   */
  public static final File createTempDir() {
    return createTempDir( true );
  }

  /**
   * <p>
   * Creates a temporary directory location and allows to create it.
   * </p>
   * 
   * @param create
   *          <code>true</code> <=> Create a directory.
   * 
   * @return A temporary directory. Not <code>null</code>.
   */
  public static final File createTempDir( boolean create ) {
    try {
      File tempfile = File.createTempFile( "a4e.", ".dir" );
      int tries = 10;
      while( tempfile.isFile() && (tries > 0) ) {
        if( tempfile.delete() ) {
          break;
        } else {
          try {
            Thread.sleep( 3000 );
          } catch( InterruptedException ex ) {
          }
        }
        tries--;
      }
      if( tempfile.isFile() ) {
        Assert.fail();
      }
      if( create ) {
        if( !tempfile.mkdir() ) {
          Assert.fail();
        }
      }
      Runtime.getRuntime().addShutdownHook( new Cleaner( tempfile ) );
      return tempfile;
    } catch( IOException ex ) {
      Assert.fail( ex.getMessage() );
      return null;
    }
  }

  /**
   * <p>
   * Creates a temporary file.
   * </p>
   * 
   * @return A temporary file. Not <code>null</code>.
   */
  public static final File createTempFile() {
    try {
      File result = File.createTempFile( "a4e.", ".file" );
      Runtime.getRuntime().addShutdownHook( new Cleaner( result ) );
      return result;
    } catch( IOException ex ) {
      Assert.fail( ex.getMessage() );
      return null;
    }
  }

  private static class Cleaner extends Thread {

    private File file;

    public Cleaner( File resource ) {
      file = resource;
    }

    @Override
    public void run() {
      delete( file );
    }

    public void delete( File resource ) {
      if( resource.exists() ) {
        if( resource.isDirectory() ) {
          File[] children = resource.listFiles();
          for( File child : children ) {
            delete( child );
          }
        }
        resource.delete();
      }
    }

  } /* ENDCLASS */

} /* ENDCLASS */
