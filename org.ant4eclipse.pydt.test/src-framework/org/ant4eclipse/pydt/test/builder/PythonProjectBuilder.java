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

/**
 * Simple interface allowing to use the different varieties of python project natures to be tested using the same
 * testcode.
 * 
 * @author Daniel Kasmeroglu (Daniel.Kasmeroglu@Kasisoft.net)
 */
public interface PythonProjectBuilder {

  /**
   * Establishs a project dependency to another project referred using the supplied name.
   * 
   * @param projectname
   *          The name of the project that has to be used. Neither <code>null</code> nor empty.
   * @param export
   *          <code>true</code> <=> This dependency shall be exported.
   */
  void useProject(final String projectname, final boolean export);

  /**
   * Changes the default source folder name.
   * 
   * @param sourcename
   *          The new name of the default source folder. Neither <code>null</code> nor empty.
   */
  void setSourceFolder(final String sourcename);

  /**
   * Adds another source folder to this project.
   * 
   * @param additionalfolder
   *          An additional source folder. Neither <code>null</code> nor empty.
   */
  void addSourceFolder(final String additionalfolder);

} /* ENDINTERFACE */
