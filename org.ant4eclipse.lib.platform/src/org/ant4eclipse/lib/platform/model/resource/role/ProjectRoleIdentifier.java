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
package org.ant4eclipse.lib.platform.model.resource.role;

import org.ant4eclipse.lib.core.A4EService;
import org.ant4eclipse.lib.platform.model.resource.EclipseProject;
import org.ant4eclipse.lib.platform.model.resource.ProjectNature;

import java.util.List;
import java.util.Set;

/**
 * <p>
 * The {@link ProjectRoleIdentifier} defines the common interface for project role identifiers. A project role
 * identifier can be used to assign a specific role to an eclipse project when the project is reed from the underlying
 * workspace.
 * </p>
 * 
 * @author Daniel Kasmeroglu (daniel.kasmeroglu@kasisoft.net)
 */
public interface ProjectRoleIdentifier extends A4EService {

  /**
   * <p>
   * Returns <code>true</code> if the supplied project provides support for a specific role.
   * </p>
   * 
   * @param project
   *          the eclipse project that should be tested.
   * 
   * @return <code>true</code> if the role is applicable for the eclipse project.
   */
  boolean isRoleSupported( EclipseProject project );

  /**
   * <p>
   * Creates the specific {@link ProjectRole} for the given {@link EclipseProject}. During the parsing process you must
   * be aware that accessing other EclipseProject instances through the workspace is prohibited since that project
   * itself might not have been created yet.
   * </p>
   * 
   * @param project
   *          the eclipse project.
   */
  ProjectRole createRole( EclipseProject project );

  /**
   * This function will be invoked after each project within a workspace has been setup. Therefore each action that
   * requires to access other projects within the workspace can be implemented here.
   * 
   * @param project
   *          The project which role has to be modified if necessary. Not <code>null</code>.
   */
  void postProcess( EclipseProject project );

  /**
   * Returns a list of natures that are associated with this identifier. Usually the returned list only contains one
   * entry but it's possible to support multiple at the same time.
   * 
   * @return A list of natures that are associated with this identifier. Maybe <code>null</code>.
   */
  Set<ProjectNature> getNatures();

  /**
   * Returns a list of possible abbreviations for the natures. This function only makes sense if {@link #getNatures()}
   * delivers some value.
   * 
   * @return The possible abbreviations for the natures. Maybe <code>null</code>.
   */
  List<String> getNatureNicknames();

} /* ENDINTERFACE */
