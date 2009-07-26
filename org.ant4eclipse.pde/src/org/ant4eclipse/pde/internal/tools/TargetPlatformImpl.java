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
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;

import org.ant4eclipse.core.Assert;
import org.ant4eclipse.core.logging.A4ELogging;
import org.ant4eclipse.core.util.Utilities;
import org.ant4eclipse.pde.model.pluginproject.BundleSource;
import org.ant4eclipse.pde.tools.TargetPlatform;
import org.ant4eclipse.pde.tools.TargetPlatformConfiguration;
import org.eclipse.osgi.service.resolver.BundleDescription;
import org.eclipse.osgi.service.resolver.ResolverError;
import org.eclipse.osgi.service.resolver.State;
import org.eclipse.osgi.service.resolver.StateObjectFactory;

import javax.security.auth.login.Configuration;

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
  private final BundleSet             _pluginProjectSet;

  /** contains a list of all the binary bundle sets that belong to this target location */
  private List<BundleSet>             _binaryBundleSets;

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
   *          the {@link Configuration} of this target platform
   */
  public TargetPlatformImpl(final BundleSet pluginProjectSet, final BundleSet[] binaryBundleSets,
      final TargetPlatformConfiguration configuration) {
    Assert.notNull(configuration);

    // set the plug-in project set
    this._pluginProjectSet = pluginProjectSet;

    // set the binary bundle sets
    if (binaryBundleSets != null) {
      this._binaryBundleSets = Arrays.asList(binaryBundleSets);
    } else {
      this._binaryBundleSets = new LinkedList<BundleSet>();
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
   *          the {@link Configuration} of this target platform
   */
  public TargetPlatformImpl(final BundleSet pluginProjectSet, final BundleSet binaryPluginSet,
      final TargetPlatformConfiguration configuration) {

    // delegate
    this(pluginProjectSet, (binaryPluginSet != null ? new BundleSet[] { binaryPluginSet } : null), configuration);
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

      for (final Iterator<BundleSet> iterator = this._binaryBundleSets.iterator(); iterator.hasNext();) {
        final BundleSet bundleSet = iterator.next();
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
      result.addAll(Arrays.asList(this._pluginProjectSet.getAllBundleDescriptions()));
    }

    // step 3: add bundles from binary bundle sets to the result
    for (BundleSet binaryBundleSet : _binaryBundleSets) {

      for (BundleDescription bundleDescription : binaryBundleSet.getAllBundleDescriptions()) {
        if ((this._pluginProjectSet != null) && preferProjects
            && this._pluginProjectSet.contains(bundleDescription.getSymbolicName())) {
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
    for (int i = 0; i < bundleDescriptions.length; i++) {
      final BundleDescription description = bundleDescriptions[i];
      final ResolverError[] errors = state.getResolverErrors(description);
      if (!description.isResolved() || ((errors != null) && (errors.length != 0))) {
        if ((errors != null) && (errors.length == 1) && (errors[0].getType() == ResolverError.SINGLETON_SELECTION)) {
          A4ELogging.warn("Not using '%s' -- another version resolved", getBundleInfo(description));
        } else {
          // allStatesResolved = false;
          A4ELogging.warn("Could not resolve '%s':", getBundleInfo(description));
          for (int j = 0; j < errors.length; j++) {
            final ResolverError error = errors[j];
            A4ELogging.warn("  %s", error);
          }
        }
      }
    }

    // fail on non resolved bundles
    // if ((!allStatesResolved) && this._configuration.failOnNonResolvedBundles()) {
    // // TODO: A4E-Exception
    // throw new RuntimeException("Could not resolve all bundles");
    // }

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