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
package org.ant4eclipse.platform.model.resource.workspaceregistry;

import java.io.File;
import java.io.FileFilter;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import org.ant4eclipse.core.Assert;
import org.ant4eclipse.core.logging.A4ELogging;
import org.ant4eclipse.platform.internal.model.resource.workspaceregistry.LocationFileParser;

/**
 * <p>
 * Implements a {@link WorkspaceDefinition} for the 'standard' eclipse workspace.
 * </p>
 * 
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 */
public class DefaultEclipseWorkspaceDefinition implements WorkspaceDefinition {

  /** path to the projects directory in the meta data */
  private static final String METADATA_PROJECTS = ".metadata/.plugins/org.eclipse.core.resources/.projects";

  /** the workspace directory */
  private final File          _workspaceDirectory;

  /** the meta data location directory */
  private final File          _metadataLocationDirectory;

  /**
   * <p>
   * Creates a new instance of type {@link DefaultEclipseWorkspaceDefinition}.
   * </p>
   * 
   * @param workspaceDirectory
   *          the workspace directory
   */
  public DefaultEclipseWorkspaceDefinition(final File workspaceDirectory) {
    Assert.isDirectory(workspaceDirectory);

    this._workspaceDirectory = workspaceDirectory;
    this._metadataLocationDirectory = new File(workspaceDirectory, METADATA_PROJECTS);
  }

  /**
   * {@inheritDoc}
   */
  public File[] getProjectFolders() {

    // define the result
    final List<File> result = new LinkedList<File>();

    // read all directories in the workspace directory
    File[] directories = this._workspaceDirectory.listFiles(new FileFilter() {
      public boolean accept(final File file) {
        final boolean accepted = file.isDirectory() && !file.getName().equals(".metadata") && isProjectDirectory(file);
        final String message = String
            .format(
                "DefaultEclipseWorkspaceDefinition.getProjectFolders(): directory '%s' - accept as project directory: '%s'",
                new Object[] { file.getAbsolutePath(), Boolean.valueOf(accepted) });
        A4ELogging.debug(message);
        return accepted;
      }
    });

    // add all project directories to the result
    result.addAll(Arrays.asList(directories));

    // check the METADATA_PROJECTS directory
    if (isDirectory(this._metadataLocationDirectory)) {

      // read all directories in the METADATA_PROJECTS directory
      directories = this._metadataLocationDirectory.listFiles(new FileFilter() {
        public boolean accept(final File file) {
          final boolean accepted = file.isDirectory() && isLocationDirectory(file);
          final String message = String
              .format(
                  "DefaultEclipseWorkspaceDefinition.getProjectFolders(): directory '%s' - accept as project directory: '%s'",
                  new Object[] { file.getAbsolutePath(), Boolean.valueOf(accepted) });
          A4ELogging.debug(message);
          return accepted;
        }
      });

      // add the resolved linked directories to the result
      for (File directorie : directories) {
        final File linkedProject = LocationFileParser.getProjectDirectory(new File(directorie, ".location"));
        if ((linkedProject != null) && !result.contains(linkedProject)) {
          result.add(linkedProject);
        }
      }
    }

    // return the result
    return result.toArray(new File[0]);
  }

  /**
   * <p>
   * Returns <code>true</code>, if the specified directory is an eclipse project directory.
   * </p>
   * 
   * @param directory
   *          the directory
   * @return <code>true</code>, if the specified directory is an eclipse project directory.
   */
  private boolean isProjectDirectory(final File directory) {
    return isDirectory(directory) && new File(directory, ".project").exists();
  }

  /**
   * <p>
   * </p>
   * 
   * @param directory
   * @return
   */
  private boolean isLocationDirectory(final File directory) {
    return isDirectory(directory) && new File(directory, ".location").exists();
  }

  /**
   * @param directory
   * @return
   */
  private boolean isDirectory(final File directory) {
    return (directory != null) && directory.exists();
  }
} /* ENDCLASS */
