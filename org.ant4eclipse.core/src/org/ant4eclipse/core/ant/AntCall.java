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

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Task;
import org.apache.tools.ant.taskdefs.Ant;

import java.io.File;
import java.io.FileNotFoundException;
import java.lang.reflect.Field;

/**
 * <p>
 * 
 * </p>
 * 
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 */
public class AntCall extends Ant {

  /**
   * <p>
   * Creates a new instance of type {@link AntCall}.
   * </p>
   */
  public AntCall() {
    super();
  }

  /**
   * <p>
   * Creates a new instance of type {@link AntCall}.
   * </p>
   * 
   * @param owner
   */
  public AntCall(Task owner) {
    super(owner);
  }

  /**
   * {@inheritDoc}
   * 
   * <p>
   * Overrides the <code>execute()</code> method of the {@link Ant} super class. This method will return <b>without</b>
   * an exception if the defined ant file or the defined target is not available.
   * </p>
   */
  @Override
  public void execute() throws BuildException {

    // configure ant4eclipse
    Ant4EclipseConfigurator.configureAnt4Eclipse(getProject());

    try {
      // execute the super class
      super.execute();
    } catch (BuildException buildException) {
      // get the cause of the BuildException
      Throwable cause = getCause(buildException);

      if (cause instanceof FileNotFoundException
          && (cause.getMessage().indexOf(getAntFile()) != -1 || getAntFile() == null)) {
        // ignore - FileNotFoundException is thrown because the given ant file doesn't exist.
        // That's OK here...
      } else if ((buildException.getMessage().indexOf("does not exist in the project") != -1)) {
        buildException.printStackTrace();
        // ignore
      } else {
        throw buildException;
      }
    }
  }

  /**
   * <p>
   * Gets the cause of the {@link BuildException}.
   * </p>
   * 
   * @param throwable
   *          the exception for which the cause should be returned
   * @return the cause
   */
  private Throwable getCause(Throwable throwable) {
    if (throwable instanceof BuildException) {
      return getCause(((BuildException) throwable).getException());
    }
    return throwable;
  }

  /**
   * <p>
   * Helper method that reads the (private) field <code>antfile</code> from the {@link Ant} class. If the private field
   * can't be read, <code>null</code> will be returned.
   * </p>
   * 
   * @return the content of the <code>antfile</code> field
   */
  private String getAntFile() {
    try {
      // retrieve the private field 'antFile' from the super class.
      Field field = Ant.class.getDeclaredField("antFile");
      // make it accessible
      field.setAccessible(true);
      // convert string to file
      File file = new File((String) field.get(this));
      return file.getAbsolutePath();
    } catch (Exception e) {
      // in case that an exception is thrown, null should be returned
      return null;
    }
  }
}
