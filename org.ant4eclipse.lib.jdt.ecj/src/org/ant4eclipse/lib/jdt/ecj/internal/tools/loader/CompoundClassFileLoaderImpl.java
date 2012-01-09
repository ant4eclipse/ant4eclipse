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
package org.ant4eclipse.lib.jdt.ecj.internal.tools.loader;

import org.ant4eclipse.lib.core.ClassName;
import org.ant4eclipse.lib.core.util.Utilities;
import org.ant4eclipse.lib.jdt.ecj.ClassFile;
import org.ant4eclipse.lib.jdt.ecj.ClassFileLoader;
import org.ant4eclipse.lib.jdt.ecj.ReferableSourceFile;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CompoundClassFileLoaderImpl implements ClassFileLoader {

  private ClassFileLoader[]                 _classFileLoaders;

  /** maps packages to a package provider that contains a list of one or more class path entries */
  private Map<String,List<ClassFileLoader>> _allPackages;

  // Assure.notNull( "classFileLoaders", classFileLoaders );
  public CompoundClassFileLoaderImpl( ClassFileLoader[] classFileLoaders ) {
    _classFileLoaders = classFileLoaders;
    _allPackages = new HashMap<String,List<ClassFileLoader>>();
    initialise();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public List<File> getClasspath() {
    List<File>    files = new ArrayList<File>();
    List<String>  set   = new ArrayList<String>();
    for( ClassFileLoader loader : _classFileLoaders ) {
      List<File> entries = loader.getClasspath();
      for( File entry : entries ) {
        entry = Utilities.getCanonicalFile( entry );
        String path = entry.getAbsolutePath();
        if( Utilities.isWindows() ) {
          // for windows the case makes no difference
          path = path.toLowerCase();
        }
        if( !set.contains( path ) ) {
          set.add( path );
          files.add( entry );
        }
      }
    }
    return files;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public List<String> getAllPackages() {
    return new ArrayList<String>( _allPackages.keySet() );
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean hasPackage( String packageName ) {
    return _allPackages.containsKey( packageName );
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public ClassFile loadClass( ClassName className ) {

    // get the class file loader list
    List<ClassFileLoader> classFileLoaderList = _allPackages.get( className.getPackageName() );

    // return if class file loader list is null
    if( classFileLoaderList == null ) {
      return null;
    }

    // declare the result
    ClassFile result = null;

    // try to find the class file...
    for( ClassFileLoader classFileLoader : classFileLoaderList ) {

      // try to load class file...
      ClassFile classFile = classFileLoader.loadClass( className );

      // class file was found...
      if( classFile != null ) {

        // if the class file has no access restrictions, return the class file...
        if( !classFile.hasAccessRestriction() ) {
          return classFile;
        }
        // else set the class file a result, if result is null
        else if( result == null ) {
          result = classFile;
        }
      }
    }

    // return the result
    return result;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public ReferableSourceFile loadSource( ClassName className ) {

    // TODO: Access restrictions for source files!!

    // if the package name is not in the map of all packages, return immediately
    List<ClassFileLoader> classFileLoaderList = _allPackages.get( className.getPackageName() );
    if( classFileLoaderList == null ) {
      return null;
    }

    // search for the source file
    for( ClassFileLoader classFileLoader : classFileLoaderList ) {
      ReferableSourceFile sourceFile = classFileLoader.loadSource( className );
      if( sourceFile != null ) {
        return sourceFile;
      }
    }

    // last resort: return null
    return null;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String toString() {
    StringBuffer buffer = new StringBuffer();
    buffer.append( "[CompoundClassFileLoader:" );
    buffer.append( " { " );
    for( int i0 = 0; (_classFileLoaders != null) && (i0 < _classFileLoaders.length); i0++ ) {
      buffer.append( " _classFileLoaders[" + i0 + "]: " );
      buffer.append( _classFileLoaders[i0] );
    }
    buffer.append( " } " );
    // buffer.append(" _allPackages: ");
    // buffer.append(_allPackages);
    buffer.append( "]" );
    return buffer.toString();
  }

  private void initialise() {

    for( ClassFileLoader classFileLoader : _classFileLoaders ) {
      List<String> packages = classFileLoader.getAllPackages();
      for( String aPackage : packages ) {
        if( _allPackages.containsKey( aPackage ) ) {
          List<ClassFileLoader> classFileLoaderList = _allPackages.get( aPackage );
          if( !classFileLoaderList.contains( classFileLoader ) ) {
            classFileLoaderList.add( classFileLoader );
          }
        } else {
          List<ClassFileLoader> classFileLoaderList = new ArrayList<ClassFileLoader>();
          classFileLoaderList.add( classFileLoader );
          _allPackages.put( aPackage, classFileLoaderList );
        }
      }
    }
  }
  
} /* ENDCLASS */
