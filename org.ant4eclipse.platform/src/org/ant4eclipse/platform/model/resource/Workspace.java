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
package org.ant4eclipse.platform.model.resource;

import org.ant4eclipse.platform.model.resource.role.ProjectRole;

/**
 * <p>
 * The central hub for your user's data files is called a workspace. The workspace contains a collection of resources.
 * From the user's perspective, there are three different types of resources: projects, folders, and files. A project is
 * a collection of any number of files and folders.
 * </p>
 * <p>
 * A workspace's resources are organized into a tree structure, with projects at the top, and folders and files
 * underneath. A workspace can have any number of projects, each of which can be stored in a different location in some
 * file system.
 * </p>
 * 
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 */
public interface Workspace {

  /**
   * <p>
   * Returns <code>true</code>, if the {@link Workspace} contains a {@link EclipseProject} with the given name.
   * </p>
   * 
   * @param projectName
   *          the name of the {@link EclipseProject}
   * @return <code>true</code>, if the workspace contains a project with the given name.
   */
  public boolean hasProject(final String projectName);

  /**
   * <p>
   * Returns the {@link EclipseProject} associated with the specified name.
   * </p>
   * 
   * @param projectName
   *          the name of the {@link EclipseProject}.
   * 
   * @return the {@link EclipseProject} associated with the specified name.
   */
  public EclipseProject getProject(final String projectName);

  /**
   * <p>
   * Returns an array of {@link EclipseProject EclipseProjects} with the specified project names.
   * </p>
   * 
   * @param projectNames
   *          the names of the projects
   * @param failOnMissingProjects
   *          if set to <code>true</code>, the method with throw an exception if a requested project is not contained in
   *          the {@link Workspace}.
   * @return an array of {@link EclipseProject EclipseProjects} with the specified project names.
   */
  public EclipseProject[] getProjects(final String[] projectNames, final boolean failOnMissingProjects);

  /**
   * <p>
   * Returns all the {@link EclipseProject EclipseProjects} that are contained in the {@link Workspace}.
   * </p>
   * 
   * @return all the {@link EclipseProject EclipseProjects} that are contained in the {@link Workspace}.
   */
  public EclipseProject[] getAllProjects();

  /**
   * <p>
   * Returns all the {@link EclipseProject EclipseProjects} with the specified project role.
   * </p>
   * 
   * @return all the {@link EclipseProject EclipseProjects} with the specified project role.
   * 
   * @param projectRole
   *          the class of the project role. Has to be assignable from class {@link ProjectRole}.
   * @return all the @link EclipseProject EclipseProjects} with the specified project role.
   */
  public EclipseProject[] getAllProjects(final Class<? extends ProjectRole> projectRole);

} /* ENDCLASS */