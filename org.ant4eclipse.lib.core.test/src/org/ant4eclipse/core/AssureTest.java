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
package org.ant4eclipse.core;

import static org.junit.Assert.assertEquals;

import org.ant4eclipse.lib.core.Assure;
import org.ant4eclipse.lib.core.exception.Ant4EclipseException;
import org.junit.Test;

import javax.swing.JFrame;

import java.io.File;
import java.io.IOException;

/**
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 */
public class AssureTest {

  /**
   * 
   */
  @Test
  public void testAssertNotNull() {
    Assure.notNull(new Object());
    try {
      Assure.notNull(null);
    } catch (Ant4EclipseException ex) {
      assertEquals("Precondition violated: Object has to be set!", ex.getMessage());
    }
  }

  @Test
  public void testInstanceOf() {
    Assure.instanceOf("parameter", "", String.class);

    try {
      Assure.instanceOf("parameter", new Object(), String.class);
    } catch (RuntimeException e) {
      assertEquals(
          "Precondition violated: Parameter 'parameter' should be of type 'java.lang.String' but is a 'java.lang.Object'",
          e.getMessage());
    }

    try {
      Assure.instanceOf("parameter", null, JFrame.class);
    } catch (RuntimeException e) {
      assertEquals("Precondition violated: Parameter 'parameter' should be of type 'javax.swing.JFrame' but was null",
          e.getMessage());
    }
  }

  /**
   * 
   */
  @Test
  public void testAssertNonEmpty() {

    Assure.nonEmpty("test");

    try {
      Assure.nonEmpty("");
    } catch (RuntimeException e) {
      assertEquals("Precondition violated: An empty string is not allowed here !", e.getMessage());
    }

    try {
      Assure.nonEmpty(null);
    } catch (RuntimeException e) {
      assertEquals("Precondition violated: Object has to be set!", e.getMessage());
    }
  }

  /**
   * @throws IOException
   * 
   */
  @Test
  public void testExist() throws IOException {

    File testFile = File.createTempFile("a4e-testExists", null);
    testFile.deleteOnExit();

    System.out.println("Using temp. testfile: " + testFile.getAbsolutePath());

    Assure.exists(testFile);
    Assure.exists(testFile.getParentFile());

    try {
      Assure.exists(new File("NICHT_DA"));
    } catch (RuntimeException e) {
      String userDir = System.getProperty("user.dir") + File.separator;
      assertEquals("Precondition violated: " + userDir + "NICHT_DA has to exist!", e.getMessage());
    }
  }

  /**
   * @throws IOException
   * 
   */
  @Test
  public void testIsFile() throws IOException {

    File testFile = File.createTempFile("a4e-testIsFile", null);
    testFile.deleteOnExit();
    System.out.println("Using temp. testfile: " + testFile.getAbsolutePath());

    try {
      Assure.isFile(testFile.getParentFile());
    } catch (RuntimeException e) {
      assertEquals("Precondition violated: " + testFile.getParentFile().getAbsolutePath()
          + " has to be a file, not a directory!", e.getMessage());
    }

    try {
      Assure.isFile(new File("NICHT_DA"));
    } catch (RuntimeException e) {
      String userDir = System.getProperty("user.dir") + File.separator;
      assertEquals("Precondition violated: " + userDir + "NICHT_DA has to exist!", e.getMessage());
    }
  }

  /**
   * @throws IOException
   * 
   */
  @Test
  public void testIsDirectory() throws IOException {

    File testFile = File.createTempFile("a4e-testIsDirectory", null);
    testFile.deleteOnExit();
    System.out.println("Using temp. testfile: " + testFile.getAbsolutePath());

    Assure.isDirectory(testFile.getParentFile());

    try {
      Assure.isDirectory(testFile);
    } catch (RuntimeException e) {
      assertEquals("Precondition violated: " + testFile.getAbsolutePath() + " has to be a directory, not a file!", e
          .getMessage());
    }

    try {
      Assure.isDirectory(new File("NICHT_DA"));
    } catch (RuntimeException e) {
      String userDir = System.getProperty("user.dir") + File.separator;
      assertEquals("Precondition violated: " + userDir + "NICHT_DA has to exist!", e.getMessage());
    }
  }

  /**
   * 
   */
  @Test
  public void testAssertTrue() {
    Assure.assertTrue(true, "true");

    try {
      Assure.assertTrue(false, "false");
    } catch (RuntimeException e) {
      assertEquals("Precondition violated: false", e.getMessage());
    }
  }

  /**
   * 
   */
  @Test
  public void testInRange() {
    Assure.inRange(5, 1, 10);

    Assure.inRange(1, 1, 10);

    Assure.inRange(10, 1, 10);

    try {
      Assure.inRange(0, 1, 10);
    } catch (RuntimeException e) {
      assertEquals("Precondition violated: 0 must be within the range 1..10 !", e.getMessage());
    }

    try {
      Assure.inRange(11, 1, 10);
    } catch (RuntimeException e) {
      assertEquals("Precondition violated: 11 must be within the range 1..10 !", e.getMessage());
    }
  }
}