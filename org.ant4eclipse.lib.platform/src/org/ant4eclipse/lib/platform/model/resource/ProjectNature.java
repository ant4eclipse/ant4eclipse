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
package org.ant4eclipse.lib.platform.model.resource;

import org.ant4eclipse.lib.core.Assure;

/**
 * <p>
 * Encapsulates a nature of a project. A project can have multiple natures.
 * </p>
 * 
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 * @author Daniel Kasmeroglu (Daniel.Kasmeroglu@kasisoft.net)
 */
public class ProjectNature {

  private String nature;

  /**
   * <p>Creates a new instance of this nature.</p>
   * 
   * @param name   The full name of the nature. Neither <code>null</code> nor empty.
   */
  public ProjectNature( String name ) {
    Assure.nonEmpty( "name", name );
    nature = name;
  }

  /**
   * <p>
   * Returns the name of this nature.
   * </p>
   * 
   * @return  The name of this nature. Neither <code>null</code> nor empty.
   */
  public String getName() {
    return nature;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean equals( Object obj ) {
    if( this == obj ) {
      return true;
    }
    if( obj == null ) {
      return false;
    }
    if( obj.getClass() != getClass() ) {
      return false;
    }
    ProjectNature other = (ProjectNature) obj;
    return nature.equals( other.nature );
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public int hashCode() {
    return nature.hashCode();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String toString() {
    return String.format( "[ProjectNature: nature: %s]", nature );
  }
  
} /* ENDCLASS */