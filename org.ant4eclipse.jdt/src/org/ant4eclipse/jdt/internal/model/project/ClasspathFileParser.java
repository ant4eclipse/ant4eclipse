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

import org.ant4eclipse.lib.core.Assure;
import org.ant4eclipse.lib.core.util.Utilities;
import org.ant4eclipse.lib.core.xquery.XQuery;
import org.ant4eclipse.lib.core.xquery.XQueryHandler;

import java.io.File;

/**
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 */
public class ClasspathFileParser {

  public static void parseClasspath(JavaProjectRoleImpl javaProjectRole) {
    Assure.notNull(javaProjectRole);

    File classpathFile = javaProjectRole.getEclipseProject().getChild(".classpath");

    XQueryHandler queryhandler = new XQueryHandler();

    // queries for the 'kind', 'path','output' and 'exported' attributes. The
    // resulting array will have the same length.
    XQuery kindquery = queryhandler.createQuery("/classpath/classpathentry/@kind");
    XQuery pathquery = queryhandler.createQuery("/classpath/classpathentry/@path");
    XQuery outputquery = queryhandler.createQuery("/classpath/classpathentry/@output");
    XQuery exportedquery = queryhandler.createQuery("/classpath/classpathentry/@exported");

    // parse the file
    XQueryHandler.queryFile(classpathFile, queryhandler);

    String[] kinds = kindquery.getResult();
    String[] pathes = pathquery.getResult();
    String[] outputs = outputquery.getResult();
    String[] exporteds = exportedquery.getResult();

    for (int i = 0; i < exporteds.length; i++) {
      String path = Utilities.removeTrailingPathSeparator(pathes[i]);
      if (outputs[i] != null) {
        javaProjectRole.addEclipseClasspathEntry(new RawClasspathEntryImpl(kinds[i], path, Utilities
            .removeTrailingPathSeparator(outputs[i])));
      } else if (exporteds[i] != null) {
        javaProjectRole.addEclipseClasspathEntry(new RawClasspathEntryImpl(kinds[i], path, Boolean
            .parseBoolean(exporteds[i])));
      } else {
        javaProjectRole.addEclipseClasspathEntry(new RawClasspathEntryImpl(kinds[i], path));
      }
    }
  }

}
