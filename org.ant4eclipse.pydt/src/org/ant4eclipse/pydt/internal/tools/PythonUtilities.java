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

import org.ant4eclipse.core.Assert;

import org.ant4eclipse.platform.model.resource.EclipseProject;

import org.ant4eclipse.pydt.model.project.PyDLTKProjectRole;
import org.ant4eclipse.pydt.model.project.PyDevProjectRole;

/**
 * <p>Collection of python related helper functions.</p>
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
   * Returns <code>true</code> in case the supplied project has one of the supported python natures.
   * This method is just a convenience function which combines {@link #isPyDevProject(EclipseProject)}
   * and {@link #isPyDLTKProject(EclipseProject)}.
   * 
   * @param project
   *            The project that has to be examined. Not <code>null</code>.
   *            
   * @return   <code>true</code> <=> The supplied project has one of the supported python natures.
   */
  public static final boolean isPythonRelatedProject(final EclipseProject project) {
    Assert.notNull(project);
    return project.hasRole(PyDevProjectRole.class) || project.hasRole(PyDLTKProjectRole.class);
  }
  
  /**
   * Returns <code>true</code> in case the supplied project has been created within the PyDev framework.
   *  
   * @param project
   *            The project that has to be examined. Not <code>null</code>.
   *            
   * @return   <code>true</code> <=> The supplied project has been created using the PyDev framework.
   */
  public static final boolean isPyDevProject(final EclipseProject project) {
    Assert.notNull(project);
    return project.hasRole(PyDevProjectRole.class);
  }

  /**
   * Returns <code>true</code> in case the supplied project has been created within the Python DLTK framework.
   * 
   * @param project
   *            The project that has to be examined. Not <code>null</code>.
   *            
   * @return   <code>true</code> <=> The supplied project has been created within the Python DLTK framework.
   */
  public static final boolean isPyDLTKProject(final EclipseProject project) {
    Assert.notNull(project);
    return project.hasRole(PyDLTKProjectRole.class);
  }
  
} /* ENDCLASS */
