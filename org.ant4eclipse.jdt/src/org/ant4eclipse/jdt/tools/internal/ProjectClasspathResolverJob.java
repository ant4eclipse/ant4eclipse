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
package org.ant4eclipse.jdt.tools.internal;

import java.io.File;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;

import org.ant4eclipse.jdt.tools.ResolvedClasspath;
import org.ant4eclipse.jdt.tools.ResolvedClasspathEntry;
import org.ant4eclipse.platform.model.resource.EclipseProject;
import org.ant4eclipse.platform.model.resource.Workspace;


/**
 * ProjectClasspathResolverJob --
 */
public class ProjectClasspathResolverJob implements ResolvedClasspath {

  /** the root eclipse project */
  private final EclipseProject _rootProject;

  /** the workspace that contains all projects */
  private final Workspace      _workspace;

  /** indicates whether the class path should be resolved relative or absolute */
  private final boolean        _relative;

  /** indicates whether the class path is a runtime class path or not */
  private final boolean        _runtimeClasspath;

  private final Properties     _properties;

  /** the list with all the resolved path entries */
  private final List           _classpath;       /* ResolvedClasspathEntry */

  private final List           _bootclasspath;   /* ResolvedClasspathEntry */

  /**
   * @param rootProject
   * @param workspace
   * @param relative
   * @param runtime
   */
  public ProjectClasspathResolverJob(final EclipseProject rootProject, final Workspace workspace,
      final boolean relative, final boolean runtime, final Properties properties) {

    this._rootProject = rootProject;
    this._workspace = workspace;
    this._relative = relative;
    this._runtimeClasspath = runtime;
    this._properties = properties;

    this._classpath = new LinkedList();
    this._bootclasspath = new LinkedList();
  }

  /**
   * @return the rootProject
   */
  public final EclipseProject getRootProject() {
    return this._rootProject;
  }

  /**
   * @return the workspace
   */
  public final Workspace getWorkspace() {
    return this._workspace;
  }

  /**
   * @return the relative
   */
  public final boolean isRelative() {
    return this._relative;
  }

  /**
   * @return the runtimeClasspath
   */
  public final boolean isRuntimeClasspath() {
    return this._runtimeClasspath;
  }

  /**
   * @return the class path
   */
  public final ResolvedClasspathEntry[] getClasspath() {
    return (ResolvedClasspathEntry[]) this._classpath.toArray(new ResolvedClasspathEntry[0]);
  }

  /**
   * @return the boot class path
   */
  public final ResolvedClasspathEntry[] getBootClasspath() {
    return (ResolvedClasspathEntry[]) this._bootclasspath.toArray(new ResolvedClasspathEntry[0]);
  }

  /**
   * @see org.ant4eclipse.jdt.tools.ResolvedClasspath#getBootClasspathFiles()
   */
  public File[] getBootClasspathFiles() {
    return resolveClasspathToFiles(this._bootclasspath);
  }

  /**
   * @see org.ant4eclipse.jdt.tools.ResolvedClasspath#getClasspathFiles()
   */
  public File[] getClasspathFiles() {
    return resolveClasspathToFiles(this._classpath);
  }

  /**
   * @return the properties
   */
  public final Properties getProperties() {
    return this._properties;
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
    if (!this._bootclasspath.contains(resolvedClasspathEntry)) {
      this._bootclasspath.add(resolvedClasspathEntry);
    }
  }

  private File[] resolveClasspathToFiles(final List classpath) {

    final List result = new LinkedList();

    for (final Iterator iterator = classpath.iterator(); iterator.hasNext();) {
      final ResolvedClasspathEntry resolvedClasspathEntry = (ResolvedClasspathEntry) iterator.next();

      final File[] files = resolvedClasspathEntry.getEntries();

      for (int i = 0; i < files.length; i++) {
        if (!result.contains(files[i])) {
          result.add(files[i]);
        }
      }
    }

    return (File[]) result.toArray(new File[0]);
  }
}
