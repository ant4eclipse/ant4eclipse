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
package org.ant4eclipse.lib.pydt.internal.tools;

import org.ant4eclipse.lib.core.A4ECore;
import org.ant4eclipse.lib.platform.model.resource.EclipseProject;
import org.ant4eclipse.lib.pydt.internal.model.project.PythonProjectRole;
import org.ant4eclipse.lib.pydt.model.PythonInterpreter;
import org.ant4eclipse.lib.pydt.model.project.DLTKProjectRole;
import org.ant4eclipse.lib.pydt.model.project.PyDevProjectRole;
import org.ant4eclipse.lib.pydt.model.pyre.PythonRuntimeRegistry;

import java.util.List;

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
  // Assure.notNull( "project", project );
  public static final boolean isPythonRelatedProject( EclipseProject project ) {
    return project.hasRole( PyDevProjectRole.class ) || project.hasRole( DLTKProjectRole.class );
  }

  /**
   * Returns <code>true</code> in case the supplied project has been created within the PyDev framework.
   * 
   * @param project
   *          The project that has to be examined. Not <code>null</code>.
   * 
   * @return <code>true</code> <=> The supplied project has been created using the PyDev framework.
   */
  // Assure.notNull( "project", project );
  public static final boolean isPyDevProject( EclipseProject project ) {
    if( project.hasRole( PyDevProjectRole.class ) ) {
      PythonProjectRole role = project.getRole( PyDevProjectRole.class );
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
  // Assure.notNull( "project", project );
  public static final boolean isPyDLTKProject( EclipseProject project ) {
    if( project.hasRole( DLTKProjectRole.class ) ) {
      PythonProjectRole role = project.getRole( DLTKProjectRole.class );
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
  public static final List<PythonInterpreter> getPythonInterpreters() {
    PythonRuntimeRegistry registry = A4ECore.instance().getRequiredService( PythonRuntimeRegistry.class );
    return registry.getSupportedInterpreters();
  }

} /* ENDCLASS */
