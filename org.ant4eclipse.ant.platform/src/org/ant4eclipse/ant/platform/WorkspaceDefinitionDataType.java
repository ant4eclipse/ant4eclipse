package org.ant4eclipse.ant.platform;

import org.ant4eclipse.ant.core.AbstractAnt4EclipseDataType;
import org.ant4eclipse.lib.core.A4ECore;
import org.ant4eclipse.lib.core.logging.A4ELogging;
import org.ant4eclipse.lib.platform.internal.model.resource.workspaceregistry.ProjectFileParser;
import org.ant4eclipse.lib.platform.model.resource.workspaceregistry.FilesetWorkspaceDefinition;
import org.ant4eclipse.lib.platform.model.resource.workspaceregistry.WorkspaceRegistry;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.types.DirSet;

import java.io.File;
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
      File fsBase = fs.getDirectoryScanner().getBasedir();
      for (String d : fs.getDirectoryScanner().getIncludedDirectories()) {
        File file = new File(fsBase, d);

        // make sure it exists
        if (!file.exists()) {
          A4ELogging.debug("File " + file + " does not exist and will be ignored.");
          continue;
        }

        // make sure its a directory
        if (!file.isDirectory()) {
          A4ELogging.debug("File " + file + " is not a directory and will be ignored.");
          continue;
        }

        // and it should have a .project file
        if (!ProjectFileParser.isProjectDirectory(file)) {
          A4ELogging.debug("File " + file + " is not an eclipse project directory and will be ignored.");
          continue;
        }

        projectDirectories.add(file);
      }
    }

    WorkspaceRegistry registry = A4ECore.instance().getRequiredService( WorkspaceRegistry.class );
    registry.registerWorkspace(this._id, new FilesetWorkspaceDefinition(projectDirectories
        .toArray(new File[projectDirectories.size()])));
  }
}
