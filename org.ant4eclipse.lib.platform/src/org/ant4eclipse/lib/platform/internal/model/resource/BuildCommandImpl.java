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
package org.ant4eclipse.lib.platform.internal.model.resource;

import org.ant4eclipse.lib.core.Assure;
import org.ant4eclipse.lib.platform.model.resource.BuildCommand;

/**
 * <p>
 * Encapsulates an build command of an eclipse java project.
 * </p>
 * 
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 */
public class BuildCommandImpl implements BuildCommand {

  /** the name of the build command */
  private String _name;

  /**
   * the 'triggers'-element, or null if not present
   */
  private String _triggers;

  /**
   * @param name
   *          the name of the build command.
   */
  public BuildCommandImpl( String name ) {
    this( name, null );
  }

  /**
   * Creates a new instance of type BuildCommand.
   * 
   * @param name
   *          the name of the build command.
   * @param the
   *          triggers that would cause this builder to run in eclipse (might be null)
   */
  public BuildCommandImpl( String name, String triggers ) {
    Assure.notNull( "name", name );
    _name = name;
    if( (triggers != null) && triggers.endsWith( "," ) ) {
      if( triggers.length() > 1 ) { // remove trailing ,
        triggers = triggers.substring( 0, triggers.length() - 1 );
      } else {
        triggers = null;
      }
    }
    _triggers = triggers;
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
  public String getTriggers() {
    return _triggers;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean hasTriggers() {
    return _triggers != null;
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
    BuildCommandImpl other = (BuildCommandImpl) o;
    if( _name == null ) {
      return other._name == null;
    } else {
      return _name.equals( other._name );
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String toString() {
    return String.format( "[BuildCommand: name: %s triggers: %s]", _name, _triggers );
  }
  
} /* ENDCLASS */
