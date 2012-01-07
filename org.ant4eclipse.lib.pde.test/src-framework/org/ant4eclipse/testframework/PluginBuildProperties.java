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
package org.ant4eclipse.testframework;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class PluginBuildProperties {

  private List<Library> _libraries = new ArrayList<Library>();

  public Library withLibrary( String name ) {
    Library library = new Library( name );
    _libraries.add( library );
    return library;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String toString() {
    StringBuffer result = new StringBuffer();

    for( Library library : _libraries ) {
      result.append( library );
    }
    // bin.includes = META-INF/,\
    // .,\

    result.append( "bin.includes = " );
    result.append( "META-INF/" );
    if( !_libraries.isEmpty() ) {
      result.append( ",\\\n" );
    }

    for( Iterator<Library> iterator = _libraries.iterator(); iterator.hasNext(); ) {
      Library library = iterator.next();
      result.append( library.getName() );
      if( iterator.hasNext() ) {
        result.append( ",\\\n" );
      }
    }

    return result.toString();
  }

  public class Library {

    /** - */
    private String       _name;

    private List<String> _sourceList;

    private List<String> _outputList;

    public Library( String name ) {
      _name = name;
      _sourceList = new ArrayList<String>();
      _outputList = new ArrayList<String>();
    }

    /**
     * <p>
     * </p>
     * 
     * @return the name
     */
    public String getName() {
      return _name;
    }

    /**
     * <p>
     * </p>
     * 
     * @param string
     * @return
     */
    public Library withSource( String source ) {
      _sourceList.add( source );
      return this;
    }

    public Library withOutput( String source ) {
      _outputList.add( source );
      return this;
    }

    public PluginBuildProperties finishLibrary() {
      return PluginBuildProperties.this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {

      // source.. = src/,\
      // resource/
      // output.. = bin/

      StringBuffer result = new StringBuffer();

      if( !_sourceList.isEmpty() ) {
        result.append( "source." );
        result.append( _name );
        result.append( " = " );
        for( Iterator<String> iterator = _sourceList.iterator(); iterator.hasNext(); ) {
          String source = iterator.next();
          result.append( source );
          result.append( "/" );
          if( iterator.hasNext() ) {
            result.append( ",\\" );
          }
          result.append( "\n" );
        }
      }

      if( !_outputList.isEmpty() ) {
        result.append( "output." );
        result.append( _name );
        result.append( " = " );
        for( Iterator<String> iterator = _outputList.iterator(); iterator.hasNext(); ) {
          String source = iterator.next();
          result.append( source );
          result.append( "/" );
          if( iterator.hasNext() ) {
            result.append( ",\\" );
          }
          result.append( "\n" );
        }
      }
      return result.toString();
    }
  }
  
} /* ENDCLASS */
