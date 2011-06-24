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
package org.ant4eclipse.ant.core;

import org.ant4eclipse.lib.core.service.ServiceRegistryAccess;
import org.ant4eclipse.lib.core.util.StopWatch;
import org.ant4eclipse.lib.core.util.StopWatchService;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Project;

/**
 * @author Nils Hartmann
 * 
 */
public class StopWatchTask extends AbstractAnt4EclipseTask {

  private String _command;

  private String _name;

  @Override
  public void doExecute() throws BuildException {

    if (isCommand("start")) {
      getStopWatchService().getOrCreateStopWatch(getName()).start();
    } else if (isCommand("stop")) {
      StopWatch stopWatch = getStopWatchService().getOrCreateStopWatch(getName());
      long elapsedTime = stopWatch.stop();
      log(String.format("Stopped watch '%s' after '%d' ms", stopWatch.getName(), elapsedTime), Project.MSG_INFO);
    } else if (isCommand("reset-all")) {
      getStopWatchService().resetAll();
    } else if (isCommand("dump-all")) {
      getStopWatchService().dumpAll();
    } else {
      throw new BuildException("Unkown command: '" + this._command + "'");
    }
  }

  public String getCommand() {
    return this._command;
  }

  /**
   * Set the StopWatchCommand
   * 
   * @param command
   */
  public void setCommand(String command) {
    this._command = command;
  }

  public String getName() {
    return this._name;
  }

  /**
   * Set the name of the stop watch
   * 
   * @param name
   */
  public void setName(String name) {
    this._name = name;
  }

  private StopWatchService getStopWatchService() {
    return ServiceRegistryAccess.instance().getService(StopWatchService.class);
  }

  private boolean isCommand(String expected) {
    return (this._command != null && expected.equalsIgnoreCase(this._command.trim()));
  }

}
