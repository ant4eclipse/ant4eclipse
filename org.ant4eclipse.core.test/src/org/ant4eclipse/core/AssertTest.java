package org.ant4eclipse.core;

import static org.junit.Assert.assertEquals;

import org.ant4eclipse.testframework.ConfigurableAnt4EclipseTestCase;
import org.junit.Test;

import javax.swing.JFrame;

import java.io.File;
import java.io.IOException;

/**
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 */
public class AssertTest extends ConfigurableAnt4EclipseTestCase {

  /**
   * 
   */
  @Test
  public void testAssertNotNull() {

    Assert.notNull(new Object());

    try {
      Assert.notNull(null);
    } catch (RuntimeException e) {
      assertEquals("Precondition violated: Object has to be set!", e.getMessage());
    }
  }

  @Test
  public void testInstanceOf() {
    Assert.instanceOf("parameter", "", String.class);

    try {
      Assert.instanceOf("parameter", new Object(), String.class);
    } catch (RuntimeException e) {
      assertEquals(
          "Precondition violated: Parameter 'parameter' should be of type 'java.lang.String' but is a 'java.lang.Object'",
          e.getMessage());
    }

    try {
      Assert.instanceOf("parameter", null, JFrame.class);
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

    Assert.nonEmpty("test");

    try {
      Assert.nonEmpty("");
    } catch (RuntimeException e) {
      assertEquals("Precondition violated: An empty string is not allowed here !", e.getMessage());
    }

    try {
      Assert.nonEmpty(null);
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

    Assert.exists(testFile);
    Assert.exists(testFile.getParentFile());

    try {
      Assert.exists(new File("NICHT_DA"));
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
      Assert.isFile(testFile.getParentFile());
    } catch (RuntimeException e) {
      assertEquals("Precondition violated: " + testFile.getParentFile().getAbsolutePath()
          + " has to be a file, not a directory!", e.getMessage());
    }

    try {
      Assert.isFile(new File("NICHT_DA"));
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

    Assert.isDirectory(testFile.getParentFile());

    try {
      Assert.isDirectory(testFile);
    } catch (RuntimeException e) {
      assertEquals("Precondition violated: " + testFile.getAbsolutePath() + " has to be a directory, not a file!", e
          .getMessage());
    }

    try {
      Assert.isDirectory(new File("NICHT_DA"));
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
    Assert.assertTrue(true, "true");

    try {
      Assert.assertTrue(false, "false");
    } catch (RuntimeException e) {
      assertEquals("Precondition violated: false", e.getMessage());
    }
  }

  /**
   * 
   */
  @Test
  public void testInRange() {
    Assert.inRange(5, 1, 10);

    Assert.inRange(1, 1, 10);

    Assert.inRange(10, 1, 10);

    try {
      Assert.inRange(0, 1, 10);
    } catch (RuntimeException e) {
      assertEquals("Precondition violated: 0 must be within the range 1..10 !", e.getMessage());
    }

    try {
      Assert.inRange(11, 1, 10);
    } catch (RuntimeException e) {
      assertEquals("Precondition violated: 11 must be within the range 1..10 !", e.getMessage());
    }
  }
}