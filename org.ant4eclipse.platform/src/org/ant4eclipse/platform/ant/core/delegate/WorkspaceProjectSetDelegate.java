package org.ant4eclipse.platform.ant.core.delegate;

import org.ant4eclipse.platform.ant.core.ProjectSetComponent;
import org.ant4eclipse.platform.ant.core.WorkspaceComponent;
import org.ant4eclipse.platform.ant.core.WorkspaceProjectSetComponent;
import org.ant4eclipse.platform.model.resource.EclipseProject;
import org.ant4eclipse.platform.model.resource.Workspace;
import org.ant4eclipse.platform.model.team.projectset.TeamProjectSet;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.ProjectComponent;

import java.io.File;

public class WorkspaceProjectSetDelegate implements WorkspaceProjectSetComponent {

  /** - */
  private final ProjectSetComponent _projectSetComponent;

  /** - */
  private final WorkspaceComponent  _workspaceComponent;

  /** - */
  private boolean                   _allProjects;

  /**
   * <p>
   * Creates a new instance of type WorkspaceProjectSetDelegate.
   * </p>
   * 
   * @param component
   */
  public WorkspaceProjectSetDelegate(ProjectComponent component) {
    this._projectSetComponent = new ProjectSetDelegate(component);
    this._workspaceComponent = new WorkspaceDelegate(component);
  }

  /**
   * {@inheritDoc}
   */
  public boolean isAllProjects() {
    return this._allProjects;
  }

  /**
   * {@inheritDoc}
   */
  public void requireAllProjectsOrProjectSetOrProjectNamesSet() {
    if (!this._allProjects && !isProjectNamesSet() && !isTeamProjectSetSet()) {
      // TODO
      throw new BuildException("allProjects or projectNames or teamProjectSet has to be set!");
    }
  }

  /**
   * {@inheritDoc}
   */
  public void setAllProjects(boolean allprojects) {
    this._allProjects = allprojects;
  }

  /**
   * {@inheritDoc}
   */
  public String[] getProjectNames() {

    // if 'allProjects' return all workspace projects
    if (this._allProjects) {
      // get workspace projects
      EclipseProject[] projects = getWorkspace().getAllProjects();
      // create project names array
      String[] projectNames = new String[projects.length];
      // set project names
      for (int i = 0; i < projects.length; i++) {
        EclipseProject project = projects[i];
        projectNames[i] = project.getSpecifiedName();
      }
      return projectNames;
    }
    // return project set projects
    else {
      return this._projectSetComponent.getProjectNames();
    }
  }

  /**
   * {@inheritDoc}
   */
  public TeamProjectSet getTeamProjectSet() {
    return this._projectSetComponent.getTeamProjectSet();
  }

  /**
   * {@inheritDoc}
   */
  public boolean isProjectNamesSet() {
    return this._projectSetComponent.isProjectNamesSet();
  }

  /**
   * {@inheritDoc}
   */
  public boolean isTeamProjectSetSet() {
    return this._projectSetComponent.isTeamProjectSetSet();
  }

  /**
   * {@inheritDoc}
   */
  public void requireProjectNamesSet() {
    this._projectSetComponent.requireProjectNamesSet();
  }

  /**
   * {@inheritDoc}
   */
  public void requireTeamProjectSetOrProjectNamesSet() {
    this._projectSetComponent.requireTeamProjectSetOrProjectNamesSet();
  }

  /**
   * {@inheritDoc}
   */
  public void requireTeamProjectSetSet() {
    this._projectSetComponent.requireTeamProjectSetSet();
  }

  /**
   * {@inheritDoc}
   */
  public void setProjectNames(String projectNames) {
    this._projectSetComponent.setProjectNames(projectNames);
  }

  /**
   * {@inheritDoc}
   */
  public void setTeamProjectSet(File projectSetFile) {
    this._projectSetComponent.setTeamProjectSet(projectSetFile);
  }

  /**
   * {@inheritDoc}
   */
  public Workspace getWorkspace() {
    return this._workspaceComponent.getWorkspace();
  }

  /**
   * {@inheritDoc}
   */
  public File getWorkspaceDirectory() {
    return this._workspaceComponent.getWorkspaceDirectory();
  }

  /**
   * {@inheritDoc}
   */
  public boolean isWorkspaceDirectorySet() {
    return this._workspaceComponent.isWorkspaceDirectorySet();
  }

  /**
   * {@inheritDoc}
   */
  public void requireWorkspaceDirectorySet() {
    this._workspaceComponent.requireWorkspaceDirectorySet();
  }

  /**
   * {@inheritDoc}
   */
  @Deprecated
  public void setWorkspace(File workspace) {
    this._workspaceComponent.setWorkspace(workspace);
  }

  /**
   * {@inheritDoc}
   */
  public void setWorkspaceDirectory(File workspaceDirectory) {
    this._workspaceComponent.setWorkspaceDirectory(workspaceDirectory);
  }

}
