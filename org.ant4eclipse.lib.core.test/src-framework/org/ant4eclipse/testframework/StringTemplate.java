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


import org.ant4eclipse.lib.core.Assert;
import org.ant4eclipse.lib.core.util.StringMap;
import org.ant4eclipse.lib.core.util.Utilities;

import java.util.Iterator;

/**
 * @author Gerd Wuetherich (gerd@gerd-wuetherich.de)
 */
public class StringTemplate {

  /** - */
  private static String PREFIX  = "${";

  /** - */
  private static String POSTFIX = "}";

  /** - */
  private StringBuilder _stringTemplate;

  /** - */
  private StringMap     _stringsToReplace;

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

    Assert.notNull(content);

    this._stringTemplate.append(content);
  }

  /**
   * @param content
   */
  public StringTemplate append(String content) {
    Assert.notNull(content);

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
    Assert.notNull(name);
    Assert.notNull(value);
    this._stringsToReplace.put(name, value);
  }

  @Override
  public String toString() {

    String template = this._stringTemplate.toString();
    Iterator<String> iterator = this._stringsToReplace.keySet().iterator();

    while (iterator.hasNext()) {

      String name = iterator.next();
      String stringToReplace = PREFIX + name + POSTFIX;
      String value = this._stringsToReplace.get(name);

      int index = template.indexOf(stringToReplace);
      while (index != -1) {
        template = template.substring(0, index) + value + template.substring(index + stringToReplace.length());
        index = template.indexOf(stringToReplace);
      }
    }

    return template;
  }
}
