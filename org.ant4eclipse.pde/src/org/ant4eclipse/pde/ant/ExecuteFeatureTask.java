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


/**
 * <p>
 * The {@link ExecuteFeatureTask} can be used to iterate over a feature. It implements a loop over all the bundle and/or
 * plug-in-projects contained in a <code>feature.xml</code> file.
 * <p>
 * 
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 */
public class ExecuteFeatureTask extends AbstractPdeBuildTask {

//  /** the file that represents the feature to build (that may is null) */
//  private File                       _feature;
//
//  /** the name of the target that should be called to build each plug-in */
//  private String                     _executeFeaturePluginTarget;
//
//  /** the name of the target */
//  private String                     _preExecuteFeaturePluginsTarget;
//
//  /** the name of the target */
//  private String                     _postExecuteFeaturePluginsTarget;
//
//  /** the execute build delegate */
//  private final ExecuteBuildDelegate _executeBuildDelegate;
//
//  // private final boolean _failOnNonResolvedBundles = false;
//
//  /**
//   * <p>
//   * Creates a new instance of type {@link ExecuteFeatureTask}.
//   * </p>
//   */
//  public ExecuteFeatureTask() {
//    this._executeBuildDelegate = new ExecuteBuildDelegate(this);
//  }
//
//  /**
//   * @param property
//   */
//  public void addParam(final Property property) {
//    this._executeBuildDelegate.addParam(property);
//  }
//
//  /**
//   * @param reference
//   */
//  public void addReference(final Reference reference) {
//    this._executeBuildDelegate.addReference(reference);
//  }
//
//  /**
//   * @param prefix
//   */
//  public void setPrefix(final String prefix) {
//    this._executeBuildDelegate.setPrefix(prefix);
//  }
//
//  /**
//   * <p>
//   * Sets the <code>feature.xml</code> file that should be executed.
//   * </p>
//   * 
//   * @param feature
//   *          the <code>feature.xml</code> file that should be executed.
//   */
//  public void setFeature(final File feature) {
//    this._feature = feature;
//  }
//
//  /**
//   * <p>
//   * Returns <code>true</code>, if a <code>feature.xml</code> file is set.
//   * </p>
//   * 
//   * @return <code>true</code>, if a <code>feature.xml</code> file is set.
//   */
//  public boolean isFeatureSet() {
//    return this._feature != null;
//  }
//
//  /**
//   * @param preExecuteFeaturePluginsTarget
//   */
//  public void setPreExecuteFeaturePluginsTarget(final String preExecuteFeaturePluginsTarget) {
//    this._preExecuteFeaturePluginsTarget = preExecuteFeaturePluginsTarget;
//  }
//
//  /**
//   * <p>
//   * Sets the name of the ant target that should be called for each plug-in.
//   * </p>
//   * 
//   * @param executeFeaturePluginTarget
//   *          the name of the ant target that should be called to build each plug-in.
//   */
//  public void setExecuteFeaturePluginTarget(final String executeFeaturePluginTarget) {
//    this._executeFeaturePluginTarget = executeFeaturePluginTarget;
//  }
//
//  /**
//   * @param postExecuteFeaturePluginsTarget
//   */
//  public void setPostExecuteFeaturePluginsTarget(final String postExecuteFeaturePluginsTarget) {
//    this._postExecuteFeaturePluginsTarget = postExecuteFeaturePluginsTarget;
//  }
//
//  /**
//   * {@inheritDoc}
//   */
//  public void execute() throws BuildException {
//
//    // 1. Check required fields
//    checkRequiredFields();
//
//    // 2. resolve (ordered) BundleDescriptions for plug-ins
//    final PluginAndBundleDescription[] pluginAndBundleDescriptions = getBundleDesciptionsForFeaturePlugins();
//
//    // 3. Create call targets
//    final List antCallTargets = new LinkedList();
//
//    // 3.1 Create call target 'PreExecuteFeaturePlugins'
//    antCallTargets.add(createPreExecuteFeaturePluginsCallTarget());
//
//    // 3.2 Create call targets 'ExecutePlugin'
//    for (int i = 0; i < pluginAndBundleDescriptions.length; i++) {
//      final CallTarget target = createExecuteFeaturePluginCallTarget(pluginAndBundleDescriptions[i]);
//      antCallTargets.add(target);
//    }
//
//    // 3.3 Create call targets 'PostExecuteFeaturePlugins'
//    antCallTargets.add(createPostExecuteFeaturePluginsCallTarget());
//
//    // 4. Execute call targets
//    this._executeBuildDelegate.executeSequential(antCallTargets);
//  }
//
//  /**
//   * @return
//   */
//  private PluginAndBundleDescription[] getBundleDesciptionsForFeaturePlugins() {
//
//    // 1. Parse the feature
//    FeatureManifest feature;
//    try {
//      feature = getFeatureManifest();
//    } catch (final FileNotFoundException e) {
//      throw new BuildException(e.getMessage(), e);
//    }
//
//    // 2. Get all defined plug-in descriptions
//    final Plugin[] featurePlugins = feature.getPlugins();
//
//    // 3. Initialize target platform
//    final TargetPlatformConfiguration configuration = new TargetPlatformConfiguration();
//    configuration.setPreferProjects(true);
//    final TargetPlatform targetPlatform = TargetPlatformRegistry.Helper.getRegistry().getInstance(getWorkspace(),
//        getTargetPlatformLocations(), configuration);
//
//    // 3.1 resolve state
//    final State state = targetPlatform.getState();
//
//    // 4. Retrieve BundlesDescriptions for feature plug-ins
//    final Map map = new HashMap();
//    final List bundleDescriptions = new LinkedList();
//
//    for (int i = 0; i < featurePlugins.length; i++) {
//      final Plugin plugin = featurePlugins[i];
//
//      // if a plug-in reference uses a version, the exact version must be found in the workspace
//      // if a plug-in reference specifies "0.0.0" as version, the newest plug-in found will be used
//      BundleDescription bundleDescription = null;
//      if (plugin.getVersion().equals(Version.emptyVersion)) {
//        // find newest plug-in
//        bundleDescription = state.getBundle(plugin.getId(), null);
//      } else {
//        bundleDescription = state.getBundle(plugin.getId(), plugin.getVersion());
//      }
//      if (bundleDescription == null) {
//        throw new BuildException("Could not find bundle with id '" + plugin.getId() + "' and version '"
//            + plugin.getVersion() + "' in workspace!");
//      }
//      Assert.assertTrue(bundleDescription.isResolved(), "asd");
//      bundleDescriptions.add(bundleDescription);
//      map.put(bundleDescription, plugin);
//    }
//    final BundleDescription[] featureBundles = (BundleDescription[]) bundleDescriptions
//        .toArray(new BundleDescription[0]);
//
//    // 5. Sort the bundles
//    final Object[][] cycles = state.getStateHelper().sortBundles(featureBundles);
//    // warn on circular dependencies
//    if ((cycles != null) && (cycles.length > 0)) {
//      // TODO: better error messages
//      A4ELogging.warn("Detected circular dependencies:");
//      for (int i = 0; i < cycles.length; i++) {
//        A4ELogging.warn(Arrays.asList(cycles[i]).toString());
//      }
//    }
//
//    // 6.1 create result
//    final List result = new LinkedList();
//
//    // 6.2 populate result
//    for (int i = 0; i < featureBundles.length; i++) {
//      final BundleDescription bundleDescription = featureBundles[i];
//      final Plugin plugin = (Plugin) map.get(bundleDescription);
//      final PluginAndBundleDescription pluginAndBundleDescription = new PluginAndBundleDescription(bundleDescription,
//          plugin);
//      result.add(pluginAndBundleDescription);
//    }
//
//    // 6.3 return result
//    return (PluginAndBundleDescription[]) result.toArray(new PluginAndBundleDescription[0]);
//  }
//
//  /**
//   * 
//   */
//  private void checkRequiredFields() {
//    if (isFeatureSet()) {
//      requireWorkspaceSet();
//      if (!this._feature.exists()) {
//        throw new BuildException("Wrong parameter: Feature '" + this._feature + "' has to exist.");
//      }
//      if (!this._feature.isFile()) {
//        throw new BuildException("Wrong parameter: Feature '" + this._feature + "' has to be a file.");
//      }
//    } else {
//      requireWorkspaceAndProjectNameOrProjectSet();
//    }
//    requireTargetExists("PreExecutePluginTarget", this._preExecuteFeaturePluginsTarget);
//    requireTargetExists("ExecutePluginTarget", this._executeFeaturePluginTarget);
//    requireTargetExists("PostExecutePluginTarget", this._postExecuteFeaturePluginsTarget);
//  }
//
//  /**
//   * @return
//   * @throws FileNotFoundException
//   */
//  private FeatureManifest getFeatureManifest() throws FileNotFoundException {
//    FeatureManifest feature;
//    if (this._feature != null) {
//      InputStream inputStream;
//      inputStream = new FileInputStream(this._feature);
//      feature = FeatureManifestParser.parseFeature(inputStream);
//    } else {
//      final EclipseProject project = getEclipseProject();
//      final FeatureProjectRole featureProjectRole = (FeatureProjectRole) project.getRole(FeatureProjectRole.class);
//      feature = featureProjectRole.getFeature();
//    }
//    return feature;
//  }
//
//  /**
//   * <p>
//   * The 'buildPluginTarget' attribute has to be set properly.
//   * </p>
//   */
//  private void requireTargetExists(final String name, final String value) {
//    Assert.nonEmpty(name);
//
//    if ((value != null) || (value.trim().length() != 0)) {
//      if (!this._executeBuildDelegate.isCallTarget(value)) {
//        final String msg = MessageCreator.createMessage("The %s '%s' does not exist!", new Object[] { name, value });
//        throw new BuildException(msg);
//      }
//    }
//
//  }
//
//  /**
//   * @return
//   */
//  private CallTarget createPreExecuteFeaturePluginsCallTarget() {
//    return this._executeBuildDelegate.createCallTarget(this.getOwningTarget(), this._preExecuteFeaturePluginsTarget,
//        true, true, new HashMap());
//  }
//
//  /**
//   * @return
//   */
//  private CallTarget createPostExecuteFeaturePluginsCallTarget() {
//    return this._executeBuildDelegate.createCallTarget(this.getOwningTarget(), this._postExecuteFeaturePluginsTarget,
//        true, true, new HashMap());
//  }
//
//  /**
//   * <p>
//   * Creates a {@link CallTarget} for the specified plug-in.
//   * </p>
//   * 
//   * @param plugin
//   *          the plugin to create a CallTarget.
//   * @param targetName
//   *          the name of the target that should be called.
//   * @param inheritAll
//   * @param inheritRefs
//   * @return the new CallTarget
//   */
//  private CallTarget createExecuteFeaturePluginCallTarget(final PluginAndBundleDescription pluginAndBundleDescription) {
//    Assert.notNull(pluginAndBundleDescription);
//
//    final BundleDescription bundleDescription = pluginAndBundleDescription.getBundleDescription();
//    final Plugin plugin = pluginAndBundleDescription.getPlugin();
//
//    A4ELogging.debug("createCallTarget %s for plugin %s", new String[] { this._executeFeaturePluginTarget,
//        plugin.getId() });
//
//    final Map parameters = new HashMap();
//
//    // add plug-in id
//    if (plugin.hasId()) {
//      parameters.put("plugin.id", plugin.getId());
//    }
//
//    // the location of the bundle (either location of eclipse project or location in target platform)
//    final BundleSource bundleSource = (BundleSource) bundleDescription.getUserObject();
//    if (bundleSource.isEclipseProject()) {
//      final File bundleLocation = bundleSource.getAsEclipseProject().getFolder();
//      parameters.put("plugin.isSource", "true");
//      parameters.put("plugin.file", bundleLocation.getAbsolutePath());
//      parameters.put("plugin.filename", bundleLocation.getName());
//    } else {
//      final File bundleLocation = bundleSource.getAsFile();
//      parameters.put("plugin.file", bundleLocation.getAbsolutePath());
//      parameters.put("plugin.filename", bundleLocation.getName());
//    }
//
//    if (plugin.hasVersion()) {
//      // plugin.version contains the version from plugin.xml
//      parameters.put("plugin.version", plugin.getVersion().toString());
//    }
//
//    // plugin.effectiveVersion contains the "resolved" version
//    // that is - if plugin version is 0.0.0 - the actual version
//    // of the bundle that has been found for this plug-in entry
//    parameters.put("plugin.resolvedversion", bundleDescription.getVersion().toString());
//
//    if (plugin.hasDownloadSize()) {
//      parameters.put("plugin.downloadsize", plugin.getDownloadSize());
//    }
//    if (plugin.hasInstallSize()) {
//      parameters.put("plugin.installsize", plugin.getInstallSize());
//    }
//    if (plugin.hasWindowingSystem()) {
//      parameters.put("plugin.windowingsystem", plugin.getWindowingSystem());
//    }
//    if (plugin.hasMachineArchitecture()) {
//      parameters.put("plugin.machinearchitecture", plugin.getMachineArchitecture());
//    }
//    if (plugin.hasOperatingSystem()) {
//      parameters.put("plugin.operatingsystem", plugin.getOperatingSystem());
//    }
//    if (plugin.hasLocale()) {
//      parameters.put("plugin.locale", plugin.getLocale());
//    }
//    parameters.put("plugin.fragment", Boolean.toString(plugin.isFragment()));
//    parameters.put("plugin.unpack", Boolean.toString(plugin.isUnpack()));
//
//    return this._executeBuildDelegate.createCallTarget(this.getOwningTarget(), this._executeFeaturePluginTarget, true,
//        true, parameters);
//  }
//
//  /**
//   * <p>
//   * Inner class that holds the {@link BundleDescription} that is associated with a given feature {@link Plugin}.
//   * </p>
//   * 
//   * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
//   */
//  private class PluginAndBundleDescription {
//
//    /** the feature plug-in */
//    private final Plugin            _plugin;
//
//    /** the bundle description */
//    private final BundleDescription _bundleDescription;
//
//    /**
//     * <p>
//     * Creates a new instance of type {@link PluginAndBundleDescription}.
//     * </p>
//     * 
//     * @param bundleDescription
//     *          the {@link BundleDescription}
//     * @param plugin
//     *          the {@link Plugin}
//     */
//    public PluginAndBundleDescription(final BundleDescription bundleDescription, final Plugin plugin) {
//      Assert.notNull(bundleDescription);
//      Assert.notNull(plugin);
//
//      this._bundleDescription = bundleDescription;
//      this._plugin = plugin;
//    }
//
//    /**
//     * <p>
//     * Returns the {@link Plugin}.
//     * </p>
//     * 
//     * @return the {@link Plugin}.
//     */
//    public Plugin getPlugin() {
//      return this._plugin;
//    }
//
//    /**
//     * <p>
//     * Returns the {@link getBundleDescription}.
//     * </p>
//     * 
//     * @return the {@link getBundleDescription}.
//     */
//    public BundleDescription getBundleDescription() {
//      return this._bundleDescription;
//    }
//  }
}
