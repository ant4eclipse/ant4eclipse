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

import org.ant4eclipse.lib.core.Assure;
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
  public AbstractProjectValidator(String ident, Class<?>... roleclasses) {
    Assure.nonEmpty("ident", ident);
    Assure.notNull("roleclasses", roleclasses);

    this._types = roleclasses;
    this._key = ident;
  }

  /**
   * {@inheritDoc}
   */
  public boolean canValidate(ProjectRole role) {
    for (Class<?> type : this._types) {
      if (type.isAssignableFrom(role.getClass())) {
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
   * Warns the user about a validation problem.
   * </p>
   * 
   * @param project
   *          The project used for the warning. Not <code>null</code>.
   * @param message
   *          A warning message. Neither <code>null</code> nor empty.
   */
  protected void addWarning(EclipseProject project, String message) {
    A4ELogging.warn("Project '%s': %s > %s", project.getSpecifiedName(), this._key, message);
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
  protected void addError(EclipseProject project, String message) {
    A4ELogging.error("Project '%s': %s > %s", project.getSpecifiedName(), this._key, message);
  }

} /* ENDCLASS */
