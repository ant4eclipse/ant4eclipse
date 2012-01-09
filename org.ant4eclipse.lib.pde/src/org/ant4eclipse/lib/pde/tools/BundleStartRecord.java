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

/**
 * <p>
 * A BundleStartRecord just provides the information needed for the setup of the config.ini file.
 * </p>
 * 
 * @author Daniel Kasmeroglu (Daniel.Kasmeroglu@Kasisoft.net)
 */
public class BundleStartRecord implements Comparable<BundleStartRecord> {

  private boolean _autostart;

  private String  _id;

  private int     _startlevel;

  /**
   * Initialises this data structure.
   */
  public BundleStartRecord() {
    _autostart = false;
    _id = null;
    _startlevel = -1;
  }

  /**
   * Initialises this data structure from a compound description used for the bundle start.
   * 
   * @param description
   *          A compound description for the start information which has the following structure:
   * 
   *          <bundleid> [ '@' <startlevel>] [ ':' 'start' ]
   */
  public BundleStartRecord( String description ) {
    this();
    _autostart = description.endsWith( ":start" );
    if( _autostart ) {
      description = description.substring( 0, description.length() - 6 ); // 6 == ":start".length()
    }
    int idx = description.lastIndexOf( '@' );
    // get the startlevel
    if( idx != -1 ) {
      _startlevel = Integer.parseInt( description.substring( idx + 1 ) );
      description = description.substring( 0, idx );
    }
    _id = description;
  }

  /**
   * Returns a short textual description of this record.
   * 
   * @return A short textual description of this record. Not <code>null</code>.
   */
  public String getShortDescription() {

    StringBuilder result = new StringBuilder();

    // set the identifier
    result.append( _id );

    // set the start level
    if( _startlevel > 0 || _autostart ) {

      // append ampersand
      result.append( "@" );

      // set the start level
      if( _startlevel > 0 ) {
        result.append( _startlevel );
        result.append( ":" );
      }
      // set the auto start
      if( _autostart ) {
        result.append( "start" );
      }

    }

    // return the result
    return result.toString();
  }

  /**
   * Changes the id for the corresponding plugin.
   * 
   * @param newid
   *          The new id for the corresponding plugin. Neither <code>null</code> nor empty.
   */
  public void setId( String newid ) {
    _id = newid;
  }

  /**
   * Returns the id for the corresponding plugin.
   * 
   * @return The id for the corresponding plugin. Neither <code>null</code> nor empty.
   */
  public String getId() {
    return _id;
  }

  /**
   * Enables/disables the autostart for the corresponding plugin.
   * 
   * @param newautostart
   *          <code>true</code> <=> Enables the autostart for the corresponding plugin.
   */
  public void setAutoStart( boolean newautostart ) {
    _autostart = newautostart;
  }

  /**
   * Returns <code>true</code> if autostart for the corressponding plugin is enabled.
   * 
   * @return <code>true</code> <=> Autostart for the corresponding plugin is enabled.
   */
  public boolean isAutoStart() {
    return _autostart;
  }

  /**
   * Changes the current start level.
   * 
   * @param newstartlevel
   *          The new start level.
   */
  public void setStartLevel( int newstartlevel ) {
    _startlevel = newstartlevel;
  }

  /**
   * Returns the current start level.
   * 
   * @return The current start level.
   */
  public int getStartLevel() {
    return _startlevel;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String toString() {
    return String.format( "[BundleStartRecord: _id: %s, _autostart: %s, _startlevel: %s]", _id, _autostart, _startlevel );
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public int hashCode() {
    int result = 1;
    result = 31 * result + ((_id == null) ? 0 : _id.hashCode());
    result = 31 * result + (_autostart ? 0 : 1);
    result = 31 * result + _startlevel;
    return result;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public int compareTo( BundleStartRecord other ) {
    if( other != null ) {
      return _id.compareTo( other._id );
    }
    return 1;
  }

} /* ENDCLASS */
