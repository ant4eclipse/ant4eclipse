package org.ant4eclipse.jdt.tools;

import org.ant4eclipse.core.Assert;

import org.ant4eclipse.jdt.internal.tools.ClasspathEntryResolverExecutor;
import org.ant4eclipse.jdt.internal.tools.ClasspathResolverContextImpl;
import org.ant4eclipse.jdt.internal.tools.ResolvedClasspathImpl;
import org.ant4eclipse.jdt.internal.tools.ResolverJob;
import org.ant4eclipse.jdt.internal.tools.classpathentry.ClasspathEntryResolver;
import org.ant4eclipse.jdt.internal.tools.classpathentry.ContainerClasspathEntryResolver;
import org.ant4eclipse.jdt.internal.tools.classpathentry.LibraryClasspathEntryResolver;
import org.ant4eclipse.jdt.internal.tools.classpathentry.OutputClasspathEntryResolver;
import org.ant4eclipse.jdt.internal.tools.classpathentry.ProjectClasspathEntryResolver;
import org.ant4eclipse.jdt.internal.tools.classpathentry.SourceClasspathEntryResolver;
import org.ant4eclipse.jdt.internal.tools.classpathentry.VariableClasspathEntryResolver;
import org.ant4eclipse.jdt.tools.container.JdtClasspathContainerArgument;

import org.ant4eclipse.platform.model.resource.EclipseProject;

import java.util.List;

/**
 * <p>
 * Helper class. Use this class to resolve the class path of a given eclipse project.
 * </p>
 * 
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 */
public class JdtResolver {

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
   * @param classpathContainerArguments
   *          an optional list with class path container arguments
   * @return the resolved class path
   */
  public static final ResolvedClasspath resolveProjectClasspath(final EclipseProject project,
      final boolean resolveRelative, final boolean isRuntimeClasspath,
      final List<JdtClasspathContainerArgument> classpathContainerArguments) {
    Assert.notNull(project);

    // create a ResolverJob
    final ResolverJob job = new ResolverJob(project, project.getWorkspace(), resolveRelative, isRuntimeClasspath,
        classpathContainerArguments);

    // create the ClasspathEntryResolverExecutor
    final ClasspathEntryResolverExecutor executor = new ClasspathEntryResolverExecutor(true);

    // create the ClasspathEntryResolvers
    final ClasspathEntryResolver[] resolvers = new ClasspathEntryResolver[] { new VariableClasspathEntryResolver(),
        new ContainerClasspathEntryResolver(), new SourceClasspathEntryResolver(), new ProjectClasspathEntryResolver(),
        new LibraryClasspathEntryResolver(), new OutputClasspathEntryResolver() };

    // create the result object
    final ResolvedClasspathImpl resolvedClasspath = new ResolvedClasspathImpl();

    // execute the job
    executor.resolve(job.getRootProject(), resolvers,
        new ClasspathResolverContextImpl(executor, job, resolvedClasspath));

    // return the ResolvedClasspath
    return resolvedClasspath;
  }
}
