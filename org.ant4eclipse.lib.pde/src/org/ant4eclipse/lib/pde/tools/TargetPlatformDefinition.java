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
package org.ant4eclipse.lib.pde.tools;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * Represents a definition of a target platform. A target platform contains one or more locations that can contain
 * features and/or plug-ins.
 * </p>
 * 
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 */
public class TargetPlatformDefinition {

  private List<File> _locations;

  /**
   * <p>
   * Creates a new instance of type {@link TargetPlatformDefinition}.
   * </p>
   */
  public TargetPlatformDefinition() {
    _locations = new ArrayList<File>();
  }

  /**
   * <p>
   * Adds a location to the target platform definition.
   * </p>
   * 
   * @param location
   *          the location to add
   */
  // Assure.isDirectory( "location", location );
  public void addLocation( File location ) {
    if( !_locations.contains( location ) ) {
      _locations.add( location );
    }
  }

  /**
   * <p>
   * Returns all the locations defined in this target platform location.
   * </p>
   * 
   * @return all the locations defined in this target platform location.
   */
  public final File[] getLocations() {
    return _locations.toArray( new File[_locations.size()] );
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String toString() {
    StringBuffer buffer = new StringBuffer();
    buffer.append( "[" );
    buffer.append( getClass().getSimpleName() );
    buffer.append( ": _locations={" );
    if( !_locations.isEmpty() ) {
      buffer.append( _locations.get( 0 ).getAbsolutePath() );
      for( int i = 1; i < _locations.size(); i++ ) {
        buffer.append( "," );
        buffer.append( _locations.get( i ).getAbsolutePath() );
      }
    }
    buffer.append( "}]" );
    return buffer.toString();
  }

} /* ENDCLASS */
