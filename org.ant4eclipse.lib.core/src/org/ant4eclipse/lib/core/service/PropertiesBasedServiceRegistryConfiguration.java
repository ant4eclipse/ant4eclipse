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
import org.ant4eclipse.lib.core.configuration.Ant4EclipseConfiguration;
import org.ant4eclipse.lib.core.util.Pair;
import org.ant4eclipse.lib.core.util.Utilities;

/**
 * The PropertiesBasedServiceRegistryConfiguration configures the ServiceRegistry with properties from
 * {@link Ant4EclipseConfiguration}.
 * 
 * <p>
 * The properties that are used to configure the registry must have the service identifier as key and the service'
 * implementation class as value. The key must be prefixed with {@value #PROPERTY_PREFIX}
 * 
 * @author Nils Hartmann <nils@nilshartmann.net>
 * @version $Revision$
 */
public class PropertiesBasedServiceRegistryConfiguration implements ServiceRegistryConfiguration {

  private Ant4EclipseConfiguration _ant4EclipseConfiguration;

  /**
   * The prefix of properties that should be interpreted as service description.
   * 
   * <p>
   * These properties must have the following form:
   * 
   * <pre>
   * PROPERTY_PREFIX.serviceName = full.qualified.ServiceName
   * </pre>
   * 
   */
  public static final String       PROPERTY_PREFIX = "service";

  /**
   * <p>
   * Sets up this configuration for a {@link ServiceRegistry}.
   * </p>
   * 
   * @param ant4EclipseConfiguration
   *          The configuration to be used. Not <code>null</code>.
   */
  public PropertiesBasedServiceRegistryConfiguration(Ant4EclipseConfiguration ant4EclipseConfiguration) {
    Assure.notNull("ant4EclipseConfiguration", ant4EclipseConfiguration);
    this._ant4EclipseConfiguration = ant4EclipseConfiguration;
  }

  /**
   * {@inheritDoc}
   */
  public void configure(ConfigurationContext context) {
    Assure.notNull("context", context);

    // get all properties describing a service
    Iterable<Pair<String, String>> serviceProperties = this._ant4EclipseConfiguration.getAllProperties(PROPERTY_PREFIX);

    // Iterate over all service descriptions found
    for (Pair<String, String> serviceProperty : serviceProperties) {

      // instantiate new service instance
      Object serviceInstance = Utilities.newInstance(serviceProperty.getSecond());

      // register new service
      context.registerService(serviceInstance, serviceProperty.getFirst());
    }

  }

} /* ENDCLASS */
