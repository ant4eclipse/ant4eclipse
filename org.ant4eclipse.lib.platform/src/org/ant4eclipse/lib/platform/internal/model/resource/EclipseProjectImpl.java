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
package org.ant4eclipse.lib.platform.internal.model.resource;

import org.ant4eclipse.core.service.ServiceRegistry;


import org.ant4eclipse.lib.core.Assert;
import org.ant4eclipse.lib.platform.internal.model.resource.role.NatureNicknameRegistry;
import org.ant4eclipse.lib.platform.model.resource.BuildCommand;
import org.ant4eclipse.lib.platform.model.resource.EclipseProject;
import org.ant4eclipse.lib.platform.model.resource.ProjectNature;
import org.ant4eclipse.lib.platform.model.resource.Workspace;
import org.ant4eclipse.lib.platform.model.resource.role.AbstractProjectRole;
import org.ant4eclipse.lib.platform.model.resource.role.ProjectRole;

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
  public static final String       SETTINGS_FOLDER_NAME = ".settings";

  /** the workspace that contains this project */
  private Workspace                _workspace;

  /** the file that represents this project */
  private File                     _projectDirectory;

  /** the project name specified in the project description */
  private String                   _specifiedName;

  /** the <tt>.settings</tt> folder of the project or <tt>null<tt> if there is no <tt>.settings</tt> folder */
  private File                     _settingsFolder;

  /** the project comment */
  private String                   _comment;

  /** the list of project natures */
  private List<ProjectNature>      _natures;

  /** the list of project roles */
  private List<ProjectRole>        _roles;

  /** the list of buildCommands */
  private List<BuildCommand>       _buildCommands;

  /** the referenced project specified in the project description */
  private List<String>             _referencedProjects;

  /** the linked resources specified in the project description */
  private List<LinkedResourceImpl> _linkedResources;

  /** the names of the linked resource. used for the mapping */
  private List<String>             _linkedResourceNames;

  /**
   * Creates a new instance of type project.
   * 
   * @param workspace
   *          the workspace
   * @param projectName
   *          the name of the project
   */
  public EclipseProjectImpl(Workspace workspace, File projectDirectory) {
    Assert.isDirectory(projectDirectory);

    this._workspace = workspace;
    this._projectDirectory = projectDirectory;
    this._natures = new LinkedList<ProjectNature>();
    this._roles = new LinkedList<ProjectRole>();
    this._buildCommands = new LinkedList<BuildCommand>();
    this._referencedProjects = new LinkedList<String>();
    this._linkedResources = new LinkedList<LinkedResourceImpl>();
    this._linkedResourceNames = new LinkedList<String>();

    File settingsFolder = getChild(SETTINGS_FOLDER_NAME);
    this._settingsFolder = (settingsFolder.isDirectory() ? settingsFolder : null);
  }

  /**
   * {@inheritDoc}
   */
  public String getSpecifiedName() {
    return this._specifiedName;
  }

  /**
   * @param specifiedName
   *          The specifiedName to set.
   */
  public void setSpecifiedName(String specifiedName) {
    this._specifiedName = specifiedName;
  }

  /**
   * {@inheritDoc}
   */
  public String getComment() {
    return this._comment;
  }

  /**
   * @param comment
   *          The comment to set.
   */
  public void setComment(String comment) {
    this._comment = comment;
  }

  /**
   * {@inheritDoc}
   */
  public String getFolderName() {
    return this._projectDirectory.getName();
  }

  /**
   * {@inheritDoc}
   */
  public File getFolder() {
    return this._projectDirectory;
  }

  /**
   * {@inheritDoc}
   */
  public File getFolder(PathStyle pathstyle) {
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
   * {@inheritDoc}
   */
  public boolean exists() {
    return this._projectDirectory.isDirectory();
  }

  /**
   * {@inheritDoc}
   */
  public boolean hasChild(String path) {
    Assert.notNull(path);
    File child = getChild(path);
    return child.exists();
  }

  /**
   * {@inheritDoc}
   */
  public File getChild(String path) {
    return (getChild(path, PathStyle.ABSOLUTE));
  }

  /**
   * {@inheritDoc}
   */
  public File[] getChildren(String[] path) {
    return getChildren(path, PathStyle.ABSOLUTE);
  }

  /**
   * {@inheritDoc}
   */
  public File[] getChildren(String[] path, PathStyle relative) {
    Assert.notNull(path);

    File[] result = new File[path.length];

    for (int i = 0; i < result.length; i++) {
      result[i] = getChild(path[i], relative);
    }

    return result;
  }

  protected boolean hasSettingsFolder() {
    return (this._settingsFolder != null);
  }

  /**
   * {@inheritDoc}
   */
  public boolean hasSettingsFile(String settingsFileName) {
    // check if settings folder exists
    if (!hasSettingsFolder()) {
      return false;
    }
    File settingsFile = new File(this._settingsFolder, settingsFileName);
    // is it an existing file?
    return settingsFile.isFile();
  }

  /**
   * {@inheritDoc}
   */
  public File getSettingsFile(String settingsFileName) throws RuntimeException {
    Assert.notNull("The parameter 'settingsFileName' must not be null", settingsFileName);
    Assert.assertTrue(hasSettingsFolder(), "The project '" + getFolderName() + "' must have a .settings folder");

    File settingsFile = new File(this._settingsFolder, settingsFileName);
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
   * {@inheritDoc}
   */
  public File getChild(String path, PathStyle pathstyle) {
    Assert.notNull(path);

    String name = path;
    String rest = null;
    int idx = firstFileSeparator(path);
    if (idx != -1) {
      name = path.substring(0, idx);
      rest = path.substring(idx + 1);
    }

    // handle linked resource
    if (isLinkedResource(name)) {
      LinkedResourceImpl resource = getLinkedResource(name);
      if ((pathstyle != PathStyle.ABSOLUTE) && (resource.getRelativeLocation() == null)) {
        // TODO
        throw (new RuntimeException("cannot calculate relative location for linked resource '" + name + "' !"));
      }
      File result = new File(pathstyle != PathStyle.ABSOLUTE ? resource.getRelativeLocation() : resource.getLocation());
      if (rest != null) {
        result = new File(result, rest);
      }
      if ((pathstyle == PathStyle.ABSOLUTE) && (!result.isAbsolute())) {
        result = new File(this._projectDirectory, result.getPath());
      }
      return (result);
    }

    //
    if (pathstyle == PathStyle.PROJECT_RELATIVE_WITHOUT_LEADING_PROJECT_NAME) {
      if (path.length() == 0) {
        path = ".";
      }
      return (new File(path));
    } else if (pathstyle == PathStyle.PROJECT_RELATIVE_WITH_LEADING_PROJECT_NAME) {
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
  private int firstFileSeparator(String str) {
    int idx1 = str.indexOf('/');
    int idx2 = str.indexOf('\\');
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
  public void addNature(ProjectNature nature) {
    Assert.notNull(nature);
    if (!this._natures.contains(nature)) {
      this._natures.add(nature);
    }
  }

  /**
   * {@inheritDoc}
   */
  public boolean hasNature(String natureName) {
    Assert.notNull(natureName);
    return hasNature(new ProjectNatureImpl(natureName));
  }

  /**
   * {@inheritDoc}
   */
  public boolean hasNature(ProjectNature nature) {
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
   * {@inheritDoc}
   */
  public ProjectNature[] getNatures() {
    return this._natures.toArray(new ProjectNature[this._natures.size()]);
  }

  /**
   * @param referencedProject
   */
  public void addReferencedProject(String referencedProject) {
    if ((referencedProject != null) && !this._referencedProjects.contains(referencedProject)) {
      this._referencedProjects.add(referencedProject);
    }
  }

  /**
   * {@inheritDoc}
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
  public void addRole(ProjectRole role) {
    Assert.notNull(role);
    if (hasRole(role.getClass())) {
      throw new RuntimeException("ProjectRole " + role.getClass() + " is already set!");
    }

    this._roles.add(role);
  }

  /**
   * {@inheritDoc}
   */
  public boolean hasRole(Class<? extends ProjectRole> projectRoleClass) {
    Assert.notNull(projectRoleClass);
    Iterator<ProjectRole> iterator = this._roles.iterator();
    while (iterator.hasNext()) {
      AbstractProjectRole role = (AbstractProjectRole) iterator.next();
      if (projectRoleClass.isAssignableFrom(role.getClass())) {
        return true;
      }
    }
    return false;
  }

  /**
   * {@inheritDoc}
   */
  public ProjectRole getRole(Class<? extends ProjectRole> projectRoleClass) {
    Assert.notNull(projectRoleClass);
    Assert.assertTrue(hasRole(projectRoleClass), "hasRole(projectRoleClass) on project '" + getFolderName()
        + "'has to be true for role '" + projectRoleClass + "'!");

    Iterator<ProjectRole> iterator = this._roles.iterator();

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
   * {@inheritDoc}
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
  public void addBuildCommand(BuildCommand command) {
    Assert.notNull(command);

    this._buildCommands.add(command);
  }

  /**
   * {@inheritDoc}
   */
  public boolean hasBuildCommand(String commandName) {
    Assert.notNull(commandName);
    BuildCommand command = new BuildCommandImpl(commandName);
    return hasBuildCommand(command);
  }

  /**
   * {@inheritDoc}
   */
  public boolean hasBuildCommand(BuildCommand command) {
    Assert.notNull(command);
    return this._buildCommands.contains(command);
  }

  /**
   * {@inheritDoc}
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
  public void addLinkedResource(LinkedResourceImpl linkedResource) {
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
  public LinkedResourceImpl getLinkedResource(String name) {
    Assert.assertTrue(isLinkedResource(name), "Cannot retrieve linked resource '" + name + "' !");
    int idx = this._linkedResourceNames.indexOf(name);
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
  public boolean isLinkedResource(String name) {
    Assert.notNull(name);
    return (this._linkedResourceNames.contains(name));
  }

  /**
   * {@inheritDoc}
   */
  public Workspace getWorkspace() {
    return this._workspace;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String toString() {
    StringBuffer buffer = new StringBuffer();
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
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null) {
      return false;
    }
    if (o.getClass() != getClass()) {
      return false;
    }
    EclipseProjectImpl castedObj = (EclipseProjectImpl) o;
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