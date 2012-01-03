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
package org.ant4eclipse.lib.pydt.model.project;

import org.ant4eclipse.lib.core.Assure;
import org.ant4eclipse.lib.core.logging.A4ELogging;
import org.ant4eclipse.lib.platform.model.resource.EclipseProject;
import org.ant4eclipse.lib.platform.model.resource.role.ProjectRole;
import org.ant4eclipse.lib.platform.model.resource.role.ProjectRoleIdentifier;
import org.ant4eclipse.lib.pydt.internal.model.project.PythonProjectRoleImpl;
import org.ant4eclipse.lib.pydt.internal.tools.DLTKParser;

/**
 * <p>
 * {@link ProjectRoleIdentifier} for the Python DLTK project role.
 * </p>
 * 
 * @author Daniel Kasmeroglu (Daniel.Kasmeroglu@Kasisoft.net)
 */
public final class DLTKRoleIdentifier implements ProjectRoleIdentifier {

  /**
   * <p>
   * Returns <code>true</code> if the given project has the nature of the Python DLTK framework.
   * </p>
   */
  @Override
  public boolean isRoleSupported(EclipseProject project) {
    return project.hasNature(DLTKProjectRole.NATURE);
  }

  /**
   * <p>
   * Adds a {@link DLTKProjectRole} to the given project and parses the pathes.
   * </p>
   */
  @Override
  public ProjectRole createRole(EclipseProject project) {
    A4ELogging.trace("PyDLTKRoleIdentifier.applyRole(%s)", project);
    Assure.notNull("project", project);
    PythonProjectRoleImpl result = new PythonProjectRoleImpl(project, true);
    DLTKParser.contributePathes(result);
    return result;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void postProcess(EclipseProject project) {
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Integer getPriority() {
    return null;
  }
  
} /* ENDCLASS */
