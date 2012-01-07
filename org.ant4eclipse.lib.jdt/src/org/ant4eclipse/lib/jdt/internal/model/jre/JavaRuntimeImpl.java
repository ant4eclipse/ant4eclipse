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

import org.ant4eclipse.lib.core.Assure;
import org.ant4eclipse.lib.core.data.Version;
import org.ant4eclipse.lib.jdt.model.jre.JavaProfile;
import org.ant4eclipse.lib.jdt.model.jre.JavaRuntime;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * Defines a java runtime environment.
 * </p>
 * 
 * @author Daniel Kasmeroglu (daniel.kasmeroglu@kasisoft.net)
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 */
public class JavaRuntimeImpl implements JavaRuntime {

  /** the id */
  private String      _id                       = null;

  /** the location */
  private File        _location                 = null;

  /** the version */
  private Version     _javaVersion              = null;

  /** the version */
  private Version     _javaSpecificationVersion = null;

  /** - */
  private JavaProfile _javaProfile;

  /** the libraries */
  private List<File>  _libraries                = new ArrayList<File>();

  /**
   * <p>
   * Creates a new instance of type {@link JavaRuntimeImpl}.
   * </p>
   * 
   * @param id
   * @param location
   * @param libraries
   * @param javaVersion
   * @param javaSpecificationVersion
   * @param javaProfile
   */
  JavaRuntimeImpl( String id, File location, List<File> libraries, Version javaVersion, Version javaSpecificationVersion, JavaProfile javaProfile ) {

    Assure.nonEmpty( "id", id );
    Assure.isDirectory( "location", location );
    Assure.notNull( "libraries", libraries );
    Assure.notNull( "javaVersion", javaVersion );
    Assure.notNull( "javaSpecificationVersion", javaSpecificationVersion );
    Assure.notNull( "javaProfile", javaProfile );

    _id = id;
    _location = location;
    _libraries = libraries;
    _javaVersion = javaVersion;
    _javaSpecificationVersion = javaSpecificationVersion;
    _javaProfile = javaProfile;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getId() {
    return _id;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public File getLocation() {
    return _location;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public List<File> getLibraries() {
    return _libraries;
  }

  /**
   * @see org.ant4eclipse.lib.jdt.model.jre.JavaRuntime#isJavaVersion(net.sf.ant4eclipse.model.jdt.jre.JavaRuntimeImpl.Version
   *      )
   */
  public boolean isJavaVersion( Version version ) {
    return (version.getMajor() == _javaVersion.getMajor()) && (version.getMinor() == _javaVersion.getMinor());
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Version getJavaVersion() {
    return _javaVersion;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Version getSpecificationVersion() {
    return _javaSpecificationVersion;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public JavaProfile getJavaProfile() {
    return _javaProfile;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String toString() {
    StringBuffer buffer = new StringBuffer();
    buffer.append( "[JavaRuntime:" );
    buffer.append( " id: " );
    buffer.append( _id );
    buffer.append( " javaVersion: " );
    buffer.append( _javaVersion );
    buffer.append( " javaSpecificationVersion: " );
    buffer.append( _javaSpecificationVersion );
    buffer.append( " javaProfile: " );
    buffer.append( _javaProfile );
    buffer.append( " location: " );
    buffer.append( _location );
    buffer.append( " { " );
    for( int i0 = 0; (_libraries != null) && (i0 < _libraries.size()); i0++ ) {
      buffer.append( " libraries[" + i0 + "]: " );
      buffer.append( _libraries.get(i0) );
    }
    buffer.append( " } " );
    buffer.append( "]" );
    return buffer.toString();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public int hashCode() {
    int prime = 31;
    int result = 1;
    result = prime * result + ((_id == null) ? 0 : _id.hashCode());
    result = prime * result + ((_location == null) ? 0 : _location.hashCode());
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
    JavaRuntimeImpl other = (JavaRuntimeImpl) obj;
    if( _id == null ) {
      if( other._id != null ) {
        return false;
      }
    } else if( !_id.equals( other._id ) ) {
      return false;
    }
    if( _location == null ) {
      if( other._location != null ) {
        return false;
      }
    } else if( !_location.equals( other._location ) ) {
      return false;
    }
    return true;
  }

} /* ENDCLASS */
