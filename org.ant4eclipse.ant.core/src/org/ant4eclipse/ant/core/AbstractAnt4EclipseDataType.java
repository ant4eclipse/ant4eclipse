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
package org.ant4eclipse.ant.core;

import org.ant4eclipse.lib.core.logging.Ant4EclipseLogger;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.PropertyHelper;
import org.apache.tools.ant.types.DataType;

import java.util.ArrayList;
import java.util.List;

/**
 * Base type for all ant4eclipse types.
 * 
 * <p>
 * Used to configure ant4eclipse runtime environment if necessary
 * 
 * @author Nils Hartmann (nils@nilshartmann.net)
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 */
public abstract class AbstractAnt4EclipseDataType extends DataType {

  private static List<AbstractAnt4EclipseDataType> instances  = new ArrayList<AbstractAnt4EclipseDataType>();

  private boolean validated;

  /**
   * <p>
   * Creates a new instance of type AbstractAnt4EclipseDataType.
   * </p>
   * 
   * @param project
   */
  public AbstractAnt4EclipseDataType( Project project ) {

    validated = false;
    
    setProject( project );

    // add instance
    synchronized( instances ) {
      instances.add( this );
    }
    // configure ant4eclipse
    configureA4E( project );

  }

  /**
   * <p>
   * Configures Ant4Eclipse in a ant based environment (the standard case).
   * </p>
   * 
   * @param project
   *          the ant project
   */
  private void configureA4E( Project project ) {
    // set ant4eclipse property helper
    PropertyHelper.getPropertyHelper( project ).setNext( new ThreadDispatchingPropertyHelper( project ) );
    Ant4EclipseLogger logger = new AntBasedLogger();
    project.addBuildListener( new ProjectBuildListener( logger ) );
  }

  /**
   * <p>
   * </p>
   * 
   * @return the validated
   */
  public boolean isValidated() {
    return validated;
  }

  /**
   * <p>
   * </p>
   */
  public final void validate() {
    if( ! validated ) {
      doValidate();
      validated = true;
    }
  }

  /**
   * <p>
   * </p>
   */
  protected void doValidate() {
  }

  /**
   * <p>
   * Validates all registered {@link AbstractAnt4EclipseDataType}.
   * </p>
   */
  static void validateAll() {

    // add to instances map
    synchronized( instances ) {

      // iterate over the registered AbstractAnt4EclipseDataTypes
      for( AbstractAnt4EclipseDataType dataType : instances ) {
        dataType.validate();
      }
    }
  }
  
} /* ENDCLASS */
