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
package org.ant4eclipse.platform.ant.core;

import java.io.File;

import org.ant4eclipse.platform.model.team.projectset.TeamProjectSet;

/**
 * Base class for all tasks working with project sets
 * 
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 */
public interface TeamProjectSetComponent {

  /**
   * <p>
   * </p>
   * 
   * @return Returns the psfFileName.
   */
  public TeamProjectSet getTeamProjectSet();

  /**
   * <p>
   * </p>
   * 
   * @param projectSet
   *          The psfFileName to set.
   */
  public void setTeamProjectSet(File projectSet);

  /**
   * <p>
   * Returns true if the project set has been set.
   * </p>
   * 
   * @return true <=> The project set has been set.
   */
  public boolean isTeamProjectSetSet();

  /**
   * <p>
   * </p>
   */
  public void requireTeamProjectSetSet();
}