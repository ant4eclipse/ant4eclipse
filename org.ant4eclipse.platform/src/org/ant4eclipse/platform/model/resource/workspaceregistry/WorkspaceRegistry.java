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
package org.ant4eclipse.platform.model.resource.workspaceregistry;

import org.ant4eclipse.core.service.ServiceRegistry;

import org.ant4eclipse.platform.model.resource.Workspace;

import org.ant4eclipse.lib.core.Lifecycle;

/**
 * <p>
 * The {@link WorkspaceRegistry} is used to maintain workspaces.
 * </p>
 */
public interface WorkspaceRegistry extends Lifecycle {

  /**
   * <p>
   * Registers a new instance of type {@link Workspace} that is described by the given {@link WorkspaceDefinition} under
   * the specified id.
   * </p>
   * 
   * @param id
   *          the identifier under which the new {@link Workspace} instance is stored.
   * @param WorkspaceDefinition
   *          the workspace definition
   * @return the new {@link Workspace} instance
   */
  Workspace registerWorkspace(String id, WorkspaceDefinition workspaceDefinition);

  /**
   * <p>
   * Returns <code>true</code> if the registry contains a {@link Workspace} that is registered under the specified
   * identifier.
   * </p>
   * 
   * @param id
   *          the identifier.
   * @return <code>true</code> if the registry contains a {@link Workspace} that is registered under the specified
   *         identifier.
   */
  boolean containsWorkspace(String id);

  /**
   * <p>
   * Returns the {@link Workspace} that is registered under the given identifier. If no {@link Workspace} is registered
   * under the given identifier, <code>null</code> will be returned instead.
   * </p>
   * 
   * @param id
   *          the identifier.
   * @return the {@link Workspace} that is registered under the given identifier.
   */
  Workspace getWorkspace(String id);

  /**
   * <p>
   * Sets the specified workspace as the current workspace.
   * </p>
   * 
   * @param currentWorkspace
   *          the workspace.
   */
  void setCurrent(Workspace currentWorkspace);

  /**
   * <p>
   * Sets the workspace with the specified identifier as the current workspace.
   * </p>
   * 
   * @param specified
   *          the identifier of the workspace.
   */
  void setCurrent(String id);

  /**
   * <p>
   * Returns <code>true</code> if a current workspace is set.
   * </p>
   * 
   * @return <code>true</code> if a current workspace is set.
   */
  boolean hasCurrent();

  /**
   * <p>
   * Returns the current {@link Workspace}.
   * </p>
   * 
   * @return the current {@link Workspace}.
   */
  Workspace getCurrent();

  /**
   * <p>
   * Helper class to retrieve the {@link WorkspaceRegistry}.
   * </p>
   */
  class Helper {

    /**
     * <p>
     * Fetches the {@link WorkspaceRegistry} instance from the {@link ServiceRegistry}.
     * </p>
     * 
     * @return the registered {@link WorkspaceRegistry}
     */
    public static WorkspaceRegistry getRegistry() {
      return (WorkspaceRegistry) ServiceRegistry.instance().getService(WorkspaceRegistry.class.getName());
    }
  }

} /* ENDINTERFACE */
