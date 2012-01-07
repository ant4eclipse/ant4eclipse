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
package org.ant4eclipse.lib.platform.model.launcher;

import org.ant4eclipse.lib.core.A4EService;

import java.io.File;

/**
 * Reads and parses a Eclipse <tt>.launch</tt> configuration file
 * 
 * @author Nils Hartmann (nils@nilshartmann.net)
 * @author Daniel Kasmeroglu (Daniel.Kasmeroglu@kasisoft.net)
 */
public interface LaunchConfigurationReader extends A4EService {

  /**
   * Read the specified launchConfigurationFile.
   * 
   * @param launchConfigurationFile
   *          The launch configuration file to parse
   * @return The {@link LaunchConfiguration}-instance
   */
  LaunchConfiguration readLaunchConfiguration( File launchConfigurationFile );

} /* ENDINTERFACE */
