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
package net.sf.ant4eclipse.core;

import java.io.File;

import net.sf.ant4eclipse.core.logging.A4ELogging;
import net.sf.ant4eclipse.core.util.MessageCreator;

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
  public static void notNull(final Object object) {
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
  public static void notNull(final String message, final Object object) {
    if (object == null) {
      A4ELogging.debug(message);
      throw new RuntimeException("Precondition violated: " + message);
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
  public static void nonEmpty(final String string) {
    notNull(string);
    if (string.length() == 0) {
      final String msg = "Precondition violated: An empty string is not allowed here !";
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
  public static void exists(final File file) {
    notNull(file);
    if (!file.exists()) {
      final String msg = MessageCreator
          .createMessage("Precondition violated: %s has to exist!", file.getAbsolutePath());
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
  public static void isFile(final File file) {
    Assert.exists(file);
    if (!file.isFile()) {
      final String msg = MessageCreator.createMessage("Precondition violated: %s has to be a file, not a directory!",
          file.getAbsolutePath());
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
  public static void isDirectory(final File file) {
    Assert.exists(file);
    if (!file.isDirectory()) {
      final String msg = MessageCreator.createMessage("Precondition violated: %s has to be a directory, not a file!",
          file.getAbsolutePath());
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
  public static void assertTrue(final boolean condition, final String msg) {
    if (!condition) {
      final String errmsg = MessageCreator.createMessage("Precondition violated: %s", msg);
      // A4ELogging.debug(errmsg);
      throw new RuntimeException(errmsg);
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
  public static void inRange(final int value, final int from, final int to) {
    if ((value < from) || (value > to)) {
      final String msg = MessageCreator.createMessage("Precondition violated: %d must be within the range %d..%d !",
          new Object[] { new Integer(value), new Integer(from), new Integer(to) });
      A4ELogging.debug(msg);
      throw new RuntimeException(msg);
    }
  }

} /* ENDCLASS */