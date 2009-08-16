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
package org.ant4eclipse.pydt;

import org.ant4eclipse.core.exception.ExceptionCode;
import org.ant4eclipse.core.nls.NLS;
import org.ant4eclipse.core.nls.NLSMessage;

/**
 * ExceptionCodes for Pydt tools.
 * 
 * @author Daniel Kasmeroglu (Daniel.Kasmeroglu@Kasisoft.net)
 */
public class PydtExceptionCode extends ExceptionCode {

  @NLSMessage("Project '%s' must have a python project role (either PyDev or Python DLTK).")
  public static PydtExceptionCode NO_PYTHON_PROJECT_ROLE;

  @NLSMessage("The python runtime with the id '%s' has not been registered (see 'pythonContainer').")
  public static PydtExceptionCode UNKNOWN_PYTHON_RUNTIME;

  static {
    NLS.initialize(PydtExceptionCode.class);
  }

  private PydtExceptionCode(String message) {
    super(message);
  }

} /* ENDCLASS */
