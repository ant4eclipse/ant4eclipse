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

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import net.sf.ant4eclipse.core.Assert;

/**
 * Encapsultes a set of team project descriptions. A TeamProjectSet is used to
 * share a set of projects within a team. The TeamProjectSet is read form an
 * eclipse .psf-file.
 * 
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 */
public abstract class TeamProjectSet {

  /** the name of the project set */
  private String _name;

  /** the team project descriptions */
  private List _projectDescriptions;
  
  /**
   * Creates a new instance of type TeamProjectSet.
   * 
   * @param name
   *          the name of the team project set.
   */
  public TeamProjectSet(String name) {
    Assert.notNull(name);

    _name = name;
    _projectDescriptions = new LinkedList();
  }
  
  /**
   * Returns true if this TeamProjectSet represents a CVS-based
   * PSF-file
   * @return true if this TeamProjectSet is a CVS-PSF
   */
  public abstract boolean isCvsProjectSet();
  
  /**
   * Returns true if this TeamProjectSet represents a Subversion-based
   * PSF-file
   * @return true if this TeamProjectSet is for Subversion
   */
  public abstract boolean isSvnProjectSet();  

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
    return (TeamProjectDescription[]) _projectDescriptions.toArray(new TeamProjectDescription[0]);
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

    for (Iterator iterator = _projectDescriptions.iterator(); iterator.hasNext();) {
      TeamProjectDescription description = (TeamProjectDescription) iterator.next();

      if (name.equals(description.getProjectName())) {
        return description;
      }
    }

    throw new RuntimeException("EclipseProject " + name + " does not exist!");
  }

  /**
   * Returns a list of project names.
   * 
   * @return  A list of project names.
   */
  public String[] getProjectNames() {

    String[] result = new String[_projectDescriptions.size()];
    int i = 0;
    for (Iterator iterator = _projectDescriptions.iterator(); iterator.hasNext();) {
      TeamProjectDescription description = (TeamProjectDescription) iterator.next();
      String projectName = description.getProjectName();
      result[i] = projectName;
      i++;
    }

    return result;
  }

  /**
   * Sets the user and password that should be used when this Team Project Set will be checked out.
   * 
   * @param user
   *          cvs user.
   * @param pwd
   *          the password might be null
   */
  public abstract void setUserAndPassword(String user, String pwd);
  
  /**
   * {@inheritDoc}
   */
  public String toString() {
    StringBuffer buffer = new StringBuffer();
    buffer.append("[TeamProjectSet:");
    buffer.append(" name: ");
    buffer.append(_name);
    buffer.append(" { ");
    for (Iterator iterator = _projectDescriptions.iterator(); iterator.hasNext();) {
      TeamProjectDescription description = (TeamProjectDescription) iterator.next();
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

  protected List getProjectDescriptions() {
    return _projectDescriptions;
  }
  
  
}
