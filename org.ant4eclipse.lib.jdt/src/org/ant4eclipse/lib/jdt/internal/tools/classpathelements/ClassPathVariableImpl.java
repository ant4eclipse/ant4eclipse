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

import org.ant4eclipse.lib.core.Assure;
import org.ant4eclipse.lib.core.util.Utilities;
import org.ant4eclipse.lib.jdt.tools.classpathelements.ClassPathVariable;

import java.io.File;

/**
 * <p>
 * Implementation of {@link ClassPathVariable}.
 * </p>
 * 
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 */
public class ClassPathVariableImpl implements ClassPathVariable {

  /** the name of the class path variable */
  private String _name;

  /** the path of this class path variable */
  private File   _path;

  /**
   * <p>
   * Creates a new instance of type ClasspathVariableImpl.
   * </p>
   * 
   * @param name
   * @param path
   */
  public ClassPathVariableImpl( String name, File path ) {
    Assure.nonEmpty( "name", name );
    Assure.notNull( "path", path );
    _name = name;
    _path = Utilities.getCanonicalFile( path );
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
  public File getPath() {
    return _path;
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
    ClassPathVariableImpl castedObj = (ClassPathVariableImpl) o;
    if( _name == null ) {
      if( castedObj._name != null ) {
        return false;
      }
    } else {
      if( !_name.equals( castedObj._name ) ) {
        return false;
      }
    }
    if( _path == null ) {
      return castedObj._path == null;
    } else {
      return _path.equals( castedObj._path );
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public int hashCode() {
    int hashCode = 1;
    hashCode = 31 * hashCode + (_name == null ? 0 : _name.hashCode());
    hashCode = 31 * hashCode + (_path == null ? 0 : _path.hashCode());
    return hashCode;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String toString() {
    return String.format("[ClasspathVariableImpl: _name: %s _path: %s]", _name, _path);
  }

} /* ENDCLASS */
