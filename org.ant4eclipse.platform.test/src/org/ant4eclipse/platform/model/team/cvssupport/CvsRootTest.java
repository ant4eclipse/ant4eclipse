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
package org.ant4eclipse.platform.model.team.cvssupport;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class CvsRootTest {

  @Test
  public void test_Parse() {
    CvsRoot cvsRoot = new CvsRoot(":pserver:rob:secret@ant4eclipse.org/cvsroot");
    assertEquals("pserver", cvsRoot.getConnectionType());
    assertEquals("rob", cvsRoot.getUser());
    assertEquals("secret", cvsRoot.getEncodedPassword());
    assertEquals("ant4eclipse.org", cvsRoot.getHost());
    assertEquals("/cvsroot", cvsRoot.getRepository());
  }

  @Test
  public void test_NoPassword() {

    CvsRoot cvsRoot = new CvsRoot(":pserver:rob@ant4eclipse.org/cvsroot");
    assertEquals("pserver", cvsRoot.getConnectionType());
    assertEquals("rob", cvsRoot.getUser());
    assertNull(cvsRoot.getEncodedPassword());
    assertEquals("ant4eclipse.org", cvsRoot.getHost());
    assertEquals("/cvsroot", cvsRoot.getRepository());
  }

  @Test
  public void test_WithPort() {
    CvsRoot cvsRoot = new CvsRoot(":pserver:rob@ant4eclipse.org:1234/cvsroot");
    assertEquals("pserver", cvsRoot.getConnectionType());
    assertEquals("rob", cvsRoot.getUser());
    assertNull(cvsRoot.getEncodedPassword());
    assertEquals("ant4eclipse.org:1234", cvsRoot.getHost());
    assertEquals("/cvsroot", cvsRoot.getRepository());
  }

  @Test
  public void test_NoConnectionType_Ext() {
    CvsRoot cvsRoot = new CvsRoot("rob@ant4eclipse.org:1234/cvsroot");
    assertEquals("ext", cvsRoot.getConnectionType());
    assertEquals("rob", cvsRoot.getUser());
    assertNull(cvsRoot.getEncodedPassword());
    assertEquals("ant4eclipse.org:1234", cvsRoot.getHost());
    assertEquals("/cvsroot", cvsRoot.getRepository());
  }

  @Test
  public void test_No_ConnectionType_NoUser() {
    CvsRoot cvsRoot = new CvsRoot("ant4eclipse.org:1234/cvsroot");
    assertEquals("ext", cvsRoot.getConnectionType());
    assertNull(cvsRoot.getUser());
    assertNull(cvsRoot.getEncodedPassword());
    assertEquals("ant4eclipse.org:1234", cvsRoot.getHost());
    assertEquals("/cvsroot", cvsRoot.getRepository());
  }

  @Test
  public void test_LocalCvsRoot() {
    CvsRoot cvsRoot = new CvsRoot("/cvsroot");
    assertEquals("local", cvsRoot.getConnectionType());
    assertNull(cvsRoot.getUser());
    assertNull(cvsRoot.getEncodedPassword());
    assertNull(cvsRoot.getHost());
    assertEquals("/cvsroot", cvsRoot.getRepository());

  }

}
