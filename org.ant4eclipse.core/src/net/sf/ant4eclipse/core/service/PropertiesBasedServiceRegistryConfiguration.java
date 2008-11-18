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

import net.sf.ant4eclipse.core.Ant4EclipseConfigurationProperties;

/**
 * The PropertiesBasedServiceRegistryConfiguration configures the ServiceRegistry with properties from
 * {@link Ant4EclipseConfigurationProperties}.
 * 
 * <p>
 * The properties that are used to configure the registry must have the service identifier as key and the service'
 * implementation class as value. The key must be prefixed with {@value #PROPERTY_PREFIX}
 * 
 * @author Nils Hartmann <nils@nilshartmann.net>
 * @version $Revision$
 */
public class PropertiesBasedServiceRegistryConfiguration implements ServiceRegistryConfiguration {

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
  public static final String PROPERTY_PREFIX = "service";

  public void configure(ConfigurationContext context) {

    // get all properties describing a service
    Ant4EclipseConfigurationProperties configProperties = Ant4EclipseConfigurationProperties.getInstance();
    Iterable<String[]> serviceProperties = configProperties.getAllProperties(PROPERTY_PREFIX);

    // Iterate over all service descriptions found
    for (String[] serviceProperty : serviceProperties) {
      String serviceInterfaceName = serviceProperty[0];
      String serviceImplementationName = serviceProperty[1];

      // instantiate new service instance
      Object serviceInstance = createServiceInstance(serviceImplementationName);

      // register new service
      context.registerService(serviceInstance, serviceInterfaceName);
    }

  }

  /**
   * Creates a new instance of the given service implementation.
   * 
   * <p>
   * The service class must have a public default constructor that will be used to instantiate the service
   * 
   * @param serviceImplementationName
   *          The name of the service class
   * @return the new service instance
   */
  protected Object createServiceInstance(String serviceImplementationName) {
    Class<?> serviceType;
    try {
      serviceType = Class.forName(serviceImplementationName);
    } catch (Exception ex) {
      throw new RuntimeException("Could not load service implementation class '" + serviceImplementationName + "': "
          + ex, ex);
    }

    Object instance;
    try {
      instance = serviceType.newInstance();
    } catch (Exception ex) {
      throw new RuntimeException("Could not instantiate service '" + serviceType + "': " + ex, ex);
    }

    return instance;
  }

}
