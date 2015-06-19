/**********************************************************************
 * Copyright (c) 2005-2009 ant4eclipse project team.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Nils Hartmann, Daniel Kasmeroglu, Gerd Wuetherich, Christoph Läubrich
 **********************************************************************/
package org.ant4eclipse.ant.pde;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import org.ant4eclipse.ant.core.AbstractAnt4EclipseDataType;
import org.ant4eclipse.lib.core.Assure;
import org.ant4eclipse.lib.core.exception.Ant4EclipseException;
import org.ant4eclipse.lib.core.logging.A4ELogging;
import org.ant4eclipse.lib.core.service.ServiceRegistryAccess;
import org.ant4eclipse.lib.pde.PdeExceptionCode;
import org.ant4eclipse.lib.pde.tools.TargetPlatformDefinition;
import org.ant4eclipse.lib.pde.tools.TargetPlatformRegistry;
import org.apache.tools.ant.DirectoryScanner;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.types.DataType;
import org.apache.tools.ant.types.DirSet;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;

/**
 * <p>
 * Represents a definition of a target platform. A target platform contains one or more locations. Each location must
 * contain bundles in a sub-directory named 'plug-ins'.
 * </p>
 * 
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 */
public class TargetPlatformDefinitionDataType extends AbstractAnt4EclipseDataType {

  /** the target platform definition */
  private TargetPlatformDefinition _targetPlatformDefinition;

  /** the id of the target platform */
  private String                   _id;

  /**
   * The target file to parse for location entries
   */
  private File                     _targetFile;

  /**
   * <p>
   * Creates a new instance of type {@link TargetPlatformDefinitionDataType}.
   * </p>
   * 
   * @param project
   *          the ant project
   */
  public TargetPlatformDefinitionDataType(Project project) {
    super(project);

    // create a new TargetPlatformDefinition
    this._targetPlatformDefinition = new TargetPlatformDefinition();
  }

  /**
   * <p>
   * Sets the id of the target platform location.
   * </p>
   * 
   * @param id
   *          the id of the target platform location.
   */
  public void setId(String id) {
    if (isReference()) {
      throw tooManyAttributes();
    }

    this._id = id;
  }

  /**
   * @param targetFile
   *          the new value for _targetFile
   */
  public void setTargetFile(File targetFile) {
    this._targetFile = targetFile;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected void doValidate() {
    if (this._id == null || "".equals(this._id)) {
      throw new Ant4EclipseException(PdeExceptionCode.ANT_ATTRIBUTE_NOT_SET, "id");
    }
    // Parse target file...
    if (this._targetFile != null) {
      if (!this._targetFile.exists()) {
        throw new Ant4EclipseException(PdeExceptionCode.TARGET_FILE_NOT_FOUND, this._targetFile.toString());
      }
      try {
        XMLReader reader = XMLReaderFactory.createXMLReader();
        reader.setContentHandler(new TargetPlattformContentHandler(this._targetFile, this));
        FileInputStream in = new FileInputStream(this._targetFile);
        try {
          reader.parse(new InputSource(new InputStreamReader(in, "UTF-8")));
        } finally {
          in.close();
        }
      } catch (SAXException e) {
        Ant4EclipseException exeption = new Ant4EclipseException(PdeExceptionCode.TARGET_PARSING_FAILED,
            this._targetFile.toString(), e.toString());
        exeption.initCause(e);
        throw exeption;
      } catch (IOException e) {
        Ant4EclipseException exeption = new Ant4EclipseException(PdeExceptionCode.TARGET_PARSING_FAILED,
            this._targetFile.toString(), e.toString());
        exeption.initCause(e);
        throw exeption;
      }
    }
    if (this._targetPlatformDefinition.getLocations().length == 0) {
      A4ELogging.warn("Target definition with id %s does not contain any locations!", this._id);
    }
    // add the target platform definition
    TargetPlatformRegistry targetPlatformRegistry = ServiceRegistryAccess.instance().getService(
        TargetPlatformRegistry.class);
    targetPlatformRegistry.addTargetPlatformDefinition(this._id, this._targetPlatformDefinition);
  }

  /**
   * <p>
   * Adds a {@link Location}.
   * </p>
   * 
   * @param location
   *          the {@link Location} to add.
   */
  public void addConfiguredLocation(Location location) {
    Assure.notNull("location", location);
    this._targetPlatformDefinition.addLocation(location.getDirectory());
  }

  /** Add all files selected by the specified dirset to the target platform */
  /**
   * @param dirSet
   *          The {@link DirSet}
   */
  public void addConfiguredDirSet(DirSet dirSet) {
    final DirectoryScanner scanner = dirSet.getDirectoryScanner(getProject());

    String[] includedDirectories = scanner.getIncludedDirectories();
    for (String string : includedDirectories) {
      final File directory = new File(dirSet.getDir(), string);
      Location location = new Location();
      location.setProject(getProject());
      location.setDir(directory);
      addConfiguredLocation(location);
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String toString() {
    StringBuffer buffer = new StringBuffer();
    buffer.append("[");
    buffer.append(this.getClass().getSimpleName());
    buffer.append(": _id=");
    buffer.append(this._id);
    buffer.append(", _targetPlatformDefinition=");
    buffer.append(this._targetPlatformDefinition);
    buffer.append("]");
    return buffer.toString();
  }

  /**
   * <p>
   * Implements a target platform location.
   * </p>
   * 
   * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
   */
  public static class Location extends DataType {

    /** the location path */
    private File _directory;

    /**
     * <p>
     * Creates a new instance of type {@link Location}.
     * </p>
     */
    public Location() {
      // empty constructor...
    }

    /**
     * <p>
     * Creates a new instance of type {@link Location}.
     * </p>
     * 
     * @param directory
     *          the directory.
     */
    public Location(File directory) {
      Assure.isDirectory("directory", directory);
      this._directory = directory;
    }

    /**
     * <p>
     * Returns the directory.
     * </p>
     * 
     * @return the directory
     */
    public File getDirectory() {
      return this._directory;
    }

    /**
     * <p>
     * Sets the directory.
     * </p>
     * 
     * @param directory
     *          the directory
     */
    public void setDir(File directory) {
      Assure.isDirectory("directory", directory);
      this._directory = directory;
    }

    /**
     * <p>
     * Returns <code>true</code>, if the directory is set, <code>false</code> otherwise.
     * </p>
     * 
     * @return <code>true</code>, if the directory is set, <code>false</code> otherwise.
     */
    public boolean hasDirectory() {
      return this._directory != null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
      int PRIME = 31;
      int result = 1;
      result = PRIME * result + ((this._directory == null) ? 0 : this._directory.hashCode());
      return result;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(Object obj) {
      if (this == obj) {
        return true;
      }
      if (obj == null) {
        return false;
      }
      if (getClass() != obj.getClass()) {
        return false;
      }
      Location other = (Location) obj;
      if (this._directory == null) {
        if (other._directory != null) {
          return false;
        }
      } else if (!this._directory.equals(other._directory)) {
        return false;
      }
      return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
      StringBuffer result = new StringBuffer();
      result.append("[Location path=");
      result.append(this._directory);
      result.append("]");
      return result.toString();
    }
  }
}
