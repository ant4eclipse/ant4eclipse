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
package org.ant4eclipse.ant.core;

import org.ant4eclipse.core.Assert;
import org.ant4eclipse.core.util.Utilities;

import org.apache.tools.ant.types.FileList;
import org.apache.tools.ant.types.FileList.FileName;

import java.io.File;

/**
 * <p>
 * Helper class to create {@link FileList FileLists}.
 * </p>
 * 
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 */
public class FileListHelper {

  /**
   * <p>
   * Creates a {@link FileList} that contains all children of the specified file.
   * </p>
   * 
   * @param file
   *          the file
   * @return a {@link FileList} that contains all children of the specified file.
   */
  public static FileList getFileList(File file) {
    Assert.exists(file);

    File parentFile = file.getParentFile();

    FileList fileList = new FileList();
    fileList.setDir(parentFile);

    if (file.isFile()) {
      FileName fileName = new FileList.FileName();
      fileName.setName(file.getName());
      fileList.addConfiguredFile(fileName);
    }

    else if (file.isDirectory()) {
      for (File child : Utilities.getAllChildren(file)) {
        FileName fileName = new FileList.FileName();
        fileName.setName(child.getAbsolutePath().substring(parentFile.getAbsolutePath().length() + 1));
        fileList.addConfiguredFile(fileName);
      }
    }

    return fileList;
  }

  /**
   * <p>
   * Creates a new instance of type FileListHelper.
   * </p>
   */
  private FileListHelper() {
    super();
  }
}
