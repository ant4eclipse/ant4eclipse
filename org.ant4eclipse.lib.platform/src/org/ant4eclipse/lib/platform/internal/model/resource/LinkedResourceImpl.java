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
 * Represents a "linked resource" of an eclipse project
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
  public LinkedResourceImpl(String name, String location, String relative, int type) {
    this._name = name;
    this._location = location;
    this._type = type;
    this._relativelocation = relative;
  }

  /**
   * The location of the linked resource.
   * 
   * @return The location of the linked resource.
   */
  public String getLocation() {
    return this._location;
  }

  /**
   * The name of the linked resource.
   * 
   * @return The name of the linked resource.
   */
  public String getName() {
    return this._name;
  }

  /**
   * The type of the linked resource.
   * 
   * @return The type of the linked resource.
   */
  public int getType() {
    return this._type;
  }

  /**
   * Returns the location relative to the project.
   * 
   * @return The location relative to the project. Maybe null in case no relative location can be calculated.
   */
  public String getRelativeLocation() {
    return this._relativelocation;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String toString() {
    StringBuffer buffer = new StringBuffer();
    buffer.append("[LinkedResource:");
    buffer.append(" name: ");
    buffer.append(this._name);
    buffer.append(" location: ");
    buffer.append(this._location);
    buffer.append(" relativelocation: ");
    buffer.append(this._relativelocation);
    buffer.append(" type: ");
    buffer.append(this._type);
    buffer.append("]");
    return buffer.toString();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null) {
      return false;
    }
    if (o.getClass() != getClass()) {
      return false;
    }
    LinkedResourceImpl castedObj = (LinkedResourceImpl) o;
    return ((this._name == null ? castedObj._name == null : this._name.equals(castedObj._name))
        && (this._location == null ? castedObj._location == null : this._location.equals(castedObj._location))
        && (this._type == castedObj._type) && (this._relativelocation == null ? castedObj._relativelocation == null
        : this._relativelocation.equals(castedObj._relativelocation)));
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public int hashCode() {
    int hashCode = 1;
    hashCode = 31 * hashCode + (this._name == null ? 0 : this._name.hashCode());
    hashCode = 31 * hashCode + (this._location == null ? 0 : this._location.hashCode());
    hashCode = 31 * hashCode + (this._relativelocation == null ? 0 : this._relativelocation.hashCode());
    hashCode = 31 * hashCode + this._type;
    return hashCode;
  }

} /* ENDCLASS */
