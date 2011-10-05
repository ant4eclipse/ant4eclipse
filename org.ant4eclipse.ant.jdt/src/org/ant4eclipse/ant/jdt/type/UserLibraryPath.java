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

import java.io.File;

import org.ant4eclipse.ant.core.AbstractAnt4EclipseDataType;
import org.ant4eclipse.ant.core.HasReferencesDataType;
import org.ant4eclipse.ant.platform.core.delegate.WorkspaceDelegate;
import org.ant4eclipse.lib.core.logging.A4ELogging;
import org.ant4eclipse.lib.core.service.ServiceRegistryAccess;
import org.ant4eclipse.lib.jdt.model.userlibrary.Archive;
import org.ant4eclipse.lib.jdt.model.userlibrary.UserLibraries;
import org.ant4eclipse.lib.jdt.model.userlibrary.UserLibrariesFileParser;
import org.ant4eclipse.lib.jdt.model.userlibrary.UserLibrary;
import org.ant4eclipse.lib.jdt.tools.classpathelements.ClassPathElementsRegistry;
import org.ant4eclipse.lib.platform.model.resource.Workspace;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.types.Path;

/**
 * Simple path extension that allows to be configured using an eclipse configuration file.
 * 
 * @author Daniel Kasmeroglu (daniel.kasmeroglu@kasisoft.net)
 */
public class UserLibraryPath extends AbstractAnt4EclipseDataType implements HasReferencesDataType {

  private static final String PREFIX = "org.eclipse.jdt.USER_LIBRARY/";

  private File                _userlibfile;

  private WorkspaceDelegate   _workspaceDelegate;

  /**
   * Simply initializes this new type.
   * 
   * @param project
   *          The project this type applies to.
   */
  public UserLibraryPath(Project project) {
    super(project);
    this._workspaceDelegate = new WorkspaceDelegate(this);
    this._userlibfile = null;
  }

  /**
   * Set the identifier of the workspace
   */
  public void setWorkspaceId(String identifier) {
    this._workspaceDelegate.setWorkspaceId(identifier);
  }

  /**
   * Sets the directory of the workspace
   *
   * @param workspaceDirectory
   */
  public void setWorkspaceDirectory(String workspaceDirectory) {
    this._workspaceDelegate.setWorkspaceDirectory(workspaceDirectory);
  }

  /**
   * Changes the user library configuration file.
   * 
   * @param userlib
   *          The new user library configuration file.
   */
  public void setUserlibraries(File userlib) {
      this._userlibfile = userlib;
    // if (!userlib.isFile()) {
    // A4ELogging.warn("missing file '%s'", userlib.getPath());
    // } else {
    // this._userlibfile = userlib;
    // loadConfigurationFile();
    // }
  }

  @Override
  protected void doValidate() {
    if (this._userlibfile == null) {
      throw new BuildException("Property userlibraries must be set");
    }

    // if (!_workspaceDelegate.isWorkspaceDirectorySet() && _workspaceDelegate.isWorkspaceIdSet()) {
    // A4ELogging.warn("No workspace directory or id has been set. Will not be able to resolve ")
    // }

    if (!this._userlibfile.isFile()) {
      A4ELogging.warn("missing file '%s'", this._userlibfile.getPath());
      return;
    }

    loadConfigurationFile();
  }

  /**
   * Tries to load the configuration file if all necessary information is available.
   * 
   * @todo [04-Aug-2005:KASI] Would be nicer if only one configuration file is used to create all library entries.
   */
  private void loadConfigurationFile() {
    try {

      UserLibrariesFileParser parser = ServiceRegistryAccess.instance().getService(UserLibrariesFileParser.class);

      UserLibraries userlibs = parser.parseUserLibrariesFile(this._userlibfile, getWorkspace());
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
        ServiceRegistryAccess.instance().getService(ClassPathElementsRegistry.class)
            .registerClassPathContainer(PREFIX + library.getName(), library.getArchiveFiles());
      }
    } catch (Exception ex) {
      ex.printStackTrace();
      A4ELogging.error("Failed to load userlibraries file.\n'%s'.", ex);
    }
  }

  /**
   * @return the workspace specified or null if no workspace has been set
   */
  private Workspace getWorkspace() {
    if (this._workspaceDelegate.isWorkspaceDirectorySet() || this._workspaceDelegate.isWorkspaceIdSet()) {
      return this._workspaceDelegate.getWorkspace();
    }
    return null;
  }

} /* ENDCLASS */
