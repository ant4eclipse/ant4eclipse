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
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.ProjectComponent;
import org.apache.tools.ant.PropertyHelper;
import org.apache.tools.ant.taskdefs.condition.Condition;

/**
 * <p>
 * Abstract base class for all ant4eclipse conditions.
 * </p>
 * 
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 */
public abstract class AbstractAnt4EclipseCondition extends ProjectComponent implements Condition {

  /**
   * {@inheritDoc}
   */
  @Override
  public final boolean eval() throws BuildException {
    
    // Validates the Ant4EclipseDataTypes
    AbstractAnt4EclipseDataType.validateAll();

    // configure ant4eclipse
    configureA4E( getProject() );

    // delegate the implementation
    try {
      return doEval();
    } catch( Exception ex ) {
      throw new BuildException( ex.toString(), ex );
    }
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
   * This method replaces the original <code>eval()</code> method defined in
   * <code>org.apache.tools.ant.taskdefs.condition.Condition</code>. Overwrite this method to implement own condition
   * logic.
   * </p>
   * 
   * @see Task#execute()
   */
  protected abstract boolean doEval();
  
} /* ENDCLASS */
