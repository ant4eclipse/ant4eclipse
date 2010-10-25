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
package org.ant4eclipse.lib.pde.internal.tools;

import org.ant4eclipse.lib.core.util.Utilities;

import java.io.File;

/**
 * <p>
 * Helper class to fetch the expansion directory. The expansion directory is used to extract files from bundles or
 * features during the runtime of ant4eclipse.
 * </p>
 * 
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 */
public class ExpansionDirectory {

  /** the default expansion directory **/
  public static final String DEFAULT_EXPANSION_DIRECTORY       = System.getProperty("java.io.tmpdir")
                                                                   + File.separatorChar + "a4e_expand_dir";

  /** the name of the expansion directory property **/
  public static final String EXPANSION_DIRECTORY_PROPERTY_NAME = "a4e.expansion.directory";

  /** the expansion directory */
  private static File        expansionDir                      = null;

  /**
   * <p>
   * Returns the expansion directory.
   * </p>
   * 
   * @return the expansion directory.
   */
  public static synchronized File getExpansionDir() {

    // if the expansion directory is not set, create it...
    if (expansionDir == null) {

      // get the directory name
      String expansionDirectory = System.getProperty(ExpansionDirectory.EXPANSION_DIRECTORY_PROPERTY_NAME,
          ExpansionDirectory.DEFAULT_EXPANSION_DIRECTORY);

      // create the directory
      expansionDir = new File(expansionDirectory);

      // delete expansion directory if is exists. This is necessary to prevent invalid bundle content if
      // the same version number is used in several builds
      if (expansionDir.exists()) {
        Utilities.delete(expansionDir);
      }

      // set delete on exit
      expansionDir.deleteOnExit();

      // create if not exists
      if (!expansionDir.exists()) {
        expansionDir.mkdirs();
      }
    }

    // return the expansion directory
    return expansionDir;
  }
}
