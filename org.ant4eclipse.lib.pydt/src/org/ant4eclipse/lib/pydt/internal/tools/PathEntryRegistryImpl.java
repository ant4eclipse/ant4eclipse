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
package org.ant4eclipse.lib.pydt.internal.tools;

import org.ant4eclipse.lib.pydt.model.RawPathEntry;
import org.ant4eclipse.lib.pydt.model.ResolvedPathEntry;
import org.ant4eclipse.lib.pydt.tools.PathEntryRegistry;

import java.util.Hashtable;
import java.util.Map;

/**
 * Implementation of the path registry.
 * 
 * @author Daniel Kasmeroglu (Daniel.Kasmeroglu@Kasisoft.net)
 */
public class PathEntryRegistryImpl implements PathEntryRegistry {

  private Map<RawPathEntry,ResolvedPathEntry> _resolvedentries;

  /**
   * Initialises this registry for path entries.
   */
  public PathEntryRegistryImpl() {
    _resolvedentries = new Hashtable<RawPathEntry,ResolvedPathEntry>();
  }

  /**
   * {@inheritDoc}
   */
  // Assure.notNull( "entry", entry );
  @Override
  public boolean isResolved( RawPathEntry entry ) {
    return _resolvedentries.containsKey( entry );
  }

  /**
   * {@inheritDoc}
   */
  // Assure.notNull( "origin", origin );
  // Assure.notNull( "solution", solution );
  @Override
  public void registerResolvedPathEntry( RawPathEntry origin, ResolvedPathEntry solution ) {
    _resolvedentries.put( origin, solution );
  }

  /**
   * {@inheritDoc}
   */
  @Override
  // Assure.notNull( "entry", entry );
  public ResolvedPathEntry getResolvedPathEntry( RawPathEntry entry ) {
    return _resolvedentries.get( entry );
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Integer getPriority() {
    return null;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void reset() {
    _resolvedentries.clear();
  }

} /* ENDCLASS */
