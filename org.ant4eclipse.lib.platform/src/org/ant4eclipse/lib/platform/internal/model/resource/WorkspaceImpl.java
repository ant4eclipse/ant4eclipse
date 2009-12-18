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

import org.ant4eclipse.lib.core.Assure;
import org.ant4eclipse.lib.core.exception.Ant4EclipseException;
import org.ant4eclipse.lib.core.logging.A4ELogging;
import org.ant4eclipse.lib.platform.PlatformExceptionCode;
import org.ant4eclipse.lib.platform.model.resource.EclipseProject;
import org.ant4eclipse.lib.platform.model.resource.Workspace;
import org.ant4eclipse.lib.platform.model.resource.role.ProjectRole;

import java.util.Collection;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * Encapsulates the workspace that contains the eclipse projects.
 * </p>
 * 
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 */
public final class WorkspaceImpl implements Workspace {

  /** map with all the eclipse projects */
  private Map<String, EclipseProject> _projects;

  /**
   * {@inheritDoc}
   */
  public boolean hasProject(String name) {
    Assure.nonEmpty(name);

    return this._projects.containsKey(name);
  }

  /**
   * {@inheritDoc}
   */
  public EclipseProject getProject(String name) {
    Assure.nonEmpty(name);

    return this._projects.get(name);
  }

  /**
   * {@inheritDoc}
   */
  public EclipseProject[] getProjects(String[] names, boolean failOnMissingProjects) {
    Assure.paramNotNull("names", names);

    // the result list with all the eclipse projects...
    List<EclipseProject> projects = new LinkedList<EclipseProject>();

    // iterate over the project names...
    for (String name : names) {
      // get the eclipse project
      EclipseProject project = getProject(name);

      // handle project is null
      if (project == null) {
        if (failOnMissingProjects) {
          throw new Ant4EclipseException(PlatformExceptionCode.SPECIFIED_PROJECT_DOES_NOT_EXIST, name);
        } else {
          A4ELogging.debug("Specified project '%s' does not exist.", name);
        }
      }
      // add the project to the result list...
      else {
        projects.add(project);
      }
    }

    // return the result
    return projects.toArray(new EclipseProject[0]);
  }

  /**
   * {@inheritDoc}
   */
  public EclipseProject[] getAllProjects() {
    Collection<EclipseProject> projects = this._projects.values();
    return projects.toArray(new EclipseProject[0]);
  }

  /**
   * {@inheritDoc}
   */
  public EclipseProject[] getAllProjects(Class<? extends ProjectRole> projectRole) {
    Assure.paramNotNull("projectRole", projectRole);
    Assure
        .assertTrue(ProjectRole.class.isAssignableFrom(projectRole), String.format(
            "Class '%s' must be assignable from class '%s'", projectRole.getClass().getName(), ProjectRole.class
                .getName()));

    List<EclipseProject> result = new LinkedList<EclipseProject>();
    Collection<EclipseProject> projects = this._projects.values();
    for (EclipseProject eclipseProject : projects) {
      if (eclipseProject.hasRole(projectRole)) {
        result.add(eclipseProject);
      }
    }

    return result.toArray(new EclipseProject[0]);
  }

  /**
   * <p>
   * Creates a new instance of type {@link WorkspaceImpl}.
   * </p>
   */
  public WorkspaceImpl() {
    this._projects = new Hashtable<String, EclipseProject>();
  }

  public void registerEclipseProject(EclipseProject eclipseProject) {
    Assure.paramNotNull("eclipseProject", eclipseProject);

    // we have to use the specified name here instead of the directory name
    String key = eclipseProject.getSpecifiedName();

    if (this._projects.containsKey(key) && !eclipseProject.equals(this._projects.get(key))) {

      throw new Ant4EclipseException(PlatformExceptionCode.PROJECT_WITH_SAME_SPECIFIED_NAME_ALREADY_EXISTS,
          this._projects.get(key), eclipseProject);
    }

    this._projects.put(key, eclipseProject);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public int hashCode() {
    int prime = 31;
    int result = 1;
    result = prime * result + ((this._projects == null) ? 0 : this._projects.hashCode());
    return result;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    WorkspaceImpl other = (WorkspaceImpl) obj;
    if (this._projects == null) {
      if (other._projects != null) {
        return false;
      }
    } else if (!this._projects.equals(other._projects)) {
      return false;
    }
    return true;
  }
} /* ENDCLASS */