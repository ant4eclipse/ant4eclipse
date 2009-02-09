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
package org.ant4eclipse.core.exception;

public class Ant4EclipseException extends RuntimeException {

  /** serialVersionUID */
  private static final long   serialVersionUID = -2322126644590371742L;

  private final ExceptionCode _exceptionCode;

  private final Object[]      _args;

  public Ant4EclipseException(final ExceptionCode exceptionCode, final Throwable cause, final Object... args) {
    super(cause);

    this._exceptionCode = exceptionCode;
    this._args = args;

  }

  /**
   * @param exceptionCode
   */
  public Ant4EclipseException(final ExceptionCode exceptionCode) {
    super();

    this._exceptionCode = exceptionCode;
    this._args = new Object[] {};
  }

  /**
   * @param exceptionCode
   * @param arg
   */
  public Ant4EclipseException(final ExceptionCode exceptionCode, final Object arg) {
    super();

    this._exceptionCode = exceptionCode;
    this._args = new Object[] { arg };
  }

  /**
   * @param exceptionCode
   * @param args
   */
  public Ant4EclipseException(final ExceptionCode exceptionCode, final Object... args) {
    super();

    this._exceptionCode = exceptionCode;
    this._args = args;
  }

  public ExceptionCode getExceptionCode() {
    return this._exceptionCode;
  }

  public Object[] getArgs() {
    return this._args;
  }

  @Override
  public String getMessage() {
    return String.format(this._exceptionCode.getMessage(), this._args);
  }

  // @Override
  // public String toString() {
  // final StringBuffer buffer = new StringBuffer();
  // buffer.append("[Ant4EclipseException:");
  // buffer.append(" exceptionCode: ");
  // buffer.append(this._exceptionCode);
  // buffer.append(" args: ");
  // buffer.append(" { ");
  // for (int i0 = 0; (this._args != null) && (i0 < this._args.length); i0++) {
  // buffer.append(" [" + i0 + "]: ");
  // buffer.append(this._args[i0]);
  // }
  // buffer.append(" } ");
  // buffer.append("]");
  // return buffer.toString();
  // }
}
