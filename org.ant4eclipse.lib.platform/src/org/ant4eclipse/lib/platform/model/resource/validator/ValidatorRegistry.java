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
package org.ant4eclipse.lib.platform.model.resource.validator;

import org.ant4eclipse.lib.core.A4ECore;
import org.ant4eclipse.lib.platform.model.resource.EclipseProject;
import org.ant4eclipse.lib.platform.model.resource.role.ProjectRole;

import java.util.List;

/**
 * <p>
 * Registry for all {@link ProjectValidator} instances currently part of the runtime.
 * </p>
 * 
 * @author Daniel Kasmeroglu (daniel.kasmeroglu@kasisoft.net)
 */
public class ValidatorRegistry {

  private ProjectValidator[] _validators;

  public ValidatorRegistry() {
    List<ProjectValidator> validators = A4ECore.instance().getServices( ProjectValidator.class );
    _validators = validators.toArray( new ProjectValidator[validators.size()] );
  }

  /**
   * @param project
   */
  public void validate( EclipseProject project ) {
    List<ProjectRole> roles = project.getRoles();
    for( ProjectRole role : roles ) {
      for( ProjectValidator validator : _validators ) {
        if( validator.canValidate( role ) ) {
          validator.validate( role );
        }
      }
    }
  }

} /* ENDCLASS */
