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
import java.util.List;

/**
 * <p>
 * Delegate class for all tasks, types and conditions that deal with project pathes.
 * </p>
 * 
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 */
public class GetPathDelegate extends PathDelegate implements GetPathComponent {

  private String       _pathId   = null;
  private String       _property = null;
  private boolean      _relative = false;
  private List<File>   _resolvedPath;

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
  public void setPathId( String id ) {
    if( _pathId == null ) {
      _pathId = id;
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getPathId() {
    return _pathId;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isPathIdSet() {
    return _pathId != null;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isRelative() {
    return _relative;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setRelative( boolean relative ) {
    _relative = relative;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setProperty( String property ) {
    _property = property;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getProperty() {
    return _property;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isPropertySet() {
    return _property != null;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void requirePathIdOrPropertySet() {
    if( !isPathIdSet() && !isPropertySet() ) {
      throw new BuildException( "At least one of 'pathId' or 'property' has to be set!" );
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public List<File> getResolvedPath() {
    return _resolvedPath;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setResolvedPath( List<File> resolvedPath ) {
    _resolvedPath = resolvedPath;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void populateProperty() {
    if( isPropertySet() ) {
      String resolvedpath = convertToString( getResolvedPath() );
      getAntProject().setProperty( getProperty(), resolvedpath );
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void populatePathId() {
    if( isPathIdSet() ) {
      Path resolvedPath = convertToPath( getResolvedPath() );
      getAntProject().addReference( getPathId(), resolvedPath );
    }
  }
  
} /* ENDCLASS */
