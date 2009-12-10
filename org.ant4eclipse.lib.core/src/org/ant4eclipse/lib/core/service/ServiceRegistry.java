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


import org.ant4eclipse.lib.core.CoreExceptionCode;
import org.ant4eclipse.lib.core.Lifecycle;
import org.ant4eclipse.lib.core.exception.Ant4EclipseException;
import org.ant4eclipse.lib.core.service.ServiceRegistryConfiguration.ConfigurationContext;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
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
 */
public class ServiceRegistry {

  /** the instance */
  private static ServiceRegistry _instance;

  /** the service map **/
  private Map<String, Object>    _serviceMap;

  /** list that contains the ordering of the services **/
  private List<Object>           _serviceOrdering;

  /** indicates whether the registry is configured **/
  private static boolean         _configured    = false;

  /** indicates whether the registry instance is initialized **/
  private boolean                _isInitialized = false;

  /**
   * <p>
   * Configures the {@link ServiceRegistry}. The registry has to be configured before it can be used.
   * </p>
   * 
   * @param configuration
   *          the service registry configuration
   */
  public static void configure(ServiceRegistryConfiguration configuration) {
    parameterNotNull("configuration", configuration);
    assertTrue(!isConfigured(), "ServiceRegistry already is configured.");

    _instance = new ServiceRegistry();
    configuration.configure(_instance.new ConfigurationContextImpl());
    _configured = true;
    try {
      _instance.initialize();
    } catch (RuntimeException exception) {
      _configured = false;
      throw exception;
    }
  }

  /**
   * <p>
   * Returns <code>true</code> if the {@link ServiceRegistry} already is configured, <code>false</code> otherwise.
   * </p>
   * 
   * @return <code>true</code> if the {@link ServiceRegistry} already is configured, <code>false</code> otherwise.
   */
  public static boolean isConfigured() {
    return _configured;
  }

  /**
   * <p>
   * Resets the {@link ServiceRegistry}.
   * </p>
   */
  public static void reset() {
    assertTrue(isConfigured(), "ServiceRegistry has to be configured.");

    // if the service registry is configured, it is also initialized and needs to be disposed
    _instance.dispose();

    // set configured = false;
    _configured = false;

    // set instance to null
    _instance = null;
  }

  /**
   * <p>
   * Returns the instance.
   * </p>
   * 
   * @return the instance.
   */
  public static ServiceRegistry instance() {
    assertTrue(isConfigured(), "ServiceRegistry has to be configured.");

    return _instance;
  }

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
    return (T) getService(serviceType.getName());
  }

  /**
   * <p>
   * Gives access to a specific service.
   * </p>
   * 
   * @param serviceIdentifier
   *          The identifier used to select a service.
   * 
   * @return The service instance. Not <code>null</code>.
   * 
   * @throws Ant4EclipseException
   *           if the desired service is not available.
   */
  public final Object getService(String serviceIdentifier) {
    Object result = this._serviceMap.get(serviceIdentifier);

    if (result == null) {
      throw new Ant4EclipseException(CoreExceptionCode.SERVICE_NOT_AVAILABLE, serviceIdentifier);
    }

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
  private void initialize() {
    assertTrue(!isInitialized(), "Service registry already has been initialized!");

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
  private void dispose() {
    assertTrue(isInitialized(), "Service registry is not initialized.");

    Iterator<Object> iterator = this._serviceOrdering.iterator();

    while (iterator.hasNext()) {
      Object service = iterator.next();
      if (service instanceof Lifecycle) {
        try {
          ((Lifecycle) service).dispose();
        } catch (Exception e) {
          // no need to do anything here...
          System.err.printf(CoreExceptionCode.SERVICE_COULD_NOT_BE_DISPOSED.getMessage(), service);
        }
      }
    }

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
   * Assert that the specified object is not null.
   * </p>
   * 
   * @param message
   *          an error message
   * @param object
   *          the object that must be set.
   */
  private static void parameterNotNull(String parameterName, Object parameter) {
    if (parameter == null) {
      throw new Ant4EclipseException(CoreExceptionCode.ASSERT_PARAMETER_NOT_NULL_FAILED, parameterName);
    }
  }

  /**
   * <p>
   * Assert that the given condition is <code>true</code>
   * </p>
   * 
   * @param condition
   *          the condition
   * @param msg
   *          the message
   */
  private static void assertTrue(boolean condition, String msg) {
    if (!condition) {
      throw new Ant4EclipseException(CoreExceptionCode.ASSERT_TRUE_FAILED, msg);
    }
  }

  /**
   * <p>
   * Creates an instance of type {@link ServiceRegistry}.
   * </p>
   */
  private ServiceRegistry() {
    this._serviceMap = new HashMap<String, Object>();
    this._serviceOrdering = new LinkedList<Object>();
  }

  /**
   * ConfigurationContextImpl --
   * 
   * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
   */
  protected class ConfigurationContextImpl implements ConfigurationContext {

    public final void registerService(Object service, String serviceIdentifier) {
      assertTrue(!ServiceRegistry.this._isInitialized, "ServiceRegistry.this._isInitialized!");
      parameterNotNull("service", service);
      parameterNotNull("serviceIdentifier", serviceIdentifier);

      if (!ServiceRegistry.this._serviceMap.containsKey(serviceIdentifier)) {
        ServiceRegistry.this._serviceMap.put(serviceIdentifier, service);
        ServiceRegistry.this._serviceOrdering.add(service);
      } else {
        throw new Ant4EclipseException(CoreExceptionCode.SERVICE_IDENTIFIER_IS_NOT_UNIQUE, serviceIdentifier);
      }
    }

    public final void registerService(Object service, String[] serviceIdentifier) {
      assertTrue(!ServiceRegistry.this._isInitialized, "ServiceRegistry.this._isInitialized!");
      parameterNotNull("service", service);
      parameterNotNull("serviceIdentifier", serviceIdentifier);
      assertTrue(serviceIdentifier.length > 0, "serviceIdentifier.length = 0!");

      for (int i = 0; i < serviceIdentifier.length; i++) {
        assertTrue(serviceIdentifier[i] != null, "Parameter serviceIdentifier[" + i + "] has to be set!");
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
  }
}
