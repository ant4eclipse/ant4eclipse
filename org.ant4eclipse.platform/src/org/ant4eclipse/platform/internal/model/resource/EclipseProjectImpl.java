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
package org.ant4eclipse.platform.internal.model.resource;

import org.ant4eclipse.core.Assert;
import org.ant4eclipse.core.service.ServiceRegistry;

import org.ant4eclipse.platform.internal.model.resource.role.NatureNicknameRegistry;
import org.ant4eclipse.platform.model.resource.BuildCommand;
import org.ant4eclipse.platform.model.resource.EclipseProject;
import org.ant4eclipse.platform.model.resource.ProjectNature;
import org.ant4eclipse.platform.model.resource.Workspace;
import org.ant4eclipse.platform.model.resource.role.AbstractProjectRole;
import org.ant4eclipse.platform.model.resource.role.ProjectRole;

import java.io.File;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * Encapsultes a project. A project contains a workspace and is represented by a directory in this workspace. A project
 * can have multiple natures and multiple roles.
 * 
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 */
public final class EclipseProjectImpl implements EclipseProject {

  /** The name of the projects <tt>.settings</tt> folder */
  public static final String             SETTINGS_FOLDER_NAME = ".settings";

  /** the workspace that contains this project */
  private final Workspace                _workspace;

  /** the file that represents this project */
  private final File                     _projectDirectory;

  /** the project name specified in the project description */
  private String                         _specifiedName;

  /** the <tt>.settings</tt> folder of the project or <tt>null<tt> if there is no <tt>.settings</tt> folder */
  private final File                     _settingsFolder;

  /** the project comment */
  private String                         _comment;

  /** the list of project natures */
  private final List<ProjectNature>      _natures;

  /** the list of project roles */
  private final List<ProjectRole>        _roles;

  /** the list of buildCommands */
  private final List<BuildCommand>       _buildCommands;

  /** the referenced project specified in the project description */
  private final List<String>             _referencedProjects;

  /** the linked resources specified in the project description */
  private final List<LinkedResourceImpl> _linkedResources;

  /** the names of the linked resource. used for the mapping */
  private final List<String>             _linkedResourceNames;

  /**
   * Creates a new instance of type project.
   * 
   * @param workspace
   *          the workspace
   * @param projectName
   *          the name of the project
   */
  public EclipseProjectImpl(final Workspace workspace, final File projectDirectory) {
    Assert.isDirectory(projectDirectory);

    this._workspace = workspace;
    this._projectDirectory = projectDirectory;
    this._natures = new LinkedList<ProjectNature>();
    this._roles = new LinkedList<ProjectRole>();
    this._buildCommands = new LinkedList<BuildCommand>();
    this._referencedProjects = new LinkedList<String>();
    this._linkedResources = new LinkedList<LinkedResourceImpl>();
    this._linkedResourceNames = new LinkedList<String>();

    final File settingsFolder = getChild(SETTINGS_FOLDER_NAME);
    this._settingsFolder = (settingsFolder.isDirectory() ? settingsFolder : null);
  }

  /**
   * @return Returns the specifiedName.
   */
  public String getSpecifiedName() {
    return this._specifiedName;
  }

  /**
   * @param specifiedName
   *          The specifiedName to set.
   */
  public void setSpecifiedName(final String specifiedName) {
    this._specifiedName = specifiedName;
  }

  /**
   * @return Returns the comment.
   */
  public String getComment() {
    return this._comment;
  }

  /**
   * @param comment
   *          The comment to set.
   */
  public void setComment(final String comment) {
    this._comment = comment;
  }

  /**
   * Returns the name of the project
   * 
   * @return the name of the project
   */
  public String getFolderName() {
    return this._projectDirectory.getName();
  }

  /**
   * Returns the folder that represents this project.
   * 
   * @return Returns the folder that represents this project.
   */
  public File getFolder() {
    return this._projectDirectory;
  }

  /**
   * {@inheritDoc}
   */
  public File getFolder(final PathStyle pathstyle) {
    Assert.notNull(pathstyle);
    if (pathstyle == PathStyle.PROJECT_RELATIVE_WITHOUT_LEADING_PROJECT_NAME) {
      return new File(".");
    } else if (pathstyle == PathStyle.PROJECT_RELATIVE_WITH_LEADING_PROJECT_NAME) {
      return new File(this._projectDirectory.getName());
    } else /* if (pathstyle == PathStyle.ABSOLUTE) */{
      return this._projectDirectory.getAbsoluteFile();
    }
  }

  /**
   * Returns whether this project exists. A project exists if the underlying file exists and if it is an directory.
   * 
   * @return Returns whether this project exists.
   */
  public boolean exists() {
    return this._projectDirectory.isDirectory();
  }

  /**
   * Returns whether this project has a child with the given path.
   * 
   * @param path
   *          the path of child
   * @return Returns whether this project has a child with the given path.
   */
  public boolean hasChild(final String path) {
    Assert.notNull(path);
    final File child = getChild(path);
    return child.exists();
  }

  /**
   * Returns the child of this project with the given path.
   * 
   * @param path
   *          The child which shall be returned.
   * 
   * @return Returns the child of this project with the given path.
   */
  public File getChild(final String path) {
    return (getChild(path, PathStyle.ABSOLUTE));
  }

  public File[] getChildren(final String[] path) {
    return getChildren(path, PathStyle.ABSOLUTE);
  }

  public File[] getChildren(final String[] path, final PathStyle relative) {
    Assert.notNull(path);

    final File[] result = new File[path.length];

    for (int i = 0; i < result.length; i++) {
      result[i] = getChild(path[i], relative);
    }

    return result;
  }

  protected boolean hasSettingsFolder() {
    return (this._settingsFolder != null);
  }

  /*
   * (non-Javadoc)
   * 
   * @see net.sf.ant4eclipse.model.platform.resource.EclipseProject#hasSettingsFile(java.lang.String)
   */
  public boolean hasSettingsFile(final String settingsFileName) {
    // check if settings folder exists
    if (!hasSettingsFolder()) {
      return false;
    }

    final File settingsFile = new File(this._settingsFolder, settingsFileName);

    // is it an existing file?
    return settingsFile.isFile();
  }

  /*
   * (non-Javadoc)
   * 
   * @see net.sf.ant4eclipse.model.platform.resource.EclipseProject#getSettingsFile(java.lang.String)
   */
  public File getSettingsFile(final String settingsFileName) throws RuntimeException {
    Assert.notNull("The parameter 'settingsFileName' must not be null", settingsFileName);
    Assert.assertTrue(hasSettingsFolder(), "The project '" + getFolderName() + "' must have a .settings folder");

    final File settingsFile = new File(this._settingsFolder, settingsFileName);
    if (!settingsFile.exists()) {
      throw new RuntimeException("Settings File '" + settingsFileName + "' not found in project '" + getFolderName()
          + "'");
    }

    if (!settingsFile.isFile()) {
      throw new RuntimeException("Settings File '" + settingsFile + "' in project '" + getFolderName()
          + "' is not a file");
    }

    return settingsFile;
  }

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
  public File getChild(String path, final PathStyle relative) {
    Assert.notNull(path);

    String name = path;
    String rest = null;
    final int idx = firstFileSeparator(path);
    if (idx != -1) {
      name = path.substring(0, idx);
      rest = path.substring(idx + 1);
    }

    // handle linked resource
    if (isLinkedResource(name)) {
      final LinkedResourceImpl resource = getLinkedResource(name);
      if ((relative != PathStyle.ABSOLUTE) && (resource.getRelativeLocation() == null)) {
        // TODO
        throw (new RuntimeException("cannot calculate relative location for linked resource '" + name + "' !"));
      }
      File result = new File(relative != PathStyle.ABSOLUTE ? resource.getRelativeLocation() : resource.getLocation());
      if (rest != null) {
        result = new File(result, rest);
      }
      if ((relative == PathStyle.ABSOLUTE) && (!result.isAbsolute())) {
        result = new File(this._projectDirectory, result.getPath());
      }
      return (result);
    }

    //
    if (relative == PathStyle.PROJECT_RELATIVE_WITHOUT_LEADING_PROJECT_NAME) {
      if (path.length() == 0) {
        path = ".";
      }
      return (new File(path));
    } else if (relative == PathStyle.PROJECT_RELATIVE_WITH_LEADING_PROJECT_NAME) {
      return (new File(this._projectDirectory.getName(), path));
    }

    // handle ABSOLUTE
    return (new File(this._projectDirectory, path));
  }

  /**
   * Returns the index of the first file separator.
   * 
   * @param str
   *          The string that shall be tested.
   * 
   * @return The index of the first file separator or -1.
   */
  private int firstFileSeparator(final String str) {
    final int idx1 = str.indexOf('/');
    final int idx2 = str.indexOf('\\');
    if ((idx1 == -1) && (idx2 == -1)) {
      return (-1);
    }
    if ((idx1 != -1) && (idx2 != -1)) {
      return (Math.min(idx1, idx2));
    }
    return (Math.max(idx1, idx2));
  }

  /**
   * Adds the specified nature to the project.
   * 
   * @param nature
   *          the nature to add.
   */
  public void addNature(final ProjectNature nature) {
    Assert.notNull(nature);
    if (!this._natures.contains(nature)) {
      this._natures.add(nature);
    }
  }

  /**
   * Returns whether the nature with the specified name is set or not.
   * 
   * @param natureName
   * @return Returns whether the nature with the specified name is set or not.
   */
  public boolean hasNature(final String natureName) {
    Assert.notNull(natureName);

    return hasNature(new ProjectNatureImpl(natureName));
  }

  /**
   * <p>
   * Returns whether the specified nature is set or not.
   * </p>
   * 
   * @param nature
   * @return Returns whether the specified nature is set or not.
   */
  public boolean hasNature(final ProjectNature nature) {
    Assert.notNull(nature);

    // nature unknown:
    if (!this._natures.contains(nature)) {

      NatureNicknameRegistry nicknameRegistry = ServiceRegistry.instance().getService(NatureNicknameRegistry.class);

      // try if the user supplied an abbreviation
      String abbreviation = nature.getName().toLowerCase();
      if (nicknameRegistry.hasNatureForNickname(abbreviation)) {
        // check the nature with the full id now
        String[] ids = nicknameRegistry.getNaturesForNickname(abbreviation);
        for (String id : ids) {
          if (hasNature(id)) {
            return true;
          }
        }
      }
      // there's no mapping so we don't have an abbreviation here
      return false;
    }

    // nature known:
    else {
      return true;
    }
  }

  /**
   * Returns the project natures of the project.
   * 
   * @return Returns the project natures of the project.
   */
  public ProjectNature[] getNatures() {
    return this._natures.toArray(new ProjectNature[this._natures.size()]);
  }

  /**
   * @param referencedProject
   */
  public void addReferencedProject(final String referencedProject) {
    if ((referencedProject != null) && !this._referencedProjects.contains(referencedProject)) {
      this._referencedProjects.add(referencedProject);
    }
  }

  /**
   * Returns a list of all referenced project names.
   * 
   * @return A list of all reference project names.
   */
  public String[] getReferencedProjects() {
    return this._referencedProjects.toArray(new String[this._referencedProjects.size()]);
  }

  /**
   * Adds the specified role to the EclipseProject.
   * 
   * @param role
   *          Adds the specified role to the EclipseProject.
   */
  public void addRole(final ProjectRole role) {
    Assert.notNull(role);
    if (hasRole(role.getClass())) {
      throw new RuntimeException("ProjectRole " + role.getClass() + " is already set!");
    }

    this._roles.add(role);
  }

  /**
   * Returns whether the role of the given type is set or not.
   * 
   * @param projectRoleClass
   * @return Returns whether the role of the given type is set or not.
   * 
   */
  public boolean hasRole(final Class<? extends ProjectRole> projectRoleClass) {
    Assert.notNull(projectRoleClass);

    final Iterator<ProjectRole> iterator = this._roles.iterator();

    while (iterator.hasNext()) {
      final AbstractProjectRole role = (AbstractProjectRole) iterator.next();
      if (projectRoleClass.isAssignableFrom(role.getClass())) {
        return true;
      }
    }

    return false;
  }

  /**
   * Returns the role of the given type. If the role is not set, an exception will be thrown.
   * 
   * @param projectRoleClass
   * @return Returns the role of the given type. If the role is not set, an exception will be thrown.
   */
  public ProjectRole getRole(final Class<? extends ProjectRole> projectRoleClass) {
    Assert.notNull(projectRoleClass);
    Assert.assertTrue(hasRole(projectRoleClass), "hasRole(projectRoleClass) on project '" + getFolderName()
        + "'has to be true for role '" + projectRoleClass + "'!");

    final Iterator<ProjectRole> iterator = this._roles.iterator();

    ProjectRole role = null;

    while (iterator.hasNext()) {
      role = iterator.next();
      if (projectRoleClass.isAssignableFrom(role.getClass())) {
        break;
      }
    }
    return role;
  }

  /**
   * Returns the roles of this project.
   * 
   * @return Returns the roles of this project.
   */
  public ProjectRole[] getRoles() {
    return this._roles.toArray(new ProjectRole[this._roles.size()]);
  }

  /**
   * Adds the specified build command to the project.
   * 
   * @param command
   *          the specified build command to the project.
   */
  public void addBuildCommand(final BuildCommand command) {
    Assert.notNull(command);

    this._buildCommands.add(command);
  }

  /**
   * Returns whether the build command with the specified name is set or not.
   * 
   * @param commandName
   * @return Returns whether the build command with the specified name is set or not.
   */
  public boolean hasBuildCommand(final String commandName) {
    Assert.notNull(commandName);

    final BuildCommand command = new BuildCommandImpl(commandName);

    return hasBuildCommand(command);
  }

  /**
   * Returns whether the specified build command is set or not.
   * 
   * @param command
   *          The command name that has to be tested.
   * 
   * @return Returns whether the specified build command is set or not.
   */
  public boolean hasBuildCommand(final BuildCommand command) {
    Assert.notNull(command);

    return this._buildCommands.contains(command);
  }

  /**
   * Returns the build commands of this project.
   * 
   * @return Returns the build commands of this project.
   */
  public BuildCommand[] getBuildCommands() {
    return this._buildCommands.toArray(new BuildCommand[0]);
  }

  /**
   * Adds a new linked resource to the project.
   * 
   * @param linkedResource
   *          the linked resource to add.
   */
  public void addLinkedResource(final LinkedResourceImpl linkedResource) {
    Assert.notNull(linkedResource);

    if (!this._linkedResources.contains(linkedResource)) {
      this._linkedResources.add(linkedResource);
      this._linkedResourceNames.add(linkedResource.getName());
    }
  }

  /**
   * Returns a specific LinkedResource instance.
   * 
   * @param name
   *          The name of the desired LinkedResource instance.
   * 
   * @return The desired LinkedResource instance.
   */
  public LinkedResourceImpl getLinkedResource(final String name) {
    Assert.assertTrue(isLinkedResource(name), "Cannot retrieve linked resource '" + name + "' !");
    final int idx = this._linkedResourceNames.indexOf(name);
    return this._linkedResources.get(idx);
  }

  /**
   * Returns true if the supplied name refers to a linked resource.
   * 
   * @param name
   *          The name of a potential linked resource.
   * 
   * @return true <=> The name applies to a specific linked resource.
   */
  public boolean isLinkedResource(final String name) {
    Assert.notNull(name);
    return (this._linkedResourceNames.contains(name));
  }

  /**
   * Returns the workspace the project belongs to.
   * 
   * @return Returns the workspace the project belongs to.
   */
  public Workspace getWorkspace() {
    return this._workspace;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String toString() {
    final StringBuffer buffer = new StringBuffer();
    buffer.append("[EclipseProject:");
    buffer.append(" name: ");
    buffer.append(getSpecifiedName());
    buffer.append(" folder: ");
    buffer.append(getFolder());
    buffer.append("]");
    return buffer.toString();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean equals(final Object o) {
    if (this == o) {
      return true;
    }
    if (o == null) {
      return false;
    }
    if (o.getClass() != getClass()) {
      return false;
    }
    final EclipseProjectImpl castedObj = (EclipseProjectImpl) o;
    return ((this._workspace == null ? castedObj._workspace == null : this._workspace.equals(castedObj._workspace))
        && (this._projectDirectory == null ? castedObj._projectDirectory == null : this._projectDirectory
            .equals(castedObj._projectDirectory))
        && (this._specifiedName == null ? castedObj._specifiedName == null : this._specifiedName
            .equals(castedObj._specifiedName))
        && (this._comment == null ? castedObj._comment == null : this._comment.equals(castedObj._comment))
        && (this._natures == null ? castedObj._natures == null : this._natures.equals(castedObj._natures))
        && (this._roles == null ? castedObj._roles == null : this._roles.equals(castedObj._roles))
        && (this._buildCommands == null ? castedObj._buildCommands == null : this._buildCommands
            .equals(castedObj._buildCommands))
        && (this._referencedProjects == null ? castedObj._referencedProjects == null : this._referencedProjects
            .equals(castedObj._referencedProjects)) && (this._linkedResources == null ? castedObj._linkedResources == null
        : this._linkedResources.equals(castedObj._linkedResources)));
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public int hashCode() {
    int hashCode = 1;
    hashCode = 31 * hashCode + (this._workspace == null ? 0 : this._workspace.hashCode());
    hashCode = 31 * hashCode + (this._projectDirectory == null ? 0 : this._projectDirectory.hashCode());
    hashCode = 31 * hashCode + (this._specifiedName == null ? 0 : this._specifiedName.hashCode());
    hashCode = 31 * hashCode + (this._comment == null ? 0 : this._comment.hashCode());
    hashCode = 31 * hashCode + (this._natures == null ? 0 : this._natures.hashCode());
    hashCode = 31 * hashCode + (this._roles == null ? 0 : this._roles.hashCode());
    hashCode = 31 * hashCode + (this._buildCommands == null ? 0 : this._buildCommands.hashCode());
    hashCode = 31 * hashCode + (this._referencedProjects == null ? 0 : this._referencedProjects.hashCode());
    hashCode = 31 * hashCode + (this._linkedResources == null ? 0 : this._linkedResources.hashCode());
    return hashCode;
  }
}