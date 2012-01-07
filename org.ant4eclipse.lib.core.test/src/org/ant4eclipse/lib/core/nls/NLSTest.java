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
package org.ant4eclipse.lib.core.nls;

import org.junit.Assert;
import org.junit.Test;

public class NLSTest {

  @Test
  public void NLS() {
    Assert.assertEquals( "A message", TestMessages.A_MESSAGE );
    Assert.assertEquals( "A message with default", TestMessages.A_MESSAGE_WITH_DEFAULT );
    Assert.assertEquals( "The message from properties file", TestMessages.A_MESSAGE_WITH_DEFAULT_AND_PROPERTY );
    Assert.assertEquals( "An exception code", TestMessages.AN_EXCEPTION_CODE.getMessage() );
    Assert.assertEquals( "An exception with default", TestMessages.AN_EXCEPTION_CODE_WITH_DEFAULT.getMessage() );
    Assert.assertEquals( "A java exception", TestMessages.A_JAVA_EXCEPTION.getMessage() );
    Assert.assertEquals( "A java exception with default", TestMessages.A_JAVA_EXCEPTION_WITH_DEFAULT.getMessage() );
  }

} /* ENDCLASS */
