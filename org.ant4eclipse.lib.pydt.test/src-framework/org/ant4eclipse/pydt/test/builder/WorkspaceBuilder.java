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
package org.ant4eclipse.pydt.test.builder;



import org.ant4eclipse.lib.core.util.Utilities;
import org.ant4eclipse.lib.platform.test.builder.EclipseProjectBuilder;

import java.io.File;

/**
 * Helper class used to represent a single workspace.
 * 
 * @author Daniel Kasmeroglu (Daniel.Kasmeroglu@Kasisoft.net)
 */
public class WorkspaceBuilder {

  private File _workspacedir;

  /**
   * Initialises this workspace.
   */
  public WorkspaceBuilder() {
    this._workspacedir = Utilities.createTempDir();
  }

  /**
   * Returns the location of the workspace directory.
   * 
   * @return The location of the workspace directory. Not <code>null</code>.
   */
  public File getWorkspaceFolder() {
    return this._workspacedir;
  }

  /**
   * Returns the location of a project folder.
   * 
   * @param projectname
   *          The name of the corresponding project. Neither <code>null</code> nor empty.
   * 
   * @return The folder containing the projects content. Not <code>null</code>.
   */
  public File getProjectFolder(String projectname) {
    return new File(this._workspacedir, projectname);
  }

  /**
   * Adds the supplied project to the workspace.
   * 
   * @param projectbuilder
   *          The project builder used to add a project. Not <code>null</code>.
   * 
   * @return The location of the project. Not <code>null</code>.
   */
  public File addProject(EclipseProjectBuilder projectbuilder) {
    return projectbuilder.createIn(this._workspacedir);
  }

  /**
   * Removes the supplied project from the workspace.
   * 
   * @param projectbuilder
   *          The project builder used to remove a project. Not <code>null</code>.
   */
  public void removeProject(EclipseProjectBuilder projectbuilder) {
    File dir = new File(this._workspacedir, projectbuilder.getProjectName());
    if (dir.isDirectory()) {
      if (!Utilities.delete(dir)) {
        throw new RuntimeException(String.format("Failed to remove project '%s'.", projectbuilder.getProjectName()));
      }
    }
  }

  /**
   * Causes the disposal of the complete workspace.
   */
  public void dispose() {
    if (!Utilities.delete(this._workspacedir)) {
      throw new RuntimeException(String.format("Failed to remove the workspace (%s).", this._workspacedir.getPath()));
    }
  }

} /* ENDCLASS */
