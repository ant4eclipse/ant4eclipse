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
package org.ant4eclipse.jdt.ecj;

import java.io.File;
import java.util.Map;

import org.ant4eclipse.core.Assert;

/**
 * <p>
 * A {@link CompileJobDescription} describes a compile job that can be executed with the eclipse java compiler.
 * </p>
 * 
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 */
public interface CompileJobDescription {

  /**
   * <p>
   * Returns the compiler options for the compile job.
   * </p>
   * 
   * @return the compiler options for the compile job.
   */
  public Map<String, String> getCompilerOptions();

  /**
   * <p>
   * Returns an array of directories that contains the source files that should be compiled.
   * </p>
   * 
   * @return an array of directories that contains the source files that should be compiled.
   */
  public SourceFile[] getSourceFiles();

  /**
   * <p>
   * Returns the {@link ClassFileLoader} that is responsible to load binary classes that are requested during the
   * compilation process.
   * </p>
   * 
   * @return the {@link ClassFileLoader} that is responsible to load binary classes that are requested during the
   *         compilation process.
   */
  public ClassFileLoader getClassFileLoader();

  /**
   * <p>
   * Describes a source file that should be compiled.
   * </p>
   * 
   * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
   */
  public static class SourceFile {

    /** the constant FILE_ENCODING_SYSTEM_PROPERTY */
    private static final String FILE_ENCODING_SYSTEM_PROPERTY = "file.encoding";

    /** the folder that contains the source file */
    private final File          _sourceFolder;

    /** the name of the source file */
    private final String        _sourceFileName;

    /** the destination folder */
    private final File          _destinationFolder;

    /** the file encoding */
    private final String        _encoding;

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
    public SourceFile(final File sourceFolder, final String sourceFileName, final File destinationFolder,
        final String encoding) {
      Assert.isDirectory(sourceFolder);
      Assert.nonEmpty(sourceFileName);
      Assert.isDirectory(destinationFolder);
      Assert.nonEmpty(encoding);

      this._destinationFolder = destinationFolder;
      this._encoding = encoding;
      this._sourceFileName = sourceFileName;
      this._sourceFolder = sourceFolder;
    }

    /**
     * <p>
     * Creates a new instance of type {@link SourceFile} with the default encoding.
     * </p>
     * 
     * @param sourceFolder
     *          the folder that contains the source file
     * @param sourceFileName
     *          the name of the source file
     * @param destinationFolder
     *          the destination folder
     */
    public SourceFile(final File sourceFolder, final String sourceFileName, final File destinationFolder) {
      this(sourceFolder, sourceFileName, destinationFolder, System.getProperty(FILE_ENCODING_SYSTEM_PROPERTY));
    }

    /**
     * <p>
     * Returns the source folder.
     * </p>
     * 
     * @return the source folder.
     */
    public File getSourceFolder() {
      return _sourceFolder;
    }

    /**
     * <p>
     * Returns the source file name.
     * </p>
     * 
     * @return the source file name.
     */
    public String getSourceFileName() {
      return _sourceFileName;
    }

    /**
     * <p>
     * Returns the source file.
     * </p>
     * 
     * @return the source file.
     */
    public File getSourceFile() {
      return new File(_sourceFolder, _sourceFileName);
    }

    /**
     * <p>
     * Returns the destination folder.
     * </p>
     * 
     * @return the destination folder.
     */
    public File getDestinationFolder() {
      return _destinationFolder;
    }

    /**
     * <p>
     * Returns the file encoding.
     * </p>
     * 
     * @return the file encoding.
     */
    public String getEncoding() {
      return _encoding;
    }

    /**
     * 
     */
    @Override
    public String toString() {
      final StringBuffer buffer = new StringBuffer();
      buffer.append("[SourceFile:");
      buffer.append(" _sourceFolder: ");
      buffer.append(_sourceFolder);
      buffer.append(" _sourceFileName: ");
      buffer.append(_sourceFileName);
      buffer.append(" _destinationFolder: ");
      buffer.append(_destinationFolder);
      buffer.append(" _encoding: ");
      buffer.append(_encoding);
      buffer.append("]");
      return buffer.toString();
    }

  }
}
