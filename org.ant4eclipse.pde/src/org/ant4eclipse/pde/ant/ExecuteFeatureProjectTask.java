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
package org.ant4eclipse.pde.ant;

import org.ant4eclipse.core.Assert;
import org.ant4eclipse.core.logging.A4ELogging;

import org.ant4eclipse.pde.model.featureproject.FeatureManifest;
import org.ant4eclipse.pde.model.featureproject.FeatureProjectRole;
import org.ant4eclipse.pde.model.featureproject.FeatureManifest.Plugin;
import org.ant4eclipse.pde.model.pluginproject.BundleSource;
import org.ant4eclipse.pde.tools.TargetPlatform;
import org.ant4eclipse.pde.tools.TargetPlatformConfiguration;
import org.ant4eclipse.pde.tools.TargetPlatformRegistry;

import org.ant4eclipse.platform.ant.core.MacroExecutionComponent;
import org.ant4eclipse.platform.ant.core.MacroExecutionValues;
import org.ant4eclipse.platform.ant.core.ScopedMacroDefinition;
import org.ant4eclipse.platform.ant.core.delegate.MacroExecutionDelegate;
import org.ant4eclipse.platform.ant.core.delegate.MacroExecutionValuesProvider;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.DynamicElement;
import org.apache.tools.ant.taskdefs.MacroDef;
import org.apache.tools.ant.taskdefs.MacroDef.NestedSequential;
import org.eclipse.osgi.service.resolver.BundleDescription;
import org.eclipse.osgi.service.resolver.State;
import org.osgi.framework.Version;

import java.io.File;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * The {@link ExecuteFeatureProjectTask} can be used to iterate over a feature. It implements a loop over all the bundle
 * and/or plug-in-projects contained in a <code>feature.xml</code> file.
 * <p>
 * 
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 */
public class ExecuteFeatureProjectTask extends AbstractPdeBuildTask implements DynamicElement,
    MacroExecutionComponent<String> {

  private static final String                  SCOPE_PLUGIN       = "SCOPE_PLUGIN";

  private static final String                  SCOPE_ROOT_FEATURE = "SCOPE_ROOT_FEATURE";

  /** the macro execution delegate */
  private final MacroExecutionDelegate<String> _macroExecutionDelegate;

  /**
   * <p>
   * Creates a new instance of type ExecuteFeatureProjectTask.
   * </p>
   */
  public ExecuteFeatureProjectTask() {

    // create the delegates
    this._macroExecutionDelegate = new MacroExecutionDelegate<String>(this, "executeFeatureProject");
  }

  /**
   * {@inheritDoc}
   */
  public NestedSequential createScopedMacroDefinition(String scope) {
    return _macroExecutionDelegate.createScopedMacroDefinition(scope);
  }

  /**
   * {@inheritDoc}
   */
  public void executeMacroInstance(MacroDef macroDef, MacroExecutionValuesProvider provider) {
    _macroExecutionDelegate.executeMacroInstance(macroDef, provider);
  }

  /**
   * {@inheritDoc}
   */
  public String getPrefix() {
    return _macroExecutionDelegate.getPrefix();
  }

  /**
   * {@inheritDoc}
   */
  public List<ScopedMacroDefinition<String>> getScopedMacroDefinitions() {
    return _macroExecutionDelegate.getScopedMacroDefinitions();
  }

  /**
   * {@inheritDoc}
   */
  public void setPrefix(String prefix) {
    _macroExecutionDelegate.setPrefix(prefix);
  }

  public Object createDynamicElement(final String name) {

    if ("ForEachPlugin".equalsIgnoreCase(name)) {
      return createScopedMacroDefinition(SCOPE_PLUGIN);
    } else if ("ForRootFeature".equalsIgnoreCase(name)) {
      return createScopedMacroDefinition(SCOPE_ROOT_FEATURE);
    }

    return null;
  }

  /**
   * {@inheritDoc}
   */
  public void doExecute() {

    // 2. resolve (ordered) BundleDescriptions for plug-ins
    final PluginAndBundleDescription[] pluginAndBundleDescriptions = getBundleDesciptionsForFeaturePlugins();

    // execute scoped macro definitions
    for (final ScopedMacroDefinition<String> scopedMacroDefinition : getScopedMacroDefinitions()) {

      if (SCOPE_ROOT_FEATURE.equals(scopedMacroDefinition.getScope())) {
        executeRootFeatureScopedMacroDef(scopedMacroDefinition.getMacroDef());
      } else if (SCOPE_PLUGIN.equals(scopedMacroDefinition.getScope())) {
        executePluginScopedMacroDef(scopedMacroDefinition.getMacroDef());
      } else {
        // TODO
        throw new RuntimeException("Unknown Scope '" + scopedMacroDefinition.getScope() + "'");
      }
    }
  }

  private void executePluginScopedMacroDef(MacroDef macroDef) {

    PluginAndBundleDescription[] pluginAndBundleDescriptions = getBundleDesciptionsForFeaturePlugins();

    for (final PluginAndBundleDescription pluginAndBundleDescription : pluginAndBundleDescriptions) {

      // execute macro
      executeMacroInstance(macroDef, new MacroExecutionValuesProvider() {

        public MacroExecutionValues provideMacroExecutionValues(final MacroExecutionValues values) {

          // TODO: References

          final BundleDescription bundleDescription = pluginAndBundleDescription.getBundleDescription();
          final Plugin plugin = pluginAndBundleDescription.getPlugin();

          // add plug-in id
          if (plugin.hasId()) {
            values.getProperties().put("plugin.id", plugin.getId());
          }

          // the location of the bundle (either location of eclipse project or location in target platform)
          final BundleSource bundleSource = (BundleSource) bundleDescription.getUserObject();
          if (bundleSource.isEclipseProject()) {
            final File bundleLocation = bundleSource.getAsEclipseProject().getFolder();
            values.getProperties().put("plugin.isSource", "true");
            values.getProperties().put("plugin.file", bundleLocation.getAbsolutePath());
            values.getProperties().put("plugin.filename", bundleLocation.getName());
          } else {
            final File bundleLocation = bundleSource.getAsFile();
            values.getProperties().put("plugin.file", bundleLocation.getAbsolutePath());
            values.getProperties().put("plugin.filename", bundleLocation.getName());
          }

          if (plugin.hasVersion()) {
            // plugin.version contains the version from plugin.xml
            values.getProperties().put("plugin.version", plugin.getVersion().toString());
          }

          // plugin.effectiveVersion contains the "resolved" version
          // that is - if plugin version is 0.0.0 - the actual version
          // of the bundle that has been found for this plug-in entry
          values.getProperties().put("plugin.resolvedversion", bundleDescription.getVersion().toString());

          if (plugin.hasDownloadSize()) {
            values.getProperties().put("plugin.downloadsize", plugin.getDownloadSize());
          }
          if (plugin.hasInstallSize()) {
            values.getProperties().put("plugin.installsize", plugin.getInstallSize());
          }
          if (plugin.hasWindowingSystem()) {
            values.getProperties().put("plugin.windowingsystem", plugin.getWindowingSystem());
          }
          if (plugin.hasMachineArchitecture()) {
            values.getProperties().put("plugin.machinearchitecture", plugin.getMachineArchitecture());
          }
          if (plugin.hasOperatingSystem()) {
            values.getProperties().put("plugin.operatingsystem", plugin.getOperatingSystem());
          }
          if (plugin.hasLocale()) {
            values.getProperties().put("plugin.locale", plugin.getLocale());
          }
          values.getProperties().put("plugin.fragment", Boolean.toString(plugin.isFragment()));
          values.getProperties().put("plugin.unpack", Boolean.toString(plugin.isUnpack()));

          // return the values
          return values;
        }
      });
    }
  }

  private void executeRootFeatureScopedMacroDef(MacroDef macroDef) {
    // execute macro
    executeMacroInstance(macroDef, new MacroExecutionValuesProvider() {

      public MacroExecutionValues provideMacroExecutionValues(final MacroExecutionValues values) {

        // return the values
        return values;
      }
    });
  }

  /**
   * @return
   */
  private PluginAndBundleDescription[] getBundleDesciptionsForFeaturePlugins() {

    // 1. Parse the feature
    FeatureManifest feature = FeatureProjectRole.Helper.getFeatureProjectRole(getEclipseProject()).getFeature();

    // 2. Get all defined plug-in descriptions
    final Plugin[] featurePlugins = feature.getPlugins();

    // 3. Initialize target platform
    final TargetPlatformConfiguration configuration = new TargetPlatformConfiguration();
    configuration.setPreferProjects(true);
    final TargetPlatform targetPlatform = TargetPlatformRegistry.Helper.getRegistry().getInstance(getWorkspace(),
        getTargetPlatformId(), configuration);

    // 3.1 resolve state
    final State state = targetPlatform.getState();

    // 4. Retrieve BundlesDescriptions for feature plug-ins
    final Map map = new HashMap();
    final List bundleDescriptions = new LinkedList();

    for (int i = 0; i < featurePlugins.length; i++) {
      final Plugin plugin = featurePlugins[i];

      // if a plug-in reference uses a version, the exact version must be found in the workspace
      // if a plug-in reference specifies "0.0.0" as version, the newest plug-in found will be used
      BundleDescription bundleDescription = null;
      if (plugin.getVersion().equals(Version.emptyVersion)) {
        // find newest plug-in
        bundleDescription = state.getBundle(plugin.getId(), null);
      } else {
        bundleDescription = state.getBundle(plugin.getId(), plugin.getVersion());
      }
      if (bundleDescription == null) {
        throw new BuildException("Could not find bundle with id '" + plugin.getId() + "' and version '"
            + plugin.getVersion() + "' in workspace!");
      }
      Assert.assertTrue(bundleDescription.isResolved(), "asd");
      bundleDescriptions.add(bundleDescription);
      map.put(bundleDescription, plugin);
    }
    final BundleDescription[] featureBundles = (BundleDescription[]) bundleDescriptions
        .toArray(new BundleDescription[0]);

    // 5. Sort the bundles
    final Object[][] cycles = state.getStateHelper().sortBundles(featureBundles);
    // warn on circular dependencies
    if ((cycles != null) && (cycles.length > 0)) {
      // TODO: better error messages
      A4ELogging.warn("Detected circular dependencies:");
      for (int i = 0; i < cycles.length; i++) {
        A4ELogging.warn(Arrays.asList(cycles[i]).toString());
      }
    }

    // 6.1 create result
    final List result = new LinkedList();

    // 6.2 populate result
    for (int i = 0; i < featureBundles.length; i++) {
      final BundleDescription bundleDescription = featureBundles[i];
      final Plugin plugin = (Plugin) map.get(bundleDescription);
      final PluginAndBundleDescription pluginAndBundleDescription = new PluginAndBundleDescription(bundleDescription,
          plugin);
      result.add(pluginAndBundleDescription);
    }

    // 6.3 return result
    return (PluginAndBundleDescription[]) result.toArray(new PluginAndBundleDescription[0]);
  }

  /**
   *
   */
  protected void preconditions() throws BuildException {
    requireWorkspaceAndProjectNameSet();
    requireTargetPlatformIdSet();

    // if (isFeatureSet()) {
    // requireWorkspaceSet();
    // if (!this._feature.exists()) {
    // throw new BuildException("Wrong parameter: Feature '" + this._feature + "' has to exist.");
    // }
    // if (!this._feature.isFile()) {
    // throw new BuildException("Wrong parameter: Feature '" + this._feature + "' has to be a file.");
    // }
    // } else {
    // requireWorkspaceAndProjectNameOrProjectSet();
    // }
  }

  /**
   * <p>
   * Inner class that holds the {@link BundleDescription} that is associated with a given feature {@link Plugin}.
   * </p>
   * 
   * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
   */
  private class PluginAndBundleDescription {

    /** the feature plug-in */
    private final Plugin            _plugin;

    /** the bundle description */
    private final BundleDescription _bundleDescription;

    /**
     * <p>
     * Creates a new instance of type {@link PluginAndBundleDescription}.
     * </p>
     * 
     * @param bundleDescription
     *          the {@link BundleDescription}
     * @param plugin
     *          the {@link Plugin}
     */
    public PluginAndBundleDescription(final BundleDescription bundleDescription, final Plugin plugin) {
      Assert.notNull(bundleDescription);
      Assert.notNull(plugin);

      this._bundleDescription = bundleDescription;
      this._plugin = plugin;
    }

    /**
     * <p>
     * Returns the {@link Plugin}.
     * </p>
     * 
     * @return the {@link Plugin}.
     */
    public Plugin getPlugin() {
      return this._plugin;
    }

    /**
     * <p>
     * Returns the {@link getBundleDescription}.
     * </p>
     * 
     * @return the {@link getBundleDescription}.
     */
    public BundleDescription getBundleDescription() {
      return this._bundleDescription;
    }
  }
}
