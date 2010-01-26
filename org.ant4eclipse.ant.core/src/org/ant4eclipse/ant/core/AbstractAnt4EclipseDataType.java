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


import org.ant4eclipse.lib.core.Ant4EclipseConfigurator;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.types.DataType;

import java.util.HashSet;
import java.util.Set;

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

  /** - */
  private static Set<AbstractAnt4EclipseDataType> instances  = new HashSet<AbstractAnt4EclipseDataType>();

  /** - */
  private boolean                                 _validated = false;

  /**
   * <p>
   * Creates a new instance of type AbstractAnt4EclipseDataType.
   * </p>
   * 
   * @param project
   */
  public AbstractAnt4EclipseDataType(Project project) {
    setProject(project);

    // add instance
    synchronized (instances) {
      instances.add(this);
    }
    // configure ant4eclipse
    AntConfigurator.configureAnt4Eclipse(project);

  }

  /**
   * <p>
   * </p>
   * 
   * @return the validated
   */
  public boolean isValidated() {
    return this._validated;
  }

  /**
   * <p>
   * </p>
   */
  public final void validate() {
    if (this._validated) {
      return;
    }

    doValidate();

    this._validated = true;
  }

  /**
   * <p>
   * </p>
   */
  protected void doValidate() {
    //
  }

  /**
   * <p>
   * Validates all registered {@link AbstractAnt4EclipseDataType}.
   * </p>
   */
  static void validateAll() {

    // add to instances map
    synchronized (instances) {

      // iterate over the registered AbstractAnt4EclipseDataTypes
      for (AbstractAnt4EclipseDataType dataType : instances) {
        dataType.validate();
      }
    }
  }
}
