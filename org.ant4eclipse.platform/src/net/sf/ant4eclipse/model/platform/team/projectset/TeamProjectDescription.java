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

/**
 * <p>
 * Implements a description of a project to be shared in a team. The description contains the name, the
 * repository-location and the branch/version tag of the project. The TeamProjectDescription is read form an eclipse
 * .psf-file.
 * </p>
 * 
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 */
public interface TeamProjectDescription {

  /**
   * Returns the name of the project.
   * 
   * @return Returns the name of the project.
   */
  public String getProjectName();

}
