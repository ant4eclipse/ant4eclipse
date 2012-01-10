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


import org.ant4eclipse.lib.core.A4EService;
import org.ant4eclipse.lib.platform.model.resource.Workspace;

/**
 * <p>
 * The {@link WorkspaceRegistry} is used to maintain workspaces.
 * </p>
 */
public interface WorkspaceRegistry extends A4EService {

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
  Workspace registerWorkspace( String id, WorkspaceDefinition workspaceDefinition );

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
  boolean containsWorkspace( String id );

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
  Workspace getWorkspace( String id );

} /* ENDINTERFACE */
