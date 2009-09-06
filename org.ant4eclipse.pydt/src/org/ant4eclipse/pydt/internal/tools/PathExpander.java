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

import org.ant4eclipse.platform.model.resource.EclipseProject;
import org.ant4eclipse.platform.model.resource.Workspace;

import org.ant4eclipse.pydt.model.ReferenceKind;
import org.ant4eclipse.pydt.model.ResolvedContainerEntry;
import org.ant4eclipse.pydt.model.ResolvedLibraryEntry;
import org.ant4eclipse.pydt.model.ResolvedOutputEntry;
import org.ant4eclipse.pydt.model.ResolvedPathEntry;
import org.ant4eclipse.pydt.model.ResolvedProjectEntry;
import org.ant4eclipse.pydt.model.ResolvedRuntimeEntry;
import org.ant4eclipse.pydt.model.ResolvedSourceEntry;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Expands abstract path entries into real filesystem pathes.
 * 
 * @author Daniel Kasmeroglu (Daniel.Kasmeroglu@Kasisoft.net)
 */
public class PathExpander {

  private Workspace _workspace;

  /**
   * Initialises this path expander.
   * 
   * @param workspace
   *          The Workspace instance currently used for the path expansion. Not <code>null</code>.
   */
  public PathExpander(final Workspace workspace) {
    Assert.notNull(workspace);
    _workspace = workspace;
  }

  /**
   * Translates the resolved path entries into filesystem pathes.
   * 
   * @param entries
   *          A list of resolved path entries which have to be translated into filesystem pathes. Not <code>null</code>.
   * @param pathstyle
   *          The style used to calculate the pathes. Not <code>null</code>.
   * 
   * @return A list of resolved filesystem locations. Not <code>null</code>.
   */
  public File[] expand(final ResolvedPathEntry[] entries, final EclipseProject.PathStyle pathstyle) {
    Assert.notNull(entries);
    Assert.notNull(pathstyle);
    List<File> list = new ArrayList<File>();
    for (ResolvedPathEntry entry : entries) {
      expand(list, entry, pathstyle);
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
   * @param pathstyle
   *          The style used to calculate the pathes. Not <code>null</code>.
   */
  private void expand(final List<File> receiver, final ResolvedPathEntry entry, final EclipseProject.PathStyle pathstyle) {
    if (entry.getKind() == ReferenceKind.Container) {
      expandContainer(receiver, (ResolvedContainerEntry) entry, pathstyle);
    } else if (entry.getKind() == ReferenceKind.Library) {
      expandLibrary(receiver, (ResolvedLibraryEntry) entry, pathstyle);
    } else if (entry.getKind() == ReferenceKind.Output) {
      expandOutput(receiver, (ResolvedOutputEntry) entry, pathstyle);
    } else if (entry.getKind() == ReferenceKind.Project) {
      expandProject(receiver, (ResolvedProjectEntry) entry, pathstyle);
    } else if (entry.getKind() == ReferenceKind.Runtime) {
      expandRuntime(receiver, (ResolvedRuntimeEntry) entry, pathstyle);
    } else /* if (entry.getKind() == ReferenceKind.Source) */{
      expandSource(receiver, (ResolvedSourceEntry) entry, pathstyle);
    }
  }

  /**
   * @see #expand(ResolvedPathEntry[], EclipseProject.PathStyle)
   */
  private void expandSource(List<File> receiver, ResolvedSourceEntry entry, final EclipseProject.PathStyle pathstyle) {
    File sourcefolder = null;
    if (entry.getFolder() == null) {
      sourcefolder = getProject(entry).getFolder(pathstyle);
    } else {
      sourcefolder = getProject(entry).getChild(entry.getFolder(), pathstyle);
    }
    receiver.add(sourcefolder);
  }

  /**
   * @see #expand(ResolvedPathEntry[], EclipseProject.PathStyle)
   */
  private void expandRuntime(final List<File> receiver, final ResolvedRuntimeEntry entry,
      final EclipseProject.PathStyle pathstyle) {
    final File[] libraries = entry.getLibraries();
    for (File lib : libraries) {
      receiver.add(lib);
    }
  }

  /**
   * @see #expand(ResolvedPathEntry[], EclipseProject.PathStyle)
   */
  private void expandProject(final List<File> receiver, final ResolvedProjectEntry entry,
      final EclipseProject.PathStyle pathstyle) {
    if (entry.getProjectname().equals(entry.getOwningProjectname())) {
      receiver.add(getProject(entry).getFolder(pathstyle));
    } else {
      final EclipseProject otherproject = _workspace.getProject(entry.getProjectname());
      receiver.add(otherproject.getFolder(pathstyle));
    }
  }

  /**
   * @see #expand(ResolvedPathEntry[], EclipseProject.PathStyle)
   */
  private void expandOutput(final List<File> receiver, final ResolvedOutputEntry entry,
      final EclipseProject.PathStyle pathstyle) {
    File outputfolder = null;
    if (entry.getFolder() == null) {
      outputfolder = getProject(entry).getFolder(pathstyle);
    } else {
      outputfolder = getProject(entry).getChild(entry.getFolder(), pathstyle);
    }
    receiver.add(outputfolder);
  }

  /**
   * @see #expand(ResolvedPathEntry[], EclipseProject.PathStyle)
   */
  private void expandLibrary(final List<File> receiver, final ResolvedLibraryEntry entry,
      final EclipseProject.PathStyle pathstyle) {
    File file = new File(entry.getLocation());
    if (!file.isAbsolute()) {
      file = getProject(entry).getChild(entry.getLocation(), pathstyle);
    }
    receiver.add(file);
  }

  /**
   * @see #expand(ResolvedPathEntry[], EclipseProject.PathStyle)
   */
  private void expandContainer(final List<File> receiver, final ResolvedContainerEntry entry,
      final EclipseProject.PathStyle pathstyle) {
    final File[] pathes = entry.getPathes();
    for (File path : pathes) {
      receiver.add(path);
    }
  }

  /**
   * Returns the project associated with the supplied entry.
   * 
   * @param entry
   *          The entry which project has to be returned. Not <code>null</code>.
   * 
   * @return The project associated with the supplied entry. Not <code>null</code>.
   */
  private EclipseProject getProject(final ResolvedPathEntry entry) {
    return _workspace.getProject(entry.getOwningProjectname());
  }

} /* ENDCLASS */
