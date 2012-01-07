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

import org.ant4eclipse.lib.platform.model.resource.LinkedResourcePathVariableService;

import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 * Implements the {@link LinkedResourcePathVariableService}.
 * </p>
 * 
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 * @author Daniel Kasmeroglu (Daniel.Kasmeroglu@kasisoft.net)
 */
public class LinkedResourcePathVariableServiceImpl implements LinkedResourcePathVariableService {

  private Map<String,String> _variables;

  public LinkedResourcePathVariableServiceImpl() {
    _variables = new HashMap<String,String>();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getLinkedResourcePath( String pathVariable ) {
    return _variables.get( pathVariable );
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void registerLinkedResourcePathVariable( String pathVariable, String location ) {
    _variables.put( pathVariable, location );
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
    _variables.clear();
  }

} /* ENDCLASS */
