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

import org.ant4eclipse.ant.platform.core.PathComponent;
import org.ant4eclipse.ant.platform.core.delegate.PathDelegate;
import org.apache.tools.ant.types.Path;

import java.io.File;
import java.util.List;

/**
 * <p>
 * Abstract base class for project set based tasks that are able to resolve pathes.
 * </p>
 * 
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 */
public abstract class AbstractProjectSetPathBasedTask extends AbstractProjectSetBasedTask implements PathComponent {

  /** the path delegate */
  private PathDelegate _pathDelegate;

  /**
   * <p>
   * Creates a new instance of type {@link AbstractProjectSetPathBasedTask}.
   * </p>
   */
  public AbstractProjectSetPathBasedTask() {
    super();

    // create the path delegate
    _pathDelegate = new PathDelegate( this );
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public final String getDirSeparator() {
    return _pathDelegate.getDirSeparator();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public final String getPathSeparator() {
    return _pathDelegate.getPathSeparator();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public final boolean isDirSeparatorSet() {
    return _pathDelegate.isDirSeparatorSet();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public final boolean isPathSeparatorSet() {
    return _pathDelegate.isPathSeparatorSet();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public final void setDirSeparator( String newdirseparator ) {
    _pathDelegate.setDirSeparator( newdirseparator );
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public final void setPathSeparator( String newpathseparator ) {
    _pathDelegate.setPathSeparator( newpathseparator );
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public final Path convertToPath( List<File> entries ) {
    return _pathDelegate.convertToPath( entries );
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public final String convertToString( List<File> entries ) {
    return _pathDelegate.convertToString( entries );
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public final Path convertToPath( File entry ) {
    return _pathDelegate.convertToPath( entry );
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public final String convertToString( File entry ) {
    return _pathDelegate.convertToString( entry );
  }

  /**
   * {@inheritDoc}
   */
  public PathDelegate getPathDelegate() {
    return _pathDelegate;
  }
  
} /* ENDCLASS */
