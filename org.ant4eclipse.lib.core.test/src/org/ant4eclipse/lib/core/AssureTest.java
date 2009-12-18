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
package org.ant4eclipse.lib.core;

import org.ant4eclipse.lib.core.exception.Ant4EclipseException;
import org.junit.Assert;
import org.junit.Test;

import javax.swing.JFrame;

import java.io.File;
import java.io.IOException;

/**
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 */
public class AssureTest {

  @Test
  public void assertNotNull() {
    Assure.paramNotNull("dummy", new Object());
    try {
      Assure.paramNotNull("dummy", null);
    } catch (Ant4EclipseException ex) {
      Assert.assertEquals(CoreExceptionCode.PRECONDITION_VIOLATION, ex.getExceptionCode());
    }
  }

  @Test
  public void instanceOf() {
    Assure.instanceOf("parameter", "", String.class);
    try {
      Assure.instanceOf("parameter", new Object(), String.class);
    } catch (Ant4EclipseException ex) {
      Assert.assertEquals(CoreExceptionCode.PRECONDITION_VIOLATION, ex.getExceptionCode());
    }
    try {
      Assure.instanceOf("parameter", null, JFrame.class);
    } catch (Ant4EclipseException ex) {
      Assert.assertEquals(CoreExceptionCode.PRECONDITION_VIOLATION, ex.getExceptionCode());
    }
  }

  @Test
  public void assertNonEmpty() {
    Assure.nonEmpty("dummy", "test");
    Assure.nonEmpty("param", new boolean[] { false });
    Assure.nonEmpty("param", new byte[] { (byte) 0 });
    Assure.nonEmpty("param", new short[] { (short) 0 });
    Assure.nonEmpty("param", new char[] { (char) 0 });
    Assure.nonEmpty("param", new int[] { 0 });
    Assure.nonEmpty("param", new long[] { 0 });
    try {
      Assure.nonEmpty("dummy", "");
    } catch (Ant4EclipseException ex) {
      Assert.assertEquals(CoreExceptionCode.PRECONDITION_VIOLATION, ex.getExceptionCode());
    }
    try {
      Assure.nonEmpty("dummy", (String) null);
    } catch (Ant4EclipseException ex) {
      Assert.assertEquals(CoreExceptionCode.PRECONDITION_VIOLATION, ex.getExceptionCode());
    }
    try {
      Assure.nonEmpty("param", new boolean[0]);
    } catch (Ant4EclipseException ex) {
      Assert.assertEquals(CoreExceptionCode.PRECONDITION_VIOLATION, ex.getExceptionCode());
    }
    try {
      Assure.nonEmpty("param", new byte[0]);
    } catch (Ant4EclipseException ex) {
      Assert.assertEquals(CoreExceptionCode.PRECONDITION_VIOLATION, ex.getExceptionCode());
    }
    try {
      Assure.nonEmpty("param", new char[0]);
    } catch (Ant4EclipseException ex) {
      Assert.assertEquals(CoreExceptionCode.PRECONDITION_VIOLATION, ex.getExceptionCode());
    }
    try {
      Assure.nonEmpty("param", new short[0]);
    } catch (Ant4EclipseException ex) {
      Assert.assertEquals(CoreExceptionCode.PRECONDITION_VIOLATION, ex.getExceptionCode());
    }
    try {
      Assure.nonEmpty("param", new int[0]);
    } catch (Ant4EclipseException ex) {
      Assert.assertEquals(CoreExceptionCode.PRECONDITION_VIOLATION, ex.getExceptionCode());
    }
    try {
      Assure.nonEmpty("param", new long[0]);
    } catch (Ant4EclipseException ex) {
      Assert.assertEquals(CoreExceptionCode.PRECONDITION_VIOLATION, ex.getExceptionCode());
    }
  }

  @Test
  public void resourceExist() throws IOException {

    File testFile = File.createTempFile("a4e-testExists", null);
    System.out.println("Using temp. testfile: " + testFile.getAbsolutePath());
    testFile.deleteOnExit();

    // positive check for a file
    Assure.exists("testFile", testFile);

    // positive check for a directory
    Assure.exists("testFile.getParentFile()", testFile.getParentFile());

    try {
      Assure.exists("NICHT_DA", new File("NICHT_DA"));
    } catch (Ant4EclipseException ex) {
      Assert.assertEquals(CoreExceptionCode.PRECONDITION_VIOLATION, ex.getExceptionCode());
    }

  }

  @Test
  public void isFile() throws IOException {

    File testFile = File.createTempFile("a4e-testIsFile", null);
    File nonexistingDir = new File(testFile.getParentFile(), testFile.getName() + ".dir");
    System.out.println("Using temp. testfile: " + testFile.getAbsolutePath());
    testFile.deleteOnExit();

    Assure.isFile(testFile);

    try {
      Assure.isFile(testFile.getParentFile());
    } catch (Ant4EclipseException ex) {
      Assert.assertEquals(CoreExceptionCode.PRECONDITION_VIOLATION, ex.getExceptionCode());
    }

    try {
      Assure.isFile(nonexistingDir);
    } catch (Ant4EclipseException ex) {
      Assert.assertEquals(CoreExceptionCode.PRECONDITION_VIOLATION, ex.getExceptionCode());
    }

    try {
      Assure.isFile(new File("NICHT_DA"));
    } catch (Ant4EclipseException ex) {
      Assert.assertEquals(CoreExceptionCode.PRECONDITION_VIOLATION, ex.getExceptionCode());
    }

  }

  /**
   * @throws IOException
   * 
   */
  @Test
  public void isDirectory() throws IOException {

    File testFile = File.createTempFile("a4e-testIsDirectory", null);
    testFile.deleteOnExit();
    System.out.println("Using temp. testfile: " + testFile.getAbsolutePath());

    Assure.isDirectory(testFile.getParentFile());

    try {
      Assure.isDirectory(testFile);
    } catch (Ant4EclipseException ex) {
      Assert.assertEquals(CoreExceptionCode.PRECONDITION_VIOLATION, ex.getExceptionCode());
    }

    try {
      Assure.isDirectory(new File("NICHT_DA"));
    } catch (Ant4EclipseException ex) {
      Assert.assertEquals(CoreExceptionCode.PRECONDITION_VIOLATION, ex.getExceptionCode());
    }

  }

  @Test
  public void assertTrue() {
    Assure.assertTrue(true, "true");
    try {
      Assure.assertTrue(false, "false");
    } catch (Ant4EclipseException ex) {
      Assert.assertEquals(CoreExceptionCode.PRECONDITION_VIOLATION, ex.getExceptionCode());
    }
  }

  /**
   * 
   */
  @Test
  public void inRange() {

    Assure.inRange(5, 1, 10);
    Assure.inRange(1, 1, 10);
    Assure.inRange(10, 1, 10);

    try {
      Assure.inRange(0, 1, 10);
    } catch (Ant4EclipseException ex) {
      Assert.assertEquals(CoreExceptionCode.PRECONDITION_VIOLATION, ex.getExceptionCode());
    }

    try {
      Assure.inRange(11, 1, 10);
    } catch (Ant4EclipseException ex) {
      Assert.assertEquals(CoreExceptionCode.PRECONDITION_VIOLATION, ex.getExceptionCode());
    }
  }

} /* ENDCLASS */