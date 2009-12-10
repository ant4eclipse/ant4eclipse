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

import org.ant4eclipse.core.logging.A4ELogging;

import java.io.File;

/**
 * <p>
 * Implements utility methods to support design-by-contract. If a condition is evaluated to false, a RuntimeException
 * will be thrown.
 * </p>
 * 
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 * @author Daniel Kasmeroglu (daniel.kasmeroglu@kasisoft.net)
 */
public class Assert {

  /**
   * <p>
   * Assert that the specified object is not null.
   * </p>
   * 
   * @param object
   *          the object that must be set.
   */
  public static void notNull(Object object) {
    notNull("Object has to be set!", object);
  }

  /**
   * <p>
   * Assert that the specified object is not null.
   * </p>
   * 
   * @param message
   *          an error message
   * @param object
   *          the object that must be set.
   */
  public static void notNull(String message, Object object) {
    if (object == null) {
      A4ELogging.debug(message);
      throw new RuntimeException("Precondition violated: " + message);
    }
  }

  /**
   * Asserts that the given parameter is an instance of the given type
   * 
   * @param parameterName
   *          The name of the parameter that is checked
   * @param parameter
   *          The actual parameter value
   * @param expectedType
   *          The type the parameter should be an instance of
   */
  public static void instanceOf(String parameterName, Object parameter, Class<?> expectedType) {
    if (parameter == null) {
      throw new RuntimeException("Precondition violated: Parameter '" + parameterName + "' should be of type '"
          + expectedType.getName() + "' but was null");
    }

    if (!expectedType.isInstance(parameter)) {
      throw new RuntimeException("Precondition violated: Parameter '" + parameterName + "' should be of type '"
          + expectedType.getName() + "' but is a '" + parameter.getClass().getName() + "'");
    }
  }

  /**
   * <p>
   * Assert that the supplied string provides a value or not.
   * </p>
   * 
   * @param string
   *          the string that must provide a value.
   */
  public static void nonEmpty(String string) {
    notNull(string);
    if (string.length() == 0) {
      String msg = "Precondition violated: An empty string is not allowed here !";
      A4ELogging.debug(msg);
      throw new RuntimeException(msg);
    }
  }

  /**
   * <p>
   * Assert that the specified file is not null and exists.
   * </p>
   * 
   * @param file
   *          the file that must exist.
   */
  public static void exists(File file) {
    notNull(file);
    if (!file.exists()) {
      String msg = String.format("Precondition violated: %s has to exist!", file.getAbsolutePath());
      A4ELogging.debug(msg);
      throw new RuntimeException(msg);
    }
  }

  /**
   * <p>
   * Assert that the specified file is not null, exists and is a file.
   * </p>
   * 
   * @param file
   *          the file that must be a file.
   */
  public static void isFile(File file) {
    Assert.exists(file);
    if (!file.isFile()) {
      String msg = String
          .format("Precondition violated: %s has to be a file, not a directory!", file.getAbsolutePath());
      A4ELogging.debug(msg);
      throw new RuntimeException(msg);
    }
  }

  /**
   * <p>
   * Assert that the specified file is not null, exists and is a directory.
   * </p>
   * 
   * @param file
   *          the file that must be a directory.
   */
  public static void isDirectory(File file) {
    Assert.exists(file);
    if (!file.isDirectory()) {
      String msg = String
          .format("Precondition violated: %s has to be a directory, not a file!", file.getAbsolutePath());
      A4ELogging.debug(msg);
      throw new RuntimeException(msg);
    }
  }

  /**
   * <p>
   * Assert that the given condition is <code>true</code>
   * </p>
   * 
   * @param condition
   *          the condition
   * @param msg
   *          the message
   */
  public static void assertTrue(boolean condition, String msg) {
    if (!condition) {
      // A4ELogging.debug(errmsg);
      throw new RuntimeException(String.format("Precondition violated: %s", msg));
    }
  }

  /**
   * <p>
   * Checks whether a value is in a specific range or not.
   * </p>
   * 
   * @param value
   *          the value that shall be tested.
   * @param from
   *          the lower bound inclusive.
   * @param to
   *          the upper bound inclusive.
   */
  public static void inRange(int value, int from, int to) {
    if ((value < from) || (value > to)) {
      String msg = String.format("Precondition violated: %d must be within the range %d..%d !", Integer.valueOf(value),
          Integer.valueOf(from), Integer.valueOf(to));
      A4ELogging.debug(msg);
      throw new RuntimeException(msg);
    }
  }

} /* ENDCLASS */