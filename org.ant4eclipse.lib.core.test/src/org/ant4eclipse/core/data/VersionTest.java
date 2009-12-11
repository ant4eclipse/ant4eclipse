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
package org.ant4eclipse.core.data;

import org.ant4eclipse.lib.core.CoreExceptionCode;
import org.ant4eclipse.lib.core.data.Version;
import org.ant4eclipse.lib.core.exception.Ant4EclipseException;
import org.junit.Assert;
import org.junit.Test;

public class VersionTest {

  @Test
  public void illegalFormat() {
    try {
      new Version("a");
    } catch (Ant4EclipseException ex) {
      Assert.assertEquals(CoreExceptionCode.ILLEGAL_FORMAT, ex.getExceptionCode());
    }
    try {
      new Version("1.2.3.4");
    } catch (Ant4EclipseException ex) {
      Assert.assertEquals(CoreExceptionCode.ILLEGAL_FORMAT, ex.getExceptionCode());
    }
    try {
      new Version(" ");
    } catch (Ant4EclipseException ex) {
      Assert.assertEquals(CoreExceptionCode.ILLEGAL_FORMAT, ex.getExceptionCode());
    }
  }

  @Test
  public void validFormat() {
    Assert.assertEquals("1.0.0", String.valueOf(new Version("1")));
    Assert.assertEquals("1.2.0", String.valueOf(new Version("1.2")));
    Assert.assertEquals("1.2.3", String.valueOf(new Version("1.2.3")));
    Assert.assertEquals("1.2.3_a", String.valueOf(new Version("1.2.3_a")));
  }

} /* ENDCLASS */
