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
package org.ant4eclipse.core.util;

import org.ant4eclipse.lib.core.test.JUnitUtilities;
import org.ant4eclipse.lib.core.util.Utilities;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.jar.JarFile;

import junit.framework.Assert;

public class UtilitiesTest {

  @Test
  public void expand() throws IOException {

    File destdir = JUnitUtilities.createTempDir();

    URL url = getClass().getClassLoader().getResource("util/test-jar.jar");
    File file = new File(url.getFile());

    JarFile jarfile = new JarFile(file);
    try {
      Utilities.expandJarFile(jarfile, destdir);
    } finally {
      jarfile.close();
    }

    Assert.assertTrue(new File(destdir, "test.jar").isFile());
    Assert.assertTrue(new File(destdir, "test.txt").isFile());
    Assert.assertTrue(new File(destdir, "test2.jar").isFile());
    Assert.assertTrue(new File(destdir, "META-INF").isDirectory());
    Assert.assertTrue(new File(destdir, "META-INF/MANIFEST.MF").isFile());

  }

  /*
   * public void testCalcRelative() { String relative = Utilities.calcRelative(new File("/schnerd"), new
   * File("/temp/rep/schrepp/depp")); System.out.println(relative); }
   */

  /*
   * public void test_newInstance() { Dummy dummy = Utilities.newInstance(Dummy.class.getName());
   * Assert.assertNotNull(dummy); }
   */

  /*
   * public static class Dummy { public Dummy() { // needed } }
   */

} /* ENDCLASS */
