/**********************************************************************
 * Copyright (c) 2005-2009 ant4eclipse project team.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Nils Hartmann, Daniel Kasmeroglu, Gerd Wuetherich
 **********************************************************************/
package org.ant4eclipse.ant.platform.core;

import org.apache.tools.ant.BuildException;

/**
 * <p>
 * Extends the interface {@link TeamProjectSetComponent} and allows to set a list of project names to define a project
 * set.
 * </p>
 * 
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 */
public interface ProjectSetComponent extends TeamProjectSetComponent {

  /**
   * <p>
   * Sets the list of project names.
   * </p>
   * 
   * @param projectNames
   *          a comma separated list of project names.
   */
  void setProjectNames( String projectNames );

  /**
   * <p>
   * Returns the list of project names.
   * </p>
   * 
   * @return the list of project names.
   */
  String[] getProjectNames();

  /**
   * <p>
   * Returns <code>true</code>, if the project names are set.
   * </p>
   * 
   * @return <code>true</code>, if the project names are set.
   */
  boolean isProjectNamesSet();

  /**
   * <p>
   * Throws an {@link BuildException} if the project names are not set.
   * </p>
   */
  void requireProjectNamesSet();

  /**
   * <p>
   * Throws an {@link BuildException} if the project names and team project set are not set.
   * </p>
   */
  void requireTeamProjectSetOrProjectNamesSet();
  
} /* ENDINTERFACE */
