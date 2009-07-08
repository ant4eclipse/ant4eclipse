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
   * </p>
   * <p>
   * E.g. {"platform", "jdt"}
   * </p>
   * 
   * @return
   */
  String[] getReferenceTypes();

  /**
   * <p>
   * </p>
   * <p>
   * </p>
   * 
   * @param project
   * @param referenceTypes
   *          e.g. {"platform", "jdt"}
   * @param additionalElements
   * @return
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
