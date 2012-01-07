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


import org.ant4eclipse.lib.jdt.tools.container.JdtClasspathContainerArgument;
import org.ant4eclipse.lib.platform.model.resource.EclipseProject;
import org.ant4eclipse.lib.platform.model.resource.Workspace;

import java.util.List;

/**
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 */
public class ResolverJob {

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
  public ResolverJob( EclipseProject rootProject, Workspace workspace, boolean relative, boolean runtime,
      List<JdtClasspathContainerArgument> classpathContainerArguments ) {

    _rootProject = rootProject;
    _workspace = workspace;
    _relative = relative;
    _runtimeClasspath = runtime;
    _classpathContainerArguments = classpathContainerArguments;
  }

  /**
   * @return the rootProject
   */
  public final EclipseProject getRootProject() {
    return _rootProject;
  }

  /**
   * @return the workspace
   */
  public final Workspace getWorkspace() {
    return _workspace;
  }

  /**
   * @return the relative
   */
  public final boolean isRelative() {
    return _relative;
  }

  /**
   * @return the runtimeClasspath
   */
  public final boolean isRuntimeClasspath() {
    return _runtimeClasspath;
  }

  /**
   * @return the properties
   */
  public final List<JdtClasspathContainerArgument> getJdtClasspathContainerArguments() {
    return _classpathContainerArguments;
  }
  
} /* ENDCLASS */
