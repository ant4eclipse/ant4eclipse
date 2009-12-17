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

import org.ant4eclipse.lib.core.Assure;
import org.ant4eclipse.lib.core.util.StringMap;
import org.ant4eclipse.lib.core.util.Utilities;

/**
 * @author Gerd Wuetherich (gerd@gerd-wuetherich.de)
 */
public class StringTemplate {

  /** - */
  private static final String OPEN  = "${";

  /** - */
  private static final String CLOSE = "}";

  /** - */
  private StringBuilder       _stringTemplate;

  /** - */
  private StringMap           _stringsToReplace;

  /**
   * 
   */
  public StringTemplate() {
    this._stringTemplate = new StringBuilder();
    this._stringsToReplace = new StringMap();
  }

  /**
   * 
   */
  public StringTemplate(String content) {
    this();
    Assure.notNull(content);
    this._stringTemplate.append(content);
  }

  /**
   * @param content
   */
  public StringTemplate append(String content) {
    Assure.notNull(content);
    this._stringTemplate.append(content);
    return this;
  }

  /**
   * Adds a NewLine to the Template
   * 
   * @return
   */
  public StringTemplate nl() {
    this._stringTemplate.append(Utilities.NL);
    return this;
  }

  /**
   * @param name
   * @param value
   */
  public void replace(String name, String value) {
    Assure.notNull(name);
    Assure.notNull(value);
    this._stringsToReplace.put(name, value);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String toString() {
    StringBuffer buffer = new StringBuffer(this._stringTemplate.toString());
    int index = buffer.indexOf(OPEN);
    while (index != -1) {
      int close = buffer.indexOf(CLOSE, index);
      if (close == -1) {
        // no close, so continue with the next candidate
        index = buffer.indexOf(OPEN, index + OPEN.length());
        continue;
      }
      String key = buffer.substring(index + OPEN.length(), close);
      String value = this._stringsToReplace.get(key);
      if (value != null) {
        // remove the existing content
        buffer.delete(index, close + CLOSE.length());
        buffer.insert(index, value);
        // this is advisable as the value might contain the key, so we're preventing a loop here
        index += value.length();
      } else {
        // no replacement value found, so go on with the next candidate
        index = close + CLOSE.length();
      }
      index = buffer.indexOf(OPEN, index);
    }
    return buffer.toString();
  }

} /* ENDCLASS */
