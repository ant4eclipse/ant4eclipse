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

import org.ant4eclipse.lib.core.A4ECore;
import org.ant4eclipse.lib.core.Assure;
import org.ant4eclipse.lib.core.exception.Ant4EclipseException;
import org.ant4eclipse.lib.core.logging.A4ELogging;
import org.ant4eclipse.lib.platform.model.resource.EclipseProject;
import org.ant4eclipse.lib.platform.model.resource.Workspace;
import org.ant4eclipse.lib.pydt.PydtExceptionCode;
import org.ant4eclipse.lib.pydt.internal.model.project.PythonProjectRole;
import org.ant4eclipse.lib.pydt.model.RawPathEntry;
import org.ant4eclipse.lib.pydt.model.ReferenceKind;
import org.ant4eclipse.lib.pydt.model.ResolvedContainerEntry;
import org.ant4eclipse.lib.pydt.model.ResolvedLibraryEntry;
import org.ant4eclipse.lib.pydt.model.ResolvedPathEntry;
import org.ant4eclipse.lib.pydt.model.ResolvedProjectEntry;
import org.ant4eclipse.lib.pydt.model.ResolvedRuntimeEntry;
import org.ant4eclipse.lib.pydt.model.ResolvedSourceEntry;
import org.ant4eclipse.lib.pydt.model.pyre.PythonRuntime;
import org.ant4eclipse.lib.pydt.model.pyre.PythonRuntimeRegistry;
import org.ant4eclipse.lib.pydt.tools.PathEntryRegistry;

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
  public PythonResolver(Workspace workspace, Mode mode, boolean ignoreruntimes) {
    Assure.notNull("workspace", workspace);
    this._pathregistry = A4ECore.instance().getRequiredService(PathEntryRegistry.class);
    this._runtimeregistry = A4ECore.instance().getRequiredService(PythonRuntimeRegistry.class);
    this._workspace = workspace;
    this._ignoreruntimes = ignoreruntimes;
    this._mode = mode;
    if (this._mode == null) {
      this._mode = Mode.all;
    }
  }

  /**
   * Resolves the supplied entry to get access to a source folder.
   * 
   * @param entry
   *          The unresolved entry pointing to a source folder. Not <code>null</code>.
   */
  private void resolveImpl(RawPathEntry entry) {
    Assure.notNull("entry", entry);
    ResolvedPathEntry result = this._pathregistry.getResolvedPathEntry(entry);
    if (result == null) {
      result = newResolvedEntry(entry);
    }
    if (result != null) {
      this._pathregistry.registerResolvedPathEntry(entry, result);
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
  public ResolvedPathEntry[] resolve(String projectname) {
    EclipseProject project = this._workspace.getProject(projectname);
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
  public ResolvedPathEntry[] resolve(RawPathEntry... entries) {
    Assure.notNull("entries", entries);
    List<RawPathEntry> input = new ArrayList<RawPathEntry>();
    for (RawPathEntry entry : entries) {
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
  public ResolvedPathEntry[] resolve(List<RawPathEntry> entries) {
    Assure.notNull("entries", entries);
    List<ResolvedPathEntry> list = new ArrayList<ResolvedPathEntry>();
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
  private List<RawPathEntry> filter(List<RawPathEntry> input) {
    if (this._ignoreruntimes) {
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
  private void resolve(List<ResolvedPathEntry> receiver, List<RawPathEntry> entries) {

    Set<RawPathEntry> followed = new HashSet<RawPathEntry>();
    while (!entries.isEmpty()) {

      RawPathEntry entry = entries.remove(0);
      if (!this._pathregistry.isResolved(entry)) {
        // until now it has not been resolved, so resolve and register it
        resolveImpl(entry);
      }

      // access the resolved path entry
      ResolvedPathEntry resolved = this._pathregistry.getResolvedPathEntry(entry);
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

      if (this._mode == Mode.direct) {
        // we're not interested in indirectly used entries
        continue;
      }

      if (resolved.getKind() == ReferenceKind.Project) {
        // fetch the EclipseProject instance from the workspace
        EclipseProject refproject = this._workspace.getProject(((ResolvedProjectEntry) resolved).getProjectname());
        if (this._mode == Mode.all) {
          // this mode doesn't care for the 'export' flag on path settings
          entries.addAll(0, loadEntries(refproject));
        } else if ((this._mode == Mode.exported) && entry.isExported()) {
          // just follow exported entries
          entries.addAll(0, loadEntries(refproject));
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
  private List<RawPathEntry> loadEntries(EclipseProject project) {
    List<RawPathEntry> result = new ArrayList<RawPathEntry>();
    PythonProjectRole role = project.getRole(PythonProjectRole.class);
    RawPathEntry[] entries = role.getRawPathEntries();
    for (RawPathEntry entry : entries) {
      if (entry.getKind() == ReferenceKind.Runtime) {
        if (this._ignoreruntimes) {
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
  private boolean canBeFollowed(ResolvedPathEntry entry) {
    return entry.getKind() == ReferenceKind.Project;
  }

  /**
   * Creates a new resolved record for the supplied entry.
   * 
   * @param entry
   *          The path entry which needs to be resolved. Not <code>null</code>.
   */
  private ResolvedPathEntry newResolvedEntry(RawPathEntry entry) {
    if (entry.getKind() == ReferenceKind.Container) {
      return newResolvedContainerEntry(entry);
    } else if (entry.getKind() == ReferenceKind.Library) {
      return newResolvedLibraryEntry(entry);
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
  private ResolvedContainerEntry newResolvedContainerEntry(RawPathEntry entry) {
    return new ResolvedContainerEntry(entry.getProjectname(), new File[0]);
  }

  /**
   * Creates a new record representing a library.
   * 
   * @param entry
   *          The raw entry. Not <code>null</code>.
   */
  private ResolvedLibraryEntry newResolvedLibraryEntry(RawPathEntry entry) {
    return new ResolvedLibraryEntry(entry.getProjectname(), entry.getValue());
  }

  /**
   * Creates a new record representing a project.
   * 
   * @param entry
   *          The raw entry. Not <code>null</code>.
   */
  private ResolvedProjectEntry newResolvedProjectEntry(RawPathEntry entry) {
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
  private ResolvedSourceEntry newResolvedSourceEntry(RawPathEntry entry) {
    return new ResolvedSourceEntry(entry.getProjectname(), entry.getValue());
  }

  /**
   * Creates a new record representing a single runtime..
   * 
   * @param entry
   *          The raw entry. Not <code>null</code>.
   */
  private ResolvedRuntimeEntry newResolvedRuntimeEntry(RawPathEntry entry) {
    String value = entry.getValue();
    PythonRuntime runtime = null;
    if (value.length() == 0) {
      // use the default runtime
      runtime = this._runtimeregistry.getRuntime();
    } else {
      // use the selected runtime
      runtime = this._runtimeregistry.getRuntime(value);
    }
    if (runtime == null) {
      // now runtime available
      throw new Ant4EclipseException(PydtExceptionCode.UNKNOWN_PYTHON_RUNTIME, value);
    }
    return new ResolvedRuntimeEntry(entry.getProjectname(), runtime.getVersion(), runtime.getLibraries());
  }

} /* ENDCLASS */
