package org.ant4eclipse.ant.pde;

import org.ant4eclipse.ant.pde.analysis.TestClassAnalyser;
import org.ant4eclipse.ant.platform.ExecuteLauncherTask;
import org.ant4eclipse.ant.platform.core.MacroExecutionValues;
import org.ant4eclipse.lib.core.A4ECore;
import org.ant4eclipse.lib.core.logging.A4ELogging;
import org.ant4eclipse.lib.core.osgi.BundleLayoutResolver;
import org.ant4eclipse.lib.core.util.StringMap;
import org.ant4eclipse.lib.jdt.model.jre.JavaRuntime;
import org.ant4eclipse.lib.jdt.model.jre.JavaRuntimeRegistry;
import org.ant4eclipse.lib.jdt.model.project.JavaProjectRole;
import org.ant4eclipse.lib.pde.internal.tools.BundleDependenciesResolver;
import org.ant4eclipse.lib.pde.internal.tools.BundleDependenciesResolver.BundleDependency;
import org.ant4eclipse.lib.pde.internal.tools.UnresolvedBundleException;
import org.ant4eclipse.lib.pde.model.launcher.EquinoxLaunchConfigurationWrapper;
import org.ant4eclipse.lib.pde.model.pluginproject.BundleSource;
import org.ant4eclipse.lib.pde.model.pluginproject.PluginProjectRole;
import org.ant4eclipse.lib.pde.tools.TargetPlatform;
import org.ant4eclipse.lib.platform.model.launcher.LaunchConfiguration;
import org.apache.tools.ant.BuildException;
import org.eclipse.osgi.service.resolver.BundleDescription;

import java.io.File;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * <p>
 * Executes a PDE JUnit Launch Configuration (type="org.eclipse.pde.ui.EquinoxLauncher")
 * </p>
 * <p>
 * TODOS:
 * <ul>
 * <li>Support for 'All workspace and enabled target plug-ins'</li>
 * <li>If the test bundle is a fragment bundle, the host must be set as test bundle</li>
 * <li></li>
 * </ul>
 * </p>
 * 
 * @author Nils Hartmann (nils@nilshartmann.net)
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 */
public class ExecutePdeJunitLauncherTask extends ExecuteLauncherTask implements TargetPlatformAwareComponent {

  /** the target platform delegate */
  private TargetPlatformAwareDelegate _targetPlatformAwareDelegate;

  /** the 'bundles.info' string builder */
  private StringBuilder               _bundlesInfo;

  /** the 'osgi.bundles' string builder */
  private StringBuilder               _osgiBundles;

  /** the 'dev.properties' string builder */
  private StringBuilder               _devProperties;

  /**
   * <p>
   * Creates a new instance of type {@link ExecutePdeJunitLauncherTask}.
   * </p>
   */
  public ExecutePdeJunitLauncherTask() {
    super("executePdeJunitLauncher");

    // create the delegate
    this._targetPlatformAwareDelegate = new TargetPlatformAwareDelegate();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getPlatformConfigurationId() {
    return this._targetPlatformAwareDelegate.getPlatformConfigurationId();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public final String getTargetPlatformId() {
    return this._targetPlatformAwareDelegate.getTargetPlatformId();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isPlatformConfigurationIdSet() {
    return this._targetPlatformAwareDelegate.isPlatformConfigurationIdSet();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public final boolean isTargetPlatformIdSet() {
    return this._targetPlatformAwareDelegate.isTargetPlatformIdSet();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setPlatformConfigurationId(String platformConfigurationId) {
    this._targetPlatformAwareDelegate.setPlatformConfigurationId(platformConfigurationId);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public final void setTargetPlatformId(String targetPlatformId) {
    this._targetPlatformAwareDelegate.setTargetPlatformId(targetPlatformId);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public final void requireTargetPlatformIdSet() {
    this._targetPlatformAwareDelegate.requireTargetPlatformIdSet();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected void ensureSupportedLaunchConfiguration(LaunchConfiguration launchConfiguration) {
    super.ensureSupportedLaunchConfiguration(launchConfiguration);

    // makes sure the selected launch configuration is a PDE-Launch Configuration
    if (!EquinoxLaunchConfigurationWrapper.isPdeJunitLaunchConfiguration(launchConfiguration)) {
      throw new BuildException("The launch configuration you've specified must be a Equinox launch configuration");
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected void preconditions() throws BuildException {
    super.preconditions();

    //
    this._targetPlatformAwareDelegate.requireTargetPlatformIdSet();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected MacroExecutionValues provideDefaultMacroExecutionValues(MacroExecutionValues values) {
    final MacroExecutionValues defaultValues = super.provideDefaultMacroExecutionValues(values);

    // add JRE-Location to scoped properties
    JavaRuntime javaRuntime = getJavaRuntime();
    defaultValues.getProperties().put("jre.location", javaRuntime.getLocation().getAbsolutePath());

    StringMap jrtProperties = javaRuntime.getJavaProfile().getProperties();
    defaultValues.getProperties().put("org.osgi.framework.system.packages",
        jrtProperties.get("org.osgi.framework.system.packages"));
    defaultValues.getProperties().put("org.osgi.framework.bootdelegation",
        jrtProperties.get("org.osgi.framework.bootdelegation"));
    defaultValues.getProperties().put("org.osgi.framework.executionenvironment",
        jrtProperties.get("org.osgi.framework.executionenvironment"));

    // compute the bundle information
    computeBundlesInfo();
    defaultValues.getProperties().put("bundles.info", this._bundlesInfo.toString());
    defaultValues.getProperties().put("osgi.bundles", this._osgiBundles.toString());
    defaultValues.getProperties().put("dev.properties", this._devProperties.toString());

    // set the osgi framework
    BundleDescription osgiFramework = this._targetPlatformAwareDelegate.getTargetPlatform(getWorkspace())
        .getResolvedBundle("org.eclipse.osgi", null);
    if (osgiFramework == null) {
      throw new RuntimeException("Bundle 'org.eclipse.osgi' is missing.");
    }
    defaultValues.getProperties().put("osgi.framework", osgiFramework.getLocation());

    // set the test plug-in name and location
    PluginProjectRole pluginProjectRole = getEclipseProject().getRole(PluginProjectRole.class);
    BundleDescription bundleDescription = pluginProjectRole.getBundleDescription();
    bundleDescription = this._targetPlatformAwareDelegate.getTargetPlatform(getWorkspace()).getResolvedBundle(
        bundleDescription.getSymbolicName(), bundleDescription.getVersion());
    BundleDescription bundleHost = BundleDependenciesResolver.getHost(bundleDescription);

    defaultValues.getProperties().put("testplugin.bundlehost.name", bundleHost.getSymbolicName());
    defaultValues.getProperties().put("testplugin.name", bundleDescription.getSymbolicName());
    defaultValues.getProperties().put("testplugin.location", bundleDescription.getLocation());

    // find contained test classes
    defaultValues.getProperties().put("test.classes",
        new TestClassAnalyser(getEclipseProject()).getTestClassesAsString());

    // compute the 'pde.test.utils' class path
    try {
      BundleDescription b = this._targetPlatformAwareDelegate.getTargetPlatform(getWorkspace()).getResolvedBundle(
          "pde.test.utils", null);
      StringBuilder collectorClassPath = new StringBuilder();
      for (BundleDependency dependency : new BundleDependenciesResolver().resolveBundleClasspath(b)) {
        buildClassPath(dependency.getHost(), collectorClassPath);
      }
      buildClassPath(b, collectorClassPath);
      defaultValues.getProperties().put("collectorCP", collectorClassPath.toString());
    } catch (UnresolvedBundleException e) {
      e.printStackTrace();
    }

    //
    return defaultValues;
  }

  /**
   * <p>
   * </p>
   * 
   * @param bundleDescription
   * @param classpath
   */
  private void buildClassPath(BundleDescription bundleDescription, StringBuilder classpath) {

    // get the layout resolver
    BundleLayoutResolver resolver = BundleDependenciesResolver.getBundleLayoutResolver(bundleDescription);

    // add all entries
    File[] entries = resolver.resolveBundleClasspathEntries();
    for (File file : entries) {
      classpath.append(file.getAbsolutePath());
      classpath.append(File.pathSeparatorChar);
    }
  }

  /**
   * <p>
   * </p>
   */
  private void computeBundlesInfo() {

    // initialize result
    this._bundlesInfo = new StringBuilder();
    this._osgiBundles = new StringBuilder();
    this._devProperties = new StringBuilder();
    this._devProperties.append("@ignoredot@=true\n");

    // compute contained bundles
    computeBundles(getLaunchConfiguration().getAttribute("selected_workspace_plugins"), true);
    computeBundles(getLaunchConfiguration().getAttribute("selected_target_plugins"), false);

    // add additional bundles
    BundleDescription b = this._targetPlatformAwareDelegate.getTargetPlatform(getWorkspace()).getResolvedBundle(
        "org.eclipse.pde.junit.runtime", null);
    handle(b.getSymbolicName(), "4", "false", false);
    b = this._targetPlatformAwareDelegate.getTargetPlatform(getWorkspace()).getResolvedBundle(
        "org.eclipse.jdt.junit4.runtime", null);
    handle(b.getSymbolicName(), "4", "false", false);
    try {
      List<BundleDependency> dependencies = new BundleDependenciesResolver().resolveBundleClasspath(b);
      for (BundleDependency bundleDependency : dependencies) {
        // TODO: fragments
        handle(bundleDependency.getHost().getSymbolicName(), "4", "false", false);
      }
    } catch (UnresolvedBundleException e) {
      e.printStackTrace();
    }
  }

  /**
   * <p>
   * </p>
   * 
   * @param bundleList
   * @param takeBundlesFromWorkspace
   */
  private void computeBundles(String bundleList, boolean takeBundlesFromWorkspace) {

    String[] workspacePlugins = bundleList.split(",");

    for (String workspacePlugin : workspacePlugins) {
      Pattern pattern = Pattern.compile("(.*)@(.*):(.*)");
      Matcher matcher = pattern.matcher(workspacePlugin);
      matcher.matches();

      handle(matcher.group(1), matcher.group(2), matcher.group(3), takeBundlesFromWorkspace);
    }
  }

  /**
   * <p>
   * </p>
   * 
   * @param symName
   * @param startLevel
   * @param autoStart
   * @param workspaceBundles
   */
  private void handle(String symName, String startLevel, String autoStart, boolean workspaceBundles) {

    //
    if (symName.indexOf('*') != -1) {
      symName = symName.split("\\*")[0];
    }

    //
    TargetPlatform targetPlatform = this._targetPlatformAwareDelegate.getTargetPlatform(getWorkspace());
    BundleDescription description = workspaceBundles ? targetPlatform.getBundleDescriptionFromWorkspace(symName)
        : targetPlatform.getBundleDescriptionFromBinaryBundles(symName);

    //
    if (description == null) {
      A4ELogging.warn("Bundle '%s' does not exist!", symName);
      return;
    }

    //
    if (description.getBundleId() != 0) {

      //
      BundleSource bundleSource = (BundleSource) description.getUserObject();

      //
      if (bundleSource.isEclipseProject()) {

        //
        JavaProjectRole javaProjectRole = bundleSource.getAsEclipseProject().getRole(JavaProjectRole.class);
        PluginProjectRole pluginProjectRole = bundleSource.getAsEclipseProject().getRole(PluginProjectRole.class);

        //
        this._devProperties.append(pluginProjectRole.getBundleDescription().getSymbolicName());
        this._devProperties.append("=");
        this._devProperties.append(javaProjectRole.getDefaultOutputFolder());
        this._devProperties.append("\n");
      }

      this._bundlesInfo.append(String.format("%s,%s,file:/%s,4,%s\n", description.getSymbolicName(),
          description.getVersion(), description.getLocation(), autoStart.equals("true")));

      this._osgiBundles.append(String.format("reference\\:file\\:%s", description.getLocation().replace("\\", "/")));
      if (autoStart.equals("true")) {
        this._osgiBundles.append("@start");
      }

      this._osgiBundles.append(",");
    }
  }

  /**
   * <p>
   * Returns the Java Runtime for the current launch configuration.
   * </p>
   * 
   * @return
   */
  protected JavaRuntime getJavaRuntime() {

    // get the java runtime registry
    JavaRuntimeRegistry javaRuntimeRegistry = A4ECore.instance().getRequiredService( JavaRuntimeRegistry.class );

    // get the vm path
    String vmPath = getLaunchConfiguration().getAttribute("org.eclipse.jdt.launching.JRE_CONTAINER");
    if (vmPath == null) {
      return javaRuntimeRegistry.getDefaultJavaRuntime();
    }

    // return the runtime
    return javaRuntimeRegistry.getJavaRuntimeForPath(vmPath);
  }

}
