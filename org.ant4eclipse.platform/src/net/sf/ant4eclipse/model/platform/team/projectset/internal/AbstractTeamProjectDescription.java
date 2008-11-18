/**********************************************************************
 * Copyright (c) 2005-2006 ant4eclipse project team.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Nils Hartmann, Daniel Kasmeroglu, Gerd Wuetherich
 **********************************************************************/
package net.sf.ant4eclipse.model.platform.team.projectset.internal;

import org.ant4eclipse.model.platform.team.projectset.TeamProjectDescription;

import net.sf.ant4eclipse.core.Assert;

public class AbstractTeamProjectDescription implements TeamProjectDescription {
  /** the name of the project */
  private String _projectname;

  /**
   * Returns the name of the project.
   * 
   * @return Returns the name of the project.
   */
  public String getProjectName() {
    return _projectname;
  }

  public AbstractTeamProjectDescription(String projectname) {
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
   * Returns <code>true</code> if this <code>AbstractTeamProjectDescription</code> is the same as the o argument.
   * 
   * @return <code>true</code> if this <code>AbstractTeamProjectDescription</code> is the same as the o argument.
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
    AbstractTeamProjectDescription castedObj = (AbstractTeamProjectDescription) o;
    return ((this._projectname == null ? castedObj._projectname == null : this._projectname
        .equals(castedObj._projectname)));
  }
}
