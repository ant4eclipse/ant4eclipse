package org.ant4eclipse.jdt.tools;

import java.util.List;

import org.ant4eclipse.core.Assert;
import org.ant4eclipse.jdt.tools.container.JdtClasspathContainerArgument;
import org.ant4eclipse.jdt.tools.internal.ClasspathEntryResolverExecutor;
import org.ant4eclipse.jdt.tools.internal.ClasspathResolverContextImpl;
import org.ant4eclipse.jdt.tools.internal.ResolvedClasspathImpl;
import org.ant4eclipse.jdt.tools.internal.ResolverJob;
import org.ant4eclipse.jdt.tools.internal.classpathentry.ClasspathEntryResolver;
import org.ant4eclipse.jdt.tools.internal.classpathentry.ContainerClasspathEntryResolver;
import org.ant4eclipse.jdt.tools.internal.classpathentry.LibraryClasspathEntryResolver;
import org.ant4eclipse.jdt.tools.internal.classpathentry.OutputClasspathEntryResolver;
import org.ant4eclipse.jdt.tools.internal.classpathentry.ProjectClasspathEntryResolver;
import org.ant4eclipse.jdt.tools.internal.classpathentry.SourceClasspathEntryResolver;
import org.ant4eclipse.jdt.tools.internal.classpathentry.VariableClasspathEntryResolver;
import org.ant4eclipse.platform.model.resource.EclipseProject;

/**
 * @author
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
   * @return the resolved class path
   */
  public static final ResolvedClasspath resolveProjectClasspath(final EclipseProject project,
      final boolean resolveRelative, final boolean isRuntimeClasspath,
      final List<JdtClasspathContainerArgument> classpathContainerArguments) {
    Assert.notNull(project);

    // create a ResolverJob
    final ResolverJob job = new ResolverJob(project, project.getWorkspace(), resolveRelative, isRuntimeClasspath,
        classpathContainerArguments);

    final ClasspathEntryResolverExecutor executor = new ClasspathEntryResolverExecutor(true);
    final ResolvedClasspathImpl resolvedClasspath = new ResolvedClasspathImpl();

    executor.resolve(job.getRootProject(), new ClasspathEntryResolver[] { new VariableClasspathEntryResolver(),
        new ContainerClasspathEntryResolver(), new SourceClasspathEntryResolver(), new ProjectClasspathEntryResolver(),
        new LibraryClasspathEntryResolver(), new OutputClasspathEntryResolver() }, new ClasspathResolverContextImpl(
        executor, job, resolvedClasspath));

    // return the ResolvedClasspath
    return resolvedClasspath;
  }
}
