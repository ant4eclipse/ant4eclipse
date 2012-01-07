/**********************************************************************
 * Copyright (c) 2005-2010 ant4eclipse project team.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Nils Hartmann, Daniel Kasmeroglu, Gerd Wuetherich
 **********************************************************************/
package org.ant4eclipse.ant.platform;

import org.ant4eclipse.ant.core.AbstractAnt4EclipseDataType;
import org.ant4eclipse.lib.core.A4ECore;
import org.ant4eclipse.lib.core.util.Utilities;
import org.ant4eclipse.lib.platform.model.resource.LinkedResourcePathVariableService;
import org.apache.tools.ant.Project;

/**
 * <p>
 * Defines a path variable for a linked resource.
 * </p>
 * 
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 */
public class LinkedResourcePathVariable extends AbstractAnt4EclipseDataType {

  private String _name;

  private String _location;

  /**
   * <p>
   * Creates a new instance of type {@link LinkedResourcePathVariable}.
   * </p>
   * 
   * @param project
   */
  public LinkedResourcePathVariable( Project project ) {
    super( project );
  }

  /**
   * <p>
   * </p>
   * 
   * @return
   */
  public String getName() {
    return _name;
  }

  /**
   * <p>
   * </p>
   * 
   * @param name
   */
  public void setName( String name ) {
    _name = name;
  }

  /**
   * <p>
   * </p>
   * 
   * @return
   */
  public String getLinkedResourceLocation() {
    return _location;
  }

  /**
   * <p>
   * </p>
   * 
   * @param location
   */
  public void setLocation( String location ) {
    _location = location;
  }

  /**
   * @see org.ant4eclipse.ant.core.AbstractAnt4EclipseDataType#doValidate()
   */
  @Override
  protected void doValidate() {

    // fetch the ClassPathElementsRegistry
    LinkedResourcePathVariableService variableService = A4ECore.instance().getRequiredService(
        LinkedResourcePathVariableService.class );

    //
    if( !Utilities.hasText( _name ) ) {
      // TODO: Ant4EclipseException
      throw new RuntimeException( "Missing attribute 'name'." );
    }

    if( !Utilities.hasText( _location ) ) {
      // TODO: Ant4EclipseException
      throw new RuntimeException( "Missing attribute 'location'." );
    }

    // register the variable
    variableService.registerLinkedResourcePathVariable( _name, _location );
  }

} /* ENDCLASS */
