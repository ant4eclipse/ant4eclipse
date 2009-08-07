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

import org.ant4eclipse.core.Assert;
import org.ant4eclipse.core.service.ServiceRegistry;

import org.ant4eclipse.pydt.model.RawPathEntry;
import org.ant4eclipse.pydt.model.ReferenceKind;
import org.ant4eclipse.pydt.model.ResolvedSourceEntry;
import org.ant4eclipse.pydt.tools.PathEntryRegistry;

/**
 * Resolver for source path entries.
 * 
 * @author Daniel Kasmeroglu (Daniel.Kasmeroglu@Kasisoft.net)
 */
public class PydtSourcepathResolver {

  private PathEntryRegistry _registry;

  /**
   * Initialises this resolver for source folders.
   */
  public PydtSourcepathResolver() {
    _registry = ServiceRegistry.instance().getService(PathEntryRegistry.class);
  }

  /**
   * Resolves the supplied entry to get access to a source folder.
   * 
   * @param entry
   *          The unresolved entry pointing to a source folder. Not <code>null</code>.
   * 
   * @return A resolved entry identifying a source folder. Not <code>null</code>.
   */
  public ResolvedSourceEntry resolve(final RawPathEntry entry) {
    Assert.notNull(entry);
    Assert.assertTrue(entry.getKind() == ReferenceKind.Source, "Only parameters referring to sources are allowed !");
    ResolvedSourceEntry result = (ResolvedSourceEntry) _registry.getResolvedPathEntry(entry);
    if (result == null) {
      result = newResolvedSourceEntry(entry);
      _registry.registerResolvedPathEntry(entry, result);
    }
    return result;
  }

  /**
   * Resolves the supplied entries to get access to the source folders.
   * 
   * @param entries
   *          The unresolved entries pointing to the source folders. Not <code>null</code>.
   * 
   * @return The resolved entries identifying the source folders. Not <code>null</code>.
   */
  public ResolvedSourceEntry[] resolve(final RawPathEntry[] entries) {
    Assert.notNull(entries);
    final ResolvedSourceEntry[] result = new ResolvedSourceEntry[entries.length];
    for (int i = 0; i < entries.length; i++) {
      result[i] = resolve(entries[i]);
    }
    return result;
  }

  /**
   * Creates a new record representing a source folder.
   * 
   * @param entry
   *          The raw entry. Not <code>null</code>.
   * 
   * @return A newly created record used to represent a source folder. Not <code>null</code>.
   */
  private ResolvedSourceEntry newResolvedSourceEntry(final RawPathEntry entry) {
    return new ResolvedSourceEntry(entry.getValue());
  }

} /* ENDCLASS */
