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
package org.ant4eclipse.platform.ant.core.delegate;

import java.io.File;

import org.ant4eclipse.core.ant.delegate.AbstractAntDelegate;
import org.ant4eclipse.platform.ant.core.TeamProjectSetComponent;
import org.ant4eclipse.platform.model.team.projectset.TeamProjectSet;
import org.ant4eclipse.platform.model.team.projectset.TeamProjectSetFileParser;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.ProjectComponent;

/**
 * Base class for all tasks working with project sets
 * 
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 */
public class TeamProjectSetDelegate extends AbstractAntDelegate implements TeamProjectSetComponent {
  /**  */
  private File           _teamProjectSetFile;

  /**  */
  private TeamProjectSet _teamProjectSet;

  /**
   * Creates a new instance of type ProjectSetBase
   * 
   * @param component
   */
  public TeamProjectSetDelegate(final ProjectComponent component) {
    super(component);
  }

  /**
   * @return Returns the psfFileName.
   */
  public final TeamProjectSet getTeamProjectSet() {
    if (this._teamProjectSet == null) {
      this._teamProjectSet = readTeamProjectSet();
    }

    return this._teamProjectSet;
  }

  /**
   * @param projectSet
   *          The psfFileName to set.
   */
  public final void setTeamProjectSet(final File projectSet) {
    this._teamProjectSetFile = projectSet;
  }

  /**
   * Returns true if the project set has been set.
   * 
   * @return true <=> The project set has been set.
   */
  public final boolean isTeamProjectSetSet() {
    return this._teamProjectSetFile != null;
  }

  /**
   * 
   */
  public final void requireTeamProjectSetSet() {
    if (!isTeamProjectSetSet()) {
      throw new BuildException("projectSet has to be set!");
    }
  }

  /**
   * Reads and parses the projectset for this task. The project set to load is determindes by the
   * {@link #setTeamProjectSet(File)}project set name.
   * 
   * @precondition Name of projectset file has to be set before.
   * 
   * @return Parsed projectset
   */
  private TeamProjectSet readTeamProjectSet() {
    requireTeamProjectSetSet();
    return TeamProjectSetFileParser.Helper.getInstance().parseTeamProjectSetFile(this._teamProjectSetFile);
  }
}