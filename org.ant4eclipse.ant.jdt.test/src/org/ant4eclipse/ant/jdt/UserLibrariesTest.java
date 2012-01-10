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
package org.ant4eclipse.ant.jdt;

import org.ant4eclipse.ant.jdt.base.AbstractJdtClassPathTest;
import org.ant4eclipse.lib.core.logging.A4ELogging;
import org.ant4eclipse.testframework.JdtProjectBuilder;

import java.io.File;

/**
 * <p>
 * </p>
 * 
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 */
public class UserLibrariesTest extends AbstractJdtClassPathTest {

  private static final String USERLIBRARIES = 
    "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>\n" +
    "<eclipse-userlibraries version=\"2\">\n" +
    "  <library name=\"testLibrary\" systemlibrary=\"true\">\n" +
    "    <archive path=\"%s\"/>\n" +
    "  </library>\n" +
    "</eclipse-userlibraries>";
  
  @Override
  public void setUp() throws Exception {
    super.setUp();
    setupBuildFile( "userLibraries.xml" );
  }

  /**
   * <p>
   * </p>
   * 
   * @throws Exception
   */
  public void testClasspathVariables() throws Exception {
    // create simple project 'project' with a source directory 'src' and a output directory 'bin'
    JdtProjectBuilder.getPreConfiguredJdtBuilder( "projectk" )
        .withContainerClasspathEntry( "org.eclipse.jdt.USER_LIBRARY/testLibrary" )
        .createIn( getTestWorkspaceDirectory() );

    getTestWorkspace().createFile( "myUserLibraries.xml", getContent() );

    File workspacedir = getTestWorkspaceDirectory();
    File[] entries = workspacedir.listFiles();
    A4ELogging.info( "COUNT: %d", Integer.valueOf( entries.length ) );
    for( File entry : entries ) {
      log( entry, "    " );
    }
    
    getProject().setProperty( "projectName", "projectk" );

    // execute target
    String classpath = executeTestTarget( "projectk", true, true );
    System.err.println( classpath );
    
    assertClasspath( classpath, new File( "projectk/bin" ), new File( getTestWorkspaceDirectory(), "haensel" ) );
    
  }

  private void log( File file, String prefix ) {
    if( file.isDirectory() ) {
      A4ELogging.info( "%s(dir)  %s", prefix, file.getName() );
      File[] children = file.listFiles();
      for( File child : children ) {
        log( child, prefix + "    " );
      }
    } else {
      A4ELogging.info( "%s(file) %s", prefix, file.getName() );
    }
  }
  
  private String getContent() {
    String pathDir = getTestWorkspaceDirectory().getAbsolutePath() + File.separatorChar + "haensel";
    getTestWorkspace().createSubDirectory( "haensel" );
    return String.format( USERLIBRARIES, pathDir );
  }
  
} /* ENDCLASS */
