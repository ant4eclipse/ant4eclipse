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
package org.ant4eclipse.platform.ant;

import java.util.Iterator;
import java.util.List;

import org.ant4eclipse.core.Assert;
import org.ant4eclipse.platform.ant.core.ProjectReferenceAwareComponent;
import org.ant4eclipse.platform.ant.core.delegate.ProjectReferenceAwareDelegate;
import org.ant4eclipse.platform.ant.core.delegate.SubElementDelegate;
import org.ant4eclipse.platform.ant.core.task.AbstractProjectSetBasedTask;
import org.ant4eclipse.platform.model.resource.EclipseProject;
import org.ant4eclipse.platform.tools.BuildOrderResolver;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.DynamicElement;

/**
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 * @author Nils Hartmann (nils@nilshartmann.net)
 */
public class GetBuildOrderTask extends AbstractProjectSetBasedTask implements DynamicElement,
    ProjectReferenceAwareComponent {

  /** the delegate used for handling sub elements (e.g. &lt;jdtClasspathContainerArgument&gt; */
  private final SubElementDelegate            _subElementDelegate;

  private final ProjectReferenceAwareDelegate _projectReferenceAwareDelegate;

  /** the property that should hold the ordered projects */
  private String                              _buildorderProperty;

  // /** - */
  // private NonJavaProjectHandling _nonjavaProjectHandling;

  public GetBuildOrderTask() {
    super();

    this._subElementDelegate = new SubElementDelegate(this);

    this._projectReferenceAwareDelegate = new ProjectReferenceAwareDelegate();
  }

  public String[] getProjectReferenceTypes() {
    return this._projectReferenceAwareDelegate.getProjectReferenceTypes();
  }

  public boolean isProjectReferenceTypesSet() {
    return this._projectReferenceAwareDelegate.isProjectReferenceTypesSet();
  }

  public void requireProjectReferenceTypesSet() {
    this._projectReferenceAwareDelegate.requireProjectReferenceTypesSet();
  }

  public void setProjectReferenceTypes(String buildOrderReferenceTypes) {
    this._projectReferenceAwareDelegate.setProjectReferenceTypes(buildOrderReferenceTypes);
  }

  // public NonJavaProjectHandling getNonJavaProjectHandling() {
  // if (this._nonjavaProjectHandling == null) {
  // this._nonjavaProjectHandling = new NonJavaProjectHandling("fail");
  // }
  // return this._nonjavaProjectHandling;
  // }
  //
  // public void setNonJavaProjectHandling(final NonJavaProjectHandling nonjavaProjectHandling) {
  // this._nonjavaProjectHandling = nonjavaProjectHandling;
  // }

  // /**
  // * Changes a flag which indicates whether all projects within a workspace shall be recognized or not.
  // *
  // * @param allprojects
  // * true <=> Take all projects within the workspace into account.
  // */
  // public void setAllProjects(final boolean allprojects) {
  // this._allprojects = allprojects;
  // }

  /**
   * @param buildorderProperty
   */
  public final void setBuildorderProperty(final String buildorderProperty) {
    this._buildorderProperty = buildorderProperty;
  }

  /**
   * <p>
   * Returns the name of the build order property.
   * </p>
   * 
   * @return The name of the build order property.
   */
  public final String getBuildorderProperty() {
    return this._buildorderProperty;
  }

  /**
   * <p>
   * Returns <code>true</code> if the build order property has been set.
   * </p>
   * 
   * @return <code>true</code> if the build order property has been set.
   */
  public final boolean isBuildorderPropertySet() {
    return ((this._buildorderProperty != null) && (this._buildorderProperty.length() > 0));
  }

  // public String getProjectNames() {
  // return this._projectNames;
  // }
  //
  // public void setProjectNames(final String projectNames) {
  // this._projectNames = projectNames;
  // }
  //
  // public boolean isProjectNamesSet() {
  // return ((this._projectNames != null) && (this._projectNames.length() > 0));
  // }

  /**
   * 
   */
  public final void requireBuildorderPropertySet() {
    if (!isBuildorderPropertySet()) {
      throw new BuildException("buildorderProperty has to be set!");
    }
  }

  /**
   * @see org.apache.tools.ant.DynamicElement#createDynamicElement(java.lang.String)
   */
  public Object createDynamicElement(String name) throws BuildException {
    return this._subElementDelegate.createDynamicElement(name);
  }

  // protected void requireProjectSetOrProjectNamesSet() {
  // if (!this._allprojects) {
  // if (!isProjectSetSet() && !isProjectNamesSet()) {
  // throw new BuildException("Missing parameter: neither 'projectSet' nor 'projectNames' has been set.");
  // }
  // if (isProjectSetSet() && isProjectNamesSet()) {
  // throw new BuildException("Duplicate parameter: either 'projectSet' or 'projectNames' must be set.");
  // }
  // }
  // }

  @Override
  protected void doExecute() {
    requireWorkspaceSet();
    requireTeamProjectSetOrProjectNamesSet();
    requireBuildorderPropertySet();

    // calculate build order
    final List<EclipseProject> orderedProjects = BuildOrderResolver.resolveBuildOrder(getWorkspace(),
        getProjectNames(), this._projectReferenceAwareDelegate.getProjectReferenceTypes(), this._subElementDelegate
            .getSubElements());

    getProject().setProperty(this._buildorderProperty, convertToString(orderedProjects, ','));
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
  private String convertToString(final List<EclipseProject> projects, final char separator) {
    Assert.notNull(projects);
    final StringBuffer buffer = new StringBuffer();

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

  // public static class NonJavaProjectHandling extends EnumeratedAttribute {
  //
  // public NonJavaProjectHandling() {
  // // needed by Ant to instantiate
  // }
  //
  // public NonJavaProjectHandling(final String value) {
  // super();
  // setValue(value);
  // }
  //
  // /**
  // * {@inheritDoc}
  // */
  // @Override
  // public String[] getValues() {
  // return new String[] { "fail", "ignore", "prepend", "append" };
  // }
  //
  // public int asBuildOrderResolverConstant() {
  // return getIndex() + 1;
  // }
  //
  // }
}