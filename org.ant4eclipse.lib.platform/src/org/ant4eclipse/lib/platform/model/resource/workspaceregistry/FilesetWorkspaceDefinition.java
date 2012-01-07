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
package org.ant4eclipse.lib.platform.model.resource.workspaceregistry;

import org.ant4eclipse.lib.core.Assure;

import java.io.File;
import java.util.List;

/**
 * <p>
 * Very simple WorkspaceDefinition implementation which simply maintains an array of project directories. The
 * directories are applied at instantiation by a task that registers the workspace.
 * </p>
 * 
 * @author mriley
 */
public class FilesetWorkspaceDefinition implements WorkspaceDefinition {

  /** the set of project directories */
  private List<File>   directories;

  /**
   * <p>
   * Creates a new instance of type {@link FilesetWorkspaceDefinition}.
   * </p>
   * 
   * @param directories
   */
  public FilesetWorkspaceDefinition( List<File> newdirectories ) {
    Assure.notNull( "newdirectories", newdirectories );
    directories = newdirectories;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public List<File> getProjectFolders() {
    return directories;
  }
  
} /* ENDCLASS */
