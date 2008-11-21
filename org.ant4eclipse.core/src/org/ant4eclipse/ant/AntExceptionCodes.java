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
package org.ant4eclipse.ant;

import org.ant4eclipse.core.NLSMessage;
import org.ant4eclipse.core.NLS;
import org.ant4eclipse.core.exception.ExceptionCode;

/**
 * Defines ExceptionCodes to be used in the 'ant'-layer
 * 
 * @author Nils Hartmann
 */
public class AntExceptionCodes extends ExceptionCode {

  @NLSMessage("Paramter '%s' must be set on task '%s'")
  public static AntExceptionCodes PARAMETER_MUST_BE_SET_ON_TASK;

  @NLSMessage("Paramter '%s' must be set on type '%s'")
  public static AntExceptionCodes PARAMETER_MUST_BE_SET_ON_TYPE;

  private AntExceptionCodes(final String message) {
    super(message);
  }

  static {
    NLS.initialize(AntExceptionCodes.class);
  }

}
