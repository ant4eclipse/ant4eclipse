/**********************************************************************
 * Copyright (c) 2005-2009 ant4eclipse project team.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Nils Hartmann, Daniel Kasmeroglu, Gerd Wuetherich
 **********************************************************************/
package org.ant4eclipse.lib.platform.internal.tools;

import org.ant4eclipse.lib.core.configuration.Ant4EclipseConfiguration;
import org.ant4eclipse.lib.core.logging.A4ELogging;
import org.ant4eclipse.lib.core.service.ServiceRegistryAccess;
import org.ant4eclipse.lib.core.util.Pair;
import org.ant4eclipse.lib.core.util.Utilities;
import org.ant4eclipse.lib.platform.model.resource.EclipseProject;
import org.ant4eclipse.lib.platform.tools.ReferencedProjectsResolver;
import org.ant4eclipse.lib.platform.tools.ReferencedProjectsResolverService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * <p>
 * Helper class that allows you to resolve referenced projects.
 * </p>
 * 
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 */
public class ReferencedProjectsResolverServiceImpl implements ReferencedProjectsResolverService {

  /** - */
  private static final String                     REFERENCED_PROJECTS_RESOLVER_PREFIX = "referencedProjectsResolver";

  /** - */
  private Map<String, ReferencedProjectsResolver> _referencedProjectsResolvers;

  /** - */
  private boolean                                 _initialized                        = false;

  /**
   * {@inheritDoc}
   */
  public String[] getReferenceTypes() {

    // lazy initialization of the resolver services
    init();

    // return all known reference types
    return this._referencedProjectsResolvers.keySet().toArray(new String[0]);
  }

  /**
   * {@inheritDoc}
   */
  public List<EclipseProject> resolveReferencedProjects(EclipseProject project, String[] referenceTypes,
      List<Object> additionalElements) {

    // lazy initialization of the resolver services
    init();

    Set<EclipseProject> result = new HashSet<EclipseProject>();

    referenceTypes = Utilities.cleanup(referenceTypes);

    if (referenceTypes != null) {
      for (String referenceType : referenceTypes) {
        if (this._referencedProjectsResolvers.containsKey(referenceType)) {
          ReferencedProjectsResolver resolver = this._referencedProjectsResolvers.get(referenceType);
          if (resolver.canHandle(project)) {
            List<EclipseProject> referencedProjects = resolver.resolveReferencedProjects(project, additionalElements);
            result.addAll(referencedProjects);
          } else {
            A4ELogging.debug("The reference type '%s' can't handle project '%s'.", referenceType, project
                .getSpecifiedName());
          }
        } else {
          A4ELogging.warn("The reference type '%s' is not supported.", referenceType);
        }
      }
    } else {
      A4ELogging.warn("The resolving process didn't come with at least one reference type.");
    }
    return new ArrayList<EclipseProject>(result);
  }

  /**
   * {@inheritDoc}
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
    Ant4EclipseConfiguration config = ServiceRegistryAccess.instance().getService(Ant4EclipseConfiguration.class);
    Iterable<Pair<String, String>> referencedProjectsResolverEntries = config
        .getAllProperties(REFERENCED_PROJECTS_RESOLVER_PREFIX);

    Map<String, ReferencedProjectsResolver> referencedProjectsResolvers = new HashMap<String, ReferencedProjectsResolver>();

    // Instantiate all ReferencedProjectsResolvers
    for (Pair<String, String> referencedProjectsResolverEntry : referencedProjectsResolverEntries) {

      String referencedProjectsResolverClassName = referencedProjectsResolverEntry.getSecond();
      ReferencedProjectsResolver referencedProjectsResolver = Utilities
          .newInstance(referencedProjectsResolverClassName);
      referencedProjectsResolvers.put(referencedProjectsResolverEntry.getFirst(), referencedProjectsResolver);
    }

    this._referencedProjectsResolvers = referencedProjectsResolvers;

    this._initialized = true;
  }
}
