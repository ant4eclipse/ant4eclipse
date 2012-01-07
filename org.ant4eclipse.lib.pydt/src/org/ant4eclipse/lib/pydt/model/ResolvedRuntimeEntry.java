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
package org.ant4eclipse.lib.pydt.model;

import org.ant4eclipse.lib.core.Assure;
import org.ant4eclipse.lib.core.data.Version;

import java.io.File;
import java.util.List;

/**
 * Resolved record used to identify a python runtime.
 * 
 * @author Daniel Kasmeroglu (Daniel.Kasmeroglu@Kasisoft.net)
 */
public class ResolvedRuntimeEntry implements ResolvedPathEntry {

  private Version      _version;
  private List<File>   _libs;
  private String       _owningproject;

  /**
   * Initialises this entry used to describe a runtime.
   * 
   * @param owningproject
   *          The name of the related eclipse project. Neither <code>null</code> nor empty.
   * @param version
   *          The version of the runtime. Not <code>null</code>.
   * @param libs
   *          The bundled libraries representing the runtime. Not <code>null</code>.
   */
  public ResolvedRuntimeEntry( String owningproject, Version version, List<File> libs ) {
    Assure.notNull( "version", version );
    Assure.notNull( "libs", libs );
    Assure.nonEmpty( "owningproject", owningproject );
    _owningproject = owningproject;
    _version = version;
    _libs = libs;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getOwningProjectname() {
    return _owningproject;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public ReferenceKind getKind() {
    return ReferenceKind.Runtime;
  }

  /**
   * Returns the version of the runtime.
   * 
   * @return The version of the runtime. Not <code>null</code>.
   */
  public Version getVersion() {
    return _version;
  }

  /**
   * Returns the libraries for this runtime.
   * 
   * @return The libraries for this runtime. Not <code>null</code>.
   */
  public List<File> getLibraries() {
    return _libs;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean equals( Object object ) {
    if( this == object ) {
      return true;
    }
    if( object == null ) {
      return false;
    }
    if( object.getClass() != getClass() ) {
      return false;
    }
    ResolvedRuntimeEntry other = (ResolvedRuntimeEntry) object;
    if( !_owningproject.equals( other._owningproject ) ) {
      return false;
    }
    if( _libs.size() != other._libs.size() ) {
      return false;
    }
    if( !_version.equals( other._version ) ) {
      return false;
    }
    for( int i = 0; i < _libs.size(); i++ ) {
      if( !_libs.get(i).equals( other._libs.get(i) ) ) {
        return false;
      }
    }
    return true;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public int hashCode() {
    int result = _owningproject.hashCode();
    result = 31 * result + _version.hashCode();
    for( File lib : _libs ) {
      result = 31 * result + lib.hashCode();
    }
    return result;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String toString() {
    StringBuffer buffer = new StringBuffer();
    buffer.append( "[ResolvedRuntimeEntry:" );
    buffer.append( " _owningproject: " );
    buffer.append( _owningproject );
    buffer.append( ", _version: " );
    buffer.append( _version );
    buffer.append( ", _libs: {" );
    buffer.append( _libs.get(0) );
    for( int i = 1; i < _libs.size(); i++ ) {
      buffer.append( ", " );
      buffer.append( _libs.get(i) );
    }
    buffer.append( "}]" );
    return buffer.toString();
  }

} /* ENDCLASS */
