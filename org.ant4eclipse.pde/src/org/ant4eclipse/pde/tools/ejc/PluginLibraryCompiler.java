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
package org.ant4eclipse.pde.tools.ejc;

import org.ant4eclipse.platform.model.resource.EclipseProject;

/**
 * PluginLibraryCompiler abstracts several ways of compiling an Eclipse plugin
 * 
 * @author Nils Hartmann (nils@nilshartmann.net)
 */
public interface PluginLibraryCompiler {

  public void setEclipseProject(EclipseProject eclipseProject);

  /**
   * Returns a human readable name of this compiler ("javac" or "Eclipse compiler", ...)
   * 
   * @return a human readable name of this compiler ("javac" or "Eclipse compiler", ...)
   */
  public String getName();

  /**
   * Compiles a library from its sourceFolders to the destFolder
   * 
   * @param context
   *          The context containing information about source and classfile destinations etc
   */
  public void compile(PluginLibraryBuilderContext context);

}
