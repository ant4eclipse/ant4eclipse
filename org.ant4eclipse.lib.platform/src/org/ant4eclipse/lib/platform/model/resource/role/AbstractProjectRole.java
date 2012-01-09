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

import org.ant4eclipse.lib.core.Assure;
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
  public AbstractProjectRole( String name, EclipseProject eclipseProject ) {
    Assure.nonEmpty( "name", name );
    Assure.notNull( "eclipseProject", eclipseProject );
    _name = name;
    _eclipseProject = eclipseProject;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public final String getName() {
    return _name;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public EclipseProject getEclipseProject() {
    return _eclipseProject;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean equals( Object o ) {
    if( this == o ) {
      return true;
    }
    if( o == null ) {
      return false;
    }
    if( o.getClass() != getClass() ) {
      return false;
    }
    AbstractProjectRole castedObj = (AbstractProjectRole) o;
    return(_name == null ? castedObj._name == null : _name.equals( castedObj._name ));
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public int hashCode() {
    int hashCode = 1;
    hashCode = 31 * hashCode + (_name == null ? 0 : _name.hashCode());
    return hashCode;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String toString() {
    return String.format( "[AbstractProjectRole: _name: %s _eclipseProject: %s]", _name, _eclipseProject );
  }
  
} /* ENDCLASS */
