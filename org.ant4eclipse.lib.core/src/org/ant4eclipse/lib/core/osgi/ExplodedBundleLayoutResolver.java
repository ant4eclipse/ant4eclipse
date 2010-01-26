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
import org.ant4eclipse.lib.core.util.ManifestHelper;

import java.io.File;
import java.io.FileInputStream;
import java.util.LinkedList;
import java.util.List;
import java.util.jar.Manifest;

/**
 * <p>
 * Implements a {@link BundleLayoutResolver} for exploded bundles.
 * </p>
 * 
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 */
public class ExplodedBundleLayoutResolver implements BundleLayoutResolver {

  /** the location */
  private File     _location;

  /** the manifest */
  private Manifest _manifest;

  /**
   * <p>
   * Creates a new instances of type {@link ExplodedBundleLayoutResolver}.
   * </p>
   * 
   * @param location
   *          the root directory of the exploded bundle.
   */
  public ExplodedBundleLayoutResolver(File location) {
    Assure.isDirectory(location);

    // set the location
    this._location = location;

    // Get manifest for exploded bundle
    File manifestFile = new File(location, "META-INF/MANIFEST.MF");
    try {
      this._manifest = new Manifest(new FileInputStream(manifestFile));
    } catch (Exception e) {
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

    // prepare results
    List<File> result = new LinkedList<File>();

    // get bundle class path
    String[] bundleClasspathEntries = ManifestHelper.getBundleClasspath(this._manifest);

    // add class path entries to the result
    for (String bundleClasspathEntrie : bundleClasspathEntries) {

      // add 'self'
      if (".".equals(bundleClasspathEntrie)) {
        result.add(this._location);
      }
      // add entry
      else {
        File classpathEntry = new File(this._location, bundleClasspathEntrie);
        if (classpathEntry.exists()) {
          result.add(classpathEntry);
        }
      }
    }

    // return result
    return result.toArray(new File[0]);
  }
}
