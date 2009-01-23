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
package org.ant4eclipse.jdt.ejc;

import org.ant4eclipse.core.exception.ExceptionCode;
import org.ant4eclipse.core.nls.NLS;
import org.ant4eclipse.core.nls.NLSMessage;

/**
 * <p>
 * Defines the exception codes for the eclipse java compiler subsystem.
 * </p>
 * 
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 */
public class EjcExceptionCodes extends ExceptionCode {

  @NLSMessage("The file '%s' with global compiler settings could not be found.")
  public static EjcExceptionCodes GLOBAL_COMPILER_SETTINGS_NOT_FOUND_EXCEPTION;

  @NLSMessage("Unable to read content of compilation unit '%s' in source folder '%s' with encoding '%s'.")
  public static EjcExceptionCodes UNABLE_TO_READ_COMPILATION_CONTENT_EXCEPTION;

  static {
    NLS.initialize(EjcExceptionCodes.class);
  }

  /**
   * <p>
   * Creates a new instance of type {@link EjcExceptionCodes}.
   * </p>
   * 
   * @param message
   *          the message
   */
  private EjcExceptionCodes(final String message) {
    super(message);
  }
}
