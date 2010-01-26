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
package org.ant4eclipse.platform.model.team.projectset.internal;


import org.ant4eclipse.platform.model.team.projectset.TeamProjectDescription;
import org.ant4eclipse.platform.model.team.projectset.TeamProjectSet;

import org.ant4eclipse.lib.core.Assert;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public abstract class AbstractTeamProjectSet implements TeamProjectSet {

  /** the name of the project set */
  private String                       _name;

  /** the team project descriptions */
  private List<TeamProjectDescription> _projectDescriptions;

  /**
   * Creates a new instance of type TeamProjectSet.
   * 
   * @param name
   *          the name of the team project set.
   */
  public AbstractTeamProjectSet(String name) {
    Assert.notNull(name);

    this._name = name;
    this._projectDescriptions = new LinkedList<TeamProjectDescription>();
  }

  /**
   * {@inheritDoc}
   */
  public String getName() {
    return this._name;
  }

  /**
   * {@inheritDoc}
   */
  public TeamProjectDescription[] getTeamProjectDescriptions() {
    return this._projectDescriptions.toArray(new TeamProjectDescription[0]);
  }

  /**
   * {@inheritDoc}
   */
  public TeamProjectDescription getTeamProjectDescriptionByName(String name) {
    Assert.notNull(name);

    for (TeamProjectDescription description : this._projectDescriptions) {
      if (name.equals(description.getProjectName())) {
        return description;
      }
    }

    throw new RuntimeException("EclipseProject " + name + " does not exist!");
  }

  /**
   * {@inheritDoc}
   */
  public String[] getProjectNames() {

    String[] result = new String[this._projectDescriptions.size()];
    int i = 0;
    for (TeamProjectDescription description : this._projectDescriptions) {
      String projectName = description.getProjectName();
      result[i] = projectName;
      i++;
    }

    return result;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String toString() {
    StringBuffer buffer = new StringBuffer();
    buffer.append("[TeamProjectSet:");
    buffer.append(" name: ");
    buffer.append(this._name);
    buffer.append(" { ");
    for (Iterator<TeamProjectDescription> iterator = this._projectDescriptions.iterator(); iterator.hasNext();) {
      TeamProjectDescription description = iterator.next();
      buffer.append(description);

      if (iterator.hasNext()) {
        buffer.append(",");
      }
    }
    buffer.append(" } ");
    buffer.append("]");
    return buffer.toString();
  }

  /**
   * @param description
   */
  protected void addTeamProjectDescription(TeamProjectDescription description) {
    Assert.notNull(description);
    this._projectDescriptions.add(description);
  }

  protected List<TeamProjectDescription> getProjectDescriptions() {
    return this._projectDescriptions;
  }

}
