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
package org.ant4eclipse.pde.internal.tools;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.ant4eclipse.core.Assert;
import org.ant4eclipse.core.logging.A4ELogging;
import org.ant4eclipse.core.util.StopWatch;

import org.ant4eclipse.pde.model.featureproject.FeatureManifest;

import org.eclipse.osgi.service.resolver.BundleDescription;
import org.osgi.framework.Version;

/**
 * <p>
 * Abstract base implementation for all {@link BundleAndFeatureSet BundleAndFeatureSets}.
 * </p>
 * 
 * @author Nils Hartmann (nils@nilshartmann.net)
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 */
public abstract class AbstractBundleAndFeatureSet implements BundleAndFeatureSet {

  /** the list that contains all the bundle descriptions */
  private final List<BundleDescription>  _bundleDescriptonList;

  /** the list that contains all the feature descriptions */
  private final List<FeatureDescription> _featureDescriptonList;

  /** indicates whether or not the platform is initialized */
  private boolean                        _isInitialised = false;

  /**
   * <p>
   * Creates a new instance of type {@link AbstractBundleAndFeatureSet}.
   * </p>
   */
  protected AbstractBundleAndFeatureSet() {

    // create the bundle description list
    this._bundleDescriptonList = new LinkedList<BundleDescription>();

    // create the feature description list
    this._featureDescriptonList = new LinkedList<FeatureDescription>();
  }

  /**
   * <p>
   * Abstract method. Must be implemented to read bundles and features from an underlying location (e.g. a directory
   * that contains bundles and features or a workspace that contains bundle projects and feature projects).
   * </p>
   */
  protected abstract void readBundlesAndFeatures();

  /**
   * {@inheritDoc}
   */
  public final void initialize() {

    // return if already initialized
    if (this._isInitialised) {
      return;
    }

    // create stop watch for debugging purpose
    StopWatch stopWatch = new StopWatch();

    // refresh
    stopWatch.start();
    refresh();
    stopWatch.stop();

    // debug
    if (A4ELogging.isDebuggingEnabled()) {
      A4ELogging.debug("Needed %s ms to read %s bundles and %s features from bundle set.", stopWatch.getElapsedTime(),
          this._bundleDescriptonList.size(), this._featureDescriptonList.size());
    }

    // set initialized
    this._isInitialised = true;
  }

  /**
   * {@inheritDoc}
   */
  public FeatureDescription getFeatureDescription(String featureId, Version version) {
    Assert.nonEmpty(featureId);
    Assert.notNull(version);

    // initialize if necessary
    initialize();

    // iterate over feature list
    for (FeatureDescription featureDescription : _featureDescriptonList) {

      // return if match
      if (featureDescription.getFeatureManifest().getId().equals(featureId)
          && featureDescription.getFeatureManifest().getVersion().equals(version)) {

        return featureDescription;
      }
    }

    // no Feature found -> return null
    return null;
  }

  /**
   * {@inheritDoc}
   */
  public FeatureDescription getFeatureDescription(String featureId) {
    Assert.nonEmpty(featureId);

    // initialize if necessary
    initialize();

    // result
    FeatureDescription result = null;

    // iterate over feature descriptions
    for (FeatureDescription featureDescription : _featureDescriptonList) {

      // get the feature manifest
      FeatureManifest featureManifest = featureDescription.getFeatureManifest();

      // if match -> set as result
      if (featureManifest.getId().equals(featureId)) {
        if (result != null && result.getFeatureManifest().getVersion().compareTo(featureManifest.getVersion()) < 0) {
          result = featureDescription;
        } else {
          result = featureDescription;
        }
      }
    }

    // return result
    return result;
  }

  /**
   * {@inheritDoc}
   */
  public final List<BundleDescription> getAllBundleDescriptions() {

    // initialize if necessary
    initialize();

    return this._bundleDescriptonList;
  }

  /**
   * {@inheritDoc}
   */
  public final boolean containsBundle(final String symbolicName) {
    Assert.notNull(symbolicName);

    // initialize if necessary
    initialize();

    final Iterator<BundleDescription> iterator = this._bundleDescriptonList.iterator();

    while (iterator.hasNext()) {
      final BundleDescription description = iterator.next();
      if (symbolicName.equals(description.getSymbolicName())
      /*
       * TODO || "system.bundle".equals(symbolicName) && description.isSystemBundle()
       */) {
        return true;
      }
    }

    return false;
  }

  /**
   * <p>
   * Adds the given {@link BundleDescription} to the {@link BundleAndFeatureSet}.
   * </p>
   * 
   * @param bundleDescription
   *          the {@link BundleDescription} to add.
   */
  protected final void addBundleDescription(final BundleDescription bundleDescription) {
    Assert.notNull(bundleDescription);

    this._bundleDescriptonList.add(bundleDescription);
  }

  /**
   * <p>
   * Adds the given {@link FeatureDescription} to the {@link BundleAndFeatureSet}.
   * </p>
   * 
   * @param featureDescription
   *          the {@link FeatureDescription} to add
   */
  protected final void addFeaturesDescription(final FeatureDescription featureDescription) {
    Assert.notNull(featureDescription);

    this._featureDescriptonList.add(featureDescription);
  }

  /**
   * <p>
   * Refreshes the {@link BundleAndFeatureSet}.
   * </p>
   */
  private final void refresh() {

    // clear list of bundles...
    this._bundleDescriptonList.clear();

    // clear list of features...
    this._bundleDescriptonList.clear();

    // read all bundles and features...
    readBundlesAndFeatures();

    this._isInitialised = true;
  }
}
