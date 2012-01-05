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

import org.ant4eclipse.lib.core.A4EService;
import org.ant4eclipse.lib.pydt.model.PythonInterpreter;

import java.io.File;

/**
 * Registry used to manage all runtimes used for python. The registry explicitly allows to register a specific runtime
 * as the default one. If there's only one runtime available, this will be used as the default one.
 * 
 * @author Daniel Kasmeroglu (Daniel.Kasmeroglu@Kasisoft.net)
 */
public interface PythonRuntimeRegistry extends A4EService {

  /**
   * Registers a Python runtime that is specified using the given location with this registry.
   * 
   * @param id
   *          The id of the python runtime. Neither <code>null</code> nor empty.
   * @param location
   *          The location of the runtime installation. Not <code>null</code> and must be a directory.
   * @param sitepackages
   *          <code>true</code> <=> Enable support for site packages on the runtime.
   */
  void registerRuntime(String id, File location, boolean sitepackages);

  /**
   * Sets the ID for the {@link PythonRuntime} that has to be used by default. If there's no runtime with the supplied
   * id an exception will be caused.
   * 
   * @param id
   *          The id of the default python runtime. Neither <code>null</code> nor empty.
   */
  void setDefaultRuntime(String id);

  /**
   * Returns <code>true</code> if a python runtime is registered with the given id.
   * 
   * @param id
   *          The id which has been used to register the python runtime. Neither <code>null</code> nor empty.
   * 
   * @return <code>true</code> <=> The java runtime with the given id is known.
   */
  boolean hasRuntime(String id);

  /**
   * Returns the runtime with the given id.
   * 
   * @param id
   *          The id which has been used to register the python runtime. Neither <code>null</code> nor empty.
   * 
   * @return The python runtime with the given path or <code>null</code> if {@link #hasRuntime(String)} is
   *         <code>false</code>.
   */
  PythonRuntime getRuntime(String id);

  /**
   * Returns the default python runtime. This method will cause an exception when no default runtime is available.
   * 
   * @return The default python runtime. Not <code>null</code>.
   */
  PythonRuntime getRuntime();

  /**
   * Returns a list of supported python interpreters.
   * 
   * @return A list of supported python interpreters. Not <code>null</code>.
   */
  PythonInterpreter[] getSupportedInterpreters();

} /* ENDINTERFACE */
