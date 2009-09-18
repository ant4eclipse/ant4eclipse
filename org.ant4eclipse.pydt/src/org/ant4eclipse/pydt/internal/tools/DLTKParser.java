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

import org.ant4eclipse.pydt.internal.model.project.PythonProjectRoleImpl;
import org.ant4eclipse.pydt.model.RawPathEntry;
import org.ant4eclipse.pydt.model.ReferenceKind;

import java.io.File;

/**
 * This parser is used to contribute the necessary configuration information to the associated project role. This
 * implementation is used to support the Python DLTK framework.
 * 
 * @author Daniel Kasmeroglu (Daniel.Kasmeroglu@Kasisoft.net)
 */
public class DLTKParser {

  private static final String NAME_BUILDPATH = ".buildpath";

  private static final String KIND_SOURCE    = "src";

  private static final String KIND_LIBRARY   = "lib";

  private static final String KIND_PROJECT   = "prj";

  private static final String KIND_CONTAINER = "con";

  private static final String KEY_RUNTIME    = "org.eclipse.dltk.launching.INTERPRETER_CONTAINER";

  /**
   * Maps a textual representation of a record to the kind of reference known by us.
   * 
   * @param kind
   *          The textual representation as supplied within the configuration file. Not <code>null</code>.
   * 
   * @return The kind of reference used for this type of record.
   */
  private static final ReferenceKind getReferenceKind(String kind) {
    if (KIND_SOURCE.equals(kind)) {
      return ReferenceKind.Source;
    } else if (KIND_LIBRARY.equals(kind)) {
      return ReferenceKind.Library;
    } else if (KIND_PROJECT.equals(kind)) {
      return ReferenceKind.Project;
    } else if (KIND_CONTAINER.equals(kind)) {
      return ReferenceKind.Container;
    } else {
      return null;
    }
  }

  /**
   * Enriches the supplied role with the necessary information taken from the configuration files used by the Python
   * DLTK framework.
   * 
   * @param pythonrole
   *          The role instance which will be filled with the corresponding information. Not <code>null</code>.
   */
  public static final void contributePathes(PythonProjectRoleImpl pythonrole) {

    String projectname = pythonrole.getEclipseProject().getSpecifiedName();
    File buildpath = pythonrole.getEclipseProject().getChild(NAME_BUILDPATH);

    XQueryHandler queryhandler = new XQueryHandler();

    // prepare the access for the attributes 'kind', 'path', 'exported', 'external'
    XQuery kindquery = queryhandler.createQuery("//buildpath/buildpathentry/@kind");
    XQuery pathquery = queryhandler.createQuery("//buildpath/buildpathentry/@path");
    XQuery exportedquery = queryhandler.createQuery("//buildpath/buildpathentry/@exported");
    XQuery externalquery = queryhandler.createQuery("//buildpath/buildpathentry/@external");

    // now fetch the necessary data
    XQueryHandler.queryFile(buildpath, queryhandler);

    String[] kinds = kindquery.getResult();
    String[] pathes = pathquery.getResult();
    String[] exported = exportedquery.getResult();
    String[] externals = externalquery.getResult();

    for (int i = 0; i < kinds.length; i++) {
      ReferenceKind refkind = getReferenceKind(kinds[i]);
      String path = Utilities.removeTrailingPathSeparator(pathes[i]);
      if (refkind == null) {
        // we currently don't provide support for this kind, so skip it. just log the skipped path so the user will know
        // whether this issue is relevant for him or not.
        A4ELogging.warn("Skipping buildpath entry with path '%s'. The kind '%s' is currently not supported.", path,
            kinds[i]);
        continue;
      }
      boolean isexported = Boolean.parseBoolean(exported[i]);
      boolean isexternal = Boolean.parseBoolean(externals[i]);
      if (refkind == ReferenceKind.Container) {
        // runtimes are declared as containers in Python DLTK
        if (path.startsWith(KEY_RUNTIME)) {
          refkind = ReferenceKind.Runtime;
          path = path.substring(KEY_RUNTIME.length());
          if (path.startsWith("/")) {
            path = path.substring(1);
          }
        }
      }
      path = Utilities.cleanup(path);
      pythonrole.addRawPathEntry(new RawPathEntry(projectname, refkind, path, isexported, isexternal));

    }

  }
} /* ENDCLASS */
