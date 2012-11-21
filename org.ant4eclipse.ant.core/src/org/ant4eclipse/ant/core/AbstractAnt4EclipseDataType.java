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

import java.util.LinkedList;
import java.util.List;

import org.apache.tools.ant.Project;
import org.apache.tools.ant.types.DataType;

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
  private static List<AbstractAnt4EclipseDataType> instances  = new LinkedList<AbstractAnt4EclipseDataType>();

  /** - */
  private boolean                                  _validated = false;

  /**
   * <p>
   * Creates a new instance of type AbstractAnt4EclipseDataType.
   * </p>
   * 
   * @param project
   */
  public AbstractAnt4EclipseDataType(Project project) {
    setProject(project);

    // Validate all datatypes known so far in case this data types needs access
    // to earlier defined datatypes
    AbstractAnt4EclipseDataType.validateAll();

    // add instance
    synchronized (instances) {
      if (instances.isEmpty() || (this instanceof HasReferencesDataType)) {
        // add to end
        instances.add(this);
      } else {
        instances.add(0, this);
      }
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
