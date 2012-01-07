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

    // create the delegates
    this._workspaceProjectSetDelegate = new WorkspaceProjectSetDelegate( this );
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String[] getProjectNames() {
    return this._workspaceProjectSetDelegate.getProjectNames();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public TeamProjectSet getTeamProjectSet() {
    return this._workspaceProjectSetDelegate.getTeamProjectSet();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Workspace getWorkspace() {
    return this._workspaceProjectSetDelegate.getWorkspace();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public File getWorkspaceDirectory() {
    return this._workspaceProjectSetDelegate.getWorkspaceDirectory();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isAllWorkspaceProjects() {
    return this._workspaceProjectSetDelegate.isAllWorkspaceProjects();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isProjectNamesSet() {
    return this._workspaceProjectSetDelegate.isProjectNamesSet();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isTeamProjectSetSet() {
    return this._workspaceProjectSetDelegate.isTeamProjectSetSet();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isWorkspaceDirectorySet() {
    return this._workspaceProjectSetDelegate.isWorkspaceDirectorySet();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void requireAllWorkspaceProjectsOrProjectSetOrProjectNamesSet() {
    this._workspaceProjectSetDelegate.requireAllWorkspaceProjectsOrProjectSetOrProjectNamesSet();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void requireProjectNamesSet() {
    this._workspaceProjectSetDelegate.requireProjectNamesSet();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void requireTeamProjectSetOrProjectNamesSet() {
    this._workspaceProjectSetDelegate.requireTeamProjectSetOrProjectNamesSet();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void requireTeamProjectSetSet() {
    this._workspaceProjectSetDelegate.requireTeamProjectSetSet();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getWorkspaceId() {
    return this._workspaceProjectSetDelegate.getWorkspaceId();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isWorkspaceIdSet() {
    return this._workspaceProjectSetDelegate.isWorkspaceIdSet();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void requireWorkspaceDirectoryOrWorkspaceIdSet() {
    this._workspaceProjectSetDelegate.requireWorkspaceDirectoryOrWorkspaceIdSet();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setWorkspaceId( String identifier ) {
    this._workspaceProjectSetDelegate.setWorkspaceId( identifier );
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setAllWorkspaceProjects( boolean allprojects ) {
    this._workspaceProjectSetDelegate.setAllWorkspaceProjects( allprojects );
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setProjectNames( String projectNames ) {
    this._workspaceProjectSetDelegate.setProjectNames( projectNames );
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setTeamProjectSet( File projectSetFile ) {
    this._workspaceProjectSetDelegate.setTeamProjectSet( projectSetFile );
  }

  /**
   * {@inheritDoc}
   */
  @Override
  @Deprecated
  public void setWorkspace( String workspace ) {
    this._workspaceProjectSetDelegate.setWorkspace( workspace );
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setWorkspaceDirectory( String workspaceDirectory ) {
    this._workspaceProjectSetDelegate.setWorkspaceDirectory( workspaceDirectory );
  }

} /* ENDCLASS */
