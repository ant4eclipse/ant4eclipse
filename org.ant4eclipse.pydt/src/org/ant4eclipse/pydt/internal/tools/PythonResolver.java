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

import org.ant4eclipse.platform.model.resource.EclipseProject;
import org.ant4eclipse.platform.model.resource.Workspace;

import org.ant4eclipse.pydt.PydtExceptionCode;
import org.ant4eclipse.pydt.internal.model.project.PythonProjectRole;
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
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * General resolved for python.
 * 
 * @author Daniel Kasmeroglu (Daniel.Kasmeroglu@Kasisoft.net)
 */
public class PythonResolver {

  public enum Mode {
    all, exported, direct
  }

  private PathEntryRegistry     _pathregistry;

  private PythonRuntimeRegistry _runtimeregistry;

  private Workspace             _workspace;

  private Mode                  _mode;

  private boolean               _ignoreruntimes;

  /**
   * Initialises this resolver.
   * 
   * @param workspace
   *          The Workspace instance currently used for the resolving. Not <code>null</code>.
   * @param mode
   *          The resolving mode. <code>null</code> means Mode#all.
   * @param ignoreruntimes
   *          <code>true</code> <=> Runtimes have to be ignored.
   */
  public PythonResolver(final Workspace workspace, final Mode mode, final boolean ignoreruntimes) {
    Assert.notNull(workspace);
    _pathregistry = ServiceRegistry.instance().getService(PathEntryRegistry.class);
    _runtimeregistry = ServiceRegistry.instance().getService(PythonRuntimeRegistry.class);
    _workspace = workspace;
    _ignoreruntimes = ignoreruntimes;
    _mode = mode;
    if (_mode == null) {
      _mode = Mode.all;
    }
  }

  /**
   * Resolves the supplied entry to get access to a source folder.
   * 
   * @param entry
   *          The unresolved entry pointing to a source folder. Not <code>null</code>.
   */
  private void resolveImpl(final RawPathEntry entry) {
    Assert.notNull(entry);
    ResolvedPathEntry result = _pathregistry.getResolvedPathEntry(entry);
    if (result == null) {
      result = newResolvedEntry(entry);
    }
    if (result != null) {
      _pathregistry.registerResolvedPathEntry(entry, result);
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
  public ResolvedPathEntry[] resolve(final String projectname) {
    final EclipseProject project = _workspace.getProject(projectname);
    return resolve(loadEntries(project));
  }

  /**
   * Resolves the supplied entries to get access to the source folders.
   * 
   * @param entries
   *          The unresolved entries pointing to the source folders. Not <code>null</code>.
   * 
   * @return The resolved entries identifying the source folders. Not <code>null</code>.
   */
  public ResolvedPathEntry[] resolve(final RawPathEntry... entries) {
    Assert.notNull(entries);
    final List<RawPathEntry> input = new ArrayList<RawPathEntry>();
    for (final RawPathEntry entry : entries) {
      input.add(entry);
    }
    return resolve(input);
  }

  /**
   * Resolves the supplied entries to get access to the source folders.
   * 
   * @param entries
   *          The unresolved entries pointing to the source folders. Not <code>null</code>.
   * 
   * @return The resolved entries identifying the source folders. Not <code>null</code>.
   */
  public ResolvedPathEntry[] resolve(final List<RawPathEntry> entries) {
    Assert.notNull(entries);
    final List<ResolvedPathEntry> list = new ArrayList<ResolvedPathEntry>();
    resolve(list, filter(entries));
    return list.toArray(new ResolvedPathEntry[list.size()]);
  }

  /**
   * Filters entries that don't need to be processed due to the current setup.
   * 
   * @param input
   *          A list of entries that need to be filtered. Not <code>null</code>.
   * 
   * @return A list only containing the entries that need to be processed. Not <code>null</code>.
   */
  private List<RawPathEntry> filter(final List<RawPathEntry> input) {
    if (_ignoreruntimes) {
      List<RawPathEntry> result = new ArrayList<RawPathEntry>();
      for (int i = 0; i < input.size(); i++) {
        if (input.get(i).getKind() != ReferenceKind.Runtime) {
          result.add(input.get(i));
        }
      }
      return result;
    } else {
      return input;
    }
  }

  /**
   * Performs the actual resolving process.
   * 
   * @param receiver
   *          The list used to collect all resolved entries. Not <code>null</code>.
   * @param entries
   *          The entries that need to be processed. Not <code>null</code>.
   */
  private void resolve(final List<ResolvedPathEntry> receiver, final List<RawPathEntry> entries) {

    final Set<RawPathEntry> followed = new HashSet<RawPathEntry>();
    while (!entries.isEmpty()) {

      final RawPathEntry entry = entries.remove(0);
      if (!_pathregistry.isResolved(entry)) {
        // until now it has not been resolved, so resolve and register it
        resolveImpl(entry);
      }

      // access the resolved path entry
      final ResolvedPathEntry resolved = _pathregistry.getResolvedPathEntry(entry);
      if (!receiver.contains(resolved)) {
        receiver.add(resolved);
      }

      if (!canBeFollowed(resolved)) {
        // the entry cannot be refined so there's no more to do
        continue;
      }

      if (followed.contains(entry)) {
        // this one already has been followed
        continue;
      }

      followed.add(entry);

      if (_mode == Mode.direct) {
        // we're not interested in indirectly used entries
        continue;
      }

      if (resolved.getKind() == ReferenceKind.Project) {
        // fetch the EclipseProject instance from the workspace
        final EclipseProject refproject = _workspace.getProject(((ResolvedProjectEntry) resolved).getProjectname());
        if (_mode == Mode.all) {
          // this mode doesn't care for the 'export' flag on path settings
          entries.addAll(loadEntries(refproject));
        } else if ((_mode == Mode.exported) && entry.isExported()) {
          // just follow exported entries
          entries.addAll(loadEntries(refproject));
        }
      }

    }
  }

  /**
   * Returns a list of raw path entries from the supplied project.
   * 
   * @param project
   *          The project which raw path entries have to be returned. Not <code>null</code>.
   * 
   * @return A list of raw path entries. Not <code>null</code>.
   */
  private List<RawPathEntry> loadEntries(final EclipseProject project) {
    final List<RawPathEntry> result = new ArrayList<RawPathEntry>();
    final PythonProjectRole role = (PythonProjectRole) project.getRole(PythonProjectRole.class);
    final RawPathEntry[] entries = role.getRawPathEntries();
    for (RawPathEntry entry : entries) {
      if (entry.getKind() == ReferenceKind.Runtime) {
        if (_ignoreruntimes) {
          continue;
        }
      }
      result.add(entry);
    }
    return result;
  }

  /**
   * Returns <code>true</code> if the supplied entry can be refined.
   * 
   * @param entry
   *          The entry that can be refined. Not <code>null</code>.
   * 
   * @return <code>true</code> <=> The entry could be refined.
   */
  private boolean canBeFollowed(final ResolvedPathEntry entry) {
    return entry.getKind() == ReferenceKind.Project;
  }

  /**
   * Creates a new resolved record for the supplied entry.
   * 
   * @param entry
   *          The path entry which needs to be resolved. Not <code>null</code>.
   */
  private ResolvedPathEntry newResolvedEntry(final RawPathEntry entry) {
    if (entry.getKind() == ReferenceKind.Container) {
      return newResolvedContainerEntry(entry);
    } else if (entry.getKind() == ReferenceKind.Library) {
      return newResolvedLibraryEntry(entry);
    } else if (entry.getKind() == ReferenceKind.Output) {
      return newResolvedOutputEntry(entry);
    } else if (entry.getKind() == ReferenceKind.Project) {
      return newResolvedProjectEntry(entry);
    } else if (entry.getKind() == ReferenceKind.Runtime) {
      return newResolvedRuntimeEntry(entry);
    } else /* if (entry.getKind() == ReferenceKind.Source) */{
      return newResolvedSourceEntry(entry);
    }
  }

  /**
   * Creates a new record representing a path container.
   * 
   * @param entry
   *          The raw entry. Not <code>null</code>.
   */
  private ResolvedContainerEntry newResolvedContainerEntry(final RawPathEntry entry) {
    return new ResolvedContainerEntry(entry.getProjectname(), new File[0]);
  }

  /**
   * Creates a new record representing a library.
   * 
   * @param entry
   *          The raw entry. Not <code>null</code>.
   */
  private ResolvedLibraryEntry newResolvedLibraryEntry(final RawPathEntry entry) {
    return new ResolvedLibraryEntry(entry.getProjectname(), entry.getValue());
  }

  /**
   * Creates a new record representing an output folder.
   * 
   * @param entry
   *          The raw entry. Not <code>null</code>.
   */
  private ResolvedOutputEntry newResolvedOutputEntry(final RawPathEntry entry) {
    return new ResolvedOutputEntry(entry.getProjectname(), entry.getValue());
  }

  /**
   * Creates a new record representing a project.
   * 
   * @param entry
   *          The raw entry. Not <code>null</code>.
   */
  private ResolvedProjectEntry newResolvedProjectEntry(final RawPathEntry entry) {
    String value = entry.getValue();
    if ((value.charAt(0) != '/') || (value.length() == 1)) {
      /** @todo [02-Aug-2009:KASI] We need to cause an exception here. */
      A4ELogging.warn("The raw projectname '%s' does not start conform to the required format '/' <identifier> !",
          value);
      return null;
    }
    return new ResolvedProjectEntry(entry.getProjectname(), value.substring(1));
  }

  /**
   * Creates a new record representing a source folder.
   * 
   * @param entry
   *          The raw entry. Not <code>null</code>.
   */
  private ResolvedSourceEntry newResolvedSourceEntry(final RawPathEntry entry) {
    return new ResolvedSourceEntry(entry.getProjectname(), entry.getValue());
  }

  /**
   * Creates a new record representing a single runtime..
   * 
   * @param entry
   *          The raw entry. Not <code>null</code>.
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
      throw new Ant4EclipseException(PydtExceptionCode.UNKNOWN_PYTHON_RUNTIME, value);
    }
    return new ResolvedRuntimeEntry(entry.getProjectname(), runtime.getVersion(), runtime.getLibraries());
  }

} /* ENDCLASS */
