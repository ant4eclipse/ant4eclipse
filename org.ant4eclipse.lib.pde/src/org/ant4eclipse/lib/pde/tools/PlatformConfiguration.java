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
package org.ant4eclipse.lib.pde.tools;

import org.ant4eclipse.lib.core.A4ECore;
import org.ant4eclipse.lib.jdt.internal.model.jre.JavaProfileReader;
import org.ant4eclipse.lib.pde.model.pluginproject.Constants;
import org.eclipse.core.runtime.internal.adaptor.EclipseEnvironmentInfo;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

/**
 * TargetPlatformConfiguration --
 */
public class PlatformConfiguration {

  private boolean            _preferProjects = true;

  private Map<Object,Object> _configurationProperties;

  /**
   * 
   */
  public PlatformConfiguration() {
    _preferProjects = true;

    _configurationProperties = new HashMap<Object,Object>();

    _configurationProperties.putAll( A4ECore.instance().getRequiredService( JavaProfileReader.class )
        .readDefaultProfile().getProperties() );

    _configurationProperties.put( Constants.PROP_WS, EclipseEnvironmentInfo.getDefault().getWS() );
    _configurationProperties.put( Constants.PROP_OS, EclipseEnvironmentInfo.getDefault().getOS() );
    _configurationProperties.put( Constants.PROP_ARCH, EclipseEnvironmentInfo.getDefault().getOSArch() );
    _configurationProperties.put( Constants.PROP_NL, EclipseEnvironmentInfo.getDefault().getNL() );
  }

  public void setPreferProjects( boolean preferProjects ) {
    _preferProjects = preferProjects;
  }

  public void setOperatingSystem( String value ) {
    _configurationProperties.put( Constants.PROP_OS, value );
  }

  public void setArchitecture( String value ) {
    _configurationProperties.put( Constants.PROP_ARCH, value );
  }

  public void setWindowingSystem( String value ) {
    _configurationProperties.put( Constants.PROP_WS, value );
  }

  public void setProfileName( String value ) {
    _configurationProperties.put( "osgi.java.profile.name", value );
  }

  public void setLanguageSetting( String value ) {
    _configurationProperties.put( Constants.PROP_NL, value );
  }

  public boolean isPreferProjects() {
    return _preferProjects;
  }

  public Properties getConfigurationProperties() {
    Properties result = new Properties();
    Set<Map.Entry<Object,Object>> entrySet = _configurationProperties.entrySet();
    Iterator<Map.Entry<Object,Object>> iterator = entrySet.iterator();
    while( iterator.hasNext() ) {
      Map.Entry<Object,Object> entry = iterator.next();
      result.put( entry.getKey(), entry.getValue() );
    }
    return result;
  }

  public String getProfileName() {
    return (String) _configurationProperties.get( "osgi.java.profile.name" );
  }

  public String getLanguageSetting() {
    return (String) _configurationProperties.get( Constants.PROP_NL );
  }

  public String getOperatingSystem() {
    return (String) _configurationProperties.get( Constants.PROP_OS );
  }

  public String getArchitecture() {
    return (String) _configurationProperties.get( Constants.PROP_ARCH );
  }

  public String getWindowingSystem() {
    return (String) _configurationProperties.get( Constants.PROP_WS );
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public int hashCode() {
    int prime = 31;
    int result = 1;
    result = prime * result + ((_configurationProperties == null) ? 0 : _configurationProperties.hashCode());
    result = prime * result + (_preferProjects ? 1231 : 1237);
    return result;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean equals( Object obj ) {
    if( this == obj ) {
      return true;
    }
    if( obj == null ) {
      return false;
    }
    if( getClass() != obj.getClass() ) {
      return false;
    }
    PlatformConfiguration other = (PlatformConfiguration) obj;
    if( _configurationProperties == null ) {
      if( other._configurationProperties != null ) {
        return false;
      }
    } else if( !_configurationProperties.equals( other._configurationProperties ) ) {
      return false;
    }
    if( _preferProjects != other._preferProjects ) {
      return false;
    }
    return true;
  }

} /* ENDCLASS */
