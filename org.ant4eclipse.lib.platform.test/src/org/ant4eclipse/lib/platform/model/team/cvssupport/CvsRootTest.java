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
package org.ant4eclipse.lib.platform.model.team.cvssupport;

import org.junit.Assert;
import org.junit.Test;

public class CvsRootTest {

  @Test
  public void parse() {
    CvsRoot cvsRoot = new CvsRoot(":pserver:rob:secret@ant4eclipse.org/cvsroot");
    Assert.assertEquals("pserver", cvsRoot.getConnectionType());
    Assert.assertEquals("rob", cvsRoot.getUser());
    Assert.assertEquals("secret", cvsRoot.getEncodedPassword());
    Assert.assertEquals("ant4eclipse.org", cvsRoot.getHost());
    Assert.assertEquals("/cvsroot", cvsRoot.getRepository());
  }

  @Test
  public void noUser() {
    CvsRoot cvsRoot = new CvsRoot(":pserver:ant4eclipse.org:/cvsroot/ant4eclipse");
    Assert.assertEquals("pserver", cvsRoot.getConnectionType());
    Assert.assertFalse(cvsRoot.hasUser());
    Assert.assertNull(cvsRoot.getUser());
    Assert.assertNull(cvsRoot.getEncodedPassword());
    Assert.assertEquals("ant4eclipse.org", cvsRoot.getHost());
    Assert.assertEquals("/cvsroot/ant4eclipse", cvsRoot.getRepository());
  }

  @Test
  public void noPassword() {

    CvsRoot cvsRoot = new CvsRoot(":pserver:rob@ant4eclipse.org/cvsroot");
    Assert.assertEquals("pserver", cvsRoot.getConnectionType());
    Assert.assertEquals("rob", cvsRoot.getUser());
    Assert.assertNull(cvsRoot.getEncodedPassword());
    Assert.assertEquals("ant4eclipse.org", cvsRoot.getHost());
    Assert.assertEquals("/cvsroot", cvsRoot.getRepository());
  }

  @Test
  public void withPort() {
    CvsRoot cvsRoot = new CvsRoot(":pserver:rob@ant4eclipse.org:1234/cvsroot");
    Assert.assertEquals("pserver", cvsRoot.getConnectionType());
    Assert.assertEquals("rob", cvsRoot.getUser());
    Assert.assertNull(cvsRoot.getEncodedPassword());
    Assert.assertEquals("ant4eclipse.org:1234", cvsRoot.getHost());
    Assert.assertEquals("/cvsroot", cvsRoot.getRepository());
  }

  @Test
  public void noConnectionTypeExt() {
    CvsRoot cvsRoot = new CvsRoot("rob@ant4eclipse.org:1234/cvsroot");
    Assert.assertEquals("ext", cvsRoot.getConnectionType());
    Assert.assertEquals("rob", cvsRoot.getUser());
    Assert.assertNull(cvsRoot.getEncodedPassword());
    Assert.assertEquals("ant4eclipse.org:1234", cvsRoot.getHost());
    Assert.assertEquals("/cvsroot", cvsRoot.getRepository());
  }

  @Test
  public void noConnectionTypeNoUser() {
    CvsRoot cvsRoot = new CvsRoot("ant4eclipse.org:1234/cvsroot");
    Assert.assertEquals("ext", cvsRoot.getConnectionType());
    Assert.assertNull(cvsRoot.getUser());
    Assert.assertNull(cvsRoot.getEncodedPassword());
    Assert.assertEquals("ant4eclipse.org:1234", cvsRoot.getHost());
    Assert.assertEquals("/cvsroot", cvsRoot.getRepository());
  }

  @Test
  public void localCvsRoot() {
    CvsRoot cvsRoot = new CvsRoot("/cvsroot");
    Assert.assertEquals("local", cvsRoot.getConnectionType());
    Assert.assertNull(cvsRoot.getUser());
    Assert.assertNull(cvsRoot.getEncodedPassword());
    Assert.assertNull(cvsRoot.getHost());
    Assert.assertEquals("/cvsroot", cvsRoot.getRepository());
  }

} /* ENDCLASS */
