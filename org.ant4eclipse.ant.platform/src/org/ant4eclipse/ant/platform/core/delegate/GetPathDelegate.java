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
package org.ant4eclipse.ant.platform.core.delegate;


import org.ant4eclipse.ant.platform.core.GetPathComponent;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.ProjectComponent;
import org.apache.tools.ant.types.Path;

import java.io.File;

/**
 * <p>
 * Delegate class for all tasks, types and conditions that deal with project pathes.
 * </p>
 * 
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 */
public class GetPathDelegate extends PathDelegate implements GetPathComponent {

  /** the id of the path */
  private String  _pathId   = null;

  /** the property */
  private String  _property = null;

  /** indicates whether the class path should be resolved relative or not */
  private boolean _relative = false;

  /** the resolved path entries */
  private File[]  _resolvedPath;

  /**
   * @param component
   */
  public GetPathDelegate( ProjectComponent component ) {
    super( component );
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public final void setPathId( String id ) {
    if( this._pathId == null ) {
      this._pathId = id;
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public final String getPathId() {
    return this._pathId;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public final boolean isPathIdSet() {
    return this._pathId != null;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public final boolean isRelative() {
    return this._relative;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public final void setRelative( boolean relative ) {
    this._relative = relative;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public final void setProperty( String property ) {
    this._property = property;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public final String getProperty() {
    return this._property;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public final boolean isPropertySet() {
    return this._property != null;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public final void requirePathIdOrPropertySet() {
    if( !isPathIdSet() && !isPropertySet() ) {
      throw new BuildException( "At least one of 'pathId' or 'property' has to be set!" );
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public final File[] getResolvedPath() {
    return this._resolvedPath;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public final void setResolvedPath( File[] resolvedPath ) {
    this._resolvedPath = resolvedPath;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public final void populateProperty() {
    if( isPropertySet() ) {
      String resolvedpath = convertToString( getResolvedPath() );
      getAntProject().setProperty( getProperty(), resolvedpath );
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public final void populatePathId() {
    if( isPathIdSet() ) {
      Path resolvedPath = convertToPath( getResolvedPath() );
      getAntProject().addReference( getPathId(), resolvedPath );
    }
  }
  
} /* ENDCLASS */
