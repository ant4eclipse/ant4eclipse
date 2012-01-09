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

import java.io.File;

/**
 * Datastructure used to simply describe the known Python interpreters.
 * 
 * @author Daniel Kasmeroglu (Daniel.Kasmeroglu@Kasisoft.net)
 */
public class PythonInterpreter implements Comparable<PythonInterpreter> {

  private static final String[] EXESUFFICES = new String[] { "", ".exe", ".bat", ".sh" };

  private String                _name;

  private String[]              _executables;

  /**
   * Sets up this datastructure used for python interpreters.
   * 
   * @param name
   *          The name of the python interpreter. Neither <code>null</code> nor empty.
   * @param executables
   *          The supported executable names. Not <code>null</code>. Must be sorted lexicographically and each element
   *          is supposed to be neither <code>null</code> nor empty.
   */
  // Assure.notNull( "name", name );
  // Assure.notNull( "executables", executables );
  public PythonInterpreter( String name, String[] executables ) {
    _name = name;
    _executables = executables;
  }

  /**
   * Returns the name associated with this interpreter.
   * 
   * @return The name associated with this interpreter.
   */
  public String getName() {
    return _name;
  }

  /**
   * Looks for the python interpreter executable within the supplied installation directory.
   * 
   * @param directory
   *          The python installation directory. Not <code>null</code>.
   * 
   * @return The location of the python executable or <code>null</code>.
   */
  // Assure.notNull( "directory", directory );
  public File lookup( File directory ) {
    for( String exename : _executables ) {
      for( String suffix : EXESUFFICES ) {
        File candidate = new File( directory, exename + suffix );
        if( candidate.isFile() ) {
          // found a match
          return candidate;
        }
      }
    }
    return null;
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
    PythonInterpreter other = (PythonInterpreter) object;
    if( !_name.equals( other._name ) ) {
      return false;
    }
    if( _executables.length != other._executables.length ) {
      return false;
    }
    for( int i = 0; i < _executables.length; i++ ) {
      if( !_executables[i].equals( other._executables ) ) {
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
    int result = _name.hashCode();
    for( String executable : _executables ) {
      result = result * 31 + executable.hashCode();
    }
    return result;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String toString() {
    StringBuffer buffer = new StringBuffer();
    buffer.append( "[PythonInterpreter:" );
    buffer.append( " _name: " );
    buffer.append( _name );
    buffer.append( ", _executables: {" );
    if( _executables.length > 0 ) {
      buffer.append( _executables[0] );
      for( int i = 1; i < _executables.length; i++ ) {
        buffer.append( ", " );
        buffer.append( _executables[i] );
      }
    }
    buffer.append( "}]" );
    return buffer.toString();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public int compareTo( PythonInterpreter other ) {
    return _name.compareTo( other._name );
  }

} /* ENDCLASS */
