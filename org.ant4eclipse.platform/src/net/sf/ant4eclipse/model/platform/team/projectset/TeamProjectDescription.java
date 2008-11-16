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
package net.sf.ant4eclipse.model.platform.team.projectset;

import net.sf.ant4eclipse.core.Assert;


/**
 * <p>
 * Implements a description of a project to be shared in a team. The description contains the name, the
 * repository-location and the branch/version tag of the project. The TeamProjectDescription is read form an eclipse
 * .psf-file.
 * </p>
 * 
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 */
public class TeamProjectDescription {

  /** the name of the project */
  private String  _projectname;

  /**
   * Returns the name of the project.
   * 
   * @return Returns the name of the project.
   */
  public String getProjectName() {
    return _projectname;
  }

  public TeamProjectDescription(String projectname) {
    Assert.notNull(projectname);
    _projectname = projectname;
  }

  /**
   * Override hashCode.
   *
   * @return the Objects hashcode.
   */
  public int hashCode() {
    int hashCode = 1;
    hashCode = 31 * hashCode + (_projectname == null ? 0 : _projectname.hashCode());
    return hashCode;
  }

  /**
   * Returns <code>true</code> if this <code>TeamProjectDescription</code> is the same as the o argument.
   *
   * @return <code>true</code> if this <code>TeamProjectDescription</code> is the same as the o argument.
   */
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null) {
      return false;
    }
    if (o.getClass() != getClass()) {
      return false;
    }
    TeamProjectDescription castedObj = (TeamProjectDescription) o;
    return ((this._projectname == null ? castedObj._projectname == null : this._projectname
      .equals(castedObj._projectname)));
  }
}
