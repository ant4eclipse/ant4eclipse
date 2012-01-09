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
package org.ant4eclipse.lib.jdt.internal.tools;

import org.ant4eclipse.lib.jdt.tools.ResolvedClasspath;
import org.ant4eclipse.lib.jdt.tools.ResolvedClasspathEntry;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * Implements the {@link ResolvedClasspath}.
 * </p>
 * 
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 */
public class ResolvedClasspathImpl implements ResolvedClasspath {

  /** the list with all the resolved path entries */
  private List<ResolvedClasspathEntry> _classpath;

  /** the boot class path. Might be null * */
  private ResolvedClasspathEntry       _bootclasspath;

  /**
   * <p>
   * Creates a new instance of type {@link ResolvedClasspathImpl}.
   * </p>
   */
  public ResolvedClasspathImpl() {
    _classpath = new ArrayList<ResolvedClasspathEntry>();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public List<ResolvedClasspathEntry> getClasspath() {
    return _classpath;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public ResolvedClasspathEntry getBootClasspath() {
    return _bootclasspath;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public List<File> getBootClasspathFiles() {
    if( hasBootClasspath() ) {
      return _bootclasspath.getClassPathEntries();
    } else {      
      return new ArrayList<File>();
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean hasBootClasspath() {
    return _bootclasspath != null;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public List<File> getClasspathFiles() {
    return resolveClasspathToFiles( _classpath );
  }

  /**
   * <p>
   * Adds the given class path entry to the class path.
   * </p>
   * 
   * @param resolvedClasspathEntry
   *          the class path entry to add.
   */
  // Assure.notNull( "resolvedClasspathEntry", resolvedClasspathEntry );
  public final void addClasspathEntry( ResolvedClasspathEntry resolvedClasspathEntry ) {
    if( !_classpath.contains( resolvedClasspathEntry ) ) {
      _classpath.add( resolvedClasspathEntry );
    }
  }

  /**
   * <p>
   * Add the boot class path entry. The boot class path entry can only set once.
   * </p>
   * 
   * @param resolvedClasspathEntry
   */
  // Assure.notNull( "resolvedClasspathEntry", resolvedClasspathEntry );
  public final void addBootClasspathEntry( ResolvedClasspathEntry resolvedClasspathEntry ) {
    if( _bootclasspath != null ) {
      // TODO: NLS
      throw new RuntimeException( "FAIL" );
    }
    _bootclasspath = resolvedClasspathEntry;
  }

  /**
   * <p>
   * Helper method that returns a list with all class path entries as files.
   * </p>
   * 
   * @param classpath
   *          the class path
   * @return a list with all class path entries as files.
   */
  private List<File> resolveClasspathToFiles( List<ResolvedClasspathEntry> classpath ) {
    List<File> result = new ArrayList<File>();
    for( Object element : classpath ) {
      ResolvedClasspathEntry resolvedClasspathEntry = (ResolvedClasspathEntry) element;
      List<File> files = resolvedClasspathEntry.getClassPathEntries();
      for( int i = 0; i < files.size(); i++ ) {
        if( !result.contains( files.get(i) ) ) {
          result.add( files.get(i) );
        }
      }
    }
    return result;
  }
  
} /* ENDCLASS */
