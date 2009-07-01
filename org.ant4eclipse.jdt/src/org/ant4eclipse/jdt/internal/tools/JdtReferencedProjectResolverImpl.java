package org.ant4eclipse.jdt.internal.tools;

import org.ant4eclipse.core.Assert;

import org.ant4eclipse.jdt.internal.tools.classpathentry.ClasspathEntryResolver;
import org.ant4eclipse.jdt.internal.tools.classpathentry.ContainerClasspathEntryResolver;
import org.ant4eclipse.jdt.internal.tools.classpathentry.ProjectClasspathEntryResolver;
import org.ant4eclipse.jdt.model.project.JavaProjectRole;
import org.ant4eclipse.jdt.tools.container.JdtClasspathContainerArgument;

import org.ant4eclipse.platform.model.resource.EclipseProject;
import org.ant4eclipse.platform.tools.ReferencedProjectsResolver;

import java.util.LinkedList;
import java.util.List;

public class JdtReferencedProjectResolverImpl implements ReferencedProjectsResolver {

  /**
   * @see org.ant4eclipse.platform.tools.ReferencedProjectsResolver#canHandle(org.ant4eclipse.platform.model.resource.EclipseProject)
   */
  public boolean canHandle(final EclipseProject project) {
    return JavaProjectRole.Helper.hasJavaProjectRole(project);
  }

  /**
   * <p>
   * Returns a list that contains all directly referenced projects of the given project.
   * </p>
   * 
   * @param project
   * @param properties
   * @return
   */
  public List<EclipseProject> resolveReferencedProjects(final EclipseProject project,
      final List<Object> additionalElements) {
    Assert.notNull(project);

    final List<JdtClasspathContainerArgument> classpathContainerArguments = new LinkedList<JdtClasspathContainerArgument>();

    if (additionalElements != null) {
      for (final Object object : additionalElements) {
        if (object instanceof JdtClasspathContainerArgument) {
          final JdtClasspathContainerArgument argument = (JdtClasspathContainerArgument) object;
          classpathContainerArguments.add(argument);
        }
      }
    }
    // create a ResolverJob
    final ResolverJob job = new ResolverJob(project, project.getWorkspace(), false, false, classpathContainerArguments);

    final ClasspathEntryResolverExecutor classpathEntryResolverExecutor = new ClasspathEntryResolverExecutor(false);

    classpathEntryResolverExecutor.resolve(job.getRootProject(), new ClasspathEntryResolver[] {
        new ContainerClasspathEntryResolver(), new ProjectClasspathEntryResolver(), },
        new ClasspathResolverContextImpl(classpathEntryResolverExecutor, job));

    // System.err.println(" Resolved: " + classpathEntryResolverExecutor.getReferencedProjects());

    return classpathEntryResolverExecutor.getReferencedProjects();
  }
}
