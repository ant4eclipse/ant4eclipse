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
package org.ant4eclipse.pde.tools;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

import org.ant4eclipse.core.Assert;

/**
 * <p>
 * Represents a definition of a target platfrom. A target platfrom contains one or more locations.
 * </p>
 * 
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 */
public class TargetPlatformDefinition {

  /** the list of all locations */
  private List<File> _locations;

  // /** */
  // private List<TargetPlatformDefinition> _platformDefinitions;

  /**
   * <p>
   * Creates a new instance of type TargetPlatformDefinition.
   * </p>
   */
  public TargetPlatformDefinition() {
    _locations = new LinkedList<File>();
    // _platformDefinitions = new LinkedList<TargetPlatformDefinition>();
  }

  /**
   * <p>
   * </p>
   * 
   * @param location
   */
  public void addLocation(File location) {
    Assert.isDirectory(location);

    if (!_locations.contains(location)) {
      _locations.add(location);
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
    return _locations.toArray(new File[0]);
  }
}
