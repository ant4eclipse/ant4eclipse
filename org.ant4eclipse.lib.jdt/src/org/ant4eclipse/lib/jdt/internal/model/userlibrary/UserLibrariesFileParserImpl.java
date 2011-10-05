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
package org.ant4eclipse.lib.jdt.internal.model.userlibrary;

import static org.ant4eclipse.lib.core.logging.A4ELogging.trace;

import java.io.File;

import org.ant4eclipse.lib.core.xquery.XQuery;
import org.ant4eclipse.lib.core.xquery.XQueryHandler;
import org.ant4eclipse.lib.jdt.EclipsePathUtil;
import org.ant4eclipse.lib.jdt.model.userlibrary.UserLibraries;
import org.ant4eclipse.lib.jdt.model.userlibrary.UserLibrariesFileParser;
import org.ant4eclipse.lib.platform.model.resource.EclipseProject;
import org.ant4eclipse.lib.platform.model.resource.Workspace;

/**
 * Parsing class used to process an eclipse user library configuration file.
 * 
 * @author Daniel Kasmeroglu (daniel.kasmeroglu@kasisoft.net)
 */
public class UserLibrariesFileParserImpl implements UserLibrariesFileParser {

  /**
   * {@inheritDoc}
   */
  public UserLibraries parseUserLibrariesFile(File configuration, Workspace workspace) {
    UserLibrariesImpl userlibs = new UserLibrariesImpl();

    trace("Parsing UserLibraries configuration file '%s'", configuration);

    XQueryHandler queryhandler2 = new XQueryHandler();

    // queries for the 'name' and 'systemlibrary' attributes. the resulting array
    // will have the same length.
    XQuery namequery = queryhandler2.createQuery("/eclipse-userlibraries/library/@name");
    XQuery syslibquery = queryhandler2.createQuery("/eclipse-userlibraries/library/@systemlibrary");

    // function query which count the number of 'archive' elements within each 'library'
    // element. so it has the same length as the attribute-value-arrays.
    XQuery countquery = queryhandler2.createQuery("/eclipse-userlibraries/library/archive[count()]");

    // queries for the 'path', 'source', 'javadoc' attributes of 'archive' elements.
    XQuery pathquery = queryhandler2.createQuery("/eclipse-userlibraries/library/archive/@path");
    XQuery sourcequery = queryhandler2.createQuery("/eclipse-userlibraries/library/archive/@source");
    XQuery javadocquery = queryhandler2.createQuery("/eclipse-userlibraries/library/archive/@javadoc");

    // parse the file
    XQueryHandler.queryFile(configuration, queryhandler2);

    String[] names = namequery.getResult();
    String[] syslibs = syslibquery.getResult();
    String[] counters = countquery.getResult();
    String[] pathes = pathquery.getResult();
    String[] sources = sourcequery.getResult();
    String[] javadocs = javadocquery.getResult();

    for (int i = 0, j = 0; i < names.length; i++) {

      // create an entry for each 'library' element
      UserLibraryImpl userlib = new UserLibraryImpl(names[i], "true".equals(syslibs[i]));
      userlibs.addLibrary(userlib);

      // check how many 'archive' elements are associated
      // with the current 'library' element.
      int arccount = Integer.parseInt(counters[i]);
      while (arccount > 0) {

        String path = pathes[j];
        File archiveFile;
        int pathType = EclipsePathUtil.getPathType(path, workspace);
        if (pathType == EclipsePathUtil.ABSOLUTE_PATH) {
          archiveFile = new File(path);
        } else {
          // Workspace relative
          String[] splitted = EclipsePathUtil.splitHeadAndTail(path);
          EclipseProject project = workspace.getProject(splitted[0]);
          archiveFile = project.getChild(splitted[1]);
        }

        // create an Archive instance for each 'archive' element.
        ArchiveImpl archive = new ArchiveImpl(archiveFile);
        userlib.addArchive(archive);

        if ((sources[j] != null) && (!"".equals(sources[j].trim()))) {
          archive.setSource(new File(sources[j]));
        }
        if ((javadocs[j] != null) && (!"".equals(javadocs[j].trim()))) {
          archive.setJavaDoc(javadocs[j]);
        }

        j++;
        arccount--;

      }

    }
    return userlibs;
  }

} /* ENDCLASS */
