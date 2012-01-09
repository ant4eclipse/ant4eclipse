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
package org.ant4eclipse.lib.jdt.ecj.internal.tools;

import org.ant4eclipse.lib.core.Assure;
import org.ant4eclipse.lib.jdt.ecj.SourceFile;

import java.io.File;

/**
 * <p>
 * Describes a source file that should be compiled.
 * </p>
 * 
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 */
public class SourceFileImpl implements SourceFile {

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
   * Creates a new instance of type {@link SourceFileImpl}.
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
  public SourceFileImpl( File sourceFolder, String sourceFileName, File destinationFolder, String encoding ) {
    Assure.isDirectory( "sourceFolder", sourceFolder );
    Assure.nonEmpty( "sourceFileName", sourceFileName );
    Assure.isDirectory( "destinationFolder", destinationFolder );
    Assure.nonEmpty( "encoding", encoding );
    _destinationFolder = destinationFolder;
    _encoding = encoding;
    _sourceFileName = sourceFileName;
    _sourceFolder = sourceFolder;
  }

  /**
   * <p>
   * Creates a new instance of type {@link SourceFileImpl} with the default encoding.
   * </p>
   * 
   * @param sourceFolder
   *          the folder that contains the source file
   * @param sourceFileName
   *          the name of the source file
   * @param destinationFolder
   *          the destination folder
   */
  public SourceFileImpl( File sourceFolder, String sourceFileName, File destinationFolder ) {
    this( sourceFolder, sourceFileName, destinationFolder, System.getProperty( FILE_ENCODING_SYSTEM_PROPERTY ) );
  }

  /**
   * <p>
   * Creates a new instance of type {@link SourceFileImpl}.
   * </p>
   * 
   * @param sourceFolder
   * @param sourceFileName
   */
  protected SourceFileImpl( File sourceFolder, String sourceFileName ) {
    Assure.isDirectory( "sourceFolder", sourceFolder );
    Assure.nonEmpty( "sourceFileName", sourceFileName );
    _sourceFileName = sourceFileName;
    _sourceFolder = sourceFolder;
    _encoding = System.getProperty( FILE_ENCODING_SYSTEM_PROPERTY );
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public File getSourceFolder() {
    return _sourceFolder;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getSourceFileName() {
    return _sourceFileName;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public File getSourceFile() {
    return new File( _sourceFolder, _sourceFileName );
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public File getDestinationFolder() {
    return _destinationFolder;
  }

  /**
   * <p>
   * Returns <code>true</code> if a destination folder is set, <code>false</code> otherwise.
   * </p>
   * 
   * @return <code>true</code> if a destination folder is set, <code>false</code> otherwise.
   */
  public boolean hasDestinationFolder() {
    return _destinationFolder != null;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getEncoding() {
    return _encoding;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String toString() {
    return String.format(
      "[SourceFile: _sourceFolder: %s _sourceFileName: %s _destinationFolder: %s _encoding: %s]",
      _sourceFolder, _sourceFileName, _destinationFolder, _encoding
    );
  }

} /* ENDCLASS */
