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
package net.sf.ant4eclipse.ant.platform;

import java.io.File;

import net.sf.ant4eclipse.ant.Ant4EclipseConfiguration;
import net.sf.ant4eclipse.ant.platform.delegate.ProjectDelegate;
import net.sf.ant4eclipse.model.platform.resource.EclipseProject;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.ProjectComponent;
import org.apache.tools.ant.taskdefs.condition.Condition;

/**
 * An ant condition that allows to check if a project has a specific nature.
 * 
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 */
public class HasNature extends ProjectComponent implements Condition {

  /** the project delegate */
  private final ProjectDelegate _projectDelegate;

  /** Comment for <code>_nature</code> */
  private String                _nature;

  /**
   * Creates a new instance of type HasNature.
   */
  public HasNature() {
    Ant4EclipseConfiguration.configureAnt4Eclipse(getProject());
    this._projectDelegate = new ProjectDelegate(this);
  }

  /**
   * {@inheritDoc}
   */
  public boolean eval() throws BuildException {
    this._projectDelegate.requireWorkspaceAndProjectNameOrProjectSet();
    requireNatureSet();
    try {
      final EclipseProject project = this._projectDelegate.getEclipseProject();
      boolean result = project.hasNature(this._nature);
      // TODO: NICKNAME-HANDLING!!
      // if (!result) {
      // if ("java".equals(this._nature)) {
      // result = project.hasNature(JavaProjectRole.JAVA_NATURE);
      // } else if ("c".equals(this._nature)) {
      // result = project.hasNature(CProjectRole.C_NATURE);
      // } else if ("c++".equals(this._nature)) {
      // result = project.hasNature(CProjectRole.CC_NATURE);
      // } else if ("python".equals(this._nature)) {
      // result = project.hasNature(PythonProjectRole.PYTHON_NATURE);
      // }
      // }
      return (result);
    } catch (final BuildException e) {
      throw e;
    } catch (final Exception e) {
      throw new BuildException(e.getMessage(), e);
    }
  }

  /**
   * Sets the nature to check for.
   * 
   * @param nature
   *          the nature to set.
   */
  public void setNature(final String nature) {
    this._nature = nature;
  }

  /**
   * Returns <code>true</code> if the nature has been set.
   * 
   * @return <code>true</code> if the nature has been set.
   */
  public boolean isNatureSet() {
    return this._nature != null;
  }

  /**
   * Makes sure the nature attribute has been set. Otherwise throws a BuildException
   */
  public final void requireNatureSet() {
    if (!isNatureSet()) {
      throw new BuildException("Attribute 'nature' has to be set!");
    }
  }

  /**
   * Sets the name of the project.
   * 
   * @param project
   *          the name of the project.
   */
  public void setProjectName(final String project) {
    this._projectDelegate.setProjectName(project);
  }

  /**
   * Sets the workspace.
   * 
   * @param workspace
   *          the workspace.
   */
  public void setWorkspace(final File workspace) {
    this._projectDelegate.setWorkspace(workspace);
  }

  /**
   * Sets the project.
   * 
   * @param project
   *          the project.
   */
  public void setProject(final File project) {
    this._projectDelegate.setProject(project);
  }
}