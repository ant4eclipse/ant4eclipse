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
package org.ant4eclipse.pydt.model.pyre;

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

} /* ENDINTERFACE */