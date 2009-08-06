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
import org.ant4eclipse.pydt.model.ResolvedOutputEntry;
import org.ant4eclipse.pydt.tools.PathEntryRegistry;

/**
 * Resolver for output path entries.
 * 
 * @author Daniel Kasmeroglu (Daniel.Kasmeroglu@Kasisoft.net)
 */
public class PydtOutputpathResolver {

  private PathEntryRegistry _registry;

  /**
   * Initialises this resolve for output folders.
   */
  public PydtOutputpathResolver() {
    _registry = ServiceRegistry.instance().getService(PathEntryRegistry.class);
  }

  /**
   * Resolves the supplied entry to get access to an output folder.
   * 
   * @param entry
   *          The unresolved entry pointing to an output folder. Not <code>null</code>.
   * 
   * @return A resolved entry identifying an output folder. Not <code>null</code>.
   */
  public ResolvedOutputEntry resolve(final RawPathEntry entry) {
    Assert.notNull(entry);
    Assert.assertTrue(entry.getKind() == ReferenceKind.Output,
        "Only parameters referring to ouput pathes are allowed !");
    ResolvedOutputEntry result = (ResolvedOutputEntry) _registry.getResolvedPathEntry(entry);
    if (result == null) {
      result = newResolvedOutputEntry(entry);
      _registry.registerResolvedPathEntry(entry, result);
    }
    return result;
  }

  /**
   * Resolves the supplied entries to get access to the output folders.
   * 
   * @param entries
   *          The unresolved entries pointing to the output folders. Not <code>null</code>.
   * 
   * @return The resolved entries identifying the output folders. Not <code>null</code>.
   */
  public ResolvedOutputEntry[] resolve(final RawPathEntry[] entries) {
    Assert.notNull(entries);
    final ResolvedOutputEntry[] result = new ResolvedOutputEntry[entries.length];
    for (int i = 0; i < entries.length; i++) {
      result[i] = resolve(entries[i]);
    }
    return result;
  }

  /**
   * Creates a new record representing an output folder.
   * 
   * @param entry
   *          The raw entry. Not <code>null</code>.
   * 
   * @return A newly created record used to represent an output folder. Not <code>null</code>.
   */
  private ResolvedOutputEntry newResolvedOutputEntry(final RawPathEntry entry) {
    return new ResolvedOutputEntry(entry.getValue());
  }

} /* ENDCLASS */
