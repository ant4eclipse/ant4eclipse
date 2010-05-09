package org.ant4eclipse.lib.pde.model.launcher;

import static org.ant4eclipse.lib.core.Assure.notNull;

/**
 * Represents information about a bundle that is selected inside a Equinox launch configuration, like it's name,
 * version, start level etc.
 * 
 * <p>
 * In Eclipse selected bundles inside a launch configuration are installed (and eventually started) into Equinox when
 * the launch configuration is executed
 * 
 * @author Nils Hartmann
 * 
 */
public class SelectedLaunchConfigurationBundle {

  /**
   * The symbolic name of the bundle. Nver null.
   */
  private final String _bundleSymbolicName;

  /**
   * The version of the selected bundle might be null
   */
  private final String _version;

  /**
   * The selected startLevel of the bundle. Never null
   */
  private final String _startLevel;

  /**
   * The autostart flag (true, false, default) of the selected bundle. Never null
   */
  private final String _autoStart;

  public SelectedLaunchConfigurationBundle(String bundleSymbolicName, String version, String startLevel,
      String autoStart) {
    notNull("bundleSymbolicName", bundleSymbolicName);
    notNull("startLevel", startLevel);
    notNull("autoStart", autoStart);

    this._bundleSymbolicName = bundleSymbolicName;
    this._version = version;
    this._startLevel = startLevel;
    this._autoStart = autoStart;
  }

  public String getBundleSymbolicName() {
    return this._bundleSymbolicName;
  }

  public String getVersion() {
    return this._version;
  }

  public String getStartLevel() {
    return this._startLevel;
  }

  public String getAutoStart() {
    return this._autoStart;
  }

  public boolean hasVersion() {
    return (this._version != null);
  }

}
