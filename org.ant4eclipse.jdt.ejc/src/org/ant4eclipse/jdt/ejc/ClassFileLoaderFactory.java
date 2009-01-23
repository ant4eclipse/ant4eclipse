/**********************************************************************
 * Copyright (c) 2005-2008 ant4eclipse project team.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Nils Hartmann, Daniel Kasmeroglu, Gerd Wuetherich
 **********************************************************************/
package org.ant4eclipse.jdt.ejc;

import java.io.File;

import org.ant4eclipse.jdt.ejc.internal.tools.loader.ClasspathClassFileLoaderImpl;
import org.ant4eclipse.jdt.ejc.internal.tools.loader.CompoundClassFileLoaderImpl;
import org.ant4eclipse.jdt.ejc.internal.tools.loader.FilteringClassFileLoader;

/**
 * <p>
 * Provides static factory methods to create class file finders.
 * </p>
 * 
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 */
public class ClassFileLoaderFactory {

  /**
   * <p>
   * Creates an new instance of type {@link ClassFileLoader}, that can load {@link ClassFile ClassFiles} from an array
   * of files (jar files or directories).
   * </p>
   * 
   * @param source
   *          the file, that represents the source (e.g. a jar file, the root directory of an "exploded" bundle or the
   *          root directory of an eclipse project) for the {@link ClassFileLoader}.
   * @param type
   *          the type of the source. Possible values are {@link EjcAdapter#LIBRARY} and {@link EjcAdapter#PROJECT}.
   * @param classpathEntries
   *          the class path entries for the {@link ClassFileLoader}.
   * 
   * @return creates an new instance of type {@link ClassFileLoader}, that can load {@link ClassFile ClassFiles} from an
   *         array of files (jar files or directories).
   */
  public static ClassFileLoader createClasspathClassFileLoader(final File source, final byte type,
      final File[] classpathEntries) {
    return new ClasspathClassFileLoaderImpl(source, type, classpathEntries);
  }

  public static ClassFileLoader createClasspathClassFileLoader(final File entry, final byte type) {
    return new ClasspathClassFileLoaderImpl(entry, type);
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
  public static ClassFileLoader createCompoundClassFileLoader(final ClassFileLoader[] classFileLoaders) {
    return new CompoundClassFileLoaderImpl(classFileLoaders);
  }

  public static ClassFileLoader createFilteringClassFileLoader(final ClassFileLoader classFileLoader, String filter) {
    return new FilteringClassFileLoader(classFileLoader, filter);
  }
}
