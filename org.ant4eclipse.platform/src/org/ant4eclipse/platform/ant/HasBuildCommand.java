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
package org.ant4eclipse.platform.ant;

import org.ant4eclipse.platform.ant.core.condition.AbstractProjectBasedCondition;
import org.ant4eclipse.platform.model.resource.EclipseProject;
import org.apache.tools.ant.BuildException;

/**
 * <p>
 * {@link HasBuildCommand} implements a condition to test whether a eclipse project has a specific build command or not
 * </p>
 * 
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 */
public class HasBuildCommand extends AbstractProjectBasedCondition {

  /** the build command */
  private String _buildCommand;

  /**
   * <p>
   * Creates a new instance of type {@link HasBuildCommand}.
   * </p>
   */
  public HasBuildCommand() {
  }

  /**
   * <p>
   * Returns <code>true</code> if the eclipse project contains the requested buildCommand
   * </p>
   * 
   * @return <code>true</code> if the eclipse project contains the requested buildCommand.
   */
  @Override
  public boolean doEval() {
    requireWorkspaceAndProjectNameSet();
    requireBuildCommandSet();
    final EclipseProject project = getEclipseProject();
    return project.hasBuildCommand(this._buildCommand);
  }

  /**
   * <p>
   * Sets the name of the build command.
   * </p>
   * 
   * @param command
   *          name of the build command.
   */
  public void setBuildCommand(final String command) {
    this._buildCommand = command;
  }

  /**
   * <p>
   * Returns <code>true</code> if the build command has been set.
   * </p>
   * 
   * @return <code>true</code> if the build command has been set.
   */
  public boolean isBuildCommandSet() {
    return this._buildCommand != null;
  }

  /**
   * <p>
   * Throws a build exception if a build command is not set.
   * </p>
   */
  public final void requireBuildCommandSet() {
    if (!isBuildCommandSet()) {
      throw new BuildException("Attribute 'buildCommand' has to be set!");
    }
  }
}