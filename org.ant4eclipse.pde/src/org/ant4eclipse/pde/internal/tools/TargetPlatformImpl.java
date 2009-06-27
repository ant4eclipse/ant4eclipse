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

import org.ant4eclipse.core.logging.A4ELogging;
import org.ant4eclipse.core.util.Utilities;
import org.ant4eclipse.pde.model.pluginproject.BundleSource;
import org.ant4eclipse.pde.tools.TargetPlatform;
import org.ant4eclipse.pde.tools.TargetPlatformConfiguration;
import org.eclipse.osgi.service.resolver.BundleDescription;
import org.eclipse.osgi.service.resolver.ResolverError;
import org.eclipse.osgi.service.resolver.State;
import org.eclipse.osgi.service.resolver.StateObjectFactory;

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
  private final BundleSet     _pluginProjectSet;

  /** contains a list of all the binary bundle sets that belong to this target location */
  private List<BundleSet>     _binaryBundleSets;

  /** - */
  TargetPlatformConfiguration _configuration;

  /** - */
  private State               _state;

  /**
   * <p>
   * Creates a new instance of type {@link TargetPlatformImpl}.
   * </p>
   * 
   * @param pluginProjectSet
   *          the bundle set that contains the plug-in projects
   * @param binaryBundleSets
   *          the binary bundle sets that belong to this target location
   */
  public TargetPlatformImpl(final BundleSet pluginProjectSet, final BundleSet[] binaryBundleSets,
      final TargetPlatformConfiguration configuration) {
    this._pluginProjectSet = pluginProjectSet;

    if (binaryBundleSets != null) {
      this._binaryBundleSets = Arrays.asList(binaryBundleSets);
    } else {
      this._binaryBundleSets = new LinkedList<BundleSet>();
    }

    this._configuration = configuration;

    initialize();
  }

  /**
   * <p>
   * Creates a new instance of type TargetPlatform.
   * </p>
   * 
   * @param pluginProjectSet
   *          the bundle set that contains the plug-in projects
   * @param binaryPluginSet
   *          the binary bundle sets that belong to this target location
   */
  public TargetPlatformImpl(final BundleSet pluginProjectSet, final BundleSet binaryPluginSet,
      final TargetPlatformConfiguration configuration) {
    this(pluginProjectSet, (binaryPluginSet != null ? new BundleSet[] { binaryPluginSet } : null), configuration);
  }

  public State getState() {
    return this._state;
  }

  public TargetPlatformConfiguration getTargetPlatformConfiguration() {
    return this._configuration;
  }

  /**
   * {@inheritDoc}
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

  // /**
  // * {@inheritDoc}
  // */
  // public void invalidate() {
  // this._isInitialised = false;
  //
  // if (this._pluginProjectSet != null) {
  // this._pluginProjectSet.invalidate();
  // }
  //
  // for (final Iterator iterator = this._binaryBundleSets.iterator(); iterator.hasNext();) {
  // ((BundleSet) iterator.next()).invalidate();
  // }
  // }

  /**
   * {@inheritDoc}
   */
  public void refresh() {

    this._state = null;
    initialize();
  }

  /**
   * {@inheritDoc}
   */
  private BundleDescription[] getAllBundleDescriptions(final boolean preferProjects) {

    final List<BundleDescription> result = new LinkedList<BundleDescription>();

    if (this._pluginProjectSet != null) {
      final BundleDescription[] descriptions = this._pluginProjectSet.getAllBundleDescriptions();
      for (int i = 0; i < descriptions.length; i++) {
        final BundleDescription description = descriptions[i];
        result.add(description);
      }
    }

    for (final Iterator<BundleSet> iterator = this._binaryBundleSets.iterator(); iterator.hasNext();) {
      final BundleSet bundleSet = iterator.next();

      final BundleDescription[] descriptions = bundleSet.getAllBundleDescriptions();

      for (int i = 0; i < descriptions.length; i++) {
        final BundleDescription description = descriptions[i];
        if ((this._pluginProjectSet != null) && preferProjects
            && this._pluginProjectSet.contains(description.getSymbolicName())) {
          // TODO: WARNING AUSGEBEN?
        } else {
          result.add(description);
        }
      }
    }

    return result.toArray(new BundleDescription[result.size()]);
  }

  /**
   * {@inheritDoc}
   */
  private State resolve() {

    // create new state
    final State state = StateObjectFactory.defaultFactory.createState(true);

    // add all bundle descriptions to the state
    final BundleDescription[] descriptions = getAllBundleDescriptions(this._configuration.isPreferProjects());
    for (int i = 0; i < descriptions.length; i++) {
      final BundleDescription description = descriptions[i];
      final BundleDescription copy = StateObjectFactory.defaultFactory.createBundleDescription(description);
      copy.setUserObject(description.getUserObject());
      if (!state.addBundle(copy)) {
        throw new RuntimeException("Could not add bundle '" + description + "' to state!");
      }
      if (A4ELogging.isTraceingEnabled()) {
        A4ELogging.trace("Copied bundle to state: '%s'", getBundleInfo(description));
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