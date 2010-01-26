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

import org.ant4eclipse.core.exception.Ant4EclipseException;
import org.ant4eclipse.core.logging.A4ELogging;

import org.ant4eclipse.platform.model.team.projectset.TeamProjectDescription;
import org.ant4eclipse.platform.model.team.svnsupport.projectset.SvnTeamProjectDescription;
import org.ant4eclipse.platform.model.team.svnsupport.projectset.SvnTeamProjectSet;

import org.ant4eclipse.lib.core.Assert;
import org.ant4eclipse.lib.platform.PlatformExceptionCode;
import org.apache.tools.ant.Project;
import org.tigris.subversion.svnant.SvnTask;
import org.tigris.subversion.svnant.commands.Checkout;
import org.tigris.subversion.svnant.commands.Export;
import org.tigris.subversion.svnant.commands.Update;
import org.tigris.subversion.svnclientadapter.SVNUrl;

import java.io.File;
import java.net.MalformedURLException;

/**
 * An adapter to access a subversion repository using the svn-ant task (by polarion)
 * 
 * @author Nils Hartmann (nils@nilshartmann.net)
 */
public class SvnAdapter extends VcsAdapter {

  /**
   * Set to <tt>false</tt> to use command line client interface instead of JNI JavaHL binding. e
   */
  private boolean _javahl;

  /**
   * Set to false to use command line client interface instead of JavaSVN binding.
   */
  private boolean _svnkit;

  /**
   * formatter definition used to format/parse dates (e.g. when revision is specified as date).
   */
  private String  _dateFormatter;

  /**
   * time zone used to format/parse dates (e.g. when revision is specified as date).
   */
  private String  _dateTimeZone;

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
    this._javahl = javahl;
    this._svnkit = svnkit;
    this._dateFormatter = dateFormatter;
    this._dateTimeZone = dateTimeZone;
  }

  /**
   * {@inheritDoc}
   */
  @Override
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
    A4ELogging
        .debug("Setting dest path for project '%s' to '%s'", svnTeamProjectDescription.getProjectName(), destPath);
    checkout.setDestpath(destPath);
    checkout.setRevision(svnTeamProjectDescription.getRevision());
    try {
      checkout.setUrl(new SVNUrl(svnTeamProjectDescription.getUrl()));
    } catch (MalformedURLException e) {
      throw new Ant4EclipseException(PlatformExceptionCode.COULD_NOT_BUILD_SVNURL_FOR_PROJECT,
          svnTeamProjectDescription.getUrl(), svnTeamProjectDescription.getProjectName(), e.toString());
    }
    task.addCheckout(checkout);

    try {
      task.execute();
    } catch (Exception ex) {
      throw new Ant4EclipseException(ex, PlatformExceptionCode.ERROR_WHILE_EXECUTING_SVN_COMMAND, "checkout", ex
          .toString());
    }

  }

  /**
   * {@inheritDoc}
   */
  @Override
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
    A4ELogging
        .debug("Setting dest path for project '%s' to '%s'", svnTeamProjectDescription.getProjectName(), destPath);
    export.setDestPath(destPath);
    export.setRevision(svnTeamProjectDescription.getRevision());
    try {
      export.setSrcUrl(new SVNUrl(svnTeamProjectDescription.getUrl()));
    } catch (MalformedURLException e) {
      throw new Ant4EclipseException(PlatformExceptionCode.COULD_NOT_BUILD_SVNURL_FOR_PROJECT,
          svnTeamProjectDescription.getUrl(), svnTeamProjectDescription.getProjectName(), e.toString());
    }
    task.addExport(export);

    try {
      task.execute();
    } catch (Exception ex) {
      throw new Ant4EclipseException(ex, PlatformExceptionCode.ERROR_WHILE_EXECUTING_SVN_COMMAND, "export", ex
          .toString());
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
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
    A4ELogging
        .debug("Setting dest path for project '%s' to '%s'", svnTeamProjectDescription.getProjectName(), destPath);
    update.setDir(destPath);
    update.setRevision(svnTeamProjectDescription.getRevision());
    task.addUpdate(update);

    try {
      task.execute();
    } catch (Exception ex) {
      throw new Ant4EclipseException(ex, PlatformExceptionCode.ERROR_WHILE_EXECUTING_SVN_COMMAND, "update", ex
          .toString());
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
    svnTask.setJavahl(this._javahl);
    svnTask.setJavasvn(this._svnkit);
    if (this._dateFormatter != null) {
      svnTask.setDateFormatter(this._dateFormatter);
    }

    if (this._dateTimeZone != null) {
      svnTask.setDateTimezone(this._dateTimeZone);
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
