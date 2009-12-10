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

import org.ant4eclipse.lib.core.logging.A4ELogging;
import org.ant4eclipse.lib.core.util.Utilities;
import org.ant4eclipse.lib.core.xquery.XQuery;
import org.ant4eclipse.lib.core.xquery.XQueryHandler;
import org.ant4eclipse.pydt.internal.model.project.PythonProjectRoleImpl;
import org.ant4eclipse.pydt.model.RawPathEntry;
import org.ant4eclipse.pydt.model.ReferenceKind;

import java.io.File;

/**
 * This parser is used to contribute the necessary configuration information to the associated project role. This
 * implementation is used to support the PyDev framework.
 * 
 * @author Daniel Kasmeroglu (Daniel.Kasmeroglu@Kasisoft.net)
 */
public class PyDevParser {

  private static final String[] LIBSUFFICES       = new String[] { "egg", "jar", "zip" };

  private static final String   KEY_DEFAULT       = "Default";

  private static final String   NAME_PYDEVPROJECT = ".pydevproject";

  /**
   * Enriches the supplied role with the necessary information taken from the configuration files used by the PyDev
   * framework.
   * 
   * @param pythonrole
   *          The role instance which will be filled with the corresponding information. Not <code>null</code>.
   */
  public static final void contributePathes(PythonProjectRoleImpl pythonrole) {

    String projectname = pythonrole.getEclipseProject().getSpecifiedName();
    File pydevproject = pythonrole.getEclipseProject().getChild(NAME_PYDEVPROJECT);

    XQueryHandler queryhandler = new XQueryHandler();

    // prepare the access for the pathes
    XQuery pathquery = queryhandler
        .createQuery("/pydev_project/pydev_pathproperty[@name='org.python.pydev.PROJECT_SOURCE_PATH']/path");
    XQuery externalquery = queryhandler
        .createQuery("/pydev_project/pydev_pathproperty[@name='org.python.pydev.PROJECT_EXTERNAL_SOURCE_PATH']/path");
    XQuery runtimequery = queryhandler
        .createQuery("/pydev_project/pydev_property[@name='org.python.pydev.PYTHON_PROJECT_INTERPRETER']");

    // now fetch the necessary data
    XQueryHandler.queryFile(pydevproject, queryhandler);

    String[] internalsourcepathes = pathquery.getResult();
    String[] externalsourcepathes = externalquery.getResult();
    String runtime = runtimequery.getSingleResult();

    // PyDev identifies the projects by workspace relative pathes, so we need to strip these
    // prefixes from the project names
    String prefix = "/" + pythonrole.getEclipseProject().getSpecifiedName();

    // a path can be a library or a source directory
    for (String internalsourcepathe : internalsourcepathes) {
      String path = internalsourcepathe;
      if (!path.startsWith(prefix)) {
        A4ELogging.error(
            "The internal path '%s' does not start with the expected prefix '%s' and is therefore being skipped.",
            path, prefix);
        continue;
      }
      String internalpath = Utilities.cleanup(path.substring(prefix.length()));
      if ((internalpath != null) && internalpath.startsWith("/")) {
        internalpath = Utilities.cleanup(internalpath.substring(1));
      }
      if ((internalpath != null) && isLibrary(internalpath)) {
        pythonrole.addRawPathEntry(new RawPathEntry(projectname, ReferenceKind.Library, internalpath, true, false));
      } else {
        pythonrole.addRawPathEntry(new RawPathEntry(projectname, ReferenceKind.Source, internalpath, true, false));
      }
    }

    for (String path : externalsourcepathes) {
      if (isLibrary(path)) {
        pythonrole.addRawPathEntry(new RawPathEntry(projectname, ReferenceKind.Library, path, true, true));
      } else {
        pythonrole.addRawPathEntry(new RawPathEntry(projectname, ReferenceKind.Source, path, true, true));
      }
    }

    if ((runtime != null) && (runtime.length() > 0)) {
      if (KEY_DEFAULT.equals(runtime)) {
        runtime = "";
      }
      pythonrole.addRawPathEntry(new RawPathEntry(projectname, ReferenceKind.Runtime, runtime, true, false));
    }

    // PyDev uses the platform mechanism for referenced projects. this is somewhat unlikely
    // so we need to convert this information into corresponding entries while sorting out
    // the ones with the wrong natures
    String[] projects = pythonrole.getEclipseProject().getReferencedProjects();
    for (String project : projects) {
      pythonrole.addRawPathEntry(new RawPathEntry(projectname, ReferenceKind.Project, "/" + project, true, false));
    }

  }

  /**
   * Returns <code>true</code> if the supplied path is considered to be a library. A library is detected by it's format
   * which is considered to be an egg, a jar or a zip file.
   * 
   * @param path
   *          The path to be investigated. Not <code>null</code>.
   * 
   * @return <code>true</code> <=> Interprete the supplied path as a library.
   */
  private static final boolean isLibrary(String path) {
    int lidx = path.lastIndexOf('.');
    if (lidx == -1) {
      return false;
    }
    String suffix = path.substring(lidx + 1).toLowerCase();
    return Utilities.contains(suffix, LIBSUFFICES);
  }

} /* ENDCLASS */
