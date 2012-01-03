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

import org.ant4eclipse.lib.core.logging.Ant4EclipseLogger;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.Target;
import org.apache.tools.ant.Task;

/**
 * @author Daniel Kasmeroglu (daniel.kasmeroglu@kasisoft.net)
 */
public class AntBasedLogger implements Ant4EclipseLogger {

  /** the (thread local) context */
  private ThreadLocal<Object> _context = new ThreadLocal<Object>();

  /**
   * <p>
   * </p>
   */
  public AntBasedLogger() {
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Integer getPriority() {
    return Integer.valueOf(-1);
  }
  
  /**
   * {@inheritDoc}
   */
  @Override
  public void setContext(Object context) {
    this._context.set(context);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isDebuggingEnabled() {
    return true;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isTraceingEnabled() {
    return true;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void debug(String msg, Object... args) {
    log(Project.MSG_VERBOSE, msg, args);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void trace(String msg, Object... args) {
    log(Project.MSG_DEBUG, msg, args);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void info(String msg, Object... args) {
    log(Project.MSG_INFO, msg, args);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void warn(String msg, Object... args) {
    log(Project.MSG_WARN, msg, args);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void error(String msg, Object... args) {
    log(Project.MSG_ERR, msg, args);
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
    if( ctx instanceof Task ) {
      // log with task context
      Task task = (Task) ctx;
      task.getProject().log(task, String.format(msg, args), msgLevel);
    } else if( ctx instanceof Target ) {
      // log with target context
      Target target = (Target) ctx;
      target.getProject().log(target, String.format(msg, args), msgLevel);
    } else if( ctx instanceof Project ) {
      // log without context
      Project project = (Project) ctx;
      project.log(String.format(msg, args), msgLevel);
    } else {
      // temporary solution as it's currently possible to "run" initializations before
      // the execution of a task actually takes place
      System.out.println(String.format(msg, args));
    }
  }

} /* ENDCLASS */
