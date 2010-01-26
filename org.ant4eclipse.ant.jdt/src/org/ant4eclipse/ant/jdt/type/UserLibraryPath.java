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
package org.ant4eclipse.ant.jdt.type;



import org.ant4eclipse.lib.core.ant.AbstractAnt4EclipseDataType;
import org.ant4eclipse.lib.core.logging.A4ELogging;
import org.ant4eclipse.lib.jdt.model.userlibrary.Archive;
import org.ant4eclipse.lib.jdt.model.userlibrary.UserLibraries;
import org.ant4eclipse.lib.jdt.model.userlibrary.UserLibrariesFileParser;
import org.ant4eclipse.lib.jdt.model.userlibrary.UserLibrary;
import org.ant4eclipse.lib.jdt.tools.classpathelements.ClassPathElementsRegistry;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.types.Path;

import java.io.File;

/**
 * Simple path extension that allows to be configured using an eclipse configuration file.
 * 
 * @author Daniel Kasmeroglu (daniel.kasmeroglu@kasisoft.net)
 */
public class UserLibraryPath extends AbstractAnt4EclipseDataType {

  private static final String PREFIX = "org.eclipse.jdt.USER_LIBRARY/";

  private File                _userlibfile;

  /**
   * Simply initializes this new type.
   * 
   * @param project
   *          The project this type applies to.
   */
  public UserLibraryPath(Project project) {
    super(project);
    this._userlibfile = null;
  }

  /**
   * Changes the user library configuration file.
   * 
   * @param userlib
   *          The new user library configuration file.
   */
  public void setUserlibraries(File userlib) {
    if (!userlib.isFile()) {
      A4ELogging.warn("missing file '%s'", userlib.getPath());
    } else {
      this._userlibfile = userlib;
      loadConfigurationFile();
    }
  }

  /**
   * Tries to load the configuration file if all necessary information is available.
   * 
   * @todo [04-Aug-2005:KASI] Would be nicer if only one configuration file is used to create all library entries.
   */
  private void loadConfigurationFile() {
    try {
      UserLibrariesFileParser parser = UserLibrariesFileParser.Helper.getUserLibrariesFileParser();

      UserLibraries userlibs = parser.parseUserLibrariesFile(this._userlibfile);
      String[] libs = userlibs.getAvailableLibraries();
      for (String lib : libs) {
        UserLibrary library = userlibs.getLibrary(lib);
        Archive[] archives = library.getArchives();
        Path path = new Path(getProject());
        for (Archive archive : archives) {
          path.createPathElement().setLocation(archive.getPath());
        }

        // add it as an ant path
        getProject().addReference(PREFIX + lib, path);

        // add it to the ClassPathElementsRegistry
        ClassPathElementsRegistry.Helper.getRegistry().registerClassPathContainer(PREFIX + library.getName(),
            library.getArchiveFiles());
      }
    } catch (Exception ex) {
      A4ELogging.error("Failed to load userlibraries file.\n'%s'.", ex);
    }
  }

} /* ENDCLASS */
