/**********************************************************************
 * Copyright (c) 2005-2008 ant4eclipse project team.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Nils Hartmann, Daniel Kasmeroglu, Gerd Wuetherich
 **********************************************************************/
package org.ant4eclipse.jdt.tools.internal.classpathentry;

import java.io.File;

import org.ant4eclipse.jdt.model.ClasspathEntry;
import org.ant4eclipse.jdt.model.project.RawClasspathEntry;
import org.ant4eclipse.jdt.tools.container.ClasspathResolverContext;


/**
 * 
 */
public class LibraryClasspathEntryResolver extends AbstractClasspathEntryResolver {

  /** DOS_STYLE */
  private static final boolean DOS_STYLE               = File.pathSeparatorChar == ';';

  /** ABSOLUTE_PATH */
  private static int           ABSOLUTE_PATH           = 0;

  /** WORKSPACE_RELATIVE_PATH */
  private static int           WORKSPACE_RELATIVE_PATH = 1;

  /** PROJECT_RELATIVE_PATH */
  private static int           PROJECT_RELATIVE_PATH   = 2;

  /**
   * @see org.ant4eclipse.jdt.tools.internal.classpathentry.ClasspathEntryResolver#canResolve(org.ant4eclipse.jdt.model.ClasspathEntry)
   */
  public boolean canResolve(final ClasspathEntry entry) {
    return isRawClasspathEntryOfKind(entry, RawClasspathEntry.CPE_LIBRARY)
    /* || isRuntimeClasspathEntryOfKind(entry, RuntimeClasspathEntry.RCE_ARCHIVE) */;
  }

  /**
   * @see org.ant4eclipse.jdt.tools.internal.classpathentry.ClasspathEntryResolver#resolve(org.ant4eclipse.jdt.model.ClasspathEntry,
   *      org.ant4eclipse.jdt.tools.container.ClasspathResolverContext)
   */
  public void resolve(final ClasspathEntry entry, final ClasspathResolverContext context) {

    // do not resolve if the class path entry is not visible
    if (!isClasspathEntryVisible(entry, context)) {
      return;
    }

    // PROJECT_RELATIVE_PATH
    if (getPathType(entry, context) == PROJECT_RELATIVE_PATH) {
      resolveProjectRelativeResource(context.getCurrentProject(), entry.getPath(), context);
    }

    // WORKSPACE_RELATIVE_PATH
    else if (getPathType(entry, context) == WORKSPACE_RELATIVE_PATH) {
      final String[] splitted = splitHeadAndTail(entry.getPath());
      resolveProjectRelativeResource(context.getWorkspace().getProject(splitted[0]), splitted[1], context);
    }

    // ABSOULTE_PATH
    else if (getPathType(entry, context) == ABSOLUTE_PATH) {
      resolveAbsoluteResource(entry.getPath(), context);
    }
  }

  /**
   * @param entry
   * @return
   */
  private int getPathType(final ClasspathEntry entry, final ClasspathResolverContext context) {

    final String entrypath = entry.getPath();

    // dos file system
    if (DOS_STYLE) {
      if (new File(entrypath).isAbsolute()) {
        // if ant4eclipse runs on a dos-based filesystem, an absolute
        // path is always a "real" absolute path on the filesystem
        return ABSOLUTE_PATH;
      } else {
        // if it's not a "real" dos-absolute path, it can either start with a
        // "/" which means the path is "workspace relative" or it starts without
        // leading "/" which means the path is project relative
        if (entrypath.startsWith("/")) {
          // workspace relative
          return WORKSPACE_RELATIVE_PATH;
        } else {
          // project relative
          return PROJECT_RELATIVE_PATH;
        }
      }
    }

    // unix file system
    else {

      if (entrypath.startsWith("/")) {
        // on unix a path starting with "/" has two meanings:
        // - it can be a "real" absolute path pointing to a location
        // outside of the workspace
        // - it can be a workspace relative path
        // We consider a path to be a "workspace relative path" if the
        // path exist inside the workspace

        final String[] splitted = splitHeadAndTail(entrypath);

        if (context.getWorkspace().hasProject(splitted[0])
            && context.getWorkspace().getProject(splitted[0]).hasChild(splitted[1])) {
          // path exists in the workspace; treat as "workspace relative path"
          return WORKSPACE_RELATIVE_PATH;
        } else {

          // path does not exist in the workspace; treat as "real" absolute path
          return ABSOLUTE_PATH;
        }

      } else {

        // relative path (i.e. path not starting with "/") must be "project
        // relative"
        return PROJECT_RELATIVE_PATH;
      }
    }
  }

  /**
   * @param entryPath
   * @return
   */
  private String[] splitHeadAndTail(final String entryPath) {

    String path = entryPath;

    if (path.startsWith("/")) {
      path = path.substring(1);
    }

    final String[] result = new String[2];

    result[0] = path.substring(0, path.indexOf("/"));
    result[1] = path.substring(path.indexOf("/") + 1);

    return result;
  }
}
