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

import java.util.LinkedList;
import java.util.List;

import org.ant4eclipse.core.Assert;

/**
 * Description of an user library.
 * 
 * @author Daniel Kasmeroglu (daniel.kasmeroglu@kasisoft.net)
 */
public class UserLibrary {

  /**
   * The name of the user library
   */
  private final String        _name;

  /**
   * Represents this a system library (i.e. a library added to the boot classpath)
   */
  private final boolean       _systemlibrary;

  /**
   * The content of this library.
   * 
   * @see Archive
   */
  private final List<Archive> _archives;

  /**
   * Creates a new user library entry with a specific name.
   * 
   * @param name
   *          The name of the user library.
   * @param syslib
   *          true <=> This library affects the boot class path.
   */
  public UserLibrary(final String name, final boolean syslib) {
    Assert.notNull(name);

    this._name = name;
    this._systemlibrary = syslib;
    this._archives = new LinkedList<Archive>();
  }

  /**
   * Returns the name of this user library.
   * 
   * @return The name of this user library.
   */
  public String getName() {
    return (this._name);
  }

  /**
   * Returns true if this library affects the boot class path.
   * 
   * @return true <=> This library affects the boot class path.
   */
  public boolean isSystemLibrary() {
    return (this._systemlibrary);
  }

  /**
   * Adds an archive to this library entry.
   * 
   * @param arc
   *          The archive that will be added.
   */
  public void addArchive(final Archive arc) {
    Assert.notNull(arc);

    this._archives.add(arc);
  }

  /**
   * Returns a list of archives that are registered with this library entry.
   * 
   * @return A list of archives that are registered with this library entry.
   */
  public Archive[] getArchives() {
    final Archive[] result = new Archive[this._archives.size()];
    this._archives.toArray(result);
    return (result);
  }

} /* ENDCLASS */
