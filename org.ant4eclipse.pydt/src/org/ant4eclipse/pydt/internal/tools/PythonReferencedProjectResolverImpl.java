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
import org.ant4eclipse.core.util.Utilities;

import org.ant4eclipse.platform.model.resource.EclipseProject;
import org.ant4eclipse.platform.tools.ReferencedProjectsResolver;

import org.ant4eclipse.pydt.ant.usedargs.UsedProjectsArgumentComponent;

import java.util.List;

/**
 * <p>
 * Resolver implementation for the cdt. Currently the cdt doesn't support any kind of specific containers used to access
 * other projects, so the referenced projects are used in general.
 * </p>
 * 
 * @author Daniel Kasmeroglu (Daniel.Kasmeroglu@Kasisoft.net)
 */
public class PythonReferencedProjectResolverImpl implements ReferencedProjectsResolver {

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
    final UsedProjectsArgumentComponent args = getArgs(additionalElements);
    if (args.isExport() && PythonUtilities.isPyDevProject(project)) {
      A4ELogging.warn("The mode 'exported' is treated as 'all' for a PyDev project.");
    }
    final PythonProjectResolver resolver = new PythonProjectResolver(project.getWorkspace());
    resolver.setArgs(args);
    return resolver.resolve(project);
  }

  /**
   * Returns the arguments used to control the resolving process.
   * 
   * @param additionalElements
   *          Additional elements provided by the ant subsystem.
   * 
   * @return An instance controlling the resolving process. Not <code>null</code>.
   */
  private UsedProjectsArgumentComponent getArgs(final List<Object> additionalElements) {
    if (additionalElements != null) {
      final List<Object> elements = Utilities.filter(additionalElements, UsedProjectsArgumentComponent.class);
      if (!elements.isEmpty()) {
        final UsedProjectsArgumentComponent args = (UsedProjectsArgumentComponent) elements.get(0);
        if (elements.size() > 1) {
          A4ELogging.warn("Only one element '%s' is allowed ! Using the first one: '%s'.",
              UsedProjectsArgumentComponent.ELEMENTNAME, String.valueOf(args));
        }
        return args;
      }
    }
    return UsedProjectsArgumentComponent.DEFAULT;
  }

} /* ENDCLASS */
