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
package org.ant4eclipse.lib.pde.tools;

import org.ant4eclipse.lib.core.Assure;
import org.ant4eclipse.lib.pde.model.buildproperties.PluginBuildProperties;
import org.ant4eclipse.lib.pde.model.buildproperties.PluginBuildProperties.Library;
import org.ant4eclipse.lib.pde.model.pluginproject.PluginProjectRole;
import org.ant4eclipse.lib.platform.model.resource.EclipseProject;

import java.util.ArrayList;
import java.util.List;

public class LibraryHelper {

  /**
   * <p>
   * </p>
   * 
   * @param eclipseProject
   * @return
   */
  public static final String[] getSourceLibraryNames( EclipseProject eclipseProject ) {
    Library[] libraries = getLibraries( eclipseProject );
    String[] names = new String[libraries.length];
    for( int i = 0; i < libraries.length; i++ ) {
      Library library = libraries[i];
      names[i] = getSourceNameForLibrary( library.getName() );
    }
    return names;
  }

  /**
   * @return
   */
  public static final Library[] getLibraries( EclipseProject eclipseProject ) {
    // get the plug-in project role
    PluginProjectRole pluginProjectRole = eclipseProject.getRole( PluginProjectRole.class );
    // get the libraries
    PluginBuildProperties pluginBuildProperties = pluginProjectRole.getBuildProperties();
    // TODO: should we take sourceIncludes for source builds?
    List<String> binaryIncludes = pluginBuildProperties.getBinaryIncludes();
    List<Library> result = new ArrayList<Library>();
    Library[] libraries = pluginBuildProperties.getOrderedLibraries();
    // only include libraries that are defined in the binary include list
    for( Library library : libraries ) {
      if( binaryIncludes.contains( library.getName() ) ) {
        result.add( library );
      }
    }
    return result.toArray( new Library[0] );
  }

  /**
   * <p>
   * </p>
   * 
   * @param libraryName
   * @return
   */
  public static final String getSourceNameForLibrary( String libraryName ) {
    Assure.notNull( "libraryName", libraryName );
    String result = libraryName;
    if( result.endsWith( ".jar" ) ) {
      result = result.substring( 0, result.length() - 4 );
    } else if( result.endsWith( "/" ) || result.endsWith( "\\" ) ) {
      result = result.substring( 0, result.length() - 1 );
    }
    return String.format( "%s.src", result );
  }

} /* ENDCLASS */
