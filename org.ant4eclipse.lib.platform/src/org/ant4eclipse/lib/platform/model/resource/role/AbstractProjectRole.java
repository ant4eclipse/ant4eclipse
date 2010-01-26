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
package org.ant4eclipse.lib.platform.model.resource.role;



import org.ant4eclipse.lib.core.Assert;
import org.ant4eclipse.lib.platform.model.resource.EclipseProject;

/**
 * <p>
 * Abstract base class for all {@link ProjectRole ProjectRoles}.
 * </p>
 * 
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 */
public abstract class AbstractProjectRole implements ProjectRole {

  /** the name of the project role */
  private String         _name;

  /** the eclipse project */
  private EclipseProject _eclipseProject;

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
  public AbstractProjectRole(String name, EclipseProject eclipseProject) {
    Assert.nonEmpty(name);
    Assert.notNull(eclipseProject);

    this._name = name;
    this._eclipseProject = eclipseProject;
  }

  /**
   * {@inheritDoc}
   */
  public final String getName() {
    return this._name;
  }

  /**
   * {@inheritDoc}
   */
  public EclipseProject getEclipseProject() {
    return this._eclipseProject;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null) {
      return false;
    }
    if (o.getClass() != getClass()) {
      return false;
    }
    AbstractProjectRole castedObj = (AbstractProjectRole) o;
    return ((this._name == null ? castedObj._name == null : this._name.equals(castedObj._name)));
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public int hashCode() {
    int hashCode = 1;
    hashCode = 31 * hashCode + (this._name == null ? 0 : this._name.hashCode());
    return hashCode;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String toString() {
    StringBuffer buffer = new StringBuffer();
    buffer.append("[AbstractProjectRole:");
    buffer.append(" _name: ");
    buffer.append(this._name);
    buffer.append(" _eclipseProject: ");
    buffer.append(this._eclipseProject);
    buffer.append("]");
    return buffer.toString();
  }
} /* ENDCLASS */