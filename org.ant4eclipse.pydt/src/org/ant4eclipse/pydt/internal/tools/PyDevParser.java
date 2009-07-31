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
package org.ant4eclipse.pydt.internal.tools;

import org.ant4eclipse.pydt.internal.model.project.PythonProjectRoleImpl;

/**
 * This parser is used to contribute the necessary configuration information to the associated project role. This
 * implementation is used to support the PyDev framework.
 * 
 * @author Daniel Kasmeroglu (Daniel.Kasmeroglu@Kasisoft.net)
 */
public class PyDevParser {

  /**
   * Enriches the supplied role with the necessary information taken from the configuration files used by the PyDev
   * framework.
   * 
   * @param pythonrole
   *          The role instance which will be filled with the corresponding information. Not <code>null</code>.
   */
  public static final void contributePathes(PythonProjectRoleImpl pythonrole) {
  }

} /* ENDCLASS */
