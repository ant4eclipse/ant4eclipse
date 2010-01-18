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
package org.ant4eclipse.core.nls;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class NLSTest {

  @Test
  public void test_NLS() {

    assertEquals("A message", TestMessages.A_MESSAGE);
    assertEquals("A message with default", TestMessages.A_MESSAGE_WITH_DEFAULT);
    assertEquals("The message from properties file", TestMessages.A_MESSAGE_WITH_DEFAULT_AND_PROPERTY);
    assertEquals("An exception code", TestMessages.AN_EXCEPTION_CODE.getMessage());
    assertEquals("An exception with default", TestMessages.AN_EXCEPTION_CODE_WITH_DEFAULT.getMessage());
    assertEquals("A java exception", TestMessages.A_JAVA_EXCEPTION.getMessage());
    assertEquals("A java exception with default", TestMessages.A_JAVA_EXCEPTION_WITH_DEFAULT.getMessage());

  }

}
