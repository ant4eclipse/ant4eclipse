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
package org.ant4eclipse.pde.ant;

import org.ant4eclipse.core.ant.FileListHelper;

import org.ant4eclipse.pde.PdeExceptionCode;
import org.ant4eclipse.pde.internal.tools.FeatureDescription;
import org.ant4eclipse.pde.model.featureproject.FeatureManifest;
import org.ant4eclipse.pde.model.featureproject.FeatureProjectRole;
import org.ant4eclipse.pde.model.featureproject.FeatureManifest.Includes;
import org.ant4eclipse.pde.model.featureproject.FeatureManifest.Plugin;
import org.ant4eclipse.pde.model.pluginproject.BundleSource;
import org.ant4eclipse.pde.tools.PdeBuildHelper;
import org.ant4eclipse.pde.tools.ResolvedFeature;
import org.ant4eclipse.pde.tools.TargetPlatform;
import org.ant4eclipse.pde.tools.TargetPlatformConfiguration;
import org.ant4eclipse.pde.tools.TargetPlatformRegistry;

import org.ant4eclipse.platform.ant.core.MacroExecutionValues;
import org.ant4eclipse.platform.ant.core.ScopedMacroDefinition;
import org.ant4eclipse.platform.ant.core.delegate.MacroExecutionValuesProvider;
import org.ant4eclipse.platform.ant.core.task.AbstractExecuteProjectTask;

import org.ant4eclipse.lib.core.exception.Ant4EclipseException;
import org.ant4eclipse.lib.core.logging.A4ELogging;
import org.ant4eclipse.lib.core.service.ServiceRegistry;
import org.ant4eclipse.lib.core.util.Pair;
import org.ant4eclipse.lib.core.util.Utilities;
import org.ant4eclipse.lib.platform.PlatformExceptionCode;
import org.ant4eclipse.lib.platform.model.resource.EclipseProject;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.taskdefs.MacroDef;
import org.eclipse.osgi.service.resolver.BundleDescription;
import org.osgi.framework.Version;

import java.io.File;
import java.util.Iterator;

/**
 * <p>
 * The {@link ExecuteFeatureTask} can be used to iterate over a feature. It implements a loop over all the bundles
 * and/or plug-in-projects contained in a <code>feature.xml</code> file.
 * <p>
 * 
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 */
public class ExecuteFeatureTask extends AbstractExecuteProjectTask implements PdeExecutorValues,
    TargetPlatformAwareComponent {

  /** the plug-in scope */
  private static final String         SCOPE_PLUGIN                = "SCOPE_PLUGIN";

  /** the plug-in scope element name */
  private static final String         SCOPE_NAME_PLUGIN           = "ForEachPlugin";

  /** the root feature scope */
  private static final String         SCOPE_ROOT_FEATURE          = "SCOPE_ROOT_FEATURE";

  /** the root feature scope element name */
  private static final String         SCOPE_NAME_ROOT_FEATURE     = "ForRootFeature";

  /** the included feature scope */
  private static final String         SCOPE_INCLUDED_FEATURE      = "SCOPE_INCLUDED_FEATURE";

  /** the included feature scope element name */
  private static final String         SCOPE_NAME_INCLUDED_FEATURE = "ForEachIncludedFeature";

  /** the id of the feature to build (either featureId or projectName has to be set) */
  private String                      _featureId;

  /** the version of the feature to build (must be used together with featureId) */
  private Version                     _featureVersion;

  /** the target platform delegate */
  private TargetPlatformAwareDelegate _targetPlatformAwareDelegate;

  /** the resolved feature */
  private ResolvedFeature             _resolvedFeature;

  /** a semicolon separated list of bundle-symbolicNames and versions */
  private String                      _resolvedBundleVersions;

  /**
   * <p>
   * Creates a new instance of type {@link ExecuteFeatureTask}.
   * </p>
   */
  public ExecuteFeatureTask() {
    super("executeFeature");

    // create the target platform delegate
    this._targetPlatformAwareDelegate = new TargetPlatformAwareDelegate();
  }

  /**
   * {@inheritDoc}
   */
  public final String getTargetPlatformId() {
    return this._targetPlatformAwareDelegate.getTargetPlatformId();
  }

  /**
   * {@inheritDoc}
   */
  public final boolean isTargetPlatformIdSet() {
    return this._targetPlatformAwareDelegate.isTargetPlatformIdSet();
  }

  /**
   * {@inheritDoc}
   */
  public final void requireTargetPlatformIdSet() {
    this._targetPlatformAwareDelegate.requireTargetPlatformIdSet();
  }

  /**
   * {@inheritDoc}
   */
  public final void setTargetPlatformId(String targetPlatformId) {
    this._targetPlatformAwareDelegate.setTargetPlatformId(targetPlatformId);
  }

  /**
   * <p>
   * Returns the feature id.
   * </p>
   * 
   * @return the featureId
   */
  public String getFeatureId() {
    return this._featureId;
  }

  /**
   * <p>
   * Sets the feature id.
   * </p>
   * 
   * @param featureId
   *          the featureId to set
   */
  public void setFeatureId(String featureId) {
    if (Utilities.hasText(featureId)) {
      this._featureId = featureId;
    }
  }

  /**
   * <p>
   * Returns the feature version.
   * </p>
   * 
   * @return the version the version of the feature
   */
  public Version getFeatureVersion() {
    return this._featureVersion;
  }

  /**
   * <p>
   * Sets the feature version.
   * </p>
   * 
   * @param version
   *          the version to set
   */
  public void setFeatureVersion(String version) {
    if (Utilities.hasText(version)) {
      this._featureVersion = new Version(version);
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setProjectName(String projectName) {
    if (Utilities.hasText(projectName)) {
      super.setProjectName(projectName);
    }
  }

  /**
   * {@inheritDoc}
   */
  public Object createDynamicElement(String name) {

    // create macro definition for SCOPE_ROOT_FEATURE
    if (SCOPE_NAME_ROOT_FEATURE.equalsIgnoreCase(name)) {
      return createScopedMacroDefinition(SCOPE_ROOT_FEATURE);
    }
    // create macro definition for SCOPE_NAME_INCLUDED_FEATURE
    else if (SCOPE_NAME_INCLUDED_FEATURE.equalsIgnoreCase(name)) {
      return createScopedMacroDefinition(SCOPE_INCLUDED_FEATURE);
    }
    // create macro definition for SCOPE_NAME_PLUGIN
    else if (SCOPE_NAME_PLUGIN.equalsIgnoreCase(name)) {
      return createScopedMacroDefinition(SCOPE_PLUGIN);
    }

    // return null otherwise
    return null;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void doExecute() {

    // set a default version if feature version is not set
    if (this._featureId != null && this._featureVersion == null) {
      this._featureVersion = Version.emptyVersion;
    }

    // resolve the feature
    this._resolvedFeature = resolveFeature();

    // extract the resolved bundle versions
    this._resolvedBundleVersions = extractBundleVersions();

    // execute scoped macro definitions
    for (ScopedMacroDefinition<String> scopedMacroDefinition : getScopedMacroDefinitions()) {

      // execute macro definition for SCOPE_ROOT_FEATURE
      if (SCOPE_ROOT_FEATURE.equals(scopedMacroDefinition.getScope())) {
        executeRootFeatureScopedMacroDef(scopedMacroDefinition.getMacroDef());
      }
      // execute macro definition for SCOPE_INCLUDED_FEATURE
      else if (SCOPE_INCLUDED_FEATURE.equals(scopedMacroDefinition.getScope())) {
        executeIncludedFeatureScopedMacroDef(scopedMacroDefinition.getMacroDef());
      }
      // execute macro definition for SCOPE_PLUGIN
      else if (SCOPE_PLUGIN.equals(scopedMacroDefinition.getScope())) {
        executePluginScopedMacroDef(scopedMacroDefinition.getMacroDef());
      }
      // unknown execution scope
      else {
        throw new Ant4EclipseException(PlatformExceptionCode.UNKNOWN_EXECUTION_SCOPE, scopedMacroDefinition.getScope());
      }
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected void preconditions() throws BuildException {

    // require workspace directory set...
    requireWorkspaceDirectorySet();

    // require target platform id set...
    requireTargetPlatformIdSet();

    // require project name *or* featureId (and version) set...
    if ((isProjectNameSet() && (this._featureId != null || this._featureVersion != null))) {
      throw new Ant4EclipseException(PdeExceptionCode.ANT_ATTRIBUTE_X_OR_Y, "projectName",
          "featureId' and 'featureVersion");
    }
    if ((!isProjectNameSet() && (this._featureId == null || this._featureVersion == null))) {
      throw new Ant4EclipseException(PdeExceptionCode.ANT_ATTRIBUTE_X_OR_Y, "projectName",
          "featureId' and 'featureVersion");
    }
  }

  /**
   * <p>
   * Execute plug-in scoped macro definition.
   * </p>
   * 
   * @param macroDef
   */
  private void executePluginScopedMacroDef(MacroDef macroDef) {

    for (final Pair<Plugin, BundleDescription> pluginAndBundleDescription : this._resolvedFeature
        .getPluginToBundleDescptionList()) {

      // execute macro
      executeMacroInstance(macroDef, new MacroExecutionValuesProvider() {

        public MacroExecutionValues provideMacroExecutionValues(MacroExecutionValues values) {

          // TODO: References
          Plugin plugin = pluginAndBundleDescription.getFirst();
          BundleDescription bundleDescription = pluginAndBundleDescription.getSecond();

          // add plug-in id
          if (plugin.hasId()) {
            values.getProperties().put(PLUGIN_ID, plugin.getId());
          }

          // the location of the bundle (either location of eclipse project or location in target platform)
          BundleSource bundleSource = (BundleSource) bundleDescription.getUserObject();
          if (bundleSource.isEclipseProject()) {
            File bundleLocation = bundleSource.getAsEclipseProject().getFolder();
            values.getProperties().put(PLUGIN_IS_SOURCE, "true");
            values.getProperties().put(PLUGIN_FILE, bundleLocation.getAbsolutePath());
            values.getProperties().put(PLUGIN_FILENAME, bundleLocation.getName());
          } else {
            File bundleLocation = bundleSource.getAsFile();
            values.getProperties().put(PLUGIN_IS_SOURCE, "false");
            values.getProperties().put(PLUGIN_FILE, bundleLocation.getAbsolutePath());
            values.getProperties().put(PLUGIN_FILENAME, bundleLocation.getName());
            values.getReferences().put(PLUGIN_FILELIST, FileListHelper.getFileList(bundleLocation));
          }

          if (plugin.hasVersion()) {
            // plugin.version contains the version from plugin.xml
            values.getProperties().put(PLUGIN_VERSION, plugin.getVersion().toString());

            // PLUGIN_RESOLVED_VERSION contains the "resolved" version
            // that is - if plugin version is 0.0.0 - the actual version
            // of the bundle that has been found for this plug-in entry
            values.getProperties().put(PLUGIN_RESOLVED_VERSION, bundleDescription.getVersion().toString());
          }

          if (plugin.hasDownloadSize()) {
            values.getProperties().put(PLUGIN_DOWNLOADSIZE, plugin.getDownloadSize());
          }
          if (plugin.hasInstallSize()) {
            values.getProperties().put(PLUGIN_INSTALLSIZE, plugin.getInstallSize());
          }
          if (plugin.hasWindowingSystem()) {
            values.getProperties().put(PLUGIN_WINDOWINGSYSTEM, plugin.getWindowingSystem());
          }
          if (plugin.hasMachineArchitecture()) {
            values.getProperties().put(PLUGIN_MACHINEARCHITECTURE, plugin.getMachineArchitecture());
          }
          if (plugin.hasOperatingSystem()) {
            values.getProperties().put(PLUGIN_OPERATINGSYSTEM, plugin.getOperatingSystem());
          }
          if (plugin.hasLocale()) {
            values.getProperties().put(PLUGIN_LOCALE, plugin.getLocale());
          }
          values.getProperties().put(PLUGIN_FRAGMENT, Boolean.toString(plugin.isFragment()));
          values.getProperties().put(PLUGIN_UNPACK, Boolean.toString(plugin.isUnpack()));

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

      public MacroExecutionValues provideMacroExecutionValues(MacroExecutionValues values) {

        // feature is an eclipse feature project
        if (ExecuteFeatureTask.this._resolvedFeature.getSource() instanceof EclipseProject) {

          // get the eclipse project
          EclipseProject eclipseProject = (EclipseProject) ExecuteFeatureTask.this._resolvedFeature.getSource();

          // set the properties
          values.getProperties().put(FEATURE_IS_SOURCE, "true");
          values.getProperties().put(FEATURE_FILE, eclipseProject.getFolder().getAbsolutePath());
          values.getProperties().put(FEATURE_FILE_NAME, eclipseProject.getSpecifiedName());

          // FeatureProjectRole featureProjectRole = FeatureProjectRole.Helper.getFeatureProjectRole(eclipseProject);

          // if (featureProjectRole.hasBuildProperties()) {
          // FeatureBuildProperties buildProperties = featureProjectRole.getBuildProperties();
          // values.getProperties().put(BUILD_PROPERTIES_BINARY_INCLUDES, buildProperties.getBinaryIncludesAsString());
          // values.getProperties().put(BUILD_PROPERTIES_BINARY_EXCLUDES, buildProperties.getBinaryExcludesAsString());
          // }

          // set references
          values.getReferences().put(FEATURE_FILE_PATH, convertToPath(eclipseProject.getFolder()));
        }

        // feature is a archive or a directory
        else if (ExecuteFeatureTask.this._resolvedFeature.getSource() instanceof File) {
          values.getProperties().put(FEATURE_IS_SOURCE, "false");
          // get the source file
          File file = (File) ExecuteFeatureTask.this._resolvedFeature.getSource();

          // set properties
          values.getProperties().put(FEATURE_FILE, file.getAbsolutePath());
          values.getProperties().put(FEATURE_FILE_NAME, file.getName());

          // set references
          values.getReferences().put(FEATURE_FILE_PATH, convertToPath(file));
          values.getReferences().put(FEATURE_FILELIST, FileListHelper.getFileList(file));
        }

        // get the feature manifest
        FeatureManifest manifest = ExecuteFeatureTask.this._resolvedFeature.getFeatureManifest();

        // set the properties
        values.getProperties().put(FEATURE_ID, manifest.getId());
        values.getProperties().put(FEATURE_VERSION, manifest.getVersion().toString());
        Version resolvedFeatureVersion = PdeBuildHelper.resolveVersion(manifest.getVersion(), PdeBuildHelper
            .getResolvedContextQualifier());
        values.getProperties().put(FEATURE_RESOLVED_VERSION, resolvedFeatureVersion.toString());

        if (Utilities.hasText(manifest.getLabel())) {
          values.getProperties().put(FEATURE_LABEL, manifest.getLabel());
        }

        if (Utilities.hasText(manifest.getProviderName())) {
          values.getProperties().put(FEATURE_PROVIDERNAME, manifest.getProviderName());
        }

        values.getProperties().put(FEATURE_PLUGINS_RESOLVED_VERSIONS, ExecuteFeatureTask.this._resolvedBundleVersions);

        // return the values
        return values;
      }
    });
  }

  /**
   * <p>
   * Execute the macro definitions for each included feature.
   * </p>
   * 
   * @param macroDef
   *          the macro definition to execute
   */
  private void executeIncludedFeatureScopedMacroDef(MacroDef macroDef) {

    // iterate over the includes>
    for (final Pair<Includes, FeatureDescription> pair : this._resolvedFeature.getIncludesToFeatureDescriptionList()) {

      // execute macro definition
      executeMacroInstance(macroDef, new MacroExecutionValuesProvider() {

        public MacroExecutionValues provideMacroExecutionValues(MacroExecutionValues values) {

          // add the feature id
          values.getProperties().put(FEATURE_ID, pair.getFirst().getId());

          // add the feature version
          values.getProperties().put(FEATURE_VERSION, pair.getFirst().getVersion().toString());

          // add the resolved version
          Version resolvedFeatureVersion = PdeBuildHelper.resolveVersion(pair.getSecond().getFeatureManifest()
              .getVersion(), PdeBuildHelper.getResolvedContextQualifier());
          values.getProperties().put(FEATURE_RESOLVED_VERSION, resolvedFeatureVersion.toString());

          // add the name
          if (Utilities.hasText(pair.getFirst().getName())) {
            values.getProperties().put(FEATURE_NAME, pair.getFirst().getName());
          }

          // add the optional value
          values.getProperties().put(FEATURE_OPTIONAL, Boolean.toString(pair.getFirst().isOptional()));

          // add the searchLocation
          if (Utilities.hasText(pair.getFirst().getSearchLocation())) {
            values.getProperties().put(FEATURE_SEARCH_LOCATION, pair.getFirst().getSearchLocation());
          }

          // add the operatingSystem
          if (Utilities.hasText(pair.getFirst().getOperatingSystem())) {
            values.getProperties().put(FEATURE_OS, pair.getFirst().getOperatingSystem());
          }

          // add the machineArchitecture
          if (Utilities.hasText(pair.getFirst().getMachineArchitecture())) {
            values.getProperties().put(FEATURE_ARCH, pair.getFirst().getMachineArchitecture());
          }

          // add the windowingSystem
          if (Utilities.hasText(pair.getFirst().getWindowingSystem())) {
            values.getProperties().put(FEATURE_WS, pair.getFirst().getWindowingSystem());
          }

          // add the locale
          if (Utilities.hasText(pair.getFirst().getLocale())) {
            values.getProperties().put(FEATURE_NL, pair.getFirst().getLocale());
          }

          // return the values
          return values;
        }
      });
    }
  }

  /**
   * <p>
   * Resolves the specified feature description by matching each entry in the feature to a 'real' bundle in the target
   * platform.
   * </p>
   * 
   * @return the resolved feature
   */
  private ResolvedFeature resolveFeature() {

    // create a new target platform configuration
    TargetPlatformConfiguration configuration = new TargetPlatformConfiguration();
    configuration.setPreferProjects(true);

    // fetch the target platform
    TargetPlatformRegistry registry = ServiceRegistry.instance().getService(TargetPlatformRegistry.class);
    TargetPlatform targetPlatform = registry.getInstance(getWorkspace(), getTargetPlatformId(), configuration);

    // let the target platform resolve the feature
    // case 1: pde feature project
    if (isProjectNameSet()) {
      FeatureManifest featureManifest = getEclipseProject().getRole(FeatureProjectRole.class)
          .getFeatureManifest();
      return targetPlatform.resolveFeature(getEclipseProject(), featureManifest);
    }
    // case 2: feature taken from the target platform
    else {
      if (A4ELogging.isDebuggingEnabled()) {
        A4ELogging.debug("Trying to get feature '%s_%s' from target platform.", this._featureId, this._featureVersion);
      }

      FeatureDescription featureDescription = targetPlatform.getFeatureDescription(this._featureId,
          this._featureVersion);
      FeatureManifest featureManifest = featureDescription.getFeatureManifest();
      return targetPlatform.resolveFeature(featureDescription.getSource(), featureManifest);
    }
  }

  /**
   * <p>
   * Creates a (comma-separated) list with bundles ids and resolved versions, e.g.
   * <code><pre>testproject=1.0.0;org.eclipse.osgi=3.4.2.R34x_v20080826-1230;org.eclipse.osgi.util=3.1.300.v20080303;org.eclipse.
   * osgi.services=3.1.200.v20071203;example_bundle=1.0.0.200907270913.
   * </pre><code>
   * </p>
   * <p>
   * This list can be used within the {@link PatchFeatureManifestTask} to patch each bundle that has a version '0.0.0'
   * (the default version).
   * </p>
   * 
   * @return a (comma-separated) list with bundles ids and resolved versions.
   */
  private String extractBundleVersions() {

    // create result
    StringBuilder result = new StringBuilder();

    // iterate over all the resolved bundles and add them to the result...
    for (Iterator<Pair<Plugin, BundleDescription>> iterator = this._resolvedFeature.getPluginToBundleDescptionList()
        .iterator(); iterator.hasNext();) {

      Pair<Plugin, BundleDescription> pair = iterator.next();

      // add id and resolved version
      result.append(pair.getFirst().getId());
      result.append("=");
      result.append(PdeBuildHelper.resolveVersion(pair.getSecond().getVersion(), PdeBuildHelper
          .getResolvedContextQualifier()));

      // append ';' if necessary
      if (iterator.hasNext()) {
        result.append(";");
      }
    }

    // return the result
    return result.toString();
  }
}
