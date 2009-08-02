package org.ant4eclipse.platform.tools;

import org.ant4eclipse.core.service.ServiceRegistry;

import org.ant4eclipse.platform.model.resource.EclipseProject;

import java.util.List;

/**
 * <p>
 * Resolves all projects that are directly referenced by the specified project.
 * </p>
 */
public interface ReferencedProjectsResolverService {

  /**
   * <p>
   * Returns a list of all currently supported reference types. F.e. {"platform","jdt"}.
   * </p>
   * 
   * @return A list of all currently supported reference types. Not <code>null</code>.
   */
  String[] getReferenceTypes();

  /**
   * <p>
   * Returns a list of resolved projects that are directly or indirectly reference by a given project. Only the
   * resolvers for the specified reference types will be used.
   * </p>
   * 
   * @param project
   *          The project which referenced projects will be looked up. Not <code>null</code>.
   * @param referenceTypes
   *          e.g. {"platform", "jdt"}. If this list doesn't contain at least one entry, the returnvalue will be empty.
   * @param additionalElements
   * @todo [02-Aug-2009:KASI] Still necessary to be described.
   * 
   * @return A list of all referenced projects. Not <code>null</code>.
   */
  List<EclipseProject> resolveReferencedProjects(final EclipseProject project, String[] referenceTypes,
      final List<Object> additionalElements);

  /**
   * @param project
   * @param properties
   * @return
   */
  List<EclipseProject> resolveReferencedProjects(final EclipseProject project, final List<Object> additionalElements);

  /**
   */
  static class Helper {

    /**
     * <p>
     * Fetches the {@link ReferencedProjectsResolverService} instance from the {@link ServiceRegistry}.
     * </p>
     * 
     * @return the registered {@link ReferencedProjectsResolverService}
     */
    public static ReferencedProjectsResolverService getService() {
      return (ReferencedProjectsResolverService) ServiceRegistry.instance().getService(
          ReferencedProjectsResolverService.class.getName());
    }
  }
}
