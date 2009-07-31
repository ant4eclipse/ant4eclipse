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
package org.ant4eclipse.pydt.model;

/**
 * Common API used for resolved path entries.
 * 
 * @author Daniel Kasmeroglu (Daniel.Kasmeroglu@Kasisoft.net)
 */
public interface ResolvedPathEntry {

  /**
   * Returns the kind of reference established by this entry.
   * 
   * @return The kind of reference established by this entry. Not <code>null</code>.
   */
  ReferenceKind getKind();

} /* ENDINTERFACE */
