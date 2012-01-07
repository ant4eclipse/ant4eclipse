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
package org.ant4eclipse.lib.jdt.ecj;

import org.ant4eclipse.lib.core.A4ECore;
import org.ant4eclipse.lib.core.util.Utilities;
import org.ant4eclipse.lib.jdt.ecj.internal.tools.loader.ClasspathClassFileLoaderImpl;
import org.ant4eclipse.lib.jdt.ecj.internal.tools.loader.CompoundClassFileLoaderImpl;
import org.ant4eclipse.lib.jdt.ecj.internal.tools.loader.FilteringClassFileLoader;

import java.io.File;
import java.util.Arrays;
import java.util.List;

/**
 * <p>
 * Provides static factory methods to create class file finders.
 * </p>
 * 
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 */
public class ClassFileLoaderFactory {

  // /**
  // * <p>
  // * Creates an new instance of type {@link ClassFileLoader}, that can load {@link ClassFile ClassFiles} from an array
  // * of files (jar files or directories).
  // * </p>
  // *
  // * @param source
  // * the file, that represents the source (e.g. a jar file, the root directory of an "exploded" bundle or the
  // * root directory of an eclipse project) for the {@link ClassFileLoader}.
  // * @param type
  // * the type of the source. Possible values are {@link EcjAdapter#LIBRARY} and {@link EcjAdapter#PROJECT}.
  // * @param classpathEntries
  // * the class path entries for the {@link ClassFileLoader}.
  // *
  // * @return creates an new instance of type {@link ClassFileLoader}, that can load {@link ClassFile ClassFiles} from
  // an
  // * array of files (jar files or directories).
  // */
  // public static ClassFileLoader createClasspathClassFileLoader(File source, byte type, File[] classpathEntries) {
  // PerformanceLogging.start(ClassFileLoaderFactory.class, "createClasspathClassFileLoader-ohneSourcePath");
  // PerformanceLogging.start(ClassFileLoaderFactory.class, "createClasspathClassFileLoader-ohneSourcePath-" + source);
  // try {
  // return new ClasspathClassFileLoaderImpl(source, type, classpathEntries);
  // } finally {
  // PerformanceLogging.stop(ClassFileLoaderFactory.class, "createClasspathClassFileLoader-ohneSourcePath");
  // PerformanceLogging.stop(ClassFileLoaderFactory.class, "createClasspathClassFileLoader-ohneSourcePath-" + source);
  // }
  // }

  /**
   * <p>
   * </p>
   * 
   * @param source
   * @param type
   * @param classpathEntries
   * @param sourcepathEntries
   * @return
   */
  public static ClassFileLoader createClasspathClassFileLoader( File source, byte type, List<File> classpathEntries, List<File> sourcepathEntries ) {
    ClassFileLoaderCacheKey cacheKey = new ClassFileLoaderCacheKey( source, type, classpathEntries, sourcepathEntries );

    // Try to get already initialized ClassFileLoader from cache
    ClassFileLoader classFileLoader = A4ECore.instance().getRuntimeValue( cacheKey );
    if( classFileLoader == null ) {
      // Create new ClassFileLoader
      classFileLoader = new ClasspathClassFileLoaderImpl( source, type, classpathEntries, sourcepathEntries );

      // add ClassFileLoader to Cache
      A4ECore.instance().instance().putRuntimeValue( cacheKey, classFileLoader );
    }

    // Return the ClassFileLoader
    return classFileLoader;
  }

  private static class ClassFileLoaderCacheKey {
    
    private File          _source;
    private byte          _type;
    private List<File>    _classpathEntries;
    private List<File>    _sourcepathEntries;

    public ClassFileLoaderCacheKey( File source, byte type, List<File> classpathEntries, List<File> sourcepathEntries ) {
      super();
      _source = source;
      _type = type;
      _classpathEntries = classpathEntries;
      _sourcepathEntries = sourcepathEntries;
    }

    @Override
    public int hashCode() {
      final int prime = 31;
      int result = 1;
      result = prime * result + Arrays.hashCode( _classpathEntries.toArray() );
      result = prime * result + ((_source == null) ? 0 : _source.hashCode());
      result = prime * result + Arrays.hashCode( _sourcepathEntries.toArray() );
      result = prime * result + _type;
      return result;
    }

    @Override
    public boolean equals( Object obj ) {
      if( this == obj ) {
        return true;
      }
      if( obj == null ) {
        return false;
      }
      if( getClass() != obj.getClass() ) {
        return false;
      }
      ClassFileLoaderCacheKey other = (ClassFileLoaderCacheKey) obj;
      if( !Arrays.equals( _classpathEntries.toArray(), other._classpathEntries.toArray() ) ) {
        return false;
      }
      if( _source == null ) {
        if( other._source != null ) {
          return false;
        }
      } else if( !_source.equals( other._source ) ) {
        return false;
      }
      if( !Arrays.equals( _sourcepathEntries.toArray(), other._sourcepathEntries.toArray() ) ) {
        return false;
      }
      if( _type != other._type ) {
        return false;
      }
      return true;
    }

    @Override
    public String toString() {
      return "ClassFileLoaderCacheKey [_source=" + _source + ", _type=" + _type + ", _classpathEntries="
          + Utilities.toString( _classpathEntries ) + ", _sourcepathEntries="
          + Utilities.toString( _sourcepathEntries ) + "]";
    }

  }

  // /**
  // * <p>
  // * </p>
  // *
  // * @param classPathEntry
  // * @param type
  // * @param sourcePathEntry
  // * @return
  // */
  // public static ClassFileLoader createClasspathClassFileLoader(File classPathEntry, byte type, File sourcePathEntry)
  // {
  // PerformanceLogging.start(ClassFileLoaderFactory.class, "createClasspathClassFileLoader-ohneBinaryPath");
  // PerformanceLogging.start(ClassFileLoaderFactory.class, "createClasspathClassFileLoader-ohneBinaryPath-"
  // + classPathEntry);
  // try {
  // return new ClasspathClassFileLoaderImpl(classPathEntry, type, sourcePathEntry);
  // } finally {
  // PerformanceLogging.stop(ClassFileLoaderFactory.class, "createClasspathClassFileLoader-ohneBinaryPath");
  // PerformanceLogging.stop(ClassFileLoaderFactory.class, "createClasspathClassFileLoader-ohneBinaryPath-"
  // + classPathEntry);
  // }
  // }

  /**
   * <p>
   * Creates an new instance of type {@link ClassFileLoader}, that can load {@link ClassFile ClassFiles} from a jar file
   * or directory.
   * </p>
   * 
   * @param entry
   *          the class path entry for the {@link ClassFileLoader}.
   * @param type
   *          the type of the source. Possible values are {@link EcjAdapter#LIBRARY} and {@link EcjAdapter#PROJECT}.
   * 
   * @return a new instance of type {@link ClassFileLoader}.
   */
  public static ClassFileLoader createClasspathClassFileLoader( File entry, byte type ) {
    String cacheKey = String.valueOf( entry ) + "/" + type;
    // Try to get ClassFileLoader from cache
    ClassFileLoader classFileLoader = A4ECore.instance().getRuntimeValue( cacheKey );
    if( classFileLoader == null ) {
      // Create new ClassFileLoader
      classFileLoader = new ClasspathClassFileLoaderImpl( entry, type );

      // add to cache
      A4ECore.instance().putRuntimeValue( cacheKey, classFileLoader );
    }

    return classFileLoader;
  }

  /**
   * <p>
   * Creates an new instance of type {@link ClassFileLoader}, that can load classes from multiple underlying class file
   * loaders.
   * </p>
   * 
   * @param classFileLoaders
   *          the class file loaders that should be contained in the compound class file loader.
   * @return an new instance of type {@link ClassFileLoader}, that can load classes from multiple underlying class file
   *         loaders.
   */
  public static ClassFileLoader createCompoundClassFileLoader( ClassFileLoader[] classFileLoaders ) {
    return new CompoundClassFileLoaderImpl( classFileLoaders );
  }

  /**
   * <p>
   * Creates an new instance of type {@link ClassFileLoader}, that can filter the access to classes in an underlying
   * class file loader.
   * </p>
   * 
   * @param classFileLoader
   *          the underlying class file loader
   * @param filter
   *          the filter
   * @return the class file loader
   */
  public static ClassFileLoader createFilteringClassFileLoader( ClassFileLoader classFileLoader, String filter ) {
    return new FilteringClassFileLoader( classFileLoader, filter );
  }

} /* ENDCLASS */
