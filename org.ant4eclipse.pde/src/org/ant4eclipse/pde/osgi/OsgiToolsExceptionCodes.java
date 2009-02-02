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
package org.ant4eclipse.pde.osgi;

import org.ant4eclipse.core.exception.ExceptionCode;


public class OsgiToolsExceptionCodes extends ExceptionCode {

  public static OsgiToolsExceptionCodes GLOBAL_COMPILER_SETTINGS_NOT_FOUND = new OsgiToolsExceptionCodes(
                                                                               "The file with global compiler settings '%s' could not be found");

  public OsgiToolsExceptionCodes(final String message) {
    super(message);

  }

}
