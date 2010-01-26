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
package org.ant4eclipse.lib.pde.tools;


import org.ant4eclipse.pde.model.buildproperties.PluginBuildProperties;
import org.ant4eclipse.pde.model.buildproperties.PluginBuildProperties.Library;
import org.ant4eclipse.pde.model.pluginproject.PluginProjectRole;

import org.ant4eclipse.platform.model.resource.EclipseProject;

import org.ant4eclipse.lib.core.Assure;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class LibraryHelper {

  /**
   * <p>
   * </p>
   * 
   * @param eclipseProject
   * @return
   */
  public static String[] getSourceLibraryNames(EclipseProject eclipseProject) {

    Library[] libraries = getLibraries(eclipseProject);
    String[] names = new String[libraries.length];

    for (int i = 0; i < libraries.length; i++) {
      Library library = libraries[i];
      names[i] = getSourceNameForLibrary(library.getName());
    }

    return names;
  }

  /**
   * @return
   */
  public static Library[] getLibraries(EclipseProject eclipseProject) {

    // get the plug-in project role
    PluginProjectRole pluginProjectRole = PluginProjectRole.Helper.getPluginProjectRole(eclipseProject);

    // get the libraries
    PluginBuildProperties pluginBuildProperties = pluginProjectRole.getBuildProperties();

    // TODO: should we take sourceIncludes for source builds?
    List<String> binaryIncludes = Arrays.asList(pluginBuildProperties.getBinaryIncludes());

    List<Library> result = new LinkedList<Library>();

    Library[] libraries = pluginBuildProperties.getOrderedLibraries();

    // only include libraries that are defined in the binary include list
    for (Library library : libraries) {
      if (binaryIncludes.contains(library.getName())) {
        result.add(library);
      }
    }

    return result.toArray(new Library[0]);
  }

  /**
   * <p>
   * </p>
   * 
   * @param libraryName
   * @return
   */
  public static String getSourceNameForLibrary(String libraryName) {

    Assure.notNull(libraryName);

    String result = libraryName;

    if (result.endsWith(".jar")) {
      result = result.substring(0, result.length() - 4);
    } else if (result.endsWith("/") || result.endsWith("\\")) {
      result = result.substring(0, result.length() - 1);
    }

    return result + ".src";
  }
}
