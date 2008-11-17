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

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import net.sf.ant4eclipse.core.Assert;
import net.sf.ant4eclipse.model.platform.team.projectset.TeamProjectDescription;
import net.sf.ant4eclipse.model.platform.team.projectset.TeamProjectSet;

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

    _name = name;
    _projectDescriptions = new LinkedList<TeamProjectDescription>();
  }

  /**
   * Returns the name of the TeamProjectSet.
   * 
   * @return Returns the name of the TeamProjectSet.
   */
  public String getName() {
    return _name;
  }

  /**
   * Returns the TeamProjectDescriptions.
   * 
   * @return Returns the TeamProjectDescriptions.
   */
  public TeamProjectDescription[] getTeamProjectDescriptions() {
    return _projectDescriptions.toArray(new TeamProjectDescription[0]);
  }

  /**
   * Returns a TeamProjectDescription by the given name.
   * 
   * @param name
   *          the name of the TeamProjectDescription to return.
   * @return Returns a TeamProjectDescription by the given name.
   */
  public TeamProjectDescription getTeamProjectDescriptionByName(String name) {
    Assert.notNull(name);

    for (Iterator<TeamProjectDescription> iterator = _projectDescriptions.iterator(); iterator.hasNext();) {
      TeamProjectDescription description = iterator.next();

      if (name.equals(description.getProjectName())) {
        return description;
      }
    }

    throw new RuntimeException("EclipseProject " + name + " does not exist!");
  }

  /**
   * Returns a list of project names.
   * 
   * @return A list of project names.
   */
  public String[] getProjectNames() {

    String[] result = new String[_projectDescriptions.size()];
    int i = 0;
    for (Iterator<TeamProjectDescription> iterator = _projectDescriptions.iterator(); iterator.hasNext();) {
      TeamProjectDescription description = iterator.next();
      String projectName = description.getProjectName();
      result[i] = projectName;
      i++;
    }

    return result;
  }

  /**
   * {@inheritDoc}
   */
  public String toString() {
    StringBuffer buffer = new StringBuffer();
    buffer.append("[TeamProjectSet:");
    buffer.append(" name: ");
    buffer.append(_name);
    buffer.append(" { ");
    for (Iterator<TeamProjectDescription> iterator = _projectDescriptions.iterator(); iterator.hasNext();) {
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
    _projectDescriptions.add(description);
  }

  protected List<TeamProjectDescription> getProjectDescriptions() {
    return _projectDescriptions;
  }

}
