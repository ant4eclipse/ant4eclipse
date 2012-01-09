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
package org.ant4eclipse.ant.platform.internal.team;

import org.ant4eclipse.lib.core.exception.Ant4EclipseException;
import org.ant4eclipse.lib.core.util.Utilities;
import org.ant4eclipse.lib.platform.model.team.projectset.TeamProjectDescription;
import org.apache.tools.ant.Project;

import java.io.File;

/**
 * Encapsulates the access to a Version control system.
 * 
 * @author Nils Hartmann (nils@nilshartmann.net)
 */
public abstract class VcsAdapter {

  /** the ant project in which the cvs tasks will be executed */
  private Project _antProject;

  // Assure.notNull( "antProject", antProject );
  public VcsAdapter( Project antProject ) {
    _antProject = antProject;
  }

  /**
   * <p>
   * Runs a CVS checkout operation on a given project.
   * </p>
   * 
   * @param workspace
   *          the current workspace in which the project will be checked out.
   * @param projectDescription
   *          the description of the shared project.
   * @param deleteExisting
   *          indicates if existing projects should be deleted.
   * 
   * @throws VcsException
   *           The CVS operation failed for some reason.
   */
  // Assure.notNull( "projectDescription", projectDescription );
  public final void checkoutProject( File destination, TeamProjectDescription projectDescription, boolean deleteExisting )
      throws Ant4EclipseException {
    String projectName = projectDescription.getProjectName();
    if( deleteExisting && Utilities.hasChild( destination, projectName ) ) {
      Utilities.delete( Utilities.getChild( destination, projectName ) );
    }
    checkout( destination, projectDescription );
  }

  /**
   * <p>
   * Runs a CVS update operation on a given project.
   * </p>
   * 
   * @param destination
   *          the current workspace in which the project will be updated.
   * @param projectDescription
   *          the description of the shared project.
   * 
   * @throws VcsException
   *           The CVS operation failed for some reason.
   */
  // Assure.notNull( "projectDescription", projectDescription );
  public final void updateProject( File destination, TeamProjectDescription projectDescription )
      throws Ant4EclipseException {
    update( destination, projectDescription );
  }

  /**
   * <p>
   * Runs a CVS export operation on a given project.
   * </p>
   * 
   * @param workspace
   *          the current workspace in which the project will be exported.
   * @param projectDescription
   *          the description of the shared project.
   * @param deleteExisting
   *          indicates if existing projects should be deleted.
   * @throws VcsException
   */
  // Assure.notNull( "projectDescription", projectDescription );
  public final void exportProject( File destination, TeamProjectDescription projectDescription, boolean deleteExisting )
      throws Ant4EclipseException {
    String projectName = projectDescription.getProjectName();
    if( deleteExisting && Utilities.hasChild( destination, projectName ) ) {
      Utilities.delete( Utilities.getChild( destination, projectName ) );
    }
    export( destination, projectDescription );
  }

  /**
   * Does the actual export operation. Must be implemented by subclasses to provide VCS-specific actions.
   */
  protected abstract void export( File destination, TeamProjectDescription projectDescription )
      throws Ant4EclipseException;

  /**
   * Does the update export operation. Must be implemented by subclasses to provide VCS-specific actions.
   */
  protected abstract void update( File destination, TeamProjectDescription projectDescription )
      throws Ant4EclipseException;

  /**
   * Does the actual checkout operation. Must be implemented by subclasses to provide VCS-specific actions.
   */
  protected abstract void checkout( File destination, TeamProjectDescription projectDescription )
      throws Ant4EclipseException;

  protected Project getAntProject() {
    return _antProject;
  }

} /* ENDCLASS */
