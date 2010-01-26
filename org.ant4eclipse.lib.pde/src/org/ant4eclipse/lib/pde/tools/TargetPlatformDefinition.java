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
package org.ant4eclipse.lib.pde.tools;

import org.ant4eclipse.lib.core.Assert;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

/**
 * <p>
 * Represents a definition of a target platform. A target platform contains one or more locations that can contain
 * features and/or plug-ins.
 * </p>
 * 
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 */
public class TargetPlatformDefinition {

  /** the list of all locations */
  private List<File> _locations;

  /**
   * <p>
   * Creates a new instance of type {@link TargetPlatformDefinition}.
   * </p>
   */
  public TargetPlatformDefinition() {
    // create the locations list
    this._locations = new LinkedList<File>();
  }

  /**
   * <p>
   * Adds a location to the target platform definition.
   * </p>
   * 
   * @param location
   *          the location to add
   */
  public void addLocation(File location) {
    Assert.isDirectory(location);

    if (!this._locations.contains(location)) {
      this._locations.add(location);
    }
  }

  /**
   * <p>
   * Returns all the locations defined in this target platform location.
   * </p>
   * 
   * @return all the locations defined in this target platform location.
   */
  public final File[] getLocations() {
    return this._locations.toArray(new File[0]);
  }
}
