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
package org.ant4eclipse.ant.platform.core.condition;

import org.ant4eclipse.ant.core.AbstractAnt4EclipseCondition;
import org.ant4eclipse.ant.platform.core.EclipseProjectComponent;
import org.ant4eclipse.ant.platform.core.delegate.EclipseProjectDelegate;
import org.ant4eclipse.lib.platform.model.resource.EclipseProject;
import org.ant4eclipse.lib.platform.model.resource.Workspace;
import org.ant4eclipse.lib.platform.model.resource.role.ProjectRole;
import org.apache.tools.ant.BuildException;

import java.io.File;

/**
 * <p>
 * Abstract condition for all project based conditions.
 * </p>
 * 
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 */
public abstract class AbstractProjectBasedCondition extends AbstractAnt4EclipseCondition implements
    EclipseProjectComponent {

  /** the project delegate */
  private EclipseProjectDelegate _projectDelegate;

  /**
   * <p>
   * Creates a new instance of type {@link AbstractProjectBasedCondition}.
   * </p>
   */
  public AbstractProjectBasedCondition() {
    _projectDelegate = new EclipseProjectDelegate( this );
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setProjectName( String project ) {
    _projectDelegate.setProjectName( project );
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public final void setWorkspaceDirectory( String workspaceDirectory ) {
    _projectDelegate.setWorkspaceDirectory( workspaceDirectory );
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void ensureRole( Class<? extends ProjectRole> projectRoleClass ) {
    _projectDelegate.ensureRole( projectRoleClass );
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public EclipseProject getEclipseProject() throws BuildException {
    return _projectDelegate.getEclipseProject();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public final Workspace getWorkspace() {
    return _projectDelegate.getWorkspace();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public final File getWorkspaceDirectory() {
    return _projectDelegate.getWorkspaceDirectory();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public final boolean isProjectNameSet() {
    return _projectDelegate.isProjectNameSet();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public final boolean isWorkspaceDirectorySet() {
    return _projectDelegate.isWorkspaceDirectorySet();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getWorkspaceId() {
    return _projectDelegate.getWorkspaceId();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isWorkspaceIdSet() {
    return _projectDelegate.isWorkspaceIdSet();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void requireWorkspaceDirectoryOrWorkspaceIdSet() {
    _projectDelegate.requireWorkspaceDirectoryOrWorkspaceIdSet();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setWorkspaceId( String identifier ) {
    _projectDelegate.setWorkspaceId( identifier );
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public final void requireWorkspaceAndProjectNameSet() {
    _projectDelegate.requireWorkspaceAndProjectNameSet();
  }

} /* ENDCLASS */
