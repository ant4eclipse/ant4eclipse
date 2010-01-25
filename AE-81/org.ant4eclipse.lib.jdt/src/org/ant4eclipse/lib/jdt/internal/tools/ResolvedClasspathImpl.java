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

import org.ant4eclipse.lib.core.Assure;
import org.ant4eclipse.lib.jdt.tools.ResolvedClasspath;
import org.ant4eclipse.lib.jdt.tools.ResolvedClasspathEntry;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

/**
 * <p>
 * Implements the {@link ResolvedClasspath}.
 * </p>
 * 
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 */
public final class ResolvedClasspathImpl implements ResolvedClasspath {

  /** the list with all the resolved path entries */
  private List<ResolvedClasspathEntry> _classpath;

  /** - * */
  private ResolvedClasspathEntry       _bootclasspath;

  /**
   * <p>
   * Creates a new instance of type {@link ResolvedClasspathImpl}.
   * </p>
   */
  public ResolvedClasspathImpl() {
    this._classpath = new LinkedList<ResolvedClasspathEntry>();
  }

  /**
   * {@inheritDoc}
   */
  public final ResolvedClasspathEntry[] getClasspath() {
    return this._classpath.toArray(new ResolvedClasspathEntry[0]);
  }

  /**
   * {@inheritDoc}
   */
  public final ResolvedClasspathEntry getBootClasspath() {
    return this._bootclasspath;
  }

  /**
   * {@inheritDoc}
   */
  public File[] getBootClasspathFiles() {
    return this._bootclasspath.getClassPathEntries();
  }

  /**
   * {@inheritDoc}
   */
  public File[] getClasspathFiles() {
    return resolveClasspathToFiles(this._classpath);
  }

  /**
   * <p>
   * Adds the given class path entry to the class path.
   * </p>
   * 
   * @param resolvedClasspathEntry
   *          the class path entry to add.
   */
  public final void addClasspathEntry(ResolvedClasspathEntry resolvedClasspathEntry) {
    Assure.notNull("resolvedClasspathEntry", resolvedClasspathEntry);
    if (!this._classpath.contains(resolvedClasspathEntry)) {
      this._classpath.add(resolvedClasspathEntry);
    }
  }

  /**
   * <p>
   * Add the boot class path entry. The boot class path entry can only set once.
   * </p>
   * 
   * @param resolvedClasspathEntry
   */
  public final void addBootClasspathEntry(ResolvedClasspathEntry resolvedClasspathEntry) {
    Assure.notNull("resolvedClasspathEntry", resolvedClasspathEntry);
    if (this._bootclasspath != null) {
      // TODO: NLS
      throw new RuntimeException("FAIL");
    }
    this._bootclasspath = resolvedClasspathEntry;
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
  private File[] resolveClasspathToFiles(List<ResolvedClasspathEntry> classpath) {

    // create result
    List<File> result = new LinkedList<File>();

    // add all files
    for (Object element : classpath) {
      ResolvedClasspathEntry resolvedClasspathEntry = (ResolvedClasspathEntry) element;
      File[] files = resolvedClasspathEntry.getClassPathEntries();
      for (int i = 0; i < files.length; i++) {
        if (!result.contains(files[i])) {
          result.add(files[i]);
        }
      }
    }

    // return result
    return result.toArray(new File[0]);
  }
}
