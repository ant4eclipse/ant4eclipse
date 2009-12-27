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

import java.util.Map;

/**
 * <p>
 * A {@link CompileJobDescription} describes a compile job that can be executed with the eclipse java compiler.
 * </p>
 * 
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 */
public interface CompileJobDescription {

  /**
   * <p>
   * Returns the compiler options for the compile job.
   * </p>
   * 
   * @return the compiler options for the compile job.
   */
  Map<String, String> getCompilerOptions();

  /**
   * <p>
   * Returns an array of directories that contains the source files that should be compiled.
   * </p>
   * 
   * @return an array of directories that contains the source files that should be compiled.
   */
  SourceFile[] getSourceFiles();

  /**
   * <p>
   * Returns the {@link ClassFileLoader} that is responsible to load binary classes that are requested during the
   * compilation process.
   * </p>
   * 
   * @return the {@link ClassFileLoader} that is responsible to load binary classes that are requested during the
   *         compilation process.
   */
  ClassFileLoader getClassFileLoader();
}
