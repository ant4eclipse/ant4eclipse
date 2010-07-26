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
package org.ant4eclipse.lib.jdt.internal.tools.classpathentry;

import org.ant4eclipse.lib.jdt.model.ClasspathEntry;
import org.ant4eclipse.lib.jdt.model.project.RawClasspathEntry;
import org.ant4eclipse.lib.jdt.tools.ResolvedClasspathEntry;
import org.ant4eclipse.lib.jdt.tools.container.ClasspathResolverContext;
import org.ant4eclipse.lib.platform.model.resource.EclipseProject;

import java.io.File;

/**
 * <p>
 * The {@link ContainerClasspathEntryResolver} is responsible for resolving library class path entries (class path
 * entries of kind 'lib' where path doesn't point to an eclipse project, e.g. &lt;classpathentry kind="lib"
 * path="/org.ant4eclipse.external/libs/ant/apache-ant-1.7.1.jar"
 * sourcepath="/org.ant4eclipse.external/libs/ant/apache-ant-1.7.1-src.zip"/&gt;).
 * </p>
 * 
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
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
   * {@inheritDoc}
   */
  public boolean canResolve(ClasspathEntry entry) {
    return isRawClasspathEntryOfKind(entry, RawClasspathEntry.CPE_LIBRARY)
    /* || isRuntimeClasspathEntryOfKind(entry, RuntimeClasspathEntry.RCE_ARCHIVE) */;
  }

  /**
   * {@inheritDoc}
   */
  public void resolve(ClasspathEntry entry, ClasspathResolverContext context) {

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
      String[] splitted = splitHeadAndTail(entry.getPath());

      // AE-225: Classpath resolution fails if a project is added as a library
      if (splitted.length == 1) {
        EclipseProject referencedProject = context.getWorkspace().getProject(splitted[0]);
        context.addClasspathEntry(new ResolvedClasspathEntry(referencedProject.getFolder()));
      } else {
        resolveProjectRelativeResource(context.getWorkspace().getProject(splitted[0]), splitted[1], context);
      }

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
  private int getPathType(ClasspathEntry entry, ClasspathResolverContext context) {

    String entrypath = entry.getPath();

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

        String[] splitted = splitHeadAndTail(entrypath);

        // AE-225: Classpath resolution fails if a project is added as a library
        if (splitted.length == 1) {

          if (context.getWorkspace().hasProject(splitted[0])) {
            // path exists in the workspace; treat as "workspace relative path"
            return WORKSPACE_RELATIVE_PATH;
          } else {
            // path does not exist in the workspace; treat as "real" absolute path
            return ABSOLUTE_PATH;
          }

        } else if (context.getWorkspace().hasProject(splitted[0])
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
  private String[] splitHeadAndTail(String entryPath) {

    String path = entryPath;

    if (path.startsWith("/")) {
      path = path.substring(1);
    }

    String[] result;

    // AE-225: Classpath resolution fails if a project is added as a library
    if (path.indexOf("/") != -1) {
      result = new String[2];
      result[0] = path.substring(0, path.indexOf("/"));
      result[1] = path.substring(path.indexOf("/") + 1);
    } else {
      return new String[] { path };
    }

    return result;
  }
}
