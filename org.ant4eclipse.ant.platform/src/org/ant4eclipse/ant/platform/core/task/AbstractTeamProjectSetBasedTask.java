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
import org.ant4eclipse.ant.platform.core.TeamProjectSetComponent;
import org.ant4eclipse.ant.platform.core.delegate.TeamProjectSetDelegate;
import org.ant4eclipse.lib.platform.model.team.projectset.TeamProjectSet;

import java.io.File;

/**
 * <p>
 * Base class for all tasks working with project sets
 * </p>
 * 
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 */
public abstract class AbstractTeamProjectSetBasedTask extends AbstractAnt4EclipseTask implements
    TeamProjectSetComponent {

  /** the project set delegate */
  private TeamProjectSetDelegate _projectSetDelegate;

  /**
   *
   */
  public AbstractTeamProjectSetBasedTask() {
    this._projectSetDelegate = new TeamProjectSetDelegate(this);
  }

  /**
   * {@inheritDoc}
   */
  public TeamProjectSet getProjectSet() {
    return this._projectSetDelegate.getTeamProjectSet();
  }

  /**
   * {@inheritDoc}
   */
  public boolean isProjectSetSet() {
    return this._projectSetDelegate.isTeamProjectSetSet();
  }

  /**
   * {@inheritDoc}
   */
  public void requireProjectSetSet() {
    this._projectSetDelegate.requireTeamProjectSetSet();
  }

  /**
   * {@inheritDoc}
   */
  public void setProjectSet(File projectSet) {
    this._projectSetDelegate.setTeamProjectSet(projectSet);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public final TeamProjectSet getTeamProjectSet() {
    return this._projectSetDelegate.getTeamProjectSet();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public final boolean isTeamProjectSetSet() {
    return this._projectSetDelegate.isTeamProjectSetSet();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public final void requireTeamProjectSetSet() {
    this._projectSetDelegate.requireTeamProjectSetSet();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public final void setTeamProjectSet(File projectSet) {
    this._projectSetDelegate.setTeamProjectSet(projectSet);
  }
}