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
package org.ant4eclipse.ant.platform.core.delegate;

import org.ant4eclipse.ant.platform.core.ProjectSetComponent;
import org.ant4eclipse.ant.platform.core.WorkspaceComponent;
import org.ant4eclipse.ant.platform.core.WorkspaceProjectSetComponent;
import org.ant4eclipse.lib.platform.model.resource.EclipseProject;
import org.ant4eclipse.lib.platform.model.resource.Workspace;
import org.ant4eclipse.lib.platform.model.team.projectset.TeamProjectSet;
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
  @Override
  public boolean isAllWorkspaceProjects() {
    return this._allWorkspaceProjects;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void requireAllWorkspaceProjectsOrProjectSetOrProjectNamesSet() {
    if (!this._allWorkspaceProjects && !isProjectNamesSet() && !isTeamProjectSetSet()) {
      // TODO
      throw new BuildException("allWorkspaceProjects or projectNames or teamProjectSet has to be set!");
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setAllWorkspaceProjects(boolean allprojects) {
    this._allWorkspaceProjects = allprojects;
  }

  /**
   * {@inheritDoc}
   */
  @Override
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
  @Override
  public TeamProjectSet getTeamProjectSet() {
    return this._projectSetComponent.getTeamProjectSet();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isProjectNamesSet() {
    return this._projectSetComponent.isProjectNamesSet();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isTeamProjectSetSet() {
    return this._projectSetComponent.isTeamProjectSetSet();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void requireProjectNamesSet() {
    this._projectSetComponent.requireProjectNamesSet();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void requireTeamProjectSetOrProjectNamesSet() {
    this._projectSetComponent.requireTeamProjectSetOrProjectNamesSet();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void requireTeamProjectSetSet() {
    this._projectSetComponent.requireTeamProjectSetSet();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setProjectNames(String projectNames) {
    this._projectSetComponent.setProjectNames(projectNames);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setTeamProjectSet(File projectSetFile) {
    this._projectSetComponent.setTeamProjectSet(projectSetFile);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Workspace getWorkspace() {
    return this._workspaceComponent.getWorkspace();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public File getWorkspaceDirectory() {
    return this._workspaceComponent.getWorkspaceDirectory();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isWorkspaceDirectorySet() {
    return this._workspaceComponent.isWorkspaceDirectorySet();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  @Deprecated
  public void setWorkspace(String workspace) {
    this._workspaceComponent.setWorkspace(workspace);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setWorkspaceDirectory(String workspaceDirectory) {
    this._workspaceComponent.setWorkspaceDirectory(workspaceDirectory);
  }

  @Override
  public String getWorkspaceId() {
    return this._workspaceComponent.getWorkspaceId();
  }

  @Override
  public boolean isWorkspaceIdSet() {
    return this._workspaceComponent.isWorkspaceIdSet();
  }

  @Override
  public void requireWorkspaceDirectoryOrWorkspaceIdSet() {
    this._workspaceComponent.requireWorkspaceDirectoryOrWorkspaceIdSet();
  }

  @Override
  public void setWorkspaceId(String identifier) {
    this._workspaceComponent.setWorkspaceId(identifier);
  }

}
