package org.ant4eclipse.lib.jdt.ecj.internal.tools;

import org.ant4eclipse.core.Assert;

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
  public SourceFileImpl(File sourceFolder, String sourceFileName, File destinationFolder, String encoding) {
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
  public SourceFileImpl(File sourceFolder, String sourceFileName, File destinationFolder) {
    this(sourceFolder, sourceFileName, destinationFolder, System.getProperty(FILE_ENCODING_SYSTEM_PROPERTY));
  }

  /**
   * <p>
   * Creates a new instance of type {@link SourceFileImpl}.
   * </p>
   * 
   * @param sourceFolder
   * @param sourceFileName
   */
  protected SourceFileImpl(File sourceFolder, String sourceFileName) {
    Assert.isDirectory(sourceFolder);
    Assert.nonEmpty(sourceFileName);

    this._sourceFileName = sourceFileName;
    this._sourceFolder = sourceFolder;
    this._encoding = System.getProperty(FILE_ENCODING_SYSTEM_PROPERTY);
  }

  /**
   * {@inheritDoc}
   */
  public File getSourceFolder() {
    return this._sourceFolder;
  }

  /**
   * {@inheritDoc}
   */
  public String getSourceFileName() {
    return this._sourceFileName;
  }

  /**
   * {@inheritDoc}
   */
  public File getSourceFile() {
    return new File(this._sourceFolder, this._sourceFileName);
  }

  /**
   * {@inheritDoc}
   */
  public File getDestinationFolder() {
    return this._destinationFolder;
  }

  /**
   * <p>
   * Returns <code>true</code> if a destination folder is set, <code>false</code> otherwise.
   * </p>
   * 
   * @return <code>true</code> if a destination folder is set, <code>false</code> otherwise.
   */
  public boolean hasDestinationFolder() {
    return this._destinationFolder != null;
  }

  /**
   * {@inheritDoc}
   */
  public String getEncoding() {
    return this._encoding;
  }

  /**
   * {@inheritDoc}
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