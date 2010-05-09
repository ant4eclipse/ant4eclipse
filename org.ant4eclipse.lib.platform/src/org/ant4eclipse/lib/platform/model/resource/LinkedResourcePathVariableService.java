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
package org.ant4eclipse.lib.platform.model.resource;

/**
 * <p>
 * </p>
 * 
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 * 
 */
public interface LinkedResourcePathVariableService {

  /**
   * <p>
   * </p>
   * 
   * @param pathVariable
   */
  String getLinkedResourcePath(String pathVariable);

  /**
   * <p>
   * </p>
   * 
   * @param pathVariable
   * @param location
   */
  void registerLinkedResourcePathVariable(String pathVariable, String location);
}
