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
package org.ant4eclipse.ant.core;

import java.util.IllegalFormatException;

import org.ant4eclipse.lib.core.Assure;
import org.ant4eclipse.lib.core.logging.Ant4EclipseLogger;
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
  private Project             _project = null;

  /** the (thread local) context */
  private ThreadLocal<Object> _context = new ThreadLocal<Object>();

  /**
   * <p>
   * </p>
   * 
   * @param project
   */
  public AntBasedLogger(Project project) {
    Assure.notNull("project", project);

    this._project = project;
    this._project.addBuildListener(this);
  }

  /**
   * {@inheritDoc}
   */
  public void setContext(Object context) {
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
  public void debug(String msg, Object... args) {
    log(Project.MSG_VERBOSE, msg, args);
  }

  /**
   * {@inheritDoc}
   */
  public void trace(String msg, Object... args) {
    log(Project.MSG_DEBUG, msg, args);
  }

  /**
   * {@inheritDoc}
   */
  public void info(String msg, Object... args) {
    log(Project.MSG_INFO, msg, args);
  }

  /**
   * {@inheritDoc}
   */
  public void warn(String msg, Object... args) {
    log(Project.MSG_WARN, msg, args);
  }

  /**
   * {@inheritDoc}
   */
  public void error(String msg, Object... args) {
    log(Project.MSG_ERR, msg, args);
  }

  /**
   * {@inheritDoc}
   */
  public void taskStarted(BuildEvent event) {
    setContext(event.getTask());
  }

  /**
   * {@inheritDoc}
   */
  public void taskFinished(BuildEvent event) {
    setContext(null);
  }

  /**
   * {@inheritDoc}
   */
  public void targetStarted(BuildEvent event) {
    setContext(event.getTarget());
  }

  /**
   * {@inheritDoc}
   */
  public void targetFinished(BuildEvent event) {
    setContext(null);
  }

  /**
   * {@inheritDoc}
   */
  public void messageLogged(BuildEvent event) {
    // emtpy method block - we don't need this event here...
  }

  /**
   * {@inheritDoc}
   */
  public void buildStarted(BuildEvent event) {
    // emtpy method block - we don't need this event here...
  }

  /**
   * {@inheritDoc}
   */
  public void buildFinished(BuildEvent event) {
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
  private void log(int msgLevel, String msg, Object... args) {
    // retrieve the context
    Object ctx = this._context.get();
    String message;
    if (args.length > 0) {
      try {
        message = String.format(msg, args);
      } catch (IllegalFormatException e) {
        message = msg;
      }
    } else {
      message = msg;
    }
    if (ctx instanceof Task) {
      // log with task context
      this._project.log((Task) ctx, message, msgLevel);
    } else if (ctx instanceof Target) {
      // log with target context
      this._project.log((Target) ctx, message, msgLevel);
    } else {
      // log without context
      this._project.log(message, msgLevel);
    }
  }

} /* ENDCLASS */
