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
package org.ant4eclipse.lib.pydt.model.pyre;

import org.ant4eclipse.core.data.Version;

import org.ant4eclipse.lib.pydt.model.PythonInterpreter;

import java.io.File;

/**
 * Each runtime is a combination of a predefined set of types as provided with a python release.
 * 
 * @author Daniel Kasmeroglu (Daniel.Kasmeroglu@Kasisoft.net)
 */
public interface PythonRuntime {

  /**
   * Returns the id of the {@link PythonRuntime}.
   * 
   * @return The id of the {@link PythonRuntime}. Neither <code>null</code> nor empty.
   */
  String getId();

  /**
   * Returns the location of the {@link PythonRuntime} within the filesystem.
   * 
   * @return The location of the {@link PythonRuntime}. Not <code>null</code>.
   */
  File getLocation();

  /**
   * Returns the version of this runtime.
   * 
   * @return The version of this runtime. Not <code>null</code>.
   */
  Version getVersion();

  /**
   * Returns a list of all libraries used for this runtime. This might be an egg, a jar, a zip or a directory.
   * 
   * @return A list of all libraries. Not <code>null</code>.
   */
  File[] getLibraries();

  /**
   * Returns a datastructure representing the associated python interpreter.
   * 
   * @return A datastructure representing the associated python interpreter. Not <code>null</code>.
   */
  PythonInterpreter getInterpreter();

  /**
   * Returns the location of the executable responsible to launch the python scripts.
   * 
   * @return The location of the executable responsible to launch the python scripts. Not <code>null</code>.
   */
  File getExecutable();

} /* ENDINTERFACE */