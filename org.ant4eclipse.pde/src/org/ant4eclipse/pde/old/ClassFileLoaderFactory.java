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
package org.ant4eclipse.pde.old;

import java.io.File;
import java.util.jar.Manifest;

import net.sf.ant4eclipse.core.Assert;
import net.sf.ant4eclipse.tools.core.ejc.internal.loader.ClasspathClassFileLoaderImpl;
import net.sf.ant4eclipse.tools.core.ejc.internal.loader.CompoundClassFileLoaderImpl;
import net.sf.ant4eclipse.tools.core.ejc.loader.ClassFileLoader;
import net.sf.ant4eclipse.tools.core.osgi.BundleLayoutResolver;

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
   * Create a new compound class file loader.
   * </p>
   *
   * @param classFileLoaders
   *          the class file loader that should be contained in the compound class file loader.
   * @return the new compound class file loader.
   */
  public static ClassFileLoader createCompoundClassFileLoader(final ClassFileLoader[] classFileLoaders) {
    return new CompoundClassFileLoaderImpl(classFileLoaders);
  }

  /**
   * @param manifest
   * @param classFileLoader
   * @return
   */
  public static ClassFileLoader createFilteringClassFileLoader(final Manifest manifest,
      final ClassFileLoader classFileLoader) {
    return new FilteredClasspathClassFileLoader(manifest, classFileLoader);
  }

  /**
   * @param manifests
   * @param classFileLoader
   * @return
   */
  public static ClassFileLoader createFilteringClassFileLoader(final Manifest[] manifests,
      final ClassFileLoader classFileLoader) {
    return new FilteredClasspathClassFileLoader(manifests, classFileLoader);
  }

  /**
   * @param bundleLayoutResolver
   * @return
   */
  public static ClassFileLoader createClasspathClassFileLoader(final BundleLayoutResolver bundleLayoutResolver) {
    Assert.notNull(bundleLayoutResolver);

    // get the source
    final File source = bundleLayoutResolver.getLocation();

    // resolve class path entries
    final File[] classpathEntries = bundleLayoutResolver.resolveBundleClasspathEntries();

    // get the type of the bundle
    final byte type = bundleLayoutResolver.getType();

    // return the new class file loader
    return new ClasspathClassFileLoaderImpl(source, type, classpathEntries);
  }
}
