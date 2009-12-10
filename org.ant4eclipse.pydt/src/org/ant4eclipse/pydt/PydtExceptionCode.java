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
package org.ant4eclipse.pydt;

import org.ant4eclipse.lib.core.exception.ExceptionCode;
import org.ant4eclipse.lib.core.nls.NLS;
import org.ant4eclipse.lib.core.nls.NLSMessage;

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

  @NLSMessage("Failed to create relative path from '%s' to '%s'.")
  public static PydtExceptionCode NO_RELATIVE_PATH;

  @NLSMessage("The project '%s' must have the Python or PyDev project role!")
  public static PydtExceptionCode MISSING_PYTHON_ROLE;

  @NLSMessage("The Project '%s' contains multiple source folders ! If you want to allow this, you have to set allowMultipleFolders='true'!")
  public static PydtExceptionCode MULTIPLEFOLDERS;

  @NLSMessage("The attribute '%s' has not been set !")
  public static PydtExceptionCode MISSINGATTRIBUTE;

  @NLSMessage("The path '%s' doesn't refer to a directory !")
  public static PydtExceptionCode NOTADIRECTORY;

  static {
    NLS.initialize(PydtExceptionCode.class);
  }

  private PydtExceptionCode(String message) {
    super(message);
  }

} /* ENDCLASS */
