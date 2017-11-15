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
package org.ant4eclipse.lib.jdt.internal.model.jre.support;

/**
 * Used to discover the boot path, extension directories, and endorsed directories for a Java VM.
 */
public class LibraryDetector {

  /**
   * Prints system properties to std.out
   * <ul>
   * <li>java.version</li>
   * <li>sun.boot.class.path</li>
   * <li>java.ext.dirs</li>
   * <li>java.endorsed.dirs</li>
   * <li>java.specification.version</li>
   * <li>java.specification.name</li>
   * <li>java.vendor</li>
   * </ul>
   * 
   * @param args
   */
  public static void main(String[] args) {
    System.out.println(System.getProperty("java.version", ""));
    System.out.println(System.getProperty("sun.boot.class.path", ""));
    System.out.println(System.getProperty("java.ext.dirs", ""));
    System.out.println(System.getProperty("java.endorsed.dirs", ""));
    System.out.println(System.getProperty("java.specification.version", ""));
    System.out.println(System.getProperty("java.specification.name", ""));
    System.out.println(System.getProperty("java.vendor", ""));
  }
}
