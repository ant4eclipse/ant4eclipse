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
package org.ant4eclipse.lib.core.osgi;

import org.ant4eclipse.lib.core.exception.ExceptionCode;

/**
 * <p>
 * Collection of failure codes for osgi related tools.
 * </p>
 */
public class OsgiToolsExceptionCodes extends ExceptionCode {

  /** -- */
  public static OsgiToolsExceptionCodes GLOBAL_COMPILER_SETTINGS_NOT_FOUND = new OsgiToolsExceptionCodes(
                                                                               "The file with global compiler settings '%s' could not be found");

  /**
   * <p>
   * Initialises this failure code with a specific message.
   * </p>
   * 
   * @param message
   *          The message used for the user presentation. Not <code>null</code>.
   */
  public OsgiToolsExceptionCodes(String message) {
    super(message);

  }

} /* ENDCLASS */
