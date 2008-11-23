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
package org.ant4eclipse.jdt.tools.ejc;

import java.io.File;
import java.util.Map;

import org.ant4eclipse.core.Assert;
import org.ant4eclipse.jdt.tools.ejc.loader.ClassFileLoader;

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
   * </p>
   * 
   * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
   */
  public static class SourceFile {

    /** the constant FILE_ENCODING_SYSTEM_PROPERTY */
    private static final String FILE_ENCODING_SYSTEM_PROPERTY = "file.encoding";

    private final File          _sourceFolder;

    private final String        _sourceFileName;

    private final File          _destinationFolder;

    private final String        _encoding;

    /**
     * <p>
     * </p>
     * 
     * @param sourceFolder
     * @param sourceFileName
     * @param destinationFolder
     * @param encoding
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
     * </p>
     * 
     * @param sourceFolder
     * @param sourceFileName
     * @param destinationFolder
     */
    public SourceFile(final File sourceFolder, final String sourceFileName, final File destinationFolder) {
      this(sourceFolder, sourceFileName, destinationFolder, System.getProperty(FILE_ENCODING_SYSTEM_PROPERTY));
    }

    /**
     * <p>
     * </p>
     * 
     * @return
     */
    public File getSourceFolder() {
      return _sourceFolder;
    }

    /**
     * <p>
     * </p>
     * 
     * @return
     */
    public String getSourceFileName() {
      return _sourceFileName;
    }

    public File getSourceFile() {
      return new File(_sourceFolder, _sourceFileName);
    }

    /**
     * <p>
     * </p>
     * 
     * @return
     */
    public File getDestinationFolder() {
      return _destinationFolder;
    }

    /**
     * <p>
     * </p>
     * 
     * @return
     */
    public String getEncoding() {
      return _encoding;
    }
  }
}
