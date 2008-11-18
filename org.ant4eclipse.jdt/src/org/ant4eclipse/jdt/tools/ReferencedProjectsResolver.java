package org.ant4eclipse.jdt.tools;

import java.util.LinkedList;
import java.util.List;
import java.util.Stack;

import org.ant4eclipse.core.Assert;
import org.ant4eclipse.core.exception.Ant4EclipseException;
import org.ant4eclipse.core.logging.A4ELogging;
import org.ant4eclipse.jdt.model.JdtModelExceptionCode;
import org.ant4eclipse.jdt.model.project.JavaProjectRole;
import org.ant4eclipse.jdt.model.project.RawClasspathEntry;
import org.ant4eclipse.jdt.model.project.internal.JavaProjectRoleImpl;
import org.ant4eclipse.platform.model.resource.EclipseProject;


/**
 * @author Nils Hartmann (nils@nilshartmann.net)
 */
public class ReferencedProjectsResolver {

  /**
   * Returns all projects that are referenced by this project's <code>.project</code> file
   * 
   * @param eclipseProject
   *          The project used to resolve the referenced projects.
   * @param recursive
   *          true <=> Resolve referenced projects, too.
   * @param rejected
   *          null <=> A missing project results in a FileParserException, else the list will be filled with names of
   *          non-existing projects.
   * 
   * @return A list of all referenced projects.
   * 
   * @throws BuildOrderException
   *           The build order could not be resolved.
   * @throws FileParserException
   *           Some project information could not be parsed.
   */
  public static final EclipseProject[] getReferencedProjects(final EclipseProject eclipseProject,
      final boolean recursive, final List rejected) {
    Assert.notNull(eclipseProject);
    return getReferencedProjects(eclipseProject, new Stack(), recursive, rejected);
  }

  /**
   * Returns all projects that are referenced by this project's <code>.project</code> file
   * 
   * @param eclipseProject
   *          The project used to resolve the referenced projects.
   * @param projectStack
   *          A stack used to prevent cycles.
   * @param recursive
   *          true <=> Resolve referenced projects, too.
   * @param rejected
   *          null <=> A missing project results in a FileParserException, else the list will be filled with names of
   *          non-existing projects.
   * 
   * @return A list of all referenced projects.
   * 
   * @throws BuildOrderException
   *           The build order could not be resolved.
   * @throws FileParserException
   *           Some project information could not be parsed.
   */
  public static final EclipseProject[] getReferencedProjects(final EclipseProject eclipseProject,
      final Stack projectStack, final boolean recursive, final List rejected) {

    if (projectStack.contains(eclipseProject)) {
      A4ELogging.warn("Detected circular reference: '%s'", eclipseProject.getFolderName());
      return new EclipseProject[0];
    }

    projectStack.push(eclipseProject);

    final List result = new LinkedList();

    final String[] referencedProjectNames = eclipseProject.getReferencedProjects();
    for (int i = 0; i < referencedProjectNames.length; i++) {
      final String referencedProjectName = referencedProjectNames[i];
      if (!eclipseProject.getWorkspace().hasProject(referencedProjectName)) {
        // generally this state would lead to a failure but if the user wants
        // to know about this, we will add this to the list of rejected projects.
        if (rejected != null) {
          rejected.add(referencedProjectName);
          continue;
        }
      }
      final EclipseProject referencedProject = eclipseProject.getWorkspace().getProject(referencedProjectName);
      if (recursive) {
        final EclipseProject[] requiredProjects = getReferencedProjects(referencedProject, projectStack, recursive,
            rejected);
        for (int j = 0; j < requiredProjects.length; j++) {
          final EclipseProject requiredProject = requiredProjects[j];
          if (!result.contains(requiredProject)) {
            result.add(requiredProject);
          }
        }
      } else {
        if (!result.contains(referencedProject)) {
          result.add(referencedProject);
        }
      }
    }

    if (!result.contains(eclipseProject)) {
      result.add(eclipseProject);
    }

    projectStack.pop();

    return (EclipseProject[]) result.toArray(new EclipseProject[result.size()]);
  }

  /**
   * Returns all projects that referenced by this project's classpath.
   * 
   * @param eclipseProject
   *          The project used to resolve the referenced projects.
   * @param exportedProjectsOnly
   *          true <=> Only return exported projects.
   * @param rejected
   *          null <=> A missing project results in a FileParserException, else the list will be filled with names of
   *          non-existing projects.
   * 
   * @return A list of all referenced projects.
   * 
   * @throws BuildOrderException
   *           The build order could not be resolved.
   * @throws FileParserException
   *           Some project information could not be parsed.
   */
  public static final EclipseProject[] getProjectsReferencedByClasspath(final EclipseProject eclipseProject,
      final boolean exportedProjectsOnly, final List rejected) {
    Assert.notNull(eclipseProject);
    return getProjectsReferencedByClasspath(eclipseProject, exportedProjectsOnly, eclipseProject, rejected);
  }

  /**
   * Returns all projects that are referenced by this project's <code>.project</code> file
   * 
   * @param eclipseProject
   *          The project used to resolve the referenced projects.
   * @param exportedProjectsOnly
   *          true <=> Only return exported projects.
   * @param rootProject
   *          The root project used to calculate the referenced projects.
   * @param rejected
   *          null <=> A missing project results in a FileParserException, else the list will be filled with names of
   *          non-existing projects.
   * 
   * @return A list of all referenced projects.
   * 
   * @throws BuildOrderException
   *           The build order could not be resolved.
   * @throws FileParserException
   *           Some project information could not be parsed.
   */
  private static final EclipseProject[] getProjectsReferencedByClasspath(final EclipseProject eclipseProject,
      final boolean exportedProjectsOnly, final EclipseProject rootProject, final List rejected) {
    return getProjectsReferencedByClasspath(eclipseProject, new Stack(), exportedProjectsOnly, rootProject, rejected);
  }

  /**
   * Returns all projects that are referenced by this project's <code>.project</code> file
   * 
   * @param eclipseProject
   *          The project used to resolve the referenced projects.
   * @param projectStack
   *          A stack used to prevent cycles.
   * @param exportedProjectsOnly
   *          true <=> Only return exported projects.
   * @param rootProject
   *          The root project used to calculate the referenced projects.
   * @param rejected
   *          null <=> A missing project results in a FileParserException, else the list will be filled with names of
   *          non-existing projects.
   * 
   * @return A list of all referenced projects.
   * 
   * @throws BuildOrderException
   *           The build order could not be resolved.
   * @throws FileParserException
   *           Some project information could not be parsed.
   */
  private static final EclipseProject[] getProjectsReferencedByClasspath(final EclipseProject eclipseProject,
      final Stack projectStack, final boolean exportedProjectsOnly, final EclipseProject rootProject,
      final List rejected) {
    if (!eclipseProject.hasRole(JavaProjectRoleImpl.class)) {
      throw new Ant4EclipseException(JdtModelExceptionCode.NO_JAVA_PROJECT_ROLE, eclipseProject.getFolderName());
    }
    final List result = new LinkedList();

    if (projectStack.contains(eclipseProject)) {
      A4ELogging.warn("Detected circular reference: '%s'", eclipseProject.getFolderName());
      return new EclipseProject[0];
    }

    projectStack.push(eclipseProject);

    final JavaProjectRole javaProjectRole = (JavaProjectRole) eclipseProject.getRole(JavaProjectRoleImpl.class);
    final RawClasspathEntry[] classpathEntries = javaProjectRole.getRawClasspathEntries();
    for (int i = 0; i < classpathEntries.length; i++) {
      final RawClasspathEntry classpathEntry = classpathEntries[i];

      if (classpathEntry.getEntryKind() == RawClasspathEntry.CPE_PROJECT) {
        if (classpathEntry.isExported() || !exportedProjectsOnly || eclipseProject.equals(rootProject)) {
          final String path = classpathEntry.getPath();
          final String referencedProjectName = path.substring(1);
          if (!eclipseProject.getWorkspace().hasProject(referencedProjectName)) {
            // generally this state would lead to a failure but if the user wants
            // to know about this, we will add this to the list of rejected projects.
            if (rejected != null) {
              rejected.add(referencedProjectName);
              continue;
            }
          }
          final EclipseProject referencedProject = eclipseProject.getWorkspace().getProject(referencedProjectName);
          final EclipseProject[] requiredProjects = getProjectsReferencedByClasspath(referencedProject, projectStack,
              exportedProjectsOnly, rootProject, rejected);
          for (int j = 0; j < requiredProjects.length; j++) {
            final EclipseProject requiredProject = requiredProjects[j];
            if (!result.contains(requiredProject)) {
              result.add(requiredProject);
            }
          }
        }
      }
    }

    if (!result.contains(eclipseProject)) {
      result.add(eclipseProject);
    }

    projectStack.pop();

    return (EclipseProject[]) result.toArray(new EclipseProject[result.size()]);
  }
}
