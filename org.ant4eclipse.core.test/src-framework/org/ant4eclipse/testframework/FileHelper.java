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
import org.ant4eclipse.lib.core.util.Utilities;

import java.io.File;
import java.io.IOException;

public class FileHelper {

  public static final void createDirectory(File directory) {
    Assure.notNull(directory);

    if (directory.isFile()) {
      throw new RuntimeException("Directory '" + directory + "' is a file");
    }
    if (directory.isDirectory()) {
      throw new RuntimeException("Directory '" + directory + "' already exists");
    }

    if (!directory.mkdirs()) {
      throw new RuntimeException("Directory '" + directory + "' could not be created for an unkown reason");
    }
  }

  public static final void createFile(File file) {
    Assure.notNull(file);

    try {
      if (!file.exists()) {
        if (!file.createNewFile()) {
          throw new RuntimeException("Could not create file: " + file);
        }
      }
    } catch (IOException e) {
      throw new RuntimeException(e.getMessage(), e);
    }
  }

  /**
   * Returns the content of the resource from the classpath
   * 
   * @param resourceName
   *          the name of the resource that should be loaded from the classpath
   * @return the content of the resource
   * @throws IOException
   *           if the file not exits or if an I/O error occurs.
   */
  public static final String getResource(String resourceName) {
    StringBuffer buffer = Utilities.readTextContent("/" + resourceName, Utilities.ENCODING, true);
    return buffer.toString();
  }

}
