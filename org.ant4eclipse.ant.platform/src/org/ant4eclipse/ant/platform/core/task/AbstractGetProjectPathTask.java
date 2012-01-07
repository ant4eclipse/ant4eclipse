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
package org.ant4eclipse.ant.platform.core.task;

import org.ant4eclipse.ant.core.AbstractAnt4EclipseTask;
import org.ant4eclipse.ant.platform.core.EclipseProjectComponent;
import org.ant4eclipse.ant.platform.core.GetPathComponent;
import org.ant4eclipse.ant.platform.core.delegate.EclipseProjectDelegate;
import org.ant4eclipse.ant.platform.core.delegate.GetPathDelegate;
import org.ant4eclipse.lib.platform.model.resource.EclipseProject;
import org.ant4eclipse.lib.platform.model.resource.Workspace;
import org.ant4eclipse.lib.platform.model.resource.role.ProjectRole;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.types.Path;

import java.io.File;

/**
 * <p>
 * Abstract base class for all tasks that resolve paths from an eclipse project.
 * </p>
 * 
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 */
public abstract class AbstractGetProjectPathTask extends AbstractAnt4EclipseTask implements GetPathComponent,
    EclipseProjectComponent {

  /** the project delegate */
  private EclipseProjectDelegate _projectDelegate;

  /** the get path delegate */
  private GetPathDelegate        _getPathDelegate;

  /**
   * <p>
   * Create a new instance of type {@link AbstractGetProjectPathTask}.
   * </p>
   */
  public AbstractGetProjectPathTask() {
    super();

    // create the delegates
    this._projectDelegate = new EclipseProjectDelegate( this );
    this._getPathDelegate = new GetPathDelegate( this );
  }

  /**
   * <p>
   * Resolves the current path.
   * </p>
   * 
   * @return A list of resolved pathes. Not <code>null</code>.
   */
  protected abstract File[] resolvePath();

  /**
   * {@inheritDoc}
   */
  @Override
  protected void preconditions() throws BuildException {
    super.preconditions();
    // check requires attributes
    requireWorkspaceAndProjectNameSet();
    requirePathIdOrPropertySet();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void doExecute() {

    // resolve path
    File[] resolvedPath = resolvePath();
    setResolvedPath( resolvedPath );

    // set path
    if( isPathIdSet() ) {
      populatePathId();
    }

    // set property
    if( isPropertySet() ) {
      populateProperty();
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public final String getDirSeparator() {
    return this._getPathDelegate.getDirSeparator();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public EclipseProject getEclipseProject() throws BuildException {
    return this._projectDelegate.getEclipseProject();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public final String getPathId() {
    return this._getPathDelegate.getPathId();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public final String getPathSeparator() {
    return this._getPathDelegate.getPathSeparator();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public final String getProperty() {
    return this._getPathDelegate.getProperty();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public final File[] getResolvedPath() {
    return this._getPathDelegate.getResolvedPath();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public final Workspace getWorkspace() {
    return this._projectDelegate.getWorkspace();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public final File getWorkspaceDirectory() {
    return this._projectDelegate.getWorkspaceDirectory();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public final boolean isDirSeparatorSet() {
    return this._getPathDelegate.isDirSeparatorSet();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public final boolean isPathIdSet() {
    return this._getPathDelegate.isPathIdSet();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public final boolean isPathSeparatorSet() {
    return this._getPathDelegate.isPathSeparatorSet();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public final boolean isProjectNameSet() {
    return this._projectDelegate.isProjectNameSet();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public final boolean isPropertySet() {
    return this._getPathDelegate.isPropertySet();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public final boolean isRelative() {
    return this._getPathDelegate.isRelative();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public final boolean isWorkspaceDirectorySet() {
    return this._projectDelegate.isWorkspaceDirectorySet();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public final void populatePathId() {
    this._getPathDelegate.populatePathId();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public final void populateProperty() {
    this._getPathDelegate.populateProperty();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public final void requirePathIdOrPropertySet() {
    this._getPathDelegate.requirePathIdOrPropertySet();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public final void requireWorkspaceAndProjectNameSet() {
    this._projectDelegate.requireWorkspaceAndProjectNameSet();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getWorkspaceId() {
    return this._projectDelegate.getWorkspaceId();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isWorkspaceIdSet() {
    return this._projectDelegate.isWorkspaceIdSet();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void requireWorkspaceDirectoryOrWorkspaceIdSet() {
    this._projectDelegate.requireWorkspaceDirectoryOrWorkspaceIdSet();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setWorkspaceId( String identifier ) {
    this._projectDelegate.setWorkspaceId( identifier );
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public final void setDirSeparator( String newdirseparator ) {
    this._getPathDelegate.setDirSeparator( newdirseparator );
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public final void setPathId( String id ) {
    this._getPathDelegate.setPathId( id );
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public final void setPathSeparator( String newpathseparator ) {
    this._getPathDelegate.setPathSeparator( newpathseparator );
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public final void setProjectName( String projectName ) {
    this._projectDelegate.setProjectName( projectName );
  }

  /**
   * {@inheritDoc}
   */
  @Override
  @SuppressWarnings( "deprecation" )
  @Deprecated
  public void setProject( File projectPath ) {
    this._projectDelegate.setProject( projectPath );
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public final void setProperty( String property ) {
    this._getPathDelegate.setProperty( property );
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public final void setRelative( boolean relative ) {
    this._getPathDelegate.setRelative( relative );
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public final void setResolvedPath( File[] resolvedPath ) {
    this._getPathDelegate.setResolvedPath( resolvedPath );
  }

  /**
   * {@inheritDoc}
   */
  @SuppressWarnings( "deprecation" )
  @Override
  public final void setWorkspace( String workspace ) {
    this._projectDelegate.setWorkspace( workspace );
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public final void setWorkspaceDirectory( String workspaceDirectory ) {
    this._projectDelegate.setWorkspaceDirectory( workspaceDirectory );
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public final Path convertToPath( File entry ) {
    return this._getPathDelegate.convertToPath( entry );
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public final String convertToString( File entry ) {
    return this._getPathDelegate.convertToString( entry );
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public final Path convertToPath( File[] entries ) {
    return this._getPathDelegate.convertToPath( entries );
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public final String convertToString( File[] entries ) {
    return this._getPathDelegate.convertToString( entries );
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public final void ensureRole( Class<? extends ProjectRole> projectRoleClass ) {
    this._projectDelegate.ensureRole( projectRoleClass );
  }
  
} /* ENDCLASS */
