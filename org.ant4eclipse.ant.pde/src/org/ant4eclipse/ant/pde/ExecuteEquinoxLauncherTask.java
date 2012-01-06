package org.ant4eclipse.ant.pde;

import static org.ant4eclipse.lib.core.Assure.notNull;
import static org.ant4eclipse.lib.core.logging.A4ELogging.warn;

import org.ant4eclipse.ant.core.FileListHelper;
import org.ant4eclipse.ant.platform.ExecuteLauncherTask;
import org.ant4eclipse.ant.platform.core.MacroExecutionValues;
import org.ant4eclipse.ant.platform.core.delegate.MacroExecutionValuesProvider;
import org.ant4eclipse.lib.core.A4ECore;
import org.ant4eclipse.lib.core.util.StringMap;
import org.ant4eclipse.lib.core.util.Utilities;
import org.ant4eclipse.lib.jdt.model.jre.JavaRuntime;
import org.ant4eclipse.lib.jdt.model.jre.JavaRuntimeRegistry;
import org.ant4eclipse.lib.pde.model.launcher.EquinoxLaunchConfigurationWrapper;
import org.ant4eclipse.lib.pde.model.launcher.SelectedLaunchConfigurationBundle;
import org.ant4eclipse.lib.pde.model.pluginproject.BundleSource;
import org.ant4eclipse.lib.platform.model.launcher.LaunchConfiguration;
import org.ant4eclipse.lib.platform.model.resource.EclipseProject;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.taskdefs.MacroDef;
import org.eclipse.osgi.service.resolver.BundleDescription;
import org.osgi.framework.Version;

import java.io.File;

/**
 * Executes a Equinox Launch Configuration (type="org.eclipse.pde.ui.EquinoxLauncher")
 * 
 * @author Nils Hartmann (nils@nilshartmann.net)
 */
public class ExecuteEquinoxLauncherTask extends ExecuteLauncherTask implements TargetPlatformAwareComponent {

  /**
   * The forEachBundle scope that is invoked for each bundle that is selected either from the workspace or the target
   * platform
   */
  public static final String          SCOPE_FOR_EACH_SELECTED_BUNDLE = "forEachSelectedBundle";

  /** - */
  private TargetPlatformAwareDelegate _targetPlatformAwareDelegate;

  /**
   * Create new instance
   */
  public ExecuteEquinoxLauncherTask() {
    super("executeEquinoxLauncher");

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
   * Makes sure the selected launch configuration is a PDE-Launch Configuration
   */
  @Override
  protected void ensureSupportedLaunchConfiguration(LaunchConfiguration launchConfiguration) {
    super.ensureSupportedLaunchConfiguration(launchConfiguration);

    if (!EquinoxLaunchConfigurationWrapper.isEquinoxLaunchConfiguration(launchConfiguration)) {
      throw new BuildException("The launch configuration you've specified must be a Equinox launch configuration");
    }
  }

  @Override
  public Object createDynamicElement(String name) throws BuildException {
    // handle 'forEachSelectedBundle' element
    if (SCOPE_FOR_EACH_SELECTED_BUNDLE.equalsIgnoreCase(name)) {
      return createScopedMacroDefinition(SCOPE_FOR_EACH_SELECTED_BUNDLE);
    }

    // all other scopes: not handled here, try super class
    return super.createDynamicElement(name);
  }

  /**
   * Make sure all preconditions are fulfilled before executing the task
   */
  @Override
  protected void preconditions() throws BuildException {
    super.preconditions();

    this._targetPlatformAwareDelegate.requireTargetPlatformIdSet();
  }

  /**
   * Execute the scoped macro definitions
   */
  @Override
  protected void doExecute(String scope, MacroDef macroDef) {
    if (SCOPE_FOR_EACH_SELECTED_BUNDLE.equals(scope)) {
      executeForEachSelectedBundleScopedMacroDef(macroDef);
    } else {
      super.doExecute(scope, macroDef);
    }
  }

  /**
   * Run the 'forEachSelectedBundle' macro
   * 
   * @param macroDef
   */
  private void executeForEachSelectedBundleScopedMacroDef(MacroDef macroDef) {

    // Step 1: read the launchconfiguration file
    final LaunchConfiguration launchConfiguration = getLaunchConfiguration();

    // Step 2: Create a EquinoxLaunchConfigurationWrapper to access Equinox-specific settings in launch config
    EquinoxLaunchConfigurationWrapper wrapper = new EquinoxLaunchConfigurationWrapper(launchConfiguration);

    // Step 3: Get all selected workspace bundles
    final SelectedLaunchConfigurationBundle[] selectedWorkspaceBundles = wrapper.getSelectedWorkspaceBundles();

    // Step 3a: run the macro for each workspace bundle
    for (SelectedLaunchConfigurationBundle selectedWorkspaceBundle : selectedWorkspaceBundles) {
      executeMacroInstance(macroDef, createBundleMacroExecuteValuesProvider(wrapper, selectedWorkspaceBundle, true));
    }

    // Step 4: Get all selected bundles from target platform
    final SelectedLaunchConfigurationBundle[] selectedTargetBundles = wrapper.getSelectedTargetBundles();

    // Step 4a: Run the macro for each selected target platform bundle
    for (SelectedLaunchConfigurationBundle selectedTargetBundle : selectedTargetBundles) {
      executeMacroInstance(macroDef, createBundleMacroExecuteValuesProvider(wrapper, selectedTargetBundle, false));
    }

  }

  /**
   * Creates a {@link MacroExecutionValuesProvider} for the given selected bundle.
   * 
   * <p>
   * The providered that is returned contains all properties and references that should passed to the macro
   * 
   * @param wrapper
   * @param selectedBundle
   * @param workspaceBundle
   * @return
   */
  protected MacroExecutionValuesProvider createBundleMacroExecuteValuesProvider(
      final EquinoxLaunchConfigurationWrapper wrapper, final SelectedLaunchConfigurationBundle selectedBundle,
      final boolean workspaceBundle) {
    notNull("wrapper", wrapper);
    notNull("launchConfigurationBundleInfo", selectedBundle);

    final BundleDescription bundleDescription = getResolvedBundle(selectedBundle);

    return new MacroExecutionValuesProvider() {
      @Override
      public MacroExecutionValues provideMacroExecutionValues(MacroExecutionValues values) {
        values = provideDefaultMacroExecutionValues(values);

        final StringMap properties = values.getProperties();
        properties.put("selectedBundle.symbolicName", selectedBundle.getBundleSymbolicName());
        String version = selectedBundle.hasVersion() ? selectedBundle.getVersion() : "";
        properties.put("selectedBundle.version", version);
        properties.put("selectedBundle.startLevel", selectedBundle.getStartLevel());
        properties.put("selectedBundle.autoStart", selectedBundle.getAutoStart());
        properties.put("selectedBundle.workspaceBundle", Boolean.toString(workspaceBundle));
        properties.put("selectedBundle.targetBundle", Boolean.toString(!workspaceBundle));
        final String resolvedStartLevel = wrapper.getResolvedStartLevel(selectedBundle);
        properties.put("selectedBundle.resolvedStartLevel", resolvedStartLevel);
        final String resolvedAutoStart = wrapper.getResolvedAutoStart(selectedBundle);
        properties.put("selectedBundle.resolvedAutoStart", resolvedAutoStart);

        // set the "bundle start parameter" as expected in osgi.bundles property
        String bundleStart = "";
        if (Utilities.hasText(resolvedStartLevel)) {
          bundleStart = "@" + resolvedStartLevel;
        }
        if (Utilities.hasText(resolvedAutoStart)) {
          if (Utilities.hasText(bundleStart)) {
            bundleStart += ":" + resolvedAutoStart;
          } else {
            bundleStart = "@" + resolvedAutoStart;
          }
        }

        properties.put("selectedBundle.startParameter", bundleStart);

        if (bundleDescription != null) {
          BundleSource bundlesource = (BundleSource) bundleDescription.getUserObject();

          properties.put("resolvedBundle.version", bundleDescription.getVersion().toString());
          properties.put("resolvedBundle.isSystemBundle", Boolean.toString(bundleDescription.getBundleId() == 0));
          if (bundlesource.isEclipseProject()) {
            // Plug-in is a source project contained in the workspace
            EclipseProject project = bundlesource.getAsEclipseProject();
            File location = project.getFolder();
            properties.put("resolvedBundle.isSource", "true");
            properties.put("resolvedBundle.file", location.getAbsolutePath());
            properties.put("resolvedBundle.fileName", location.getName());
            properties.put("resolvedBundle.projectName", project.getSpecifiedName());
          } else {
            // Bundle comes from the target platform
            File location = bundlesource.getAsFile();
            properties.put("resolvedBundle.isSource", "false");
            properties.put("resolvedBundle.file", location.getAbsolutePath());
            properties.put("resolvedBundle.fileName", location.getName());
            values.getReferences().put("resolvedBundle.fileList", FileListHelper.getFileList(location));
          }
        }
        return values;
      }
    };
  }

  /**
   * Returns the resolved bundle from the target platform
   * 
   * @param launchConfigurationBundleInfo
   * @return the {@link BundleDescription} for the selected bundle or null if the bundle could not be resolved
   */
  protected BundleDescription getResolvedBundle(SelectedLaunchConfigurationBundle launchConfigurationBundleInfo) {
    String bundleSymbolicName = launchConfigurationBundleInfo.getBundleSymbolicName();
    Version osgiVersion = (launchConfigurationBundleInfo.hasVersion() ? new Version(launchConfigurationBundleInfo
        .getVersion()) : null);

    BundleDescription bundleDescription = this._targetPlatformAwareDelegate.getTargetPlatform(getWorkspace())
        .getResolvedBundle(bundleSymbolicName, osgiVersion);

    if (bundleDescription == null) {
      warn("Bundle '%s' with version '%s' not found in target platform", bundleSymbolicName, osgiVersion);
    }

    return bundleDescription;
  }

  @Override
  protected MacroExecutionValues provideDefaultMacroExecutionValues(MacroExecutionValues values) {
    final MacroExecutionValues defaultValues = super.provideDefaultMacroExecutionValues(values);

    // Add JRE-Location to scoped properties
    JavaRuntime javaRuntime = getJavaRuntime();
    defaultValues.getProperties().put("jre.location", javaRuntime.getLocation().getAbsolutePath());

    return defaultValues;
  }

  /**
   * Returns the Java Runtime for the current launch configuration
   * 
   * @return
   */
  protected JavaRuntime getJavaRuntime() {
    LaunchConfiguration launchConfiguration = getLaunchConfiguration();
    JavaRuntimeRegistry javaRuntimeRegistry = A4ECore.instance().getRequiredService( JavaRuntimeRegistry.class );

    String vmPath = launchConfiguration.getAttribute("org.eclipse.jdt.launching.JRE_CONTAINER");
    if (vmPath == null) {
      return javaRuntimeRegistry.getDefaultJavaRuntime();
    }

    JavaRuntime javaRuntime = javaRuntimeRegistry.getJavaRuntimeForPath(vmPath);
    return javaRuntime;

  }

}
