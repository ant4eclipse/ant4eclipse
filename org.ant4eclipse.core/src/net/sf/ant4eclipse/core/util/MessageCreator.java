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
package net.sf.ant4eclipse.core.util;

import java.util.StringTokenizer;

/**
 * MessageCreator --
 * 
 * @author Daniel Kasmeroglu (daniel.kasmeroglu@kasisoft.net)
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 */
public class MessageCreator {

  /**
   * Creates a message while using some formatted text.
   * 
   * @param msg
   *          The formatted message.
   * @param args
   *          The arguments used for the formatting.
   * 
   * @return A formatted message.
   */
  public static String createMessage(final String msg, final Object[] args) {
    return (rawDoFormat(msg, args));
  }

  /**
   * Creates a message while using a causing Throwable instance.
   * 
   * @param msg
   *          Some informational text.
   * @param t
   *          The Throwable instance which caused this message.
   * 
   * @return A corresponding error message.
   */
  public static String createMessage(final String msg, final Object t) {
    /**
     * @todo [12-Feb-2006:KASI] Support stacktraces within the formatting sequence ?
     */
    return (rawDoFormat(msg, new Object[] { t }));
  }

  /**
   * Formats a string using a set of arguments.
   * 
   * @param msg
   *          The message with formatting codes. %s = string, %d = decimal notation, %x = hexadecimal notation, %% = %
   * @param args
   *          The arguments used for the formatted message.
   * 
   * @return A formatted string.
   */
  private static String rawDoFormat(final String msg, final Object[] args) {

    final StringBuffer receiver = new StringBuffer();
    int offset = 0;
    final int idx = msg.indexOf('%');
    if ((args == null) || (offset >= args.length) || (idx == -1) || (idx == (msg.length() - 1))) {
      receiver.append(msg.replaceAll("%%", "%"));
      return (receiver.toString());
    }

    final StringTokenizer tokenizer = new StringTokenizer(msg, "%", true);
    boolean formattingcode = false;
    while (tokenizer.hasMoreTokens() && (offset < args.length)) {
      final String token = tokenizer.nextToken();
      final char[] chars = token.toCharArray();
      if (formattingcode) {
        char fillchar = ' ';
        int i = 0;
        int length = 0;
        // check for a '%%' sequence
        if ((i < chars.length) && (chars[i] == '%')) {
          receiver.append('%');
          formattingcode = false;
          continue;
        }
        // shall we fill the field with zeroes
        if ((i < chars.length) && (chars[i] == '0')) {
          fillchar = '0';
          i++;
        }
        // check for the length of the field (1..9)
        if ((i < chars.length) && Character.isDigit(chars[i])) {
          length = chars[i] - '0';
          i++;
        }
        // are we using a valid formatting character
        if ((i < chars.length) && ("sdx".indexOf(chars[i]) != -1)) {
          // a string doesn't require any kind of formatting
          if (chars[i] == 's') {
            receiver.append(String.valueOf(args[offset]));
            receiver.append(chars, i + 1, chars.length - i - 1);
            formattingcode = false;
            offset++;
            continue;
          }
          // we need to handle a decimal value
          final String str = getString(args[offset], chars[i] == 'x');
          if (str != null) {
            // append fill characters
            length = length - str.length();
            while (length > 0) {
              receiver.append(fillchar);
              length--;
            }
            receiver.append(str);
            receiver.append(chars, i + 1, chars.length - i - 1);
            formattingcode = false;
            offset++;
            continue;
          }
        }
        // couldn't handle formatting sequence, so dump it
        receiver.append("%");
        receiver.append(token);
        formattingcode = false;
      } else if (chars[0] == '%') {
        formattingcode = true;
      } else {
        receiver.append(token);
      }
    }
    while (tokenizer.hasMoreTokens()) {
      receiver.append(tokenizer.nextToken());
    }
    return (receiver.toString());
  }

  /**
   * Returns a string representation of an argument.
   * 
   * @param obj
   *          The argument object.
   * @param hex
   *          true <=> Hexadecimal notation.
   * 
   * @return A string representation or null in case the argument could not be handled.
   */
  private static String getString(final Object obj, final boolean hex) {
    final int radix = hex ? 16 : 10;
    if (obj instanceof Integer) {
      return (Integer.toString(((Integer) obj).intValue(), radix));
    }
    if (obj instanceof Short) {
      return (Integer.toString(((Short) obj).shortValue(), radix));
    }
    if (obj instanceof Byte) {
      return (Integer.toString(((Byte) obj).byteValue(), radix));
    }
    return (null);
  }
}
