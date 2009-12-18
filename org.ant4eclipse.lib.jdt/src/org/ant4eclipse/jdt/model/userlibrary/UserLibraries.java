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
package org.ant4eclipse.jdt.model.userlibrary;

/**
 * This class provides the content of a user library configuration.
 * 
 * @author Daniel Kasmeroglu (daniel.kasmeroglu@kasisoft.net)
 */
public interface UserLibraries {

  /**
   * Returns true if there's a specific library entry available.
   * 
   * @param name
   *          The name of the desired library.
   * 
   * @return true <=> A library with the supplied name is available.
   */
  boolean hasLibrary(String name);

  /**
   * Returns a user library entry associated with the supplied name.
   * 
   * @param name
   *          The name used to access this user library.
   * 
   * @return The associated user library entry.
   */
  UserLibrary getLibrary(String name);

  /**
   * Returns a list with the names of the available libraries.
   * 
   * @return A list with the names of the available libraries.
   */
  String[] getAvailableLibraries();

} /* ENDCLASS */
