package org.ant4eclipse.pde.ant;

import org.ant4eclipse.core.ant.AbstractAnt4EclipseDataType;

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

  /** the ant attribute 'includes' */
  private String                 _includes;

  /** the ant attribute 'excludes' */
  private String                 _excludes;

  /** the ant attribute 'useDefaultExcludes' */
  private boolean                _useDefaultExcludes    = true;

  /** the ant attribute 'caseSensitive' */
  private boolean                _caseSensitive;

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
  public PdeProjectFileSet(final Project project) {
    super(project);

    // create the project delegate
    _eclipseProjectDelegate = new EclipseProjectDelegate(this);

    // create the result list
    _resourceList = new LinkedList<Resource>();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setRefid(Reference ref) {
    if (_includes != null && !"".equals(_includes)) {
      throw tooManyAttributes();
    }
    if (_excludes != null && !"".equals(_excludes)) {
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
    return _includes;
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
    return _excludes;
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

    _excludes = excludes;
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
    return (isReference()) ? getRef(getProject()).getDefaultexcludes() : _useDefaultExcludes;
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
    return (isReference()) ? getRef(getProject()).isCaseSensitive() : _caseSensitive;
  }

  /**
   * {@inheritDoc}
   */
  public void ensureRole(Class<? extends ProjectRole> projectRoleClass) {
    _eclipseProjectDelegate.ensureRole(projectRoleClass);
  }

  /**
   * {@inheritDoc}
   */
  public EclipseProject getEclipseProject() throws BuildException {
    return _eclipseProjectDelegate.getEclipseProject();
  }

  /**
   * {@inheritDoc}
   */
  public final Workspace getWorkspace() {
    return _eclipseProjectDelegate.getWorkspace();
  }

  /**
   * {@inheritDoc}
   */
  public final File getWorkspaceDirectory() {
    return _eclipseProjectDelegate.getWorkspaceDirectory();
  }

  /**
   * {@inheritDoc}
   */
  public final boolean isProjectNameSet() {
    return _eclipseProjectDelegate.isProjectNameSet();
  }

  /**
   * {@inheritDoc}
   */
  public final boolean isWorkspaceDirectorySet() {
    return _eclipseProjectDelegate.isWorkspaceDirectorySet();
  }

  /**
   * {@inheritDoc}
   */
  public final void requireWorkspaceAndProjectNameSet() {
    _eclipseProjectDelegate.requireWorkspaceAndProjectNameSet();
  }

  /**
   * {@inheritDoc}
   */
  public final void requireWorkspaceDirectorySet() {
    _eclipseProjectDelegate.requireWorkspaceDirectorySet();
  }

  /**
   * {@inheritDoc}
   */
  @Deprecated
  public void setProject(File projectPath) {
    _eclipseProjectDelegate.setProject(projectPath);
  }

  /**
   * {@inheritDoc}
   */
  public final void setProjectName(String projectName) {
    _eclipseProjectDelegate.setProjectName(projectName);
  }

  /**
   * {@inheritDoc}
   */
  @Deprecated
  public final void setWorkspace(File workspace) {
    _eclipseProjectDelegate.setWorkspace(workspace);
  }

  /**
   * {@inheritDoc}
   */
  public final void setWorkspaceDirectory(File workspaceDirectory) {
    _eclipseProjectDelegate.setWorkspaceDirectory(workspaceDirectory);
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

    return _resourceList.iterator();
  }

  /**
   * {@inheritDoc}
   */
  public int size() {
    computeFileSet();

    return _resourceList.size();
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
    _resourceList.clear();
    _fileListComputed = false;
  }

  /**
   * <p>
   * Computes the file set.
   * </p>
   */
  protected void computeFileSet() {

    // return if file list already is computed
    if (_fileListComputed) {
      return;
    }

    // require workspace and project name set
    requireWorkspaceAndProjectNameSet();

    // nothing to do if no inclusion pattern is defined
    if (_includes == null || "".equals(_includes.trim())) {
      return;
    }

    // split the exclusion pattern
    if (_excludes != null && !"".equals(_excludes.trim())) {
      StringTokenizer stringTokenizer = new StringTokenizer(_excludes, SEPARATOR);
      int count = stringTokenizer.countTokens();
      _excludedPattern = new String[count];
      int i = 0;
      while (stringTokenizer.hasMoreTokens()) {
        _excludedPattern[i] = stringTokenizer.nextToken();
        i++;
      }
    } else {
      _excludedPattern = new String[0];
    }

    // clear the resource list
    _resourceList.clear();

    // iterate over the included pattern set
    StringTokenizer stringTokenizer = new StringTokenizer(_includes, SEPARATOR);
    while (stringTokenizer.hasMoreTokens()) {
      String token = stringTokenizer.nextToken().trim();

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
          _resourceList.add(new FileResource(getEclipseProject().getFolder(), token));
        } else {
          // if the child is a directory, scan the directory
          DirectoryScanner directoryScanner = new DirectoryScanner();
          // set base directory
          directoryScanner.setBasedir(file);
          // set case sensitive
          directoryScanner.setCaseSensitive(this._caseSensitive);
          // set includes
          directoryScanner.setIncludes(null);
          // set default excludes
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
                _resourceList.add(new FileResource(file, fileName));
              }
            } else {
              if (!matchExcludePattern(token + File.separatorChar + fileName)) {
                System.err.println("Path: " + file.getPath());
                System.err.println("token: " + token);
                String filePath = normalize(file.getPath());
                String rootPath = normalize(filePath).substring(0, filePath.indexOf(normalize(token)));
                System.err.println("RootPath: " + rootPath);
                _resourceList.add(new FileResource(new File(rootPath), token + File.separatorChar + fileName));
              }
            }
          }
        }
      }
    }

    // for (Resource resource : _resourceList) {
    // System.err.println(resource);
    // }

    // set _fileListComputed
    _fileListComputed = true;
  }

  /**
   * <p>
   * </p>
   * 
   * @param string
   * @return
   */
  private String normalize(String string) {

    String result = string.replace('/', File.separatorChar).replace('\\', File.separatorChar);

    if (result.endsWith("/") || result.endsWith("\\")) {
      result = result.substring(0, result.length() - 1);
    }

    return result;
  }

  /**
   * <p>
   * </p>
   * 
   * @param string
   * @return
   */
  private boolean matchExcludePattern(String string) {
    for (String pattern : _excludedPattern) {
       System.err.println("string: " + string);
       System.err.println("pattern: " + pattern);
      if (SelectorUtils.matchPath(normalize(pattern), normalize(string), _caseSensitive)) {
         System.err.println("result: true");
        return true;
      }
      System.err.println("result: false");
    }

    return false;
  }
}
