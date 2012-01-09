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
package org.ant4eclipse.lib.core.util;

/**
 * <p>
 * Simple helper class used to generate textual output.
 * </p>
 * 
 * @author Daniel Kasmeroglu (Daniel.Kasmeroglu@Kasisoft.net)
 */
public class TextBuffer {

  private static final String DEFAULT_INDENT = "  ";

  private StringBuffer        _buffer;
  private String              _indent;
  private StringBuffer        _indention;
  private String              _nl;

  /**
   * Initialises this buffer used for the text generation process.
   * 
   * @param indent
   *          The indention to be used. If <code>null<code> a default of two spaces is used.
   * @param newline
   *          The line separator to be used. If <code>null</code> the linefeed is used.
   */
  public TextBuffer( String indent, String newline ) {
    _nl = newline;
    _buffer = new StringBuffer();
    _indention = new StringBuffer();
    _indent = indent;
    if( _indent == null ) {
      _indent = DEFAULT_INDENT;
    }
    if( _nl == null ) {
      _nl = "\n";
    }
  }

  /**
   * Increases the current indention.
   */
  public void indent() {
    _indention.append( _indent );
  }

  /**
   * Decreases the current indention.
   */
  public void dedent() {
    if( _indention.length() >= _indent.length() ) {
      _indention.setLength( _indention.length() - _indent.length() );
    }
  }

  /**
   * Writes the supplied line causing to emit a full line.
   * 
   * @param line
   *          The text which needs to be dumped. Not <code>null</code>.
   */
  public void writeLine( String line ) {
    _buffer.append( _indention );
    _buffer.append( line );
    _buffer.append( _nl );
  }

  /**
   * Writes the supplied line causing to emit a full line.
   * 
   * @param fmt
   *          The formatting string. Not <code>null</code>.
   * @param args
   *          The arguments used for the formatting string.
   */
  public void writeLineF( String fmt, Object ... args ) {
    _buffer.append( _indention );
    _buffer.append( String.format( fmt, args ) );
    _buffer.append( _nl );
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String toString() {
    return _buffer.toString();
  }

} /* ENDCLASS */
