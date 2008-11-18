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

import java.util.Collection;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.ant4eclipse.model.platform.PlatformModelExceptionCode;
import org.ant4eclipse.model.platform.resource.EclipseProject;
import org.ant4eclipse.model.platform.resource.Workspace;
import org.ant4eclipse.model.platform.resource.role.ProjectRole;

import net.sf.ant4eclipse.core.Assert;
import net.sf.ant4eclipse.core.exception.Ant4EclipseException;
import net.sf.ant4eclipse.core.util.MessageCreator;

/**
 * <p>
 * Encapsulates the workspace that contains the eclipse projects.
 * </p>
 * 
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 */
public final class WorkspaceImpl implements Workspace {

  /** map with all the eclipse projects */
  private final Map<String, EclipseProject> _projects;

  /**
   * {@inheritDoc}}
   */
  public boolean hasProject(final String name) {
    Assert.nonEmpty(name);

    return this._projects.containsKey(name);
  }

  /**
   * {@inheritDoc}}
   */
  public EclipseProject getProject(final String name) {
    Assert.nonEmpty(name);

    return this._projects.get(name);
  }

  /**
   * {@inheritDoc}}
   */
  public EclipseProject[] getProjects(final String[] names, final boolean failOnMissingProjects) {
    Assert.notNull(names);

    // TODO
    return null;
  }

  /**
   * {@inheritDoc}}
   */
  public EclipseProject[] getAllProjects() {
    final Collection<EclipseProject> projects = this._projects.values();
    return projects.toArray(new EclipseProject[0]);
  }

  public EclipseProject[] getAllProjects(final Class<? extends ProjectRole> projectRole) {
    Assert.notNull(projectRole);
    Assert.assertTrue(ProjectRole.class.isAssignableFrom(projectRole), MessageCreator.createMessage(
        "Class '%s' must be assignable from class '%s'", new Object[] { projectRole.getClass().getName(),
            ProjectRole.class.getName() }));

    final List<EclipseProject> result = new LinkedList<EclipseProject>();
    final Collection<EclipseProject> projects = this._projects.values();
    for (final Iterator<EclipseProject> iterator = projects.iterator(); iterator.hasNext();) {
      final EclipseProject eclipseProject = iterator.next();
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
  WorkspaceImpl() {
    this._projects = new Hashtable<String, EclipseProject>();
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