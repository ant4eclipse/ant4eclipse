package org.ant4eclipse.platform.tools;

import java.util.List;

import org.ant4eclipse.core.service.ServiceRegistry;
import org.ant4eclipse.platform.model.resource.EclipseProject;

/**
 */
public interface ReferencedProjectsResolverService {

  /**
   * @param project
   * @param properties
   * @return
   */
  public List<EclipseProject> resolveReferencedProjects(final EclipseProject project,
      final List<Object> additionalElements);

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
