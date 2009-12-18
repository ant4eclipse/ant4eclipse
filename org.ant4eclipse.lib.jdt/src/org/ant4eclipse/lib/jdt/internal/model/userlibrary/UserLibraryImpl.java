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
import org.ant4eclipse.lib.jdt.model.userlibrary.UserLibrary;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

/**
 * Description of an user library.
 * 
 * @author Daniel Kasmeroglu (daniel.kasmeroglu@kasisoft.net)
 */
public class UserLibraryImpl implements UserLibrary {

  /** The name of the user library */
  private String            _name;

  /**
   * Represents this a system library (i.e. a library added to the boot classpath)
   */
  private boolean           _systemlibrary;

  /** The content of this library */
  private List<ArchiveImpl> _archives;

  /**
   * Creates a new user library entry with a specific name.
   * 
   * @param name
   *          The name of the user library.
   * @param syslib
   *          true <=> This library affects the boot class path.
   */
  public UserLibraryImpl(String name, boolean syslib) {
    Assure.paramNotNull("name", name);
    this._name = name;
    this._systemlibrary = syslib;
    this._archives = new LinkedList<ArchiveImpl>();
  }

  /**
   * {@inheritDoc}
   */
  public String getName() {
    return (this._name);
  }

  /**
   * {@inheritDoc}
   */
  public boolean isSystemLibrary() {
    return (this._systemlibrary);
  }

  /**
   * {@inheritDoc}
   */
  public ArchiveImpl[] getArchives() {
    ArchiveImpl[] result = new ArchiveImpl[this._archives.size()];
    this._archives.toArray(result);
    return (result);
  }

  /**
   * {@inheritDoc}
   */
  public File[] getArchiveFiles() {

    // create new result list
    List<File> result = new LinkedList<File>();

    // add all path entries
    for (ArchiveImpl archive : this._archives) {
      result.add(archive.getPath());
    }

    // return result
    return result.toArray(new File[0]);
  }

  /**
   * Adds an archive to this library entry.
   * 
   * @param arc
   *          The archive that will be added.
   */
  public void addArchive(ArchiveImpl arc) {
    Assure.paramNotNull("arc", arc);
    this._archives.add(arc);
  }
} /* ENDCLASS */
