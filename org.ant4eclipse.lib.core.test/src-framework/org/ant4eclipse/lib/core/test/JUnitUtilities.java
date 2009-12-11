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
package org.ant4eclipse.lib.core.test;

import java.io.File;
import java.io.IOException;

import junit.framework.Assert;

/**
 * <p>
 * Collection of utilities used in conjunction with the JUnit testsuite.
 * </p>
 * 
 * @author Daniel Kasmeroglu (Daniel.Kasmeroglu@Kasisoft.net)
 */
public class JUnitUtilities {

  /**
   * <p>
   * Creates a temporary directory.
   * </p>
   * 
   * @return A temporary directory. Not <code>null</code>.
   */
  public static final File createTempDir() {
    try {
      File tempfile = File.createTempFile("a4e.", ".dir");
      int tries = 10;
      while (tempfile.isFile() && (tries > 0)) {
        if (tempfile.delete()) {
          break;
        } else {
          try {
            Thread.sleep(3000);
          } catch (InterruptedException ex) {
          }
        }
        tries--;
      }
      if (tempfile.isFile()) {
        Assert.fail();
      }
      if (!tempfile.mkdir()) {
        Assert.fail();
      }
      return tempfile;
    } catch (IOException ex) {
      Assert.fail(ex.getMessage());
      return null;
    }
  }

} /* ENDCLASS */