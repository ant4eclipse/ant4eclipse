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
package org.ant4eclipse.ant.platform.internal.team;

import java.io.File;

import net.sf.ant4eclipse.core.Assert;
import net.sf.ant4eclipse.core.exception.Ant4EclipseException;
import net.sf.ant4eclipse.core.logging.A4ELogging;

import org.ant4eclipse.ant.platform.team.TeamExceptionCode;
import org.ant4eclipse.model.platform.team.cvssupport.projectset.CvsTeamProjectDescription;
import org.ant4eclipse.model.platform.team.projectset.TeamProjectDescription;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.taskdefs.Cvs;

/**
 * <p>
 * Implements an adapter for accessing cvs.
 * </p>
 * 
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 */
public class CvsAdapter extends VcsAdapter {

  /** indicates if the cvs commands should be executed quiet */
  private final boolean _quiet;

  /** indicates if the cvs commands should be executed really quiet */
  private final boolean _reallyQuiet;

  private final String  _tag;

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
  public CvsAdapter(final Project antProject, final boolean quiet, final boolean reallyquiet, final String tag) {
    super(antProject);
    this._quiet = quiet;
    this._reallyQuiet = reallyquiet;
    this._tag = tag;
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
   */
  protected void export(final File destination, final TeamProjectDescription projectDescription) {
    Assert.isDirectory(destination);
    Assert.notNull(projectDescription);
    Assert.assertTrue(projectDescription instanceof CvsTeamProjectDescription,
        "ProjectDescription must be a CvsTeamProjectDescription");

    final CvsTeamProjectDescription cvsTeamProjectDescription = (CvsTeamProjectDescription) projectDescription;

    A4ELogging.debug("export(%s, %s)", new Object[] { destination, projectDescription });

    final Cvs cvs = createCvsTask(destination, cvsTeamProjectDescription);
    final String nameInRepository = quote(cvsTeamProjectDescription.getNameInRepository());
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
    } catch (final Exception e) {
      throw new Ant4EclipseException(TeamExceptionCode.ERROR_WHILE_EXECUTING_CVS_COMMAND, new Object[] {
          cvs.getCommand(), e.toString() }, e);
    }
  }

  /**
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
  protected void update(final File destination, final TeamProjectDescription projectDescription)
      throws Ant4EclipseException {
    Assert.isDirectory(destination);
    Assert.notNull(projectDescription);
    Assert.assertTrue(projectDescription instanceof CvsTeamProjectDescription,
        "ProjectDescription must be a CvsTeamProjectDescription");

    A4ELogging.debug("update(%s, %s)", new Object[] { destination, projectDescription });
    final CvsTeamProjectDescription cvsTeamProjectDescription = (CvsTeamProjectDescription) projectDescription;
    final Cvs cvs = createCvsTask(destination, cvsTeamProjectDescription);
    final File projectFolder = new File(destination, projectDescription.getProjectName());
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
    } catch (final Exception e) {
      throw new Ant4EclipseException(TeamExceptionCode.ERROR_WHILE_EXECUTING_CVS_COMMAND, new Object[] {
          cvs.getCommand(), e.toString() }, e);
    }
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
   * @throws VcsException
   */
  protected void checkout(final File destination, final TeamProjectDescription projectDescription)
      throws Ant4EclipseException {
    Assert.isDirectory(destination);
    Assert.notNull(projectDescription);
    Assert.assertTrue(projectDescription instanceof CvsTeamProjectDescription,
        "ProjectDescription must be a CvsTeamProjectDescription");

    A4ELogging.debug("checkout(%s, %s)", new Object[] { destination, projectDescription });
    final CvsTeamProjectDescription cvsTeamProjectDescription = (CvsTeamProjectDescription) projectDescription;

    final Cvs cvs = createCvsTask(destination, cvsTeamProjectDescription);
    final String nameInRepository = quote(cvsTeamProjectDescription.getNameInRepository());
    cvs.setPackage(nameInRepository);

    cvs.setCommand("checkout -d " + quote(projectDescription.getProjectName()));

    if (this._tag != null) {
      cvs.setTag(this._tag);
    } else if (!cvsTeamProjectDescription.isHead()) {
      cvs.setTag(cvsTeamProjectDescription.getBranchOrVersionTag());
    }

    try {
      cvs.execute();
    } catch (final Exception e) {
      throw new Ant4EclipseException(TeamExceptionCode.ERROR_WHILE_EXECUTING_CVS_COMMAND, new Object[] {
          cvs.getCommand(), e.toString() }, e);
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
  private Cvs createCvsTask(final File destination, final CvsTeamProjectDescription projectDescription) {
    final Cvs cvs = (Cvs) getAntProject().createTask("cvs");

    A4ELogging.debug("Created task cvs %s", cvs);

    cvs.setCvsRoot(projectDescription.getResolvedCvsRoot().toString());
    cvs.setDest(destination);

    A4ELogging.debug("CVS, quiet: %s, _reallyQuiet: %s", new Object[] { new Boolean(this._quiet),
        new Boolean(this._reallyQuiet) });
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