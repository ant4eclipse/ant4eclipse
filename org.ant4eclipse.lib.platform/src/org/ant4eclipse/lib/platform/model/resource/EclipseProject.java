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
package org.ant4eclipse.lib.platform.model.resource;

import org.ant4eclipse.lib.platform.model.resource.role.ProjectRole;

import java.io.File;

/**
 * <p>
 * An {@link EclipseProject} represents an eclipse project in the workspace. An eclipse project <bold>must</bold>
 * contain a <code>.project</code> file that contains further project information.
 * </p>
 * 
 * 
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 */
public interface EclipseProject {

  enum PathStyle {

    /** the constant ABSOLUTE */
    ABSOLUTE,

    /** the constant PROJECT_RELATIVE_WITH_LEADING_PROJECT_NAME */
    PROJECT_RELATIVE_WITH_LEADING_PROJECT_NAME,

    /** the constant PROJECT_RELATIVE_WITHOUT_LEADING_PROJECT_NAME */
    PROJECT_RELATIVE_WITHOUT_LEADING_PROJECT_NAME;

  }

  /**
   * <p>
   * Returns the workspace this {@link EclipseProject} belongs to.
   * </p>
   * 
   * @return the workspace this {@link EclipseProject} belongs to.
   */
  Workspace getWorkspace();

  /**
   * <p>
   * Returns the name of the project as specified in the <code>.project</code> file. If the name is not explicitly set,
   * the result of the <code>getFolderName()</code> method will be returned.
   * </p>
   * 
   * @return the name specified name of the project.
   */
  String getSpecifiedName();

  /**
   * <p>
   * Returns the name of the project folder.
   * </p>
   * 
   * @return the name of the project folder.
   */
  String getFolderName();

  /**
   * <p>
   * Returns the folder that represents this project.
   * </p>
   * 
   * @return Returns the folder that represents this project.
   */
  File getFolder();

  /**
   * Returns the folder that represents this project.
   * 
   * @param pathstyle
   *          The format of the resolved File. Not <code>null</code>.
   * 
   * @return Returns the folder that represents this project. Not <code>null</code>.
   */
  File getFolder(PathStyle pathstyle);

  /**
   * <p>
   * Returns the comment that is maybe set in the <code>.project</code> file.
   * </p>
   * 
   * @return the comment that is maybe set in the <code>.project</code> file.
   */
  String getComment();

  /**
   * <p>
   * Returns whether this project exists. A project exists if the underlying file exists and if it is an directory.
   * </p>
   * 
   * @return Returns whether this project exists.
   */
  boolean exists();

  /**
   * <p>
   * Returns whether this project has a child with the given path.
   * </p>
   * 
   * @param path
   *          the path of child
   * @return Returns whether this project has a child with the given path.
   */
  boolean hasChild(String path);

  /**
   * <p>
   * Returns the child of this project with the given path.
   * </p>
   * 
   * @param path
   *          The child which shall be returned.
   * 
   * @return Returns the child of this project with the given path.
   */
  File getChild(String path);

  /**
   * <p>
   * </p>
   * 
   * @param path
   * @return
   */
  File[] getChildren(String[] path);

  /**
   * <p>
   * Returns the child of this project with the given path.
   * </p>
   * 
   * @param path
   *          The child which shall be returned.
   * @param relative
   *          true <=> Create a relative location.
   * 
   * @return Returns the child of this project with the given path.
   */
  File getChild(String path, PathStyle relative);

  /**
   * <p>
   * </p>
   * 
   * @param path
   * @param relative
   * @return
   */
  File[] getChildren(String[] path, PathStyle relative);

  /**
   * <p>
   * Returns whether this project has a file with the given name in the project's <tt>.settings</tt> directory.
   * </p>
   * 
   * @param settingsFileName
   *          the name of the file inside the <tt>.settings</tt> file
   * @return true if there is such a file otherwise false
   */
  boolean hasSettingsFile(String settingsFileName);

  /**
   * <p>
   * Returns the file with the given name from the project's <tt>.settings</tt> directory.
   * </p>
   * 
   * @param settingsFileName
   *          the name of the file inside the <tt>.settings</tt> file
   * @return the file - never null
   * 
   * @throws RuntimeException
   */
  File getSettingsFile(String settingsFileName) throws RuntimeException;

  /**
   * <p>
   * Returns whether the nature with the specified name is set or not.
   * </p>
   * 
   * @param natureName
   * @return Returns whether the nature with the specified name is set or not.
   */
  boolean hasNature(String natureName);

  /**
   * <p>
   * Returns whether the specified nature is set or not.
   * </p>
   * 
   * @param nature
   * @return Returns whether the specified nature is set or not.
   */
  boolean hasNature(ProjectNature nature);

  /**
   * <p>
   * Returns the project natures of the project.
   * </p>
   * 
   * @return Returns the project natures of the project.
   */
  ProjectNature[] getNatures();

  /**
   * <p>
   * Returns whether the role of the given type is set or not.
   * </p>
   * 
   * @param projectRoleClass
   * @return Returns whether the role of the given type is set or not.
   * 
   */
  boolean hasRole(Class<? extends ProjectRole> projectRoleClass);

  /**
   * <p>
   * Returns the role of the given type. If the role is not set, an exception will be thrown.
   * </p>
   * 
   * @param projectRoleClass
   * @return Returns the role of the given type. If the role is not set, an exception will be thrown.
   */
  <T extends ProjectRole> T getRole(Class<T> projectRoleClass);

  /**
   * <p>
   * Returns the roles of this project.
   * </p>
   * 
   * @return Returns the roles of this project.
   */
  ProjectRole[] getRoles();

  /**
   * <p>
   * Returns whether the build command with the specified name is set or not.
   * </p>
   * 
   * @param commandName
   * @return Returns whether the build command with the specified name is set or not.
   */
  boolean hasBuildCommand(String commandName);

  /**
   * <p>
   * Returns whether the specified build command is set or not.
   * </p>
   * 
   * @param command
   *          The command name that has to be tested.
   * 
   * @return Returns whether the specified build command is set or not.
   */
  boolean hasBuildCommand(BuildCommand command);

  /**
   * <p>
   * Returns the build commands of this project.
   * </p>
   * 
   * @return Returns the build commands of this project.
   */
  BuildCommand[] getBuildCommands();

  /**
   * <p>
   * Returns a list of all referenced project names.
   * </p>
   * 
   * @return A list of all reference project names. Not <code>null</code>.
   */
  String[] getReferencedProjects();

} /* ENDCLASS */