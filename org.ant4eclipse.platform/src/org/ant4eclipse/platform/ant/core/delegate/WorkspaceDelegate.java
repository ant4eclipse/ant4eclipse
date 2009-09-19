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
package org.ant4eclipse.platform.ant.core.delegate;

import org.ant4eclipse.core.ant.delegate.AbstractAntDelegate;
import org.ant4eclipse.core.logging.A4ELogging;

import org.ant4eclipse.platform.ant.core.WorkspaceComponent;
import org.ant4eclipse.platform.model.resource.Workspace;
import org.ant4eclipse.platform.model.resource.workspaceregistry.DefaultEclipseWorkspaceDefinition;
import org.ant4eclipse.platform.model.resource.workspaceregistry.WorkspaceRegistry;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.ProjectComponent;

import java.io.File;

/**
 * <p>
 * Delegate class for ant4eclipse tasks, conditions and types that require a workspace.
 * </p>
 * 
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 */
public class WorkspaceDelegate extends AbstractAntDelegate implements WorkspaceComponent {

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
  public WorkspaceDelegate(ProjectComponent component) {
    super(component);
  }

  /**
   * {@inheritDoc}
   */
  @Deprecated
  public final void setWorkspace(File workspace) {
    A4ELogging.warn("The attribute 'workspace' is deprecated. Please use 'workspaceDirectory' instead !");
    setWorkspaceDirectory(workspace);
  }

  /**
   * {@inheritDoc}
   */
  public final void setWorkspaceDirectory(File workspaceDirectory) {
    this._workspaceDirectory = workspaceDirectory;
  }

  /**
   * {@inheritDoc}
   */
  public final File getWorkspaceDirectory() {
    return this._workspaceDirectory;
  }

  /**
   * {@inheritDoc}
   */
  public final boolean isWorkspaceDirectorySet() {
    return this._workspaceDirectory != null;
  }

  /**
   * {@inheritDoc}
   */
  public final void requireWorkspaceDirectorySet() {
    if (!isWorkspaceDirectorySet()) {
      // TODO!!
      throw new BuildException("Workspace directory has to be set!");
    }
  }

  /**
   * {@inheritDoc}
   */
  public final Workspace getWorkspace() {
    requireWorkspaceDirectorySet();

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