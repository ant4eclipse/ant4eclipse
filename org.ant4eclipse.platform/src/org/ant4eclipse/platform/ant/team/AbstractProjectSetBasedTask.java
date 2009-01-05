/**********************************************************************
 * Copyright (c) 2005-2008 ant4eclipse project team.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Nils Hartmann, Daniel Kasmeroglu, Gerd Wuetherich
 **********************************************************************/
package org.ant4eclipse.platform.ant.team;

import java.io.File;

import org.ant4eclipse.core.ant.AbstractAnt4EclipseTask;
import org.ant4eclipse.core.exception.Ant4EclipseException;
import org.ant4eclipse.platform.ant.delegate.ProjectSetDelegate;
import org.ant4eclipse.platform.model.resource.Workspace;
import org.ant4eclipse.platform.model.team.projectset.TeamProjectSet;

/**
 * Base class for all tasks working with project sets
 * 
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 */
public abstract class AbstractProjectSetBasedTask extends AbstractAnt4EclipseTask {

  private final ProjectSetDelegate _projectSetBase;

  /**
   * 
   */
  public AbstractProjectSetBasedTask() {
    this._projectSetBase = new ProjectSetDelegate(this);
  }

  /**
   * Returns the ProjectSetBase which allows to do configurations related to a project set.
   * 
   * @return The ProjectSetBase instance.
   */
  protected ProjectSetDelegate getProjectSetBase() {
    return (this._projectSetBase);
  }

  /**
   * Returns the TeamProjectSet instance associated with this task.
   * 
   * @return The TeamProjectSet instance associated with this task.
   * 
   * @throws Ant4EclipseException
   *           Reading the data failed for some reason.
   */
  public TeamProjectSet getProjectSet() {
    return this._projectSetBase.getProjectSet();
  }

  /**
   * Returns the Workspace instance associated with this task.
   * 
   * @return The Workspace instance associated with this task.
   */
  public Workspace getWorkspace() {
    return this._projectSetBase.getWorkspace();
  }

  /**
   * Returns true if the project set has been set.
   * 
   * @return true <=> The project set has been set.
   */
  public boolean isProjectSetSet() {
    return this._projectSetBase.isProjectSetSet();
  }

  /**
   * Returns true if the Workspace has been set.
   * 
   * @return true <=> The Workspace has been set.
   */
  public boolean isWorkspaceSet() {
    return this._projectSetBase.isWorkspaceSet();
  }

  /**
   * 
   */
  public void requireProjectSetSet() {
    this._projectSetBase.requireProjectSetSet();
  }

  /**
   * 
   */
  public void requireWorkspaceSet() {
    this._projectSetBase.requireWorkspaceSet();
  }

  /**
   * @param projectSet
   */
  public void setProjectSet(final File projectSet) {
    this._projectSetBase.setProjectSet(projectSet);
  }

  /**
   * @param workspace
   */
  public void setWorkspace(final File workspace) {
    this._projectSetBase.setWorkspace(workspace);
  }
}