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
package org.ant4eclipse.lib.platform.model.team.cvssupport.project;

import org.ant4eclipse.lib.core.Assure;
import org.ant4eclipse.lib.core.exception.Ant4EclipseException;
import org.ant4eclipse.lib.core.util.Utilities;
import org.ant4eclipse.lib.platform.PlatformExceptionCode;
import org.ant4eclipse.lib.platform.model.resource.EclipseProject;
import org.ant4eclipse.lib.platform.model.team.cvssupport.CvsRoot;

import java.io.File;

/**
 * <p>
 * Helper class for parsing cvs information.
 * </p>
 * 
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 */
public class CvsParser {

  /**
   * Returns whether the specified project
   * 
   * @param project
   *          The EclipseProject instance where to lookup the CVS root file.
   * 
   * @return true <=> The CVS project has been set.
   */
  public static boolean isCvsProject( EclipseProject project ) {
    Assure.notNull( "project", project );
    return project.hasChild( "CVS" + File.separator + "Root" );
  }

  /**
   * Reads the name of the CVS repository.
   * 
   * @param project
   *          The EclipseProject instance where to read the repository name from.
   * 
   * @return The name of the CVS repository.
   * 
   * @throws Ant4EclipseException
   *           Loading the content failed for some reason.
   */
  public static String readCvsRepositoryName( EclipseProject project ) throws Ant4EclipseException {
    Assure.notNull( "project", project );
    File cvsRepositoryFile = project.getChild( "CVS" + File.separator + "Repository" );
    return readFile( cvsRepositoryFile );
  }

  /**
   * Reads the content of the CVS root file.
   * 
   * @param project
   *          The EclipseProject instance where to look for a CVS root file.
   * 
   * @return A CVSRoot instance associated with the supplied project.
   * 
   * @throws Ant4EclipseException
   *           Loading the root file failed.
   */
  public static CvsRoot readCvsRoot( EclipseProject project ) throws Ant4EclipseException {
    Assure.notNull( "project", project );
    File cvsRootFile = project.getChild( "CVS" + File.separator + "Root" );
    String cvsRoot = readFile( cvsRootFile );
    return new CvsRoot( cvsRoot );
  }

  public static String readTag( EclipseProject project ) throws Ant4EclipseException {
    Assure.notNull( "project", project );
    String tagname = String.format( "CVS%sTag", File.separator );
    if( !project.hasChild( tagname ) ) {
      return null;
    }
    File tagFile = project.getChild( tagname );
    String tag = readFile( tagFile );
    if( tag.length() <= 1 ) {
      return null;
    }
    return tag.substring( 1 );
  }

  /**
   * Reads the given file and returns its content as a String.
   * 
   * @param file
   *          The file to read
   * @return The file content
   * @throws Ant4EclipseException
   *           When reading the file fails for some reason
   */
  private static String readFile( File file ) throws Ant4EclipseException {
    StringBuffer buffy = new StringBuffer();
    try {
      buffy = Utilities.readTextContent( file, Utilities.ENCODING, false );
    } catch( Ant4EclipseException e ) {
      throw new Ant4EclipseException( e.getCause(), PlatformExceptionCode.ERROR_WHILE_READING_CVS_FILE, file,
          e.toString() );
    }
    return buffy.toString();
  }

  /**
   * Private Constructor: no need to create an instance of type CvsParser.
   */
  private CvsParser() {
    // avoid creating instances
  }
  
} /* ENDCLASS */
