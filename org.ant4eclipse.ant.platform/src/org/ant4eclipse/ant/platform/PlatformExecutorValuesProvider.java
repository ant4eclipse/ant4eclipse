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
package org.ant4eclipse.ant.platform;

import org.ant4eclipse.ant.platform.core.MacroExecutionValues;
import org.ant4eclipse.ant.platform.core.PathComponent;
import org.ant4eclipse.lib.platform.model.resource.EclipseProject;
import org.ant4eclipse.lib.platform.model.resource.ProjectNature;

/**
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 */
public class PlatformExecutorValuesProvider {

  /** the key for the project name property */
  public static final String PROJECT_NAME           = "project.name";

  /** the key for the project directory property */
  public static final String PROJECT_DIRECTORY      = "project.directory";

  /** the key for the project directory path */
  public static final String PROJECT_DIRECTORY_PATH = "project.directory.path";

  /** the internally used path component */
  private PathComponent      _pathComponent;

  /**
   * <p>
   * The path delegate.
   * </p>
   * 
   * @param pathComponent
   */
  // Assure.notNull( "pathComponent", pathComponent );
  public PlatformExecutorValuesProvider( PathComponent pathComponent ) {
    _pathComponent = pathComponent;
  }

  /**
   * <p>
   * </p>
   * 
   * @param eclipseProject
   * @param executionValues
   */
  // Assure.notNull( "eclipseProject", eclipseProject );
  // Assure.notNull( "executionValues", executionValues );
  public void provideExecutorValues( EclipseProject eclipseProject, MacroExecutionValues executionValues ) {

    // create scoped properties
    executionValues.getProperties()
        .put( PlatformExecutorValuesProvider.PROJECT_NAME, eclipseProject.getSpecifiedName() );
    executionValues.getProperties().put( PlatformExecutorValuesProvider.PROJECT_DIRECTORY,
        _pathComponent.convertToString( eclipseProject.getFolder() ) );
    for( ProjectNature projectNature : eclipseProject.getNatures() ) {
      executionValues.getProperties().put( projectNature.getName(), Boolean.TRUE.toString() );
    }
    // create scoped references
    executionValues.getReferences().put( PlatformExecutorValuesProvider.PROJECT_DIRECTORY_PATH,
        _pathComponent.convertToPath( eclipseProject.getFolder() ) );
  }
  
} /* ENDCLASS */
