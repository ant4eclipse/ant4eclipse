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
package org.ant4eclipse.platform.ant.delegate;

import java.io.File;
import java.util.Enumeration;
import java.util.Hashtable;

import org.ant4eclipse.core.Assert;
import org.ant4eclipse.core.ant.TaskHelper;
import org.ant4eclipse.core.service.ServiceRegistry;
import org.ant4eclipse.platform.model.resource.Workspace;
import org.ant4eclipse.platform.model.resource.variable.EclipseVariableResolver;
import org.ant4eclipse.platform.model.resource.workspaceregistry.DefaultEclipseWorkspaceDefinition;
import org.ant4eclipse.platform.model.resource.workspaceregistry.WorkspaceRegistry;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.ProjectComponent;
import org.apache.tools.ant.types.PropertySet;

/**
 * Base class for all Tasks working with an eclipse workspace.
 * 
 * @todo [19-Mar-2006:KASI] The dir separator must be used while setting the properties.
 * 
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 */
public class WorkspaceDelegate extends AbstractAntDelegate {

  /** ANT-Attribute */
  private File      _workspaceDirectory;

  /** ANT-Attribute */
  private String    _pathseparator;

  /** ANT-Attribute */
  private String    _dirseparator;

  /** ANT-Attribute */
  private String    _variablesref;

  private Workspace _workspace;

  private boolean   _baseinitialised;

  /**
   * Prepares this basically workspace related functionality for the supplied ant component.
   * 
   * @param component
   *          An ANT fragment: task, condition or a type.
   */
  public WorkspaceDelegate(final ProjectComponent component) {
    super(component);
    this._pathseparator = File.pathSeparator;
    this._dirseparator = File.separator;

    this._variablesref = null;
    this._baseinitialised = false;
  }

  /**
   * Sets the workspace for this task.
   * 
   * @param workspace
   *          the workspace directory that should be associated with this Task
   * 
   * @deprecated use {@link WorkspaceDelegate#setWorkspaceDirectory(File)} instead. This method is for backward
   *             compatibility only.
   */
  public void setWorkspace(final File workspace) {
    setWorkspaceDirectory(workspace);
  }

  /**
   * @param workspaceDirectory
   */
  public void setWorkspaceDirectory(final File workspaceDirectory) {
    this._workspaceDirectory = workspaceDirectory;
  }

  /**
   * @return
   */
  public File getWorkspaceDirectory() {
    return this._workspaceDirectory;
  }

  /**
   * Returns whether a workspace has been set to this task.
   * 
   * @return true <=> The workspace has been set.
   */
  public boolean isWorkspaceDirectorySet() {
    return this._workspaceDirectory != null;
  }

  /**
   * Changes the path separator for this task.
   * 
   * @param newpathseparator
   *          The new path separator.
   */
  public void setPathSeparator(final String newpathseparator) {
    Assert.nonEmpty(newpathseparator);
    this._pathseparator = newpathseparator;
  }

  /**
   * Returns the currently used path separator.
   * 
   * @return The currently used path separator.
   */
  public String getPathSeparator() {
    return (this._pathseparator);
  }

  /**
   * Changes the current directory separator.
   * 
   * @param newdirseparator
   *          The new directory separator.
   */
  public void setDirSeparator(final String newdirseparator) {
    Assert.nonEmpty(newdirseparator);
    this._dirseparator = newdirseparator;
  }

  /**
   * Returns the currently used directory separator.
   * 
   * @return The currently used directory separator.
   */
  public String getDirSeparator() {
    return (this._dirseparator);
  }

  /**
   * Sets a reference to the property set which allows to resolve Eclipse variables using an ANT property set.
   * 
   * @param ref
   *          Name of the property set that will be used.
   */
  public void setVariablesRef(final String ref) {
    this._variablesref = ref;
  }

  /**
   * Returns the name of the property set used to resolve variables.
   * 
   * @return The name of the property set used to resolve variables. Maybe null.
   */
  public String getVariablesRef() {
    return (this._variablesref);
  }

  /**
   * Registers all ANT properties so they can be used within the resolving process for eclipse variables.
   * 
   * @param properties
   *          A map (String, String) of ANT properties.
   */
  @SuppressWarnings("unchecked")
  private void registerAntProperties(final Hashtable properties) {
    final Enumeration<String> enumeration = properties.keys();
    while (enumeration.hasMoreElements()) {
      final String key = enumeration.nextElement();
      final String value = (String) properties.get(key);
      getEclipseVariableResolver().setEclipseVariable(key, value);
    }
  }

  /**
   * @return Currently associated workspace with this task.
   */
  public Workspace getWorkspace() {
    init();
    return this._workspace;
  }

  /**
   * Returns whether a workspace has been set to this task.
   * 
   * @return true <=> The workspace has been set.
   */
  public boolean isWorkspaceSet() {
    return getWorkspace() != null;
  }

  /**
   * Invokes an exception in case the workspace has not been set.
   */
  public void requireWorkspaceSet() {
    if (!isWorkspaceSet()) {
      throw new BuildException("workspace has to be set!");
    }
  }

  /**
   * Changes the path property according to the developers requirements.
   * 
   * @param propertyname
   *          The name of the property that has to be set.
   * @param pathentries
   *          The path entries containing the values.
   */
  public void setPathProperty(final String propertyname, final File[] pathentries) {
    final String resolvedpath = TaskHelper.convertToString(pathentries, getPathSeparator(), getDirSeparator(),
        getAntProject());
    getAntProject().setProperty(propertyname, resolvedpath);
  }

  /**
   * Changes the string property.
   * 
   * @param propertyname
   *          The name of the property that has to be set.
   * @param value
   *          The value for the property.
   */
  public void setStringProperty(final String propertyname, final String value) {
    getAntProject().setProperty(propertyname, value);
  }

  /**
   * Does some initialisations on this class. This must be called before a task or condition makes an attempt to use it.
   */
  private void init() {
    if (this._baseinitialised) {
      return;
    }
    registerAntProperties(getAntProject().getProperties());
    if (this._variablesref != null) {
      final Object ref = getAntProject().getReference(this._variablesref);
      if (ref instanceof PropertySet) {
        registerAntProperties(((PropertySet) ref).getProperties());
      }
    }

    if (!getWorkspaceRegistry().containsWorkspace(this._workspaceDirectory.getAbsolutePath())) {
      this._workspace = getWorkspaceRegistry().registerWorkspace(this._workspaceDirectory.getAbsolutePath(),
          new DefaultEclipseWorkspaceDefinition(this._workspaceDirectory));
    } else {
      this._workspace = getWorkspaceRegistry().getWorkspace(this._workspaceDirectory.getAbsolutePath());
    }

    this._baseinitialised = true;
  }

  public WorkspaceRegistry getWorkspaceRegistry() {
    final WorkspaceRegistry workspaceRegistry = (WorkspaceRegistry) ServiceRegistry.instance().getService(
        WorkspaceRegistry.class.getName());
    return workspaceRegistry;
  }

  private EclipseVariableResolver getEclipseVariableResolver() {
    final EclipseVariableResolver resolver = (EclipseVariableResolver) ServiceRegistry.instance().getService(
        EclipseVariableResolver.class.getName());
    return resolver;
  }

} /* ENDCLASS */