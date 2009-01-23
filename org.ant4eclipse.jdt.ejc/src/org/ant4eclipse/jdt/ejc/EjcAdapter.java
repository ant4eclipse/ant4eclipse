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
package org.ant4eclipse.jdt.ejc;

import org.ant4eclipse.jdt.ejc.internal.tools.EjcAdapterImpl;

/**
 * <p>
 * Instances of type {@link EjcAdapter} can be used to compile a given source tree with the eclipse java compiler.
 * </p>
 * 
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 */
public interface EjcAdapter {

  /** constants to describe PROJECT type */
  public static final byte PROJECT = 1;

  /** constants to describe LIBRARY type */
  public static final byte LIBRARY = 2;

  /**
   * <p>
   * Performs a compile based on the given {@link CompileJobDescription}.
   * </p>
   * 
   * @param description
   *          the {@link CompileJobDescription} that describes the compile job.
   */
  public CompileJobResult compile(CompileJobDescription description);

  /**
   * <p>
   * Inner factory to allow the creation of new {@link EjcAdapter} instances.
   * </p>
   * 
   * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
   */
  public static class Factory {

    /**
     * <p>
     * Creates a new instance of type {@link EjcAdapter}
     * </p>
     * 
     * @return a new instance of type {@link EjcAdapter}.
     */
    public static EjcAdapter create() {
      return new EjcAdapterImpl();
    }
  }
}
