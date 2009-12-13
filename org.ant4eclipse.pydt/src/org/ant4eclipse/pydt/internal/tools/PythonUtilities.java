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

import org.ant4eclipse.platform.model.resource.EclipseProject;

import org.ant4eclipse.lib.core.Assure;
import org.ant4eclipse.lib.core.service.ServiceRegistry;
import org.ant4eclipse.pydt.internal.model.project.PythonProjectRole;
import org.ant4eclipse.pydt.model.PythonInterpreter;
import org.ant4eclipse.pydt.model.project.DLTKProjectRole;
import org.ant4eclipse.pydt.model.project.PyDevProjectRole;
import org.ant4eclipse.pydt.model.pyre.PythonRuntimeRegistry;

/**
 * <p>
 * Collection of python related helper functions.
 * </p>
 * 
 * @author Daniel Kasmeroglu (Daniel.Kasmeroglu@Kasisoft.net)
 */
public class PythonUtilities {

  /**
   * Disable instantiation.
   */
  private PythonUtilities() {
  }

  /**
   * Returns <code>true</code> in case the supplied project has one of the supported python natures. This method is just
   * a convenience function which combines {@link #isPyDevProject(EclipseProject)} and
   * {@link #isPyDLTKProject(EclipseProject)}.
   * 
   * @param project
   *          The project that has to be examined. Not <code>null</code>.
   * 
   * @return <code>true</code> <=> The supplied project has one of the supported python natures.
   */
  public static final boolean isPythonRelatedProject(EclipseProject project) {
    Assure.notNull(project);
    return project.hasRole(PyDevProjectRole.class) || project.hasRole(DLTKProjectRole.class);
  }

  /**
   * Returns <code>true</code> in case the supplied project has been created within the PyDev framework.
   * 
   * @param project
   *          The project that has to be examined. Not <code>null</code>.
   * 
   * @return <code>true</code> <=> The supplied project has been created using the PyDev framework.
   */
  public static final boolean isPyDevProject(EclipseProject project) {
    Assure.notNull(project);
    if (project.hasRole(PyDevProjectRole.class)) {
      PythonProjectRole role = project.getRole(PyDevProjectRole.class);
      return !role.isDLTK();
    } else {
      return false;
    }
  }

  /**
   * Returns <code>true</code> in case the supplied project has been created within the Python DLTK framework.
   * 
   * @param project
   *          The project that has to be examined. Not <code>null</code>.
   * 
   * @return <code>true</code> <=> The supplied project has been created within the Python DLTK framework.
   */
  public static final boolean isPyDLTKProject(EclipseProject project) {
    Assure.notNull(project);
    if (project.hasRole(DLTKProjectRole.class)) {
      PythonProjectRole role = project.getRole(DLTKProjectRole.class);
      return role.isDLTK();
    } else {
      return false;
    }
  }

  /**
   * Returns a list of all known python interpreters.
   * 
   * @return A list of all known python interpreters. Not <code>null</code>.
   */
  public static final PythonInterpreter[] getPythonInterpreters() {
    PythonRuntimeRegistry registry = ServiceRegistry.instance().getService(PythonRuntimeRegistry.class);
    return registry.getSupportedInterpreters();
  }

} /* ENDCLASS */
