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
package org.ant4eclipse.cdt.internal.tools;

import org.ant4eclipse.platform.model.resource.EclipseProject;
import org.ant4eclipse.platform.tools.ReferencedProjectsResolver;

import java.util.List;

/**
 * <p>Resolver implementation for the cdt.</p>
 * 
 * @author Daniel Kasmeroglu (Daniel.Kasmeroglu@Kasisoft.net)
 */
public class CdtReferencedProjectResolverImpl implements ReferencedProjectsResolver {

  /**
   * {@inheritDoc}
   */
  public boolean canHandle(EclipseProject project) {
    return CdtUtilities.isCRelatedProject(project);
  }

  /**
   * {@inheritDoc} 
   */
  public List<EclipseProject> resolveReferencedProjects(EclipseProject project, List<Object> additionalElements) {
    return null;
  }

} /* ENDCLASS */
