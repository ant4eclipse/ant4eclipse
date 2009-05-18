/**********************************************************************
 * Copyright (c) 2005-2008 ant4eclipse project team.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Nils Hartmann, Daniel Kasmeroglu, Gerd Wuetherich
 **********************************************************************/
package org.ant4eclipse.jdt.model.userlibrary;

import java.util.Hashtable;
import java.util.Map;

import org.ant4eclipse.core.Assert;

/**
 * This class provides the content of a user library configuration.
 * 
 * @author Daniel Kasmeroglu (daniel.kasmeroglu@kasisoft.net)
 */
public final class UserLibraries {

  private final Map<String, UserLibrary> _libraries;

  /**
   * Initialises this data structure used to collect the content of an eclipse user library configuration file.
   */
  public UserLibraries() {
    this._libraries = new Hashtable<String, UserLibrary>();
  }

  /**
   * Adds the supplied user library entry to this collecting data structure.
   * 
   * @param userlibrary
   *          The user library entry which shall be added.
   */
  public void addLibrary(final UserLibrary userlibrary) {
    Assert.notNull(userlibrary);

    this._libraries.put(userlibrary.getName(), userlibrary);
  }

  /**
   * Returns true if there's a specific library entry available.
   * 
   * @param name
   *          The name of the desired library.
   * 
   * @return true <=> A library with the supplied name is available.
   */
  public boolean hasLibrary(final String name) {
    Assert.notNull(name);

    return (this._libraries.containsKey(name));
  }

  /**
   * Returns a user library entry associated with the supplied name.
   * 
   * @param name
   *          The name used to access this user library.
   * 
   * @return The associated user library entry.
   */
  public UserLibrary getLibrary(final String name) {
    Assert.notNull(name);

    return (this._libraries.get(name));
  }

  /**
   * Returns a list with the names of the available libraries.
   * 
   * @return A list with the names of the available libraries.
   */
  public String[] getAvailableLibraries() {
    final String[] result = new String[this._libraries.size()];
    this._libraries.keySet().toArray(result);
    return (result);
  }

} /* ENDCLASS */
