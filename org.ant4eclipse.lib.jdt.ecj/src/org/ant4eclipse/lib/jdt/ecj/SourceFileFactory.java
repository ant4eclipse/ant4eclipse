package org.ant4eclipse.lib.jdt.ecj;

import org.ant4eclipse.lib.jdt.ecj.internal.tools.SourceFileImpl;

import java.io.File;

/**
 * <p>
 * Factory to create {@link SourceFile SourceFiles}.
 * </p>
 * 
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 */
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
   * @return
   */
  public static SourceFile createSourceFile( File sourceFolder, String sourceFileName, File destinationFolder, String encoding ) {
    return new SourceFileImpl( sourceFolder, sourceFileName, destinationFolder, encoding );
  }

  /**
   * <p>
   * Creates a new instance of type {@link SourceFile} and the default encoding.
   * </p>
   * 
   * @param sourceFolder
   *          the folder that contains the source file
   * @param sourceFileName
   *          the name of the source file
   * @param destinationFolder
   *          the destination folder
   * @return
   */
  public static SourceFile createSourceFile( File sourceFolder, String sourceFileName, File destinationFolder ) {
    return new SourceFileImpl( sourceFolder, sourceFileName, destinationFolder );
  }
  
} /* ENDCLASS */
