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
package org.ant4eclipse.core.ant;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Location;

/**
 * Just a descendant of the original BuildException providing some convenience functionalities.
 * 
 * @author Daniel Kasmeroglu (Daniel.Kasmeroglu@Kasisoft.net)
 */
public class ExtendedBuildException extends BuildException {

  /** - */
  private static final long serialVersionUID = -7175682940553485951L;

  /**
   * Constructs a build exception with no descriptive information.
   */
  public ExtendedBuildException() {
    super();
  }

  /**
   * Constructs an exception with the given descriptive message.
   * 
   * @param message
   *          A description of or information about the exception. Should not be <code>null</code>.
   * @param args
   *          Arguments that will be used to format the message.
   */
  public ExtendedBuildException(String message, Object... args) {
    super(String.format(message, args));
  }

  /**
   * Constructs an exception with the given message and exception as a root cause.
   * 
   * @param message
   *          A description of or information about the exception. Should not be <code>null</code> unless a cause is
   *          specified.
   * @param cause
   *          The exception that might have caused this one. May be <code>null</code>.
   * @param args
   *          Arguments that will be used to format the message.
   */
  public ExtendedBuildException(String message, Throwable cause, Object... args) {
    super(String.format(message, args), cause);
  }

  /**
   * Constructs an exception with the given message and exception as a root cause and a location in a file.
   * 
   * @param cause
   *          The exception that might have caused this one. May be <code>null</code>.
   * @param location
   *          The location in the project file where the error occurred. Must not be <code>null</code>.
   * @param msg
   *          A description of or information about the exception. Should not be <code>null</code> unless a cause is
   *          specified.
   * @param args
   *          Arguments that will be used to format the message.
   */
  public ExtendedBuildException(String msg, Throwable cause, Location location, Object... args) {
    super(String.format(msg, args), cause, location);
  }

  /**
   * Constructs an exception with the given exception as a root cause.
   * 
   * @param cause
   *          The exception that might have caused this one. Should not be <code>null</code>.
   */
  public ExtendedBuildException(Throwable cause) {
    super(cause);
  }

  /**
   * Constructs an exception with the given descriptive message and a location in a file.
   * 
   * @param message
   *          A description of or information about the exception. Should not be <code>null</code>.
   * @param location
   *          The location in the project file where the error occurred. Must not be <code>null</code>.
   * @param args
   *          the message arguments
   */
  public ExtendedBuildException(String message, Location location, Object... args) {
    super(String.format(message, args), location);
  }

  /**
   * Constructs an exception with the given exception as a root cause and a location in a file.
   * 
   * @param cause
   *          The exception that might have caused this one. Should not be <code>null</code>.
   * @param location
   *          The location in the project file where the error occurred. Must not be <code>null</code>.
   */
  public ExtendedBuildException(Throwable cause, Location location) {
    super(cause, location);
  }

} /* ENDCLASS */
