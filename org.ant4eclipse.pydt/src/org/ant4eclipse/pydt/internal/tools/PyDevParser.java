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

import org.ant4eclipse.core.logging.A4ELogging;
import org.ant4eclipse.core.util.Utilities;
import org.ant4eclipse.core.xquery.XQuery;
import org.ant4eclipse.core.xquery.XQueryHandler;

import org.ant4eclipse.platform.model.resource.EclipseProject;
import org.ant4eclipse.platform.model.resource.Workspace;

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
  public static final void contributePathes(final PythonProjectRoleImpl pythonrole) {

    final File pydevproject = pythonrole.getEclipseProject().getChild(NAME_PYDEVPROJECT);

    final XQueryHandler queryhandler = new XQueryHandler();

    // prepare the access for the pathes
    final XQuery pathquery = queryhandler
        .createQuery("//pydev_project/pydev_pathproperty[@name='org.python.pydev.PROJECT_SOURCE_PATH']/path");
    final XQuery externalquery = queryhandler
        .createQuery("//pydev_project/pydev_pathproperty[@name='org.python.pydev.PROJECT_EXTERNAL_SOURCE_PATH']/path");
    final XQuery runtimequery = queryhandler
        .createQuery("//pydev_project/pydev_property[@name='org.python.pydev.PYTHON_PROJECT_INTERPRETER']");

    // now fetch the necessary data
    XQueryHandler.queryFile(pydevproject, queryhandler);

    final String[] internalsourcepathes = pathquery.getResult();
    final String[] externalsourcepathes = externalquery.getResult();
    String runtime = runtimequery.getSingleResult();

    // PyDev identifies the projects by workspace relative pathes, so we need to strip these
    // prefixes from the project names
    final String prefix = "/" + pythonrole.getEclipseProject().getSpecifiedName();

    // a path can be a library or a source directory
    for (int i = 0; i < internalsourcepathes.length; i++) {
      final String path = internalsourcepathes[i];
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
        pythonrole.addRawPathEntry(new RawPathEntry(ReferenceKind.Library, internalpath, true, false));
      } else {
        pythonrole.addRawPathEntry(new RawPathEntry(ReferenceKind.Source, internalpath, true, false));
        // the PyDev framework has no output path declaration. the output path is equal to the
        // source path here.
        pythonrole.addRawPathEntry(new RawPathEntry(ReferenceKind.Output, internalpath, true, false));
      }
    }

    for (int i = 0; i < externalsourcepathes.length; i++) {
      final String path = externalsourcepathes[i];
      if (isLibrary(path)) {
        pythonrole.addRawPathEntry(new RawPathEntry(ReferenceKind.Library, path, true, true));
      } else {
        pythonrole.addRawPathEntry(new RawPathEntry(ReferenceKind.Source, path, true, true));
      }
    }

    if ((runtime != null) && (runtime.length() > 0)) {
      if (KEY_DEFAULT.equals(runtime)) {
        runtime = "";
      }
      pythonrole.addRawPathEntry(new RawPathEntry(ReferenceKind.Runtime, runtime, true, false));
    }

    // PyDev uses the platform mechanism for referenced projects. this is somewhat unlikely
    // so we need to convert this information into corresponding entries while sorting out
    // the ones with the wrong natures
    final String[] projects = pythonrole.getEclipseProject().getReferencedProjects();
    final Workspace workspace = pythonrole.getEclipseProject().getWorkspace();
    for (int i = 0; i < projects.length; i++) {
      final EclipseProject refproject = workspace.getProject(projects[i]);
      if (PythonUtilities.isPyDevProject(refproject)) {
        pythonrole.addRawPathEntry(new RawPathEntry(ReferenceKind.Project, "/" + projects[i], true, false));
      }
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
  private static final boolean isLibrary(final String path) {
    int lidx = path.lastIndexOf('.');
    if (lidx == -1) {
      return false;
    }
    String suffix = path.substring(lidx + 1).toLowerCase();
    return Utilities.contains(suffix, LIBSUFFICES);
  }

} /* ENDCLASS */
