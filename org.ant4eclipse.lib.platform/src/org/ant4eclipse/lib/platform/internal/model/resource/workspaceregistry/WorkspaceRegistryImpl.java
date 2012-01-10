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
package org.ant4eclipse.lib.platform.internal.model.resource.workspaceregistry;

import org.ant4eclipse.lib.core.logging.A4ELogging;
import org.ant4eclipse.lib.platform.internal.model.resource.WorkspaceImpl;
import org.ant4eclipse.lib.platform.model.resource.EclipseProject;
import org.ant4eclipse.lib.platform.model.resource.Workspace;
import org.ant4eclipse.lib.platform.model.resource.workspaceregistry.WorkspaceDefinition;
import org.ant4eclipse.lib.platform.model.resource.workspaceregistry.WorkspaceRegistry;

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
 * @author Daniel Kasmeroglu (Daniel.Kasmeroglu@kasisoft.net)
 */
public class WorkspaceRegistryImpl implements WorkspaceRegistry {

  /** The factory used to build projects */
  private ProjectFactory        _projectFactory;

  /** the workspace map (String, Workspace) */
  private Map<String,Workspace> _registry;

  public WorkspaceRegistryImpl() {
    _registry = new HashMap<String,Workspace>();
    _projectFactory = new ProjectFactory();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Workspace getWorkspace( String id ) {
    return _registry.get( id );
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean containsWorkspace( String id ) {
    return _registry.containsKey( id );
  }

  /**
   * {@inheritDoc}
   */
  // Assure.nonEmpty( "id", id );
  // Assure.notNull( "workspaceDefinition", workspaceDefinition );
  @Override
  public Workspace registerWorkspace( String id, WorkspaceDefinition workspaceDefinition ) {

    WorkspaceImpl workspace = new WorkspaceImpl();

    // retrieve all project folders from the workspace definition
    List<File> projectFolders = workspaceDefinition.getProjectFolders();

    if( A4ELogging.isDebuggingEnabled() ) {
      A4ELogging.debug( "WorkspaceRegistry.registerWorkspace: project directory count=%d.", Integer.valueOf( projectFolders.size() ) );
    }

    // read the projects and add them to the workspace
    List<EclipseProject> projects = new ArrayList<EclipseProject>();
    for( File projectFolder : projectFolders ) {
      A4ELogging.info( "Loading project folder '%s' ...", projectFolder.getName() );
      EclipseProject eclipseProject = _projectFactory.readProjectFromWorkspace( workspace, projectFolder );
      projects.add( eclipseProject );
      workspace.registerEclipseProject( eclipseProject );
    }

    for( EclipseProject project : projects ) {
      _projectFactory.postProcessRoleSetup( project );
    }

    _registry.put( id, workspace );

    return workspace;
    
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Integer getPriority() {
    return null;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void reset() {
    _registry.clear();
  }

} /* ENDCLASS */
