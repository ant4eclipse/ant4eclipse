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
package net.sf.ant4eclipse.ant.platform.internal.team;

/**
 * <p>
 * Thrown to indicate that an Exception has been occured whilst accessing the a version control repository.
 * </p>
 * 
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 * @deprecated
 */
public class VcsException extends RuntimeException {

  /** serialVersionUID */
  private static final long serialVersionUID = 3257844381221271088L;

  /**
   * Creates a new instance of type VcsException
   */
  public VcsException() {
    super();
  }

  /**
   * Creates a new instance of type VcsException
   * 
   * @param message
   *          the detail message. The detail message is saved for later retrieval by the {@link #getMessage()} method.
   */
  public VcsException(final String message) {
    super(message);
  }

  /**
   * Creates a new instance of type VcsException
   * 
   * @param cause
   *          the cause (which is saved for later retrieval by the {@link #getCause()} method). (A <tt>null</tt> value
   *          is permitted, and indicates that the cause is nonexistent or unknown.)
   */
  public VcsException(final Throwable cause) {
    super(cause);
  }

  /**
   * Creates a new instance of type VcsException
   * 
   * @param message
   *          the detail message. The detail message is saved for later retrieval by the {@link #getMessage()} method.
   * @param cause
   *          the cause (which is saved for later retrieval by the {@link #getCause()} method). (A <tt>null</tt> value
   *          is permitted, and indicates that the cause is nonexistent or unknown.)
   */
  public VcsException(final String message, final Throwable cause) {
    super(message, cause);
  }

}
