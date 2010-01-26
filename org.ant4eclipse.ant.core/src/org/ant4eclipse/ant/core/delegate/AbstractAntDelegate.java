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
package org.ant4eclipse.ant.core.delegate;


import org.ant4eclipse.lib.core.Assure;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.ProjectComponent;

/**
 * <p>
 * Abstract base class for all ant4eclipse ant delegates. This class enables access to ANT's {@link ProjectComponent}.
 * </p>
 * 
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 */
public abstract class AbstractAntDelegate {

  /** the project component */
  private ProjectComponent _component;

  /**
   * <p>
   * Creates a new instance of type {@link AbstractAntDelegate}.
   * </p>
   * 
   * @param component
   *          the project component
   */
  public AbstractAntDelegate(ProjectComponent component) {
    Assure.notNull(component);

    this._component = component;
  }

  /**
   * <p>
   * Returns the associated {@link ProjectComponent}.
   * </p>
   * 
   * @return the project component.
   */
  protected ProjectComponent getProjectComponent() {
    return this._component;
  }

  /**
   * <p>
   * Returns the ant project.
   * </p>
   * 
   * @return the Ant {@link Project}.
   */
  protected Project getAntProject() {
    return this._component.getProject();
  }
}
