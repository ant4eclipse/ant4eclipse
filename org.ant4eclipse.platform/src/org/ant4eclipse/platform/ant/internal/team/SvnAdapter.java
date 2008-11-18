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
package org.ant4eclipse.platform.ant.internal.team;

import java.io.File;
import java.net.MalformedURLException;

import net.sf.ant4eclipse.core.Assert;
import net.sf.ant4eclipse.core.exception.Ant4EclipseException;
import net.sf.ant4eclipse.core.logging.A4ELogging;

import org.ant4eclipse.platform.ant.team.TeamExceptionCode;
import org.ant4eclipse.platform.model.team.projectset.TeamProjectDescription;
import org.ant4eclipse.platform.model.team.svnsupport.projectset.SvnTeamProjectDescription;
import org.ant4eclipse.platform.model.team.svnsupport.projectset.SvnTeamProjectSet;
import org.apache.tools.ant.Project;
import org.tigris.subversion.svnant.SvnTask;
import org.tigris.subversion.svnant.commands.Checkout;
import org.tigris.subversion.svnant.commands.Export;
import org.tigris.subversion.svnant.commands.Update;
import org.tigris.subversion.svnclientadapter.SVNUrl;

/**
 * An adapter to access a subversion repository using the svn-ant task (by polarion)
 * 
 * @author Nils Hartmann (nils@nilshartmann.net)
 */
public class SvnAdapter extends VcsAdapter {

  /**
   * Set to <tt>false</tt> to use command line client interface instead of JNI JavaHL binding. e
   */
  private final boolean _javahl;

  /**
   * Set to false to use command line client interface instead of JavaSVN binding.
   */
  private final boolean _svnkit;

  /**
   * formatter definition used to format/parse dates (e.g. when revision is specified as date).
   */
  private final String  _dateFormatter;

  /**
   * time zone used to format/parse dates (e.g. when revision is specified as date).
   */
  private final String  _dateTimeZone;

  /**
   * 
   * @param antProject
   *          must not be null
   * @param javahl
   *          Set to <tt>false</tt> to use command line client interface instead of JNI JavaHL binding.
   * @param svnkit
   *          Set to <tt>false</tt> to use command line client interface instead of SVNKit binding.
   * @param dateFormatter
   *          might be null
   * @param dateTimeZone
   *          might be null
   */
  public SvnAdapter(Project antProject, boolean javahl, boolean svnkit, String dateFormatter, String dateTimeZone) {
    super(antProject);
    _javahl = javahl;
    _svnkit = svnkit;
    _dateFormatter = dateFormatter;
    _dateTimeZone = dateTimeZone;
  }

  protected void checkout(File destination, TeamProjectDescription projectDescription) throws Ant4EclipseException {
    Assert.isDirectory(destination);
    Assert.notNull(projectDescription);
    Assert.assertTrue(projectDescription instanceof SvnTeamProjectDescription,
        "ProjectDescription must be a SvnTeamProjectDescription");

    SvnTeamProjectDescription svnTeamProjectDescription = (SvnTeamProjectDescription) projectDescription;

    SvnTask task = createSvnTask(svnTeamProjectDescription);
    Checkout checkout = new Checkout();
    checkout.setProject(getAntProject());
    checkout.setTask(task);
    File destPath = new File(destination, svnTeamProjectDescription.getProjectName());
    A4ELogging.debug("Setting dest path for project '%s' to '%s'", new Object[] {
        svnTeamProjectDescription.getProjectName(), destPath });
    checkout.setDestpath(destPath);
    checkout.setRevision(svnTeamProjectDescription.getRevision());
    try {
      checkout.setUrl(new SVNUrl(svnTeamProjectDescription.getUrl()));
    } catch (MalformedURLException e) {
      throw new Ant4EclipseException(TeamExceptionCode.COULD_NOT_BUILD_SVNURL_FOR_PROJECT, new Object[] {
          svnTeamProjectDescription.getUrl(), svnTeamProjectDescription.getProjectName(), e.toString() });
    }
    task.addCheckout(checkout);

    try {
      task.execute();
    } catch (Exception ex) {
      throw new Ant4EclipseException(TeamExceptionCode.ERROR_WHILE_EXECUTING_SVN_COMMAND, new Object[] { "checkout",
          ex.toString() }, ex);
    }

  }

  protected void export(File destination, TeamProjectDescription projectDescription) throws Ant4EclipseException {
    Assert.isDirectory(destination);
    Assert.notNull(projectDescription);
    Assert.assertTrue(projectDescription instanceof SvnTeamProjectDescription,
        "ProjectDescription must be a SvnTeamProjectDescription");

    SvnTeamProjectDescription svnTeamProjectDescription = (SvnTeamProjectDescription) projectDescription;

    SvnTask task = createSvnTask(svnTeamProjectDescription);

    Export export = new Export();
    export.setProject(getAntProject());
    export.setTask(task);

    File destPath = new File(destination, svnTeamProjectDescription.getProjectName());
    A4ELogging.debug("Setting dest path for project '%s' to '%s'", new Object[] {
        svnTeamProjectDescription.getProjectName(), destPath });
    export.setDestPath(destPath);
    export.setRevision(svnTeamProjectDescription.getRevision());
    try {
      export.setSrcUrl(new SVNUrl(svnTeamProjectDescription.getUrl()));
    } catch (MalformedURLException e) {
      throw new Ant4EclipseException(TeamExceptionCode.COULD_NOT_BUILD_SVNURL_FOR_PROJECT, new Object[] {
          svnTeamProjectDescription.getUrl(), svnTeamProjectDescription.getProjectName(), e.toString() });
    }
    task.addExport(export);

    try {
      task.execute();
    } catch (Exception ex) {
      throw new Ant4EclipseException(TeamExceptionCode.ERROR_WHILE_EXECUTING_SVN_COMMAND, new Object[] { "export",
          ex.toString() }, ex);
    }
  }

  protected void update(File destination, TeamProjectDescription projectDescription) throws Ant4EclipseException {
    Assert.isDirectory(destination);
    Assert.notNull(projectDescription);
    Assert.assertTrue(projectDescription instanceof SvnTeamProjectDescription,
        "ProjectDescription must be a SvnTeamProjectDescription");

    SvnTeamProjectDescription svnTeamProjectDescription = (SvnTeamProjectDescription) projectDescription;

    SvnTask task = createSvnTask(svnTeamProjectDescription);

    Update update = new Update();
    update.setProject(getAntProject());
    update.setTask(task);
    File destPath = new File(destination, svnTeamProjectDescription.getProjectName());
    A4ELogging.debug("Setting dest path for project '%s' to '%s'", new Object[] {
        svnTeamProjectDescription.getProjectName(), destPath });
    update.setDir(destPath);
    update.setRevision(svnTeamProjectDescription.getRevision());
    task.addUpdate(update);

    try {
      task.execute();
    } catch (Exception ex) {
      throw new Ant4EclipseException(TeamExceptionCode.ERROR_WHILE_EXECUTING_SVN_COMMAND, new Object[] { "update",
          ex.toString() }, ex);
    }
  }

  /**
   * <p>
   * Creates a Subversion task using the supplied workspace and projects.
   * </p>
   * 
   * @param projectDescription
   *          the description of the shared project.
   * 
   * @return the task used to run a svn command.
   */
  private SvnTask createSvnTask(SvnTeamProjectDescription projectDescription) {

    SvnTask svnTask = (SvnTask) getAntProject().createTask("svn");
    svnTask.setJavahl(_javahl);
    svnTask.setSvnkit(_svnkit);
    if (_dateFormatter != null) {
      svnTask.setDateFormatter(_dateFormatter);
    }

    if (_dateTimeZone != null) {
      svnTask.setDateTimezone(_dateTimeZone);
    }

    A4ELogging.debug("Created svnTask %s", svnTask);

    SvnTeamProjectSet teamProjectSet = projectDescription.getTeamProjectSet();

    if (teamProjectSet.hasUser()) {
      A4ELogging.debug("Setting SVN user '%s'", teamProjectSet.getUser());
      svnTask.setUsername(teamProjectSet.getUser());
    }

    if (teamProjectSet.hasPassword()) {
      A4ELogging.debug("Setting SVN password '%s'", teamProjectSet.getPassword());
      svnTask.setPassword(teamProjectSet.getPassword());
    }

    return svnTask;
  }

}
