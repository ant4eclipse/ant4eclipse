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
package org.ant4eclipse.jdt.internal.model.project;

import java.io.File;

import org.ant4eclipse.core.Assert;
import org.ant4eclipse.core.util.Utilities;
import org.ant4eclipse.core.xquery.XQuery;
import org.ant4eclipse.core.xquery.XQueryHandler;


/**
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 */
public class ClasspathFileParser {

  public static void parseClasspath(final JavaProjectRoleImpl javaProjectRole) {
    Assert.notNull(javaProjectRole);

    final File classpathFile = javaProjectRole.getEclipseProject().getChild(".classpath");

    final XQueryHandler queryhandler = new XQueryHandler();

    // queries for the 'kind', 'path','output' and 'exported' attributes. The
    // resulting array will have the same length.
    final XQuery kindquery = queryhandler.createQuery("//classpath/classpathentry/@kind");
    final XQuery pathquery = queryhandler.createQuery("//classpath/classpathentry/@path");
    final XQuery outputquery = queryhandler.createQuery("//classpath/classpathentry/@output");
    final XQuery exportedquery = queryhandler.createQuery("//classpath/classpathentry/@exported");

    // parse the file
    XQueryHandler.queryFile(classpathFile, queryhandler);

    final String[] kinds = kindquery.getResult();
    final String[] pathes = pathquery.getResult();
    final String[] outputs = outputquery.getResult();
    final String[] exporteds = exportedquery.getResult();

    for (int i = 0; i < exporteds.length; i++) {
      if (outputs[i] != null) {
        javaProjectRole.addEclipseClasspathEntry(new RawClasspathEntryImpl(kinds[i], Utilities
            .removeTrailingPathSeparator(pathes[i]), Utilities.removeTrailingPathSeparator(outputs[i])));
      } else if (exporteds[i] != null) {
        javaProjectRole.addEclipseClasspathEntry(new RawClasspathEntryImpl(kinds[i], Utilities
            .removeTrailingPathSeparator(pathes[i]), Boolean.valueOf(exporteds[i]).booleanValue()));
      } else {
        javaProjectRole.addEclipseClasspathEntry(new RawClasspathEntryImpl(kinds[i], Utilities
            .removeTrailingPathSeparator(pathes[i])));
      }
    }
  }

}
