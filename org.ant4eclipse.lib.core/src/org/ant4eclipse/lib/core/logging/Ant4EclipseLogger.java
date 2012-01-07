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
package org.ant4eclipse.lib.core.logging;

import org.ant4eclipse.lib.core.A4EService;

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
public interface Ant4EclipseLogger extends A4EService {

  /**
   * Applies a contextual object which might provide additional information to some output.
   * 
   * @param context
   *          A contextual object providing additional information. Maybe <code>null</code>.
   */
  void setContext( Object context );

  /**
   * Returns <code>true</code> if the debugging is enabled.
   * 
   * @return <code>true</code> <=> Debugging is enabled.
   */
  boolean isDebuggingEnabled();

  /**
   * Returns <code>true</code> if tracing is enabled.
   * 
   * @return <code>true</code> <=> Tracing is enabled.
   */
  boolean isTraceingEnabled();

  /**
   * Dumps traceing information.
   * 
   * @param msg
   *          A formatting message. Neither <code>null</code> nor empty.
   * @param args
   *          The arguments used for the formatted message.
   */
  void trace( String msg, Object ... args );

  /**
   * Dumps debugging information.
   * 
   * @param msg
   *          A formatting message. Neither <code>null</code> nor empty.
   * @param args
   *          The arguments used for the formatted message.
   */
  void debug( String msg, Object ... args );

  /**
   * Dumps informational text.
   * 
   * @param msg
   *          A formatting message. Neither <code>null</code> nor empty.
   * @param args
   *          The arguments used for the formatted message.
   */
  void info( String msg, Object ... args );

  /**
   * Dumps warning information.
   * 
   * @param msg
   *          A formatting message. Neither <code>null</code> nor empty.
   * @param args
   *          The arguments used for the formatted message.
   */
  void warn( String msg, Object ... args );

  /**
   * Dumps error information.
   * 
   * @param msg
   *          A formatting message. Neither <code>null</code> nor empty.
   * @param args
   *          The arguments used for the formatted message.
   */
  void error( String msg, Object ... args );

} /* ENDINTERFACE */
