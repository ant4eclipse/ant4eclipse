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

import org.ant4eclipse.core.util.Utilities;

import org.ant4eclipse.platform.test.builder.EclipseProjectBuilder;

import java.io.File;

/**
 * Helper class used to represent a single workspace.
 * 
 * @author Daniel Kasmeroglu (Daniel.Kasmeroglu@Kasisoft.net)
 */
public class PythonWorkspace {

  private File _workspacedir;

  /**
   * Initialises this workspace.
   */
  public PythonWorkspace() {
    _workspacedir = Utilities.createTempDir();
  }

  /**
   * Adds the supplied project to the workspace.
   * 
   * @param projectbuilder
   *          The project builder used to add a project. Not <code>null</code>.
   */
  public void addProject(final EclipseProjectBuilder projectbuilder) {
    projectbuilder.createIn(_workspacedir);
  }

  /**
   * Removes the supplied project from the workspace.
   * 
   * @param projectbuilder
   *          The project builder used to remove a project. Not <code>null</code>.
   */
  public void removeProject(final EclipseProjectBuilder projectbuilder) {
    final File dir = new File(_workspacedir, projectbuilder.getProjectName());
    if (dir.isDirectory()) {
      if (!Utilities.delete(dir)) {
        throw new RuntimeException(String.format("Failed to remove project '%s'.", projectbuilder.getProjectName()));
      }
    }
  }

} /* ENDCLASS */
