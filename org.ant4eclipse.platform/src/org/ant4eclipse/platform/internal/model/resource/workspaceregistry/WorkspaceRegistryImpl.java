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
package org.ant4eclipse.platform.internal.model.resource.workspaceregistry;

import org.ant4eclipse.platform.internal.model.resource.WorkspaceImpl;
import org.ant4eclipse.platform.model.resource.EclipseProject;
import org.ant4eclipse.platform.model.resource.Workspace;
import org.ant4eclipse.platform.model.resource.workspaceregistry.WorkspaceDefinition;
import org.ant4eclipse.platform.model.resource.workspaceregistry.WorkspaceRegistry;

import org.ant4eclipse.lib.core.Assert;
import org.ant4eclipse.lib.core.Lifecycle;
import org.ant4eclipse.lib.core.logging.A4ELogging;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * Implementation of the {@link WorkspaceRegistry} interface.
 * </p>
 * 
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 */
public class WorkspaceRegistryImpl implements WorkspaceRegistry, Lifecycle {

  /** The factory used to build projects */
  private ProjectFactory         _projectFactory;

  /** the workspace map (String, Workspace) */
  private Map<String, Workspace> _registry;

  /** the 'current' workspace */
  private Workspace              _current;

  public WorkspaceRegistryImpl() {
    super();
  }

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
  public void setCurrent(Workspace currentWorkspace) {
    this._current = currentWorkspace;
  }

  /**
   * {@inheritDoc}
   */
  public void setCurrent(String id) {
    this._current = getWorkspace(id);
  }

  /**
   * {@inheritDoc}
   */
  public Workspace getWorkspace(String id) {
    return this._registry.get(id);
  }

  /**
   * {@inheritDoc}
   */
  public boolean containsWorkspace(String id) {
    return this._registry.containsKey(id);
  }

  /**
   * {@inheritDoc}
   */
  public Workspace registerWorkspace(String id, WorkspaceDefinition workspaceDefinition) {
    Assert.nonEmpty(id);
    Assert.notNull(workspaceDefinition);

    // create new workspace implementation
    WorkspaceImpl workspace = new WorkspaceImpl();

    // retrieve all project folders from the workspace definition
    File[] projectFolders = workspaceDefinition.getProjectFolders();

    if (A4ELogging.isDebuggingEnabled()) {
      A4ELogging.debug("WorkspaceRegistry.registerWorkspace: project directory count=" + projectFolders.length);
    }

    // read the projects and add them to the workspace
    List<EclipseProject> projects = new ArrayList<EclipseProject>();
    for (File projectFolder : projectFolders) {
      EclipseProject eclipseProject = this._projectFactory.readProjectFromWorkspace(workspace, projectFolder);
      projects.add(eclipseProject);
      workspace.registerEclipseProject(eclipseProject);
    }

    for (EclipseProject project : projects) {
      this._projectFactory.postProcessRoleSetup(project);
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
    this._projectFactory = null;
  }

  /**
   * {@inheritDoc}
   */
  public void initialize() {
    this._registry = new HashMap<String, Workspace>();
    this._projectFactory = new ProjectFactory();
  }

  /**
   * {@inheritDoc}
   */
  public boolean isInitialized() {
    return (this._registry != null && this._projectFactory != null);
  }
}
