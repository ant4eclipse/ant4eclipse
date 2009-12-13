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
package org.ant4eclipse.jdt.internal.tools;

import org.ant4eclipse.jdt.tools.container.JdtClasspathContainerArgument;

import org.ant4eclipse.lib.platform.model.resource.EclipseProject;
import org.ant4eclipse.lib.platform.model.resource.Workspace;

import java.util.List;

/**
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 */
public final class ResolverJob {

  /** the root eclipse project */
  private EclipseProject                      _rootProject;

  /** the workspace that contains all projects */
  private Workspace                           _workspace;

  /** indicates whether the class path should be resolved relative or absolute */
  private boolean                             _relative;

  /** indicates whether the class path is a runtime class path or not */
  private boolean                             _runtimeClasspath;

  /** - */
  private List<JdtClasspathContainerArgument> _classpathContainerArguments;

  /**
   * @param rootProject
   * @param workspace
   * @param relative
   * @param runtime
   */
  public ResolverJob(EclipseProject rootProject, Workspace workspace, boolean relative, boolean runtime,
      List<JdtClasspathContainerArgument> classpathContainerArguments) {

    this._rootProject = rootProject;
    this._workspace = workspace;
    this._relative = relative;
    this._runtimeClasspath = runtime;
    this._classpathContainerArguments = classpathContainerArguments;
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
   * @return the properties
   */
  public final List<JdtClasspathContainerArgument> getJdtClasspathContainerArguments() {
    return this._classpathContainerArguments;
  }
}
