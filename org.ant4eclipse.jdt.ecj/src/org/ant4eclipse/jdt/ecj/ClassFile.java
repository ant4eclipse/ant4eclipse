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
package org.ant4eclipse.jdt.ecj;

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
public interface ClassFile {

  /**
   * <p>
   * Returns the type of the bundle this class file was loaded from. Possible values are {@link EcjAdapter#LIBRARY} and
   * {@link EcjAdapter#PROJECT}.
   * </p>
   * 
   * @return the type of the bundle this class file was loaded from.
   */
  public byte getLibraryType();

  /**
   * <p>
   * Returns of location of the bundle this class file was loaded from.
   * </p>
   * 
   * @return the location of the bundle this class file was loaded from.
   */
  public String getLibraryLocation();

  /**
   * <p>
   * Returns the class file as an {@link IBinaryType}.
   * </p>
   * 
   * @return this class file as an {@link IBinaryType}.
   */
  public IBinaryType getBinaryType();

  /**
   * <p>
   * Returns whether there exists an access restriction for this class file or not.
   * </p>
   * 
   * @return whether there exists an access restriction for this class file or not.
   */
  public boolean hasAccessRestriction();

  /**
   * <p>
   * Returns the access restriction for this class file or <code>null</code> if no access restriction exists.
   * </p>
   * 
   * @return the access restriction for this class file or <code>null</code> if no access restriction exists.
   */
  public AccessRestriction getAccessRestriction();
}
