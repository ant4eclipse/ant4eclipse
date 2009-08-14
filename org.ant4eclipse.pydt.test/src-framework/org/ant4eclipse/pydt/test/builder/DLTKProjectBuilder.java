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
package org.ant4eclipse.pydt.test.builder;

import org.ant4eclipse.platform.test.builder.EclipseProjectBuilder;

import org.ant4eclipse.pydt.model.project.PyDLTKProjectRole;

/**
 * Builder which is used for the DLTK based python implementation.
 * 
 * @author Daniel Kasmeroglu (Daniel.Kasmeroglu@Kasisoft.net)
 */
public class DLTKProjectBuilder extends EclipseProjectBuilder implements PythonProjectBuilder {

  /**
   * Initialises this builder using the supplied project name.
   * 
   * @param projectName
   *          The name of the project used to be created. Neither <code>null</code> nor empty.
   */
  public DLTKProjectBuilder(String projectName) {
    super(projectName);
    withNature(PyDLTKProjectRole.NATURE);
    withBuilder(PyDLTKProjectRole.BUILDCOMMAND);
  }

} /* ENDCLASS */
