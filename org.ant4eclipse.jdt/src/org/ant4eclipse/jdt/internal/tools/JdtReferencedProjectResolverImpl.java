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

/**
 * <p>
 * </p>
 * 
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 * 
 */
public class JdtReferencedProjectResolverImpl implements ReferencedProjectsResolver {

  /**
   * {@inheritDoc}
   */
  public boolean canHandle(EclipseProject project) {
    return JavaProjectRole.Helper.hasJavaProjectRole(project);
  }

  /**
   * {@inheritDoc}
   */
  public List<EclipseProject> resolveReferencedProjects(EclipseProject project, List<Object> additionalElements) {
    Assert.notNull(project);

    List<JdtClasspathContainerArgument> classpathContainerArguments = new LinkedList<JdtClasspathContainerArgument>();

    if (additionalElements != null) {
      for (Object object : additionalElements) {
        if (object instanceof JdtClasspathContainerArgument) {
          JdtClasspathContainerArgument argument = (JdtClasspathContainerArgument) object;
          classpathContainerArguments.add(argument);
        }
      }
    }
    // create a ResolverJob
    ResolverJob job = new ResolverJob(project, project.getWorkspace(), false, false, classpathContainerArguments);

    ClasspathEntryResolverExecutor classpathEntryResolverExecutor = new ClasspathEntryResolverExecutor(false);

    classpathEntryResolverExecutor.resolve(job.getRootProject(), new ClasspathEntryResolver[] {
        new ContainerClasspathEntryResolver(), new ProjectClasspathEntryResolver(), },
        new ClasspathResolverContextImpl(classpathEntryResolverExecutor, job));

    // we need to remove the calling project, since the api states that only referenced projects have to be returned
    List<EclipseProject> result = classpathEntryResolverExecutor.getReferencedProjects();
    result.remove(project);
    return result;
  }
}
