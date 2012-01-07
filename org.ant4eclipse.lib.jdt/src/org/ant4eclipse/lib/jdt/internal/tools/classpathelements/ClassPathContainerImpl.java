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
package org.ant4eclipse.lib.jdt.internal.tools.classpathelements;

import org.ant4eclipse.lib.jdt.tools.classpathelements.ClassPathContainer;

import java.io.File;
import java.util.Arrays;
import java.util.List;

/**
 * <p>
 * Implementation of {@link ClassPathContainer}.
 * </p>
 * 
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 */
public class ClassPathContainerImpl implements ClassPathContainer {

  private String      _name;
  private List<File>  _pathEntries;

  /**
   * <p>
   * Creates a new instance of type {@link ClassPathContainerImpl}.
   * </p>
   * 
   * @param name
   *          the name of the class path container
   * @param pathEntries
   *          the path entries
   */
  public ClassPathContainerImpl( String name, List<File> pathEntries ) {
    _name         = name;
    _pathEntries  = pathEntries;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getName() {
    return _name;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public List<File> getPathEntries() {
    return _pathEntries;
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
    ClassPathContainerImpl other = (ClassPathContainerImpl) o;
    if( _name == null ) {
      if( other._name != null ) {
        return false;
      }
    } else {
      if( !_name.equals( other._name ) ) {
        return false;
      }
    }
    return Arrays.equals( _pathEntries.toArray(), other._pathEntries.toArray() );
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public int hashCode() {
    int hashCode = 1;
    hashCode = 31 * hashCode + (_name == null ? 0 : _name.hashCode());
    for( int i0 = 0; _pathEntries != null && i0 < _pathEntries.size(); i0++ ) {
      hashCode = 31 * hashCode + (_pathEntries == null ? 0 : _pathEntries.get(i0).hashCode());
    }
    return hashCode;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String toString() {
    StringBuffer buffer = new StringBuffer();
    buffer.append( "[ClassPathContainerImpl:" );
    buffer.append( " _name: " );
    buffer.append( _name );
    buffer.append( " { " );
    for( int i0 = 0; _pathEntries != null && i0 < _pathEntries.size(); i0++ ) {
      buffer.append( " _pathEntries[" + i0 + "]: " );
      buffer.append( _pathEntries.get(i0) );
    }
    buffer.append( " } " );
    buffer.append( "]" );
    return buffer.toString();
  }
  
} /* ENDCLASS */
