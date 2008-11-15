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
package net.sf.ant4eclipse.core.logging;

import net.sf.ant4eclipse.core.service.ServiceRegistry;

/**
 * <p>
 * API which provides a project wide logging mechanism.
 * </p>
 * 
 * @author Daniel Kasmeroglu (daniel.kasmeroglu@kasisoft.net)
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 */
public class A4ELogging {

  /**
   * Returns true if the debugging is enabled.
   * 
   * @return true <=> Debugging is enabled.
   */
  public static final boolean isDebuggingEnabled() {
    return getLogger().isDebuggingEnabled();
  }

  public static final boolean isTraceingEnabled() {
    return getLogger().isTraceingEnabled();
  }

  /**
   * Dumps traceing information.
   * 
   * @param msg
   *          A formatting message.
   * @param args
   *          The arguments used for the formatted message.
   */
  public static final void trace(final String msg, final Object[] args) {
    getLogger().trace(msg, args);
  }

  /**
   * Dumps traceing information.
   * 
   * @param msg
   *          An error message.
   * @param obj
   *          A single argument.
   */
  public static final void trace(final String msg, final Object obj) {
    getLogger().trace(msg, obj);
  }

  /**
   * Dumps debugging information.
   * 
   * @param msg
   *          A formatting message.
   * @param args
   *          The arguments used for the formatted message.
   */
  public static final void debug(final String msg, final Object[] args) {
    getLogger().debug(msg, args);
  }

  /**
   * Dumps debugging information.
   * 
   * @param msg
   *          An error message.
   * @param obj
   *          A single argument.
   */
  public static final void debug(final String msg, final Object obj) {
    getLogger().debug(msg, obj);
  }

  /**
   * Dumps traceing information.
   * 
   * @param msg
   *          A trace message.
   */
  public static final void trace(final String msg) {
    getLogger().trace(msg);
  }

  /**
   * Dumps debugging information.
   * 
   * @param msg
   *          An error message.
   */
  public static final void debug(final String msg) {
    getLogger().debug(msg);
  }

  /**
   * Dumps informational text.
   * 
   * @param msg
   *          A formatting message.
   * @param args
   *          The arguments used for the formatted message.
   */
  public static final void info(final String msg, final Object[] args) {
    getLogger().info(msg, args);
  }

  /**
   * Dumps informational text.
   * 
   * @param msg
   *          An error message.
   * @param obj
   *          A single argument.
   */
  public static final void info(final String msg, final Object obj) {
    getLogger().info(msg, obj);
  }

  /**
   * Dumps informational text.
   * 
   * @param msg
   *          An error message.
   */
  public static final void info(final String msg) {
    getLogger().info(msg);
  }

  /**
   * Dumps warning information.
   * 
   * @param msg
   *          A formatting message.
   * @param args
   *          The arguments used for the formatted message.
   */
  public static final void warn(final String msg, final Object[] args) {
    getLogger().warn(msg, args);
  }

  /**
   * Dumps warning information.
   * 
   * @param msg
   *          An error message.
   * @param obj
   *          A single argument.
   */
  public static final void warn(final String msg, final Object obj) {
    getLogger().warn(msg, obj);
  }

  /**
   * Dumps warning information.
   * 
   * @param msg
   *          An error message.
   */
  public static final void warn(final String msg) {
    getLogger().warn(msg);
  }

  /**
   * Dumps error information.
   * 
   * @param msg
   *          A formatting message.
   * @param args
   *          The arguments used for the formatted message.
   */
  public static final void error(final String msg, final Object[] args) {
    getLogger().error(msg, args);
  }

  /**
   * Dumps error information.
   * 
   * @param msg
   *          An error message.
   * @param obj
   *          A single argument.
   */
  public static final void error(final String msg, final Object obj) {
    getLogger().error(msg, obj);
  }

  /**
   * Dumps error information.
   * 
   * @param msg
   *          An error message.
   */
  public static final void error(final String msg) {
    getLogger().error(msg);
  }

  private static Ant4EclipseLogger getLogger() {
    return (Ant4EclipseLogger) ServiceRegistry.instance().getService(Ant4EclipseLogger.class.getName());
  }
} /* ENDCLASS */
