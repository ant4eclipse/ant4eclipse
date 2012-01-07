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

import org.ant4eclipse.ant.core.delegate.AbstractAntDelegate;
import org.ant4eclipse.ant.platform.core.PathComponent;
import org.ant4eclipse.lib.core.Assure;
import org.ant4eclipse.lib.core.util.Utilities;
import org.apache.tools.ant.ProjectComponent;
import org.apache.tools.ant.types.Path;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

/**
 * <p>
 * Delegate class for all tasks, types and conditions that deal with pathes.
 * </p>
 * 
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 */
public class PathDelegate extends AbstractAntDelegate implements PathComponent {

  /** the path separator (e.g. ':' or ';') */
  private String _pathSeparator;

  /** the directory separator (e.g. '/' or '\' */
  private String _dirSeparator;

  /**
   * <p>
   * Creates a new instance of type {@link PathDelegate}.
   * </p>
   * 
   * @param component
   *          the ProjectComponent
   */
  public PathDelegate( ProjectComponent component ) {
    super( component );

    // set default separators
    _pathSeparator = File.pathSeparator;
    _dirSeparator = File.separator;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setPathSeparator( String newpathseparator ) {
    Assure.nonEmpty( "newpathseparator", newpathseparator );
    _pathSeparator = newpathseparator;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getPathSeparator() {
    return _pathSeparator;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isPathSeparatorSet() {
    return _pathSeparator != null;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setDirSeparator( String newdirseparator ) {
    Assure.nonEmpty( "newdirseparator", newdirseparator );
    _dirSeparator = newdirseparator;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getDirSeparator() {
    return _dirSeparator;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isDirSeparatorSet() {
    return _dirSeparator != null;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String convertToString( File entry ) {
    return convertToString( Arrays.asList( entry ) );
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String convertToString( List<File> entries ) {
    Assure.notNull( "entries", entries );
    // convert Files to String
    List<String> entriesAsString = new ArrayList<String>();
    for( File entry : entries ) {
      String path = entry.getPath();
      if( !entriesAsString.contains( path ) ) {
        entriesAsString.add( path );
      }
    }

    // replace path and directory separator
    StringBuilder buffer = new StringBuilder();
    Iterator<String> iterator = entriesAsString.iterator();
    while( iterator.hasNext() ) {
      String path = iterator.next().replace( '\\', '/' );
      path = Utilities.replace( path, '/', _dirSeparator );
      buffer.append( path );
      if( iterator.hasNext() ) {
        buffer.append( _pathSeparator );
      }
    }

    // return result
    return buffer.toString();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Path convertToPath( File entry ) {
    return convertToPath( Arrays.asList( entry ) );
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Path convertToPath( List<File> entries ) {
    Assure.notNull( "entries", entries );
    Path antPath = new Path( getAntProject() );
    for( File entry : entries ) {
      // TODO getPath() vs. getAbsolutePath()
      antPath.append( new Path( getAntProject(), entry.getPath() ) );
    }
    return antPath;
  }
  
} /* ENDCLASS */
