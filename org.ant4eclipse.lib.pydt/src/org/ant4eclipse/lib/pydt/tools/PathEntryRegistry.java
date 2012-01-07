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
package org.ant4eclipse.lib.pydt.tools;

import org.ant4eclipse.lib.core.A4EService;
import org.ant4eclipse.lib.pydt.model.RawPathEntry;
import org.ant4eclipse.lib.pydt.model.ResolvedPathEntry;

/**
 * This registry is used to manage the path entries used within a python project. This means the {@link RawPathEntry} as
 * well as the {@link ResolvedPathEntry} instances.
 * 
 * @author Daniel Kasmeroglu (Daniel.Kasmeroglu@Kasisoft.net)
 */
public interface PathEntryRegistry extends A4EService {

  /**
   * Returns <code>true</code> if the supplied entry already has been resolved.
   * 
   * @param entry
   *          The entry to be tested. Not <code>null</code>.
   * 
   * @return <code>true</code> <=> The entry already has been resolved.
   */
  boolean isResolved( RawPathEntry entry );

  /**
   * Registers a path entry for an raw representation.
   * 
   * @param origin
   *          A descriptional unresolved entry. Not <code>null</code>.
   * @param solution
   *          A corresponding entry which has been resolved. Not <code>null</code>.
   */
  void registerResolvedPathEntry( RawPathEntry origin, ResolvedPathEntry solution );

  /**
   * Returns the resolved entry for the supplied entry if it already has been registered.
   * 
   * @param entry
   *          The unresolved entry just containing the necessary information. Not <code>null</code>.
   * 
   * @return The resolved representation of the supplied entry. Not <code>null</code> if
   *         {@link #isResolved(RawPathEntry)} returns <code>true</code>.
   */
  ResolvedPathEntry getResolvedPathEntry( RawPathEntry entry );

} /* ENDINTERFACE */
