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
package org.ant4eclipse.platform.model.launcher;

import org.ant4eclipse.core.service.ServiceRegistry;

import java.io.File;

/**
 * 
 * Reads and parses a Eclipse <tt>.launch</tt> configuration file
 * 
 * @author Nils Hartmann (nils@nilshartmann.net)
 */
public interface LaunchConfigurationReader {

  /**
   * Read the specified launchConfigurationFile.
   * 
   * @param launchConfigurationFile
   *          The launch configuration file to parse
   * @return The {@link LaunchConfiguration}-instance
   */
  LaunchConfiguration readLaunchConfiguration(File launchConfigurationFile);

  /**
   */
  static class Helper {

    /**
     * <p>
     * Fetches the {@link LaunchConfigurationReader} instance from the {@link ServiceRegistry}.
     * </p>
     * 
     * @return the registered {@link LaunchConfigurationReader}
     */
    public static LaunchConfigurationReader getReader() {
      return (LaunchConfigurationReader) ServiceRegistry.instance().getService(
          LaunchConfigurationReader.class.getName());
    }
  }

}
