package org.ant4eclipse.platform.tools.internal;

import java.util.LinkedList;
import java.util.List;
import java.util.Properties;

import org.ant4eclipse.core.configuration.Ant4EclipseConfiguration;
import org.ant4eclipse.core.logging.A4ELogging;
import org.ant4eclipse.core.util.Utilities;
import org.ant4eclipse.platform.model.resource.EclipseProject;
import org.ant4eclipse.platform.tools.ReferencedProjectsResolver;
import org.ant4eclipse.platform.tools.ReferencedProjectsResolverService;

/**
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 */
public class ReferencedProjectsResolverServiceImpl implements ReferencedProjectsResolverService {

  private static final String              REFERENCED_PROJECTS_RESOLVER_PREFIX = "referencedProjectsResolver";

  /** - */
  private List<ReferencedProjectsResolver> _referencedProjectsResolvers;

  private boolean                          _initialized                        = false;

  /**
   * @see org.ant4eclipse.platform.tools.ReferencedProjectsResolverService#resolveReferencedProjects(org.ant4eclipse.platform.model.resource.EclipseProject,
   *      java.util.Properties)
   */
  public List<EclipseProject> resolveReferencedProjects(EclipseProject project, Properties properties) {
    init();

    List<EclipseProject> result = new LinkedList<EclipseProject>();

    for (ReferencedProjectsResolver resolver : this._referencedProjectsResolvers) {
      if (resolver.canHandle(project)) {
        List<EclipseProject> referencedProjects = resolver.resolveReferencedProjects(project, properties);
        result.addAll(referencedProjects);
      }
    }

    return result;
  }

  /**
   * Loads the configured RoleIdentifiers
   */
  protected void init() {

    if (this._initialized) {
      return;
    }

    // get all properties that defines a ReferencedProjectsResolver
    Iterable<String[]> referencedProjectsResolverEntries = Ant4EclipseConfiguration.Helper
        .getAnt4EclipseConfiguration().getAllProperties(REFERENCED_PROJECTS_RESOLVER_PREFIX);

    final List<ReferencedProjectsResolver> referencedProjectsResolvers = new LinkedList<ReferencedProjectsResolver>();

    // Instantiate all ReferencedProjectsResolvers
    for (String[] referencedProjectsResolverEntry : referencedProjectsResolverEntries) {

      // we're not interested in the key of a ReferencedProjectsResolver. Only the class name (value of the entry) is
      // relevant
      String referencedProjectsResolverClassName = referencedProjectsResolverEntry[1];
      ReferencedProjectsResolver referencedProjectsResolver = Utilities
          .newInstance(referencedProjectsResolverClassName);

      A4ELogging.trace("Register ReferencedProjectsResolver '%s'", new Object[] { referencedProjectsResolver });

      referencedProjectsResolvers.add(referencedProjectsResolver);
    }

    this._referencedProjectsResolvers = referencedProjectsResolvers;

    this._initialized = true;
  }
}
