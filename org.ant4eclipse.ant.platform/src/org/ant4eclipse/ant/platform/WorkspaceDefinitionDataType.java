package org.ant4eclipse.ant.platform;

import org.ant4eclipse.ant.core.AbstractAnt4EclipseDataType;
import org.ant4eclipse.lib.core.logging.A4ELevel;
import org.ant4eclipse.lib.core.logging.A4ELogging;
import org.ant4eclipse.lib.core.service.ServiceRegistryAccess;
import org.ant4eclipse.lib.core.util.PropertyService;
import org.ant4eclipse.lib.platform.internal.model.resource.workspaceregistry.ProjectFileParser;
import org.ant4eclipse.lib.platform.model.resource.workspaceregistry.FilesetWorkspaceDefinition;
import org.ant4eclipse.lib.platform.model.resource.workspaceregistry.WorkspaceRegistry;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.DirectoryScanner;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.types.DirSet;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.List;

/**
 * 
 * This task will register a workspace based on a fileset with the workspace registry. This allows the system to use any
 * old directory as a workspace. The benefit is that a4e will run on a system without a proper eclipse workspace.
 * 
 * @author mriley
 * 
 */
public class WorkspaceDefinitionDataType extends AbstractAnt4EclipseDataType {

  /** - */
  private String       _id;

  /**
   * The filesets which are combined to register the workspace
   */
  private List<DirSet> _dirSet;

  /**
   * indicates if this definition scanns directories recursive
   */
  private boolean      recursive;

  /**
   * <p>
   * Creates a new instance of type {@link WorkspaceDefinitionDataType}.
   * </p>
   * 
   * @param project
   */
  public WorkspaceDefinitionDataType(Project project) {
    super(project);
    //
    this._dirSet = new ArrayList<DirSet>();
  }

  public void setId(String id) {
    if (isReference()) {
      throw tooManyAttributes();
    }

    this._id = id;
  }

  /**
   * @param recursive
   *          the new value for recursive
   */
  public void setRecursive(boolean recursive) {
    this.recursive = recursive;
  }

  /**
   * <p>
   * </p>
   * 
   * @param dirSet
   */
  public void addDirSet(DirSet dirSet) {
    this._dirSet.add(dirSet);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected void doValidate() {
    if (this._dirSet.isEmpty()) {
      throw new BuildException("Workspace registration requires at least one fileset!");
    }

    if (this._id == null) {
      throw new BuildException("Workspace registration requires the identifier property to be set!");
    }
    // create a workspace from the filesets
    List<File> projectDirectories = new ArrayList<File>();
    for (DirSet fs : this._dirSet) {
      DirectoryScanner directoryScanner = fs.getDirectoryScanner();
      File fsBase = directoryScanner.getBasedir();
      for (String d : directoryScanner.getIncludedDirectories()) {
        File file = new File(fsBase, d);
        addDirectory(projectDirectories, file);
      }
    }
    if (projectDirectories.isEmpty()) {
      A4ELogging.warn("No directories found for workspace %s", this._id);
    }
    WorkspaceRegistry registry = ServiceRegistryAccess.instance().getService(WorkspaceRegistry.class);
    registry.registerWorkspace(this._id,
        new FilesetWorkspaceDefinition(projectDirectories.toArray(new File[projectDirectories.size()])));
  }

  /**
   * Adds an directory if it is valid and is a eclipse project to the set of project directories
   * 
   * @param projectDirectories
   * @param file
   */
  private void addDirectory(List<File> projectDirectories, File file) {
    if (!isValidDirectory(file)) {
      return;
    }
    // and it should have a .project file
    if (!isEclipseProject(file)) {
      // Do we use recursive scanning?
      if (this.recursive) {
        A4ELogging.debug("Scann " + file + " for subdirectories of eclipse projects.");
        File[] subFolders = file.listFiles(new FileFilter() {

          public boolean accept(File pathname) {
            return pathname.isDirectory();
          }
        });
        // Any subfolders to scan?
        if (subFolders.length > 0) {
          for (File subFolder : subFolders) {
            addDirectory(projectDirectories, subFolder);
          }
          return;
        }
      }
      A4ELogging.debug("File " + file + " is not an eclipse project directory and will be ignored.");
      return;
    }
    // Everything is fine, we add this directory!
    A4ELogging.log(getLogLevelForProjectLocation(), "Add project for location %s", file);
    projectDirectories.add(file);
  }

  /**
   * Returns the level for logging a project location.
   * 
   * @return a log level as {@link A4ELevel}
   */
  protected A4ELevel getLogLevelForProjectLocation() {
    PropertyService properties = ServiceRegistryAccess.instance().getService(PropertyService.class);
    return A4ELevel.parse(properties.getProperty("ant4eclipse.projectLocation.logLevel"), A4ELevel.INFO);
  }

  /**
   * Checks if this file is a valid eclipse project.
   * 
   * @param file
   * @return
   */
  protected boolean isEclipseProject(File file) {
    return ProjectFileParser.isProjectDirectory(file);
  }

  /**
   * Chechs if the given argument is a valid directory in terms of file system view
   * 
   * @param file
   * @return
   */
  protected boolean isValidDirectory(File file) {
    // make sure it exists
    if (!file.exists()) {
      A4ELogging.debug("File " + file + " does not exist and will be ignored.");
      return false;
    }
    // make sure its a directory
    if (!file.isDirectory()) {
      A4ELogging.debug("File " + file + " is not a directory and will be ignored.");
      return false;
    }
    // and not a "excluded" one
    String[] excludes = DirectoryScanner.getDefaultExcludes();
    String fileName = file.toString();
    for (String exclude : excludes) {
      if (DirectoryScanner.match(exclude, fileName)) {
        A4ELogging.debug("File " + file + " is an excluded directory and will be ignored.");
        return false;
      }
    }
    return true;
  }
}
