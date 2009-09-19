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
package org.ant4eclipse.jdt.ecj;

import org.ant4eclipse.core.Assert;

import java.io.File;

/**
 * <p>
 * Describes a source file that should be compiled.
 * </p>
 * 
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 */
public class SourceFile {

  /** the constant FILE_ENCODING_SYSTEM_PROPERTY */
  private static final String FILE_ENCODING_SYSTEM_PROPERTY = "file.encoding";

  /** the folder that contains the source file */
  private File                _sourceFolder;

  /** the name of the source file */
  private String              _sourceFileName;

  /** the destination folder */
  private File                _destinationFolder;

  /** the file encoding */
  private String              _encoding;

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
  public SourceFile(File sourceFolder, String sourceFileName, File destinationFolder, String encoding) {
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
  public SourceFile(File sourceFolder, String sourceFileName, File destinationFolder) {
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
    return this._sourceFolder;
  }

  /**
   * <p>
   * Returns the source file name.
   * </p>
   * 
   * @return the source file name.
   */
  public String getSourceFileName() {
    return this._sourceFileName;
  }

  /**
   * <p>
   * Returns the source file.
   * </p>
   * 
   * @return the source file.
   */
  public File getSourceFile() {
    return new File(this._sourceFolder, this._sourceFileName);
  }

  /**
   * <p>
   * Returns the destination folder.
   * </p>
   * 
   * @return the destination folder.
   */
  public File getDestinationFolder() {
    return this._destinationFolder;
  }

  /**
   * <p>
   * Returns the file encoding.
   * </p>
   * 
   * @return the file encoding.
   */
  public String getEncoding() {
    return this._encoding;
  }

  /**
   *
   */
  @Override
  public String toString() {
    StringBuffer buffer = new StringBuffer();
    buffer.append("[SourceFile:");
    buffer.append(" _sourceFolder: ");
    buffer.append(this._sourceFolder);
    buffer.append(" _sourceFileName: ");
    buffer.append(this._sourceFileName);
    buffer.append(" _destinationFolder: ");
    buffer.append(this._destinationFolder);
    buffer.append(" _encoding: ");
    buffer.append(this._encoding);
    buffer.append("]");
    return buffer.toString();
  }

}