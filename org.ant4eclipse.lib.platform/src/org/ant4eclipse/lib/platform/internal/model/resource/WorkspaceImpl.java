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
package org.ant4eclipse.lib.platform.internal.model.resource;

import org.ant4eclipse.lib.core.exception.Ant4EclipseException;
import org.ant4eclipse.lib.core.logging.A4ELogging;
import org.ant4eclipse.lib.platform.PlatformExceptionCode;
import org.ant4eclipse.lib.platform.model.resource.EclipseProject;
import org.ant4eclipse.lib.platform.model.resource.Workspace;
import org.ant4eclipse.lib.platform.model.resource.role.ProjectRole;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * Encapsulates the workspace that contains the eclipse projects.
 * </p>
 * 
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 */
public class WorkspaceImpl implements Workspace {

  /** map with all the eclipse projects */
  private Map<String,EclipseProject> _projects;

  /**
   * {@inheritDoc}
   */
  // Assure.nonEmpty( "name", name );
  @Override
  public boolean hasProject( String name ) {
    return _projects.containsKey( name );
  }

  /**
   * {@inheritDoc}
   */
  // Assure.nonEmpty( "name", name );
  @Override
  public EclipseProject getProject( String name ) {
    return _projects.get( name );
  }

  /**
   * {@inheritDoc}
   */
  // Assure.notNull( "names", names );
  @Override
  public List<EclipseProject> getProjects( List<String> names, boolean failOnMissingProjects ) {
    List<EclipseProject> projects = new ArrayList<EclipseProject>();
    for( String name : names ) {
      EclipseProject project = getProject( name );
      if( project == null ) {
        if( failOnMissingProjects ) {
          throw new Ant4EclipseException( PlatformExceptionCode.SPECIFIED_PROJECT_DOES_NOT_EXIST, name );
        } else {
          A4ELogging.debug( "Specified project '%s' does not exist.", name );
        }
      } else {
        // add the project to the result list...
        projects.add( project );
      }
    }
    return projects;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public List<EclipseProject> getAllProjects() {
    return new ArrayList<EclipseProject>( _projects.values() );
  }

  /**
   * {@inheritDoc}
   */
  // Assure.notNull( "projectRole", projectRole );
  // Assure.assertTrue( ProjectRole.class.isAssignableFrom( projectRole ), String.format(
  //    "Class '%s' must be assignable from class '%s'", projectRole.getClass().getName(),
  //    ProjectRole.class.getName() ) );
  @Override
  public List<EclipseProject> getAllProjects( Class<? extends ProjectRole> projectRole ) {
    List<EclipseProject> result = new ArrayList<EclipseProject>();
    Collection<EclipseProject> projects = _projects.values();
    for( EclipseProject eclipseProject : projects ) {
      if( eclipseProject.hasRole( projectRole ) ) {
        result.add( eclipseProject );
      }
    }
    return result;
  }

  /**
   * <p>
   * Creates a new instance of type {@link WorkspaceImpl}.
   * </p>
   */
  public WorkspaceImpl() {
    _projects = new Hashtable<String,EclipseProject>();
  }

  // Assure.notNull( "eclipseProject", eclipseProject );
  public void registerEclipseProject( EclipseProject eclipseProject ) {
    String key = eclipseProject.getSpecifiedName();
    if( _projects.containsKey( key ) && !eclipseProject.equals( _projects.get( key ) ) ) {
      throw new Ant4EclipseException( PlatformExceptionCode.PROJECT_WITH_SAME_SPECIFIED_NAME_ALREADY_EXISTS,
          _projects.get( key ), eclipseProject );
    }
    _projects.put( key, eclipseProject );
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public int hashCode() {
    int prime = 31;
    int result = 1;
    result = prime * result + ((_projects == null) ? 0 : _projects.hashCode());
    return result;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean equals( Object obj ) {
    if( this == obj ) {
      return true;
    }
    if( obj == null ) {
      return false;
    }
    if( getClass() != obj.getClass() ) {
      return false;
    }
    WorkspaceImpl other = (WorkspaceImpl) obj;
    if( _projects == null ) {
      if( other._projects != null ) {
        return false;
      }
    } else if( !_projects.equals( other._projects ) ) {
      return false;
    }
    return true;
  }
  
} /* ENDCLASS */
