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
package org.ant4eclipse.ant.platform.core.delegate;


import org.ant4eclipse.ant.platform.core.ProjectReferenceAwareComponent;
import org.apache.tools.ant.BuildException;

/**
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 */
public class ProjectReferenceAwareDelegate implements ProjectReferenceAwareComponent {

  /** project reference types */
  private String[] _projectReferenceTypes;

  /**
   * {@inheritDoc}
   */
  @Override
  public String[] getProjectReferenceTypes() {
    return _projectReferenceTypes;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isProjectReferenceTypesSet() {
    return _projectReferenceTypes != null;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void requireProjectReferenceTypesSet() {
    if( !isProjectReferenceTypesSet() ) {
      // TODO
      throw new BuildException( "referenceTypes has to be set!" );
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setProjectReferenceTypes( String projectReferenceTypes ) {
    //
    if( projectReferenceTypes == null ) {
      _projectReferenceTypes = new String[] {};
    } else {
      String[] names = projectReferenceTypes.split( "," );

      //
      _projectReferenceTypes = new String[names.length];

      for( int i = 0; i < names.length; i++ ) {
        _projectReferenceTypes[i] = names[i].trim();
      }
    }
  }
  
} /* ENDCLASS */
