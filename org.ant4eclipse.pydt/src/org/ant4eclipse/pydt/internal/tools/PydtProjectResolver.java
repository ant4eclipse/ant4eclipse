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
import org.ant4eclipse.core.logging.A4ELogging;
import org.ant4eclipse.core.service.ServiceRegistry;

import org.ant4eclipse.pydt.model.RawPathEntry;
import org.ant4eclipse.pydt.model.ReferenceKind;
import org.ant4eclipse.pydt.model.ResolvedProjectEntry;
import org.ant4eclipse.pydt.tools.PathEntryRegistry;

/**
 * Resolver for project entries.
 * 
 * @author Daniel Kasmeroglu (Daniel.Kasmeroglu@Kasisoft.net)
 */
public class PydtProjectResolver {

  private PathEntryRegistry _registry;

  /**
   * Initialises this resolver for python projects.
   */
  public PydtProjectResolver() {
    _registry = ServiceRegistry.instance().getService(PathEntryRegistry.class);
  }

  /**
   * Resolves the supplied entry to get access to a python project.
   * 
   * @param entry
   *          The unresolved entry pointing to a python project. Not <code>null</code>.
   * 
   * @return A resolved entry identifying an output folder. Not <code>null</code>.
   */
  public ResolvedProjectEntry resolve(final RawPathEntry entry) {
    Assert.notNull(entry);
    Assert.assertTrue(entry.getKind() == ReferenceKind.Project,
        "Only parameters referring to python projects are allowed !");
    ResolvedProjectEntry result = (ResolvedProjectEntry) _registry.getResolvedPathEntry(entry);
    if (result == null) {
      result = newResolvedProjectEntry(entry);
      _registry.registerResolvedPathEntry(entry, result);
    }
    return result;
  }

  /**
   * Resolves the supplied entries to get access to the python projects.
   * 
   * @param entries
   *          The unresolved entries pointing to the python projects. Not <code>null</code>.
   * 
   * @return The resolved entries identifying the python projects. Not <code>null</code>.
   */
  public ResolvedProjectEntry[] resolve(final RawPathEntry[] entries) {
    Assert.notNull(entries);
    final ResolvedProjectEntry[] result = new ResolvedProjectEntry[entries.length];
    for (int i = 0; i < entries.length; i++) {
      result[i] = resolve(entries[i]);
    }
    return result;
  }

  /**
   * Creates a new record representing a project.
   * 
   * @param entry
   *          The raw entry. Not <code>null</code>.
   * 
   * @return A newly created record used to represent a python project Not <code>null</code>.
   */
  private ResolvedProjectEntry newResolvedProjectEntry(final RawPathEntry entry) {
    String value = entry.getValue();
    if ((value.charAt(0) != '/') || (value.length() == 1)) {
      /** @todo [02-Aug-2009:KASI] We need to cause an exception here. */
      A4ELogging.warn("The raw projectname '%s' does not start conform to the required format '/' <identifier> !",
          value);
      return null;
    }
    return new ResolvedProjectEntry(value.substring(1));
  }

} /* ENDCLASS */
