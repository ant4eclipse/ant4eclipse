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
package org.ant4eclipse.jdt.tools.internal;

import java.util.EmptyStackException;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;
import java.util.Stack;

import org.ant4eclipse.core.Assert;
import org.ant4eclipse.core.Lifecycle;
import org.ant4eclipse.core.logging.A4ELogging;
import org.ant4eclipse.jdt.model.ClasspathEntry;
import org.ant4eclipse.jdt.model.project.JavaProjectRole;
import org.ant4eclipse.jdt.model.project.RawClasspathEntry;
import org.ant4eclipse.jdt.tools.ResolvedClasspathEntry;
import org.ant4eclipse.jdt.tools.container.ClasspathResolverContext;
import org.ant4eclipse.jdt.tools.internal.classpathentry.ClasspathEntryResolver;
import org.ant4eclipse.jdt.tools.internal.classpathentry.ContainerClasspathEntryResolver;
import org.ant4eclipse.jdt.tools.internal.classpathentry.LibraryClasspathEntryResolver;
import org.ant4eclipse.jdt.tools.internal.classpathentry.OutputClasspathEntryResolver;
import org.ant4eclipse.jdt.tools.internal.classpathentry.ProjectClasspathEntryResolver;
import org.ant4eclipse.jdt.tools.internal.classpathentry.SourceClasspathEntryResolver;
import org.ant4eclipse.jdt.tools.internal.classpathentry.VariableClasspathEntryResolver;
import org.ant4eclipse.platform.model.resource.EclipseProject;
import org.ant4eclipse.platform.model.resource.Workspace;


/**
 * <p>
 * The {@link ProjectClasspathResolver} can be used to resolve the class path of an eclipse project.
 * </p>
 * 
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 */
public class ProjectClasspathResolver {

  /** the project class path resolver job */
  private ProjectClasspathResolverJob    _classpathResolverJob;

  /** array that contains all resolvers for raw class path entries **/
  private final ClasspathEntryResolver[] _classpathEntryResolvers;

  /** stack of 'current projects' */
  private final Stack                    _currentProject;

  /** list with all projects that already have been parsed */
  private final List                     _parsedProjects;

  /**
   * Creates a new instance of type {@link ProjectClasspathResolver}.
   */
  // TODO Visibility
  public ProjectClasspathResolver() {

    this._parsedProjects = new LinkedList();
    this._currentProject = new Stack();

    // TODO BETTER!!!
    this._classpathEntryResolvers = new ClasspathEntryResolver[] { new VariableClasspathEntryResolver(),
        new ContainerClasspathEntryResolver(), new SourceClasspathEntryResolver(), new ProjectClasspathEntryResolver(),
        new LibraryClasspathEntryResolver(), new OutputClasspathEntryResolver() };

  }

  /**
   * @param job
   * @return
   */
  public final ProjectClasspathResolverJob resolveProjectClasspath(final ProjectClasspathResolverJob job) {

    // Initialize the ProjectClasspathResolver instance
    this._classpathResolverJob = job;
    this._parsedProjects.clear();
    this._currentProject.clear();

    // Initialize Entry Resolvers
    for (int i = 0; i < this._classpathEntryResolvers.length; i++) {
      if (this._classpathEntryResolvers[i] instanceof Lifecycle) {
        ((Lifecycle) this._classpathEntryResolvers[i]).initialize();
      }
    }

    // resolve the class path
    resolveProjectClasspath(this._classpathResolverJob.getRootProject());

    // Dispose Entry Resolvers
    for (int i = 0; i < this._classpathEntryResolvers.length; i++) {
      if (this._classpathEntryResolvers[i] instanceof Lifecycle) {
        ((Lifecycle) this._classpathEntryResolvers[i]).initialize();
      }
    }

    // return the job
    return job;
  }

  /**
   * <p>
   * Resolves the class path for a (java-)project.
   * </p>
   * 
   * @param project
   *          the (java-)project which class path should be resolved.
   */
  final void resolveProjectClasspath(final EclipseProject project) {
    Assert.notNull(project);

    if (this._currentProject.contains(project)) {
      // TODO it should be configurable if the task fails on circular dependencies
      // TODO detect which projects reference each other
      A4ELogging.warn("Circular dependency detected! Project: '%s'", new String[] { project.getFolderName() });
      return;
    }

    if (this._parsedProjects.contains(project.getFolderName())) {
      return;
    }

    this._parsedProjects.add(project.getFolderName());

    this._currentProject.push(project);

    final JavaProjectRole javaProjectRole = JavaProjectRole.Helper.getJavaProjectRole(project);
    Assert.assertTrue(javaProjectRole.hasRawClasspathEntries(), "");

    final RawClasspathEntry[] eclipseClasspathEntries = javaProjectRole.getRawClasspathEntries();

    resolveClasspathEntries(eclipseClasspathEntries);

    this._currentProject.pop();
  }

  //
  // /**
  // * <p>
  // * Returns a list of resolved path entries.
  // * </p>
  // *
  // * @return A list of resolved path entries.
  // */
  // public final File[] getResolvedPathEntries() {
  // return (File[]) this._result.toArray(new File[0]);
  // }

  // /**
  // * Resolves the class path for a (java-)project.
  // *
  // * @param project
  // * the (java-)project which class path should be resolved.
  // */
  // public final void resolveLaunchConfigurationClasspath(final AbstractLaunchConfiguration launchConfiguration) {
  // Assert.notNull(launchConfiguration);
  //
  // A4ELogging.debug("resolveRuntimeClasspath(%s, %s, %s)", new Object[] { getWorkspace(), launchConfiguration,
  // Boolean.valueOf(isWorkspaceRelative()) });
  //
  // Assert.notNull(launchConfiguration);
  //
  // final RuntimeClasspathEntry[] entries = launchConfiguration.getClasspathEntries();
  //
  // resolveClasspathEntries(entries);
  // }

  private void resolveClasspathEntries(final ClasspathEntry[] classpathEntries) {
    for (int i = 0; i < classpathEntries.length; i++) {
      try {
        resolveClasspathEntry(classpathEntries[i]);
      } catch (final Exception e) {
        // TODO
        e.printStackTrace();
        throw new RuntimeException("Exception whilst resolving the classpath ", e);
      }
    }
  }

  /**
   * <p>
   * Resolves a eclipse class path entry.
   * </p>
   * 
   * @param entry
   *          the class path entry to resolve.
   */
  private final void resolveClasspathEntry(final ClasspathEntry entry) {
    Assert.notNull(entry);

    // initialize handled
    boolean handled = false;

    // iterate over all the entry resolvers
    for (int i = 0; i < this._classpathEntryResolvers.length; i++) {
      if (this._classpathEntryResolvers[i].canResolve(entry)) {
        handled = true;
        this._classpathEntryResolvers[i].resolve(entry, new ClasspathResolverContextImpl());
        break;
      }
    }

    // if the entry is not handled, we have to throw an exception here
    if (!handled) {
      // TODO
      throw new RuntimeException("Unsupported Entrykind!" + entry);
    }
  }

  /**
   * Helper class to provide access to the {@link ProjectClasspathResolver} and the {@link ProjectClasspathResolverJob}
   * via the {@link ClasspathResolverContext} interface.
   * 
   * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
   */
  private class ClasspathResolverContextImpl implements ClasspathResolverContext {

    /**
     * {@inheritDoc}
     */
    public final void addClasspathEntry(final ResolvedClasspathEntry resolvedClasspathEntry) {
      ProjectClasspathResolver.this._classpathResolverJob.addClasspathEntry(resolvedClasspathEntry);
    }

    /**
     * {@inheritDoc}
     */
    public final void addBootClasspathEntry(final ResolvedClasspathEntry resolvedClasspathEntry) {
      ProjectClasspathResolver.this._classpathResolverJob.addBootClasspathEntry(resolvedClasspathEntry);
    }

    /**
     * {@inheritDoc}
     */
    public final EclipseProject getCurrentProject() {
      try {
        return (EclipseProject) ProjectClasspathResolver.this._currentProject.peek();
      } catch (final EmptyStackException e) {
        return null;
      }
    }

    /**
     * {@inheritDoc}
     */
    public final boolean hasCurrentProject() {
      return !ProjectClasspathResolver.this._currentProject.empty();
    }

    /**
     * {@inheritDoc}
     */
    public final Workspace getWorkspace() {
      return ProjectClasspathResolver.this._classpathResolverJob.getWorkspace();
    }

    /**
     * {@inheritDoc}
     */
    public final boolean isCurrentProjectRoot() {
      return ProjectClasspathResolver.this._classpathResolverJob.getRootProject().equals(getCurrentProject());
    }

    /**
     * {@inheritDoc}
     */
    public final boolean isRuntime() {
      return ProjectClasspathResolver.this._classpathResolverJob.isRuntimeClasspath();
    }

    /**
     * {@inheritDoc}
     */
    public Properties getProperties() {
      return ProjectClasspathResolver.this._classpathResolverJob.getProperties();
    }

    /**
     * {@inheritDoc}
     */
    public final boolean isWorkspaceRelative() {
      return ProjectClasspathResolver.this._classpathResolverJob.isRelative();
    }

    /**
     * {@inheritDoc}
     */
    public final void resolveProjectClasspath(final EclipseProject project) {
      ProjectClasspathResolver.this.resolveProjectClasspath(project);
    }
  }
}