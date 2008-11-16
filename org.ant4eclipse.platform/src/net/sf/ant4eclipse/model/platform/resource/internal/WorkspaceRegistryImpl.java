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
package net.sf.ant4eclipse.model.platform.resource.internal;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import net.sf.ant4eclipse.core.Assert;
import net.sf.ant4eclipse.core.Lifecycle;
import net.sf.ant4eclipse.core.logging.A4ELogging;
import net.sf.ant4eclipse.model.platform.resource.EclipseProject;
import net.sf.ant4eclipse.model.platform.resource.Workspace;
import net.sf.ant4eclipse.model.platform.resource.internal.factory.ProjectFactory;
import net.sf.ant4eclipse.model.platform.resource.registry.WorkspaceDefinition;
import net.sf.ant4eclipse.model.platform.resource.registry.WorkspaceRegistry;

public class WorkspaceRegistryImpl implements WorkspaceRegistry, Lifecycle {

  /** - */
  private Map       _registry;

  private Workspace _current;

  public Workspace getCurrent() {
    return this._current;
  }

  public boolean hasCurrent() {
    return this._current != null;
  }

  public void setCurrent(final Workspace currentWorkspace) {
    this._current = currentWorkspace;
  }

  public void setCurrent(final String id) {
    this._current = getWorkspace(id);
  }

  public Workspace getWorkspace(final String id) {
    return (Workspace) this._registry.get(id);
  }

  public boolean containsWorkspace(final String id) {
    return this._registry.containsKey(id);
  }

  /**
   * @see net.sf.ant4eclipse.model.platform.resource.registry.WorkspaceRegistry#registerWorkspace(java.lang.String,
   *      java.io.File)
   */
  public Workspace registerWorkspace(final String id, final WorkspaceDefinition workspaceDefinition) {
    Assert.nonEmpty(id);
    Assert.notNull(workspaceDefinition);

    final WorkspaceImpl workspace = new WorkspaceImpl();

    final File[] projectFolders = workspaceDefinition.getProjectFolders();

    A4ELogging.debug("WorkspaceRegistry.registerWorkspace: project directory count=" + projectFolders.length);

    for (int i = 0; i < projectFolders.length; i++) {
      final EclipseProject eclipseProject = ProjectFactory.readProjectFromWorkspace(workspace, projectFolders[i]);
      workspace.registerEclipseProject(eclipseProject);
    }

    // add the workspace
    this._registry.put(id, workspace);

    // return the workspace
    return workspace;
  }

  /**
   * @see net.sf.ant4eclipse.core.Lifecycle#dispose()
   */
  public void dispose() {
    this._registry.clear();
    this._registry = null;
  }

  /**
   * @see net.sf.ant4eclipse.core.Lifecycle#initialize()
   */
  public void initialize() {
    this._registry = new HashMap();
  }

  /**
   * @see net.sf.ant4eclipse.core.Lifecycle#isInitialized()
   */
  public boolean isInitialized() {
    return this._registry != null;
  }
}
