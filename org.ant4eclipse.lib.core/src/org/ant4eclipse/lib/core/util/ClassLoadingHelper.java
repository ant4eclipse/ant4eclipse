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
package org.ant4eclipse.lib.core.util;

import org.ant4eclipse.lib.core.logging.A4ELogging;

import java.io.File;
import java.lang.reflect.Method;
import java.security.CodeSource;

/**
 * <p>
 * Helper class that allows you to load classes either from the ant class loader or the standard class loader.
 * </p>
 * 
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 */
public class ClassLoadingHelper {

  /** the method 'getClasspath' */
  private static final String METHOD_GET_CLASSPATH                      = "getClasspath";

  /** the class 'AntClassLoader' */
  private static final String CLASS_ORG_APACHE_TOOLS_ANT_ANTCLASSLOADER = "org.apache.tools.ant.AntClassLoader";

  /**
   * <p>
   * Returns the class path entries for the specified class.
   * </p>
   * 
   * @param clazz
   *          the class
   * @return the class path entries for the specified class.
   */
  // Assure.notNull( "clazz", clazz );
  public static String[] getClasspathEntriesFor( Class<?> clazz ) {

    // get class loader
    ClassLoader classLoader = clazz.getClassLoader();
    Class<? extends ClassLoader> classLoaderClass = classLoader.getClass();

    // AntClassLoader: we have to call 'getClasspath()', because the code
    // source always is the 'ant.jar'
    if( classLoaderClass.getName().equals( CLASS_ORG_APACHE_TOOLS_ANT_ANTCLASSLOADER ) ) {

      try {
        Method method = classLoaderClass.getDeclaredMethod( METHOD_GET_CLASSPATH, new Class[0] );
        Object result = method.invoke( classLoader, new Object[0] );
        String[] fileNames = result.toString().split( File.pathSeparator );

        // patch the file names
        for( int i = 0; i < fileNames.length; i++ ) {
          fileNames[i] = patchFileName( fileNames[i] );
        }

        // return the file names
        return fileNames;
      } catch( Exception e ) {
        e.printStackTrace();
        return new String[0];
      }
    }
    // 'normal' class loader: just retrieve the code source
    else {
      CodeSource codeSource = clazz.getProtectionDomain().getCodeSource();

      String fileName = codeSource.getLocation().getFile();
      String patchedfileName = patchFileName( fileName );

      if( A4ELogging.isDebuggingEnabled() ) {
        A4ELogging.debug( "Class path for class '%s' is '%s' (patched: '%s').", clazz, fileName, patchedfileName );
      }

      // patch and return the file name
      return new String[] { patchFileName( codeSource.getLocation().getFile() ) };
    }
  }

  /**
   * <p>
   * Replaces any occurrence of a '%20' in the given string with an blank (' ').
   * </p>
   * 
   * @param fileName
   *          the file name
   * @return the patched file name
   */
  private static String patchFileName( String fileName ) {
    return fileName.replaceAll( "\\%20", " " );
  }
  
} /* ENDCLASS */
