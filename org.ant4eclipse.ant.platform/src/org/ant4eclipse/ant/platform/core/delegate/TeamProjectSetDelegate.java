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

import org.ant4eclipse.ant.core.delegate.AbstractAntDelegate;
import org.ant4eclipse.ant.platform.core.TeamProjectSetComponent;
import org.ant4eclipse.lib.core.service.ServiceRegistryAccess;
import org.ant4eclipse.lib.platform.model.team.projectset.TeamProjectSet;
import org.ant4eclipse.lib.platform.model.team.projectset.TeamProjectSetFileParser;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.ProjectComponent;

import java.io.File;

/**
 * <p>
 * Delegate class for all tasks working with project sets
 * </p>
 * 
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 */
public class TeamProjectSetDelegate extends AbstractAntDelegate implements TeamProjectSetComponent {

  /** the team project set file */
  private File           _teamProjectSetFile;

  /** the team project set */
  private TeamProjectSet _teamProjectSet;

  /**
   * <p>
   * Creates a new instance of type ProjectSetBase
   * </p>
   * 
   * @param component
   */
  public TeamProjectSetDelegate(ProjectComponent component) {
    super(component);
  }

  /**
   * {@inheritDoc}
   */
  public final TeamProjectSet getTeamProjectSet() {
    if (this._teamProjectSet == null) {
      this._teamProjectSet = readTeamProjectSet();
    }

    return this._teamProjectSet;
  }

  /**
   * {@inheritDoc}
   */
  public final void setTeamProjectSet(File projectSet) {
    this._teamProjectSetFile = projectSet;
  }

  /**
   * {@inheritDoc}
   */
  public final boolean isTeamProjectSetSet() {
    return this._teamProjectSetFile != null;
  }

  /**
   * {@inheritDoc}
   */
  public final void requireTeamProjectSetSet() {
    if (!isTeamProjectSetSet()) {
      throw new BuildException("projectSet has to be set!");
    }
  }

  /**
   * <p>
   * Reads and parses the project set for this delegate. The project set to load is determines by the
   * {@link #setTeamProjectSet(File)} project set name.
   * </p>
   * 
   * @precondition Name of project set file has to be set before.
   * 
   * @return the {@link TeamProjectSet}
   */
  private TeamProjectSet readTeamProjectSet() {
    requireTeamProjectSetSet();
    return ServiceRegistryAccess.instance().getService(TeamProjectSetFileParser.class).parseTeamProjectSetFile(
        this._teamProjectSetFile);
  }
}