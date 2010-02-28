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
package org.ant4eclipse.lib.platform.model.team.projectset;

/**
 * Encapsultes a set of team project descriptions. A TeamProjectSet is used to share a set of projects within a team.
 * The TeamProjectSet is read form an eclipse .psf-file.
 * 
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 */
public interface TeamProjectSet {
  /**
   * Returns the name of the TeamProjectSet.
   * 
   * @return Returns the name of the TeamProjectSet.
   */
  String getName();

  /**
   * Returns the TeamProjectDescriptions.
   * 
   * @return Returns the TeamProjectDescriptions.
   */
  TeamProjectDescription[] getTeamProjectDescriptions();

  /**
   * Returns a TeamProjectDescription by the given name.
   * 
   * @param name
   *          the name of the TeamProjectDescription to return.
   * @return Returns a TeamProjectDescription by the given name.
   */
  TeamProjectDescription getTeamProjectDescriptionByName(String name);

  /**
   * Returns a list of project names.
   * 
   * @return A list of project names.
   */
  String[] getProjectNames();

  /**
   * Sets the user and password that should be used when this Team Project Set will be checked out.
   * 
   * @param user
   *          cvs user.
   * @param pwd
   *          the password might be null
   */
  void setUserAndPassword(String user, String pwd);

}
