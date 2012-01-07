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
package org.ant4eclipse.lib.pde.model.link;

import org.ant4eclipse.lib.core.Assure;

import java.io.File;

/**
 * <p>
 * {@link LinkFile} is a representation of a <code>*.link</code> file that is used by eclipse to link (external)
 * directories into a platform location.
 * </p>
 * 
 * @author Nils Hartmann (nils@nilshartmann.net)
 */
public class LinkFile {

  /**
   * The directory path beneath _destination that contains the plugins
   * 
   * @see #getPluginsDirectory()
   */
  public static final String PLUGINS_DIRECTORY  = "eclipse/plugins";

  /** -- */
  public static final String FEATURES_DIRECTORY = "eclipse/features";

  /**
   * The path to the destination directory.
   */
  private File               _destination;

  /**
   * <p>
   * Creates a new instance of type {@link LinkFile}.
   * </p>
   * 
   * @param destination
   *          the destination of the link file.
   */
  public LinkFile( File destination ) {
    Assure.notNull( "destination", destination );
    _destination = destination;
  }

  /**
   * <p>
   * Checks if the destination directory is valid. A destination is valid if it is an existing directory that contains
   * an <code>eclipse/plugins</code> folder.
   * </p>
   * 
   * @return <code>true</code> if this is a valid destination
   */
  public boolean isValidDestination() {
    return getPluginsDirectory().isDirectory();
  }

  /**
   * <p>
   * Returns the plugins directory inside the destination directory. Note: The plugins directory might not exist.
   * </p>
   * 
   * @return the plugins directory
   * 
   * @see #isValidDestination()
   * @see #PLUGINS_DIRECTORY
   */
  public File getPluginsDirectory() {
    return new File( _destination, PLUGINS_DIRECTORY );
  }

  /**
   * <p>
   * Returns the features directory inside the destination directory. Note: The features directory might not exist.
   * </p>
   * 
   * @return the features directory
   */
  public File getFeaturesDirectory() {
    return new File( _destination, FEATURES_DIRECTORY );
  }

  /**
   * <p>
   * The destination of the link file
   * </p>
   * 
   * @return the destination of this link file
   */
  public File getDestination() {
    return _destination;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public int hashCode() {
    int PRIME = 31;
    int result = 1;
    result = PRIME * result + ((_destination == null) ? 0 : _destination.hashCode());
    return result;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean equals( Object obj ) {
    if( this == obj ) {
      return true;
    }
    if( obj == null ) {
      return false;
    }
    if( getClass() != obj.getClass() ) {
      return false;
    }
    LinkFile other = (LinkFile) obj;
    if( _destination == null ) {
      if( other._destination != null ) {
        return false;
      }
    } else if( !_destination.equals( other._destination ) ) {
      return false;
    }
    return true;
  }

} /* ENDCLASS */
