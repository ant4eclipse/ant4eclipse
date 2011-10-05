/**********************************************************************
 * Copyright (c) 2005-2011 ant4eclipse project team.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Nils Hartmann, Daniel Kasmeroglu, Gerd Wuetherich
 **********************************************************************/
package org.ant4eclipse.lib.jdt;

import java.io.File;

import org.ant4eclipse.lib.platform.model.resource.Workspace;

/**
 * In Eclipse classpath and UserLibrary files paths can be specified absolute, workspace- or project relative.
 *
 * <p>
 * This util class provides a method to determine what type a path has
 *
 * @author Nils Hartmann
 *
 */
public class EclipsePathUtil {

  /** DOS_STYLE */
  private static final boolean DOS_STYLE               = File.pathSeparatorChar == ';';

  /** ABSOLUTE_PATH */
  public static int            ABSOLUTE_PATH           = 0;

  /** WORKSPACE_RELATIVE_PATH */
  public static int            WORKSPACE_RELATIVE_PATH = 1;

  /** PROJECT_RELATIVE_PATH */
  public static int            PROJECT_RELATIVE_PATH   = 2;

  /**
   * @param entrypath
   *          the path to be resolved
   * @param workspace
   *          the workspace against the path is resolved. Might be null, in that case always ABSOLUTE_PATH is returned
   * @return one of the constants ABSOLUTE_PATH, WORKSPACE_RELATIVE_PATH, PROJECT_RELATIVE_PATH
   */
  public static int getPathType(String entrypath, Workspace workspace) {

    if (workspace == null) {
      // no chance to determine the path
      return ABSOLUTE_PATH;
    }

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

          if (workspace.hasProject(splitted[0])) {
            // path exists in the workspace; treat as "workspace relative path"
            return WORKSPACE_RELATIVE_PATH;
          } else {
            // path does not exist in the workspace; treat as "real" absolute path
            return ABSOLUTE_PATH;
          }

        } else if (workspace.hasProject(splitted[0]) && workspace.getProject(splitted[0]).hasChild(splitted[1])) {
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
  public static String[] splitHeadAndTail(String entryPath) {

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
