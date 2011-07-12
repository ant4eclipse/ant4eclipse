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

import java.util.Collections;
import java.util.Comparator;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.ant4eclipse.lib.core.logging.A4ELogging;

public class StopWatchServiceImpl implements StopWatchService {

  private final Map<String, StopWatch> _managedStopWatches;

  public StopWatchServiceImpl() {
    this._managedStopWatches = new Hashtable<String, StopWatch>();

    System.out.println("StopWatchService erzeugt!!!");

  }

  public StopWatch getOrCreateStopWatch(String name) {

    StopWatch stopWatch = this._managedStopWatches.get(name);
    if (stopWatch == null) {
      stopWatch = new StopWatch(name);
      this._managedStopWatches.put(name, stopWatch);
    }

    return stopWatch;
  }

  public void resetAll() {
    this._managedStopWatches.clear();
  }

  public void dumpAll() {

    dumpAll("total time:", new Comparator<StopWatch>() {
      public int compare(StopWatch o1, StopWatch o2) {
        return Long.valueOf(o2.getElapsedTime()).compareTo(o1.getElapsedTime());
      }
    });

  }

  private void dumpAll(String key, Comparator<StopWatch> comparator) {
    List<StopWatch> stopwatches = new LinkedList<StopWatch>(this._managedStopWatches.values());
    Collections.sort(stopwatches, comparator);
    A4ELogging.info(stopwatches.size() + " stopwatches ordered by " + key);
    for (StopWatch stopWatch : stopwatches) {
      stopWatch.stop();
      A4ELogging.info(String.format("  '%s' took total '%f' sec (invocations: %d, average: %f sec)",
          stopWatch.getName(), (double) stopWatch.getElapsedTime() / 1000, stopWatch.getInvocations(),
          stopWatch.getAverageTime() / 1000));
    }

  }

}
