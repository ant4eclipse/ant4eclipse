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
package org.ant4eclipse.lib.jdt.ecj;

import org.eclipse.jdt.core.compiler.CategorizedProblem;

import java.io.File;
import java.util.Map;

/**
 * <p>
 * The {@link CompileJobResult} represents a compile job result.
 * </p>
 * 
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 */
public interface CompileJobResult {

  /**
   * <p>
   * Returns <code>true</code>, if the compile job was successful, <code>false</code> otherwise.
   * </p>
   * 
   * @return <code>true</code>, if the compile job was successful, <code>false</code> otherwise.
   */
  boolean succeeded();

  /**
   * <p>
   * Returns the {@link CategorizedProblem CategorizedProblems}.
   * </p>
   * 
   * @return the {@link CategorizedProblem CategorizedProblems}.
   */
  CategorizedProblem[] getCategorizedProblems();

  /**
   * Returns a map for the class files that have been compiled.
   * 
   * @return A map for the class files that have been compiled. Not <code>null</code>.
   */
  Map<String,File> getCompiledClassFiles();

} /* ENDINTERFACE */
