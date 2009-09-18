package org.ant4eclipse.pde.ant;

import org.ant4eclipse.core.ant.AbstractAnt4EclipseDataType;
import org.ant4eclipse.core.logging.A4ELogging;

import org.ant4eclipse.pde.internal.ant.LibraryHelper;

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
import java.util.StringTokenizer;

/**
 * <p>
 * The {@link PdeProjectFileSet} type can be used to define plug-in project relative file sets.
 * </p>
 * 
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 */
public class PdeProjectFileSet extends AbstractAnt4EclipseDataType implements ResourceCollection,
    EclipseProjectComponent {

  /** the separator for inclusion and exclusion pattern */
  private static final String    SEPARATOR              = ",";

  /** the bundle root (self) */
  private static final String    SELF                   = ".";

  /** the name of the (default) self directory */
  private static final String    DEFAULT_SELF_DIRECTORY = "@dot";

  /** 'ant-provided' attributes **/

  /** the ant attribute 'includes' */
  private String                 _includes;

  /** the ant attribute 'excludes' */
  private String                 _excludes;

  /** the ant attribute 'useDefaultExcludes' */
  private boolean                _useDefaultExcludes    = true;

  /** the ant attribute 'caseSensitive' */
  private boolean                _caseSensitive         = false;

  /** the ant attribute 'isSourceFileSet' */
  private boolean                _isSourceFileSet       = false;

  /** 'derived' attributes **/

  /** the exclusion pattern as an array */
  private String[]               _excludedPattern;

  /** the result resource list */
  private List<Resource>         _resourceList;

  /** the eclipse project delegate */
  private EclipseProjectDelegate _eclipseProjectDelegate;

  /** indicates if the file list already has been computed */
  private boolean                _fileListComputed      = false;

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
   * Returns if this {@link PdeProjectFileSet} is a source file set.
   * </p>
   * 
   * @return if this {@link PdeProjectFileSet} is a source file set.
   */
  public boolean isSourceFileSet() {
    return this._isSourceFileSet;
  }

  /**
   * <p>
   * Sets if this {@link PdeProjectFileSet} is a source file set or not.
   * </p>
   * 
   * @param isSourceFileSet
   *          if this {@link PdeProjectFileSet} is a source file set or not.
   */
  public void setSourceFileSet(boolean isSourceFileSet) {
    this._isSourceFileSet = isSourceFileSet;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setRefid(Reference ref) {
    if (this._includes != null && !"".equals(this._includes)) {
      throw tooManyAttributes();
    }
    if (this._excludes != null && !"".equals(this._excludes)) {
      throw tooManyAttributes();
    }

    super.setRefid(ref);
  }

  /**
   * <p>
   * Returns the inclusion pattern.
   * </p>
   * 
   * @return the inclusion pattern.
   */
  public String getIncludes() {
    return this._includes;
  }

  /**
   * <p>
   * Sets the inclusion pattern.
   * </p>
   * 
   * @param includes
   *          the inclusion pattern
   */
  public void setIncludes(String includes) {
    if (isReference()) {
      throw tooManyAttributes();
    }

    this._includes = includes;
  }

  /**
   * <p>
   * Returns the exclusion pattern.
   * </p>
   * 
   * @return the exclusion pattern.
   */
  public String getExcludes() {
    return this._excludes;
  }

  /**
   * <p>
   * Sets the inclusion pattern.
   * </p>
   * 
   * @param excludes
   *          the exclusion pattern
   */
  public void setExcludes(String excludes) {
    if (isReference()) {
      throw tooManyAttributes();
    }

    this._excludes = excludes;
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

    // nothing to do if no inclusion pattern is defined
    if (this._includes == null || "".equals(this._includes.trim())) {
      return;
    }

    // split the exclusion pattern
    splitExclusionPattern();

    // clear the resource list
    this._resourceList.clear();

    // iterate over the included pattern set
    StringTokenizer stringTokenizer = new StringTokenizer(this._includes, SEPARATOR);

    while (stringTokenizer.hasMoreTokens()) {
      String token = stringTokenizer.nextToken().trim();
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

    // 'patch' the dot
    if (token.equals(SELF)) {
      token = DEFAULT_SELF_DIRECTORY;
    }
    // patch the included library if '_isSourceFileSet'
    else if (this._isSourceFileSet) {
      token = LibraryHelper.getSourceNameForLibrary(token);
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
              String rootPath = normalize(filePath).substring(0, filePath.indexOf(normalize(token)));
              this._resourceList.add(new FileResource(new File(rootPath), token + File.separatorChar + fileName));
            }
          }
        }
      }
    }
  }

  /**
   * <p>
   * </p>
   */
  private void splitExclusionPattern() {
    if (this._excludes != null && !"".equals(this._excludes.trim())) {
      StringTokenizer stringTokenizer = new StringTokenizer(this._excludes, SEPARATOR);
      int count = stringTokenizer.countTokens();
      this._excludedPattern = new String[count];
      int i = 0;
      while (stringTokenizer.hasMoreTokens()) {
        this._excludedPattern[i] = stringTokenizer.nextToken();
        i++;
      }
    } else {
      this._excludedPattern = new String[0];
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

    // iterate over all excluded pattern
    for (String pattern : this._excludedPattern) {

      // if the given path matches an exclusion pattern, return true
      if (SelectorUtils.matchPath(normalize(pattern), normalize(path), this._caseSensitive)) {
        return true;
      }
    }

    // return false
    return false;
  }
}
