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
   * @param entry
   *          The unresolved entry pointing to a source folder. Not <code>null</code>.
   * 
   * @return A resolved entry identifying a source folder. Not <code>null</code>.
   */
  public ResolvedPathEntry resolve(final RawPathEntry entry) {
    Assert.notNull(entry);
    ResolvedPathEntry result = _pathregistry.getResolvedPathEntry(entry);
    if (result == null) {
      result = newResolvedEntry(entry);
      _pathregistry.registerResolvedPathEntry(entry, result);
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
  public ResolvedPathEntry[] resolve(final RawPathEntry[] entries) {
    Assert.notNull(entries);
    final ResolvedPathEntry[] result = new ResolvedPathEntry[entries.length];
    for (int i = 0; i < entries.length; i++) {
      result[i] = resolve(entries[i]);
    }
    return result;
  }

  /**
   * Translates the resolved path entries into filesystem pathes.
   * 
   * @param entries
   *          A list of resolved path entries which have to be translated into filesystem pathes. Not <code>null</code>.
   * @param project
   *          The project containing these pathes. Not <code>null</code>.
   * 
   * @return A list of resolved filesystem locations. Not <code>null</code>.
   */
  public File[] expand(final ResolvedPathEntry[] entries, final EclipseProject project) {
    Assert.notNull(entries);
    List<File> list = new ArrayList<File>();
    for (ResolvedPathEntry entry : entries) {
      expand(list, entry, project);
    }
    return list.toArray(new File[list.size()]);
  }

  /**
   * Translates a resolved path entry into a filesystem path.
   * 
   * @param receiver
   *          A collecting datastructure for the results. Not <code>null</code>.
   * @param entry
   *          A resolved path entry which have to be translated into a filesystem path. Not <code>null</code>.
   * @param project
   *          The project containing these pathes. Not <code>null</code>.
   */
  private void expand(final List<File> receiver, final ResolvedPathEntry entry, final EclipseProject project) {
    if (entry.getKind() == ReferenceKind.Container) {
      expandContainer(receiver, (ResolvedContainerEntry) entry, project);
    } else if (entry.getKind() == ReferenceKind.Library) {
      expandLibrary(receiver, (ResolvedLibraryEntry) entry, project);
    } else if (entry.getKind() == ReferenceKind.Output) {
      expandOutput(receiver, (ResolvedOutputEntry) entry, project);
    } else if (entry.getKind() == ReferenceKind.Project) {
      expandProject(receiver, (ResolvedProjectEntry) entry, project);
    } else if (entry.getKind() == ReferenceKind.Runtime) {
      expandRuntime(receiver, (ResolvedRuntimeEntry) entry, project);
    } else /* if (entry.getKind() == ReferenceKind.Source) */{
      expandSource(receiver, (ResolvedSourceEntry) entry, project);
    }
  }

  /**
   * @see #expand(ResolvedPathEntry[], EclipseProject)
   */
  private void expandSource(List<File> receiver, ResolvedSourceEntry entry, EclipseProject project) {
    final File sourcefolder = project.getChild(entry.getFolder(), EclipseProject.PathStyle.ABSOLUTE);
    receiver.add(sourcefolder);
  }

  /**
   * @see #expand(ResolvedPathEntry[], EclipseProject)
   */
  private void expandRuntime(List<File> receiver, ResolvedRuntimeEntry entry, EclipseProject project) {
    final File[] libraries = entry.getLibraries();
    for (File lib : libraries) {
      receiver.add(lib);
    }
  }

  /**
   * @see #expand(ResolvedPathEntry[], EclipseProject)
   */
  private void expandProject(List<File> receiver, ResolvedProjectEntry entry, EclipseProject project) {
    if (entry.getProjectname().equals(project.getSpecifiedName())) {
      receiver.add(project.getFolder());
    } else {
      final EclipseProject otherproject = project.getWorkspace().getProject(entry.getProjectname());
      receiver.add(otherproject.getFolder());
    }
  }

  /**
   * @see #expand(ResolvedPathEntry[], EclipseProject)
   */
  private void expandOutput(List<File> receiver, ResolvedOutputEntry entry, EclipseProject project) {
    final File outputfolder = project.getChild(entry.getFolder(), EclipseProject.PathStyle.ABSOLUTE);
    receiver.add(outputfolder);
  }

  /**
   * @see #expand(ResolvedPathEntry[], EclipseProject)
   */
  private void expandLibrary(List<File> receiver, ResolvedLibraryEntry entry, EclipseProject project) {
    File file = new File(entry.getLocation());
    if (!file.isAbsolute()) {
      file = project.getChild(entry.getLocation(), EclipseProject.PathStyle.ABSOLUTE);
    }
    receiver.add(file);
  }

  /**
   * @see #expand(ResolvedPathEntry[], EclipseProject)
   */
  private void expandContainer(List<File> receiver, ResolvedContainerEntry entry, EclipseProject project) {
    final File[] pathes = entry.getPathes();
    for (File path : pathes) {
      receiver.add(path);
    }
  }

  /**
   * Creates a new resolved record for the supplied entry.
   * 
   * @param entry
   *          The path entry which needs to be resolved. Not <code>null</code>.
   * 
   * @return A resolved entry. Not <code>null</code>.
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
   * 
   * @return A newly created record used to represent a path container. Not <code>null</code>.
   */
  private ResolvedContainerEntry newResolvedContainerEntry(final RawPathEntry entry) {
    return new ResolvedContainerEntry(new File[0]);
  }

  /**
   * Creates a new record representing a library.
   * 
   * @param entry
   *          The raw entry. Not <code>null</code>.
   * 
   * @return A newly created record used to represent a library. Not <code>null</code>.
   */
  private ResolvedLibraryEntry newResolvedLibraryEntry(final RawPathEntry entry) {
    return new ResolvedLibraryEntry(entry.getValue());
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
      throw new Ant4EclipseException(PydtExceptionCode.UNKNOWN_PYTHON_RUNTIME, value);
    }
    return new ResolvedRuntimeEntry(runtime.getVersion(), runtime.getLibraries());
  }

} /* ENDCLASS */
