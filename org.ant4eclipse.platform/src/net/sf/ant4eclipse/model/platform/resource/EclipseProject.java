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
package net.sf.ant4eclipse.model.platform.resource;

import java.io.File;

import net.sf.ant4eclipse.model.platform.resource.role.ProjectRole;

/**
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 */
public interface EclipseProject {

  public int ABSOLUTE                                      = 0;

  public int PROJECT_RELATIVE_WITH_LEADING_PROJECT_NAME    = 1;

  public int PROJECT_RELATIVE_WITHOUT_LEADING_PROJECT_NAME = 2;

  /**
   * <p>
   * Returns the workspace this {@link EclipseProject} belongs to.
   * </p>
   * 
   * @return the workspace this {@link EclipseProject} belongs to.
   */
  public Workspace getWorkspace();

  /**
   * <p>
   * Returns the name of the project as specified in the <code>.project</code> file. If the name is not explicitly set,
   * the result of the <code>getFolderName()</code> method will be returned.
   * </p>
   * 
   * @return the name specified name of the project.
   */
  public String getSpecifiedName();

  /**
   * <p>
   * Returns the name of the project folder.
   * </p>
   * 
   * @return the name of the project folder.
   */
  public String getFolderName();

  /**
   * <p>
   * Returns the folder that represents this project.
   * </p>
   * 
   * @return Returns the folder that represents this project.
   */
  public File getFolder();

  /**
   * <p>
   * Returns the comment that is maybe set in the <code>.project</code> file.
   * </p>
   * 
   * @return the comment that is maybe set in the <code>.project</code> file.
   */
  public String getComment();

  /**
   * Returns whether this project exists. A project exists if the underlying file exists and if it is an directory.
   * 
   * @return Returns whether this project exists.
   */
  public boolean exists();

  /**
   * Returns whether this project has a child with the given path.
   * 
   * @param path
   *          the path of child
   * @return Returns whether this project has a child with the given path.
   */
  public boolean hasChild(final String path);

  /**
   * Returns the child of this project with the given path.
   * 
   * @param path
   *          The child which shall be returned.
   * 
   * @return Returns the child of this project with the given path.
   */
  public File getChild(final String path);

  public File[] getChildren(final String[] path);

  /**
   * Returns the child of this project with the given path.
   * 
   * @param path
   *          The child which shall be returned.
   * @param relative
   *          true <=> Create a relative location.
   * 
   * @return Returns the child of this project with the given path.
   */
  public File getChild(final String path, final int relative);

  public File[] getChildren(final String[] path, final int relative);

  /**
   * Returns whether this project has a file with the given name in the project's <tt>.settings</tt> directory.
   * 
   * @param settingsFileName
   *          the name of the file inside the <tt>.settings</tt> file
   * @return true if there is such a file otherwise false
   */
  public boolean hasSettingsFile(final String settingsFileName);

  /**
   * Returns the file with the given name from the project's <tt>.settings</tt> directory.
   * 
   * @param settingsFileName
   *          the name of the file inside the <tt>.settings</tt> file
   * @return the file - never null
   * 
   * @throws RuntimeException
   */
  public File getSettingsFile(final String settingsFileName) throws RuntimeException;

  /**
   * Returns whether the nature with the specified name is set or not.
   * 
   * @param natureName
   * @return Returns whether the nature with the specified name is set or not.
   */
  public boolean hasNature(final String natureName);

  /**
   * Returns whether the specified nature is set or not.
   * 
   * @param nature
   * @return Returns whether the specified nature is set or not.
   */
  public boolean hasNature(final ProjectNature nature);

  /**
   * Returns the project natures of the project.
   * 
   * @return Returns the project natures of the project.
   */
  public ProjectNature[] getNatures();

  /**
   * Returns whether the role of the given type is set or not.
   * 
   * @param projectRoleClass
   * @return Returns whether the role of the given type is set or not.
   * 
   */
  public boolean hasRole(final Class projectRoleClass);

  /**
   * Returns the role of the given type. If the role is not set, an exception will be thrown.
   * 
   * @param projectRoleClass
   * @return Returns the role of the given type. If the role is not set, an exception will be thrown.
   */
  public ProjectRole getRole(final Class projectRoleClass);

  /**
   * Returns the roles of this project.
   * 
   * @return Returns the roles of this project.
   */
  public ProjectRole[] getRoles();

  /**
   * Returns whether the build command with the specified name is set or not.
   * 
   * @param commandName
   * @return Returns whether the build command with the specified name is set or not.
   */
  public boolean hasBuildCommand(final String commandName);

  /**
   * Returns whether the specified build command is set or not.
   * 
   * @param command
   *          The command name that has to be tested.
   * 
   * @return Returns whether the specified build command is set or not.
   */
  public boolean hasBuildCommand(final BuildCommand command);

  /**
   * Returns the build commands of this project.
   * 
   * @return Returns the build commands of this project.
   */
  public BuildCommand[] getBuildCommands();

  /**
   * Returns a list of all referenced project names.
   * 
   * @return A list of all reference project names.
   */
  public String[] getReferencedProjects();
} /* ENDCLASS */