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
 * An ExceptionCode is a simple datastructure used to identify an error state.
 * </p>
 */
public class ExceptionCode {

  private String _message;

  /**
   * <p>
   * Initialises this failure code with a specific message.
   * </p>
   * 
   * @param message
   *          The message used for the user presentation. Not <code>null</code>.
   */
  protected ExceptionCode(String message) {
    this._message = message;
  }

  /**
   * <p>
   * Returns the message used for the user presentation. Not <code>null</code>.
   * </p>
   * 
   * @return The message used for the user presentation. Not <code>null</code>.
   */
  public String getMessage() {
    return this._message;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String toString() {
    StringBuffer buffer = new StringBuffer();
    buffer.append("[ExceptionCode:");
    buffer.append(" _message: ");
    buffer.append(this._message);
    buffer.append("]");
    return buffer.toString();
  }

} /* ENDCLASS */
