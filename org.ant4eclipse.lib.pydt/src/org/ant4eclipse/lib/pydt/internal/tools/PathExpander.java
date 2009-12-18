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

import org.ant4eclipse.lib.core.Assure;
import org.ant4eclipse.lib.core.exception.Ant4EclipseException;
import org.ant4eclipse.lib.core.util.Utilities;
import org.ant4eclipse.lib.platform.model.resource.EclipseProject;
import org.ant4eclipse.lib.platform.model.resource.Workspace;
import org.ant4eclipse.lib.pydt.PydtExceptionCode;
import org.ant4eclipse.lib.pydt.model.ReferenceKind;
import org.ant4eclipse.lib.pydt.model.ResolvedContainerEntry;
import org.ant4eclipse.lib.pydt.model.ResolvedLibraryEntry;
import org.ant4eclipse.lib.pydt.model.ResolvedPathEntry;
import org.ant4eclipse.lib.pydt.model.ResolvedProjectEntry;
import org.ant4eclipse.lib.pydt.model.ResolvedRuntimeEntry;
import org.ant4eclipse.lib.pydt.model.ResolvedSourceEntry;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Expands abstract path entries into real filesystem pathes.
 * 
 * @author Daniel Kasmeroglu (Daniel.Kasmeroglu@Kasisoft.net)
 */
public class PathExpander {

  private Workspace      _workspace;

  private EclipseProject _project;

  /**
   * Initialises this path expander.
   * 
   * @param project
   *          The project instance currently used for the path expansion. Not <code>null</code>.
   */
  public PathExpander(EclipseProject project) {
    Assure.notNull(project);
    this._project = project;
    this._workspace = this._project.getWorkspace();
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
  public File[] expand(ResolvedPathEntry[] entries, EclipseProject.PathStyle pathstyle) {
    Assure.notNull(entries);
    Assure.notNull(pathstyle);
    List<File> list = new ArrayList<File>();
    for (ResolvedPathEntry entry : entries) {
      expand(list, entry);
    }
    if (pathstyle != EclipseProject.PathStyle.ABSOLUTE) {
      for (int i = 0; i < list.size(); i++) {
        list.set(i, getRelative(list.get(i), pathstyle));
      }
    }
    return list.toArray(new File[list.size()]);
  }

  /**
   * Calculates the relative path for a file. The path is considered relative to the project used for the calculation.
   * Since it isn't always possible to calculate the
   * 
   * @param file
   *          The filesystem location which needs to be <i>reached</i>. Not <code>null</code>.
   * @param pathstyle
   *          A constant used to identify the desired path style. Definitely not absolute. Not <code>null</code>.
   * 
   * @return The relative filesystem location description as desired. Not <code>null</code>.
   */
  private File getRelative(File file, EclipseProject.PathStyle pathstyle) {
    String relativepath = Utilities.calcRelative(this._project.getFolder(), file);
    if (relativepath == null) {
      throw new Ant4EclipseException(PydtExceptionCode.NO_RELATIVE_PATH, this._project.getFolder(), file);
    }
    if (pathstyle == EclipseProject.PathStyle.PROJECT_RELATIVE_WITH_LEADING_PROJECT_NAME) {
      if (relativepath.length() == 0) {
        return new File(this._project.getFolderName());
      } else {
        return new File(this._project.getFolderName() + File.separator + relativepath);
      }
    } else /* if (pathstyle == EclipseProject.PathStyle.PROJECT_RELATIVE_WITHOUT_LEADING_PROJECT_NAME) */{
      if (relativepath.length() == 0) {
        return new File(".");
      } else {
        return new File(relativepath);
      }
    }
  }

  /**
   * Translates a resolved path entry into a filesystem path.
   * 
   * @param receiver
   *          A collecting datastructure for the results. Not <code>null</code>.
   * @param entry
   *          A resolved path entry which have to be translated into a filesystem path. Not <code>null</code>.
   */
  private void expand(List<File> receiver, ResolvedPathEntry entry) {
    if (entry.getKind() == ReferenceKind.Container) {
      expandContainer(receiver, (ResolvedContainerEntry) entry);
    } else if (entry.getKind() == ReferenceKind.Library) {
      expandLibrary(receiver, (ResolvedLibraryEntry) entry);
    } else if (entry.getKind() == ReferenceKind.Project) {
      expandProject(receiver, (ResolvedProjectEntry) entry);
    } else if (entry.getKind() == ReferenceKind.Runtime) {
      expandRuntime(receiver, (ResolvedRuntimeEntry) entry);
    } else /* if (entry.getKind() == ReferenceKind.Source) */{
      expandSource(receiver, (ResolvedSourceEntry) entry);
    }
  }

  /**
   * @see #expand(ResolvedPathEntry[])
   */
  private void expandSource(List<File> receiver, ResolvedSourceEntry entry) {
    File sourcefolder = null;
    if (entry.getFolder() == null) {
      sourcefolder = getProject(entry).getFolder(EclipseProject.PathStyle.ABSOLUTE);
    } else {
      sourcefolder = getProject(entry).getChild(entry.getFolder(), EclipseProject.PathStyle.ABSOLUTE);
    }
    receiver.add(sourcefolder);
  }

  /**
   * @see #expand(ResolvedPathEntry[])
   */
  private void expandRuntime(List<File> receiver, ResolvedRuntimeEntry entry) {
    File[] libraries = entry.getLibraries();
    for (File lib : libraries) {
      receiver.add(lib);
    }
  }

  /**
   * @see #expand(ResolvedPathEntry[])
   */
  private void expandProject(List<File> receiver, ResolvedProjectEntry entry) {
    if (entry.getProjectname().equals(entry.getOwningProjectname())) {
      receiver.add(getProject(entry).getFolder(EclipseProject.PathStyle.ABSOLUTE));
    } else {
      EclipseProject otherproject = this._workspace.getProject(entry.getProjectname());
      receiver.add(otherproject.getFolder(EclipseProject.PathStyle.ABSOLUTE));
    }
  }

  /**
   * @see #expand(ResolvedPathEntry[])
   */
  private void expandLibrary(List<File> receiver, ResolvedLibraryEntry entry) {
    EclipseProject project = this._workspace.getProject(entry.getOwningProjectname());
    File file = new File(entry.getLocation());
    if (!file.isAbsolute()) {
      file = project.getChild(entry.getLocation(), EclipseProject.PathStyle.ABSOLUTE);
    }
    receiver.add(file);
  }

  /**
   * @see #expand(ResolvedPathEntry[])
   */
  private void expandContainer(List<File> receiver, ResolvedContainerEntry entry) {
    File[] pathes = entry.getPathes();
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
  private EclipseProject getProject(ResolvedPathEntry entry) {
    return this._workspace.getProject(entry.getOwningProjectname());
  }

} /* ENDCLASS */
