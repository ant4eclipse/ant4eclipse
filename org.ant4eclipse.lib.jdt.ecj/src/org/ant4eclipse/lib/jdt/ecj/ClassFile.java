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

import org.eclipse.jdt.internal.compiler.env.AccessRestriction;
import org.eclipse.jdt.internal.compiler.env.IBinaryType;

/**
 * <p>
 * An instance of class {@link ClassFile} represents a java class file that is requested during the compilation process.
 * A {@link ClassFile} is associated with a {@link IBinaryType} and may has an {@link AccessRestriction}.
 * </p>
 * 
 * @author Gerd Wuetherich (gerd@gerd-wuetherich.de)
 * @author Nils Hartmann (nils@nilshartmann.net)
 */
public interface ClassFile extends ReferableType {

  /**
   * <p>
   * Returns the class file as an {@link IBinaryType}.
   * </p>
   * 
   * @return this class file as an {@link IBinaryType}.
   */
  IBinaryType getBinaryType();

  /**
   * <p>
   * </p>
   * 
   * @return
   */
  byte[] getBytes();
}
