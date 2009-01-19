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
package org.ant4eclipse.platform.ant.delegate;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.ProjectComponent;

/**
 * 
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 */
public class ProjectSetDelegate extends TeamProjectSetDelegate {

  private String[] _projectNames;

  /**
   * Creates a new instance of type ProjectSetBase
   * 
   * @param component
   */
  public ProjectSetDelegate(final ProjectComponent component) {
    super(component);
  }

  public final void setProjectNames(String projectNames) {

    //
    if (projectNames == null) {
      this._projectNames = new String[] {};
    } else {
      String[] names = projectNames.split(",");

      // 
      this._projectNames = new String[names.length];

      for (int i = 0; i < names.length; i++) {
        this._projectNames[i] = names[i].trim();
      }
    }
  }

  public final String[] getProjectNames() {
    return this._projectNames;
  }

  public final boolean isProjectNamesSet() {
    return this._projectNames != null;
  }

  public final void requireProjectNamesSet() {
    if (!isProjectNamesSet()) {
      // TODO
      throw new BuildException("projectNames has to be set!");
    }
  }

  public final void requireTeamProjectSetOrProjectNamesSet() {
    if (!isProjectNamesSet() && !isTeamProjectSetSet()) {
      // TODO
      throw new BuildException("projectNames or teamProjectSet has to be set!");
    }
  }
}