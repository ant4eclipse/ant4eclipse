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
  public void debug(final String msg) {
    log(msg, Project.MSG_VERBOSE);
  }

  /**
   * {@inheritDoc}
   */
  public void debug(final String msg, final Object arg) {
    debug(String.format(msg, arg));
  }

  /**
   * {@inheritDoc}
   */
  public void debug(final String msg, final Object[] args) {
    debug(String.format(msg, args));
  }

  /**
   * {@inheritDoc}
   */
  public void trace(final String msg, final Object[] args) {
    trace(String.format(msg, args));
  }

  /**
   * {@inheritDoc}
   */
  public void trace(final String msg, final Object obj) {
    trace(String.format(msg, obj));
  }

  /**
   * {@inheritDoc}
   */
  public void trace(final String msg) {
    log(msg, Project.MSG_DEBUG);
  }

  /**
   * {@inheritDoc}
   */
  public void info(final String msg, final Object[] args) {
    info(String.format(msg, args));
  }

  /**
   * {@inheritDoc}
   */
  public void info(final String msg, final Object obj) {
    info(String.format(msg, obj));
  }

  /**
   * {@inheritDoc}
   */
  public void info(final String msg) {
    log(msg, Project.MSG_INFO);
  }

  /**
   * {@inheritDoc}
   */
  public void warn(final String msg, final Object[] args) {
    warn(String.format(msg, args));
  }

  /**
   * {@inheritDoc}
   */
  public void warn(final String msg, final Object obj) {
    warn(String.format(msg, obj));
  }

  /**
   * {@inheritDoc}
   */
  public void warn(final String msg) {
    log(msg, Project.MSG_WARN);
  }

  /**
   * {@inheritDoc}
   */
  public void error(final String msg, final Object[] args) {
    error(String.format(msg, args));
  }

  /**
   * {@inheritDoc}
   */
  public void error(final String msg, final Object obj) {
    error(String.format(msg, obj));
  }

  /**
   * {@inheritDoc}
   */
  public void error(final String msg) {
    log(msg, Project.MSG_ERR);
  }

  /**
   * @see org.apache.tools.ant.BuildListener#taskStarted(org.apache.tools.ant.BuildEvent)
   */
  public void taskStarted(final BuildEvent event) {
    setContext(event.getTask());
  }

  /**
   * @see org.apache.tools.ant.BuildListener#taskFinished(org.apache.tools.ant.BuildEvent)
   */
  public void taskFinished(final BuildEvent event) {
    setContext(null);
  }

  /**
   * @see org.apache.tools.ant.BuildListener#targetStarted(org.apache.tools.ant.BuildEvent)
   */
  public void targetStarted(final BuildEvent event) {
    setContext(event.getTarget());
  }

  /**
   * @see org.apache.tools.ant.BuildListener#targetFinished(org.apache.tools.ant.BuildEvent)
   */
  public void targetFinished(final BuildEvent event) {
    setContext(null);
  }

  /**
   * @see org.apache.tools.ant.BuildListener#messageLogged(org.apache.tools.ant.BuildEvent)
   */
  public void messageLogged(final BuildEvent event) {
    // emtpy method block - we don't need this event here...
  }

  /**
   * @see org.apache.tools.ant.BuildListener#buildStarted(org.apache.tools.ant.BuildEvent)
   */
  public void buildStarted(final BuildEvent event) {
    // emtpy method block - we don't need this event here...
  }

  /**
   * @see org.apache.tools.ant.BuildListener#buildFinished(org.apache.tools.ant.BuildEvent)
   */
  public void buildFinished(final BuildEvent event) {
    // emtpy method block - we don't need this event here...
  }

  /**
   * <p>
   * </p>
   * 
   * @param msg
   * @param msgLevel
   */
  private void log(final String msg, final int msgLevel) {
    // retrieve the context
    final Object ctx = this._context.get();

    // log with task context
    if (ctx instanceof Task) {
      this._project.log((Task) ctx, msg, msgLevel);
    }
    // log with target context
    else if (ctx instanceof Target) {
      this._project.log((Target) ctx, msg, msgLevel);
    }
    // log without context
    else {
      this._project.log(msg, msgLevel);
    }
  }
} /* ENDCLASS */
