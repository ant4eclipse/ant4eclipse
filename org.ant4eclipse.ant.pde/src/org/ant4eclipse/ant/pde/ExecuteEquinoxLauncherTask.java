package org.ant4eclipse.ant.pde;

import static org.ant4eclipse.lib.core.Assure.notNull;
import static org.ant4eclipse.lib.core.logging.A4ELogging.warn;

import java.io.File;

import org.ant4eclipse.ant.core.FileListHelper;
import org.ant4eclipse.ant.platform.ExecuteLauncherTask;
import org.ant4eclipse.ant.platform.core.MacroExecutionValues;
import org.ant4eclipse.ant.platform.core.delegate.MacroExecutionValuesProvider;
import org.ant4eclipse.lib.core.service.ServiceRegistry;
import org.ant4eclipse.lib.core.util.StringMap;
import org.ant4eclipse.lib.pde.model.launcher.EquinoxLaunchConfigurationWrapper;
import org.ant4eclipse.lib.pde.model.launcher.SelectedLaunchConfigurationBundle;
import org.ant4eclipse.lib.pde.model.pluginproject.BundleSource;
import org.ant4eclipse.lib.pde.tools.TargetPlatform;
import org.ant4eclipse.lib.pde.tools.TargetPlatformConfiguration;
import org.ant4eclipse.lib.pde.tools.TargetPlatformRegistry;
import org.ant4eclipse.lib.platform.model.launcher.LaunchConfiguration;
import org.ant4eclipse.lib.platform.model.resource.EclipseProject;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.taskdefs.MacroDef;
import org.eclipse.osgi.service.resolver.BundleDescription;
import org.osgi.framework.Version;

/**
 * Executes a Equinox Launch Configuration (type="org.eclipse.pde.ui.EquinoxLauncher")
 * 
 * @author Nils Hartmann (nils@nilshartmann.net)
 * 
 */
public class ExecuteEquinoxLauncherTask extends ExecuteLauncherTask {

  /**
   * The forEachBundle scope that is invoked for each bundle that is selected either from the workspace or the target
   * platform
   */
  public static final String SCOPE_FOR_EACH_SELECTED_BUNDLE = "forEachSelectedBundle";

  /**
   * The id of the target platform
   */
  private String             _targetPlatformId;

  /**
   * The target platform that is used to resolve the selected bundles
   */
  private TargetPlatform     _targetPlatform;

  /**
   * Create new instance
   */
  public ExecuteEquinoxLauncherTask() {
    super("executeEquinoxLauncher");
  }

  /**
   * @return the id of the target platform that should be used to resolve the selected bundles
   */
  public String getTargetPlatformId() {
    return this._targetPlatformId;
  }

  /**
   * Sets the id of the target platform that should be used to resolve the selected bundles
   * 
   * @param targetPlatformId
   *          id of the target platform
   */
  public void setTargetPlatformId(String targetPlatformId) {
    this._targetPlatformId = targetPlatformId;
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

    if (this._targetPlatformId == null) {
      throw new BuildException("You must set argument 'targetPlatformId'");
    }
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
   * <p>
   * Helper method. Initializes the target platform.
   * </p>
   */
  private void initTargetPlatform() {

    // create the configuration
    TargetPlatformConfiguration configuration = new TargetPlatformConfiguration();
    configuration.setPreferProjects(true);

    // get the target platform registry
    TargetPlatformRegistry targetPlatformRegistry = ServiceRegistry.instance().getService(TargetPlatformRegistry.class);

    // set the target platform
    this._targetPlatform = targetPlatformRegistry.getInstance(getWorkspace(), getTargetPlatformId(), configuration);
  }

  /**
   * Run the 'forEachSelectedBundle' macro
   * 
   * @param macroDef
   */
  private void executeForEachSelectedBundleScopedMacroDef(MacroDef macroDef) {

    // Step 1: initialise and resolve the target platform
    initTargetPlatform();

    // Step 2: read the launchconfiguration file
    final LaunchConfiguration launchConfiguration = getLaunchConfiguration();

    // Step 3: Create a EquinoxLaunchConfigurationWrapper to access Equinox-specific settings in launch config
    EquinoxLaunchConfigurationWrapper wrapper = new EquinoxLaunchConfigurationWrapper(launchConfiguration);

    // Step 4: Get all selected workspace bundles
    final SelectedLaunchConfigurationBundle[] selectedWorkspaceBundles = wrapper.getSelectedWorkspaceBundles();

    // Step 4a: run the macro for each workspace bundle
    for (SelectedLaunchConfigurationBundle selectedWorkspaceBundle : selectedWorkspaceBundles) {
      executeMacroInstance(macroDef, createBundleMacroExecuteValuesProvider(selectedWorkspaceBundle, true));
    }

    // Step 5: Get all selected bundles from target platform
    final SelectedLaunchConfigurationBundle[] selectedTargetBundles = wrapper.getSelectedTargetBundles();

    // Step 5a: Run the macro for each selected target platform bundle
    for (SelectedLaunchConfigurationBundle selectedTargetBundle : selectedTargetBundles) {
      executeMacroInstance(macroDef, createBundleMacroExecuteValuesProvider(selectedTargetBundle, false));
    }

  }

  /**
   * Creates a {@link MacroExecutionValuesProvider} for the given selected bundle.
   * 
   * <p>
   * The providered that is returned contains all properties and references that should passed to the macro
   * 
   * @param selectedBundle
   * @param workspaceBundle
   * @return
   */
  protected MacroExecutionValuesProvider createBundleMacroExecuteValuesProvider(
      final SelectedLaunchConfigurationBundle selectedBundle, final boolean workspaceBundle) {
    notNull("launchConfigurationBundleInfo", selectedBundle);

    final BundleDescription bundleDescription = getResolvedBundle(selectedBundle);

    return new MacroExecutionValuesProvider() {
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

        if (bundleDescription != null) {
          BundleSource bundlesource = (BundleSource) bundleDescription.getUserObject();

          properties.put("resolvedBundle.version", bundleDescription.getVersion().toString());

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

    BundleDescription bundleDescription = this._targetPlatform.getResolvedBundle(bundleSymbolicName, osgiVersion);
    if (bundleDescription == null) {
      warn("Bundle '%s' with version '%s' not found in target platform", bundleSymbolicName, osgiVersion);
    }
    return bundleDescription;
  }

}
