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
package org.ant4eclipse.lib.pydt.tools;

import java.io.File;

/**
 * Collection of tools to use python.
 * 
 * @author Daniel Kasmeroglu (Daniel.Kasmeroglu@Kasisoft.net)
 */
public interface PythonTools {

  /**
   * Returns the location of the epydoc installation.
   * 
   * @return The location of the epydoc installation. Maybe <code>null</code> in case the python documentation tool
   *         could not be unpacked.
   */
  File getEpydocInstallation();

} /* ENDINTERFACE */
