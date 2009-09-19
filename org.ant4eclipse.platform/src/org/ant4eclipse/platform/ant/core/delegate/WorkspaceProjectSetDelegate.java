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

/**
 * <p>
 * Default implementation of the interface {@link WorkspaceProjectSetComponent}.
 * </p>
 * 
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 */
public class WorkspaceProjectSetDelegate implements WorkspaceProjectSetComponent {

  /** - */
  private ProjectSetComponent _projectSetComponent;

  /** - */
  private WorkspaceComponent  _workspaceComponent;

  /** - */
  private boolean             _allWorkspaceProjects;

  /**
   * <p>
   * Creates a new instance of type {@link WorkspaceProjectSetDelegate}.
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
  public boolean isAllWorkspaceProjects() {
    return this._allWorkspaceProjects;
  }

  /**
   * {@inheritDoc}
   */
  public void requireAllWorkspaceProjectsOrProjectSetOrProjectNamesSet() {
    if (!this._allWorkspaceProjects && !isProjectNamesSet() && !isTeamProjectSetSet()) {
      // TODO
      throw new BuildException("allWorkspaceProjects or projectNames or teamProjectSet has to be set!");
    }
  }

  /**
   * {@inheritDoc}
   */
  public void setAllWorkspaceProjects(boolean allprojects) {
    this._allWorkspaceProjects = allprojects;
  }

  /**
   * {@inheritDoc}
   */
  public String[] getProjectNames() {

    // if 'allProjects' return all workspace projects
    if (this._allWorkspaceProjects) {
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
