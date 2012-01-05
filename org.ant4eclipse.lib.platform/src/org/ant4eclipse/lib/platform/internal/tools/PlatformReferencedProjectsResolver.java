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

import java.util.Arrays;
import java.util.List;

/**
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 */
public class PlatformReferencedProjectsResolver implements ReferencedProjectsResolver {

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean canHandle( EclipseProject project ) {
    return true;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public List<EclipseProject> resolveReferencedProjects( EclipseProject project, List<Object> additionalElements ) {
    EclipseProject[] references = project.getWorkspace().getProjects( project.getReferencedProjects(), false );
    return Arrays.asList( references );
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Integer getPriority() {
    return null;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void reset() {
  }
  
  /**
   * {@inheritDoc}
   */
  @Override
  public String getReferenceType() {
    return "platform";
  }
  
} /* ENDCLASS */
