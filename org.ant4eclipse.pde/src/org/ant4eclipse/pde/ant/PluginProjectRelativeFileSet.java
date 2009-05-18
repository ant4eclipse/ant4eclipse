package org.ant4eclipse.pde.ant;

import java.io.File;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.StringTokenizer;

import org.ant4eclipse.core.ant.AbstractAnt4EclipseDataType;
import org.ant4eclipse.platform.ant.core.delegate.EclipseProjectDelegate;
import org.ant4eclipse.platform.model.resource.EclipseProject;
import org.ant4eclipse.platform.model.resource.Workspace;
import org.ant4eclipse.platform.model.resource.role.ProjectRole;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.DirectoryScanner;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.types.Resource;
import org.apache.tools.ant.types.ResourceCollection;
import org.apache.tools.ant.types.resources.FileResource;

/**
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 */
public class PluginProjectRelativeFileSet extends AbstractAnt4EclipseDataType implements ResourceCollection {

  private final static String    SEPARATOR           = ",";

  private final static String    SELF                = ".";

  private final static String    SELF_REPRESENTATION = "@dot";

  private boolean                _computed           = false;

  private EclipseProjectDelegate _eclipseProjectDelegate;

  private String                 _includes;

  private String                 _excludes;

  private String[]               _excludedPattern;

  private List<Resource>         _resourceList;

  public PluginProjectRelativeFileSet(final Project project) {
    super(project);
    _eclipseProjectDelegate = new EclipseProjectDelegate(this);
    _resourceList = new LinkedList<Resource>();
  }

  /**
   * @return the includes
   */
  public String getIncludes() {
    return _includes;
  }

  /**
   * @param includes
   *          the includes to set
   */
  public void setIncludes(String includes) {
    this._includes = includes;
  }

  /**
   * @return the excludes
   */
  public String getExcludes() {
    return _excludes;
  }

  /**
   * @param excludes
   *          the excludes to set
   */
  public void setExcludes(String excludes) {
    _excludes = excludes;
  }

  /**
   * @param projectRoleClass
   * @see org.ant4eclipse.platform.ant.core.delegate.EclipseProjectDelegate#ensureRole(java.lang.Class)
   */
  public void ensureRole(Class<? extends ProjectRole> projectRoleClass) {
    _eclipseProjectDelegate.ensureRole(projectRoleClass);
  }

  /**
   * @return
   * @throws BuildException
   * @see org.ant4eclipse.platform.ant.core.delegate.EclipseProjectDelegate#getEclipseProject()
   */
  public EclipseProject getEclipseProject() throws BuildException {
    return _eclipseProjectDelegate.getEclipseProject();
  }

  /**
   * @return
   * @see org.ant4eclipse.platform.ant.core.delegate.WorkspaceDelegate#getWorkspace()
   */
  public final Workspace getWorkspace() {
    return _eclipseProjectDelegate.getWorkspace();
  }

  /**
   * @return
   * @see org.ant4eclipse.platform.ant.core.delegate.WorkspaceDelegate#getWorkspaceDirectory()
   */
  public final File getWorkspaceDirectory() {
    return _eclipseProjectDelegate.getWorkspaceDirectory();
  }

  /**
   * @return
   * @see org.ant4eclipse.platform.ant.core.delegate.EclipseProjectDelegate#isProjectNameSet()
   */
  public final boolean isProjectNameSet() {
    return _eclipseProjectDelegate.isProjectNameSet();
  }

  /**
   * @return
   * @see org.ant4eclipse.platform.ant.core.delegate.WorkspaceDelegate#isWorkspaceDirectorySet()
   */
  public final boolean isWorkspaceSet() {
    return _eclipseProjectDelegate.isWorkspaceDirectorySet();
  }

  /**
   *
   * @see org.ant4eclipse.platform.ant.core.delegate.EclipseProjectDelegate#requireWorkspaceAndProjectNameSet()
   */
  public final void requireWorkspaceAndProjectNameSet() {
    _eclipseProjectDelegate.requireWorkspaceAndProjectNameSet();
  }

  /**
   *
   * @see org.ant4eclipse.platform.ant.core.delegate.WorkspaceDelegate#requireWorkspaceDirectorySet()
   */
  public final void requireWorkspaceSet() {
    _eclipseProjectDelegate.requireWorkspaceDirectorySet();
  }

  /**
   * @param projectName
   * @see org.ant4eclipse.platform.ant.core.delegate.EclipseProjectDelegate#setProjectName(java.lang.String)
   */
  public final void setProjectName(String projectName) {
    _eclipseProjectDelegate.setProjectName(projectName);
  }

  /**
   * @param workspace
   * @deprecated
   * @see org.ant4eclipse.platform.ant.core.delegate.WorkspaceDelegate#setWorkspace(java.io.File)
   */
  public final void setWorkspace(File workspace) {
    _eclipseProjectDelegate.setWorkspace(workspace);
  }

  /**
   * @param workspaceDirectory
   * @see org.ant4eclipse.platform.ant.core.delegate.WorkspaceDelegate#setWorkspaceDirectory(java.io.File)
   */
  public final void setWorkspaceDirectory(File workspaceDirectory) {
    _eclipseProjectDelegate.setWorkspaceDirectory(workspaceDirectory);
  }

  public boolean isFilesystemOnly() {
    return true;
  }

  public Iterator<Resource> iterator() {
    computeFileSet();

    System.err.println(_resourceList);

    return _resourceList.iterator();
  }

  public int size() {
    computeFileSet();

    return _resourceList.size();
  }

  private void computeFileSet() {
    if (_computed) {
      return;
    }

    requireWorkspaceAndProjectNameSet();

    if (_includes == null || _includes.trim().equals("")) {
      return;
      // throw new BuildException("You have to specify the includes attribute!");
    }

    if (_excludes != null && !_excludes.trim().equals("")) {
      StringTokenizer stringTokenizer = new StringTokenizer(_excludes, SEPARATOR);
      int count = stringTokenizer.countTokens();
      _excludedPattern = new String[count];
      int i = 0;
      while (stringTokenizer.hasMoreTokens()) {
        _excludedPattern[i] = stringTokenizer.nextToken();
        i++;
      }
    } else {
      _excludes = null;
    }

    _resourceList.clear();
    StringTokenizer stringTokenizer = new StringTokenizer(_includes, SEPARATOR);
    while (stringTokenizer.hasMoreTokens()) {
      String token = stringTokenizer.nextToken().trim();
      // 'patch' the dot
      if (token.equals(SELF)) {
        token = SELF_REPRESENTATION;
      }

      if (getEclipseProject().hasChild(token)) {
        File file = getEclipseProject().getChild(token);

        if (file.isFile()) {
          _resourceList.add(new FileResource(file));
        } else {
          // scan directory
          DirectoryScanner directoryScanner = new DirectoryScanner();
          directoryScanner.setBasedir(file);
          directoryScanner.setIncludes(null);

          // TODO: exlude-patterns
          directoryScanner.addExcludes(_excludedPattern);
          directoryScanner.addDefaultExcludes();
          directoryScanner.scan();
          String[] files = directoryScanner.getIncludedFiles();
          for (String name : files) {
            if (token.equals(SELF_REPRESENTATION)) {
              _resourceList.add(new FileResource(file, name));
            } else {
              _resourceList.add(new FileResource(file.getParentFile(), token + File.separatorChar + name));
            }
          }
        }
      }
    }
    _computed = true;
  }
}
