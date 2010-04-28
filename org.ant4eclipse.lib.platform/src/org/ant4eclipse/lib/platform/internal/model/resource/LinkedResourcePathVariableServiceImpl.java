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

import org.ant4eclipse.lib.core.Lifecycle;
import org.ant4eclipse.lib.platform.model.resource.LinkedResourcePathVariableService;

import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 * Implements the {@link LinkedResourcePathVariableService}.
 * </p>
 * 
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 */
public class LinkedResourcePathVariableServiceImpl implements LinkedResourcePathVariableService, Lifecycle {

  /** indicates if the service has been initialized or not */
  private boolean             _isInitialized;

  /** the variables map */
  private Map<String, String> _variables;

  /**
   * @see org.ant4eclipse.lib.platform.model.resource.LinkedResourcePathVariableService#getLinkedResourcePath(java.lang.String)
   */
  public String getLinkedResourcePath(String pathVariable) {
    return this._variables.get(pathVariable);
  }

  /**
   * @see org.ant4eclipse.lib.platform.model.resource.LinkedResourcePathVariableService#registerLinkedResourcePathVariable(java.lang.String,
   *      java.lang.String)
   */
  public void registerLinkedResourcePathVariable(String pathVariable, String location) {
    this._variables.put(pathVariable, location);
  }

  /**
   * @see org.ant4eclipse.lib.core.Lifecycle#initialize()
   */
  public void initialize() {

    // initialize the variables map
    this._variables = new HashMap<String, String>();

    // set initialized
    this._isInitialized = true;
  }

  /**
   * @see org.ant4eclipse.lib.core.Lifecycle#isInitialized()
   */
  public boolean isInitialized() {
    return this._isInitialized;
  }

  /**
   * @see org.ant4eclipse.lib.core.Lifecycle#dispose()
   */
  public void dispose() {

    // clears the variables map
    this._variables.clear();
    this._variables = null;

    // set initialize to false
    this._isInitialized = false;
  }
}
