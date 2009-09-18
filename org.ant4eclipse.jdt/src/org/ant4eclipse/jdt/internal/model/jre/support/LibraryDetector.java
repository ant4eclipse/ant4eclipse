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
package org.ant4eclipse.jdt.internal.model.jre.support;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Used to discover the boot path, extension directories, and endorsed directories for a Java VM.
 */
public class LibraryDetector {

  /**
   * Prints system properties to a file that must be specified in args[0].
   * <ul>
   * <li>java.version</li>
   * <li>sun.boot.class.path</li>
   * <li>java.ext.dirs</li>
   * <li>java.endorsed.dirs</li>
   * </ul>
   * 
   * @param args
   */
  public static void main(String[] args) {

    // create property string
    StringBuffer buffer = new StringBuffer();
    buffer.append(System.getProperty("java.version"));
    buffer.append("|");
    buffer.append(System.getProperty("sun.boot.class.path"));
    buffer.append("|");
    buffer.append(System.getProperty("java.ext.dirs"));
    buffer.append("|");
    buffer.append(System.getProperty("java.endorsed.dirs"));
    buffer.append("|");
    buffer.append(System.getProperty("java.specification.version"));
    buffer.append("|");
    buffer.append(System.getProperty("java.specification.name"));
    buffer.append("|");
    buffer.append(System.getProperty("java.vendor"));

    // write to file
    try {
      File outfile = new File(args[0]);
      outfile.createNewFile();
      BufferedWriter out = new BufferedWriter(new FileWriter(outfile));
      out.write(buffer.toString());
      out.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
    //
    // // exit
    // System.exit(0);
  }
}
