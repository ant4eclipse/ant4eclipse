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
package net.sf.ant4eclipse.ant.platform;

import java.io.File;

import net.sf.ant4eclipse.ant.Ant4EclipseConfiguration;
import net.sf.ant4eclipse.ant.platform.delegate.ProjectDelegate;
import net.sf.ant4eclipse.model.platform.resource.EclipseProject;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.ProjectComponent;
import org.apache.tools.ant.taskdefs.condition.Condition;

/**
 * <p>
 * The HasBuildCommand implements a condition to test whether a eclipse project has a specific build command or not
 * </p>
 * 
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 */
public class HasBuildCommand extends ProjectComponent implements Condition {

  /** Comment for <code>_projectDelegate</code> */
  private final ProjectDelegate _projectDelegate;

  /** Comment for <code>__buildCommand</code> */
  private String                _buildCommand;

  /**
   * Creates a new instance of type HasBuildCommand.
   */
  public HasBuildCommand() {
    Ant4EclipseConfiguration.configureAnt4Eclipse(getProject());
    this._projectDelegate = new ProjectDelegate(this);
  }

  /**
   * Returns <code>true</code> if the eclipse project contains the requested buildCommand
   * 
   * @return <code>true</code> if the eclipse project contains the requested buildCommand.
   */
  public boolean eval() throws BuildException {
    this._projectDelegate.requireWorkspaceAndProjectNameOrProjectSet();
    requireBuildCommandSet();
    try {
      final EclipseProject project = this._projectDelegate.getEclipseProject();
      return project.hasBuildCommand(this._buildCommand);
    } catch (final BuildException e) {
      throw e;
    } catch (final Exception e) {
      throw new BuildException(e.getMessage(), e);
    }
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
  public void setWorkspace(final File workspace) {
    this._projectDelegate.setWorkspace(workspace);
  }

  public void setWorkspaceDirectory(final File workspace) {
    this._projectDelegate.setWorkspaceDirectory(workspace);
  }

  /**
   * Sets the project.
   * 
   * @param project
   *          the project.
   */
  public void setProject(final File project) {
    this._projectDelegate.setProject(project);
  }
}