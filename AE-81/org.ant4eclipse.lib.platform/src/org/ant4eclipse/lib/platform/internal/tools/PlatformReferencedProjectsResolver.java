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

import org.ant4eclipse.lib.platform.model.resource.EclipseProject;
import org.ant4eclipse.lib.platform.tools.ReferencedProjectsResolver;

import java.util.LinkedList;
import java.util.List;

/**
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 */
public class PlatformReferencedProjectsResolver implements ReferencedProjectsResolver {

  /**
   * {@inheritDoc}
   */
  public boolean canHandle(EclipseProject project) {
    return true;
  }

  /**
   * {@inheritDoc}
   */
  public List<EclipseProject> resolveReferencedProjects(EclipseProject project, List<Object> additionalElements) {

    // get referenced projects
    EclipseProject[] eclipseProjects = project.getWorkspace().getProjects(project.getReferencedProjects(), false);

    // copy to result
    List<EclipseProject> result = new LinkedList<EclipseProject>();
    for (EclipseProject eclipseProject : eclipseProjects) {
      result.add(eclipseProject);
    }

    // return result
    return result;
  }
}
