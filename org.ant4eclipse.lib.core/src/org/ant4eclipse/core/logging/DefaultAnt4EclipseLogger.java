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
package org.ant4eclipse.core.logging;

import java.io.PrintStream;

/**
 * <p>
 * Default implementation of an {@link Ant4EclipseLogger} which makes use of a specific {@link PrintStream} or
 * {@link System#out} by default.
 * </p>
 */
public class DefaultAnt4EclipseLogger implements Ant4EclipseLogger {

  /** -- */
  public static final int LOG_LEVEL_ERROR   = 0;

  /** -- */
  public static final int LOG_LEVEL_WARN    = 1;

  /** -- */
  public static final int LOG_LEVEL_INFO    = 2;

  /** -- */
  public static final int LOG_LEVEL_DEBUG  /* ANT category: verbose */= 3;

  /** -- */
  public static final int LOG_LEVEL_TRACE  /* ANT category: debug */= 4;

  private static String[] LEVEL_DESCIRPTION = new String[] { "ERROR", "WARN", "INFO", "DEBUG", "TRACE" };

  private int             _logLevel;

  private PrintStream     _printer;

  /**
   * Sets up this logger implementation to make use of standard output.
   */
  public DefaultAnt4EclipseLogger() {
    this(null);
  }

  /**
   * Sets up this logger implementation to make use of a specified printer.
   * 
   * @param printer
   *          The printer that will be used for the output. If <code>null</code> the standard output is used.
   */
  public DefaultAnt4EclipseLogger(PrintStream printer) {
    this._logLevel = LOG_LEVEL_TRACE;
    this._printer = printer;
    if (this._printer == null) {
      this._printer = System.out;
    }
  }

  /**
   * {@inheritDoc}
   */
  public boolean isDebuggingEnabled() {
    return this._logLevel >= LOG_LEVEL_DEBUG;
  }

  /**
   * {@inheritDoc}
   */
  public boolean isTraceingEnabled() {
    return this._logLevel >= LOG_LEVEL_TRACE;
  }

  /**
   * Changes the current loglevel for this logger.
   * 
   * @param loglevel
   *          The new log level to be used for this logger.
   */
  public void setLogLevel(int loglevel) {
    this._logLevel = loglevel;
  }

  /**
   * {@inheritDoc}
   */
  public void setContext(Object context) {
    //
  }

  /**
   * {@inheritDoc}
   */
  public void debug(String msg, Object... args) {
    if (isDebuggingEnabled()) {
      log(LOG_LEVEL_DEBUG, msg, args);
    }
  }

  /**
   * {@inheritDoc}
   */
  public void error(String msg, Object... args) {
    log(LOG_LEVEL_ERROR, msg, args);
  }

  /**
   * {@inheritDoc}
   */
  public void info(String msg, Object... args) {
    log(LOG_LEVEL_INFO, msg, args);
  }

  /**
   * {@inheritDoc}
   */
  public void trace(String msg, Object... args) {
    if (isTraceingEnabled()) {
      log(LOG_LEVEL_TRACE, msg, args);
    }
  }

  /**
   * {@inheritDoc}
   */
  public void warn(String msg, Object... args) {
    log(LOG_LEVEL_WARN, msg, args);
  }

  /**
   * Dumps a log into the console.
   * 
   * @param level
   *          The level as declared by one of the level constants above.
   * @param msg
   *          The formatting message.
   * @param args
   *          The arguments to be used for the formatting message.
   */
  private void log(int level, String msg, Object... args) {
    this._printer.println("[" + LEVEL_DESCIRPTION[level] + "] " + String.format(msg, args));
  }

} /* ENDCLASS */
