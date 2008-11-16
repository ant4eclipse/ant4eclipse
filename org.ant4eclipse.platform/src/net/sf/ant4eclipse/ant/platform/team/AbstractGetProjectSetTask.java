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
package net.sf.ant4eclipse.ant.platform.team;

import net.sf.ant4eclipse.ant.platform.internal.team.VcsAdapter;
import net.sf.ant4eclipse.ant.platform.internal.team.VcsException;
import net.sf.ant4eclipse.core.Assert;
import net.sf.ant4eclipse.core.logging.A4ELogging;
import net.sf.ant4eclipse.model.platform.resource.Workspace;
import net.sf.ant4eclipse.model.platform.team.projectset.TeamProjectDescription;
import net.sf.ant4eclipse.model.platform.team.projectset.TeamProjectSet;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.types.EnumeratedAttribute;

public abstract class AbstractGetProjectSetTask extends AbstractProjectSetBasedTask {

  /**  */
  private static final String CHECKOUT = "checkout";

  /**  */
  private static final String UPDATE = "update";

  /**  */
  private static final String EXPORT = "export";
  
  /**
   * Comment for <code>_command</code>
   */
  private VcsCommand _command = new VcsCommand(CHECKOUT);

  private VcsAdapter _vcsAdapter;
  
  private String _username;
  
  private String _password;
  
  private boolean _deleteExistingProjects = true;
  
  public String getPassword() {
    return _password;
  }

  public void setPassword(String password) {
    _password = password;
  }

  public String getUsername() {
    return _username;
  }

  public void setUsername(String username) {
    _username = username;
  }
  
  public boolean isDeleteExistingProjects() {
    return _deleteExistingProjects;
  }

  public void setDeleteExistingProjects(boolean deleteExistingProjects) {
    _deleteExistingProjects = deleteExistingProjects;
  }

  /**
   * @return Returns the command.
   */
  public VcsCommand getCommand() {
    return _command;
  }

  /**
   * @param command
   *          The command to set.
   */
  public void setCommand(VcsCommand command) {
    Assert.notNull(command);

    _command = command;
  }
  
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
    public String[] getValues() {
      return new String[] { CHECKOUT, UPDATE, EXPORT };
    }
  }
  
  /**
   * Overwrite in subclasses to check additional prerequisits
   *
   */
  protected abstract void checkPrereqs();
  
  /**
   * {@inheritDoc}
   */
  public void execute() throws BuildException {
    // check mandatory attributes..
    requireWorkspaceSet();
    requireProjectSetSet();
    requiresCommandSet();
    checkPrereqs();
    
    _vcsAdapter = createVcsAdapter();
    A4ELogging.debug("using version control adapter = ", _vcsAdapter);

    try {
      // set user and password
      getProjectSet().setUserAndPassword(getUsername(), getPassword());

      if (getCommand().getValue().equals(CHECKOUT)) {
        checkoutProjectSet(getWorkspace(), getProjectSet(), isDeleteExistingProjects());
      }
      else if (getCommand().getValue().equals(UPDATE)) {
        updateProjectSet(getWorkspace(), getProjectSet());
      }
      if (getCommand().getValue().equals(EXPORT)) {
        exportProjectSet(getWorkspace(), getProjectSet(), isDeleteExistingProjects());
      }
    } catch (BuildException ex) {
      throw ex;
    } catch (Exception e) {
      A4ELogging.error(e.getMessage());
      throw new BuildException(e.getMessage(), e);
    }
  }



  protected abstract VcsAdapter createVcsAdapter();


  /**
   * 
   */
  private void requiresCommandSet() {
    // check that command is set..
    if (getCommand() == null) {
      throw new BuildException("command has to be set!");
    }
  }

  /**
   * @param workspace
   * @param projectSet
   * @param deleteExisting
   * @throws VcsException
   */
  public void checkoutProjectSet(Workspace workspace, TeamProjectSet projectSet, boolean deleteExisting) throws VcsException {
    Assert.notNull(workspace);
    Assert.notNull(projectSet);

    A4ELogging.debug("checkoutProjectSet(%s, %s, %s)", 
      new Object[] { workspace, projectSet, new Boolean(deleteExisting) });
    
    TeamProjectDescription[] _teamProjectDescription = projectSet.getTeamProjectDescriptions();

    for (int i = 0; i < _teamProjectDescription.length; i++) {
      TeamProjectDescription teamProjectDescription = _teamProjectDescription[i];
      _vcsAdapter.checkoutProject(workspace, teamProjectDescription, deleteExisting);
    }
  }

  /**
   * @param workspace
   * @param projectSet
   * @param deleteExisting
   * @throws VcsException
   */
  public void exportProjectSet(Workspace workspace, TeamProjectSet projectSet, boolean deleteExisting) throws VcsException {
    Assert.notNull(workspace);
    Assert.notNull(projectSet);

    TeamProjectDescription[] descriptions = projectSet.getTeamProjectDescriptions();

    for (int i = 0; i < descriptions.length; i++) {
      TeamProjectDescription description = descriptions[i];
      _vcsAdapter.exportProject(workspace, description, deleteExisting);
    }
  }

  /**
   * @param workspace
   * @param projectSet
   * @throws VcsException
   */
  public void updateProjectSet(Workspace workspace, TeamProjectSet projectSet) throws VcsException {
    Assert.notNull(workspace);
    Assert.notNull(projectSet);

    TeamProjectDescription[] descriptions = projectSet.getTeamProjectDescriptions();

    for (int i = 0; i < descriptions.length; i++) {
      TeamProjectDescription description = descriptions[i];
      _vcsAdapter.updateProject(workspace, description);
    }
  }
}
