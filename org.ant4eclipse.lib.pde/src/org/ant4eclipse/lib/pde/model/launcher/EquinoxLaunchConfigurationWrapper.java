package org.ant4eclipse.lib.pde.model.launcher;

import static org.ant4eclipse.lib.core.Assure.assertTrue;
import static org.ant4eclipse.lib.core.Assure.notNull;

import java.util.LinkedList;
import java.util.List;
import java.util.StringTokenizer;

import org.ant4eclipse.lib.platform.model.launcher.LaunchConfiguration;

/**
 * A wrapper around a Equinox-based {@link LaunchConfiguration}. This class adds several helper methods to access the
 * Equinox-specific information of the launch configuration file.
 * 
 * @author nils hartmann
 * 
 */
public class EquinoxLaunchConfigurationWrapper {

  /**
   * The supported launch configuration type
   */
  public final static String        EQUINOX_LAUNCH_CONFIGURATION_TYPE = "org.eclipse.pde.ui.EquinoxLauncher";

  /**
   * Name of the attribute that contains the list with selected bundles from the target platform
   */
  public static final String        TARGET_BUNDLES_ATTRIBUTE_NAME     = "target_bundles";

  /**
   * Name of the attribute that contains the list with selected bundles from the workspace
   */
  public static final String        WORKSPACE_BUNDLES_ATTRIBUTE_NAME  = "workspace_bundles";

  /**
   * The wrapped launch configuration. Must not be null
   */
  private final LaunchConfiguration _launchConfiguration;

  /**
   * Returns true if the specified LaunchConfiguration represents a Equinox launch configuration
   * 
   * @param launchConfiguration
   * @return
   */
  public static boolean isEquinoxLaunchConfiguration(LaunchConfiguration launchConfiguration) {
    notNull("launchConfiguration", launchConfiguration);
    return (EQUINOX_LAUNCH_CONFIGURATION_TYPE.equals(launchConfiguration.getType()));
  }

  /**
   * Creates a new {@link EquinoxLaunchConfigurationWrapper} for the specified launchConfiguration.
   * 
   * @param launchConfiguration
   *          A Equinox LaunchConfiguration
   */
  public EquinoxLaunchConfigurationWrapper(LaunchConfiguration launchConfiguration) {
    notNull("launchConfiguration", launchConfiguration);
    assertTrue(isEquinoxLaunchConfiguration(launchConfiguration), "Launch configuration must be of type '"
        + EQUINOX_LAUNCH_CONFIGURATION_TYPE + "'");

    this._launchConfiguration = launchConfiguration;
  }

  /**
   * Returns a list of all selected bundles from the workspace, i.e. bundles that are going to be installed (and
   * eventually started) when Equinox is launched
   * 
   * @return never null
   */
  public SelectedLaunchConfigurationBundle[] getSelectedWorkspaceBundles() {
    String workspaceBundles = this._launchConfiguration.getAttribute(WORKSPACE_BUNDLES_ATTRIBUTE_NAME);

    return createSelectedLaunchConfigurationBundle(workspaceBundles);
  }

  /**
   * Returns a list of all selected bundles from the target platform, i.e. bundles that are going to be installed (and
   * eventually started) when Equinox is launched
   * 
   * @return
   */
  public SelectedLaunchConfigurationBundle[] getSelectedTargetBundles() {
    String workspaceBundles = this._launchConfiguration.getAttribute(TARGET_BUNDLES_ATTRIBUTE_NAME);

    return createSelectedLaunchConfigurationBundle(workspaceBundles);
  }

  private SelectedLaunchConfigurationBundle[] createSelectedLaunchConfigurationBundle(String bundles) {

    List<SelectedLaunchConfigurationBundle> result = new LinkedList<SelectedLaunchConfigurationBundle>();
    StringTokenizer tokenizer = new StringTokenizer(bundles, ",", false);

    SelectedLaunchConfigurationBundleParser parser = new SelectedLaunchConfigurationBundleParser();

    while (tokenizer.hasMoreElements()) {
      String bundleInfoString = tokenizer.nextToken();
      final SelectedLaunchConfigurationBundle launchConfigurationBundleInfo = parser
          .parseLaunchConfigurationBundleInfo(bundleInfoString);
      result.add(launchConfigurationBundleInfo);
    }

    return result.toArray(new SelectedLaunchConfigurationBundle[0]);
  }
}
