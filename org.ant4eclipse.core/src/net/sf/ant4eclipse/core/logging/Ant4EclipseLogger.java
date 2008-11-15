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

/**
 * <p>
 * API which provides a project wide logging mechanism.
 * 
 * ERR -> INFO -> DEBUG -> TRACE, entspricht in ant (ERR -> INFO -> VERBOSE -> DEBUG)
 * </p>
 * 
 * @author Daniel Kasmeroglu (daniel.kasmeroglu@kasisoft.net)
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 */
public interface Ant4EclipseLogger {

  /**
   * @param context
   */
  public void setContext(Object context);

  /**
   * Returns true if the debugging is enabled.
   * 
   * @return true <=> Debugging is enabled.
   */
  public boolean isDebuggingEnabled();

  public boolean isTraceingEnabled();

  /**
   * Dumps traceing information.
   * 
   * @param msg
   *          A trace message.
   */
  public void trace(String msg);

  /**
   * Dumps traceing information.
   * 
   * @param msg
   *          A formatting message.
   * @param args
   *          The arguments used for the formatted message.
   */
  public void trace(String msg, Object[] args);

  /**
   * Dumps traceing information.
   * 
   * @param msg
   *          An error message.
   * @param obj
   *          A single argument.
   */
  public void trace(String msg, Object obj);

  /**
   * Dumps debugging information.
   * 
   * @param msg
   *          An error message.
   */
  public void debug(String msg);

  /**
   * Dumps debugging information.
   * 
   * @param msg
   *          An error message.
   * @param obj
   *          A single argument.
   */
  public void debug(String msg, Object obj);

  /**
   * Dumps debugging information.
   * 
   * @param msg
   *          A formatting message.
   * @param args
   *          The arguments used for the formatted message.
   */
  public void debug(String msg, Object[] args);

  /**
   * Dumps informational text.
   * 
   * @param msg
   *          An error message.
   */
  public void info(String msg);

  /**
   * Dumps informational text.
   * 
   * @param msg
   *          An error message.
   * @param obj
   *          A single argument.
   */
  public void info(String msg, Object obj);

  /**
   * Dumps informational text.
   * 
   * @param msg
   *          A formatting message.
   * @param args
   *          The arguments used for the formatted message.
   */
  public void info(String msg, Object[] args);

  /**
   * Dumps warning information.
   * 
   * @param msg
   *          An error message.
   */
  public void warn(String msg);

  /**
   * Dumps warning information.
   * 
   * @param msg
   *          An error message.
   * @param obj
   *          A single argument.
   */
  public void warn(String msg, Object obj);

  /**
   * Dumps warning information.
   * 
   * @param msg
   *          A formatting message.
   * @param args
   *          The arguments used for the formatted message.
   */
  public void warn(String msg, Object[] args);

  /**
   * Dumps error information.
   * 
   * @param msg
   *          A formatting message.
   * @param args
   *          The arguments used for the formatted message.
   */
  public void error(String msg, Object[] args);

  /**
   * Dumps error information.
   * 
   * @param msg
   *          An error message.
   * @param obj
   *          A single argument.
   */
  public void error(String msg, Object obj);

  /**
   * Dumps error information.
   * 
   * @param msg
   *          An error message.
   */
  public void error(String msg);
} /* ENDCLASS */
