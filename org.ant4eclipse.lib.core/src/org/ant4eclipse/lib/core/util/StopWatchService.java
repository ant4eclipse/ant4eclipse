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
package org.ant4eclipse.lib.core.util;

/**
 * A service managing stop watches.
 * 
 * @author Nils Hartmann
 * 
 */
public interface StopWatchService {

  /**
   * Returns a StopWatch with the specified name.
   * 
   * <p>
   * The StopWatch returned is <em>not</em> running.
   * 
   * <p>
   * A StopWatch can be started and stopped multiple times
   * 
   * @param name
   * @return
   */
  public StopWatch getOrCreateStopWatch(String name);

  /**
   * Resets all managed stopwatches
   */
  public void resetAll();

  /**
   * Dumps all stopwatches via ant4eclipse logging
   */
  public void dumpAll();

}
