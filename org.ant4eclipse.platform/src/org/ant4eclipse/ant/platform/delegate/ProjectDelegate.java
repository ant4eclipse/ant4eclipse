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
package org.ant4eclipse.ant.platform.delegate;

import java.io.File;

import net.sf.ant4eclipse.core.Assert;
import net.sf.ant4eclipse.core.logging.A4ELogging;

import org.ant4eclipse.model.platform.resource.EclipseProject;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.ProjectComponent;

/**
 * Base class for project based tasks and conditions.
 * 
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 */
public class ProjectDelegate extends WorkspaceDelegate {

  /** the eclipse project */
  private EclipseProject _eclipseProject;

  /** the name of the project */
  private String         _projectName;

  /** the directory of the project */
  private File           _projectDirectory;

  /** - */
  private boolean        _initialized = false;

  /**
   * @param component
   */
  public ProjectDelegate(final ProjectComponent component) {
    super(component);
  }

  /**
   * Returns the associated eclipse project.
   * 
   * @return the associated eclipse project.
   * 
   * @throws BuildException
   *           thrown if the eclipse project could not be read.
   */
  public EclipseProject getEclipseProject() throws BuildException {
    if (this._eclipseProject == null) {
      try {
        this._eclipseProject = readProjectFromWorkspace();
      } catch (final BuildException ex) {
        throw ex;
      } catch (final Exception ex) {
        throw new BuildException("Could not get eclipse project: " + ex, ex);
      }
    }

    return this._eclipseProject;
  }

  /**
   * Sets the name of the project.
   * 
   * @param projectName
   *          the name of the project.
   */
  public final void setProjectName(final String projectName) {
    this._projectName = projectName;
  }

  /**
   * Returns <code>true</code> if the project name has been set.
   * 
   * @return <code>true</code> if the project name has been set.
   */
  public final boolean isProjectNameSet() {
    return this._projectName != null;
  }

  /**
   * Sets the project directory.
   * 
   * @param projectDirectory
   *          the project directory.
   */
  public final void setProject(final File projectDirectory) {
    this._projectDirectory = projectDirectory;
  }

  /**
   * Returns <code>true</code> if the project has been set.
   * 
   * @return <code>true</code> if the project has been set.
   */
  public final boolean isProjectSet() {
    return this._projectDirectory != null;
  }

  /**
   * Requires that either the workspace and the project name <b>or</b> the project directory.
   */
  public final void requireWorkspaceAndProjectNameOrProjectSet() {
    this._initialized = true;
    if (isProjectSet()) {
      if (isWorkspaceDirectorySet() || isProjectNameSet()) {
        throw new BuildException(
            "You have to specify either the project attribute, or the workspace and projectName attributes!");
      }
      final File workspace = this._projectDirectory.getParentFile();
      final String projectName = this._projectDirectory.getName();

      A4ELogging.debug("Setting workspace to %s and projectName to %s", new Object[] { workspace, projectName });

      setWorkspaceDirectory(workspace);
      setProjectName(projectName);
    } else {
      if (!isWorkspaceDirectorySet() || !isProjectNameSet()) {
        throw new BuildException(
            "You have to specify either the project attribute, or the workspace and projectName attributes!");
      }
    }
  }

  /**
   * Reads the project from the workspace.
   * 
   * @return the eclipse project.
   */
  private EclipseProject readProjectFromWorkspace() {
    Assert.assertTrue(this._initialized, "ProjectBase has to be initialized!");

    if (getWorkspace().hasProject(this._projectName)) {
      return getWorkspace().getProject(this._projectName);
    } else {
      throw new BuildException("The specified project '" + this._projectName + "' does not exists in the workspace.");
    }
  }
}