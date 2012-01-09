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

import org.ant4eclipse.lib.core.logging.A4ELevel;
import org.ant4eclipse.lib.core.logging.A4ELogging;
import org.ant4eclipse.lib.platform.model.resource.EclipseProject;
import org.ant4eclipse.lib.platform.model.resource.role.ProjectRole;

/**
 * <p>
 * Base implementation of a validator.
 * </p>
 * 
 * @author Daniel Kasmeroglu (daniel.kasmeroglu@kasisoft.net)
 */
public abstract class AbstractProjectValidator implements ProjectValidator {

  private Class<?>[] _types;

  private String     _key;

  /**
   * <p>
   * Initialises this basic implementation of a validator.
   * </p>
   * 
   * @param ident
   *          The identifier used to identify this validator. Neither <code>null</code> nor empty.
   * @param roleclasses
   *          The types of role supported by this validator. Not <code>null</code>.
   */
  // Assure.nonEmpty( "ident", ident );
  // Assure.notNull( "roleclasses", roleclasses );
  public AbstractProjectValidator( String ident, Class<?> ... roleclasses ) {
    _types = roleclasses;
    _key = ident;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean canValidate( ProjectRole role ) {
    for( Class<?> type : _types ) {
      if( type.isAssignableFrom( role.getClass() ) ) {
        return true;
      }
    }
    return false;
  }

  /**
   * @todo [03-Dec-2009:KASI] At the moment validation inconsistencies will only be logged. We probably might want to
   *       attach them to the projects.
   */

  /**
   * <p>
   * Informs the user about a validation problem.
   * </p>
   * 
   * @param project
   *          The project used for the message. Not <code>null</code>.
   * @param level
   *          The log level for the message.
   * @param message
   *          A validation message. Neither <code>null</code> nor empty.
   */
  protected void addMessage( EclipseProject project, A4ELevel level, String message ) {
    A4ELogging.log( level, "Project '%s': %s > %s", project.getSpecifiedName(), _key, message );
  }

  /**
   * <p>
   * Warns the user about a validation problem.
   * </p>
   * 
   * @param project
   *          The project used for the warning. Not <code>null</code>.
   * @param message
   *          A warning message. Neither <code>null</code> nor empty.
   */
  protected void addWarning( EclipseProject project, String message ) {
    A4ELogging.warn( "Project '%s': %s > %s", project.getSpecifiedName(), _key, message );
  }

  /**
   * <p>
   * Notifies the user with an error about a validation problem.
   * </p>
   * 
   * @param project
   *          The project used for the error. Not <code>null</code>.
   * @param message
   *          An error message. Neither <code>null</code> nor empty.
   */
  protected void addError( EclipseProject project, String message ) {
    A4ELogging.error( "Project '%s': %s > %s", project.getSpecifiedName(), _key, message );
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Integer getPriority() {
    return null;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void reset() {
  }

} /* ENDCLASS */
