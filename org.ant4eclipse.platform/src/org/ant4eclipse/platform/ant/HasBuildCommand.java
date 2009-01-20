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

import java.io.File;

import org.ant4eclipse.core.ant.AbstractAnt4EclipseCondition;
import org.ant4eclipse.platform.ant.core.delegate.EclipseProjectDelegate;
import org.ant4eclipse.platform.model.resource.EclipseProject;
import org.apache.tools.ant.BuildException;

/**
 * <p>
 * {@link HasBuildCommand} implements a condition to test whether a eclipse project has a specific build command or not
 * </p>
 * 
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 */
public class HasBuildCommand extends AbstractAnt4EclipseCondition {

  /** Comment for <code>_projectDelegate</code> */
  private final EclipseProjectDelegate _projectDelegate;

  /** Comment for <code>__buildCommand</code> */
  private String                _buildCommand;

  /**
   * Creates a new instance of type HasBuildCommand.
   */
  public HasBuildCommand() {
    this._projectDelegate = new EclipseProjectDelegate(this);
  }

  /**
   * Returns <code>true</code> if the eclipse project contains the requested buildCommand
   * 
   * @return <code>true</code> if the eclipse project contains the requested buildCommand.
   */
  @Override
  public boolean doEval() {
    this._projectDelegate.requireWorkspaceAndProjectNameSet();
    requireBuildCommandSet();
    final EclipseProject project = this._projectDelegate.getEclipseProject();
    return project.hasBuildCommand(this._buildCommand);
  }

  /**
   * Sets the name of the build command.
   * 
   * @param command
   *          name of the build command.
   */
  public void setBuildCommand(final String command) {
    this._buildCommand = command;
  }

  /**
   * Returns <code>true</code> if the build command has been set.
   * 
   * @return <code>true</code> if the build command has been set.
   */
  public boolean isBuildCommandSet() {
    return this._buildCommand != null;
  }

  /**
   * Throws a build exception if a build command is not set.
   */
  public final void requireBuildCommandSet() {
    if (!isBuildCommandSet()) {
      throw new BuildException("Attribute 'buildCommand' has to be set!");
    }
  }

  /**
   * Sets the name of the project.
   * 
   * @param project
   *          the name of the project.
   */
  public void setProjectName(final String project) {
    this._projectDelegate.setProjectName(project);
  }

  /**
   * Sets the workspace.
   * 
   * @param workspace
   *          the workspace.
   * @deprecated
   */
  @Deprecated
  public void setWorkspace(final File workspace) {
    this._projectDelegate.setWorkspace(workspace);
  }

  public void setWorkspaceDirectory(final File workspace) {
    this._projectDelegate.setWorkspaceDirectory(workspace);
  }
}