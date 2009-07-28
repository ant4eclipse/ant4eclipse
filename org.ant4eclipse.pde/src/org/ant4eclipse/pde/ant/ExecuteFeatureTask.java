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

import org.ant4eclipse.core.exception.Ant4EclipseException;
import org.ant4eclipse.core.util.Pair;

import org.ant4eclipse.pde.internal.tools.FeatureDescription;
import org.ant4eclipse.pde.model.buildproperties.FeatureBuildProperties;
import org.ant4eclipse.pde.model.featureproject.FeatureManifest;
import org.ant4eclipse.pde.model.featureproject.FeatureProjectRole;
import org.ant4eclipse.pde.model.featureproject.FeatureManifest.Plugin;
import org.ant4eclipse.pde.model.pluginproject.BundleSource;
import org.ant4eclipse.pde.tools.PdeBuildHelper;
import org.ant4eclipse.pde.tools.ResolvedFeature;
import org.ant4eclipse.pde.tools.TargetPlatform;
import org.ant4eclipse.pde.tools.TargetPlatformConfiguration;
import org.ant4eclipse.pde.tools.TargetPlatformRegistry;

import org.ant4eclipse.platform.PlatformExceptionCode;
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
import org.osgi.framework.Version;

import java.io.File;
import java.util.Iterator;
import java.util.List;

/**
 * <p>
 * The {@link ExecuteFeatureTask} can be used to iterate over a feature. It implements a loop over all the bundles
 * and/or plug-in-projects contained in a <code>feature.xml</code> file.
 * <p>
 * 
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 */
public class ExecuteFeatureTask extends AbstractPdeBuildTask implements DynamicElement, MacroExecutionComponent<String> {

  /** the plug-in scope */
  private static final String                  SCOPE_PLUGIN                = "SCOPE_PLUGIN";

  /** the plug-in scope element name */
  private static final String                  SCOPE_NAME_PLUGIN           = "ForEachPlugin";

  /** the root feature scope */
  private static final String                  SCOPE_ROOT_FEATURE          = "SCOPE_ROOT_FEATURE";

  /** the root feature scope element name */
  private static final String                  SCOPE_NAME_ROOT_FEATURE     = "ForRootFeature";

  /** the included feature scope */
  private static final String                  SCOPE_INCLUDED_FEATURE      = "SCOPE_INCLUDED_FEATURE";

  /** the included feature scope element name */
  private static final String                  SCOPE_NAME_INCLUDED_FEATURE = "ForEachIncludedFeature";

  /** the id of the feature to build (either featureId or projectName has to be set) */
  private String                               _featureId;

  /** the version of the feature to build (must be used together with featureId) */
  private String                               _version;

  /** the macro execution delegate */
  private final MacroExecutionDelegate<String> _macroExecutionDelegate;

  /** the resolved feature */
  private ResolvedFeature                      _resolvedFeature;

  /** a semicolon separated list of bundle-symbolicNames and versions */
  private String                               _pluginsEffectiveVersions;

  /**
   * <p>
   * Creates a new instance of type {@link ExecuteFeatureTask}.
   * </p>
   */
  public ExecuteFeatureTask() {

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

    if (SCOPE_NAME_PLUGIN.equalsIgnoreCase(name)) {
      return createScopedMacroDefinition(SCOPE_PLUGIN);
    } else if (SCOPE_NAME_ROOT_FEATURE.equalsIgnoreCase(name)) {
      return createScopedMacroDefinition(SCOPE_ROOT_FEATURE);
    } else if (SCOPE_NAME_INCLUDED_FEATURE.equalsIgnoreCase(name)) {
      return createScopedMacroDefinition(SCOPE_INCLUDED_FEATURE);
    }

    return null;
  }

  /**
   * {@inheritDoc}
   */
  public void doExecute() {

    resolveFeature();

    // TODO: extract method
    StringBuilder builder = new StringBuilder();
    for (Iterator<Pair<Plugin, BundleDescription>> iterator = _resolvedFeature.getPluginToBundleDescptionList()
        .iterator(); iterator.hasNext();) {
      Pair<Plugin, BundleDescription> description = iterator.next();
      builder.append(description.getFirst().getId());
      builder.append("=");
      builder.append(PdeBuildHelper.resolveVersion(description.getSecond().getVersion(), PdeBuildHelper
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
      } else if (SCOPE_INCLUDED_FEATURE.equals(scopedMacroDefinition.getScope())) {
        executeIncludedFeatureScopedMacroDef(scopedMacroDefinition.getMacroDef());
      } else {
        throw new Ant4EclipseException(PlatformExceptionCode.UNKNOWN_EXECUTION_SCOPE, scopedMacroDefinition.getScope());
      }
    }
  }

  /**
   * {@inheritDoc}
   */
  protected void preconditions() throws BuildException {

    // require workspace directory set...
    requireWorkspaceDirectorySet();

    // // require project name *or* feature set...
    // if ((!isProjectNameSet() && _feature == null) || isProjectNameSet() && _feature != null) {
    // // TODO: NLS
    // throw new BuildException("You have to specify either the projectName or the feature attribute!");
    // }
    //
    // // require feature exists (if set) ...
    // if (_feature != null && !_feature.exists()) {
    // // TODO: NLS
    // throw new BuildException("Attribute feature has to contain a path to an existing feature!");
    // }

    // require target platform id set...
    requireTargetPlatformIdSet();
  }

  /**
   * <p>
   * Execute plug-in scoped macro definition.
   * </p>
   * 
   * @param macroDef
   */
  private void executePluginScopedMacroDef(MacroDef macroDef) {

    for (final Pair<Plugin, BundleDescription> pluginAndBundleDescription : _resolvedFeature
        .getPluginToBundleDescptionList()) {

      // execute macro
      executeMacroInstance(macroDef, new MacroExecutionValuesProvider() {

        public MacroExecutionValues provideMacroExecutionValues(final MacroExecutionValues values) {

          // TODO: References
          final Plugin plugin = pluginAndBundleDescription.getFirst();
          final BundleDescription bundleDescription = pluginAndBundleDescription.getSecond();

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

  private void executeIncludedFeatureScopedMacroDef(MacroDef macroDef) {
    // TODO Auto-generated method stub

  }

  /**
   * <p>
   * </p>
   * 
   * @return
   */
  private void resolveFeature() {

    // 1. Initialize target platform
    final TargetPlatformConfiguration configuration = new TargetPlatformConfiguration();
    configuration.setPreferProjects(true);
    final TargetPlatform targetPlatform = TargetPlatformRegistry.Helper.getRegistry().getInstance(getWorkspace(),
        getTargetPlatformId(), configuration);

    // 3.
    FeatureManifest featureManifest = null;

    if (isProjectNameSet()) {
      featureManifest = FeatureProjectRole.Helper.getFeatureProjectRole(getEclipseProject()).getFeatureManifest();
    } else {
      FeatureDescription featureDescription = targetPlatform.getFeatureDescription(_featureId, _version);
      featureManifest = featureDescription.getFeatureManifest();
    }

    _resolvedFeature = targetPlatform.resolveFeature(featureManifest);
  }
}
