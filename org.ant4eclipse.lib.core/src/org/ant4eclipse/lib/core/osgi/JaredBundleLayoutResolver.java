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
package org.ant4eclipse.lib.core.osgi;


import org.ant4eclipse.lib.core.Assure;
import org.ant4eclipse.lib.core.CoreExceptionCode;
import org.ant4eclipse.lib.core.exception.Ant4EclipseException;
import org.ant4eclipse.lib.core.logging.A4ELogging;
import org.ant4eclipse.lib.core.util.ManifestHelper;
import org.ant4eclipse.lib.core.util.Utilities;
import org.ant4eclipse.lib.core.util.ManifestHelper.ManifestHeaderElement;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.jar.JarFile;
import java.util.jar.Manifest;

/**
 * <p>
 * Implements a {@link BundleLayoutResolver} for jared bundles.
 * </p>
 * 
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 */
public class JaredBundleLayoutResolver implements BundleLayoutResolver {

  /** the location */
  private File     _location;

  /** the expansion directory */
  private File     _expansionDirectory;

  /** the jar file */
  private JarFile  _jarFile;

  /** the manifest */
  private Manifest _manifest;

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
  public JaredBundleLayoutResolver(File location, File expansionDirectory) {
    Assure.isFile(location);
    Assure.notNull(expansionDirectory);

    this._location = location;
    this._expansionDirectory = expansionDirectory;

    try {
      this._jarFile = new JarFile(this._location);
      this._manifest = this._jarFile.getManifest();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  /**
   * {@inheritDoc}
   */
  public byte getType() {
    return LIBRARY;
  }

  /**
   * {@inheritDoc}
   */
  public File getLocation() {
    return this._location;
  }

  /**
   * {@inheritDoc}
   */
  public Manifest getManifest() {
    return this._manifest;
  }

  /**
   * {@inheritDoc}
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
    String[] bundleClasspath = ManifestHelper.getBundleClasspath(this._manifest);

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
    ManifestHeaderElement[] elements = ManifestHelper.getManifestHeaderElements(this._manifest,
        ManifestHelper.BUNDLE_SYMBOLICNAME);

    if (elements == null || elements.length == 0 || elements[0].getValues() == null
        || elements[0].getValues().length == 0) {
      // TODO: NLS
      throw new RuntimeException("Invalid header '" + ManifestHelper.BUNDLE_SYMBOLICNAME + "' in bundle '"
          + this._location + "'.");
    }

    String version = ManifestHelper.getManifestHeader(this._manifest, ManifestHelper.BUNDLE_VERSION);

    File destination = new File(this._expansionDirectory, elements[0].getValues()[0] + "_" + version);

    // unwrap jar file
    try {
      Utilities.expandJarFile(this._jarFile, destination);
    } catch (Ant4EclipseException ex) {
      if (ex.getExceptionCode() == CoreExceptionCode.IO_FAILURE) {
        // log error
        A4ELogging.error("Could not expand jar file '%s'. Reason: '%s'", this._location, ex.getMessage());
        // return 'self'
        return new File[] { this._location };
      } else {
        throw ex;
      }
    }

    // prepare results
    List<File> result = new LinkedList<File>();

    // get bundle class path
    String[] bundleClasspathEntries = ManifestHelper.getBundleClasspath(this._manifest);

    // add class path entries to the result
    for (String bundleClasspathEntrie : bundleClasspathEntries) {

      // add 'self'
      if (".".equals(bundleClasspathEntrie)) {
        result.add(destination);
      }
      // add entry
      else {
        File classpathEntry = new File(destination, bundleClasspathEntrie);
        if (classpathEntry.exists()) {
          result.add(classpathEntry);
        }
      }
    }

    // return result
    return result.toArray(new File[0]);
  }
}
