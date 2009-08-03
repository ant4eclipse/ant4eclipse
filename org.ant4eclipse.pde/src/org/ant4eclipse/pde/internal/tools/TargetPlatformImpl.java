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

import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.ant4eclipse.core.Assert;
import org.ant4eclipse.core.logging.A4ELogging;
import org.ant4eclipse.core.util.Pair;
import org.ant4eclipse.core.util.Utilities;

import org.ant4eclipse.pde.model.featureproject.FeatureManifest;
import org.ant4eclipse.pde.model.featureproject.FeatureManifest.Includes;
import org.ant4eclipse.pde.model.featureproject.FeatureManifest.Plugin;
import org.ant4eclipse.pde.model.pluginproject.BundleSource;
import org.ant4eclipse.pde.tools.ResolvedFeature;
import org.ant4eclipse.pde.tools.TargetPlatform;
import org.ant4eclipse.pde.tools.TargetPlatformConfiguration;

import org.apache.tools.ant.BuildException;
import org.eclipse.osgi.service.resolver.BundleDescription;
import org.eclipse.osgi.service.resolver.ResolverError;
import org.eclipse.osgi.service.resolver.State;
import org.eclipse.osgi.service.resolver.StateObjectFactory;
import org.osgi.framework.Version;

/**
 * <p>
 * A target platform contains different plug-in sets. It defines the target against which plug-ins will be compiled and
 * tested.
 * </p>
 * 
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 * @author Nils Hartmann (nils@nilshartmann.net)
 */
public final class TargetPlatformImpl implements TargetPlatform {

  /** the bundle set that contains the plug-in projects */
  private final BundleAndFeatureSet   _pluginProjectSet;

  /** contains a list of all the binary bundle sets that belong to this target location */
  private List<BundleAndFeatureSet>   _binaryBundleSets;

  /** the target platform configuration */
  private TargetPlatformConfiguration _configuration;

  /** the state object */
  private State                       _state;

  /**
   * <p>
   * Creates a new instance of type {@link TargetPlatformImpl}.
   * </p>
   * 
   * @param pluginProjectSet
   *          the set of all plug-in projects contained in the workspace, may <code>null</code>.
   * @param binaryBundleSets
   *          an array of bundle sets that contain the binary bundles, may <code>null</code>.
   * @param configuration
   *          the {@link TargetPlatformConfiguration} of this target platform
   */
  public TargetPlatformImpl(final BundleAndFeatureSet pluginProjectSet, final BundleAndFeatureSet[] binaryBundleSets,
      final TargetPlatformConfiguration configuration) {
    Assert.notNull(configuration);

    // set the plug-in project set
    this._pluginProjectSet = pluginProjectSet;

    // set the binary bundle sets
    if (binaryBundleSets != null) {
      this._binaryBundleSets = Arrays.asList(binaryBundleSets);
    } else {
      this._binaryBundleSets = new LinkedList<BundleAndFeatureSet>();
    }

    // set the configuration
    this._configuration = configuration;

    // initialize
    initialize();
  }

  /**
   * <p>
   * Creates a new instance of type {@link TargetPlatformImpl}.
   * </p>
   * 
   * @param pluginProjectSet
   *          the bundle set that contains the plug-in projects
   * @param binaryPluginSet
   *          the binary bundle sets that belong to this target location
   * @param configuration
   *          the {@link TargetPlatformConfiguration} of this target platform
   */
  public TargetPlatformImpl(final BundleAndFeatureSet pluginProjectSet, final BundleAndFeatureSet binaryPluginSet,
      final TargetPlatformConfiguration configuration) {

    // delegate
    this(pluginProjectSet, (binaryPluginSet != null ? new BundleAndFeatureSet[] { binaryPluginSet } : null),
        configuration);
  }

  /**
   * {@inheritDoc}
   */
  public State getState() {
    return this._state;
  }

  /**
   * {@inheritDoc}
   */
  public TargetPlatformConfiguration getTargetPlatformConfiguration() {
    return this._configuration;
  }

  /**
   * <p>
   * </p>
   */
  private void initialize() {
    if (this._state == null) {

      if (this._pluginProjectSet != null) {
        this._pluginProjectSet.initialize();
      }

      for (final Iterator<BundleAndFeatureSet> iterator = this._binaryBundleSets.iterator(); iterator.hasNext();) {
        final BundleAndFeatureSet bundleSet = iterator.next();
        bundleSet.initialize();
      }

      this._state = resolve();
    }
  }

  /**
   * <p>
   * </p>
   * 
   */
  public void refresh() {

    this._state = null;
    initialize();
  }

  /**
   * <p>
   * Returns a list with a {@link BundleDescription BundleDescriptions} of each bundle that is contained in the plug-in
   * project set or the binary bundle sets.
   * </p>
   * 
   * @param preferProjects
   *          indicates of plug-in projects should be preferred over binary bundles or not.
   * @return a list with a {@link BundleDescription BundleDescriptions} of each bundle that is contained in the plug-in
   *         project set or the binary bundle sets.
   */
  private List<BundleDescription> getAllBundleDescriptions(final boolean preferProjects) {

    // step 1: create the result list
    final List<BundleDescription> result = new LinkedList<BundleDescription>();

    // step 2: add plug-in projects from the plug-in projects list to the result
    if (this._pluginProjectSet != null) {
      result.addAll(this._pluginProjectSet.getAllBundleDescriptions());
    }

    // step 3: add bundles from binary bundle sets to the result
    for (BundleAndFeatureSet binaryBundleSet : _binaryBundleSets) {

      for (BundleDescription bundleDescription : binaryBundleSet.getAllBundleDescriptions()) {
        if ((this._pluginProjectSet != null) && preferProjects
            && this._pluginProjectSet.containsBundle(bundleDescription.getSymbolicName())) {
          // TODO: WARNING AUSGEBEN?
        } else {
          result.add(bundleDescription);
        }
      }
    }

    // step 4: return the result
    return result;
  }

  /**
   * {@inheritDoc}
   */
  public FeatureDescription getFeatureDescription(String id, Version version) {
    Assert.nonEmpty(id);
    Assert.notNull(version);

    // 
    FeatureDescription featureDescription = _pluginProjectSet.getFeatureDescription(id, version);

    // 
    if (featureDescription != null) {
      return featureDescription;
    }

    for (BundleAndFeatureSet bundleSet : _binaryBundleSets) {
      featureDescription = bundleSet.getFeatureDescription(id, version);
      if (featureDescription != null) {
        return featureDescription;
      }
    }

    //
    return null;
  }

  /**
   * {@inheritDoc}
   */
  public boolean hasFeatureDescription(String id, Version version) {
    return getFeatureDescription(id, version) != null;
  }

  /**
   * {@inheritDoc}
   */
  public FeatureDescription getFeatureDescription(String id) {
    Assert.nonEmpty(id);

    // 
    FeatureDescription featureDescription = _pluginProjectSet.getFeatureDescription(id);

    // 
    if (featureDescription != null) {
      return featureDescription;
    }

    // result
    FeatureDescription result = null;

    // iterate over feature descriptions
    for (BundleAndFeatureSet bundleSet : _binaryBundleSets) {

      // get the feature manifest
      featureDescription = bundleSet.getFeatureDescription(id);

      // if match -> set as result
      if (featureDescription != null && featureDescription.getFeatureManifest().getId().equals(id)) {
        if (result != null
            && result.getFeatureManifest().getVersion().compareTo(featureDescription.getFeatureManifest().getVersion()) < 0) {
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
  public boolean hasFeatureDescription(String id) {
    return getFeatureDescription(id) != null;
  }

  /**
   * {@inheritDoc}
   */
  public ResolvedFeature resolveFeature(Object source, FeatureManifest manifest) {
    Assert.notNull(manifest);

    ResolvedFeature resolvedFeature = new ResolvedFeature(source, manifest);

    resolvePlugins(manifest, resolvedFeature);

    resolveIncludes(manifest, resolvedFeature);

    // 6.3 return result
    return resolvedFeature;
  }

  /**
   * <p>
   * </p>
   * 
   * @param manifest
   * @param resolvedFeature
   */
  private void resolveIncludes(FeatureManifest manifest, ResolvedFeature resolvedFeature) {

    // TODO: DependencyGraph!!
    final List<Pair<Includes, FeatureDescription>> result = new LinkedList<Pair<Includes, FeatureDescription>>();

    for (Includes includes : manifest.getIncludes()) {

      FeatureDescription featureDescription = null;

      if (includes.getVersion().equals(Version.emptyVersion)) {
        featureDescription = getFeatureDescription(includes.getId());
      } else {
        featureDescription = getFeatureDescription(includes.getId(), includes.getVersion());
      }

      if (featureDescription == null) {
        // TODO: NLS
        throw new RuntimeException();
      } else {
        result.add(new Pair<Includes, FeatureDescription>(includes, featureDescription));
      }
    }

    resolvedFeature.setIncludesToFeatureDescriptionList(result);
  }

  /**
   * <p>
   * </p>
   *
   * @param manifest
   * @param resolvedFeature
   * @throws BuildException
   */
  private void resolvePlugins(FeatureManifest manifest, ResolvedFeature resolvedFeature) throws BuildException {
    // 4. Retrieve BundlesDescriptions for feature plug-ins
    final Map<BundleDescription, Plugin> map = new HashMap<BundleDescription, Plugin>();
    final List<BundleDescription> bundleDescriptions = new LinkedList<BundleDescription>();

    for (Plugin plugin : manifest.getPlugins()) {

      // if a plug-in reference uses a version, the exact version must be found in the workspace
      // if a plug-in reference specifies "0.0.0" as version, the newest plug-in found will be used
      BundleDescription bundleDescription = _state.getBundle(plugin.getId(), plugin.getVersion().equals(
          Version.emptyVersion) ? null : plugin.getVersion());

      // TODO: NLS
      if (bundleDescription == null) {
        throw new BuildException("Could not find bundle with id '" + plugin.getId() + "' and version '"
            + plugin.getVersion() + "' in workspace or target platform!");
      }

      // TODO: NLS
      Assert.assertTrue(bundleDescription.isResolved(), "bundle has to be resolved!");
      bundleDescriptions.add(bundleDescription);
      map.put(bundleDescription, plugin);
    }

    // 5. Sort the bundles
    final BundleDescription[] sortedbundleDescriptions = (BundleDescription[]) bundleDescriptions
        .toArray(new BundleDescription[0]);
    final Object[][] cycles = _state.getStateHelper().sortBundles(sortedbundleDescriptions);
    // warn on circular dependencies
    if ((cycles != null) && (cycles.length > 0)) {
      // TODO: better error messages
      A4ELogging.warn("Detected circular dependencies:");
      for (int i = 0; i < cycles.length; i++) {
        A4ELogging.warn(Arrays.asList(cycles[i]).toString());
      }
    }

    // 6.1 create result
    final List<Pair<Plugin, BundleDescription>> result = new LinkedList<Pair<Plugin, BundleDescription>>();
    for (BundleDescription bundleDescription : sortedbundleDescriptions) {
      final Pair<Plugin, BundleDescription> pair = new Pair<Plugin, BundleDescription>(map.get(bundleDescription),
          bundleDescription);
      result.add(pair);
    }

    resolvedFeature.setPluginToBundleDescptionList(result);
  }

  /**
   * <p>
   * </p>
   * 
   * @return
   */
  private State resolve() {

    // step 1: create new state
    final State state = StateObjectFactory.defaultFactory.createState(true);

    for (BundleDescription bundleDescription : getAllBundleDescriptions(this._configuration.isPreferProjects())) {
      final BundleDescription copy = StateObjectFactory.defaultFactory.createBundleDescription(bundleDescription);
      copy.setUserObject(bundleDescription.getUserObject());
      if (!state.addBundle(copy)) {
        // TODO: NLS
        throw new RuntimeException("Could not add bundle '" + bundleDescription + "' to state!");
      }
      if (A4ELogging.isTraceingEnabled()) {
        A4ELogging.trace("Copied bundle to state: '%s'", getBundleInfo(bundleDescription));
      }
    }

    // set the platform properties
    final Properties platformProperties = this._configuration.getConfigurationProperties();
    if (A4ELogging.isDebuggingEnabled()) {
      A4ELogging.debug(Utilities.toString("Initializing TargetPlatform with properties: ", platformProperties));
    }
    state.setPlatformProperties(platformProperties);

    // resolve the state
    state.resolve();

    // log errors if any
    final BundleDescription[] bundleDescriptions = state.getBundles();
    // boolean allStatesResolved = true;
    
    if (A4ELogging.isDebuggingEnabled()) {
      for (int i = 0; i < bundleDescriptions.length; i++) {
        final BundleDescription description = bundleDescriptions[i];
        final ResolverError[] errors = state.getResolverErrors(description);
        if (!description.isResolved() || ((errors != null) && (errors.length != 0))) {
          if ((errors != null) && (errors.length == 1) && (errors[0].getType() == ResolverError.SINGLETON_SELECTION)) {
            A4ELogging.debug("Not using '%s' -- another version resolved", getBundleInfo(description));
          } else {
            // allStatesResolved = false;
            A4ELogging.debug("Could not resolve '%s':", getBundleInfo(description));
            for (int j = 0; j < errors.length; j++) {
              final ResolverError error = errors[j];
              A4ELogging.debug("  %s", error);
            }
          }
        }
      }
    }
    // return the state
    return state;
  }

  /**
   * @param bundleDescription
   * @return
   */
  private static String getBundleInfo(final BundleDescription bundleDescription) {
    final BundleSource bundleSource = BundleSource.getBundleSource(bundleDescription);

    final StringBuffer buffer = new StringBuffer();
    buffer.append(bundleDescription.getSymbolicName()).append("_").append(bundleDescription.getVersion().toString())
        .append("@");
    if (bundleSource.isEclipseProject()) {
      buffer.append("<P>").append(bundleSource.getAsEclipseProject().getFolder());
    } else {
      buffer.append(bundleSource.getAsFile().getAbsolutePath());
    }
    return buffer.toString();
  }
}