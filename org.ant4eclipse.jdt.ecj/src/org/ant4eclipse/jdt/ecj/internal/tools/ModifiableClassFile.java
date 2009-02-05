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
package org.ant4eclipse.jdt.ecj.internal.tools;

import org.ant4eclipse.jdt.ecj.ClassFile;
import org.eclipse.jdt.internal.compiler.env.AccessRestriction;

/**
 * <p>
 * Extends the interface {@link ClassFile} so that one could set an access restriction.
 * </p>
 * 
 * @author Gerd Wuetherich (gerd@gerd-wuetherich.de)
 */
public interface ModifiableClassFile extends ClassFile {

  /**
   * <p>
   * Sets the given access restriction.
   * </p>
   * 
   * @param accessRestriction
   *          the {@link AccessRestriction}.
   */
  public void setAccessRestriction(final AccessRestriction accessRestriction);
}
