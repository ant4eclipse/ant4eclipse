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

import org.ant4eclipse.lib.core.Assure;

import java.util.ArrayList;
import java.util.List;

class LaunchConfigAttribute {

  private String _name;

  private Object _value;

  public LaunchConfigAttribute( String name ) {
    Assure.notNull( "name", name );
    _name = name;
  }

  public String getName() {
    return _name;
  }

  public Object getValue() {
    return _value;
  }

  public boolean isStringBasedAttribute() {
    return _value instanceof String;
  }

  public boolean isListAttribute() {
    return _value instanceof ListAttribute;
  }

  public ListAttribute getListAttributeValue() {
    Assure.assertTrue( isListAttribute(), "LauchConfigAttribute '" + _name + "' must be a list-based attribute" );

    return (ListAttribute) _value;
  }

  public String getStringValue() {
    Assure.assertTrue( isStringBasedAttribute(), "LauchConfigAttribute '" + _name
        + "' must be a string-based attribute" );

    return (String) _value;
  }

  public void setValue( Object value ) {
    _value = value;
  }

  static class ListAttribute {
    private List<String> _entries = new ArrayList<String>();

    public List<String> getEntries() {
      return _entries;
    }

    void addEntry( String entry ) {
      _entries.add( entry );
    }

    @Override
    public String toString() {
      StringBuilder builder = new StringBuilder();
      for( int i = 0; i < _entries.size(); i++ ) {
        builder.append( _entries.get( i ) );
        if( i < _entries.size() - 1 ) {
          builder.append( "," );
        }
      }
      return builder.toString();
    }
  }

} /* ENDCLASS */
