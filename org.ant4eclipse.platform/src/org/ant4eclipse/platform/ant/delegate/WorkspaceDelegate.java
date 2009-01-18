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
package org.ant4eclipse.platform.ant.delegate;

import java.io.File;

import org.ant4eclipse.core.ant.delegate.AbstractAntDelegate;
import org.ant4eclipse.platform.model.resource.Workspace;
import org.ant4eclipse.platform.model.resource.workspaceregistry.DefaultEclipseWorkspaceDefinition;
import org.ant4eclipse.platform.model.resource.workspaceregistry.WorkspaceRegistry;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.ProjectComponent;

/**
 * <p>
 * Delegate class for ant4eclipse tasks, conditions and types that require a workspace.
 * </p>
 * 
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 */
public class WorkspaceDelegate extends AbstractAntDelegate {

  /** the workspace directory (has to be defined in the ant build file) */
  private File      _workspaceDirectory;

  /** the workspace instance */
  private Workspace _workspace;

  /**
   * <p>
   * Creates a new instance of type {@link WorkspaceDelegate}.
   * </p>
   * 
   * @param component
   *          the ant {@link ProjectComponent}
   */
  public WorkspaceDelegate(final ProjectComponent component) {
    super(component);
  }

  /**
   * <p>
   * Sets the workspace directory.
   * </p>
   * 
   * @param workspace
   *          the workspace directory
   * 
   * @deprecated use {@link WorkspaceDelegate#setWorkspaceDirectory(File)} instead. This method is for backward
   *             compatibility only.
   */
  @Deprecated
  public final void setWorkspace(final File workspace) {
    setWorkspaceDirectory(workspace);
  }

  /**
   * <p>
   * Sets the workspace directory.
   * </p>
   * 
   * @param workspace
   *          the workspace directory
   */
  public final void setWorkspaceDirectory(final File workspaceDirectory) {
    this._workspaceDirectory = workspaceDirectory;
  }

  /**
   * <p>
   * Returns the workspace directory.
   * </p>
   * 
   * @return the workspace directory.
   */
  public final File getWorkspaceDirectory() {
    return this._workspaceDirectory;
  }

  /**
   * <p>
   * Returns <code>true</code> if the workspace directory is set, <code>false</code> otherwise.
   * </p>
   * 
   * @return <code>true</code> if the workspace directory is set, <code>false</code> otherwise.
   */
  public final boolean isWorkspaceSet() {
    return this._workspaceDirectory != null;
  }

  /**
   * <p>
   * Throws an {@link BuildException} if the workspace directory has not been set.
   * </p>
   */
  public final void requireWorkspaceSet() {
    if (!isWorkspaceSet()) {
      // TODO!!
      throw new BuildException("Workspace has to be set!");
    }
  }

  /**
   * <p>
   * Returns the {@link Workspace} instance.
   * </p>
   * 
   * @return
   */
  public final Workspace getWorkspace() {
    requireWorkspaceSet();

    if (this._workspace == null) {
      if (!WorkspaceRegistry.Helper.getRegistry().containsWorkspace(this._workspaceDirectory.getAbsolutePath())) {
        this._workspace = WorkspaceRegistry.Helper.getRegistry()
            .registerWorkspace(this._workspaceDirectory.getAbsolutePath(),
                new DefaultEclipseWorkspaceDefinition(this._workspaceDirectory));
      } else {
        this._workspace = WorkspaceRegistry.Helper.getRegistry().getWorkspace(
            this._workspaceDirectory.getAbsolutePath());
      }
    }

    // return the Workspace instance
    return this._workspace;
  }
} /* ENDCLASS */