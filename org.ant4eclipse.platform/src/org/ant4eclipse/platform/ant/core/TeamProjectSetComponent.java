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
import org.apache.tools.ant.BuildException;

/**
 * <p>
 * Interface for all ant4eclipse tasks, conditions and types working with project sets.
 * </p>
 * 
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 */
public interface TeamProjectSetComponent {

  /**
   * <p>
   * Returns the {@link TeamProjectSet}.
   * </p>
   * 
   * @return the team project set.
   */
  public TeamProjectSet getTeamProjectSet();

  /**
   * <p>
   * Sets the team project set file.
   * </p>
   * 
   * @param projectSet
   *          the team project set file.
   */
  public void setTeamProjectSet(File projectSetFile);

  /**
   * <p>
   * Returns true if the project set has been set.
   * </p>
   * 
   * @return <code>true</code>, if the project set has been set.
   */
  public boolean isTeamProjectSetSet();

  /**
   * <p>
   * Throws an {@link BuildException} if the team project set is not set.
   * </p>
   */
  public void requireTeamProjectSetSet();
}