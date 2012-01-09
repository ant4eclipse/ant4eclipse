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
package org.ant4eclipse.lib.jdt.tools;

import java.io.File;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

/**
 * <p>
 * Represents a resolved class path entry. A resolved class path entry contains an array of files that belong to this
 * entry. It also contains an (optional) {@link AccessRestrictions} object that specifies the visibility rules for this
 * entry.
 * </p>
 * 
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 */
public class ResolvedClasspathEntry {

  /** the file array */
  private List<File>          _classPathEntries;

  /** the file array */
  private List<File>          _sourcePathEntries;

  /** the visibility rules */
  private AccessRestrictions  _accessRestrictions;

  /**
   * <p>
   * Creates a new instance of type {@link ResolvedClasspathEntry}.
   * </p>
   * 
   * @param classPathEntries
   *          the class path entries
   * @param accessRestrictions
   *          the access restrictions
   * @param sourcePathEntries
   *          the source path entries
   */
  public ResolvedClasspathEntry( List<File> classPathEntries, List<File> sourcePathEntries ) {
    this( classPathEntries, null, sourcePathEntries );
  }

  /**
   * <p>
   * Creates a new instance of type {@link ResolvedClasspathEntry}.
   * </p>
   * 
   * @param classPathEntries
   *          the class path entries
   * @param accessRestrictions
   *          the access restrictions
   * @param sourcePathEntries
   *          the source path entries
   */
  // Assure.notNull( "classPathEntries", classPathEntries );
  public ResolvedClasspathEntry( List<File> classPathEntries, AccessRestrictions accessRestrictions, List<File> sourcePathEntries ) {
    _classPathEntries = classPathEntries;
    _accessRestrictions = accessRestrictions;
    _sourcePathEntries = sourcePathEntries;
  }

  /**
   * <p>
   * Creates a new instance of type {@link ResolvedClasspathEntry}.
   * </p>
   * 
   * @param entries
   * @param accessRestrictions
   */
  public ResolvedClasspathEntry( List<File> entries, AccessRestrictions accessRestrictions ) {
    this( entries, accessRestrictions, null );
  }

  /**
   * <p>
   * Creates a new instance of type {@link ResolvedClasspathEntry}.
   * </p>
   * 
   * @param entry
   * @param accessRestrictions
   */
  public ResolvedClasspathEntry( File entry, AccessRestrictions accessRestrictions ) {
    this( Arrays.asList( entry ), accessRestrictions );
  }

  /**
   * <p>
   * Creates a new instance of type {@link ResolvedClasspathEntry}.
   * </p>
   * 
   * @param classPathEntries
   *          the file entries
   */
  public ResolvedClasspathEntry( List<File> classPathEntries ) {
    this( classPathEntries, (AccessRestrictions) null );
  }

  /**
   * <p>
   * Creates a new instance of type {@link ResolvedClasspathEntry}.
   * </p>
   * 
   * @param classPathEntry
   *          the class path entry
   */
  public ResolvedClasspathEntry( File classPathEntry ) {
    this( Arrays.asList( classPathEntry ), (AccessRestrictions) null );
  }

  /**
   * <p>
   * Returns all file entries for this class path entry.
   * </p>
   * 
   * @return all file entries for this class path entry.
   */
  public List<File> getClassPathEntries() {
    return _classPathEntries;
  }

  /**
   * <p>
   * Returns all the source path entries for this class path entry. If no source path entries exist, <code>null</code>
   * will be returned instead.
   * </p>
   * 
   * @return all the source path entries for this class path entry. If no source path entries exist, <code>null</code>
   *         will be returned instead.
   */
  public List<File> getSourcePathEntries() {
    return _sourcePathEntries;
  }

  /**
   * <p>
   * Returns <code>true</code> if this entry has source path entries attached.
   * </p>
   * 
   * @return <code>true</code> if this entry has source path entries attached.
   */
  public boolean hasSourcePathEntries() {
    return (_sourcePathEntries != null) && (_sourcePathEntries.size() > 0);
  }

  /**
   * <p>
   * Returns the {@link AccessRestrictions}.
   * </p>
   * 
   * @return the {@link AccessRestrictions}.
   */
  public AccessRestrictions getAccessRestrictions() {
    return _accessRestrictions;
  }

  /**
   * <p>
   * Returns <code>true</code>, if the class path entry contains {@link AccessRestrictions}.
   * </p>
   * 
   * @return <code>true</code>, if the class path entry contains {@link AccessRestrictions}.
   */
  public boolean hasAccessRestrictions() {
    return _accessRestrictions != null;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public int hashCode() {
    int prime = 31;
    int result = 1;
    result = prime * result + ((_accessRestrictions == null) ? 0 : _accessRestrictions.hashCode());
    result = prime * result + hashCode( _classPathEntries );
    result = prime * result + hashCode( _sourcePathEntries );
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
    ResolvedClasspathEntry other = (ResolvedClasspathEntry) obj;
    if( _accessRestrictions == null ) {
      if( other._accessRestrictions != null ) {
        return false;
      }
    } else if( !_accessRestrictions.equals( other._accessRestrictions ) ) {
      return false;
    }
    if( !Arrays.equals( _classPathEntries.toArray(), other._classPathEntries.toArray() ) ) {
      return false;
    }
    if( !Arrays.equals( _sourcePathEntries.toArray(), other._sourcePathEntries.toArray() ) ) {
      return false;
    }
    return true;
  }

  /**
   * <p>
   * Generated helper method that returns the hash code of an array of objects.
   * </p>
   * 
   * @param array
   *          the array.
   * @return the hash code of an array of objects.
   */
  private static int hashCode( List<File> array ) {
    int prime = 31;
    if( array == null ) {
      return 0;
    }
    int result = 1;
    for( Object element : array ) {
      result = prime * result + (element == null ? 0 : element.hashCode());
    }
    return result;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String toString() {
    StringBuffer buffer = new StringBuffer();
    buffer.append( "[ResolvedClasspathEntry:" );
    buffer.append( " { " );
    for( int i0 = 0; (_classPathEntries != null) && (i0 < _classPathEntries.size()); i0++ ) {
      buffer.append( " _classPathEntries[" + i0 + "]: " );
      buffer.append( _classPathEntries.get(i0) );
    }
    buffer.append( " } " );
    buffer.append( " { " );
    for( int i0 = 0; (_sourcePathEntries != null) && (i0 < _sourcePathEntries.size()); i0++ ) {
      buffer.append( " _sourcePathEntries[" + i0 + "]: " );
      buffer.append( _sourcePathEntries.get(i0) );
    }
    buffer.append( " } " );
    buffer.append( " _accessRestrictions: " );
    buffer.append( _accessRestrictions );
    buffer.append( "]" );
    return buffer.toString();
  }

  /**
   * <p>
   * Encapsulates the visibility rules of a resolved class path entry.
   * </p>
   * 
   * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
   */
  public static class AccessRestrictions {

    /** indicates if everything should be excluded by default */
    private boolean     _excludeAll = true;

    /** the set of public packages */
    private Set<String> _publicPackages;

    /** the set of private packages */
    private Set<String> _privatePackages;

    /**
     * <p>
     * Creates a new instance of type AccessRestrictions.
     * </p>
     * 
     */
    public AccessRestrictions() {
      _publicPackages = new LinkedHashSet<String>();
      _privatePackages = new LinkedHashSet<String>();
    }

    /**
     * <p>
     * Creates a new instance of type AccessRestrictions.
     * </p>
     * 
     * @param publicPackages
     *          the set of public packages
     * @param privatePackages
     *          the set of private packages
     * @param excludeAll
     *          indicates if everything should be excluded by default
     */
    public AccessRestrictions( Set<String> publicPackages, Set<String> privatePackages, boolean excludeAll ) {

      // TODO
      // AE-67: Support for types (exclusion/inclusion of classes/interfaces)

      _publicPackages = publicPackages;
      _privatePackages = privatePackages;
      _excludeAll = excludeAll;
    }

    /**
     * <p>
     * Returns <code>true</code> if every packages is excluded by default.
     * </p>
     * 
     * @return <code>true</code> if every packages is excluded by default.
     */
    public boolean isExcludeAll() {
      return _excludeAll;
    }

    /**
     * <p>
     * Adds a public package.
     * </p>
     * 
     * @param name
     *          the name of the public package.
     */
    public void addPublicPackage( String name ) {
      _publicPackages.add( name );
    }

    /**
     * <p>
     * Adds a private package.
     * </p>
     * 
     * @param name
     *          the name of the private package.
     */
    public void addPrivatePackage( String name ) {
      _privatePackages.add( name );
    }

    /**
     * <p>
     * Returns all public packages.
     * </p>
     * 
     * @return all public packages.
     */
    public Set<String> getPublicPackages() {
      return _publicPackages;
    }

    /**
     * <p>
     * Returns all private packages.
     * </p>
     * 
     * @return all private packages.
     */
    public Set<String> getPrivatePackages() {
      return _privatePackages;
    }

    /**
     * <p>
     * Returns the access restrictions as formatted string.
     * </p>
     * 
     * @return the access restrictions as formatted string.
     */
    public String asFormattedString() {
      StringBuffer result = new StringBuffer();

      // format public packages
      for( String publicPackage : _publicPackages ) {
        result.append( "+" );
        result.append( publicPackage.replace( '.', '/' ) );
        result.append( "/*;" );
      }

      // format private packages
      for( String privatePackage : _privatePackages ) {
        result.append( "-" );
        result.append( privatePackage.replace( '.', '/' ) );
        result.append( "/*;" );
      }

      // exclude/include all
      if( _excludeAll ) {
        result.append( "-**/*" );
      } else {
        result.append( "+**/*" );
      }

      // return result
      return result.toString();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
      return String.format( "[AccessRestrictions: _excludeAll: %s _publicPackages: %s _privatePackages: %s]", _excludeAll, _publicPackages, _privatePackages );
    }

  } /* ENDCLASS */

} /* ENDCLASS */
