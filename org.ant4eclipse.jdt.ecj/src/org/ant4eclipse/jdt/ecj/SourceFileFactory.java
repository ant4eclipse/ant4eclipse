package org.ant4eclipse.jdt.ecj;

import org.ant4eclipse.jdt.ecj.internal.tools.SourceFileImpl;

import java.io.File;

public class SourceFileFactory {

  /**
   * <p>
   * Creates a new instance of type {@link SourceFile}.
   * </p>
   * 
   * @param sourceFolder
   *          the folder that contains the source file
   * @param sourceFileName
   *          the name of the source file
   * @param destinationFolder
   *          the destination folder
   * @param encoding
   *          the file encoding
   */
  public static SourceFile createSourceFile(File sourceFolder, String sourceFileName, File destinationFolder,
      String encoding) {

    return new SourceFileImpl(sourceFolder, sourceFileName, destinationFolder, encoding);
  }

  public static SourceFile createSourceFile(File sourceFolder, String sourceFileName, File destinationFolder) {

    return new SourceFileImpl(sourceFolder, sourceFileName, destinationFolder);
  }
}
