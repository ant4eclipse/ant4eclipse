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
package org.ant4eclipse.core.ant;

import org.ant4eclipse.core.Ant4EclipseConfigurator;
import org.ant4eclipse.core.exception.Ant4EclipseException;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.Task;

/**
 * <p>
 * Abstract base class for all ant4eclipse tasks.
 * </p>
 * 
 * <p>
 * This class configures Ant4Eclipse before executing the task.
 * </p>
 * 
 * @see Ant4EclipseConfigurator#configureAnt4Eclipse(Project)
 * 
 * @author Nils Hartmann (nils@nilshartmann.net)
 */
public abstract class AbstractAnt4EclipseTask extends Task {

  /**
   * Default Constructor used by Ant to create a new instance of this Task
   */
  public AbstractAnt4EclipseTask() {
    super();
  }

  /**
   * <p>
   * Overrides the <code>setProject</code> method defined in <code>org.ant4eclipse.ant.Task</code> to configure
   * Ant4Eclipse after the <tt>project</tt> has been set
   * </p>
   */
  @Override
  public void setProject(final Project project) {
    super.setProject(project);

    // configure ant4eclipse
    Ant4EclipseConfigurator.configureAnt4Eclipse(getProject());
  }

  /**
   * Delegates to {@link #doExecute()} where the actual task logic should be implemented.
   */
  @Override
  public final void execute() throws BuildException {
    try {
      // Validates the Ant4EclipseDatattypes
      AbstractAnt4EclipseDataType.validateAll();
      preconditions();
      doExecute();
    } catch (Exception ex) {
      throw new BuildException(ex.toString(), ex);
    }
  }

  /**
   * Will be called prior to the execution of an ant task. If this method detects a misconfiguration it is supposed to
   * throw a BuildException with an appropriate message. Descending classes need to call the method of the super class.
   * 
   * @throws BuildException
   *           The exception
   */
  protected void preconditions() throws BuildException {
  }

  /**
   * <p>
   * This replaces the "original" <code>execute()</code> method defined in <code>org.ant4eclipse.ant.Task</code>.
   * Overwrite this method to implement own task logic.
   * </p>
   * 
   * @throws Ant4EclipseException
   *           in case something fails
   * 
   * @see Task#execute()
   */
  protected abstract void doExecute();
}
