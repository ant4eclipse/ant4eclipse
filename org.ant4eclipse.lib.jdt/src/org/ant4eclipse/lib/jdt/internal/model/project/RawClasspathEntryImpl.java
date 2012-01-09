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
package org.ant4eclipse.lib.jdt.internal.model.project;

import java.io.File;

import org.ant4eclipse.lib.core.Assure;
import org.ant4eclipse.lib.jdt.model.project.RawClasspathEntry;

/**
 * Encapsulates an entry in a Java project classpath.
 * 
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 */
public class RawClasspathEntryImpl implements RawClasspathEntry {

  /** the path */
  private String  _path;

  /** the entry kind */
  private int     _entryKind;

  /** the output location */
  private String  _outputLocation;

  /** whether or not the entry is exported */
  private boolean _exported = true;

  /** - */
  private String  _includes;

  /** - */
  private String  _excludes;

  /**
   * Creates a new instance of type EclipseClasspathEntry
   * 
   * @param entryKind
   * @param path
   */
  public RawClasspathEntryImpl( String entryKind, String path ) {
    this( resolveEntryKind( entryKind, path ), path, null, false );
  }

  /**
   * Creates a new instance of type EclipseClasspathEntry
   * 
   * @param entryKind
   * @param path
   * @param output
   */
  public RawClasspathEntryImpl( String entryKind, String path, String output ) {
    this( resolveEntryKind( entryKind, path ), path, output, false );
  }

  /**
   * Creates a new instance of type EclipseClasspathEntry
   * 
   * @param entryKind
   * @param path
   * @param exported
   */
  public RawClasspathEntryImpl( String entryKind, String path, boolean exported ) {
    this( resolveEntryKind( entryKind, path ), path, null, exported );
  }

  /**
   * Creates a new instance of type EclipseClasspathEntry
   * 
   * @param entryKind
   * @param path
   * @param output
   * @param exported
   */
  public RawClasspathEntryImpl( String entryKind, String path, String output, boolean exported ) {
    this( resolveEntryKind( entryKind, path ), path, output, exported );
  }

  /**
   * Creates a new instance of type EclipseClasspathEntry
   * 
   * @param entryKind
   * @param path
   */
  public RawClasspathEntryImpl( int entryKind, String path ) {
    this( entryKind, path, null, false );
  }

  /**
   * Creates a new instance of type EclipseClasspathEntry
   * 
   * @param entryKind
   * @param path
   * @param output
   * @param exported
   */
  public RawClasspathEntryImpl( int entryKind, String path, String output, boolean exported ) {
    Assure.notNull( "path", path );

    _entryKind = entryKind;
    _path = path;
    _outputLocation = output;
    _exported = exported;

    if( (_entryKind == CPE_SOURCE) || (_entryKind == CPE_OUTPUT) ) {
      if( _path != null ) {
        _path = _path.replace( '/', File.separatorChar );
        _path = _path.replace( '\\', File.separatorChar );
      }
      if( _outputLocation != null ) {
        _outputLocation = _outputLocation.replace( '/', File.separatorChar );
        _outputLocation = _outputLocation.replace( '\\', File.separatorChar );
      }
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public int getEntryKind() {
    return _entryKind;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getPath() {
    return _path;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getOutputLocation() {
    return _outputLocation;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean hasOutputLocation() {
    return _outputLocation != null;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isExported() {
    return _exported;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getIncludes() {
    return _includes;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getExcludes() {
    return _excludes;
  }

  /**
   * <p>
   * </p>
   * 
   * @param includes
   */
  public void setIncludes( String includes ) {
    _includes = includes;
  }

  /**
   * <p>
   * </p>
   * 
   * @param excludes
   */
  public void setExcludes( String excludes ) {
    _excludes = excludes;
  }

  /**
   * <p>
   * </p>
   * 
   * @param path
   */
  public void setPath( String path ) {
    _path = path;
  }

  /**
   * Determines the type of classpath entry.
   * 
   * @param entryKind
   *          A textual representation of a classpath type.
   * @param path
   *          A path which content depends on the entry kind.
   * 
   * @return A numerical information specifying the entry kind.
   */
  private static int resolveEntryKind( String entryKind, String path ) {
    Assure.notNull( "entryKind", entryKind );
    Assure.notNull( "path", path );
    if( "con".equals( entryKind ) ) {
      return CPE_CONTAINER;
    } else if( "lib".equals( entryKind ) ) {
      return CPE_LIBRARY;
    } else if( "src".equals( entryKind ) && path.startsWith( "/" ) ) {
      return CPE_PROJECT;
    } else if( "src".equals( entryKind ) && !path.startsWith( "/" ) ) {
      return CPE_SOURCE;
    } else if( "var".equals( entryKind ) ) {
      return CPE_VARIABLE;
    } else if( "output".equals( entryKind ) ) {
      return CPE_OUTPUT;
    }
    return -1;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String toString() {
    return String.format( "[EclipseClasspathEntry: path: %s entryKind: %s outputLocation: %s exported: %s]", _path, _entryKind, _outputLocation, _exported );
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
    RawClasspathEntryImpl other = (RawClasspathEntryImpl) o;
    if( _entryKind != other._entryKind ) {
      return false;
    }
    if( _exported != other._exported ) {
      return false;
    }
    if( _path == null ) {
      if( other._path != null ) {
        return false;
      }
    } else {
      if( !_path.equals( other._path ) ) {
        return false;
      }
    }
    if( _outputLocation == null ) {
      return other._outputLocation == null;
    } else {
      return _outputLocation.equals( other._outputLocation );
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public int hashCode() {
    int hashCode = 1;
    hashCode = 31 * hashCode + (_path == null ? 0 : _path.hashCode());
    hashCode = 31 * hashCode + _entryKind;
    hashCode = 31 * hashCode + (_outputLocation == null ? 0 : _outputLocation.hashCode());
    hashCode = 31 * hashCode + (_exported ? 1231 : 1237);
    return hashCode;
  }
  
} /* ENDCLASS */
