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
package org.ant4eclipse.model.platform.resource.internal;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import org.ant4eclipse.model.platform.resource.EclipseProject;
import org.ant4eclipse.model.platform.resource.Workspace;
import org.ant4eclipse.model.platform.resource.internal.factory.ProjectFactory;
import org.ant4eclipse.model.platform.resource.workspaceregistry.WorkspaceDefinition;
import org.ant4eclipse.model.platform.resource.workspaceregistry.WorkspaceRegistry;

import net.sf.ant4eclipse.core.Assert;
import net.sf.ant4eclipse.core.Lifecycle;
import net.sf.ant4eclipse.core.logging.A4ELogging;

/**
 * <p>
 * Implementation of the {@link WorkspaceRegistry} interface. 
 * </p>
 * 
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 */
public class WorkspaceRegistryImpl implements WorkspaceRegistry, Lifecycle {

  /** the workspace map (String, Workspace) */
  private Map<String, Workspace> _registry;

  /** the 'current' workspace */
  private Workspace              _current;

  /**
   * {@inheritDoc}
   */
  public Workspace getCurrent() {
    return this._current;
  }

  /**
   * {@inheritDoc}
   */
  public boolean hasCurrent() {
    return this._current != null;
  }

  /**
   * {@inheritDoc}
   */
  public void setCurrent(final Workspace currentWorkspace) {
    this._current = currentWorkspace;
  }

  /**
   * {@inheritDoc}
   */
  public void setCurrent(final String id) {
    this._current = getWorkspace(id);
  }

  /**
   * {@inheritDoc}
   */
  public Workspace getWorkspace(final String id) {
    return this._registry.get(id);
  }

  /**
   * {@inheritDoc}
   */
  public boolean containsWorkspace(final String id) {
    return this._registry.containsKey(id);
  }

  /**
   * {@inheritDoc}
   */
  public Workspace registerWorkspace(final String id, final WorkspaceDefinition workspaceDefinition) {
    Assert.nonEmpty(id);
    Assert.notNull(workspaceDefinition);

    // create new workspace implementation
    final WorkspaceImpl workspace = new WorkspaceImpl();

    // retrieve all project folders from the workspace definition
    final File[] projectFolders = workspaceDefinition.getProjectFolders();

    if (A4ELogging.isDebuggingEnabled()) {
      A4ELogging.debug("WorkspaceRegistry.registerWorkspace: project directory count=" + projectFolders.length);
    }

    // read the projects and add them to the workspace
    for (int i = 0; i < projectFolders.length; i++) {
      final EclipseProject eclipseProject = ProjectFactory.readProjectFromWorkspace(workspace, projectFolders[i]);
      workspace.registerEclipseProject(eclipseProject);
    }

    // add the workspace to the registry
    this._registry.put(id, workspace);

    // return the workspace
    return workspace;
  }

  /**
   * {@inheritDoc}
   */
  public void dispose() {
    this._registry.clear();
    this._registry = null;
  }

  /**
   * {@inheritDoc}
   */
  public void initialize() {
    this._registry = new HashMap<String, Workspace>();
  }

  /**
   * {@inheritDoc}
   */
  public boolean isInitialized() {
    return this._registry != null;
  }
}
