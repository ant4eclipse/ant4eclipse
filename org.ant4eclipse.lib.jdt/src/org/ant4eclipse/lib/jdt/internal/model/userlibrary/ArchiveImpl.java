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
import org.ant4eclipse.lib.jdt.model.userlibrary.Archive;

import java.io.File;

/**
 * Simple library entry within the user library configuration file.
 * 
 * @author Daniel Kasmeroglu (daniel.kasmeroglu@kasisoft.net)
 */
public class ArchiveImpl implements Archive {

  /** the path */
  private File   _path;

  /** the javadoc */
  private String _javadoc;

  /** the source */
  private File   _source;

  /**
   * Creates an archive entry provding some infos regarding a classpath entry.
   * 
   * @param path
   *          The location of the classes. Maybe a directory or a Jar.
   */
  public ArchiveImpl( File path ) {
    this( path, null, null );
  }

  /**
   * Creates an archive entry provding some infos regarding a classpath entry.
   * 
   * @param path
   *          The location of the classes. Maybe a directory or a Jar.
   * @param source
   *          The location of corresponding sources.
   * @param javadoc
   *          The location of the javadocs as an url.
   */
  public ArchiveImpl( File path, File source, String javadoc ) {
    Assure.exists( "path", path );
    _path = path;
    setSource( source );
    setJavaDoc( javadoc );
  }

  /**
   * Changes the source entry for this archive.
   * 
   * @param newsource
   *          The new source entry for this archive.
   */
  public void setSource( File newsource ) {
    _source = newsource;
  }

  /**
   * Changes the javadoc entry for this archive.
   * 
   * @param newjavadoc
   *          The new javadoc entry.
   */
  public void setJavaDoc( String newjavadoc ) {
    _javadoc = newjavadoc;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public File getPath() {
    return _path;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public File getSource() {
    return _source;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getJavaDoc() {
    return _javadoc;
  }

} /* ENDCLASS */
