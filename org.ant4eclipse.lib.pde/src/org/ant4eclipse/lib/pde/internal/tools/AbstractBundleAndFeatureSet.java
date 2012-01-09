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

import org.ant4eclipse.lib.core.logging.A4ELogging;
import org.ant4eclipse.lib.pde.model.featureproject.FeatureManifest;
import org.ant4eclipse.lib.pde.tools.PdeBuildHelper;
import org.eclipse.osgi.service.resolver.BundleDescription;
import org.osgi.framework.Version;

import java.util.ArrayList;
import java.util.Iterator;
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
  // Assure.nonEmpty( "description", description );
  protected AbstractBundleAndFeatureSet( String description ) {
    _description = description;
    _bundleDescriptionList = new ArrayList<BundleDescription>();
    _featureDescriptionList = new ArrayList<FeatureDescription>();
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
  @Override
  public final void initialize() {

    // return if already initialized
    if( _isInitialised ) {
      return;
    }

    refresh();

    // set initialized
    _isInitialised = true;
  }

  /**
   * {@inheritDoc}
   */
  // Assure.nonEmpty( "featureId", featureId );
  // Assure.notNull( "version", version );
  @Override
  public FeatureDescription getFeatureDescription( String featureId, Version version ) {
    initialize();
    for( FeatureDescription featureDescription : _featureDescriptionList ) {
      if( featureDescription.getFeatureManifest().getId().equals( featureId )
          && PdeBuildHelper.resolveVersion( featureDescription.getFeatureManifest().getVersion(),
              PdeBuildHelper.getResolvedContextQualifier() ).equals( version ) ) {
        return featureDescription;
      }
    }
    return null;
  }

  /**
   * {@inheritDoc}
   */
  // Assure.no nEmpty( "featureId", featureId );
  @Override
  public FeatureDescription getFeatureDescription( String featureId ) {
    initialize();
    FeatureDescription result = null;
    for( FeatureDescription featureDescription : _featureDescriptionList ) {
      FeatureManifest featureManifest = featureDescription.getFeatureManifest();

      // if match -> set as result
      if( featureManifest.getId().equals( featureId ) ) {
        if( result == null ) {
          result = featureDescription;
        } else {
          // the current feature description has a higher version, so use this one
          if( result.getFeatureManifest().getVersion().compareTo( featureDescription.getFeatureManifest().getVersion() ) < 0 ) {
            result = featureDescription;
          }
        }
      }
    }

    return result;
  }

  /**
   * {@inheritDoc}
   */
  // Assure.nonEmpty( "bundleid", bundleid );
  @Override
  public BundleDescription getBundleDescription( String bundleid ) {
    initialize();
    BundleDescription result = null;
    for( BundleDescription bundleDescription : _bundleDescriptionList ) {

      // if match -> set as result
      if( bundleDescription.getSymbolicName().equals( bundleid ) ) {
        if( result == null ) {
          result = bundleDescription;
        } else {
          // the current feature description has a higher version, so use this one
          if( result.getVersion().compareTo( bundleDescription.getVersion() ) < 0 ) {
            result = bundleDescription;
          }
        }
      }
    }

    return result;

  }

  /**
   * {@inheritDoc}
   */
  @Override
  public final List<BundleDescription> getAllBundleDescriptions() {

    // initialize if necessary
    initialize();

    return _bundleDescriptionList;
  }

  /**
   * {@inheritDoc}
   */
  // Assure.notNull( "symbolicName", symbolicName );
  @Override
  public final boolean containsBundle( String symbolicName ) {
    initialize();
    Iterator<BundleDescription> iterator = _bundleDescriptionList.iterator();
    while( iterator.hasNext() ) {
      BundleDescription description = iterator.next();
      if( symbolicName.equals( description.getSymbolicName() )
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
  // Assure.notNull( "bundleDescription", bundleDescription );
  protected final void addBundleDescription( BundleDescription bundleDescription ) {
    _bundleDescriptionList.add( bundleDescription );
  }

  /**
   * <p>
   * Adds the given {@link FeatureDescription} to the {@link BundleAndFeatureSet}.
   * </p>
   * 
   * @param featureDescription
   *          the {@link FeatureDescription} to add
   */
  // Assure.notNull( "featureDescription", featureDescription );
  protected final void addFeaturesDescription( FeatureDescription featureDescription ) {
    _featureDescriptionList.add( featureDescription );
  }

  /**
   * <p>
   * Refreshes the {@link BundleAndFeatureSet}.
   * </p>
   */
  @Override
  public final void refresh() {

    // debug
    A4ELogging.info( "Trying to read bundles and feature from '%s'.", _description );

    // clear list of bundles...
    _bundleDescriptionList.clear();

    // clear list of features...
    _bundleDescriptionList.clear();

    // read all bundles and features...
    readBundlesAndFeatures();

    _isInitialised = true;

  }
  
} /* ENDCLASS */
