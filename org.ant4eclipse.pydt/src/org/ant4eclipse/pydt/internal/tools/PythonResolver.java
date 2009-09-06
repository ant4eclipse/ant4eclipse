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
import org.ant4eclipse.core.exception.Ant4EclipseException;
import org.ant4eclipse.core.logging.A4ELogging;
import org.ant4eclipse.core.service.ServiceRegistry;

import org.ant4eclipse.pydt.PydtExceptionCode;
import org.ant4eclipse.pydt.model.RawPathEntry;
import org.ant4eclipse.pydt.model.ReferenceKind;
import org.ant4eclipse.pydt.model.ResolvedContainerEntry;
import org.ant4eclipse.pydt.model.ResolvedLibraryEntry;
import org.ant4eclipse.pydt.model.ResolvedOutputEntry;
import org.ant4eclipse.pydt.model.ResolvedPathEntry;
import org.ant4eclipse.pydt.model.ResolvedProjectEntry;
import org.ant4eclipse.pydt.model.ResolvedRuntimeEntry;
import org.ant4eclipse.pydt.model.ResolvedSourceEntry;
import org.ant4eclipse.pydt.model.pyre.PythonRuntime;
import org.ant4eclipse.pydt.model.pyre.PythonRuntimeRegistry;
import org.ant4eclipse.pydt.tools.PathEntryRegistry;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * General resolved for python.
 * 
 * @author Daniel Kasmeroglu (Daniel.Kasmeroglu@Kasisoft.net)
 */
public class PythonResolver {

  private PathEntryRegistry     _pathregistry;

  private PythonRuntimeRegistry _runtimeregistry;

  /**
   * Initialises this resolver.
   */
  public PythonResolver() {
    _pathregistry = ServiceRegistry.instance().getService(PathEntryRegistry.class);
    _runtimeregistry = ServiceRegistry.instance().getService(PythonRuntimeRegistry.class);
  }

  /**
   * Resolves the supplied entry to get access to a source folder.
   * 
   * @param receiver
   *          A list used to collect all resolved path entries.
   * @param entry
   *          The unresolved entry pointing to a source folder. Not <code>null</code>.
   */
  private void resolveImpl(final List<ResolvedPathEntry> receiver, final String projectname, final RawPathEntry entry) {
    Assert.notNull(entry);
    ResolvedPathEntry result = _pathregistry.getResolvedPathEntry(entry);
    if (result == null) {
      addResolvedEntry(receiver, projectname, entry);
    }
  }

  /**
   * Resolves the supplied entries to get access to the source folders.
   * 
   * @param entries
   *          The unresolved entries pointing to the source folders. Not <code>null</code>.
   * 
   * @return The resolved entries identifying the source folders. Not <code>null</code>.
   */
  public ResolvedPathEntry[] resolve(final String projectname, final RawPathEntry... entries) {
    Assert.notNull(entries);
    final List<ResolvedPathEntry> list = new ArrayList<ResolvedPathEntry>();
    for (int i = 0; i < entries.length; i++) {
      resolveImpl(list, projectname, entries[i]);
    }
    return list.toArray(new ResolvedPathEntry[list.size()]);
  }

  /**
   * Creates a new resolved record for the supplied entry.
   * 
   * @param receiver
   *          A list used to collect all resolved path entries.
   * @param entry
   *          The path entry which needs to be resolved. Not <code>null</code>.
   */
  private void addResolvedEntry(final List<ResolvedPathEntry> receiver, final String projectname,
      final RawPathEntry entry) {
    if (entry.getKind() == ReferenceKind.Container) {
      addResolvedContainerEntry(receiver, projectname, entry);
    } else if (entry.getKind() == ReferenceKind.Library) {
      addResolvedLibraryEntry(receiver, projectname, entry);
    } else if (entry.getKind() == ReferenceKind.Output) {
      addResolvedOutputEntry(receiver, projectname, entry);
    } else if (entry.getKind() == ReferenceKind.Project) {
      addResolvedProjectEntry(receiver, projectname, entry);
    } else if (entry.getKind() == ReferenceKind.Runtime) {
      addResolvedRuntimeEntry(receiver, projectname, entry);
    } else /* if (entry.getKind() == ReferenceKind.Source) */{
      addResolvedSourceEntry(receiver, projectname, entry);
    }
  }

  /**
   * Creates a new record representing a path container.
   * 
   * @param receiver
   *          A list used to collect all resolved path entries.
   * @param entry
   *          The raw entry. Not <code>null</code>.
   */
  private void addResolvedContainerEntry(final List<ResolvedPathEntry> receiver, final String projectname,
      final RawPathEntry entry) {
    final ResolvedContainerEntry result = new ResolvedContainerEntry(projectname, new File[0]);
    _pathregistry.registerResolvedPathEntry(entry, result);
    receiver.add(result);
  }

  /**
   * Creates a new record representing a library.
   * 
   * @param receiver
   *          A list used to collect all resolved path entries.
   * @param entry
   *          The raw entry. Not <code>null</code>.
   */
  private void addResolvedLibraryEntry(final List<ResolvedPathEntry> receiver, final String projectname,
      final RawPathEntry entry) {
    final ResolvedLibraryEntry result = new ResolvedLibraryEntry(projectname, entry.getValue());
    _pathregistry.registerResolvedPathEntry(entry, result);
    receiver.add(result);
  }

  /**
   * Creates a new record representing an output folder.
   * 
   * @param receiver
   *          A list used to collect all resolved path entries.
   * @param entry
   *          The raw entry. Not <code>null</code>.
   */
  private void addResolvedOutputEntry(final List<ResolvedPathEntry> receiver, final String projectname,
      final RawPathEntry entry) {
    final ResolvedOutputEntry result = new ResolvedOutputEntry(projectname, entry.getValue());
    _pathregistry.registerResolvedPathEntry(entry, result);
    receiver.add(result);
  }

  /**
   * Creates a new record representing a project.
   * 
   * @param receiver
   *          A list used to collect all resolved path entries.
   * @param entry
   *          The raw entry. Not <code>null</code>.
   */
  private void addResolvedProjectEntry(final List<ResolvedPathEntry> receiver, final String projectname,
      final RawPathEntry entry) {
    String value = entry.getValue();
    if ((value.charAt(0) != '/') || (value.length() == 1)) {
      /** @todo [02-Aug-2009:KASI] We need to cause an exception here. */
      A4ELogging.warn("The raw projectname '%s' does not start conform to the required format '/' <identifier> !",
          value);
      return;
    }
    final ResolvedProjectEntry result = new ResolvedProjectEntry(projectname, value.substring(1));
    _pathregistry.registerResolvedPathEntry(entry, result);
    receiver.add(result);
  }

  /**
   * Creates a new record representing a source folder.
   * 
   * @param receiver
   *          A list used to collect all resolved path entries.
   * @param entry
   *          The raw entry. Not <code>null</code>.
   */
  private void addResolvedSourceEntry(final List<ResolvedPathEntry> receiver, final String projectname,
      final RawPathEntry entry) {
    final ResolvedSourceEntry result = new ResolvedSourceEntry(projectname, entry.getValue());
    _pathregistry.registerResolvedPathEntry(entry, result);
    receiver.add(result);
  }

  /**
   * Creates a new record representing a single runtime..
   * 
   * @param receiver
   *          A list used to collect all resolved path entries.
   * @param entry
   *          The raw entry. Not <code>null</code>.
   */
  private void addResolvedRuntimeEntry(final List<ResolvedPathEntry> receiver, final String projectname,
      final RawPathEntry entry) {
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
      throw new Ant4EclipseException(PydtExceptionCode.UNKNOWN_PYTHON_RUNTIME, value);
    }
    final ResolvedRuntimeEntry result = new ResolvedRuntimeEntry(projectname, runtime.getVersion(), runtime
        .getLibraries());
    _pathregistry.registerResolvedPathEntry(entry, result);
    receiver.add(result);
  }

} /* ENDCLASS */
