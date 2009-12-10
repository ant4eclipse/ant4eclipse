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
package org.ant4eclipse.jdt.internal.model.project;

import org.ant4eclipse.jdt.model.project.RawClasspathEntry;

import java.util.LinkedList;
import java.util.List;

public class EntryResolver {

  /**
   * @param entryResolver
   * @return A list of resolved paths.
   */
  public static String[] resolveEntries(Condition condition, JavaProjectRoleImpl javaProjectRole) {

    List<String> result = new LinkedList<String>();

    RawClasspathEntry[] rawClasspathEntries = javaProjectRole.getRawClasspathEntries();

    for (RawClasspathEntry rawClasspathEntrie : rawClasspathEntries) {

      String path = condition.resolve(rawClasspathEntrie);

      if (path != null) {
        result.add(path);
      }
    }
    return result.toArray(new String[result.size()]);
  }

  /**
   * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
   */
  public static interface Condition {

    /**
     * @param entry
     * @return The path.
     */
    public String resolve(RawClasspathEntry entry);
  }
}
