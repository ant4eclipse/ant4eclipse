package org.ant4eclipse.core.ant;

import java.io.File;

import org.ant4eclipse.core.Assert;
import org.ant4eclipse.core.util.Utilities;
import org.apache.tools.ant.types.FileList;
import org.apache.tools.ant.types.FileList.FileName;

/**
 * <p>
 * </p>
 * 
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 */
public class FileListHelper {

  /**
   * <p>
   * </p>
   * 
   * @param file
   * @return
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

  private FileListHelper() {
    super();
  }
}
