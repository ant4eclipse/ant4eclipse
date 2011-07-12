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

import java.io.File;
import java.util.Arrays;

import org.ant4eclipse.lib.core.util.PerformanceLogging;
import org.ant4eclipse.lib.jdt.ecj.internal.tools.loader.ClassFileLoaderCache;
import org.ant4eclipse.lib.jdt.ecj.internal.tools.loader.ClasspathClassFileLoaderImpl;
import org.ant4eclipse.lib.jdt.ecj.internal.tools.loader.CompoundClassFileLoaderImpl;
import org.ant4eclipse.lib.jdt.ecj.internal.tools.loader.FilteringClassFileLoader;

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
  public static ClassFileLoader createClasspathClassFileLoader(File source, byte type, File[] classpathEntries,
      File[] sourcepathEntries) {
    PerformanceLogging.start(ClassFileLoaderFactory.class, "createClasspathClassFileLoader-mitSourcePath");
    PerformanceLogging.start(ClassFileLoaderFactory.class, "createClasspathClassFileLoader-mitSourcePath-" + source);
    try {
      ClassFileLoaderCacheKey cacheKey = new ClassFileLoaderCacheKey(source, type, classpathEntries, sourcepathEntries);

      // Try to get already initialized ClassFileLoader from cache
      ClassFileLoader classFileLoader = ClassFileLoaderCache.getInstance().getClassFileLoader(cacheKey);
      if (classFileLoader == null) {
        // Create new ClassFileLoader
        classFileLoader = new ClasspathClassFileLoaderImpl(source, type, classpathEntries, sourcepathEntries);

        // add ClassFileLoader to Cache
        ClassFileLoaderCache.getInstance().storeClassFileLoader(cacheKey, classFileLoader);
  }

      // Return the ClassFileLoader
      return classFileLoader;
    } finally {
      // Stop performance logging
      PerformanceLogging.stop(ClassFileLoaderFactory.class, "createClasspathClassFileLoader-mitSourcePath");
      PerformanceLogging.stop(ClassFileLoaderFactory.class, "createClasspathClassFileLoader-mitSourcePath-" + source);
    }
  }

  private static class ClassFileLoaderCacheKey {
    private final File   _source;

    private final byte   _type;

    private final File[] _classpathEntries;

    private final File[] _sourcepathEntries;

    public ClassFileLoaderCacheKey(File source, byte type, File[] classpathEntries, File[] sourcepathEntries) {
      super();
      this._source = source;
      this._type = type;
      this._classpathEntries = classpathEntries;
      this._sourcepathEntries = sourcepathEntries;
    }

    @Override
    public int hashCode() {
      final int prime = 31;
      int result = 1;
      result = prime * result + Arrays.hashCode(this._classpathEntries);
      result = prime * result + ((this._source == null) ? 0 : this._source.hashCode());
      result = prime * result + Arrays.hashCode(this._sourcepathEntries);
      result = prime * result + this._type;
      return result;
    }

    @Override
    public boolean equals(Object obj) {
      if (this == obj) {
        return true;
      }
      if (obj == null) {
        return false;
      }
      if (getClass() != obj.getClass()) {
        return false;
      }
      ClassFileLoaderCacheKey other = (ClassFileLoaderCacheKey) obj;
      if (!Arrays.equals(this._classpathEntries, other._classpathEntries)) {
        return false;
      }
      if (this._source == null) {
        if (other._source != null) {
          return false;
        }
      } else if (!this._source.equals(other._source)) {
        return false;
      }
      if (!Arrays.equals(this._sourcepathEntries, other._sourcepathEntries)) {
        return false;
      }
      if (this._type != other._type) {
        return false;
      }
      return true;
    }

    @Override
    public String toString() {
      return "ClassFileLoaderCacheKey [_source=" + this._source + ", _type=" + this._type + ", _classpathEntries="
          + Arrays.toString(this._classpathEntries) + ", _sourcepathEntries="
          + Arrays.toString(this._sourcepathEntries) + "]";
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
  public static ClassFileLoader createClasspathClassFileLoader(File entry, byte type) {
    PerformanceLogging.start(ClassFileLoaderFactory.class, "createClasspathClassFileLoader-ohneAlles");
    PerformanceLogging.start(ClassFileLoaderFactory.class, "createClasspathClassFileLoader-ohneAlles-" + entry);
    try {
      String cacheKey = String.valueOf(entry) + "/" + type;
      // Try to get ClassFileLoader from cache
      ClassFileLoader classFileLoader = ClassFileLoaderCache.getInstance().getClassFileLoader(cacheKey);
      if (classFileLoader == null) {
        // Create new ClassFileLoader
        classFileLoader = new ClasspathClassFileLoaderImpl(entry, type);

        // add to cache
        ClassFileLoaderCache.getInstance().storeClassFileLoader(cacheKey, classFileLoader);
      }

      return classFileLoader;
    } finally {
      PerformanceLogging.stop(ClassFileLoaderFactory.class, "createClasspathClassFileLoader-ohneAlles");
      PerformanceLogging.stop(ClassFileLoaderFactory.class, "createClasspathClassFileLoader-ohneAlles-" + entry);
    }
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
  public static ClassFileLoader createCompoundClassFileLoader(ClassFileLoader[] classFileLoaders) {
    PerformanceLogging.start(ClassFileLoaderFactory.class, "createCompoundClassFileLoader");
    try {
    return new CompoundClassFileLoaderImpl(classFileLoaders);
    } finally {
      PerformanceLogging.stop(ClassFileLoaderFactory.class, "createCompoundClassFileLoader");
    }
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
  public static ClassFileLoader createFilteringClassFileLoader(ClassFileLoader classFileLoader, String filter) {
    PerformanceLogging.start(ClassFileLoaderFactory.class, "createFilteringClassFileLoader");
    try {
    return new FilteringClassFileLoader(classFileLoader, filter);
    } finally {
      PerformanceLogging.stop(ClassFileLoaderFactory.class, "createFilteringClassFileLoader");
    }
  }

}
