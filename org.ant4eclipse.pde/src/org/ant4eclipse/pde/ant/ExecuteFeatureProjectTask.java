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

import org.ant4eclipse.pde.model.buildproperties.FeatureBuildProperties;
import org.ant4eclipse.pde.model.featureproject.FeatureManifest;
import org.ant4eclipse.pde.model.featureproject.FeatureProjectRole;
import org.ant4eclipse.pde.model.featureproject.FeatureManifest.Plugin;
import org.ant4eclipse.pde.model.pluginproject.BundleSource;
import org.ant4eclipse.pde.tools.PdeBuildHelper;
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
import java.util.Iterator;
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

  /** the plug-in scope */
  private static final String                  SCOPE_PLUGIN       = "SCOPE_PLUGIN";

  /** the root feature scope */
  private static final String                  SCOPE_ROOT_FEATURE = "SCOPE_ROOT_FEATURE";

  /** the macro execution delegate */
  private final MacroExecutionDelegate<String> _macroExecutionDelegate;

  /** - */
  private List<PluginAndBundleDescription>     _pluginAndBundleDescriptions;

  /** - */
  private String                               _pluginsEffectiveVersions;

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

  /**
   * {@inheritDoc}
   */
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

    // find the plug-ins
    _pluginAndBundleDescriptions = getPluginAndBundleDescriptions();

    StringBuilder builder = new StringBuilder();
    for (Iterator<PluginAndBundleDescription> iterator = _pluginAndBundleDescriptions.iterator(); iterator.hasNext();) {
      PluginAndBundleDescription description = iterator.next();
      builder.append(description.getPlugin().getId());
      builder.append("=");
      builder.append(PdeBuildHelper.resolveVersion(description.getBundleDescription().getVersion(), PdeBuildHelper
          .getResolvedContextQualifier()));
      if (iterator.hasNext()) {
        builder.append(";");
      }
    }
    _pluginsEffectiveVersions = builder.toString();

    // execute scoped macro definitions
    for (final ScopedMacroDefinition<String> scopedMacroDefinition : getScopedMacroDefinitions()) {

      if (SCOPE_ROOT_FEATURE.equals(scopedMacroDefinition.getScope())) {
        executeRootFeatureScopedMacroDef(scopedMacroDefinition.getMacroDef());
      } else if (SCOPE_PLUGIN.equals(scopedMacroDefinition.getScope())) {
        executePluginScopedMacroDef(scopedMacroDefinition.getMacroDef());
      } else {
        // TODO: NLS
        throw new RuntimeException("Unknown Scope '" + scopedMacroDefinition.getScope() + "'");
      }
    }
  }

  /**
   * {@inheritDoc}
   */
  protected void preconditions() throws BuildException {
    requireWorkspaceAndProjectNameSet();
    requireTargetPlatformIdSet();
  }

  /**
   * <p>
   * </p>
   * 
   * @param macroDef
   */
  private void executePluginScopedMacroDef(MacroDef macroDef) {

    for (final PluginAndBundleDescription pluginAndBundleDescription : _pluginAndBundleDescriptions) {

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

  /**
   * <p>
   * </p>
   * 
   * @param macroDef
   */
  private void executeRootFeatureScopedMacroDef(MacroDef macroDef) {
    PdeBuildHelper.getResolvedContextQualifier();

    // execute macro
    executeMacroInstance(macroDef, new MacroExecutionValuesProvider() {

      public MacroExecutionValues provideMacroExecutionValues(final MacroExecutionValues values) {

        final FeatureProjectRole featureProjectRole = FeatureProjectRole.Helper
            .getFeatureProjectRole(getEclipseProject());

        FeatureManifest manifest = featureProjectRole.getFeatureManifest();
        values.getProperties().put("feature.id", manifest.getId());
        values.getProperties().put("feature.version", manifest.getVersion().toString());
        Version resolvedFeatureVersion = PdeBuildHelper.resolveVersion(manifest.getVersion(), PdeBuildHelper
            .getResolvedContextQualifier());
        values.getProperties().put("feature.effective.version", resolvedFeatureVersion.toString());
        values.getProperties().put("feature.plugins.effective.versions", _pluginsEffectiveVersions);

        // if (manifest) {
        values.getProperties().put("feature.label", manifest.getLabel());
        // }
        // if (condition) {
        values.getProperties().put("feature.providername", manifest.getProviderName());
        // }

        FeatureBuildProperties buildProperties = featureProjectRole.getBuildProperties();
        values.getProperties().put("build.properties.binary.includes", buildProperties.getBinaryIncludesAsString());
        values.getProperties().put("build.properties.binary.excludes", buildProperties.getBinaryExcludesAsString());

        // return the values
        return values;
      }
    });
  }

  /**
   * <p>
   * </p>
   * 
   * @return
   */
  private List<PluginAndBundleDescription> getPluginAndBundleDescriptions() {

    // 1. Parse the feature manifest
    FeatureManifest featureManifest = FeatureProjectRole.Helper.getFeatureProjectRole(getEclipseProject())
        .getFeatureManifest();

    // 3. Initialize target platform
    final TargetPlatformConfiguration configuration = new TargetPlatformConfiguration();
    configuration.setPreferProjects(true);
    final TargetPlatform targetPlatform = TargetPlatformRegistry.Helper.getRegistry().getInstance(getWorkspace(),
        getTargetPlatformId(), configuration);

    // 3.1 resolve state
    final State state = targetPlatform.getState();

    // 4. Retrieve BundlesDescriptions for feature plug-ins
    final Map<BundleDescription, Plugin> map = new HashMap<BundleDescription, Plugin>();
    final List<BundleDescription> bundleDescriptions = new LinkedList<BundleDescription>();

    for (Plugin plugin : featureManifest.getPlugins()) {

      // if a plug-in reference uses a version, the exact version must be found in the workspace
      // if a plug-in reference specifies "0.0.0" as version, the newest plug-in found will be used
      BundleDescription bundleDescription = state.getBundle(plugin.getId(), plugin.getVersion().equals(
          Version.emptyVersion) ? null : plugin.getVersion());

      // TODO: NLS
      if (bundleDescription == null) {
        throw new BuildException("Could not find bundle with id '" + plugin.getId() + "' and version '"
            + plugin.getVersion() + "' in workspace!");
      }

      // TODO: NLS
      Assert.assertTrue(bundleDescription.isResolved(), "asd");
      bundleDescriptions.add(bundleDescription);
      map.put(bundleDescription, plugin);
    }

    // 5. Sort the bundles
    final BundleDescription[] sortedbundleDescriptions = (BundleDescription[]) bundleDescriptions
        .toArray(new BundleDescription[0]);
    final Object[][] cycles = state.getStateHelper().sortBundles(sortedbundleDescriptions);
    // warn on circular dependencies
    if ((cycles != null) && (cycles.length > 0)) {
      // TODO: better error messages
      A4ELogging.warn("Detected circular dependencies:");
      for (int i = 0; i < cycles.length; i++) {
        A4ELogging.warn(Arrays.asList(cycles[i]).toString());
      }
    }

    // 6.1 create result
    final List<PluginAndBundleDescription> result = new LinkedList<PluginAndBundleDescription>();
    for (BundleDescription bundleDescription : sortedbundleDescriptions) {
      final PluginAndBundleDescription pluginAndBundleDescription = new PluginAndBundleDescription(bundleDescription,
          map.get(bundleDescription));
      result.add(pluginAndBundleDescription);
    }

    // 6.3 return result
    return result;
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
     * Returns the {@link BundleDescription}.
     * </p>
     * 
     * @return the {@link BundleDescription}.
     */
    public BundleDescription getBundleDescription() {
      return this._bundleDescription;
    }
  }
}
