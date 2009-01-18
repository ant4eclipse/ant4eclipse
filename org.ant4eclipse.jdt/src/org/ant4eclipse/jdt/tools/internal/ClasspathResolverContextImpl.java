package org.ant4eclipse.jdt.tools.internal;

import java.util.EmptyStackException;
import java.util.Properties;

import org.ant4eclipse.jdt.tools.ResolvedClasspathEntry;
import org.ant4eclipse.jdt.tools.container.ClasspathResolverContext;
import org.ant4eclipse.platform.model.resource.EclipseProject;
import org.ant4eclipse.platform.model.resource.Workspace;

/**
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 */
public class ClasspathResolverContextImpl implements ClasspathResolverContext {

  private final ClasspathEntryResolverExecutor _executor;

  private final ResolverJob                    _resolverJob;

  private final ResolvedClasspathImpl          _resolvedClasspath;

  public ClasspathResolverContextImpl(final ClasspathEntryResolverExecutor executor, final ResolverJob resolverJob,
      final ResolvedClasspathImpl resolvedClasspath) {
    this._executor = executor;
    this._resolverJob = resolverJob;
    this._resolvedClasspath = resolvedClasspath;
  }

  public ClasspathResolverContextImpl(final ClasspathEntryResolverExecutor executor, final ResolverJob resolverJob) {
    this(executor, resolverJob, null);
  }

  public EclipseProject getCurrentProject() {
    try {
      return this._executor.getCurrentProject();
    } catch (final EmptyStackException e) {
      return null;
    }
  }

  public boolean hasCurrentProject() {
    return this._executor.hasCurrentProject();
  }

  public void resolveProjectClasspath(final EclipseProject project) {
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
  public Properties getProperties() {
    return this._resolverJob.getProperties();
  }

  /**
   * {@inheritDoc}
   */
  public final boolean isWorkspaceRelative() {
    return this._resolverJob.isRelative();
  }

  public void addBootClasspathEntry(final ResolvedClasspathEntry resolvedClasspathEntry) {
    if (this._resolvedClasspath != null) {
      this._resolvedClasspath.addBootClasspathEntry(resolvedClasspathEntry);
    }
  }

  public void addClasspathEntry(final ResolvedClasspathEntry resolvedClasspathEntry) {
    if (this._resolvedClasspath != null) {
      this._resolvedClasspath.addClasspathEntry(resolvedClasspathEntry);
    }
  }
}
