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
package org.ant4eclipse.platform.ant.team;

import org.ant4eclipse.core.Assert;
import org.ant4eclipse.core.exception.Ant4EclipseException;
import org.ant4eclipse.core.logging.A4ELogging;

import org.ant4eclipse.platform.ant.core.task.AbstractTeamProjectSetBasedTask;
import org.ant4eclipse.platform.internal.ant.team.VcsAdapter;
import org.ant4eclipse.platform.model.team.projectset.TeamProjectDescription;
import org.ant4eclipse.platform.model.team.projectset.TeamProjectSet;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.types.EnumeratedAttribute;

import java.io.File;

public abstract class AbstractGetProjectSetTask extends AbstractTeamProjectSetBasedTask {

  /** Value for the 'command' parameter, that indicates that the projects should be checked out from version control */
  private static final String CHECKOUT                = "checkout";

  /** Value for the 'command' parameter, that indicates that the projects should be updated from version control */
  private static final String UPDATE                  = "update";

  /** Value for the 'command' parameter, that indicates that the projects should be exported from version control */
  private static final String EXPORT                  = "export";

  /**
   * Comment for <code>_command</code>
   */
  private VcsCommand          _command                = new VcsCommand(CHECKOUT);

  private VcsAdapter          _vcsAdapter;

  private String              _username;

  private String              _password;

  private File                _destination;

  private boolean             _deleteExistingProjects = true;

  public String getPassword() {
    return this._password;
  }

  public File getDestination() {
    return this._destination;
  }

  /**
   * Sets the destination directory.
   * 
   * @param destination
   */
  public void setDestination(File destination) {
    this._destination = destination;
  }

  public void setPassword(String password) {
    this._password = password;
  }

  public String getUsername() {
    return this._username;
  }

  public void setUsername(String username) {
    this._username = username;
  }

  public boolean isDeleteExistingProjects() {
    return this._deleteExistingProjects;
  }

  public void setDeleteExistingProjects(boolean deleteExistingProjects) {
    this._deleteExistingProjects = deleteExistingProjects;
  }

  /**
   * @return Returns the command.
   */
  public VcsCommand getCommand() {
    return this._command;
  }

  /**
   * @param command
   *          The command to set.
   */
  public void setCommand(VcsCommand command) {
    Assert.notNull(command);

    this._command = command;
  }

  /**
   * Overwrite in subclasses to check additional prerequisits
   * 
   */
  protected abstract void checkPrereqs();

  /**
   * {@inheritDoc}
   */
  @Override
  public void doExecute() throws BuildException {
    // check mandatory attributes..
    requireDestinationSet();
    requireProjectSetSet();
    requireCommandSet();
    checkPrereqs();

    this._vcsAdapter = createVcsAdapter();
    A4ELogging.debug("using version control adapter = ", this._vcsAdapter);

    // set user and password
    getProjectSet().setUserAndPassword(getUsername(), getPassword());

    if (getCommand().getValue().equals(CHECKOUT)) {
      checkoutProjectSet(getDestination(), getProjectSet(), isDeleteExistingProjects());
    } else if (getCommand().getValue().equals(UPDATE)) {
      updateProjectSet(getDestination(), getProjectSet());
    }
    if (getCommand().getValue().equals(EXPORT)) {
      exportProjectSet(getDestination(), getProjectSet(), isDeleteExistingProjects());
    }
  }

  protected abstract VcsAdapter createVcsAdapter();

  /**
   * Ensures that the destination-Parameter has been set correctly
   */
  private void requireDestinationSet() {
    if (getDestination() == null || !getDestination().isDirectory()) {
      throw new BuildException("Parameter 'destination' must be set to an existing directory");
    }
  }

  /**
   * 
   */
  private void requireCommandSet() {
    // check that command is set..
    if (getCommand() == null) {
      throw new BuildException("command has to be set!");
    }
  }

  /**
   * @param workspace
   * @param projectSet
   * @param deleteExisting
   */
  public void checkoutProjectSet(File destination, TeamProjectSet projectSet, boolean deleteExisting)
      throws Ant4EclipseException {
    Assert.isDirectory(destination);
    Assert.notNull(projectSet);

    A4ELogging.debug("checkoutProjectSet(%s, %s, %s)", destination, projectSet,
        Boolean.valueOf(deleteExisting));

    TeamProjectDescription[] _teamProjectDescription = projectSet.getTeamProjectDescriptions();

    for (TeamProjectDescription teamProjectDescription : _teamProjectDescription) {
      this._vcsAdapter.checkoutProject(destination, teamProjectDescription, deleteExisting);
    }
  }

  /**
   * @param workspace
   * @param projectSet
   * @param deleteExisting
   */
  public void exportProjectSet(File destination, TeamProjectSet projectSet, boolean deleteExisting)
      throws Ant4EclipseException {
    Assert.isDirectory(destination);
    Assert.notNull(projectSet);

    TeamProjectDescription[] descriptions = projectSet.getTeamProjectDescriptions();

    for (TeamProjectDescription description : descriptions) {
      this._vcsAdapter.exportProject(destination, description, deleteExisting);
    }
  }

  /**
   * @param workspace
   * @param projectSet
   */
  public void updateProjectSet(File destination, TeamProjectSet projectSet) throws Ant4EclipseException {
    Assert.isDirectory(destination);
    Assert.notNull(projectSet);

    TeamProjectDescription[] descriptions = projectSet.getTeamProjectDescriptions();

    for (TeamProjectDescription description : descriptions) {
      this._vcsAdapter.updateProject(destination, description);
    }
  }

  /**
   * Represents allowed values for the 'command'-parameter of the task
   * 
   * @author Nils Hartmann <nils@nilshartmann.net>
   * @version $Revision$
   */
  public static class VcsCommand extends EnumeratedAttribute {

    public VcsCommand() {
      // needed by Ant to instantiate
    }

    public VcsCommand(String value) {
      super();
      setValue(value);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String[] getValues() {
      return new String[] { CHECKOUT, UPDATE, EXPORT };
    }
  }
}
