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

import org.ant4eclipse.lib.core.Assure;

/**
 * <p>
 * This class provides access to the ServiceRegistry independent from the running context.
 * </p>
 * 
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 * @author Daniel Kasmeroglu (Daniel.Kasmeroglu@Kasisoft.net)
 */
public class ServiceRegistryAccess {

  /** the instance */
  private static ServiceRegistry _instance;

  /**
   * <p>
   * Configures the {@link ServiceRegistry}. The registry has to be configured before it can be used.
   * </p>
   * 
   * @param configuration
   *          the service registry configuration
   */
  public static final void configure(ServiceRegistryConfiguration configuration) {
    Assure.notNull("configuration", configuration);
    Assure.assertTrue(!isConfigured(), "ServiceRegistry already is configured.");
    _instance = new ServiceRegistry(configuration);
  }

  /**
   * <p>
   * This function allows to restore a registry. The supplied registry is configured as this {@link #instance()} is the
   * only way to access an instance.
   * </p>
   * 
   * @param registry
   *          The registry used to be restored. Not <code>null</code>.
   */
  public static final void restore(ServiceRegistry registry) {
    Assure.notNull("registry", registry);
    _instance = registry;
  }

  /**
   * <p>
   * Returns <code>true</code> if the {@link ServiceRegistry} already is configured, <code>false</code> otherwise.
   * </p>
   * 
   * @return <code>true</code> if the {@link ServiceRegistry} already is configured, <code>false</code> otherwise.
   */
  public static final boolean isConfigured() {
    return _instance != null;
  }

  /**
   * <p>
   * Resets the {@link ServiceRegistry}.
   * </p>
   */
  public static final void reset() {
    _instance = null;
  }

  /**
   * <p>
   * Returns the instance.
   * </p>
   * 
   * @return the instance.
   */
  public static final ServiceRegistry instance() {
    Assure.assertTrue(isConfigured(), "ServiceRegistry has to be configured.");
    return _instance;
  }

}
