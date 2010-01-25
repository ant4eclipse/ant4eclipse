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
package org.ant4eclipse.lib.core.service;

import org.ant4eclipse.lib.core.exception.Ant4EclipseException;

/**
 * ConfigurationContext --
 * 
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 */
public interface ConfigurationContext {

  /**
   * <p>
   * Associates the specified service with the specified service identifier. If the service identifier is already set, a
   * {@link Ant4EclipseException} will be thrown.
   * </p>
   * 
   * @param service
   *          the service
   * @param serviceIdentifier
   *          the service identifier
   */
  void registerService(Object service, String serviceIdentifier);

  /**
   * <p>
   * Associates the specified service with the specified service identifiers. If one of the service identifiers is
   * already set, a {@link Ant4EclipseException} will be thrown.
   * </p>
   * 
   * @param service
   *          the service
   * @param serviceIdentifiers
   *          the service identifiers
   */
  void registerService(Object service, String[] serviceIdentifiers);

} /* ENDINTERFACE */
