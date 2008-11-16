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
package net.sf.ant4eclipse.model.platform.resource.role;

import net.sf.ant4eclipse.core.Assert;
import net.sf.ant4eclipse.model.platform.resource.EclipseProject;

/**
 * <p>
 * Abstract base class for all {@link ProjectRole ProjectRoles}.
 * </p>
 * 
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 */
public abstract class AbstractProjectRole implements ProjectRole {

  /** the name of the project role */
  private final String         _name;

  /** the eclipse project */
  private final EclipseProject _eclipseProject;

  /**
   * <p>
   * Creates a new instance of type ProjectRole.
   * </p>
   * 
   * @param name
   *          the name of the project role.
   * @param eclipseProject
   *          the eclipse project
   */
  public AbstractProjectRole(final String name, final EclipseProject eclipseProject) {
    Assert.nonEmpty(name);
    Assert.notNull(eclipseProject);

    this._name = name;
    this._eclipseProject = eclipseProject;
  }

  /**
   * <p>
   * Returns the name of the project role.
   * </p>
   * 
   * @return the name of the project role.
   */
  public final String getName() {
    return this._name;
  }

  /**
   * <p>
   * Returns the associated eclipse project.
   * </p>
   * 
   * @return the associated eclipse project.
   */
  public EclipseProject getEclipseProject() {
    return this._eclipseProject;
  }

  /**
   * {@inheritDoc}
   */
  public boolean equals(final Object o) {
    if (this == o) {
      return true;
    }
    if (o == null) {
      return false;
    }
    if (o.getClass() != getClass()) {
      return false;
    }
    final AbstractProjectRole castedObj = (AbstractProjectRole) o;
    return ((this._name == null ? castedObj._name == null : this._name.equals(castedObj._name)));
  }

  /**
   * {@inheritDoc}
   */
  public int hashCode() {
    int hashCode = 1;
    hashCode = 31 * hashCode + (this._name == null ? 0 : this._name.hashCode());
    return hashCode;
  }

  /**
   * {@inheritDoc}
   */
  public String toString() {
    final StringBuffer buffer = new StringBuffer();
    buffer.append("[AbstractProjectRole:");
    buffer.append(" _name: ");
    buffer.append(this._name);
    buffer.append(" _eclipseProject: ");
    buffer.append(this._eclipseProject);
    buffer.append("]");
    return buffer.toString();
  }
} /* ENDCLASS */