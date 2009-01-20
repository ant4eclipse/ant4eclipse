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
package org.ant4eclipse.platform.ant.core.task;

import java.io.File;

import org.ant4eclipse.core.ant.AbstractAnt4EclipseTask;
import org.ant4eclipse.core.exception.Ant4EclipseException;
import org.ant4eclipse.platform.ant.core.TeamProjectSetComponent;
import org.ant4eclipse.platform.ant.core.delegate.TeamProjectSetDelegate;
import org.ant4eclipse.platform.model.team.projectset.TeamProjectSet;

/**
 * Base class for all tasks working with project sets
 * 
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 */
public abstract class AbstractTeamProjectSetBasedTask extends AbstractAnt4EclipseTask implements
    TeamProjectSetComponent {

  private final TeamProjectSetDelegate _projectSetBase;

  /**
   * 
   */
  public AbstractTeamProjectSetBasedTask() {
    this._projectSetBase = new TeamProjectSetDelegate(this);
  }

  /**
   * Returns the ProjectSetBase which allows to do configurations related to a project set.
   * 
   * @return The ProjectSetBase instance.
   */
  protected TeamProjectSetDelegate getProjectSetBase() {
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
    return this._projectSetBase.getTeamProjectSet();
  }

  /**
   * Returns true if the project set has been set.
   * 
   * @return true <=> The project set has been set.
   */
  public boolean isProjectSetSet() {
    return this._projectSetBase.isTeamProjectSetSet();
  }

  /**
   * 
   */
  public void requireProjectSetSet() {
    this._projectSetBase.requireTeamProjectSetSet();
  }

  /**
   * @param projectSet
   */
  public void setProjectSet(final File projectSet) {
    this._projectSetBase.setTeamProjectSet(projectSet);
  }

  public final TeamProjectSet getTeamProjectSet() {
    return this._projectSetBase.getTeamProjectSet();
  }

  public final boolean isTeamProjectSetSet() {
    return this._projectSetBase.isTeamProjectSetSet();
  }

  public final void requireTeamProjectSetSet() {
    this._projectSetBase.requireTeamProjectSetSet();
  }

  public final void setTeamProjectSet(File projectSet) {
    this._projectSetBase.setTeamProjectSet(projectSet);
  }
}