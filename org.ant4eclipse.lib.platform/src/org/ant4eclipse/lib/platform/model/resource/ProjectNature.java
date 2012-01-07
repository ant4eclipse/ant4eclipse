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

import java.util.HashSet;
import java.util.Hashtable;
import java.util.Map;
import java.util.Set;

/**
 * <p>
 * Encapsulates a nature of a project. A project can have multiple natures.
 * </p>
 * 
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 * @author Daniel Kasmeroglu (Daniel.Kasmeroglu@kasisoft.net)
 */
public class ProjectNature {

  private static final Map<String,ProjectNature> NATURES = new Hashtable<String,ProjectNature>();

  private String                                 nature;

  /**
   * <p>
   * Creates a new instance of this nature.
   * </p>
   * 
   * @param name
   *          The full name of the nature. Neither <code>null</code> nor empty.
   */
  private ProjectNature( String name ) {
    nature = name;
  }

  /**
   * <p>
   * Returns the name of this nature.
   * </p>
   * 
   * @return The name of this nature. Neither <code>null</code> nor empty.
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

  /**
   * This function is used to create a new instance of a <code>ProjectNature</code>. The main advantage of this helper
   * is the reduction of <code>ProjectNature</code> instances which will have an impact on comparison operations, too.
   * 
   * @param name
   *          The name of the desired nature. Neither <code>null</code> nor empty.
   * 
   * @return The nature associated with the supplied name. Not <code>null</code>.
   */
  public static final synchronized ProjectNature createNature( String name ) {
    Assure.nonEmpty( "name", name );
    ProjectNature result = NATURES.get( name );
    if( result == null ) {
      result = new ProjectNature( name );
      NATURES.put( name, result );
    }
    return result;
  }

  /**
   * This function is used to create a set of new instances of <code>ProjectNature</code>. The main advantage of this
   * helper is the reduction of <code>ProjectNature</code> instances which will have an impact on comparison operations,
   * too.
   * 
   * @param names
   *          A list of nature names. Maybe <code>null</code>.
   * 
   * @return The natures associated with the supplied names. Maybe <code>null</code>.
   */
  public static final Set<ProjectNature> createNatures( String ... names ) {
    Set<ProjectNature> result = null;
    if( names != null ) {
      result = new HashSet<ProjectNature>();
      for( String name : names ) {
        result.add( createNature( name ) );
      }
    }
    return result;
  }

} /* ENDCLASS */
