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
package org.ant4eclipse.platform.internal.ant.team;

import org.ant4eclipse.platform.PlatformExceptionCode;
import org.ant4eclipse.platform.model.team.cvssupport.projectset.CvsTeamProjectDescription;
import org.ant4eclipse.platform.model.team.projectset.TeamProjectDescription;

import org.ant4eclipse.lib.core.Assure;
import org.ant4eclipse.lib.core.exception.Ant4EclipseException;
import org.ant4eclipse.lib.core.logging.A4ELogging;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.taskdefs.Cvs;

import java.io.File;

/**
 * <p>
 * Implements an adapter for accessing cvs.
 * </p>
 * 
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 */
public class CvsAdapter extends VcsAdapter {

  /** indicates if the cvs commands should be executed quiet */
  private boolean _quiet;

  /** indicates if the cvs commands should be executed really quiet */
  private boolean _reallyQuiet;

  private String  _tag;

  /**
   * <p>
   * Initializes this CVS adapter.
   * </p>
   * 
   * @param antProject
   *          the ant project in which the cvs tasks will be executed.
   * @param quiet
   *          indicates if the cvs commands should be executed quiet.
   * @param reallyquiet
   *          indicates if the cvs commands should be executed really quiet.
   */
  public CvsAdapter(Project antProject, boolean quiet, boolean reallyquiet, String tag) {
    super(antProject);
    this._quiet = quiet;
    this._reallyQuiet = reallyquiet;
    this._tag = tag;
  }

  /**
   * {@inheritDoc}
   * 
   * <p>
   * Runs a CVS export operation on a given project.
   * </p>
   * 
   * @param workspace
   *          the current workspace in which the project will be exported.
   * @param projectDescription
   *          the description of the shared project.
   */
  @Override
  protected void export(File destination, TeamProjectDescription projectDescription) {
    Assure.isDirectory(destination);
    Assure.notNull(projectDescription);
    Assure.assertTrue(projectDescription instanceof CvsTeamProjectDescription,
        "ProjectDescription must be a CvsTeamProjectDescription");

    CvsTeamProjectDescription cvsTeamProjectDescription = (CvsTeamProjectDescription) projectDescription;

    A4ELogging.debug("export(%s, %s)", destination, projectDescription);

    Cvs cvs = createCvsTask(destination, cvsTeamProjectDescription);
    String nameInRepository = quote(cvsTeamProjectDescription.getNameInRepository());
    cvs.setCommand("export -d " + quote(projectDescription.getProjectName()));
    cvs.setPackage(nameInRepository);

    if (this._tag != null) {
      cvs.setTag(this._tag);
    } else if (!cvsTeamProjectDescription.isHead()) {
      cvs.setTag(cvsTeamProjectDescription.getBranchOrVersionTag());
    } else {
      cvs.setDate("NOW");
    }

    try {
      cvs.execute();
    } catch (Exception e) {
      throw new Ant4EclipseException(e, PlatformExceptionCode.ERROR_WHILE_EXECUTING_CVS_COMMAND, cvs.getCommand(), e
          .toString());
    }
  }

  /**
   * {@inheritDoc}
   * 
   * <p>
   * Runs a CVS update operation on a given project.
   * </p>
   * 
   * @param workspace
   *          the current workspace in which the project will be updated.
   * @param projectDescription
   *          the description of the shared project.
   * 
   * @throws VcsException
   *           The CVS operation failed for some reason.
   */
  @Override
  protected void update(File destination, TeamProjectDescription projectDescription) throws Ant4EclipseException {
    Assure.isDirectory(destination);
    Assure.notNull(projectDescription);
    Assure.assertTrue(projectDescription instanceof CvsTeamProjectDescription,
        "ProjectDescription must be a CvsTeamProjectDescription");

    A4ELogging.debug("update(%s, %s)", destination, projectDescription);
    CvsTeamProjectDescription cvsTeamProjectDescription = (CvsTeamProjectDescription) projectDescription;
    Cvs cvs = createCvsTask(destination, cvsTeamProjectDescription);
    File projectFolder = new File(destination, projectDescription.getProjectName());
    cvs.setDest(projectFolder);
    // -d: Create any directories that exist in the repository if they're missing from the working directory.
    // Normally, update acts only on directories and files that were already enrolled in your working directory.
    cvs.setCommand("update -d");

    if (this._tag != null) {
      cvs.setTag(this._tag);
    } else if (!cvsTeamProjectDescription.isHead()) {
      cvs.setTag(cvsTeamProjectDescription.getBranchOrVersionTag());
    }

    try {
      cvs.execute();
    } catch (Exception e) {
      throw new Ant4EclipseException(e, PlatformExceptionCode.ERROR_WHILE_EXECUTING_CVS_COMMAND, cvs.getCommand(), e
          .toString());
    }
  }

  /**
   * {@inheritDoc}
   * 
   * <p>
   * Runs a CVS checkout operation on a given project.
   * </p>
   * 
   * @param workspace
   *          the current workspace in which the project will be checked out.
   * @param projectDescription
   *          the description of the shared project.
   * @throws VcsException
   */
  @Override
  protected void checkout(File destination, TeamProjectDescription projectDescription) throws Ant4EclipseException {
    Assure.isDirectory(destination);
    Assure.notNull(projectDescription);
    Assure.assertTrue(projectDescription instanceof CvsTeamProjectDescription,
        "ProjectDescription must be a CvsTeamProjectDescription");

    A4ELogging.debug("checkout(%s, %s)", destination, projectDescription);
    CvsTeamProjectDescription cvsTeamProjectDescription = (CvsTeamProjectDescription) projectDescription;

    Cvs cvs = createCvsTask(destination, cvsTeamProjectDescription);
    String nameInRepository = quote(cvsTeamProjectDescription.getNameInRepository());
    cvs.setPackage(nameInRepository);

    cvs.setCommand("checkout -d " + quote(projectDescription.getProjectName()));

    if (this._tag != null) {
      cvs.setTag(this._tag);
    } else if (!cvsTeamProjectDescription.isHead()) {
      cvs.setTag(cvsTeamProjectDescription.getBranchOrVersionTag());
    }

    try {
      cvs.execute();
    } catch (Exception e) {
      throw new Ant4EclipseException(e, PlatformExceptionCode.ERROR_WHILE_EXECUTING_CVS_COMMAND, cvs.getCommand(), e
          .toString());
    }
  }

  /**
   * <p>
   * Creates a CVS task using the supplied workspace and projects.
   * </p>
   * 
   * @param workspace
   *          the current workspace.
   * @param projectDescription
   *          the description of the shared project.
   * 
   * @return the task used to run a CVS command.
   */
  private Cvs createCvsTask(File destination, CvsTeamProjectDescription projectDescription) {
    Cvs cvs = (Cvs) getAntProject().createTask("cvs");

    A4ELogging.debug("Created task cvs %s", cvs);

    cvs.setCvsRoot(projectDescription.getResolvedCvsRoot().toString());
    cvs.setDest(destination);

    A4ELogging.debug("CVS, quiet: %s, _reallyQuiet: %s", Boolean.valueOf(this._quiet), Boolean
        .valueOf(this._reallyQuiet));
    cvs.setQuiet(this._quiet);
    cvs.setReallyquiet(this._reallyQuiet);
    return cvs;
  }

  /**
   * <p>
   * Quotes ("...") the given string. If the specified string already is quoted, the method returns with no effect.
   * </p>
   * 
   * @param string
   *          the string to quote.
   * @return the quoted string.
   */
  private String quote(String string) {
    if (!string.startsWith("\"")) {
      string = "\"" + string + "\"";
    }
    return string;
  }
}