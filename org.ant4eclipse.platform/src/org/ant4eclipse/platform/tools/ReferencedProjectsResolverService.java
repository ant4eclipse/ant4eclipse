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
package org.ant4eclipse.platform.tools;

import org.ant4eclipse.platform.model.resource.EclipseProject;

import org.ant4eclipse.lib.core.service.ServiceRegistry;

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
   * Returns a list of resolved projects that are directly referenced by a given project. Only the resolvers for the
   * specified reference types will be used.
   * </p>
   * 
   * @param project
   *          The project which referenced projects will be looked up. Not <code>null</code>.
   * @param referenceTypes
   *          e.g. {"platform", "jdt"}. If this list doesn't contain at least one entry, the returnvalue will be empty.
   * @param additionalElements
   *          These elements are generally provided by subelements of a task. Each implementor of this interface is
   *          responsible to verify that he's capable to handle these additional elements. Elements that cannot be
   *          handled have to be ignored (meaning that no message is necessary in these cases).
   * 
   * @return A list of all directly referenced projects. Not <code>null</code>.
   */
  List<EclipseProject> resolveReferencedProjects(EclipseProject project, String[] referenceTypes,
      List<Object> additionalElements);

  /**
   * <p>
   * Returns a list of resolved projects that are directly referenced by a given project. This resolving process is
   * aware of all reference types.
   * </p>
   * 
   * @param project
   *          The project which referenced projects will be looked up. Not <code>null</code>.
   * @param additionalElements
   *          These elements are generally provided by subelements of a task. Each implementor of this interface is
   *          responsible to verify that he's capable to handle these additional elements. Elements that cannot be
   *          handled have to be ignored (meaning that no message is necessary in these cases).
   * 
   * @return A list of all directly referenced projects. Not <code>null</code>.
   */
  List<EclipseProject> resolveReferencedProjects(EclipseProject project, List<Object> additionalElements);

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
      return ServiceRegistry.instance().getService(ReferencedProjectsResolverService.class);
    }
  }
}
