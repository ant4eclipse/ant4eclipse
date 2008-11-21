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
package org.ant4eclipse.platform.model;

import org.ant4eclipse.core.NLS;
import org.ant4eclipse.core.NLSMessage;
import org.ant4eclipse.core.exception.ExceptionCode;

public class PlatformModelExceptionCode extends ExceptionCode {

  @NLSMessage("There are two projects with the same specified name: '%s', '%s' ")
  public static PlatformModelExceptionCode PROJECT_WITH_SAME_SPECIFIED_NAME_ALREADY_EXISTS;

  static {
    NLS.initialize(PlatformModelExceptionCode.class);
  }

  private PlatformModelExceptionCode(final String message) {
    super(message);
  }
}
