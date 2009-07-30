/**********************************************************************
 * Copyright (c) 2005-2008 ant4eclipse project team.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Nils Hartmann, Daniel Kasmeroglu, Gerd Wuetherich
 **********************************************************************/
package org.ant4eclipse.pydt.model.project;

import org.ant4eclipse.core.Assert;
import org.ant4eclipse.core.logging.A4ELogging;

import org.ant4eclipse.platform.model.resource.EclipseProject;
import org.ant4eclipse.platform.model.resource.role.ProjectRole;
import org.ant4eclipse.platform.model.resource.role.ProjectRoleIdentifier;

import org.ant4eclipse.pydt.internal.model.project.PythonProjectRoleImpl;

/**
 * <p>
 * {@link ProjectRoleIdentifier} for the PyDev project role.
 * </p>
 * 
 * @author Daniel Kasmeroglu (Daniel.Kasmeroglu@Kasisoft.net)
 */
public final class PyDevRoleIdentifier implements ProjectRoleIdentifier {

  /**
   * <p>
   * Returns <code>true</code> if the given project has the nature used by PyDev. 
   * </p>
   */
  public boolean isRoleSupported(final EclipseProject project) {
    return (project.hasNature(PyDevProjectRole.PYDEV_NATURE));
  }

  /**
   * <p>
   * Adds a {@link PyDLTKProjectRole} to the given project and parses the pathes.
   * </p>
   */
  public ProjectRole createRole(final EclipseProject project) {
    A4ELogging.trace("PyDevRoleIdentifier.applyRole(%s)", project);
    Assert.notNull(project);
    final PythonProjectRoleImpl result = new PythonProjectRoleImpl(project);
//    ClasspathFileParser.parseClasspath(javaProjectRole);
    return result;
  }
  
} /* ENDCLASS */
