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
package org.ant4eclipse.platform.model.resource.workspaceregistry;

import java.io.File;

/**
 * <p>
 * A {@link WorkspaceDefinition} defines which directories in a given file system should be treated as members of an
 * eclipse workspace. Normally an eclipse workspace has a 'flat' layout.
 * </p>
 * 
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 */
public interface WorkspaceDefinition {

  /**
   * <p>
   * Returns an array of folders that should be treated as eclipse projects for a workspace.
   * </p>
   * 
   * @return an array of folders that should be treated as eclipse projects for a workspace.
   */
  public File[] getProjectFolders();
} /* ENDCLASS */
