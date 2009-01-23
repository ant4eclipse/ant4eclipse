/**********************************************************************
 * Copyright (c) 2005-2007 ant4eclipse project team.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Nils Hartmann, Daniel Kasmeroglu, Gerd Wuetherich
 **********************************************************************/
package org.ant4eclipse.jdt.internal.tools;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

import org.ant4eclipse.jdt.tools.ResolvedClasspath;
import org.ant4eclipse.jdt.tools.ResolvedClasspathEntry;

/**
 * ProjectClasspathResolverJob --
 */
public final class ResolvedClasspathImpl implements ResolvedClasspath {

  /** the list with all the resolved path entries */
  private final List<ResolvedClasspathEntry> _classpath;

  /** - * */
  private ResolvedClasspathEntry             _bootclasspath;

  /**
   */
  public ResolvedClasspathImpl() {
    this._classpath = new LinkedList<ResolvedClasspathEntry>();
  }

  /**
   * @return the class path
   */
  public final ResolvedClasspathEntry[] getClasspath() {
    return this._classpath.toArray(new ResolvedClasspathEntry[0]);
  }

  /**
   * @return the boot class path
   */
  public final ResolvedClasspathEntry getBootClasspath() {
    return this._bootclasspath;
  }

  /**
   * @see org.ant4eclipse.jdt.tools.ResolvedClasspath#getBootClasspathFiles()
   */
  public File[] getBootClasspathFiles() {
    return this._bootclasspath.getEntries();
  }

  /**
   * @see org.ant4eclipse.jdt.tools.ResolvedClasspath#getClasspathFiles()
   */
  public File[] getClasspathFiles() {
    return resolveClasspathToFiles(this._classpath);
  }

  /**
   * @param resolvedClasspathEntry
   */
  public final void addClasspathEntry(final ResolvedClasspathEntry resolvedClasspathEntry) {
    if (!this._classpath.contains(resolvedClasspathEntry)) {
      this._classpath.add(resolvedClasspathEntry);
    }
  }

  /**
   * @param resolvedClasspathEntry
   */
  public final void addBootClasspathEntry(final ResolvedClasspathEntry resolvedClasspathEntry) {
    if (this._bootclasspath != null) {
      // TODO
      throw new RuntimeException("FAIL");
    }

    this._bootclasspath = resolvedClasspathEntry;
  }

  private File[] resolveClasspathToFiles(final List<ResolvedClasspathEntry> classpath) {

    final List<File> result = new LinkedList<File>();

    for (final Object element : classpath) {
      final ResolvedClasspathEntry resolvedClasspathEntry = (ResolvedClasspathEntry) element;

      final File[] files = resolvedClasspathEntry.getEntries();

      for (int i = 0; i < files.length; i++) {
        if (!result.contains(files[i])) {
          result.add(files[i]);
        }
      }
    }

    return result.toArray(new File[0]);
  }
}
