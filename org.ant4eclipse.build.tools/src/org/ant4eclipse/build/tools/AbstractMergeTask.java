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
package org.ant4eclipse.build.tools;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Task;
import org.apache.tools.ant.types.FileSet;
import org.apache.tools.ant.types.ResourceCollection;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

/**
 * <p>
 * Implements the abstract base class for merge tasks.
 * </p>
 * 
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 */
public abstract class AbstractMergeTask extends Task {

  /** the resource collections */
  private List<ResourceCollection> _resourceCollections;

  /** the destination file */
  private File                     _destinationFile;

  /**
   * <p>
   * Creates a new instance of type {@link AbstractMergeTask}.
   * </p>
   */
  public AbstractMergeTask() {

    // create the resource collection list
    this._resourceCollections = new LinkedList<ResourceCollection>();
  }

  /**
   * <p>
   * Adds a set of files.
   * </p>
   * 
   * @param set
   *          a set of files.
   */
  public void addFileset(FileSet set) {
    add(set);
  }

  /**
   * <p>
   * Adds a collection of files.
   * </p>
   * 
   * @param resourceCollection
   *          a resource collection.
   */
  public void add(ResourceCollection resourceCollection) {
    this._resourceCollections.add(resourceCollection);
  }

  /**
   * <p>
   * Sets the destination file.
   * </p>
   * 
   * @param destinationFile
   *          the destination file.
   */
  public void setDestinationFile(File destinationFile) {
    this._destinationFile = destinationFile;
  }

  /**
   * <p>
   * Returns the resource collections.
   * </p>
   * 
   * @return the resource collections.
   */
  public List<ResourceCollection> getResourceCollections() {
    return this._resourceCollections;
  }

  /**
   * <p>
   * The destination file.
   * </p>
   * 
   * @return the destination file.
   */
  public File getDestinationFile() {
    return this._destinationFile;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public final void execute() throws BuildException {
    // Validates that the task parameters are valid.
    validate();
    try {
      doExecute();
    } catch (Exception e) {
      throw new BuildException(e.getMessage(), e);
    }
  }

  /**
   * <p>
   * Abstract method. Subclasses of this class must override this method.
   * </p>
   * 
   * @throws Exception
   */
  protected abstract void doExecute() throws Exception;

  /**
   * <p>
   * Validate that the task parameters are valid.
   * </p>
   * 
   * @throws BuildException
   *           if parameters are invalid
   */
  protected void validate() throws BuildException {
    if (!this._destinationFile.canWrite()) {
      throw new BuildException("Unable to write to " + this._destinationFile + ".");
    }
  }
}
