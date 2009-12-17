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

import java.io.File;
import java.io.IOException;

public class FileHelper {

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

}
