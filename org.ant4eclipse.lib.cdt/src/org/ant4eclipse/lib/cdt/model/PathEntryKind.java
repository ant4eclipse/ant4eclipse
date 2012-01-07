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
package org.ant4eclipse.lib.cdt.model;

/**
 * Various types used to identify different kinds of path entries.
 * 
 * @author Daniel Kasmeroglu (Daniel.Kasmeroglu@Kasisoft.net)
 */
public enum PathEntryKind {

  Source( "sourcePath" ), Library( "libraryPath" ), Include( "includePath" );

  private String _key;

  /**
   * Sets up this enumeration constant with the corresponding constant used to identify the kind of the path.
   * 
   * @param key
   *          The key used to identify the kind of the path.
   */
  PathEntryKind( String key ) {
    this._key = key;
  }

  /**
   * Returns the key used to identify the type of path.
   * 
   * @return The key used to identify the type of path. Neither <code>null</code> nor empty.
   */
  public String getKey() {
    return this._key;
  }

  /**
   * Returns the enumeration value depending on the supplied key.
   * 
   * @param key
   *          The key used to identify the type of path. Neither <code>null</code> nor empty.
   * 
   * @return An identified kind or <code>null</code> if the key could not be recognized.
   */
  public static final PathEntryKind valueByKey( String key ) {
    for( PathEntryKind kind : PathEntryKind.values() ) {
      if( key.equals( kind.getKey() ) ) {
        return kind;
      }
    }
    return null;
  }

} /* ENDENUM */
