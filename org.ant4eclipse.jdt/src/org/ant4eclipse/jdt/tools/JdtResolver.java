package org.ant4eclipse.jdt.tools;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;

import org.ant4eclipse.core.Assert;
import org.ant4eclipse.core.dependencygraph.DependencyGraph;
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
import org.ant4eclipse.platform.model.resource.Workspace;

/**
 * @author
 */
public class JdtResolver {

  /**
   * @param project
   * @param properties
   * @return
   */
  public static final List<EclipseProject> resolveReferencedProjects(final EclipseProject project,
      final Properties properties) {
    Assert.notNull(project);

    // create a ResolverJob
    final ResolverJob job = new ResolverJob(project, project.getWorkspace(), false, false, properties);

    final ClasspathEntryResolverExecutor classpathEntryResolverExecutor = new ClasspathEntryResolverExecutor(false);

    classpathEntryResolverExecutor.resolve(job.getRootProject(), new ClasspathEntryResolver[] {
        new ContainerClasspathEntryResolver(), new ProjectClasspathEntryResolver(), },
        new ClasspathResolverContextImpl(classpathEntryResolverExecutor, job));

    return classpathEntryResolverExecutor.getReferencedProjects();
  }

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

    // create a ResolverJob
    final ResolverJob job = new ResolverJob(project, project.getWorkspace(), resolveRelative, isRuntimeClasspath,
        properties);

    final ClasspathEntryResolverExecutor executor = new ClasspathEntryResolverExecutor(true);
    final ResolvedClasspathImpl resolvedClasspath = new ResolvedClasspathImpl();

    executor.resolve(job.getRootProject(), new ClasspathEntryResolver[] { new VariableClasspathEntryResolver(),
        new ContainerClasspathEntryResolver(), new SourceClasspathEntryResolver(), new ProjectClasspathEntryResolver(),
        new LibraryClasspathEntryResolver(), new OutputClasspathEntryResolver() }, new ClasspathResolverContextImpl(
        executor, job, resolvedClasspath));

    // return the ResolvedClasspath
    return resolvedClasspath;
  }

  public static List<EclipseProject> resolveBuildOrder(final Workspace workspace, final String[] projectNames,
      final Properties properties) {

    final EclipseProject[] eclipseProjects = workspace.getProjects(projectNames, true);

    final DependencyGraph<EclipseProject> dependencyGraph = new DependencyGraph<EclipseProject>();

    for (final EclipseProject eclipseProject : eclipseProjects) {

      final List<EclipseProject> referencedProjects = JdtResolver.resolveReferencedProjects(eclipseProject, properties);

      for (final EclipseProject referencedProject : referencedProjects) {
        if (!dependencyGraph.containsVertex(referencedProject)) {
          dependencyGraph.addVertex(referencedProject);
        }
        if (!eclipseProject.equals(referencedProject)) {
          dependencyGraph.addEdge(eclipseProject, referencedProject);
        }
      }
    }

    final List<EclipseProject> result = new LinkedList<EclipseProject>();
    final List<String> names = Arrays.asList(projectNames);

    final List<EclipseProject> orderProjects = dependencyGraph.calculateOrder();

    for (final EclipseProject eclipseProject : orderProjects) {
      if (names.contains(eclipseProject.getSpecifiedName())) {
        result.add(eclipseProject);
      }
    }
    return result;
  }
}
