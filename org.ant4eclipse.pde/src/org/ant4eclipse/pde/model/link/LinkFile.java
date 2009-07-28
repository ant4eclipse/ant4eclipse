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
package org.ant4eclipse.pde.model.link;

import java.io.File;

import org.ant4eclipse.core.Assert;

/**
 * <p>
 * LinkFile is a representation of a *.link-File that is used by eclipse to link (external) directories into a platform
 * location.
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

  public static final String FEATURES_DIRECTORY = "eclipse/features";

  /**
   * The path to the destination directory.
   */
  private final File         _destination;

  /**
   * <p>
   * Creates a new instance of type LinkFile. 
   * </p>
   *
   * @param destination
   */
  public LinkFile(final File destination) {
    Assert.notNull(destination);
    _destination = destination;
  }

  /**
   * Checks if the destination directory is valid.
   * 
   * <p>
   * A destination is valid if it is an existing directory that contains an eclipse/plugins folder.
   * 
   * @return true if this is a valid destination
   */
  public boolean isValidDestination() {
    return getPluginsDirectory().isDirectory();
  }

  /**
   * Returns the plugins directory inside the destination directory.
   * <p>
   * Note:The plugins directory might not exist
   * 
   * @return The plugins directory
   * @see #isValidDestination()
   * @see #PLUGINS_DIRECTORY
   */
  public File getPluginsDirectory() {
    return new File(_destination, PLUGINS_DIRECTORY);
  }

  public File getFeaturesDirectory() {
    return new File(_destination, FEATURES_DIRECTORY);
  }

  /**
   * @return The destination of this link file
   */
  public File getDestination() {
    return _destination;
  }

  public int hashCode() {
    final int PRIME = 31;
    int result = 1;
    result = PRIME * result + ((_destination == null) ? 0 : _destination.hashCode());
    return result;
  }

  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    final LinkFile other = (LinkFile) obj;
    if (_destination == null) {
      if (other._destination != null)
        return false;
    } else if (!_destination.equals(other._destination))
      return false;
    return true;
  }

}
