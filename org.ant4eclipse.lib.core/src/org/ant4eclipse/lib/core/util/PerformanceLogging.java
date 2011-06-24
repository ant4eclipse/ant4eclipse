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

public class PerformanceLogging {

  public static void start(Class<?> source, String name) {
    // TODO enable logging via property
    getStopWatchService().getOrCreateStopWatch(source.getName() + "::" + name).start();
  }

  public static void stop(Class<?> source, String name) {
    getStopWatchService().getOrCreateStopWatch(source.getName() + "::" + name).stop();
  }

  private static StopWatchService getStopWatchService() {
    return ServiceRegistryAccess.instance().getService(StopWatchService.class);
  }

}
