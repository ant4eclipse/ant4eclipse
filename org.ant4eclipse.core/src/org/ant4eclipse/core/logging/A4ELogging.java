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
package org.ant4eclipse.core.logging;

import org.ant4eclipse.core.service.ServiceRegistry;

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
   * Returns <code>true</code> if the debugging is enabled.
   * 
   * @return <code>true</code> <=> Debugging is enabled.
   */
  public static final boolean isDebuggingEnabled() {
    return getLogger().isDebuggingEnabled();
  }

  /**
   * Returns <code>true</code> if tracing is enabled.
   * 
   * @return <code>true</code> <=> Tracing is enabled.
   */
  public static final boolean isTraceingEnabled() {
    return getLogger().isTraceingEnabled();
  }

  /**
   * Dumps traceing information.
   * 
   * @param msg
   *          A formatting message. Neither <code>null</code> nor empty.
   * @param args
   *          The arguments used for the formatted message.
   */
  public static final void trace(String msg, Object... args) {
    getLogger().trace(msg, args);
  }

  /**
   * Dumps debugging information.
   * 
   * @param msg
   *          A formatting message. Neither <code>null</code> nor empty.
   * @param args
   *          The arguments used for the formatted message.
   */
  public static final void debug(String msg, Object... args) {
    getLogger().debug(msg, args);
  }

  /**
   * Dumps informational text.
   * 
   * @param msg
   *          A formatting message. Neither <code>null</code> nor empty.
   * @param args
   *          The arguments used for the formatted message.
   */
  public static final void info(String msg, Object... args) {
    getLogger().info(msg, args);
  }

  /**
   * Dumps warning information.
   * 
   * @param msg
   *          A formatting message. Neither <code>null</code> nor empty.
   * @param args
   *          The arguments used for the formatted message.
   */
  public static final void warn(String msg, Object... args) {
    getLogger().warn(msg, args);
  }

  /**
   * Dumps error information.
   * 
   * @param msg
   *          A formatting message. Neither <code>null</code> nor empty.
   * @param args
   *          The arguments used for the formatted message.
   */
  public static final void error(String msg, Object... args) {
    getLogger().error(msg, args);
  }

  /**
   * Returns an instance of a logger currently provided by the registry.
   * 
   * @return An instance of a logger currently provided by the registry. Not <code>null</code>.
   */
  private static final Ant4EclipseLogger getLogger() {
    return (Ant4EclipseLogger) ServiceRegistry.instance().getService(Ant4EclipseLogger.class.getName());
  }

} /* ENDCLASS */
