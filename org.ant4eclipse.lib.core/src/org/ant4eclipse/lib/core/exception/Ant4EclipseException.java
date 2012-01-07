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
package org.ant4eclipse.lib.core.exception;

/**
 * <p>
 * The {@link Ant4EclipseException} allows you to set a a exception message with several arguments.
 * </p>
 * 
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 */
public class Ant4EclipseException extends RuntimeException {

  private static final Object[] NO_ARGS = new Object[0];

  private ExceptionCode         _exceptionCode;

  private Object[]              _args;

  /**
   * <p>
   * Creates a new instance of type Ant4EclipseException.
   * </p>
   * 
   * @param cause
   *          The exception indicating the original reason for a failure. Not <code>null</code>.
   * @param exceptionCode
   *          The exception code used to point to the kind of failure that happened. Not <code>null</code>.
   * @param args
   *          The arguments to be passed to the exception message.
   */
  public Ant4EclipseException( Throwable cause, ExceptionCode exceptionCode, Object ... args ) {
    super( cause );
    _exceptionCode = exceptionCode;
    _args = args != null ? args : NO_ARGS;
  }

  /**
   * <p>
   * Creates a new instance of type Ant4EclipseException.
   * </p>
   * 
   * @param exceptionCode
   *          The exception code used to point to the kind of failure that happened. Not <code>null</code>.
   * @param args
   *          The arguments to be passed to the exception message.
   */
  public Ant4EclipseException( ExceptionCode exceptionCode, Object ... args ) {
    super();
    _exceptionCode = exceptionCode;
    _args = args != null ? args : NO_ARGS;
  }

  /**
   * <p>
   * Returns the code that indicates the type of error.
   * </p>
   * 
   * @return The code that indicates the type of error. Not <code>null</code>.
   */
  public ExceptionCode getExceptionCode() {
    return _exceptionCode;
  }

  /**
   * <p>
   * Returns the arguments used for the formatted message in order to provide additional information.
   * </p>
   * 
   * @return The arguments used for the formatted message in order to provide additional information.
   */
  public Object[] getArgs() {
    return _args;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getMessage() {
    try {
      return String.format( _exceptionCode.getMessage(), _args );
    } catch( Exception ex ) {
      // this really shouldn't happen but it's possible that it could because a user might alter the formatting
      // string in a way not capable to handle the arguments. so the only thing we can do here is to make sure
      // that the crippled message will be brought to the developer.
      StringBuffer buffer = new StringBuffer();
      buffer.append( "internal error: formatting message was '" );
      buffer.append( _exceptionCode.getMessage() );
      buffer.append( "', arguments were: " );
      if( _args.length > 0 ) {
        buffer.append( String.valueOf( _args[0] ) );
        for( int i = 1; i < _args.length; i++ ) {
          buffer.append( "," );
          buffer.append( String.valueOf( _args[i] ) );
        }
      } else {
        buffer.append( "<none>" );
      }
      return buffer.toString();
    }
  }

} /* ENDCLASS */
