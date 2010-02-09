package org.ant4eclipse.ant.platform.core.task;

import java.io.File;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.ant4eclipse.ant.core.AbstractAnt4EclipseDataType;
import org.ant4eclipse.ant.platform.core.EclipseProjectComponent;
import org.ant4eclipse.ant.platform.core.delegate.EclipseProjectDelegate;
import org.ant4eclipse.lib.platform.model.resource.EclipseProject;
import org.ant4eclipse.lib.platform.model.resource.Workspace;
import org.ant4eclipse.lib.platform.model.resource.role.ProjectRole;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.types.Reference;
import org.apache.tools.ant.types.Resource;
import org.apache.tools.ant.types.ResourceCollection;

public abstract class AbstractAnt4EclipseFileSet extends AbstractAnt4EclipseDataType implements ResourceCollection,
    EclipseProjectComponent {
  /** 'ant-provided' attributes **/

  /** the ant attribute 'useDefaultExcludes' */
  private boolean                _useDefaultExcludes = true;

  /** the ant attribute 'caseSensitive' */
  private boolean                _caseSensitive      = false;

  /** 'derived' attributes **/

  /** the result resource list */
  private List<Resource>         _resourceList;

  /** the eclipse project delegate */
  private EclipseProjectDelegate _eclipseProjectDelegate;

  /** indicates if the file list already has been computed */
  private boolean                _fileListComputed   = false;

  /**
   * <p>
   * Creates a new instance of type {@link PdeProjectFileSet}.
   * </p>
   * 
   * @param project
   *          the ant project
   */
  public AbstractAnt4EclipseFileSet(Project project) {
    super(project);

    // create the project delegate
    this._eclipseProjectDelegate = new EclipseProjectDelegate(this);

    // create the result list
    this._resourceList = new LinkedList<Resource>();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setRefid(Reference ref) {
    if (isWorkspaceDirectorySet() || isProjectNameSet()) {
      throw tooManyAttributes();
    }

    super.setRefid(ref);
  }

  /**
   * <p>
   * Sets whether default exclusions should be used or not.
   * </p>
   * 
   * @param useDefaultExcludes
   *          <code>boolean</code>.
   */
  public synchronized void setDefaultexcludes(boolean useDefaultExcludes) {
    if (isReference()) {
      throw tooManyAttributes();
    }

    this._useDefaultExcludes = useDefaultExcludes;
  }

  /**
   * <p>
   * Whether default exclusions should be used or not.
   * </p>
   * 
   * @return the default exclusions value.
   */
  public synchronized boolean getDefaultexcludes() {
    return isReference() ? getRef(getProject()).getDefaultexcludes() : this._useDefaultExcludes;
  }

  /**
   * <p>
   * Sets case sensitivity of the file system.
   * </p>
   * 
   * @param caseSensitive
   *          <code>boolean</code>.
   */
  public synchronized void setCaseSensitive(boolean caseSensitive) {
    if (isReference()) {
      throw tooManyAttributes();
    }
    this._caseSensitive = caseSensitive;
  }

  /**
   * <p>
   * Find out if the file set is case sensitive.
   * </p>
   * 
   * @return <code>boolean</code> indicating whether the file set is case sensitive.
   */
  public synchronized boolean isCaseSensitive() {
    return isReference() ? getRef(getProject()).isCaseSensitive() : this._caseSensitive;
  }

  /**
   * {@inheritDoc}
   */
  public void ensureRole(Class<? extends ProjectRole> projectRoleClass) {
    this._eclipseProjectDelegate.ensureRole(projectRoleClass);
  }

  /**
   * {@inheritDoc}
   */
  public EclipseProject getEclipseProject() throws BuildException {
    return this._eclipseProjectDelegate.getEclipseProject();
  }

  /**
   * {@inheritDoc}
   */
  public final Workspace getWorkspace() {
    return this._eclipseProjectDelegate.getWorkspace();
  }

  /**
   * {@inheritDoc}
   */
  public final File getWorkspaceDirectory() {
    return this._eclipseProjectDelegate.getWorkspaceDirectory();
  }

  /**
   * {@inheritDoc}
   */
  public final boolean isProjectNameSet() {
    return this._eclipseProjectDelegate.isProjectNameSet();
  }

  /**
   * {@inheritDoc}
   */
  public final boolean isWorkspaceDirectorySet() {
    return this._eclipseProjectDelegate.isWorkspaceDirectorySet();
  }

  /**
   * {@inheritDoc}
   */
  public final void requireWorkspaceAndProjectNameSet() {
    this._eclipseProjectDelegate.requireWorkspaceAndProjectNameSet();
  }

  /**
   * {@inheritDoc}
   */
  public final void requireWorkspaceDirectorySet() {
    this._eclipseProjectDelegate.requireWorkspaceDirectorySet();
  }

  /**
   * {@inheritDoc}
   */
  @Deprecated
  public void setProject(File projectPath) {
    this._eclipseProjectDelegate.setProject(projectPath);
  }

  /**
   * {@inheritDoc}
   */
  public final void setProjectName(String projectName) {
    this._eclipseProjectDelegate.setProjectName(projectName);
  }

  /**
   * {@inheritDoc}
   */
  @Deprecated
  public final void setWorkspace(File workspace) {
    this._eclipseProjectDelegate.setWorkspace(workspace);
  }

  /**
   * {@inheritDoc}
   */
  public final void setWorkspaceDirectory(File workspaceDirectory) {
    this._eclipseProjectDelegate.setWorkspaceDirectory(workspaceDirectory);
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
