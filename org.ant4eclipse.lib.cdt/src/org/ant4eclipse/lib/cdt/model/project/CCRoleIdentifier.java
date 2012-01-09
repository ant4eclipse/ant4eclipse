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
package org.ant4eclipse.lib.cdt.model.project;

import org.ant4eclipse.lib.cdt.internal.model.project.CCProjectRoleImpl;
import org.ant4eclipse.lib.core.logging.A4ELogging;
import org.ant4eclipse.lib.platform.model.resource.EclipseProject;
import org.ant4eclipse.lib.platform.model.resource.role.AbstractProjectRoleIdentifier;
import org.ant4eclipse.lib.platform.model.resource.role.ProjectRole;
import org.ant4eclipse.lib.platform.model.resource.role.ProjectRoleIdentifier;

/**
 * <p>
 * {@link ProjectRoleIdentifier} for the c++ project role.
 * </p>
 * 
 * @author Daniel Kasmeroglu (Daniel.Kasmeroglu@Kasisoft.net)
 */
public class CCRoleIdentifier extends AbstractProjectRoleIdentifier {

  public CCRoleIdentifier() {
    super( CCProjectRole.CC_NATURE, "cc", "c++", "cpp" );
  }

  /**
   * <p>
   * Adds a {@link CProjectRole} to the given project and parses the pathes.
   * </p>
   */
  // Assure.notNull( "project", project );
  @Override
  public ProjectRole createRole( EclipseProject project ) {
    A4ELogging.trace( "CRoleIdentifier.applyRole(%s)", project );
    return new CCProjectRoleImpl( project );
  }

} /* ENDCLASS */
