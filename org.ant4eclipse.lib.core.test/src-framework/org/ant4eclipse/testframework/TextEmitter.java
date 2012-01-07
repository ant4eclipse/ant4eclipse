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
package org.ant4eclipse.testframework;

import org.ant4eclipse.lib.core.util.Utilities;

public class TextEmitter {

  private static final String INDENTION = "  ";

  private StringBuffer        _buffer;

  private StringBuffer        _indention;

  /**
   * Initialises this emitter for text.
   */
  public TextEmitter() {
    _buffer = new StringBuffer();
    _indention = new StringBuffer();
  }

  /**
   * Indents the current emitting process.
   */
  public void indent() {
    _indention.append( INDENTION );
  }

  /**
   * Dedents the current emitting process.
   */
  public void dedent() {
    int newlength = _indention.length() - INDENTION.length();
    if( newlength >= 0 ) {
      _indention.setLength( newlength );
    }
  }

  /**
   * Inserts a newline.
   */
  public void newline() {
    _buffer.append( Utilities.NL );
  }

  public void append( String format, Object ... args ) {
    _buffer.append( String.format( format, args ) );
  }

  public void appendln( String format, Object ... args ) {
    _buffer.append( _indention );
    _buffer.append( String.format( format, args ) );
    newline();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String toString() {
    return _buffer.toString();
  }

} /* ENDCLASS */
