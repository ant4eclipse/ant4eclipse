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

import java.io.File;

import org.ant4eclipse.core.ant.AbstractAnt4EclipseCondition;
import org.ant4eclipse.platform.ant.core.delegate.EclipseProjectDelegate;
import org.ant4eclipse.platform.model.resource.EclipseProject;
import org.apache.tools.ant.BuildException;

/**
 * An ant condition that allows to check if a project has a specific nature.
 * 
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 */
public class HasNature extends AbstractAnt4EclipseCondition {

  /** the project delegate */
  private final EclipseProjectDelegate _projectDelegate;

  /** Comment for <code>_nature</code> */
  private String                _nature;

  /**
   * Creates a new instance of type HasNature.
   */
  public HasNature() {
    this._projectDelegate = new EclipseProjectDelegate(this);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean doEval() throws BuildException {
    this._projectDelegate.requireWorkspaceAndProjectNameSet();
    requireNatureSet();
    try {
      final EclipseProject project = this._projectDelegate.getEclipseProject();
      return project.hasNature(this._nature);
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
   * @deprecated
   */
  @Deprecated
  public void setWorkspace(final File workspace) {
    this._projectDelegate.setWorkspaceDirectory(workspace);
  }

  public final void setWorkspaceDirectory(File workspaceDirectory) {
    this._projectDelegate.setWorkspaceDirectory(workspaceDirectory);
  }
}