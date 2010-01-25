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
package org.ant4eclipse.pde.ant;

import org.ant4eclipse.core.ant.AbstractAnt4EclipseDataType;
import org.ant4eclipse.core.logging.A4ELogging;

import org.ant4eclipse.pde.model.buildproperties.AbstractBuildProperties;
import org.ant4eclipse.pde.model.buildproperties.PluginBuildProperties;
import org.ant4eclipse.pde.model.featureproject.FeatureProjectRole;
import org.ant4eclipse.pde.model.pluginproject.PluginProjectRole;

import org.ant4eclipse.platform.ant.core.EclipseProjectComponent;
import org.ant4eclipse.platform.ant.core.delegate.EclipseProjectDelegate;
import org.ant4eclipse.platform.model.resource.EclipseProject;
import org.ant4eclipse.platform.model.resource.Workspace;
import org.ant4eclipse.platform.model.resource.role.ProjectRole;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.DirectoryScanner;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.types.Reference;
import org.apache.tools.ant.types.Resource;
import org.apache.tools.ant.types.ResourceCollection;
import org.apache.tools.ant.types.resources.FileResource;
import org.apache.tools.ant.types.selectors.SelectorUtils;

import java.io.File;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * <p>
 * The {@link PdeProjectFileSet} type can be used to define plug-in project relative file sets.
 * </p>
 * 
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 */
public class PdeProjectFileSet extends AbstractAnt4EclipseDataType implements ResourceCollection,
    EclipseProjectComponent {

  /** the bundle root (self) */
  private static final String     SELF                   = ".";

  /** the name of the (default) self directory */
  private static final String     DEFAULT_SELF_DIRECTORY = "@dot";

  /** 'ant-provided' attributes **/

  /** the ant attribute 'useDefaultExcludes' */
  private boolean                 _useDefaultExcludes    = true;

  /** the ant attribute 'caseSensitive' */
  private boolean                 _caseSensitive         = false;

  /** the ant attribute 'excludeLibraries' */
  private boolean                 _excludeLibraries      = false;

  /** 'derived' attributes **/

  /** the result resource list */
  private List<Resource>          _resourceList;

  /** the eclipse project delegate */
  private EclipseProjectDelegate  _eclipseProjectDelegate;

  /** indicates if the file list already has been computed */
  private boolean                 _fileListComputed      = false;

  /** - */
  private boolean                 _sourceBundle          = false;

  /** - */
  private AbstractBuildProperties _buildProperties;

  /**
   * <p>
   * Creates a new instance of type {@link PdeProjectFileSet}.
   * </p>
   * 
   * @param project
   *          the ant project
   */
  public PdeProjectFileSet(Project project) {
    super(project);

    // create the project delegate
    this._eclipseProjectDelegate = new EclipseProjectDelegate(this);

    // create the result list
    this._resourceList = new LinkedList<Resource>();
  }

  /**
   * <p>
   * </p>
   * 
   * @return the sourceBundle
   */
  public boolean isSourceBundle() {
    return this._sourceBundle;
  }

  /**
   * <p>
   * </p>
   * 
   * @param sourceBundle
   *          the sourceBundle to set
   */
  public void setSourceBundle(boolean sourceBundle) {
    this._sourceBundle = sourceBundle;
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
   * </p>
   * 
   * @return the excludeLibraries
   */
  public boolean isExcludeLibraries() {
    return this._excludeLibraries;
  }

  /**
   * <p>
   * </p>
   * 
   * @param excludeLibraries
   *          the excludeLibraries to set
   */
  public void setExcludeLibraries(boolean excludeLibraries) {
    this._excludeLibraries = excludeLibraries;
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
    return (isReference()) ? getRef(getProject()).getDefaultexcludes() : this._useDefaultExcludes;
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
    return (isReference()) ? getRef(getProject()).isCaseSensitive() : this._caseSensitive;
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

  /**
   * <p>
   * Performs the check for circular references and returns the referenced {@link PdeProjectFileSet}.
   * </p>
   * 
   * @param p
   *          the current project
   * @return the referenced {@link PdeProjectFileSet}
   */
  protected PdeProjectFileSet getRef(Project p) {
    return (PdeProjectFileSet) getCheckedRef(p);
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
   * <p>
   * Computes the file set.
   * </p>
   */
  protected void computeFileSet() {

    // return if file list already is computed
    if (this._fileListComputed) {
      return;
    }

    // require workspace and project name set
    requireWorkspaceAndProjectNameSet();

    if (!(getEclipseProject().hasRole(PluginProjectRole.class) || getEclipseProject().hasRole(FeatureProjectRole.class))) {
      throw new BuildException(String.format(
          "Project '%s' must have role 'PluginProjectRole' or 'FeatureProjectRole'.", getEclipseProject()
              .getSpecifiedName()));
    }

    this._buildProperties = getEclipseProject().hasRole(PluginProjectRole.class) ? PluginProjectRole.Helper
        .getPluginProjectRole(getEclipseProject()).getBuildProperties() : FeatureProjectRole.Helper
        .getFeatureProjectRole(getEclipseProject()).getBuildProperties();

    // nothing to do if no inclusion pattern is defined
    if (this._sourceBundle && (!this._buildProperties.hasSourceIncludes())) {
      return;
    } else if ((!this._sourceBundle) && (!this._buildProperties.hasBinaryIncludes())) {
      return;
    }

    // clear the resource list
    this._resourceList.clear();

    // iterate over the included pattern set
    String[] includes = this._sourceBundle ? this._buildProperties.getSourceIncludes() : this._buildProperties
        .getBinaryIncludes();
    for (String token : includes) {
      processEntry(token);
    }

    // debug the resolved entries
    if (A4ELogging.isDebuggingEnabled()) {
      A4ELogging.debug("Resolved pde project file set for project '%s'. Entries are:", getEclipseProject()
          .getSpecifiedName());
      for (Resource resource : this._resourceList) {
        A4ELogging.debug("- '%s'", resource);
      }
    }

    // set _fileListComputed
    this._fileListComputed = true;
  }

  /**
   * <p>
   * 
   * </p>
   * 
   * @param token
   */
  private void processEntry(String token) {

    // if token is a library name and _excludeLibraries
    if (this._excludeLibraries && this._buildProperties instanceof PluginBuildProperties
        && ((PluginBuildProperties) this._buildProperties).hasLibrary(token)) {
      return;
    }

    // 'patch' the dot
    if (token.equals(SELF)) {
      token = DEFAULT_SELF_DIRECTORY;
    }

    // 'process' the token
    if (getEclipseProject().hasChild(token)) {

      // get the project child with the given name
      File file = getEclipseProject().getChild(token);

      if (file.isFile()) {
        // if the child is a file, just add it to the list
        this._resourceList.add(new FileResource(getEclipseProject().getFolder(), token));
      } else {

        // if the child is a directory, scan the directory
        DirectoryScanner directoryScanner = new DirectoryScanner();
        directoryScanner.setBasedir(file);
        directoryScanner.setCaseSensitive(this._caseSensitive);
        directoryScanner.setIncludes(null);
        if (this._useDefaultExcludes) {
          directoryScanner.addDefaultExcludes();
        }

        // do the job
        directoryScanner.scan();

        // get the included files and add it to the resource list
        String[] files = directoryScanner.getIncludedFiles();

        for (String fileName : files) {
          if (token.equals(DEFAULT_SELF_DIRECTORY)) {
            if (!matchExcludePattern(fileName)) {
              this._resourceList.add(new FileResource(file, fileName));
            }
          } else {
            if (!matchExcludePattern(token + File.separatorChar + fileName)) {
              String filePath = normalize(file.getPath());
              String rootPath = normalize(filePath).substring(0, filePath.lastIndexOf(normalize(token)));
              this._resourceList.add(new FileResource(new File(rootPath), token + File.separatorChar + fileName));
            }
          }
        }
      }
    }
  }

  /**
   * <p>
   * Helper method. Normalizes the given path.
   * </p>
   * 
   * @param path
   *          the path to normalize
   * @return the normalized path
   */
  private String normalize(String path) {

    // replace '/' and '\' with File.separatorChar
    String result = path.replace('/', File.separatorChar).replace('\\', File.separatorChar);

    // remove trailing '/' and '\'
    if (result.endsWith("/") || result.endsWith("\\")) {
      result = result.substring(0, result.length() - 1);
    }

    // return result
    return result;
  }

  /**
   * <p>
   * Helper method. Checks if the given string (which is a path) matches one of the exclude patterns.
   * </p>
   * 
   * @param path
   *          the path
   * @return <code>true</code> if the given path matches an exclusion pattern.
   */
  private boolean matchExcludePattern(String path) {

    String[] excludes = this._sourceBundle ? this._buildProperties.getSourceExcludes() : this._buildProperties
        .getBinaryExcludes();

    // iterate over all excluded pattern
    for (String pattern : excludes) {

      // if the given path matches an exclusion pattern, return true
      if (SelectorUtils.matchPath(normalize(pattern), normalize(path), this._caseSensitive)) {
        return true;
      }
    }

    // return false
    return false;
  }
}
