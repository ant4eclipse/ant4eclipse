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
package org.ant4eclipse.jdt.model.userlibrary;

import java.io.File;

import org.ant4eclipse.core.xquery.XQuery;
import org.ant4eclipse.core.xquery.XQueryHandler;

/**
 * Parsing class used to process an eclipse user library configuration file.
 * 
 * @author Daniel Kasmeroglu (daniel.kasmeroglu@kasisoft.net)
 */
public class UserLibrariesFileParser {

  private final UserLibraries _userlibs;

  /**
   * Parses the supplied eclipse user library configuration file.
   * 
   * @param configuration
   *          The eclipse user library configuration file.
   */
  public UserLibrariesFileParser(final File configuration) {
    super();
    this._userlibs = new UserLibraries();

    final XQueryHandler queryhandler2 = new XQueryHandler();

    // queries for the 'name' and 'systemlibrary' attributes. the resulting array
    // will have the same length.
    final XQuery namequery = queryhandler2.createQuery("//eclipse-userlibraries/library/@name");
    final XQuery syslibquery = queryhandler2.createQuery("//eclipse-userlibraries/library/@systemlibrary");

    // function query which count the number of 'archive' elements within each 'library'
    // element. so it has the same length as the attribute-value-arrays.
    final XQuery countquery = queryhandler2.createQuery("//eclipse-userlibraries/library/archive[count()]");

    // queries for the 'path', 'source', 'javadoc' attributes of 'archive' elements.
    final XQuery pathquery = queryhandler2.createQuery("//eclipse-userlibraries/library/archive/@path");
    final XQuery sourcequery = queryhandler2.createQuery("//eclipse-userlibraries/library/archive/@source");
    final XQuery javadocquery = queryhandler2.createQuery("//eclipse-userlibraries/library/archive/@javadoc");

    // parse the file
    XQueryHandler.queryFile(configuration, queryhandler2);

    final String[] names = namequery.getResult();
    final String[] syslibs = syslibquery.getResult();
    final String[] counters = countquery.getResult();
    final String[] pathes = pathquery.getResult();
    final String[] sources = sourcequery.getResult();
    final String[] javadocs = javadocquery.getResult();

    for (int i = 0, j = 0; i < names.length; i++) {

      // create an entry for each 'library' element
      final UserLibrary userlib = new UserLibrary(names[i], "true".equals(syslibs[i]));
      this._userlibs.addLibrary(userlib);

      // check how many 'archive' elements are associated
      // with the current 'library' element.
      int arccount = Integer.parseInt(counters[i]);
      while (arccount > 0) {

        // create an Archive instance for each 'archive' element.
        final Archive archive = new Archive(new File(pathes[j]));
        userlib.addArchive(archive);

        if ((sources[j] != null) && (!sources[j].trim().equals(""))) {
          archive.setSource(new File(sources[j]));
        }
        if ((javadocs[j] != null) && (!javadocs[j].trim().equals(""))) {
          archive.setJavaDoc(javadocs[j]);
        }

        j++;
        arccount--;

      }

    }
  }

  /**
   * Returns the datastructure that holds all relevant information regarding the eclipse user libraries.
   * 
   * @return A datastructure representing the eclipse user libraries.
   */
  public UserLibraries getUserLibraries() {
    return (this._userlibs);
  }

} /* ENDCLASS */
