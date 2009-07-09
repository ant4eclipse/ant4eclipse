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
package org.ant4eclipse.core.nls;

import org.ant4eclipse.core.CoreExceptionCode;

public class TestMessages {

  public static String            A_MESSAGE;

  public static final String      A_CONSTANT = "A constant";

  @NLSMessage("A message with default")
  public static String            A_MESSAGE_WITH_DEFAULT;

  @NLSMessage("A message with default and property")
  public static String            A_MESSAGE_WITH_DEFAULT_AND_PROPERTY;

  public static CoreExceptionCode AN_EXCEPTION_CODE;

  @NLSMessage("An exception with default")
  public static CoreExceptionCode AN_EXCEPTION_CODE_WITH_DEFAULT;

  @NLSMessage
  public static Exception         A_JAVA_EXCEPTION;

  @NLSMessage("A java exception with default")
  public static Exception         A_JAVA_EXCEPTION_WITH_DEFAULT;

  static {
    NLS.initialize(TestMessages.class);
  }

}
