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
package org.ant4eclipse.lib.jdt.internal.model.project;

import org.ant4eclipse.lib.core.util.Utilities;
import org.ant4eclipse.lib.core.xquery.XQuery;
import org.ant4eclipse.lib.core.xquery.XQueryHandler;

import java.io.File;

/**
 * <p>
 * </p>
 * 
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 */
public class ClasspathFileParser {

  // Assure.notNull( "javaProjectRole", javaProjectRole );
  public static void parseClasspath( JavaProjectRoleImpl javaProjectRole ) {

    File classpathFile = javaProjectRole.getEclipseProject().getChild( ".classpath" );

    XQueryHandler queryhandler = new XQueryHandler();

    // queries for the 'kind', 'path','output' and 'exported' attributes. The
    // resulting array will have the same length.
    XQuery kindquery = queryhandler.createQuery( "/classpath/classpathentry/@kind" );
    XQuery pathquery = queryhandler.createQuery( "/classpath/classpathentry/@path" );
    XQuery outputquery = queryhandler.createQuery( "/classpath/classpathentry/@output" );
    XQuery exportedquery = queryhandler.createQuery( "/classpath/classpathentry/@exported" );
    XQuery includedquery = queryhandler.createQuery( "/classpath/classpathentry/@including" );
    XQuery excludedquery = queryhandler.createQuery( "/classpath/classpathentry/@excluding" );

    // parse the file
    XQueryHandler.queryFile( classpathFile, queryhandler );

    String[] kinds = kindquery.getResult();
    String[] pathes = pathquery.getResult();
    String[] outputs = outputquery.getResult();
    String[] exporteds = exportedquery.getResult();
    String[] includes = includedquery.getResult();
    String[] excludes = excludedquery.getResult();

    for( int i = 0; i < exporteds.length; i++ ) {
      String path = Utilities.removeTrailingPathSeparator( pathes[i] );
      RawClasspathEntryImpl rawClasspathEntryImpl = null;
      if( outputs[i] != null ) {
        rawClasspathEntryImpl = new RawClasspathEntryImpl( kinds[i], path,
            Utilities.removeTrailingPathSeparator( outputs[i] ) );
        javaProjectRole.addEclipseClasspathEntry( rawClasspathEntryImpl );
      } else if( exporteds[i] != null ) {
        rawClasspathEntryImpl = new RawClasspathEntryImpl( kinds[i], path, Boolean.parseBoolean( exporteds[i] ) );
        javaProjectRole.addEclipseClasspathEntry( rawClasspathEntryImpl );
      } else {
        rawClasspathEntryImpl = new RawClasspathEntryImpl( kinds[i], path );
        javaProjectRole.addEclipseClasspathEntry( rawClasspathEntryImpl );
      }
      if( includes[i] != null ) {
        rawClasspathEntryImpl.setIncludes( includes[i] );
      }
      if( excludes[i] != null ) {
        rawClasspathEntryImpl.setExcludes( excludes[i] );
      }
    }
  }
  
} /* ENDCLASS */
