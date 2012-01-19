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
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * Default implementation of the interface {@link WorkspaceProjectSetComponent}.
 * </p>
 * 
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 */
public class WorkspaceProjectSetDelegate implements WorkspaceProjectSetComponent {

  private ProjectSetComponent _projectSetComponent;
  private WorkspaceComponent  _workspaceComponent;
  private boolean             _allWorkspaceProjects;

  /**
   * <p>
   * Creates a new instance of type {@link WorkspaceProjectSetDelegate}.
   * </p>
   * 
   * @param component
   */
  public WorkspaceProjectSetDelegate( ProjectComponent component ) {
    _projectSetComponent = new ProjectSetDelegate( component );
    _workspaceComponent = new WorkspaceDelegate( component );
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isAllWorkspaceProjects() {
    return _allWorkspaceProjects;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void requireAllWorkspaceProjectsOrProjectSetOrProjectNamesSet() {
    if( !_allWorkspaceProjects && !isProjectNamesSet() && !isTeamProjectSetSet() ) {
      throw new BuildException( "allWorkspaceProjects or projectNames or teamProjectSet has to be set!" );
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setAllWorkspaceProjects( boolean allprojects ) {
    _allWorkspaceProjects = allprojects;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public List<String> getProjectNames() {
    // if 'allProjects' return all workspace projects
    if( _allWorkspaceProjects ) {
      // get workspace projects
      List<EclipseProject> projects = getWorkspace().getAllProjects();
      List<String>         projectNames = new ArrayList<String>();
      for( int i = 0; i < projects.size(); i++ ) {
        projectNames.add( projects.get(i).getSpecifiedName() );
      }
      return projectNames;
    } else {
      return _projectSetComponent.getProjectNames();
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public TeamProjectSet getTeamProjectSet() {
    return _projectSetComponent.getTeamProjectSet();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isProjectNamesSet() {
    return _projectSetComponent.isProjectNamesSet();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isTeamProjectSetSet() {
    return _projectSetComponent.isTeamProjectSetSet();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void requireProjectNamesSet() {
    _projectSetComponent.requireProjectNamesSet();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void requireTeamProjectSetOrProjectNamesSet() {
    _projectSetComponent.requireTeamProjectSetOrProjectNamesSet();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void requireTeamProjectSetSet() {
    _projectSetComponent.requireTeamProjectSetSet();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setProjectNames( String projectNames ) {
    _projectSetComponent.setProjectNames( projectNames );
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setTeamProjectSet( File projectSetFile ) {
    _projectSetComponent.setTeamProjectSet( projectSetFile );
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Workspace getWorkspace() {
    return _workspaceComponent.getWorkspace();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public File getWorkspaceDirectory() {
    return _workspaceComponent.getWorkspaceDirectory();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isWorkspaceDirectorySet() {
    return _workspaceComponent.isWorkspaceDirectorySet();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setWorkspaceDirectory( String workspaceDirectory ) {
    _workspaceComponent.setWorkspaceDirectory( workspaceDirectory );
  }

  @Override
  public String getWorkspaceId() {
    return _workspaceComponent.getWorkspaceId();
  }

  @Override
  public boolean isWorkspaceIdSet() {
    return _workspaceComponent.isWorkspaceIdSet();
  }

  @Override
  public void requireWorkspaceDirectoryOrWorkspaceIdSet() {
    _workspaceComponent.requireWorkspaceDirectoryOrWorkspaceIdSet();
  }

  @Override
  public void setWorkspaceId( String identifier ) {
    _workspaceComponent.setWorkspaceId( identifier );
  }

} /* ENDCLASS */
