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

/**
 * <p>
 * The {@link Ant4EclipseException} allows you to set a a exception message with several arguments.
 * </p>
 * 
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 */
public class Ant4EclipseException extends RuntimeException {

  /** serialVersionUID */
  private static final long serialVersionUID = -2322126644590371742L;

  /** - */
  private ExceptionCode     _exceptionCode;

  /** - */
  private Object[]          _args;

  /**
   * <p>
   * Creates a new instance of type Ant4EclipseException.
   * </p>
   * 
   * @param cause
   * @param exceptionCode
   * @param args
   */
  public Ant4EclipseException(Throwable cause, ExceptionCode exceptionCode, Object... args) {
    super(cause);

    this._exceptionCode = exceptionCode;
    this._args = args;

  }

  /**
   * @param exceptionCode
   * @param args
   */
  public Ant4EclipseException(ExceptionCode exceptionCode, Object... args) {
    super();

    this._exceptionCode = exceptionCode;
    this._args = args != null ? args : new Object[0];
  }

  /**
   * <p>
   * </p>
   * 
   * @return
   */
  public ExceptionCode getExceptionCode() {
    return this._exceptionCode;
  }

  /**
   * <p>
   * </p>
   * 
   * @return
   */
  public Object[] getArgs() {
    return this._args;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getMessage() {
    return String.format(this._exceptionCode.getMessage(), this._args);
  }
}
