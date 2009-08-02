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
package org.ant4eclipse.pydt.internal.tools;

import org.ant4eclipse.core.logging.A4ELogging;

import org.ant4eclipse.platform.internal.tools.PlatformReferencedProjectsResolver;
import org.ant4eclipse.platform.model.resource.EclipseProject;

import java.util.List;

/**
 * <p>
 * Resolver implementation for the cdt. Currently the cdt doesn't support any kind of specific containers used to access
 * other projects, so the referenced projects are used in general.
 * </p>
 * 
 * @author Daniel Kasmeroglu (Daniel.Kasmeroglu@Kasisoft.net)
 */
public class PythonReferencedProjectResolverImpl extends PlatformReferencedProjectsResolver {

  /**
   * {@inheritDoc}
   */
  public boolean canHandle(EclipseProject project) {
    return PythonUtilities.isPythonRelatedProject(project);
  }

  /**
   * {@inheritDoc}
   */
  public List<EclipseProject> resolveReferencedProjects(EclipseProject project, List<Object> additionalElements) {
    if (PythonUtilities.isPyDevProject(project)) {
      // the PyDev project suite uses the original eclipse project references, so we go with the original implementation
      A4ELogging.debug("Resolving project '%s' for the PyDev framework.", project.getSpecifiedName());
      final List<EclipseProject> result = super.resolveReferencedProjects(project, additionalElements);
      // now we need to remove non PyDev projects
      for (int i = result.size() - 1; i >= 0; i--) {
        if (!PythonUtilities.isPyDevProject(result.get(i))) {
          result.remove(i);
        }
      }
      return result;
    } else /* if(PythonUtilities.isPyDLTKProject(project)) */{
      A4ELogging.debug("Resolving project '%s' for the Python DLTK framework.", project.getSpecifiedName());
      // the Python DLTK generally provides explicit references to projects (similar to java)
      PythonProjectResolver resolver = new PythonProjectResolver(project.getWorkspace());
      return resolver.resolve(project);
    }
  }

} /* ENDCLASS */
