package org.ant4eclipse.lib.pde.model.launcher;

/**
 * @author nils
 * 
 */
public class SelectedLaunchConfigurationBundleParser {

  /**
   * Parses a String with a selected bundle (i.e. <code>com.springsource.javassist*3.3.0.ga@11:default</code>)
   * 
   * @param bundleInfo
   * @return
   */
  public SelectedLaunchConfigurationBundle parseLaunchConfigurationBundleInfo( String bundleInfo ) {

    int i = bundleInfo.indexOf( '@' );
    if( i == -1 ) {
      // not sure if this will ever happen
      return new SelectedLaunchConfigurationBundle( bundleInfo, null, "default", "default" );
    }

    String version = null;
    String bundleSymbolicName = null;
    String startLevel = "default";
    String autoStart = "default";

    // Parse the name and (optionally) the version
    final String nameAndVersion = bundleInfo.substring( 0, i );
    int j = nameAndVersion.indexOf( '*' );
    if( j == -1 ) {
      bundleSymbolicName = nameAndVersion;
    } else {
      bundleSymbolicName = nameAndVersion.substring( 0, j );
      version = nameAndVersion.substring( j + 1 );
    }

    // Parse startLevel and autoStart parameter
    if( i < bundleInfo.length() - 1 ) {
      final String startLevelAndAutoStart = bundleInfo.substring( i + 1 );

      int k = startLevelAndAutoStart.indexOf( ':' );
      if( k == -1 ) {
        startLevel = startLevelAndAutoStart;
      } else {
        startLevel = startLevelAndAutoStart.substring( 0, k );
        autoStart = startLevelAndAutoStart.substring( k + 1 );
      }
    }

    // Return the a SelectedLaunchConfigurationBundle with the parsed information
    return new SelectedLaunchConfigurationBundle( bundleSymbolicName, version, startLevel, autoStart );

  }

} /* ENDCLASS */
