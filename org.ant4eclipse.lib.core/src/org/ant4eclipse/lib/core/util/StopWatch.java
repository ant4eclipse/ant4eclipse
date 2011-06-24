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
 * <p>
 * Implements a stop watch.
 * </p>
 * <p>
 * A stop watch can be started and stoped multiple times.
 * 
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 */
public class StopWatch {

  /** The name of the stop watch */
  private final String _name;

  private int          _invocations  = 0;

  private long         _elapsedTime;

  private long         _runningSince = -1;

  public StopWatch(String name) {
    super();
    this._name = name;
  }

  public long stop() {
    if (this._runningSince == -1) {
      return this._elapsedTime;
    }

    long elapsedTime = System.currentTimeMillis() - this._runningSince;
    this._elapsedTime += elapsedTime;
    this._runningSince = -1;
    return elapsedTime;
  }

  /**
   * (Re)starts the StopWatch
   */
  public void start() {
    this._invocations++;
    this._runningSince = System.currentTimeMillis();
  }

  /**
   * Returns the total time this stop watch has been run
   * 
   * @return
   */
  public long getElapsedTime() {
    return this._elapsedTime;
  }

  /**
   * returns the average time (in ms) this stop watch has been run
   * 
   * @return
   */
  public double getAverageTime() {
    if (this._invocations == 0) {
      return 0;
    }
    return this._elapsedTime / this._invocations;
  }

  /**
   * returns how often this stop watch has been used
   * 
   * @return
   */
  public int getInvocations() {
    return this._invocations;
    }

  /**
   * Returns the name of this stop watch
   * 
   * @return
   */
  public String getName() {
    return this._name;
  }

}
