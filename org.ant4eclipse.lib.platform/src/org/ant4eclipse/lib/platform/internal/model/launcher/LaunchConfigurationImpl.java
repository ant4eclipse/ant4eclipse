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
package org.ant4eclipse.lib.platform.internal.model.launcher;

import java.util.Collection;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import org.ant4eclipse.lib.core.Assure;
import org.ant4eclipse.lib.platform.internal.model.launcher.LaunchConfigAttribute.ListAttribute;
import org.ant4eclipse.lib.platform.model.launcher.LaunchConfiguration;

public class LaunchConfigurationImpl implements LaunchConfiguration {

  private String                            _type;

  private Map<String,LaunchConfigAttribute> _attributes;

  public LaunchConfigurationImpl( String type, List<LaunchConfigAttribute> attributes ) {
    super();
    _type = type;
    _attributes = new Hashtable<String,LaunchConfigAttribute>();
    for( LaunchConfigAttribute attribute : attributes ) {
      _attributes.put( attribute.getName(), attribute );
    }
  }

  @Override
  public Collection<String> getAttributeNames() {
    return _attributes.keySet();
  }

  @Override
  public boolean getBooleanAttribute( String attributeName ) {
    LaunchConfigAttribute launchConfigAttribute = getLaunchConfigAttribute( attributeName );
    if( launchConfigAttribute == null ) {
      return false;
    }
    return Boolean.parseBoolean( launchConfigAttribute.getStringValue() );
  }

  @Override
  public String getAttribute( String attributeName ) {

    LaunchConfigAttribute launchConfigAttribute = getLaunchConfigAttribute( attributeName );
    if( launchConfigAttribute == null ) {
      return null;
    }

    return String.valueOf( launchConfigAttribute.getValue() );
  }

  @Override
  public List<String> getListAttribute( String attributeName ) {
    LaunchConfigAttribute launchConfigAttribute = getLaunchConfigAttribute( attributeName );
    if( launchConfigAttribute == null ) {
      return null;
    }

    if( !launchConfigAttribute.isListAttribute() ) {
      // TODO : Exception ?
      return null;
    }
    ListAttribute listAttributeValue = launchConfigAttribute.getListAttributeValue();
    return listAttributeValue.getEntries();
  }

  protected LaunchConfigAttribute getLaunchConfigAttribute( String attributeName ) {
    Assure.notNull( "attributeName", attributeName );
    return _attributes.get( attributeName );
  }

  @Override
  public String getType() {
    return _type;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String toString() {
    return String.format( "LaunchConfigurationImpl ( %s, _type = '%s', _attributes = '" + _attributes + "' )", super.toString(), _type, _attributes );
  }

} /* ENDCLASS */
