package org.ant4eclipse.core;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.util.Properties;


import org.ant4eclipse.core.Assert;
import org.ant4eclipse.core.logging.Ant4EclipseLogger;
import org.ant4eclipse.core.logging.DefaultAnt4EclipseLogger;
import org.ant4eclipse.core.service.ServiceRegistry;
import org.ant4eclipse.testframework.ServiceRegistryConfigurator;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 */
public class AssertTest {

  @BeforeClass
  public static void configureServiceRegistry() {
    Properties properties = new Properties();
    properties.put(Ant4EclipseLogger.class.getName(), DefaultAnt4EclipseLogger.class.getName());
    ServiceRegistryConfigurator.configureServiceRegistry(properties);
  }

  @AfterClass
  public static void disposeServiceRegistry() {
    ServiceRegistry.reset();
  }

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
   * 
   */
  @Test
  public void testExist() {
    Assert.exists(new File(".classpath"));
    Assert.exists(new File("resource"));

    try {
      Assert.exists(new File("NICHT_DA"));
    } catch (RuntimeException e) {
      String userDir = System.getProperty("user.dir") + File.separator;
      assertEquals("Precondition violated: " + userDir + "NICHT_DA has to exist!", e.getMessage());
    }
  }

  /**
   * 
   */
  @Test
  public void testIsFile() {

    Assert.isFile(new File(".classpath"));

    try {
      Assert.isFile(new File("resource"));
    } catch (RuntimeException e) {
      String userDir = System.getProperty("user.dir") + File.separator;
      assertEquals("Precondition violated: " + userDir + "resource has to be a file, not a directory!", e.getMessage());
    }

    try {
      Assert.isFile(new File("NICHT_DA"));
    } catch (RuntimeException e) {
      String userDir = System.getProperty("user.dir") + File.separator;
      assertEquals("Precondition violated: " + userDir + "NICHT_DA has to exist!", e.getMessage());
    }
  }

  /**
   * 
   */
  @Test
  public void testIsDirectory() {

    Assert.isDirectory(new File("resource"));

    try {
      Assert.isDirectory(new File(".classpath"));
    } catch (RuntimeException e) {
      String userDir = System.getProperty("user.dir") + File.separator;
      assertEquals("Precondition violated: " + userDir + ".classpath has to be a directory, not a file!", e
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