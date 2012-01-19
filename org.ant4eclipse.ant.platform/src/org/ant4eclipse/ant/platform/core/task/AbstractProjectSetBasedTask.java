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
package org.ant4eclipse.ant.platform.core.task;

import org.ant4eclipse.ant.core.AbstractAnt4EclipseTask;
import org.ant4eclipse.ant.platform.core.WorkspaceProjectSetComponent;
import org.ant4eclipse.ant.platform.core.delegate.WorkspaceProjectSetDelegate;
import org.ant4eclipse.lib.platform.model.resource.Workspace;
import org.ant4eclipse.lib.platform.model.team.projectset.TeamProjectSet;

import java.io.File;
import java.util.List;

/**
 * <p>
 * Abstract base class for project set based tasks.
 * </p>
 * 
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 */
public abstract class AbstractProjectSetBasedTask extends AbstractAnt4EclipseTask implements
    WorkspaceProjectSetComponent {

  /** the workspace project set delegate */
  private WorkspaceProjectSetDelegate _workspaceProjectSetDelegate;

  /**
   * <p>
   * Create a new instance of type {@link AbstractProjectSetBasedTask}.
   * </p>
   */
  public AbstractProjectSetBasedTask() {
    _workspaceProjectSetDelegate = new WorkspaceProjectSetDelegate( this );
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public List<String> getProjectNames() {
    return _workspaceProjectSetDelegate.getProjectNames();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public TeamProjectSet getTeamProjectSet() {
    return _workspaceProjectSetDelegate.getTeamProjectSet();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Workspace getWorkspace() {
    return _workspaceProjectSetDelegate.getWorkspace();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public File getWorkspaceDirectory() {
    return _workspaceProjectSetDelegate.getWorkspaceDirectory();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isAllWorkspaceProjects() {
    return _workspaceProjectSetDelegate.isAllWorkspaceProjects();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isProjectNamesSet() {
    return _workspaceProjectSetDelegate.isProjectNamesSet();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isTeamProjectSetSet() {
    return _workspaceProjectSetDelegate.isTeamProjectSetSet();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isWorkspaceDirectorySet() {
    return _workspaceProjectSetDelegate.isWorkspaceDirectorySet();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void requireAllWorkspaceProjectsOrProjectSetOrProjectNamesSet() {
    _workspaceProjectSetDelegate.requireAllWorkspaceProjectsOrProjectSetOrProjectNamesSet();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void requireProjectNamesSet() {
    _workspaceProjectSetDelegate.requireProjectNamesSet();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void requireTeamProjectSetOrProjectNamesSet() {
    _workspaceProjectSetDelegate.requireTeamProjectSetOrProjectNamesSet();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void requireTeamProjectSetSet() {
    _workspaceProjectSetDelegate.requireTeamProjectSetSet();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getWorkspaceId() {
    return _workspaceProjectSetDelegate.getWorkspaceId();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isWorkspaceIdSet() {
    return _workspaceProjectSetDelegate.isWorkspaceIdSet();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void requireWorkspaceDirectoryOrWorkspaceIdSet() {
    _workspaceProjectSetDelegate.requireWorkspaceDirectoryOrWorkspaceIdSet();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setWorkspaceId( String identifier ) {
    _workspaceProjectSetDelegate.setWorkspaceId( identifier );
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setAllWorkspaceProjects( boolean allprojects ) {
    _workspaceProjectSetDelegate.setAllWorkspaceProjects( allprojects );
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setProjectNames( String projectNames ) {
    _workspaceProjectSetDelegate.setProjectNames( projectNames );
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setTeamProjectSet( File projectSetFile ) {
    _workspaceProjectSetDelegate.setTeamProjectSet( projectSetFile );
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setWorkspaceDirectory( String workspaceDirectory ) {
    _workspaceProjectSetDelegate.setWorkspaceDirectory( workspaceDirectory );
  }

} /* ENDCLASS */
