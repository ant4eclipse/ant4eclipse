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
package org.ant4eclipse.ant.pde;

import org.ant4eclipse.ant.core.AbstractAnt4EclipseDataType;
import org.ant4eclipse.lib.core.A4ECore;
import org.ant4eclipse.lib.core.exception.Ant4EclipseException;
import org.ant4eclipse.lib.pde.PdeExceptionCode;
import org.ant4eclipse.lib.pde.tools.PlatformConfiguration;
import org.ant4eclipse.lib.pde.tools.TargetPlatformRegistry;
import org.apache.tools.ant.Project;

/**
 * <p>
 * Implements a ant datatype that allows to define a {@link PlatformConfiguration}.
 * </p>
 * 
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 */
public class PlatformConfigurationDataType extends AbstractAnt4EclipseDataType {

  /** the id of the target platform configuration */
  private String                _id;

  /** the target platform configuration */
  private PlatformConfiguration _targetPlatformConfiguration;

  /**
   * <p>
   * Creates a new instance of type {@link PlatformConfigurationDataType}.
   * </p>
   * 
   * @param project
   *          the project
   */
  public PlatformConfigurationDataType( Project project ) {
    super( project );

    // create a new TargetPlatformConfiguration
    this._targetPlatformConfiguration = new PlatformConfiguration();
  }

  /**
   * <p>
   * Sets the id of the target platform location.
   * </p>
   * 
   * @param id
   *          the id of the target platform location.
   */
  public void setId( String id ) {
    if( isReference() ) {
      throw tooManyAttributes();
    }

    this._id = id;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected void doValidate() {
    if( this._id == null || "".equals( this._id ) ) {
      throw new Ant4EclipseException( PdeExceptionCode.ANT_ATTRIBUTE_NOT_SET, "id" );
    }
    // add the target platform definition
    TargetPlatformRegistry targetPlatformRegistry = A4ECore.instance()
        .getRequiredService( TargetPlatformRegistry.class );
    targetPlatformRegistry.addPlatformConfiguration( this._id, this._targetPlatformConfiguration );
  }

  /**
   * <p>
   * </p>
   * 
   * @param value
   *          the architecture
   */
  public void setArchitecture( String value ) {
    this._targetPlatformConfiguration.setArchitecture( value );
  }

  /**
   * <p>
   * </p>
   * 
   * @param value
   *          the platform configuration
   */
  public void setLanguageSetting( String value ) {
    this._targetPlatformConfiguration.setLanguageSetting( value );
  }

  /**
   * <p>
   * </p>
   * 
   * @param value
   *          the operating system
   */
  public void setOperatingSystem( String value ) {
    this._targetPlatformConfiguration.setOperatingSystem( value );
  }

  /**
   * <p>
   * </p>
   * 
   * @param value
   *          the windowing system
   */
  public void setWindowingSystem( String value ) {
    this._targetPlatformConfiguration.setWindowingSystem( value );
  }
  
} /* ENDCLASS */
