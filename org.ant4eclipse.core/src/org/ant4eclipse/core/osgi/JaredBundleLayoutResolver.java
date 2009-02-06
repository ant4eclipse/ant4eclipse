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
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.jar.JarFile;
import java.util.jar.Manifest;

import org.ant4eclipse.core.Assert;
import org.ant4eclipse.core.logging.A4ELogging;
import org.ant4eclipse.core.util.JarUtilities;
import org.ant4eclipse.core.util.ManifestHelper;

/**
 * <p>
 * Implements a {@link BundleLayoutResolver} for jared bundles.
 * </p>
 *
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 */
public class JaredBundleLayoutResolver implements BundleLayoutResolver {

  /** the location */
  private final File     _location;

  /** the expansion directory */
  private final File     _expansionDirectory;

  /** the jar file */
  private final JarFile  _jarFile;

  /** the manifest */
  private final Manifest _manifest;

  /**
   * <p>
   * Creates a new instance of type {@link JaredBundleLayoutResolver}.
   * </p>
   *
   * @param location
   *          the location of the jar file
   * @param expansionDirectory
   *          the expansion directory
   */
  public JaredBundleLayoutResolver(final File location, final File expansionDirectory) {
    Assert.isFile(location);
    Assert.notNull(expansionDirectory);

    this._location = location;
    this._expansionDirectory = expansionDirectory;

    try {
      this._jarFile = new JarFile(this._location);
      this._manifest = this._jarFile.getManifest();
    } catch (final IOException e) {
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
   * @see net.sf.ant4eclipse.tools.core.osgi.BundleLayoutResolver#getManifest()
   */
  public Manifest getManifest() {
    return this._manifest;
  }

  /**
   * @see net.sf.ant4eclipse.tools.core.osgi.BundleLayoutResolver#resolveBundleClasspathEntries()
   */
  public File[] resolveBundleClasspathEntries() {

    // expand if necessary
    if (needExpansion()) {
      return expand();
    }

    // return 'self'
    return new File[] { this._location };
  }

  /**
   * <p>
   * Returns if the contained jar file has to be unwrapped or not.
   * </p>
   *
   * @return <code>true</code> if the contained jar file has to be unwrapped, <code>false</code> otherwise.
   */
  private boolean needExpansion() {

    // get the bundle class path
    final String[] bundleClasspath = ManifestHelper.getBundleClasspath(this._manifest);

    // check entries
    for (int i = 0; i < bundleClasspath.length; i++) {

      // avoid expanding for erroneous BundleClasspath-Entries
      if (!".".equals(bundleClasspath[i]) && (this._jarFile.getEntry(bundleClasspath[i]) != null)) {
        return true;
      }
    }

    // no additional entries found -> no need for expansion
    return false;
  }

  /**
   * <p>
   * Expands the jared bundle to the expansion directory and returns all files that belongs to the bundle class path.
   * </p>
   *
   * @return all files that belongs to the bundle class path.
   */
  private File[] expand() {

    // compute destination
    final String symbolicName = ManifestHelper.getManifestHeader(this._manifest, ManifestHelper.BUNDLE_SYMBOLICNAME);
    final String version = ManifestHelper.getManifestHeader(this._manifest, ManifestHelper.BUNDLE_VERSION);
    final File destination = new File(this._expansionDirectory, symbolicName + "_" + version);

    // unwrap jar file
    try {
      JarUtilities.expandJarFile(this._jarFile, destination);
    } catch (final IOException e) {
      // log error
      A4ELogging.error("Could not expand jar file '%s'. Reason: '%s'", new Object[] { this._location, e.getMessage() });

      // return 'self'
      return new File[] { this._location };
    }

    // prepare results
    final List<File> result = new LinkedList<File>();

    // get bundle class path
    final String[] bundleClasspathEntries = ManifestHelper.getBundleClasspath(this._manifest);

    // add class path entries to the result
    for (int i = 0; i < bundleClasspathEntries.length; i++) {

      // add 'self'
      if (".".equals(bundleClasspathEntries[i])) {
        result.add(destination);
      }
      // add entry
      else {
        final File classpathEntry = new File(destination, bundleClasspathEntries[i]);
        if (classpathEntry.exists()) {
          result.add(classpathEntry);
        }
      }
    }

    // return result
    return (File[]) result.toArray(new File[0]);
  }
}
