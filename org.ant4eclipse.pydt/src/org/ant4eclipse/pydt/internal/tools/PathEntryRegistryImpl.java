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
package org.ant4eclipse.pydt.internal.tools;

import org.ant4eclipse.lib.core.Assure;
import org.ant4eclipse.pydt.model.RawPathEntry;
import org.ant4eclipse.pydt.model.ResolvedPathEntry;
import org.ant4eclipse.pydt.tools.PathEntryRegistry;

import java.util.Hashtable;
import java.util.Map;

/**
 * Implementation of the path registry.
 * 
 * @author Daniel Kasmeroglu (Daniel.Kasmeroglu@Kasisoft.net)
 */
public class PathEntryRegistryImpl implements PathEntryRegistry {

  private Map<RawPathEntry, ResolvedPathEntry> _resolvedentries;

  /**
   * Initialises this registry for path entries.
   */
  public PathEntryRegistryImpl() {
    this._resolvedentries = new Hashtable<RawPathEntry, ResolvedPathEntry>();
  }

  /**
   * {@inheritDoc}
   */
  public boolean isResolved(RawPathEntry entry) {
    Assure.notNull(entry);
    return this._resolvedentries.containsKey(entry);
  }

  /**
   * {@inheritDoc}
   */
  public void registerResolvedPathEntry(RawPathEntry origin, ResolvedPathEntry solution) {
    Assure.notNull(origin);
    Assure.notNull(solution);
    this._resolvedentries.put(origin, solution);
  }

  /**
   * {@inheritDoc}
   */
  public ResolvedPathEntry getResolvedPathEntry(RawPathEntry entry) {
    Assure.notNull(entry);
    return this._resolvedentries.get(entry);
  }

} /* ENDCLASS */
