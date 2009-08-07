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
import org.ant4eclipse.core.ant.ExtendedBuildException;
import org.ant4eclipse.core.service.ServiceRegistry;

import org.ant4eclipse.pydt.PydtFailures;
import org.ant4eclipse.pydt.model.RawPathEntry;
import org.ant4eclipse.pydt.model.ReferenceKind;
import org.ant4eclipse.pydt.model.ResolvedRuntimeEntry;
import org.ant4eclipse.pydt.model.pyre.PythonRuntime;
import org.ant4eclipse.pydt.model.pyre.PythonRuntimeRegistry;
import org.ant4eclipse.pydt.tools.PathEntryRegistry;

/**
 * Resolver for python runtimes.
 * 
 * @author Daniel Kasmeroglu (Daniel.Kasmeroglu@Kasisoft.net)
 */
public class PydtRuntimeResolver {

  private PathEntryRegistry     _pathregistry;

  private PythonRuntimeRegistry _runtimeregistry;

  /**
   * Initialises this resolver for runtime entries.
   */
  public PydtRuntimeResolver() {
    _pathregistry = ServiceRegistry.instance().getService(PathEntryRegistry.class);
    _runtimeregistry = ServiceRegistry.instance().getService(PythonRuntimeRegistry.class);
  }

  /**
   * Resolves the supplied entry to get access to a python runtime.
   * 
   * @param entry
   *          The unresolved entry pointing to a python runtime. Not <code>null</code>.
   * 
   * @return A resolved entry identifying a python runtime. Not <code>null</code>.
   */
  public ResolvedRuntimeEntry resolve(final RawPathEntry entry) {
    Assert.notNull(entry);
    Assert.assertTrue(entry.getKind() == ReferenceKind.Runtime, "Only parameters referring to runtimes are allowed !");
    ResolvedRuntimeEntry result = (ResolvedRuntimeEntry) _pathregistry.getResolvedPathEntry(entry);
    if (result == null) {
      result = newResolvedRuntimeEntry(entry);
      _pathregistry.registerResolvedPathEntry(entry, result);
    }
    return result;
  }

  /**
   * Resolves the supplied entries to get access to the python runtimes.
   * 
   * @param entries
   *          The unresolved entries pointing to the python runtimes. Not <code>null</code>.
   * 
   * @return The resolved entries identifying the python runtimes. Not <code>null</code>.
   */
  public ResolvedRuntimeEntry[] resolve(final RawPathEntry[] entries) {
    Assert.notNull(entries);
    final ResolvedRuntimeEntry[] result = new ResolvedRuntimeEntry[entries.length];
    for (int i = 0; i < entries.length; i++) {
      result[i] = resolve(entries[i]);
    }
    return result;
  }

  /**
   * Creates a new record representing a single runtime..
   * 
   * @param entry
   *          The raw entry. Not <code>null</code>.
   * 
   * @return A newly created record used to represent a single runtime. Not <code>null</code>.
   */
  private ResolvedRuntimeEntry newResolvedRuntimeEntry(final RawPathEntry entry) {
    final String value = entry.getValue();
    PythonRuntime runtime = null;
    if (value.length() == 0) {
      // use the default runtime
      runtime = _runtimeregistry.getRuntime();
    } else {
      // use the selected runtime
      runtime = _runtimeregistry.getRuntime(value);
    }
    if (runtime == null) {
      // now runtime available
      throw new ExtendedBuildException(PydtFailures.UNKNOWN_PYTHON_RUNTIME, value);
    }
    return new ResolvedRuntimeEntry(runtime.getVersion(), runtime.getLibraries());
  }

} /* ENDCLASS */
