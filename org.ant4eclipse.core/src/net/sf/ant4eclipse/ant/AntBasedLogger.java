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
package net.sf.ant4eclipse.ant;

import net.sf.ant4eclipse.core.logging.Ant4EclipseLogger;
import net.sf.ant4eclipse.core.util.MessageCreator;

import org.apache.tools.ant.Project;
import org.apache.tools.ant.Target;
import org.apache.tools.ant.Task;

/**
 * @author Daniel Kasmeroglu (daniel.kasmeroglu@kasisoft.net)
 */
public class AntBasedLogger implements Ant4EclipseLogger {

  private Project           _project = null;

  private final ThreadLocal _context = new ThreadLocal();

  public AntBasedLogger(final Project project) {
    this._project = project;
  }

  public void setContext(final Object context) {
    this._context.set(context);
  }

  /**
   * Returns true if the debugging is enabled.
   * 
   * @return true <=> Debugging is enabled.
   */
  public boolean isDebuggingEnabled() {
    return true;
  }

  public boolean isTraceingEnabled() {
    return true;
  }

  /**
   * Dumps debugging information.
   * 
   * @param msg
   *          An error message.
   */
  public void debug(final String msg) {
    log(msg, Project.MSG_VERBOSE);
  }

  /**
   * Dumps debugging information.
   * 
   * @param msg
   *          An error message.
   * @param obj
   *          A single argument.
   */
  public void debug(final String msg, final Object arg) {
    debug(MessageCreator.createMessage(msg, arg));
  }

  /**
   * Dumps debugging information.
   * 
   * @param msg
   *          A formatting message.
   * @param args
   *          The arguments used for the formatted message.
   */
  public void debug(final String msg, final Object[] args) {
    debug(MessageCreator.createMessage(msg, args));
  }

  /**
   * Dumps traceing information.
   * 
   * @param msg
   *          A formatting message.
   * @param args
   *          The arguments used for the formatted message.
   */
  public void trace(final String msg, final Object[] args) {
    trace(MessageCreator.createMessage(msg, args));
  }

  /**
   * Dumps traceing information.
   * 
   * @param msg
   *          An error message.
   * @param obj
   *          A single argument.
   */
  public void trace(final String msg, final Object obj) {
    trace(MessageCreator.createMessage(msg, obj));
  }

  /**
   * Dumps traceing information.
   * 
   * @param msg
   *          A trace message.
   */
  public void trace(final String msg) {
    log(msg, Project.MSG_DEBUG);
  }

  /**
   * Dumps informational text.
   * 
   * @param msg
   *          A formatting message.
   * @param args
   *          The arguments used for the formatted message.
   */
  public void info(final String msg, final Object[] args) {
    info(MessageCreator.createMessage(msg, args));
  }

  /**
   * Dumps informational text.
   * 
   * @param msg
   *          An error message.
   * @param obj
   *          A single argument.
   */
  public void info(final String msg, final Object obj) {
    info(MessageCreator.createMessage(msg, obj));
  }

  /**
   * Dumps informational text.
   * 
   * @param msg
   *          An error message.
   */
  public void info(final String msg) {
    log(msg, Project.MSG_INFO);
  }

  /**
   * Dumps warning information.
   * 
   * @param msg
   *          A formatting message.
   * @param args
   *          The arguments used for the formatted message.
   */
  public void warn(final String msg, final Object[] args) {
    warn(MessageCreator.createMessage(msg, args));
  }

  /**
   * Dumps warning information.
   * 
   * @param msg
   *          An error message.
   * @param obj
   *          A single argument.
   */
  public void warn(final String msg, final Object obj) {
    warn(MessageCreator.createMessage(msg, obj));
  }

  /**
   * Dumps warning information.
   * 
   * @param msg
   *          An error message.
   */
  public void warn(final String msg) {
    log(msg, Project.MSG_WARN);
  }

  /**
   * Dumps error information.
   * 
   * @param msg
   *          A formatting message.
   * @param args
   *          The arguments used for the formatted message.
   */
  public void error(final String msg, final Object[] args) {
    error(MessageCreator.createMessage(msg, args));
  }

  /**
   * Dumps error information.
   * 
   * @param msg
   *          An error message.
   * @param obj
   *          A single argument.
   */
  public void error(final String msg, final Object obj) {
    error(MessageCreator.createMessage(msg, obj));
  }

  /**
   * Dumps error information.
   * 
   * @param msg
   *          An error message.
   */
  public void error(final String msg) {
    log(msg, Project.MSG_ERR);
  }

  /**
   * @param msg
   * @param msgLevel
   */
  private void log(final String msg, final int msgLevel) {
    if (this._project != null) {
      final Object ctx = this._context.get();
      if (ctx instanceof Task) {
        this._project.log((Task) ctx, msg, msgLevel);
      } else if (ctx instanceof Target) {
        this._project.log((Target) ctx, msg, msgLevel);
      } else {
        this._project.log(msg, msgLevel);
      }
    }
  }
} /* ENDCLASS */
