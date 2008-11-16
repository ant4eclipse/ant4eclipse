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
package net.sf.ant4eclipse.model.platform.resource.internal.factory;

/**
 * <p>
 * Thrown to indicate that an Exception has been occured while parsing a file.
 * </p>
 * 
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 * @deprecated
 */
public class FileParserException extends RuntimeException {

  /** serialVersionUID */
  private static final long serialVersionUID = 3256441425859326006L;

  /**
   * Creates a new instance of type FileParserException
   * 
   * @param message
   *          the detail message. The detail message is saved for later retrieval by the {@link #getMessage()} method.
   */
  public FileParserException(final String message) {
    super(message);
  }

  /**
   * Creates a new instance of type FileParserException
   * 
   * @param cause
   *          the cause (which is saved for later retrieval by the {@link #getCause()} method). (A <tt>null</tt> value
   *          is permitted, and indicates that the cause is nonexistent or unknown.)
   */
  public FileParserException(final Throwable cause) {
    super(cause);
  }

  /**
   * Creates a new instance of type FileParserException
   * 
   * @param message
   *          the detail message (which is saved for later retrieval by the {@link #getMessage()} method).
   * @param cause
   *          the cause (which is saved for later retrieval by the {@link #getCause()} method). (A <tt>null</tt> value
   *          is permitted, and indicates that the cause is nonexistent or unknown.)
   */
  public FileParserException(final String message, final Throwable cause) {
    super(message, cause);
  }
}