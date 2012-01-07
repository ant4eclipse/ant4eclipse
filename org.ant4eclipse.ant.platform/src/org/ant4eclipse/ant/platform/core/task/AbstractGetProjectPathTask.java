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
import java.util.List;

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
    _projectDelegate = new EclipseProjectDelegate( this );
    _getPathDelegate = new GetPathDelegate( this );
  }

  /**
   * <p>
   * Resolves the current path.
   * </p>
   * 
   * @return A list of resolved pathes. Not <code>null</code>.
   */
  protected abstract List<File> resolvePath();

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
    List<File> resolvedPath = resolvePath();
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
  public String getDirSeparator() {
    return _getPathDelegate.getDirSeparator();
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
  public String getPathId() {
    return _getPathDelegate.getPathId();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getPathSeparator() {
    return _getPathDelegate.getPathSeparator();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getProperty() {
    return _getPathDelegate.getProperty();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public List<File> getResolvedPath() {
    return _getPathDelegate.getResolvedPath();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Workspace getWorkspace() {
    return _projectDelegate.getWorkspace();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public File getWorkspaceDirectory() {
    return _projectDelegate.getWorkspaceDirectory();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isDirSeparatorSet() {
    return _getPathDelegate.isDirSeparatorSet();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isPathIdSet() {
    return _getPathDelegate.isPathIdSet();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isPathSeparatorSet() {
    return _getPathDelegate.isPathSeparatorSet();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isProjectNameSet() {
    return _projectDelegate.isProjectNameSet();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isPropertySet() {
    return _getPathDelegate.isPropertySet();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isRelative() {
    return _getPathDelegate.isRelative();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isWorkspaceDirectorySet() {
    return _projectDelegate.isWorkspaceDirectorySet();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void populatePathId() {
    _getPathDelegate.populatePathId();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void populateProperty() {
    _getPathDelegate.populateProperty();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void requirePathIdOrPropertySet() {
    _getPathDelegate.requirePathIdOrPropertySet();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void requireWorkspaceAndProjectNameSet() {
    _projectDelegate.requireWorkspaceAndProjectNameSet();
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
  public void setDirSeparator( String newdirseparator ) {
    _getPathDelegate.setDirSeparator( newdirseparator );
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setPathId( String id ) {
    _getPathDelegate.setPathId( id );
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setPathSeparator( String newpathseparator ) {
    _getPathDelegate.setPathSeparator( newpathseparator );
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setProjectName( String projectName ) {
    _projectDelegate.setProjectName( projectName );
  }

  /**
   * {@inheritDoc}
   */
  @Override
  @SuppressWarnings( "deprecation" )
  @Deprecated
  public void setProject( File projectPath ) {
    _projectDelegate.setProject( projectPath );
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setProperty( String property ) {
    _getPathDelegate.setProperty( property );
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setRelative( boolean relative ) {
    _getPathDelegate.setRelative( relative );
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setResolvedPath( List<File> resolvedPath ) {
    _getPathDelegate.setResolvedPath( resolvedPath );
  }

  /**
   * {@inheritDoc}
   */
  @SuppressWarnings( "deprecation" )
  @Override
  public void setWorkspace( String workspace ) {
    _projectDelegate.setWorkspace( workspace );
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setWorkspaceDirectory( String workspaceDirectory ) {
    _projectDelegate.setWorkspaceDirectory( workspaceDirectory );
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Path convertToPath( File entry ) {
    return _getPathDelegate.convertToPath( entry );
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String convertToString( File entry ) {
    return _getPathDelegate.convertToString( entry );
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Path convertToPath( List<File> entries ) {
    return _getPathDelegate.convertToPath( entries );
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String convertToString( List<File> entries ) {
    return _getPathDelegate.convertToString( entries );
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void ensureRole( Class<? extends ProjectRole> projectRoleClass ) {
    _projectDelegate.ensureRole( projectRoleClass );
  }
  
} /* ENDCLASS */
