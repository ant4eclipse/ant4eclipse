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

import org.ant4eclipse.lib.jdt.model.userlibrary.Archive;
import org.ant4eclipse.lib.jdt.model.userlibrary.UserLibrary;

import java.io.File;
import java.util.ArrayList;
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
  private List<Archive>     _archives;

  /**
   * Creates a new user library entry with a specific name.
   * 
   * @param name
   *          The name of the user library.
   * @param syslib
   *          true <=> This library affects the boot class path.
   */
  // Assure.notNull( "name", name );
  public UserLibraryImpl( String name, boolean syslib ) {
    _name = name;
    _systemlibrary = syslib;
    _archives = new ArrayList<Archive>();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getName() {
    return _name;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isSystemLibrary() {
    return _systemlibrary;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public List<Archive> getArchives() {
    return _archives;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public List<File> getArchiveFiles() {
    List<File> result = new ArrayList<File>();
    for( Archive archive : _archives ) {
      result.add( archive.getPath() );
    }
    return result;
  }

  /**
   * Adds an archive to this library entry.
   * 
   * @param arc
   *          The archive that will be added.
   */
  // Assure.notNull( "arc", arc );
  public void addArchive( ArchiveImpl arc ) {
    _archives.add( arc );
  }
  
} /* ENDCLASS */
