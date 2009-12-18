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
package org.ant4eclipse.lib.jdt.internal.model.userlibrary;

import org.ant4eclipse.lib.core.Assure;
import org.ant4eclipse.lib.jdt.model.userlibrary.UserLibraries;

import java.util.Hashtable;
import java.util.Map;

/**
 * This class provides the content of a user library configuration.
 * 
 * @author Daniel Kasmeroglu (daniel.kasmeroglu@kasisoft.net)
 */
public final class UserLibrariesImpl implements UserLibraries {

  private Map<String, UserLibraryImpl> _libraries;

  /**
   * Initialises this data structure used to collect the content of an eclipse user library configuration file.
   */
  public UserLibrariesImpl() {
    this._libraries = new Hashtable<String, UserLibraryImpl>();
  }

  /**
   * Adds the supplied user library entry to this collecting data structure.
   * 
   * @param userlibrary
   *          The user library entry which shall be added.
   */
  public void addLibrary(UserLibraryImpl userlibrary) {
    Assure.paramNotNull("userlibrary", userlibrary);
    this._libraries.put(userlibrary.getName(), userlibrary);
  }

  /**
   * {@inheritDoc}
   */
  public boolean hasLibrary(String name) {
    Assure.paramNotNull("name", name);
    return this._libraries.containsKey(name);
  }

  /**
   * {@inheritDoc}
   */
  public UserLibraryImpl getLibrary(String name) {
    Assure.paramNotNull("name", name);
    return this._libraries.get(name);
  }

  /**
   * {@inheritDoc}
   */
  public String[] getAvailableLibraries() {
    String[] result = new String[this._libraries.size()];
    this._libraries.keySet().toArray(result);
    return result;
  }

} /* ENDCLASS */
