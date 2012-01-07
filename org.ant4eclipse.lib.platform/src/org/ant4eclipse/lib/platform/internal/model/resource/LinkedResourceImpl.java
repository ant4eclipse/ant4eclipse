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
package org.ant4eclipse.lib.platform.internal.model.resource;

/**
 * <p>
 * Represents a "linked resource" of an eclipse project.
 * </p>
 * 
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 */
public class LinkedResourceImpl {

  /** the name of the linked resource */
  private String _name;

  /** the location of the linked resource */
  private String _location;

  /** the location relative to the project */
  private String _relativelocation;

  /** the type of the linked resource */
  private int    _type;

  /**
   * Creates a new instance of type LinkedResource.
   * 
   * @param name
   * @param location
   * @param relative
   * @param type
   */
  public LinkedResourceImpl( String name, String location, String relative, int type ) {
    _name = name;
    _location = location;
    _type = type;
    _relativelocation = relative;
  }

  /**
   * The location of the linked resource.
   * 
   * @return The location of the linked resource.
   */
  public String getLocation() {
    return _location;
  }

  /**
   * The name of the linked resource.
   * 
   * @return The name of the linked resource.
   */
  public String getName() {
    return _name;
  }

  /**
   * The type of the linked resource.
   * 
   * @return The type of the linked resource.
   */
  public int getType() {
    return _type;
  }

  /**
   * Returns the location relative to the project.
   * 
   * @return The location relative to the project. Maybe null in case no relative location can be calculated.
   */
  public String getRelativeLocation() {
    return _relativelocation;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String toString() {
    StringBuffer buffer = new StringBuffer();
    buffer.append( "[LinkedResource:" );
    buffer.append( " name: " );
    buffer.append( _name );
    buffer.append( " location: " );
    buffer.append( _location );
    buffer.append( " relativelocation: " );
    buffer.append( _relativelocation );
    buffer.append( " type: " );
    buffer.append( _type );
    buffer.append( "]" );
    return buffer.toString();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean equals( Object o ) {
    if( this == o ) {
      return true;
    }
    if( o == null ) {
      return false;
    }
    if( o.getClass() != getClass() ) {
      return false;
    }
    LinkedResourceImpl castedObj = (LinkedResourceImpl) o;
    return((_name == null ? castedObj._name == null : _name.equals( castedObj._name ))
        && (_location == null ? castedObj._location == null : _location.equals( castedObj._location ))
        && (_type == castedObj._type) && (_relativelocation == null ? castedObj._relativelocation == null
        : _relativelocation.equals( castedObj._relativelocation )));
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public int hashCode() {
    int hashCode = 1;
    hashCode = 31 * hashCode + (_name == null ? 0 : _name.hashCode());
    hashCode = 31 * hashCode + (_location == null ? 0 : _location.hashCode());
    hashCode = 31 * hashCode + (_relativelocation == null ? 0 : _relativelocation.hashCode());
    hashCode = 31 * hashCode + _type;
    return hashCode;
  }

} /* ENDCLASS */
