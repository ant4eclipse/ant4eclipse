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

import org.ant4eclipse.lib.core.service.ServiceRegistryAccess;

/**
 * Used for ant4eclipse internal performance logging
 * 
 * @author
 * 
 */
public class PerformanceLogging {

  private static final boolean ENABLE_PERFORMANCE_LOGGING = Boolean.getBoolean("ant4eclipse.enablePerformanceLogging");

  /**
   * Starts the performance logging. The given class and name are used as identifier for the StopWatch that should be
   * used
   * 
   * @param source
   * @param name
   */
  public static void start(Class<?> source, String name) {
    if (ENABLE_PERFORMANCE_LOGGING) {
      getStopWatchService().getOrCreateStopWatch(source.getName() + "::" + name).start();
    }
  }

  /**
   * Stops the performance logging. The given class and name are used as identifier for the StopWatch that should be
   * used
   * 
   * @param source
   * @param name
   */
  public static long stop(Class<?> source, String name) {
    if (ENABLE_PERFORMANCE_LOGGING) {
      return getStopWatchService().getOrCreateStopWatch(source.getName() + "::" + name).stop();
    }

    return -1;
  }

  /**
   * @return the registered instance of the {@link StopWatchService}
   */
  private static StopWatchService getStopWatchService() {
    return ServiceRegistryAccess.instance().getService(StopWatchService.class);
  }

}
