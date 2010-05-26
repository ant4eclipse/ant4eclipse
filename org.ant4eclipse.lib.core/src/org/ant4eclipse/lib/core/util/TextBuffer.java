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

  /** - */
  private StringBuffer        _buffer;

  /** - */
  private String              _indent;

  /** - */
  private StringBuffer        _indention;

  /** - */
  private String              _nl;

  /**
   * Initialises this buffer used for the text generation process.
   * 
   * @param indent
   *          The indention to be used. If <code>null<code> a default of two spaces is used.
   * @param newline
   *          The line separator to be used. If <code>null</code> the linefeed is used.
   */
  public TextBuffer(String indent, String newline) {
    this._nl = newline;
    this._buffer = new StringBuffer();
    this._indention = new StringBuffer();
    this._indent = indent;
    if (this._indent == null) {
      this._indent = DEFAULT_INDENT;
    }
    if (this._nl == null) {
      this._nl = "\n";
    }
  }

  /**
   * Increases the current indention.
   */
  public void indent() {
    this._indention.append(this._indent);
  }

  /**
   * Decreases the current indention.
   */
  public void dedent() {
    if (this._indention.length() >= this._indent.length()) {
      this._indention.setLength(this._indention.length() - this._indent.length());
    }
  }

  /**
   * Writes the supplied line causing to emit a full line.
   * 
   * @param line
   *          The text which needs to be dumped. Not <code>null</code>.
   */
  public void writeLine(String line) {
    this._buffer.append(this._indention);
    this._buffer.append(line);
    this._buffer.append(this._nl);
  }

  /**
   * Writes the supplied line causing to emit a full line.
   * 
   * @param fmt
   *          The formatting string. Not <code>null</code>.
   * @param args
   *          The arguments used for the formatting string.
   */
  public void writeLineF(String fmt, Object... args) {
    this._buffer.append(this._indention);
    this._buffer.append(String.format(fmt, args));
    this._buffer.append(this._nl);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String toString() {
    return this._buffer.toString();
  }

} /* ENDCLASS */
