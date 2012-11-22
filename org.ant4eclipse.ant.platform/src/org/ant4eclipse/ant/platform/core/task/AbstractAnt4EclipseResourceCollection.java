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
package org.ant4eclipse.ant.platform.core.task;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.ant4eclipse.ant.core.AbstractAnt4EclipseDataType;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.types.Resource;
import org.apache.tools.ant.types.ResourceCollection;

public abstract class AbstractAnt4EclipseResourceCollection extends AbstractAnt4EclipseDataType implements
    ResourceCollection {

  /** the result resource list */
  private List<Resource> _resourceList;

  /** indicates if the file list already has been computed */
  private boolean        _fileListComputed = false;

  /**
   * <p>
   * Creates a new instance of type {@link PdeProjectFileSet}.
   * </p>
   * 
   * @param project
   *          the ant project
   */
  public AbstractAnt4EclipseResourceCollection(Project project) {
    super(project);

    // create the result list
    this._resourceList = new LinkedList<Resource>();

  }

  /**
   * {@inheritDoc}
   */
  public boolean isFilesystemOnly() {
    return true;
  }

  /**
   * <p>
   * Performs the check for circular references and returns the referenced {@link PdeProjectFileSet}.
   * </p>
   * 
   * @param p
   *          the current project
   * @return the referenced {@link PdeProjectFileSet}
   */
  protected AbstractAnt4EclipseFileSet getRef(Project p) {
    return (AbstractAnt4EclipseFileSet) getCheckedRef(p);
  }

  /**
   * <p>
   * </p>
   */
  protected void clear() {
    this._resourceList.clear();
    this._fileListComputed = false;
  }

  /**
   * {@inheritDoc}
   */
  public Iterator<Resource> iterator() {
    computeFileSet();

    return this._resourceList.iterator();
  }

  /**
   * {@inheritDoc}
   */
  public int size() {
    computeFileSet();

    return this._resourceList.size();
  }

  public boolean isFileListComputed() {
    return this._fileListComputed;
  }

  /**
   * <p>
   * Computes the file set if it has not been computed already
   * </p>
   */
  protected void computeFileSet() {
    // return if file list already is computed
    if (this._fileListComputed) {
      return;
    }

    // Clear the FileList
    this._resourceList.clear();

    // do the work
    doComputeFileSet(this._resourceList);

    // set _fileListComputed
    this._fileListComputed = true;
  }

  /**
   * Compute the actual FileSet.
   * 
   * <p>
   * This method will only be called if the FileSet has not been computed already. Implementors don't need to check if
   * it's neccessary to compute the fileset
   */
  /**
   * @param resourceList
   *          The result list all resources should be added to
   */
  protected abstract void doComputeFileSet(List<Resource> resourceList);

}
