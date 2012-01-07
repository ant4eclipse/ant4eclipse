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
package org.ant4eclipse.lib.cdt.internal.tools;

import org.ant4eclipse.lib.platform.internal.tools.PlatformReferencedProjectsResolver;
import org.ant4eclipse.lib.platform.model.resource.EclipseProject;

/**
 * <p>
 * Resolver implementation for the cdt. Currently the cdt doesn't support any kind of specific containers used to access
 * other projects, so the referenced projects are used in general.
 * </p>
 * 
 * @author Daniel Kasmeroglu (Daniel.Kasmeroglu@Kasisoft.net)
 */
public class CdtReferencedProjectResolverImpl extends PlatformReferencedProjectsResolver {

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean canHandle( EclipseProject project ) {
    return CdtUtilities.isCRelatedProject( project );
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getReferenceType() {
    return "cdt";
  }

} /* ENDCLASS */
