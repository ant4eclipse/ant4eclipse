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

import org.ant4eclipse.lib.core.configuration.Ant4EclipseConfiguration;
import org.ant4eclipse.lib.core.logging.A4ELogging;
import org.ant4eclipse.lib.core.service.ServiceRegistryAccess;
import org.ant4eclipse.lib.core.util.Pair;
import org.ant4eclipse.lib.core.util.Utilities;
import org.ant4eclipse.lib.platform.model.resource.EclipseProject;
import org.ant4eclipse.lib.platform.model.resource.role.ProjectRole;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * Registry for all {@link ProjectValidator} instances currently part of the runtime.
 * </p>
 * 
 * @author Daniel Kasmeroglu (daniel.kasmeroglu@kasisoft.net)
 */
public class ValidatorRegistry {

  /**
   * The prefix of properties that holds a Validator class name
   */
  public static final String PREFIX_VALIDATOR = "validator";

  private ProjectValidator[] _validators;

  public ValidatorRegistry() {
    init();
  }

  /**
   * Loads the configured RoleIdentifiers
   */
  protected void init() {

    // get all properties that defines a ProjectRoleIdentifier
    Ant4EclipseConfiguration config = ServiceRegistryAccess.instance().getService(Ant4EclipseConfiguration.class);
    Iterable<Pair<String, String>> entries = config.getAllProperties(PREFIX_VALIDATOR);

    List<ProjectValidator> validators = new ArrayList<ProjectValidator>();

    // Instantiate all ProjectRoleIdentifiers
    for (Pair<String, String> types : entries) {
      // we're not interested in the key of a project validator. only the classname (value of the entry) is relevant
      ProjectValidator projectvalidator = Utilities.newInstance(types.getSecond(), types.getFirst());
      A4ELogging.trace("Register ProjectValidator '%s'", projectvalidator);
      validators.add(projectvalidator);
    }

    this._validators = validators.toArray(new ProjectValidator[validators.size()]);
  }

  /**
   * @param project
   */
  public void validate(EclipseProject project) {
    ProjectRole[] roles = project.getRoles();
    for (ProjectRole role : roles) {
      for (ProjectValidator validator : this._validators) {
        if (validator.canValidate(role)) {
          validator.validate(role);
        }
      }
    }
  }

} /* ENDCLASS */
