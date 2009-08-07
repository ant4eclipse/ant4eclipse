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
package org.ant4eclipse.core.ant;

import org.ant4eclipse.core.Assert;
import org.ant4eclipse.core.logging.Ant4EclipseLogger;

import org.apache.tools.ant.BuildEvent;
import org.apache.tools.ant.BuildListener;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.Target;
import org.apache.tools.ant.Task;

/**
 * @author Daniel Kasmeroglu (daniel.kasmeroglu@kasisoft.net)
 */
public class AntBasedLogger implements Ant4EclipseLogger, BuildListener {

  /** the ant project */
  private Project                   _project = null;

  /** the (thread local) context */
  private final ThreadLocal<Object> _context = new ThreadLocal<Object>();

  /**
   * <p>
   * </p>
   * 
   * @param project
   */
  public AntBasedLogger(final Project project) {
    Assert.notNull(project);

    this._project = project;
    this._project.addBuildListener(this);
  }

  /**
   * {@inheritDoc}
   */
  public void setContext(final Object context) {
    this._context.set(context);
  }

  /**
   * {@inheritDoc}
   */
  public boolean isDebuggingEnabled() {
    return true;
  }

  /**
   * {@inheritDoc}
   */
  public boolean isTraceingEnabled() {
    return true;
  }

  /**
   * {@inheritDoc}
   */
  public void debug(final String msg, final Object... args) {
    log(Project.MSG_VERBOSE, msg, args);
  }

  /**
   * {@inheritDoc}
   */
  public void trace(final String msg, final Object... args) {
    log(Project.MSG_DEBUG, msg, args);
  }

  /**
   * {@inheritDoc}
   */
  public void info(final String msg, final Object... args) {
    log(Project.MSG_INFO, msg, args);
  }

  /**
   * {@inheritDoc}
   */
  public void warn(final String msg, final Object... args) {
    log(Project.MSG_WARN, msg, args);
  }

  /**
   * {@inheritDoc}
   */
  public void error(final String msg, final Object... args) {
    log(Project.MSG_ERR, msg, args);
  }

  /**
   * {@inheritDoc}
   */
  public void taskStarted(final BuildEvent event) {
    setContext(event.getTask());
  }

  /**
   * {@inheritDoc}
   */
  public void taskFinished(final BuildEvent event) {
    setContext(null);
  }

  /**
   * {@inheritDoc}
   */
  public void targetStarted(final BuildEvent event) {
    setContext(event.getTarget());
  }

  /**
   * {@inheritDoc}
   */
  public void targetFinished(final BuildEvent event) {
    setContext(null);
  }

  /**
   * {@inheritDoc}
   */
  public void messageLogged(final BuildEvent event) {
    // emtpy method block - we don't need this event here...
  }

  /**
   * {@inheritDoc}
   */
  public void buildStarted(final BuildEvent event) {
    // emtpy method block - we don't need this event here...
  }

  /**
   * {@inheritDoc}
   */
  public void buildFinished(final BuildEvent event) {
    // emtpy method block - we don't need this event here...
  }

  /**
   * Logs the supplied message using the currently configured context.
   * 
   * @param msgLevel
   *          The message level used for the logging.
   * @param msg
   *          The message which has to be dumped. Neither <code>null</code> nor empty.
   * @param args
   *          The arguments used to format the message.
   */
  private void log(final int msgLevel, final String msg, final Object... args) {
    // retrieve the context
    final Object ctx = this._context.get();
    if (ctx instanceof Task) {
      // log with task context
      this._project.log((Task) ctx, String.format(msg, args), msgLevel);
    } else if (ctx instanceof Target) {
      // log with target context
      this._project.log((Target) ctx, String.format(msg, args), msgLevel);
    } else {
      // log without context
      this._project.log(String.format(msg, args), msgLevel);
    }
  }

} /* ENDCLASS */
