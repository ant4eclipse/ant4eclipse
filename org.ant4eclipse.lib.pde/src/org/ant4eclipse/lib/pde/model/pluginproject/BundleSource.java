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
package org.ant4eclipse.lib.pde.model.pluginproject;

import org.ant4eclipse.lib.core.Assure;
import org.ant4eclipse.lib.platform.model.resource.EclipseProject;
import org.eclipse.osgi.service.resolver.BundleDescription;
import org.eclipse.osgi.util.ManifestElement;
import org.osgi.framework.Constants;

import java.io.File;
import java.util.jar.Manifest;

/**
 * The {@link BundleSource} wraps the source of a bundle (e.g. an eclipse plug-in project, a jared bundle or an exploded
 * bundle).
 * 
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 */
public class BundleSource {

  /**
   * <p>
   * Extracts the {@link BundleSource} for the given {@link BundleDescription}. If the bundle source is not set, a
   * {@link RuntimeException} will be thrown.
   * </p>
   * 
   * @param bundleDescription
   *          the bundle description.
   * @return the bundle source.
   */
  public static BundleSource getBundleSource(BundleDescription bundleDescription) {
    Assure.notNull("bundleDescription", bundleDescription);

    // retrieve the user object (that is always an instance of type BundleSource in our case)
    BundleSource bundleSource = (BundleSource) bundleDescription.getUserObject();

    // throw RuntimeException if the bundle source is not set
    if (bundleSource == null) {
      throw new RuntimeException("Bundle '" + bundleDescription + "' doesn't have a user object!");
    }

    // return result
    return bundleSource;
  }

  /** the bundle source */
  private Object   _source;

  /** The properties read from the manifest (=headers) */
  private Manifest _bundleManifest;

  /** The file or directory to load classes from. This might point to a directory. */
  private File     _classpathRoot;

  /**
   * <p>
   * Creates a new instance of type {@link BundleSource}.
   * </p>
   * 
   * @param source
   *          the bundle source (e.g. an eclipse plug-in project, a jared bundle or an exploded bundle)
   * @param bundleManifest
   *          the bundle manifest
   */
  public BundleSource(Object source, Manifest bundleManifest) {
    Assure.notNull("source", source);
    Assure.notNull("bundleManifest", bundleManifest);

    this._source = source;
    this._bundleManifest = bundleManifest;
    if (source instanceof File) {
      this._classpathRoot = (File) source;
    } else if (source instanceof EclipseProject) {
      this._classpathRoot = ((EclipseProject) source).getFolder();
    }
  }

  /**
   * <p>
   * Returns the bundle source. The result type can be {@link EclipseProject} or {@link File}.
   * </p>
   * 
   * @return the bundle source.
   */
  public Object getSource() {
    return this._source;
  }

  /**
   * <p>
   * Returns <code>true</code>, if the bundle source is of type {@link EclipseProject}.
   * </p>
   * 
   * @return <code>true</code>, if the bundle source is of type {@link EclipseProject}.
   */
  public boolean isEclipseProject() {
    return this._source instanceof EclipseProject;
  }

  /**
   * <p>
   * Returns the bundle source as an {@link EclipseProject}. If the bundle source is not an instance of type
   * {@link EclipseProject}, a {@link RuntimeException} will be thrown.
   * </p>
   * 
   * @return the bundle source.
   */
  public EclipseProject getAsEclipseProject() {
    Assure.assertTrue(isEclipseProject(), "Bundle source has to be instance of Eclipse Project");

    return (EclipseProject) this._source;
  }

  /**
   * <p>
   * Returns the bundle source as an {@link File}. If the bundle source is not an instance of type {@link File}, a
   * {@link RuntimeException} will be thrown.
   * </p>
   * 
   * @return the bundle source.
   */
  public File getAsFile() {
    Assure.assertTrue(this._source instanceof File, "Bundle source has to be instance of File");

    return (File) this._source;
  }

  /**
   * <p>
   * Returns the bundle manifest.
   * </p>
   * 
   * @return the bundle manifest.
   */
  public Manifest getBundleManifest() {
    return this._bundleManifest;
  }

  /**
   * <p>
   * Returns <code>true</code> if a class path root has already been set. If this methods returns <code>false</code> it
   * indicates that the {@link JarUtilities} hasn't run on this bundle yet.
   * </p>
   * 
   * @return <code>true</code> if a class path root has already been set.
   */
  public boolean hasClasspathRoot() {
    return this._classpathRoot != null;
  }

  /**
   * <p>
   * Returns the class path root or <code>null</code>. The class path root is set through the {@link JarUtilities}.
   * </p>
   * 
   * @return the class path root or <code>null</code>.
   */
  public File getClasspathRoot() {
    return this._classpathRoot;
  }

  /**
   * <p>
   * Sets the class path root.
   * </p>
   * <p>
   * Note: this method should only be called through the {@link JarUtilities}.
   * </p>
   * 
   * @param classpathRoot
   *          the class path root
   */
  public void setClasspathRoot(File classpathRoot) {
    Assure.notNull("classpathRoot", classpathRoot);
    Assure.assertTrue(!hasClasspathRoot(), "Classpath root already set!");

    this._classpathRoot = classpathRoot;
  }

  /**
   * <p>
   * Returns the 'Bundle-Classpath' entries or <code>.</code> if no 'Bundle-Classpath' has been specified (default
   * value).
   * </p>
   * 
   * @return the 'Bundle-Classpath' entries or <code>.</code> if no 'Bundle-Classpath' has been specified.
   */
  public String[] getBundleClasspath() {

    // parse the 'Bundle-Classpath' manifest entry
    String[] bundleClasspath = ManifestElement.getArrayFromList(this._bundleManifest.getMainAttributes().getValue(
        Constants.BUNDLE_CLASSPATH));

    // set default if necessary
    if ((bundleClasspath == null) || (bundleClasspath.length < 1)) {
      bundleClasspath = new String[] { "." };
    }

    // return result
    return bundleClasspath;
  }

  /**
   * <p>
   * Returns the name of the bundle as specified in the 'Bundle-Name' header or the bundles Symbolic-Name if no
   * 'Bundle-Name' header is specified.
   * </p>
   * 
   * @return the name of the bundle as specified in the 'Bundle-Name' header or the bundles Symbolic-Name if no
   *         'Bundle-Name' header is specified.
   */
  public String getBundleName() {

    // retrieve the bundle name
    String bundleName = this._bundleManifest.getMainAttributes().getValue(Constants.BUNDLE_NAME);

    // retrieve the bundle symbolic name if necessary
    if (bundleName == null) {
      bundleName = this._bundleManifest.getMainAttributes().getValue(Constants.BUNDLE_SYMBOLICNAME);
    }

    // return result
    return bundleName;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String toString() {
    StringBuffer buffer = new StringBuffer();
    buffer.append("[BundleSource:");
    buffer.append(" _source: ");
    buffer.append(this._source);
    buffer.append(" _bundleManifest: ");
    buffer.append(this._bundleManifest);
    buffer.append(" _classpathRoot: ");
    buffer.append(this._classpathRoot);
    buffer.append("]");
    return buffer.toString();
  }
}