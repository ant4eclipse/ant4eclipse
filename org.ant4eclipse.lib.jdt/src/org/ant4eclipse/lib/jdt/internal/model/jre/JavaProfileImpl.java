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
package org.ant4eclipse.lib.jdt.internal.model.jre;

import org.ant4eclipse.lib.core.util.ManifestHelper;
import org.ant4eclipse.lib.core.util.ManifestHelper.ManifestHeaderElement;
import org.ant4eclipse.lib.core.util.StringMap;
import org.ant4eclipse.lib.jdt.model.jre.JavaProfile;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * <p>
 * A JRE profile contains values for the properties <code>org.osgi.framework.system.packages</code>,
 * <code>org.osgi.framework.bootdelegation</code> and <code>org.osgi.framework.executionenvironment</code>.
 * 
 * <ul>
 * <li><b>org.osgi.framework.system.packages:</b> The system property <code>org.osgi.framework.system.packages</code>
 * contains the export packages descriptions for the system bundle. This property employs the standard Export-Package
 * manifest header syntax: <br/>
 * <br/>
 * 
 * <code>org.osgi.framework.system.packages ::= package-description ( ',' package-description )* </code><br/>
 * 
 * </li>
 * <li><b>org.osgi.framework.bootdelegation:</b></li>
 * <li><b>org.osgi.framework.executionenvironment:</b></li>
 * <li><b>osgi.java.profile.name:</b></li>
 * </ul>
 * </p>
 */
public class JavaProfileImpl implements JavaProfile {

  private static final String PROPERTY_SYSTEM_PACKAGES        = "org.osgi.framework.system.packages";

  private static final String PROPERTY_BOOTDELEGATION         = "org.osgi.framework.bootdelegation";

  private static final String PROPERTY_EXECUTIONENVIRONMENT   = "org.osgi.framework.executionenvironment";

  private static final String PROPERTY_PROFILE_NAME           = "osgi.java.profile.name";

  private StringMap           _properties;

  private List<String>        _systemPackagesList             = new ArrayList<String>();

  private List<PackageFilter> _delegatedToBootClassLoaderList = new ArrayList<PackageFilter>();

  private List<String>        _executionEnvironments          = new ArrayList<String>();

  private String              _associatedJavaRuntimeId;

  /**
   * @param properties
   */
  // Assure.notNull( "properties", properties );
  public JavaProfileImpl( StringMap properties ) {
    _properties = properties;
    initialise();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getName() {
    return _properties.get( JavaProfileImpl.PROPERTY_PROFILE_NAME );
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isSystemPackage( String packageName ) {
    return _systemPackagesList.contains( packageName );
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isDelegatedToBootClassLoader( String packageName ) {
    for( Object element : _delegatedToBootClassLoaderList ) {
      PackageFilter packageFilter = (PackageFilter) element;
      if( packageFilter.containsPackage( packageName ) ) {
        return true;
      }
    }
    return false;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public List<String> getExecutionEnvironmentNames() {
    return Collections.unmodifiableList( _executionEnvironments );
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public List<String> getSystemPackages() {
    return Collections.unmodifiableList( _systemPackagesList );
  }

  /**
   * <p>
   * </p>
   * 
   * @return
   */
  public String getAssociatedJavaRuntimeId() {
    return _associatedJavaRuntimeId;
  }

  /**
   * <p>
   * </p>
   * 
   * @param associatedJavaRuntimeId
   */
  public void setAssociatedJavaRuntimeId( String associatedJavaRuntimeId ) {
    _associatedJavaRuntimeId = associatedJavaRuntimeId;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public StringMap getProperties() {
    return _properties;
  }

  /**
   * 
   */
  private void initialise() {

    // set up system packages list...
    if( isNotEmpty( _properties.get( PROPERTY_SYSTEM_PACKAGES ) ) ) {

      // get HeaderElements
      ManifestHelper.ManifestHeaderElement[] headerElements = ManifestHelper
          .getManifestHeaderElements( _properties.get( PROPERTY_SYSTEM_PACKAGES ) );

      // iterate over result
      for( ManifestHeaderElement headerElement : headerElements ) {
        // get values (package names)
        String[] packageNames = headerElement.getValues();

        // add package names to string
        for( String packageName : packageNames ) {
          _systemPackagesList.add( packageName );
        }
      }
    }

    if( isNotEmpty( _properties.get( PROPERTY_BOOTDELEGATION ) ) ) {
      String[] packageDescriptions = _properties.get( PROPERTY_BOOTDELEGATION ).split( "," );
      for( String packageDescription : packageDescriptions ) {
        _delegatedToBootClassLoaderList.add( new PackageFilter( packageDescription ) );
      }
    }

    if( isNotEmpty( _properties.get( PROPERTY_EXECUTIONENVIRONMENT ) ) ) {
      String[] executionEnvironments = _properties.get( PROPERTY_EXECUTIONENVIRONMENT ).split( "," );
      for( String executionEnvironment : executionEnvironments ) {
        _executionEnvironments.add( executionEnvironment );
      }
    }
  }

  private boolean isNotEmpty( String string ) {
    return (string != null) && !"".equals( string.trim() );
  }

  /**
   * PackageFilter --
   * 
   * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
   */
  public class PackageFilter {

    private String[] _includedPackages;

    /**
     * @param includedPackages
     */
    public PackageFilter( String includedPackages ) {

      // set default value
      _includedPackages = includedPackages == null ? new String[] {} : includedPackages.split( "," );

      //
      for( int i = 0; i < _includedPackages.length; i++ ) {

        // replace OSGi wild cards (*) with regular expressions (.+)
        // NOTE: we're interpreting a * as "one or more characters here"
        // otherwise we have to use the regex ".*" which means "zero or more
        // characters here"
        _includedPackages[i] = _includedPackages[i].replaceAll( "\\*", ".+" );
      }
    }

    /**
     * <p>
     * </p>
     * 
     * @param packageName
     * @return
     */
    // Assure.notNull( "packageName", packageName );
    public boolean containsPackage( String packageName ) {
      for( String package1 : _includedPackages ) {
        if( matches( package1.trim(), packageName ) ) {
          return true;
        }
      }
      return false;
    }

    /**
     * <p>
     * </p>
     * 
     * @param osgiPattern
     * @param string
     * 
     * @return
     */
    // Assure.notNull( "osgiPattern", osgiPattern );
    // Assure.notNull( "string", string );
    private boolean matches( String osgiPattern, String string ) {
      return string.matches( osgiPattern );
    }
    
  } /* ENDCLASS */

  /**
   * {@inheritDoc}
   */
  @Override
  public String toString() {
    return String.format("[JavaProfile: _properties: %s]", _properties);
  }

} /* ENDCLASS */
