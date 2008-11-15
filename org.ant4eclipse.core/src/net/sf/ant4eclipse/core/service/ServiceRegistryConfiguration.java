/**********************************************************************
 * Copyright (c) 2005-2008 ant4eclipse project team.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Nils Hartmann, Daniel Kasmeroglu, Gerd Wuetherich
 **********************************************************************/
package net.sf.ant4eclipse.core.service;

/**
 * ServiceRegistryConfiguration --
 * 
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 */
public interface ServiceRegistryConfiguration {

  /**
   * <p>
   * Call-back method that must be implemented to configure the {@link ServiceRegistry}.
   * </p>
   * 
   * @param context
   *          the configuration context
   */
  public void configure(final ConfigurationContext context);

  /**
   * ConfigurationContext --
   * 
   * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
   */
  public interface ConfigurationContext {

    /**
     * <p>
     * Associates the specified service with the specified service identifier. If the service identifier is already set,
     * a {@link NoUniqueIdentifierException} will be thrown.
     * </p>
     * 
     * @param service
     *          the service
     * @param serviceIdentifier
     *          the service identifier
     */
    public void registerService(Object service, String serviceIdentifier);

    /**
     * <p>
     * Associates the specified service with the specified service identifiers. If one of the service identifiers is
     * already set, a {@link NoUniqueIdentifierException} will be thrown.
     * </p>
     * 
     * @param service
     *          the service
     * @param serviceIdentifiers
     *          the service identifiers
     */
    public void registerService(Object service, String[] serviceIdentifiers);
  }
}
