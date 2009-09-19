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
package org.ant4eclipse.pde.test.builder;

import org.ant4eclipse.core.util.Utilities;

import java.io.File;
import java.io.FileOutputStream;
import java.util.jar.Attributes;
import java.util.jar.Manifest;

/**
 * <p>
 * </p>
 * 
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 */
public class BundleManifest {

  private String _bundleName;

  private String _bundleSymbolicName;

  private String _bundleVersion                      = "0.0.0";

  private String _bundleRequiredExecutionEnvironment = "J2SE-1.5";

  private String _importPackage;

  private String _exportPackage;

  private String _fragmentHost;

  private String _classpath;

  /**
   * @param bundleSymbolicName
   */
  public BundleManifest(String bundleSymbolicName) {
    super();
    this._bundleSymbolicName = bundleSymbolicName;
    this._bundleName = this._bundleSymbolicName;
  }

  /**
   * @param bundleName
   *          the bundleName to set
   */
  public BundleManifest withBundleName(String bundleName) {
    this._bundleName = bundleName;
    return this;
  }

  /**
   * @param bundleVersion
   *          the bundleVersion to set
   */
  public BundleManifest withBundleVersion(String bundleVersion) {
    this._bundleVersion = bundleVersion;
    return this;
  }

  /**
   * @param bundleRequiredExecutionEnvironment
   *          the bundleRequiredExecutionEnvironment to set
   */
  public BundleManifest withBundleRequiredExecutionEnvironment(String bundleRequiredExecutionEnvironment) {
    this._bundleRequiredExecutionEnvironment = bundleRequiredExecutionEnvironment;
    return this;
  }

  /**
   * @param importPackage
   *          the importPackage to set
   */
  public BundleManifest withImportPackage(String importPackage) {
    this._importPackage = importPackage;
    return this;
  }

  /**
   * @param exportPackage
   *          the exportPackage to set
   */
  public BundleManifest withExportPackage(String exportPackage) {
    this._exportPackage = exportPackage;
    return this;
  }

  public BundleManifest withFragmentHost(String fragmentHost) {
    this._fragmentHost = fragmentHost;
    return this;
  }

  public BundleManifest withClassPath(String classPath) {
    this._classpath = classPath;
    return this;
  }

  public void write(File file) {

    Manifest manifest = getManifest();

    // Bundle-Version: 1.0.0
    // Bundle-Activator: test.Activator
    // Bundle-ActivationPolicy: lazy
    // Bundle-RequiredExecutionEnvironment: J2SE-1.5
    // 

    try {
      manifest.write(new FileOutputStream(file));
    } catch (Exception e) {
      e.printStackTrace();
      throw new RuntimeException(e);
    }
  }

  /**
   * <p>
   * </p>
   * 
   * @return
   */
  public Manifest getManifest() {
    Manifest manifest = new Manifest();
    Attributes attributes = manifest.getMainAttributes();

    attributes.putValue("Manifest-Version", "1.0");
    attributes.putValue("Bundle-ManifestVersion", "2");

    attributes.putValue("Bundle-SymbolicName", this._bundleSymbolicName);
    attributes.putValue("Bundle-Version", this._bundleVersion);

    if (Utilities.hasText(this._bundleName)) {
      attributes.putValue("Bundle-Name", this._bundleName);
    }

    if (Utilities.hasText(this._importPackage)) {
      attributes.putValue("Import-Package", this._importPackage);
    }

    if (Utilities.hasText(this._exportPackage)) {
      attributes.putValue("Export-Package", this._exportPackage);
    }

    if (Utilities.hasText(this._fragmentHost)) {
      attributes.putValue("Fragment-Host", this._fragmentHost);
    }

    if (Utilities.hasText(this._classpath)) {
      attributes.putValue("Bundle-ClassPath", this._classpath);
    }
    return manifest;
  }
}