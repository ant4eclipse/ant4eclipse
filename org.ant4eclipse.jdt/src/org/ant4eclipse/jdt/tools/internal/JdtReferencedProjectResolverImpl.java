package org.ant4eclipse.jdt.tools.internal;

import java.util.List;
import java.util.Properties;

import org.ant4eclipse.core.Assert;
import org.ant4eclipse.jdt.ant.containerargs.JdtClasspathContainerArgument;
import org.ant4eclipse.jdt.model.project.JavaProjectRole;
import org.ant4eclipse.jdt.tools.internal.classpathentry.ClasspathEntryResolver;
import org.ant4eclipse.jdt.tools.internal.classpathentry.ContainerClasspathEntryResolver;
import org.ant4eclipse.jdt.tools.internal.classpathentry.ProjectClasspathEntryResolver;
import org.ant4eclipse.platform.model.resource.EclipseProject;
import org.ant4eclipse.platform.tools.ReferencedProjectsResolver;

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

    final Properties properties = new Properties();

    for (final Object object : additionalElements) {
      if (object instanceof JdtClasspathContainerArgument) {
        final JdtClasspathContainerArgument argument = (JdtClasspathContainerArgument) object;
        properties.put(argument.getKey(), argument.getValue());
      }
    }

    // create a ResolverJob
    final ResolverJob job = new ResolverJob(project, project.getWorkspace(), false, false, properties);

    final ClasspathEntryResolverExecutor classpathEntryResolverExecutor = new ClasspathEntryResolverExecutor(false);

    classpathEntryResolverExecutor.resolve(job.getRootProject(), new ClasspathEntryResolver[] {
        new ContainerClasspathEntryResolver(), new ProjectClasspathEntryResolver(), },
        new ClasspathResolverContextImpl(classpathEntryResolverExecutor, job));

    return classpathEntryResolverExecutor.getReferencedProjects();
  }
}
