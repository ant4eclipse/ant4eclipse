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

import java.util.Collection;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import net.sf.ant4eclipse.core.Assert;
import net.sf.ant4eclipse.core.exception.Ant4EclipseException;
import net.sf.ant4eclipse.core.util.MessageCreator;
import net.sf.ant4eclipse.model.platform.PlatformModelExceptionCode;
import net.sf.ant4eclipse.model.platform.resource.EclipseProject;
import net.sf.ant4eclipse.model.platform.resource.Workspace;
import net.sf.ant4eclipse.model.platform.resource.role.ProjectRole;

/**
 * Encapsulates the workspace that contains the (Eclipse-)Projects.
 * 
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 */
public final class WorkspaceImpl implements Workspace {

  /** map with all the eclipse projects */
  private final Map _projects;

  /**
   * @see net.sf.ant4eclipse.model.platform.resource.Workspace#hasProject(java.lang.String)
   */
  public boolean hasProject(final String name) {
    Assert.nonEmpty(name);

    return this._projects.containsKey(name);
  }

  /**
   * @see net.sf.ant4eclipse.model.platform.resource.Workspace#getProject(java.lang.String)
   */
  public EclipseProject getProject(final String name) {
    Assert.nonEmpty(name);

    return ((EclipseProject) this._projects.get(name));
  }

  /**
   * @see net.sf.ant4eclipse.model.platform.resource.Workspace#getProjects(java.lang.String[])
   */
  public EclipseProject[] getProjects(final String[] names, final boolean failOnMissingProjects) {
    Assert.notNull(names);

    // TODO
    return null;
  }

  /**
   * @see net.sf.ant4eclipse.model.platform.resource.Workspace#getAllProjects()
   */
  public EclipseProject[] getAllProjects() {
    final Collection projects = this._projects.values();
    return (EclipseProject[]) projects.toArray(new EclipseProject[0]);
  }

  public EclipseProject[] getAllProjects(final Class projectRole) {
    Assert.notNull(projectRole);
    Assert.assertTrue(ProjectRole.class.isAssignableFrom(projectRole), MessageCreator.createMessage(
        "Class '%s' mst be assignable from class '%s'", new Object[] { projectRole.getClass().getName(),
            ProjectRole.class.getName() }));

    final List result = new LinkedList();
    final Collection projects = this._projects.values();
    for (final Iterator iterator = projects.iterator(); iterator.hasNext();) {
      final EclipseProject eclipseProject = (EclipseProject) iterator.next();
      if (eclipseProject.hasRole(projectRole)) {
        result.add(eclipseProject);
      }
    }

    return (EclipseProject[]) result.toArray(new EclipseProject[0]);
  }

  /**
   * 
   */
  WorkspaceImpl() {
    this._projects = new Hashtable();
  }

  void registerEclipseProject(final EclipseProject eclipseProject) {
    Assert.notNull(eclipseProject);

    // we have to use the specified name here instead of the directory name
    final String key = eclipseProject.getSpecifiedName();

    if (this._projects.containsKey(key) && !eclipseProject.equals(this._projects.get(key))) {

      throw new Ant4EclipseException(PlatformModelExceptionCode.PROJECT_WITH_SAME_SPECIFIED_NAME_ALREADY_EXISTS,
          new Object[] { this._projects.get(key), eclipseProject });
    }

    this._projects.put(key, eclipseProject);
  }

  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((this._projects == null) ? 0 : this._projects.hashCode());
    return result;
  }

  public boolean equals(final Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    final WorkspaceImpl other = (WorkspaceImpl) obj;
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