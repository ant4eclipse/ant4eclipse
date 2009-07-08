package org.ant4eclipse.platform.internal.tools;

import org.ant4eclipse.core.configuration.Ant4EclipseConfiguration;
import org.ant4eclipse.core.util.Utilities;

import org.ant4eclipse.platform.model.resource.EclipseProject;
import org.ant4eclipse.platform.tools.ReferencedProjectsResolver;
import org.ant4eclipse.platform.tools.ReferencedProjectsResolverService;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 */
public class ReferencedProjectsResolverServiceImpl implements ReferencedProjectsResolverService {

  private static final String                     REFERENCED_PROJECTS_RESOLVER_PREFIX = "referencedProjectsResolver";

  /** - */
  private Map<String, ReferencedProjectsResolver> _referencedProjectsResolvers;

  private boolean                                 _initialized                        = false;

  public String[] getReferenceTypes() {
    init();
    return this._referencedProjectsResolvers.keySet().toArray(new String[0]);
  }

  public List<EclipseProject> resolveReferencedProjects(EclipseProject project, String[] referenceTypes,
      List<Object> additionalElements) {
    init();

    List<EclipseProject> result = new LinkedList<EclipseProject>();

    for (String referenceType : referenceTypes) {
      if (this._referencedProjectsResolvers.containsKey(referenceType)) {
        ReferencedProjectsResolver resolver = this._referencedProjectsResolvers.get(referenceType);
        if (resolver.canHandle(project)) {
          List<EclipseProject> referencedProjects = resolver.resolveReferencedProjects(project, additionalElements);
          result.addAll(referencedProjects);
        }
      }
    }

    return result;
  }

  /**
   * @see org.ant4eclipse.platform.tools.ReferencedProjectsResolverService#resolveReferencedProjects(org.ant4eclipse.platform.model.resource.EclipseProject,
   *      java.util.Properties)
   */
  public List<EclipseProject> resolveReferencedProjects(EclipseProject project, List<Object> additionalElements) {
    return resolveReferencedProjects(project, getReferenceTypes(), additionalElements);
  }

  /**
   * Loads the configured RoleIdentifiers
   */
  protected void init() {

    /**
     * @todo [08-Jul-2009:KASI] We need a statement regarding multithreading and the use of A4E. Just in case someone
     *       might use A4E in a parallel task this might cause issues if not synchronized. Nevertheless this is unlikely
     *       to happen.
     */

    if (this._initialized) {
      return;
    }

    // get all properties that defines a ReferencedProjectsResolver
    Iterable<String[]> referencedProjectsResolverEntries = Ant4EclipseConfiguration.Helper
        .getAnt4EclipseConfiguration().getAllProperties(REFERENCED_PROJECTS_RESOLVER_PREFIX);

    final Map<String, ReferencedProjectsResolver> referencedProjectsResolvers = new HashMap<String, ReferencedProjectsResolver>();

    // Instantiate all ReferencedProjectsResolvers
    for (String[] referencedProjectsResolverEntry : referencedProjectsResolverEntries) {

      String referencedProjectsResolverClassName = referencedProjectsResolverEntry[1];
      ReferencedProjectsResolver referencedProjectsResolver = Utilities
          .newInstance(referencedProjectsResolverClassName);
      referencedProjectsResolvers.put(referencedProjectsResolverEntry[0], referencedProjectsResolver);
    }

    this._referencedProjectsResolvers = referencedProjectsResolvers;

    this._initialized = true;
  }
}
