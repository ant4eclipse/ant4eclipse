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
package org.ant4eclipse.ant.platform.core.delegate;

import org.ant4eclipse.ant.core.delegate.AbstractAntDelegate;
import org.ant4eclipse.ant.platform.core.WorkspaceComponent;
import org.ant4eclipse.lib.core.A4ECore;
import org.ant4eclipse.lib.core.exception.Ant4EclipseException;
import org.ant4eclipse.lib.core.logging.A4ELogging;
import org.ant4eclipse.lib.platform.PlatformExceptionCode;
import org.ant4eclipse.lib.platform.model.resource.Workspace;
import org.ant4eclipse.lib.platform.model.resource.workspaceregistry.DefaultEclipseWorkspaceDefinition;
import org.ant4eclipse.lib.platform.model.resource.workspaceregistry.WorkspaceRegistry;
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

  private File      _workspaceDirectory;
  private String    _workspaceId;
  private Workspace _workspace;

  /**
   * <p>
   * Creates a new instance of type {@link WorkspaceDelegate}.
   * </p>
   * 
   * @param component
   *          the ant {@link ProjectComponent}
   */
  public WorkspaceDelegate( ProjectComponent component ) {
    super( component );
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setWorkspaceDirectory( String workspaceDirectory ) {
    if( workspaceDirectory != null && !workspaceDirectory.equals( "" ) ) {
      _workspaceDirectory = new File( workspaceDirectory );
      if( !_workspaceDirectory.exists() ) {
        throw new Ant4EclipseException( PlatformExceptionCode.WORKSPACE_DIRECTORY_DOES_NOT_EXIST, _workspaceDirectory );
      }
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public File getWorkspaceDirectory() {
    return _workspaceDirectory;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isWorkspaceDirectorySet() {
    return _workspaceDirectory != null && !_workspaceDirectory.equals( "" );
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setWorkspaceId( String identifier ) {
    _workspaceId = identifier;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getWorkspaceId() {
    return _workspaceId;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isWorkspaceIdSet() {
    return _workspaceId != null && !_workspaceId.equals( "" );
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void requireWorkspaceDirectoryOrWorkspaceIdSet() {
    if( !(isWorkspaceDirectorySet() || isWorkspaceIdSet()) ) {
      throw new Ant4EclipseException( PlatformExceptionCode.MISSING_WORKSPACE_DIRECTORY_OR_WORKSPACE_ID );
    }
    if( isWorkspaceDirectorySet() && isWorkspaceIdSet() ) {
      throw new Ant4EclipseException( PlatformExceptionCode.WORKSPACE_DIRECTORY_AND_WORKSPACE_ID_SET );
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Workspace getWorkspace() {
    requireWorkspaceDirectoryOrWorkspaceIdSet();
    A4ELogging.debug("Current workspace: " + _workspace);
    if( _workspace == null ) {
      WorkspaceRegistry registry = A4ECore.instance().getRequiredService( WorkspaceRegistry.class );
      if( !registry.containsWorkspace( getIdentifier() ) ) {
        A4ELogging.debug("RELOADING");
        if( isWorkspaceDirectorySet() ) {
          _workspace = registry.registerWorkspace( getIdentifier(), new DefaultEclipseWorkspaceDefinition(
              _workspaceDirectory ) );
        } else {
          throw new Ant4EclipseException( PlatformExceptionCode.UNKNOWN_WORKSPACE_ID, getIdentifier() );
        }
      } else {
        A4ELogging.debug("REUSING");
        _workspace = registry.getWorkspace( getIdentifier() );
      }
    }
    return _workspace;
  }

  /**
   * <p>
   * </p>
   */
  private String getIdentifier() {
    if( _workspaceDirectory != null ) {
      return _workspaceDirectory.getAbsolutePath();
    } else {
      return _workspaceId;
    }
  }
  
} /* ENDCLASS */

