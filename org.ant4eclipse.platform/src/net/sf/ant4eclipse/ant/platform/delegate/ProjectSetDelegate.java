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
package net.sf.ant4eclipse.ant.platform.delegate;

import java.io.File;

import net.sf.ant4eclipse.model.platform.team.projectset.TeamProjectSet;
import net.sf.ant4eclipse.model.platform.team.projectset.TeamProjectSetFileParser;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.ProjectComponent;

/**
 * Base class for all tasks working with project sets
 * 
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 */
public class ProjectSetDelegate extends WorkspaceDelegate {
  /**  */
  private File           _projectSetFile;

  /**  */
  private TeamProjectSet _projectSet;

  /**
   * Creates a new instance of type ProjectSetBase
   * 
   * @param component
   */
  public ProjectSetDelegate(final ProjectComponent component) {
    super(component);
  }

  /**
   * @return Returns the psfFileName.
   */
  public final TeamProjectSet getProjectSet() {
    if (this._projectSet == null) {
      this._projectSet = readProjectSet();
    }

    return this._projectSet;
  }

  /**
   * @param projectSet
   *          The psfFileName to set.
   */
  public final void setProjectSet(final File projectSet) {
    this._projectSetFile = projectSet;
  }

  /**
   * Returns true if the project set has been set.
   * 
   * @return true <=> The project set has been set.
   */
  public final boolean isProjectSetSet() {
    return this._projectSetFile != null;
  }

  /**
   * 
   */
  public final void requireProjectSetSet() {
    if (!isProjectSetSet()) {
      throw new BuildException("projectSet has to be set!");
    }
  }

  /**
   * Reads and parses the projectset for this task. The project set to load is determindes by the
   * {@link #setProjectSet(File)}project set name.
   * 
   * @precondition Name of projectset file has to be set before.
   * 
   * @return Parsed projectset
   */
  private TeamProjectSet readProjectSet() {
    requireProjectSetSet();
    requireWorkspaceSet();
    return TeamProjectSetFileParser.Helper.getInstance().parseTeamProjectSetFile(this._projectSetFile);
  }
}