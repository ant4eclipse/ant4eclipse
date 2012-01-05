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
import org.ant4eclipse.lib.core.CoreExceptionCode;
import org.ant4eclipse.lib.core.Lifecycle;
import org.ant4eclipse.lib.core.exception.Ant4EclipseException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * The {@link ServiceRegistry} implements a singleton that can be used to register and retrieve services. A service is
 * an instance of any kind of class.
 * </p>
 * <p>
 * The service registry must be configured before it can be used. To configure the registry a
 * {@link ServiceRegistryConfiguration} must be implemented: <code><pre>
 * final ServiceRegistryConfiguration configuration = new ServiceRegistryConfiguration() {
 *   public void configure(final ConfigurationContext context) {
 *     // register service
 *     context.registerService(new MyServiceImpl(), MyService.class);
 *   }
 * }; </pre></code>
 * <p>
 * An instance of type {@link ServiceRegistryConfiguration} must be set through the configure() method: <code><pre>
 * ServiceRegistry.configure(configuration);</pre></code>
 * </p>
 * <p>
 * After configuring the registry, services can be requested.
 * </p>
 * 
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 * 
 * @todo [10-Dec-2009:KASI] The static reference should be removed so that classloading won't cleanup existing data
 *       (happens within a taskdef if ant).
 */
public class ServiceRegistry {

  /** the service map **/
  private Map<String, Object> _serviceMap;

  /** list that contains the ordering of the services **/
  private List<Object>        _serviceOrdering;

  /** indicates whether the registry instance is initialized **/
  private boolean             _isInitialized = false;

  /**
   * <p>
   * Returns <code>true</code> if a service with the identifier <code>serviceType.getName()</code> is registered.
   * </p>
   * 
   * @param <T>
   *          The type of the service class.
   * @param serviceType
   *          The service class itself.
   * 
   * @return <code>true</code> <=> A service is registered using the supplied type.
   */
  public <T> boolean hasService(Class<T> serviceType) {
    return hasService(serviceType.getName());
  }

  /**
   * <p>
   * Returns <code>true</code> if a service with the supplied identifier could be found.
   * </p>
   * 
   * @param serviceIdentifier
   *          The identifier of the service class used to look for.
   * 
   * @return <code>true</code> <=> A service is registered using the supplied identifier.
   */
  public final boolean hasService(String serviceIdentifier) {
    return this._serviceMap.containsKey(serviceIdentifier);
  }

  /**
   * <p>
   * Returns the service with the identifier <code>serviceType.getName()</code>.
   * </p>
   * 
   * @param <T>
   *          The type of the service class.
   * @param serviceType
   *          The service class itself.
   * 
   * @return The instance of the service class.
   */
  @SuppressWarnings("unchecked")
  public <T> T getService(Class<T> serviceType) {
    Object result = this._serviceMap.get(serviceType.getName());
    if (result == null) {
      throw new Ant4EclipseException(CoreExceptionCode.SERVICE_NOT_AVAILABLE, serviceType.getName());
    }
    return (T) result;
  }

  /**
   * Returns a list of all currently registered services.
   * 
   * @return A list of all currently registered services. Not <code>null</code>.
   */
  public Object[] getAllServices() {
    Object[] result = new Object[this._serviceMap.size()];
    this._serviceMap.values().toArray(result);
    return result;
  }

  /**
   * <p>
   * </p>
   * 
   * @return
   */
  private final boolean isInitialized() {
    return this._isInitialized;
  }

  /**
   * <p>
   * </p>
   */
  void initialize() {

    Assure.assertTrue(!isInitialized(), "Service registry already has been initialized!");

    Iterator<Object> iterator = this._serviceOrdering.iterator();

    while (iterator.hasNext()) {
      Object service = iterator.next();

      if (service instanceof Lifecycle) {
        try {
          ((Lifecycle) service).initialize();
        } catch (Exception e) {
          throw new Ant4EclipseException(e, CoreExceptionCode.SERVICE_COULD_NOT_BE_INITIALIZED, service.getClass()
              .getName());
        }
      }
    }

    setInitialized(true);
  }

  /**
   * <p>
   * </p>
   */
  void dispose() {
    Assure.assertTrue(isInitialized(), "Service registry is not initialized.");
    setInitialized(false);
  }

  /**
   * @param b
   */
  private void setInitialized(boolean b) {
    this._isInitialized = b;
  }

  /**
   * <p>
   * Creates an instance of type {@link ServiceRegistry}.
   * </p>
   * 
   * @param configuration
   *          the service registry configuration
   */
  ServiceRegistry(ServiceRegistryConfiguration configuration) {
    this._serviceMap = new HashMap<String, Object>();
    this._serviceOrdering = new ArrayList<Object>();
    configuration.configure(new ConfigurationContextImpl());
  }

  /**
   * ConfigurationContextImpl --
   * 
   * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
   */
  private class ConfigurationContextImpl implements ConfigurationContext {

    /**
     * {@inheritDoc}
     */
    @Override
    public void registerService(Object service, String serviceIdentifier) {
      Assure.assertTrue(!ServiceRegistry.this._isInitialized, "ServiceRegistry.this._isInitialized!");
      Assure.notNull("service", service);
      Assure.notNull("serviceIdentifier", serviceIdentifier);

      if (!ServiceRegistry.this._serviceMap.containsKey(serviceIdentifier)) {
        ServiceRegistry.this._serviceMap.put(serviceIdentifier, service);
        ServiceRegistry.this._serviceOrdering.add(service);
      } else {
        throw new Ant4EclipseException(CoreExceptionCode.SERVICE_IDENTIFIER_IS_NOT_UNIQUE, serviceIdentifier);
      }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void registerService(Object service, String[] serviceIdentifier) {
      Assure.assertTrue(!ServiceRegistry.this._isInitialized, "ServiceRegistry.this._isInitialized!");
      Assure.notNull("service", service);
      Assure.notNull("serviceIdentifier", serviceIdentifier);
      Assure.assertTrue(serviceIdentifier.length > 0, "serviceIdentifier.length = 0!");

      for (int i = 0; i < serviceIdentifier.length; i++) {
        Assure.assertTrue(serviceIdentifier[i] != null, "Parameter serviceIdentifier[" + i + "] has to be set!");
      }

      for (String object : serviceIdentifier) {
        if (ServiceRegistry.this._serviceMap.containsKey(object)) {
          throw new Ant4EclipseException(CoreExceptionCode.SERVICE_IDENTIFIER_IS_NOT_UNIQUE, (Object) serviceIdentifier);
        }
      }

      for (String object : serviceIdentifier) {
        ServiceRegistry.this._serviceMap.put(object, service);
        ServiceRegistry.this._serviceOrdering.add(service);
      }
    }

  } /* ENDCLASS */

} /* ENDCLASS */
