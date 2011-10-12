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
package org.ant4eclipse.lib.core.logging;

/**
 * An enum representing logging level.
 */
public enum A4ELevel {

  TRACE, DEBUG, INFO, WARN, ERROR;

  /**
   * Returns the logging level matching the given text, or defaultLevel if the text does not match any level.
   * 
   * @param text
   *          The text to parse.
   * @param defaultLevel
   *          The default level to return if the text does not match any level.
   * @return The logging level.
   */
  public static A4ELevel parse(String text, A4ELevel defaultLevel) {
    A4ELevel result = null;
    if (text != null) {
      result = valueOf(text.trim().toUpperCase());
    }
    return result != null ? result : defaultLevel;
  }
}
