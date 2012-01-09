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
package org.ant4eclipse.ant.platform;

import org.ant4eclipse.ant.platform.core.ProjectReferenceAwareComponent;
import org.ant4eclipse.ant.platform.core.SubElementAndAttributesComponent;
import org.ant4eclipse.ant.platform.core.delegate.ProjectReferenceAwareDelegate;
import org.ant4eclipse.ant.platform.core.delegate.SubElementAndAttributesDelegate;
import org.ant4eclipse.ant.platform.core.task.AbstractProjectSetBasedTask;
import org.ant4eclipse.lib.core.Assure;
import org.ant4eclipse.lib.platform.model.resource.EclipseProject;
import org.ant4eclipse.lib.platform.tools.BuildOrderResolver;
import org.apache.tools.ant.BuildException;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * Calculates the build order for a set of projects.
 * </p>
 * 
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 * @author Nils Hartmann (nils@nilshartmann.net)
 */
public class GetBuildOrderTask extends AbstractProjectSetBasedTask implements SubElementAndAttributesComponent,
    ProjectReferenceAwareComponent {

  /** the delegate used for handling sub elements (e.g. &lt;jdtClasspathContainerArgument&gt; */
  private SubElementAndAttributesDelegate _subElementAndAttributesDelegate;

  /** the project reference delegate */
  private ProjectReferenceAwareDelegate   _projectReferenceAwareDelegate;

  /** the property that should hold the ordered projects */
  private String                          _buildorderProperty;

  /**
   * <p>
   * Creates a new instance of type {@link GetBuildOrderTask}.
   * </p>
   */
  public GetBuildOrderTask() {
    _subElementAndAttributesDelegate = new SubElementAndAttributesDelegate(this);
    _projectReferenceAwareDelegate = new ProjectReferenceAwareDelegate();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setProjectReferenceTypes(String buildOrderReferenceTypes) {
    _projectReferenceAwareDelegate.setProjectReferenceTypes(buildOrderReferenceTypes);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public List<String> getProjectReferenceTypes() {
    return _projectReferenceAwareDelegate.getProjectReferenceTypes();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isProjectReferenceTypesSet() {
    return _projectReferenceAwareDelegate.isProjectReferenceTypesSet();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void requireProjectReferenceTypesSet() {
    _projectReferenceAwareDelegate.requireProjectReferenceTypesSet();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Object createDynamicElement(String name) throws BuildException {
    return _subElementAndAttributesDelegate.createDynamicElement(name);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public List<Object> getSubElements() {
    return _subElementAndAttributesDelegate.getSubElements();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Map<String, String> getSubAttributes() {
    return _subElementAndAttributesDelegate.getSubAttributes();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setDynamicAttribute(String name, String value) throws BuildException {
    _subElementAndAttributesDelegate.setDynamicAttribute(name, value);
  }

  /**
   * <p>
   * Sets the property that holds the ordered project names.
   * </p>
   * 
   * @param buildorderProperty
   */
  public final void setBuildorderProperty(String buildorderProperty) {
    _buildorderProperty = buildorderProperty;
  }

  /**
   * <p>
   * Returns the name of the build order property.
   * </p>
   * 
   * @return The name of the build order property.
   */
  public final String getBuildorderProperty() {
    return _buildorderProperty;
  }

  /**
   * <p>
   * Returns <code>true</code> if the build order property has been set.
   * </p>
   * 
   * @return <code>true</code> if the build order property has been set.
   */
  public final boolean isBuildorderPropertySet() {
    return ((_buildorderProperty != null) && (_buildorderProperty.length() > 0));
  }

  /**
   * <p>
   * Throws a {@link BuildException} if the build order property is not set.
   * </p>
   */
  public final void requireBuildorderPropertySet() {
    if (!isBuildorderPropertySet()) {
      throw new BuildException("buildorderProperty has to be set!");
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected void doExecute() {
    
    requireWorkspaceDirectoryOrWorkspaceIdSet();
    requireAllWorkspaceProjectsOrProjectSetOrProjectNamesSet();
    requireBuildorderPropertySet();

    // calculate build order
    List<EclipseProject> orderedProjects = BuildOrderResolver.resolveBuildOrder(
        getWorkspace(), 
        getProjectNames(), 
        _projectReferenceAwareDelegate.getProjectReferenceTypes(), 
        _subElementAndAttributesDelegate.getSubElements()
    );

    getProject().setProperty(_buildorderProperty, convertToString(orderedProjects, ','));
    
  }

  /**
   * Joins the names of projects using a specific separator character.
   * 
   * @param projects
   *          The list of projects that will be joined.
   * @param separator
   *          The separator character.
   * 
   * @return A String which contains the list of names.
   */
  private String convertToString(List<EclipseProject> projects, char separator) {
    Assure.notNull("projects", projects);

    // create StringBuffer
    StringBuffer buffer = new StringBuffer();

    // construct result
    for (Iterator<EclipseProject> iterator = projects.iterator(); iterator.hasNext();) {
      EclipseProject eclipseProject = iterator.next();
      buffer.append(eclipseProject.getFolderName());
      if (iterator.hasNext()) {
        buffer.append(separator);
      }
    }

    // return result
    return buffer.toString();
  }
}