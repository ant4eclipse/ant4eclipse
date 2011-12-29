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
package org.ant4eclipse.lib.pde.internal.tools;

import org.ant4eclipse.lib.core.Assure;
import org.ant4eclipse.lib.core.logging.A4ELogging;
import org.ant4eclipse.lib.pde.model.featureproject.FeatureManifest;
import org.ant4eclipse.lib.pde.tools.PdeBuildHelper;
import org.eclipse.osgi.service.resolver.BundleDescription;
import org.osgi.framework.Version;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

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
  private List<BundleDescription>  _bundleDescriptionList;

  /** the list that contains all the feature descriptions */
  private List<FeatureDescription> _featureDescriptionList;

  /** indicates whether or not the platform is initialized */
  private boolean                  _isInitialised = false;

  /** a description of this BundleAndFeatureSet */
  private String                   _description;

  /**
   * <p>
   * Creates a new instance of type {@link AbstractBundleAndFeatureSet}.
   * </p>
   * 
   * @param description
   *          a description of this {@link BundleAndFeatureSet}
   */
  protected AbstractBundleAndFeatureSet(String description) {
    Assure.nonEmpty("description", description);

    // set the description
    this._description = description;

    // create the bundle description list
    this._bundleDescriptionList = new LinkedList<BundleDescription>();

    // create the feature description list
    this._featureDescriptionList = new LinkedList<FeatureDescription>();
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

    refresh();

    // set initialized
    this._isInitialised = true;
  }

  /**
   * {@inheritDoc}
   */
  public FeatureDescription getFeatureDescription(String featureId, Version version) {
    Assure.nonEmpty("featureId", featureId);
    Assure.notNull("version", version);

    // initialize if necessary
    initialize();

    // iterate over feature list
    for (FeatureDescription featureDescription : this._featureDescriptionList) {

      // return if match
      if (featureDescription.getFeatureManifest().getId().equals(featureId)
          && PdeBuildHelper.resolveVersion(featureDescription.getFeatureManifest().getVersion(),
              PdeBuildHelper.getResolvedContextQualifier()).equals(version)) {

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
    Assure.nonEmpty("featureId", featureId);

    // initialize if necessary
    initialize();

    // result
    FeatureDescription result = null;

    // iterate over feature descriptions
    for (FeatureDescription featureDescription : this._featureDescriptionList) {

      // get the feature manifest
      FeatureManifest featureManifest = featureDescription.getFeatureManifest();

      // if match -> set as result
      if (featureManifest.getId().equals(featureId)) {
        if (result == null) {
          result = featureDescription;
        } else {
          // the current feature description has a higher version, so use this one
          if (result.getFeatureManifest().getVersion().compareTo(featureDescription.getFeatureManifest().getVersion()) < 0) {
            result = featureDescription;
          }
        }
      }
    }

    // return result
    return result;
  }

  /**
   * {@inheritDoc}
   */
  public BundleDescription getBundleDescription(String bundleid) {
    Assure.nonEmpty("bundleid", bundleid);

    // initialize if necessary
    initialize();

    // result
    BundleDescription result = null;

    for (BundleDescription bundleDescription : this._bundleDescriptionList) {

      // if match -> set as result
      if (bundleDescription.getSymbolicName().equals(bundleid)) {
        if (result == null) {
          result = bundleDescription;
        } else {
          // the current feature description has a higher version, so use this one
          if (result.getVersion().compareTo(bundleDescription.getVersion()) < 0) {
            result = bundleDescription;
          }
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

    return this._bundleDescriptionList;
  }

  /**
   * {@inheritDoc}
   */
  public final boolean containsBundle(String symbolicName) {
    Assure.notNull("symbolicName", symbolicName);

    // initialize if necessary
    initialize();

    Iterator<BundleDescription> iterator = this._bundleDescriptionList.iterator();

    while (iterator.hasNext()) {
      BundleDescription description = iterator.next();
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
  protected final void addBundleDescription(BundleDescription bundleDescription) {
    Assure.notNull("bundleDescription", bundleDescription);
    this._bundleDescriptionList.add(bundleDescription);
  }

  /**
   * <p>
   * Adds the given {@link FeatureDescription} to the {@link BundleAndFeatureSet}.
   * </p>
   * 
   * @param featureDescription
   *          the {@link FeatureDescription} to add
   */
  protected final void addFeaturesDescription(FeatureDescription featureDescription) {
    Assure.notNull("featureDescription", featureDescription);
    this._featureDescriptionList.add(featureDescription);
  }

  /**
   * <p>
   * Refreshes the {@link BundleAndFeatureSet}.
   * </p>
   */
  public final void refresh() {

    // debug
    A4ELogging.info("Trying to read bundles and feature from '%s'.", this._description);

    // clear list of bundles...
    this._bundleDescriptionList.clear();

    // clear list of features...
    this._bundleDescriptionList.clear();

    // read all bundles and features...
    readBundlesAndFeatures();

    this._isInitialised = true;

  }
}
