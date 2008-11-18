/**********************************************************************
 * Copyright (c) 2005-2008 ant4eclipse project team.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Nils Hartmann, Daniel Kasmeroglu, Gerd Wuetherich
 **********************************************************************/
package org.ant4eclipse.jdt.tools;

import java.util.Properties;

import org.ant4eclipse.core.Assert;
import org.ant4eclipse.jdt.tools.internal.ProjectClasspathResolver;
import org.ant4eclipse.jdt.tools.internal.ProjectClasspathResolverJob;
import org.ant4eclipse.platform.model.resource.EclipseProject;


/**
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 */
public class ClasspathResolver {

  /**
   * <p>
   * Resolves the class path of the given eclipse project.
   * </p>
   * 
   * @param project
   *          the eclipse project that should be resolved
   * @param resolveRelative
   *          indicates if the class path should be resolved relative to the workspace or not.
   * @param isRuntimeClasspath
   *          indicates if the class path is a runtime class path or not
   * @return the resolved class path
   */
  public static final ResolvedClasspath resolveProjectClasspath(final EclipseProject project,
      final boolean resolveRelative, final boolean isRuntimeClasspath, final Properties properties) {
    Assert.notNull(project);

    // create a ProjectClasspathResolverJob
    final ProjectClasspathResolverJob job = new ProjectClasspathResolverJob(project, project.getWorkspace(),
        resolveRelative, isRuntimeClasspath, properties);

    // execute the job
    new ProjectClasspathResolver().resolveProjectClasspath(job);

    // return the result (which must be if type ResolvedClasspath)
    return job;
  }

  // public static final File[] resolveRuntimeClasspath(final Workspace workspace,
  // final AbstractLaunchConfiguration launchConfiguration, final boolean resolveRelative) {
  //
  // final ProjectClasspathResolver classpathResolver = new ProjectClasspathResolver(workspace, resolveRelative,
  // true);
  //
  // classpathResolver.resolveLaunchConfigurationClasspath(launchConfiguration);
  //
  // return classpathResolver.getResolvedPathEntries();
  // }
}