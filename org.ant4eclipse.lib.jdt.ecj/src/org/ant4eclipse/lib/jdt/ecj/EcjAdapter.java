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

import org.ant4eclipse.lib.jdt.ecj.internal.tools.EcjAdapterImpl;

/**
 * <p>
 * Instances of type {@link EcjAdapter} can be used to compile a given source tree with the eclipse java compiler.
 * </p>
 * 
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 */
public interface EcjAdapter {

  /** constants to describe PROJECT type */
  byte PROJECT = 1;

  /** constants to describe LIBRARY type */
  byte LIBRARY = 2;

  /**
   * <p>
   * Performs a compile based on the given {@link CompileJobDescription}.
   * </p>
   * 
   * @param description
   *          the {@link CompileJobDescription} that describes the compile job.
   */
  CompileJobResult compile(CompileJobDescription description);

  /**
   * <p>
   * Inner factory to allow the creation of new {@link EcjAdapter} instances.
   * </p>
   * 
   * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
   */
  public static class Factory {

    /**
     * <p>
     * Creates a new instance of type {@link EcjAdapter}
     * </p>
     * 
     * @return a new instance of type {@link EcjAdapter}.
     */
    public static EcjAdapter create() {
      return new EcjAdapterImpl();
    }
  }
}
