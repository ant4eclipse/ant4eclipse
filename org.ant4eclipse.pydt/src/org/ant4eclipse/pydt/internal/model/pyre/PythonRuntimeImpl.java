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
package org.ant4eclipse.pydt.internal.model.pyre;

import org.ant4eclipse.pydt.model.pyre.PythonRuntime;

import java.io.File;

/**
 * Each runtime is a combination of a predefined set of types as provided with a python release.
 * 
 * @author Daniel Kasmeroglu (Daniel.Kasmeroglu@Kasisoft.net)
 */
class PythonRuntimeImpl implements PythonRuntime {

  private String _id;

  private File   _location;

  /**
   * Initialises this runtime implementation.
   * 
   * @param id
   *          The id associated with this runtime. Neither <code>null</code> nor empty.
   * @param location
   *          The location within the filesystem. Not <code>null</code>.
   */
  public PythonRuntimeImpl(final String id, final File location) {
    _id = id;
    _location = location;
  }

  /**
   * Returns the id of the {@link PythonRuntimeImpl}.
   * 
   * @return The id of the {@link PythonRuntimeImpl}. Neither <code>null</code> nor empty.
   */
  public String getId() {
    return _id;
  }

  /**
   * Returns the location of the {@link PythonRuntimeImpl} within the filesystem.
   * 
   * @return The location of the {@link PythonRuntimeImpl}. Not <code>null</code>.
   */
  public File getLocation() {
    return _location;
  }

} /* ENDCLASS */