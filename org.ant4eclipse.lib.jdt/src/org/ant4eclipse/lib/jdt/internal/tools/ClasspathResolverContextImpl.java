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

import org.ant4eclipse.core.Assert;


import org.ant4eclipse.platform.model.resource.EclipseProject;
import org.ant4eclipse.platform.model.resource.Workspace;

import org.ant4eclipse.lib.jdt.tools.ResolvedClasspathEntry;
import org.ant4eclipse.lib.jdt.tools.container.ClasspathResolverContext;
import org.ant4eclipse.lib.jdt.tools.container.JdtClasspathContainerArgument;

import java.util.EmptyStackException;
import java.util.List;

/**
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 */
public class ClasspathResolverContextImpl implements ClasspathResolverContext {

  private ClasspathEntryResolverExecutor _executor;

  private ResolverJob                    _resolverJob;

  private ResolvedClasspathImpl          _resolvedClasspath;

  public ClasspathResolverContextImpl(ClasspathEntryResolverExecutor executor, ResolverJob resolverJob,
      ResolvedClasspathImpl resolvedClasspath) {
    this._executor = executor;
    this._resolverJob = resolverJob;
    this._resolvedClasspath = resolvedClasspath;
  }

  public ClasspathResolverContextImpl(ClasspathEntryResolverExecutor executor, ResolverJob resolverJob) {
    this(executor, resolverJob, null);
  }

  /**
   * {@inheritDoc}
   */
  public EclipseProject getCurrentProject() {
    try {
      return this._executor.getCurrentProject();
    } catch (EmptyStackException e) {
      return null;
    }
  }

  /**
   * {@inheritDoc}
   */
  public boolean hasCurrentProject() {
    return this._executor.hasCurrentProject();
  }

  /**
   * {@inheritDoc}
   */
  public void resolveProjectClasspath(EclipseProject project) {
    this._executor.resolveReferencedProject(project);
  }

  /**
   * {@inheritDoc}
   */
  public final Workspace getWorkspace() {
    return this._resolverJob.getWorkspace();
  }

  /**
   * {@inheritDoc}
   */
  public final boolean isCurrentProjectRoot() {
    return this._resolverJob.getRootProject().equals(getCurrentProject());
  }

  /**
   * {@inheritDoc}
   */
  public final boolean isRuntime() {
    return this._resolverJob.isRuntimeClasspath();
  }

  /**
   * {@inheritDoc}
   */
  public final List<JdtClasspathContainerArgument> getJdtClasspathContainerArguments() {
    return this._resolverJob.getJdtClasspathContainerArguments();
  }

  /**
   * {@inheritDoc}
   */
  public JdtClasspathContainerArgument getJdtClasspathContainerArgument(String key) {
    Assert.nonEmpty(key);

    List<JdtClasspathContainerArgument> arguments = this._resolverJob.getJdtClasspathContainerArguments();

    for (JdtClasspathContainerArgument jdtClasspathContainerArgument : arguments) {
      if (key.equals(jdtClasspathContainerArgument.getKey())) {
        return jdtClasspathContainerArgument;
      }
    }

    return null;
  }

  /**
   * {@inheritDoc}
   */
  public final boolean isWorkspaceRelative() {
    return this._resolverJob.isRelative();
  }

  /**
   * {@inheritDoc}
   */
  public void setBootClasspathEntry(ResolvedClasspathEntry resolvedClasspathEntry) {
    if (this._resolvedClasspath != null) {
      this._resolvedClasspath.addBootClasspathEntry(resolvedClasspathEntry);
    }
  }

  /**
   * {@inheritDoc}
   */
  public void addClasspathEntry(ResolvedClasspathEntry resolvedClasspathEntry) {
    if (this._resolvedClasspath != null) {
      this._resolvedClasspath.addClasspathEntry(resolvedClasspathEntry);
    }
  }

  /**
   * {@inheritDoc}
   */
  public void addReferencedProjects(EclipseProject eclipseProject) {
    this._executor.addReferencedProject(eclipseProject);
  }
}
