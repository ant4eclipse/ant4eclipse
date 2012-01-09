package org.ant4eclipse.lib.pde.model.launcher;

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
  private String _bundleSymbolicName;

  /**
   * The version of the selected bundle might be null
   */
  private String _version;

  /**
   * The selected startLevel of the bundle. Never null
   */
  private String _startLevel;

  /**
   * The autostart flag (true, false, default) of the selected bundle. Never null
   */
  private String _autoStart;

  // Assure.notNull( "bundleSymbolicName", bundleSymbolicName );
  // Assure.notNull( "startLevel", startLevel );
  // Assure.notNull( "autoStart", autoStart );
  public SelectedLaunchConfigurationBundle( String bundleSymbolicName, String version, String startLevel,
      String autoStart ) {
    _bundleSymbolicName = bundleSymbolicName;
    _version = version;
    _startLevel = startLevel;
    _autoStart = autoStart;
  }

  public String getBundleSymbolicName() {
    return _bundleSymbolicName;
  }

  public String getVersion() {
    return _version;
  }

  public String getStartLevel() {
    return _startLevel;
  }

  public String getAutoStart() {
    return _autoStart;
  }

  public boolean hasVersion() {
    return _version != null;
  }

} /* ENDCLASS */
