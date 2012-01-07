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
package org.ant4eclipse.lib.pydt.internal.model.project;

import org.ant4eclipse.lib.platform.model.resource.EclipseProject;
import org.ant4eclipse.lib.platform.model.resource.role.AbstractProjectRole;
import org.ant4eclipse.lib.pydt.model.RawPathEntry;
import org.ant4eclipse.lib.pydt.model.ReferenceKind;
import org.ant4eclipse.lib.pydt.model.project.DLTKProjectRole;
import org.ant4eclipse.lib.pydt.model.project.PyDevProjectRole;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * Implements the python project role.
 * </p>
 * 
 * @author Daniel Kasmeroglu (Daniel.Kasmeroglu@Kasisoft.net)
 */
public class PythonProjectRoleImpl extends AbstractProjectRole implements DLTKProjectRole, PyDevProjectRole {

  public static final String NAME = "PythonProjectRole";

  private List<RawPathEntry>  rawpathentries;
  private boolean             isdltk;

  /**
   * <p>
   * Creates a new instance of type PythonProjectRole.
   * </p>
   * 
   * @param eclipseProject
   *          The eclipse project. Not <code>null</code>.
   */
  public PythonProjectRoleImpl( EclipseProject eclipseProject, boolean dltk ) {
    super( NAME, eclipseProject );
    rawpathentries  = new ArrayList<RawPathEntry>();
    isdltk          = dltk;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isDLTK() {
    return isdltk;
  }

  /**
   * Adds the supplied raw (unresolved) path entry to this role implementation.
   * 
   * @param rawPathEntry
   *          The raw path information associated with the current eclipse project.
   */
  public void addRawPathEntry( RawPathEntry rawpathentry ) {
    rawpathentries.add( rawpathentry );
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public List<RawPathEntry> getRawPathEntries() {
    return rawpathentries;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public List<RawPathEntry> getRawPathEntries( ReferenceKind kind ) {
    List<RawPathEntry> result = new ArrayList<RawPathEntry>();
    for( RawPathEntry entry : rawpathentries ) {
      if( kind == entry.getKind() ) {
        result.add( entry );
      }
    }
    return result;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String toString() {
    StringBuffer buffer = new StringBuffer();
    buffer.append( "[PythonProjectRole:" );
    buffer.append( " NAME: " );
    buffer.append( NAME );
    buffer.append( ", _isdltk: " );
    buffer.append( isdltk );
    buffer.append( ", _rawpathentries; {" );
    if( !rawpathentries.isEmpty() ) {
      buffer.append( rawpathentries.get( 0 ) );
      for( int i = 1; i < rawpathentries.size(); i++ ) {
        buffer.append( "," );
        buffer.append( rawpathentries.get( i ) );
      }
    }
    buffer.append( "}" );
    buffer.append( "]" );
    return buffer.toString();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public int hashCode() {
    int result = super.hashCode();
    for( int i = 0; i < rawpathentries.size(); i++ ) {
      result = result * 31 + rawpathentries.get( i ).hashCode();
    }
    result = result * 31 + (isdltk ? 1 : 0);
    return result;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean equals( Object object ) {
    if( this == object ) {
      return true;
    }
    if( !super.equals( object ) ) {
      return false;
    }
    if( object == null ) {
      return false;
    }
    if( object.getClass() != getClass() ) {
      return false;
    }
    PythonProjectRoleImpl other = (PythonProjectRoleImpl) object;
    if( isdltk != other.isdltk ) {
      return false;
    }
    if( rawpathentries.size() != other.rawpathentries.size() ) {
      return false;
    }
    for( int i = 0; i < rawpathentries.size(); i++ ) {
      if( !rawpathentries.get( i ).equals( other.rawpathentries.get( i ) ) ) {
        return false;
      }
    }
    return true;
  }

} /* ENDCLASS */
