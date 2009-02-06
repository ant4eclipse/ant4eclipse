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
package org.ant4eclipse.core.osgi;

import java.io.File;
import java.io.FileInputStream;
import java.util.LinkedList;
import java.util.List;
import java.util.jar.Manifest;

import org.ant4eclipse.core.Assert;
import org.ant4eclipse.core.util.ManifestHelper;

/**
 * <p>
 * Implements a {@link BundleLayoutResolver} for exploded bundles.
 * </p>
 *
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 */
public class ExplodedBundleLayoutResolver implements BundleLayoutResolver {

  /** the location */
  private final File     _location;

  /** the manifest */
  private final Manifest _manifest;

  /**
   * <p>
   * Creates a new instances of type {@link ExplodedBundleLayoutResolver}.
   * </p>
   *
   * @param location
   *          the root directory of the exploded bundle.
   */
  public ExplodedBundleLayoutResolver(final File location) {
    Assert.isDirectory(location);

    // set the location
    this._location = location;

    // Get manifest for exploded bundle
    final File manifestFile = new File(location, "META-INF/MANIFEST.MF");
    try {
      this._manifest = new Manifest(new FileInputStream(manifestFile));
    } catch (final Exception e) {
      throw new RuntimeException(e);
    }
  }

  /**
   * @see net.sf.ant4eclipse.tools.core.osgi.BundleLayoutResolver#getType()
   */
  public byte getType() {
    return LIBRARY;
  }

  /**
   * @see net.sf.ant4eclipse.tools.core.osgi.BundleLayoutResolver#getLocation()
   */
  public File getLocation() {
    return this._location;
  }

  /**
   * @see net.sf.ant4eclipse.tools.core.osgi.BundleLayoutResolver#getManifest(java.io.File)
   */
  public Manifest getManifest() {
    return this._manifest;
  }

  /**
   * <p>
   * </p>
   *
   * @see net.sf.ant4eclipse.tools.core.osgi.BundleLayoutResolver#resolveBundleClasspathEntries(java.io.File)
   */
  public File[] resolveBundleClasspathEntries() {

    // prepare results
    final List<File> result = new LinkedList<File>();

    // get bundle class path
    final String[] bundleClasspathEntries = ManifestHelper.getBundleClasspath(this._manifest);

    // add class path entries to the result
    for (int i = 0; i < bundleClasspathEntries.length; i++) {

      // add 'self'
      if (".".equals(bundleClasspathEntries[i])) {
        result.add(this._location);
      }
      // add entry
      else {
        final File classpathEntry = new File(this._location, bundleClasspathEntries[i]);
        if (classpathEntry.exists()) {
          result.add(classpathEntry);
        }
      }
    }

    // return result
    return (File[]) result.toArray(new File[0]);
  }
}
