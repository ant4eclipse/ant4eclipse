package org.ant4eclipse.core.service;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.ant4eclipse.core.CoreExceptionCode;
import org.ant4eclipse.core.Lifecycle;
import org.ant4eclipse.core.exception.Ant4EclipseException;
import org.ant4eclipse.core.service.ServiceRegistryConfiguration.ConfigurationContext;

/**
 * <p>
 * The {@link ServiceRegistry} implements a singleton that can be used to register and retrieve services. A service is
 * an instance of a class that implements the interface {@link Service}.
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
  private static ServiceRegistry    _instance;

  /** the service map **/
  private final Map<String, Object> _serviceMap;

  /** list that contains the ordering of the services **/
  private final List<Object>        _serviceOrdering;

  /** indicates whether the registry is configured **/
  private static boolean            _configured    = false;

  /** indicates whether the registry instance is initialized **/
  private boolean                   _isInitialized = false;

  /**
   * <p>
   * Configures the {@link ServiceRegistry}. The registry has to be configured before it can be used.
   * </p>
   * 
   * @param configuration
   *          the service registry configuration
   */
  public static void configure(final ServiceRegistryConfiguration configuration) {
    parameterNotNull("configuration", configuration);
    assertTrue(!isConfigured(), "ServiceRegistry already is configured.");

    _instance = new ServiceRegistry();
    configuration.configure(_instance.new ConfigurationContextImpl());
    _configured = true;
    _instance.initialize();
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
   * @param serviceType
   * @return
   */
  public <T> boolean hasService(Class<T> serviceType) {
    return hasService(serviceType.getName());
  }

  /**
   * <p>
   * </p>
   * 
   * @param serviceIdentifier
   * @return
   */
  public final boolean hasService(final String serviceIdentifier) {
    return this._serviceMap.containsKey(serviceIdentifier);
  }

  /**
   * <p>
   * Returns the service with the identifier <code>serviceType.getName()</code>.
   * </p>
   * 
   * @param <T>
   * @param serviceType
   * @return
   */
  @SuppressWarnings("unchecked")
  public <T> T getService(Class<T> serviceType) {
    return (T) getService(serviceType.getName());
  }

  /**
   * <p>
   * </p>
   * 
   * @param serviceIdentifier
   * @return
   * @throws ServiceNotAvailableException
   */
  public final Object getService(final String serviceIdentifier) {
    final Object result = this._serviceMap.get(serviceIdentifier);

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
    assertTrue(!isInitialized(), "initialize() darf nicht aufgerufen werden, wenn isInitialized() == true !");

    final Iterator<Object> iterator = this._serviceOrdering.iterator();

    while (iterator.hasNext()) {
      final Object service = iterator.next();

      if (service instanceof Lifecycle) {
        try {
          ((Lifecycle) service).initialize();
        } catch (final Exception e) {
          throw new Ant4EclipseException(CoreExceptionCode.SERVICE_COULD_NOT_BE_INITIALIZED, e, service.getClass()
              .getName(), e.getMessage());
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

    final Iterator<Object> iterator = this._serviceOrdering.iterator();

    while (iterator.hasNext()) {
      final Object service = iterator.next();
      if (service instanceof Lifecycle) {
        try {
          ((Lifecycle) service).dispose();
        } catch (final Exception e) {
          throw new Ant4EclipseException(CoreExceptionCode.SERVICE_COULD_NOT_BE_DISPOSED, e, service.getClass()
              .getName(), e.getMessage());
        }
      }
    }

    setInitialized(false);
  }

  /**
   * @param b
   */
  private void setInitialized(final boolean b) {
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
  private static void parameterNotNull(final String parameterName, final Object parameter) {
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
  private static void assertTrue(final boolean condition, final String msg) {
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
   * @author admin
   */
  protected class ConfigurationContextImpl implements ConfigurationContext {

    public final void registerService(final Object service, final String serviceIdentifier) {
      assertTrue(!ServiceRegistry.this._isInitialized,
          "Environment darf noch nicht initialisiert sein, wenn ein Service angemeldet wird!");
      parameterNotNull("service", service);
      parameterNotNull("serviceIdentifier", serviceIdentifier);

      if (!ServiceRegistry.this._serviceMap.containsKey(serviceIdentifier)) {
        ServiceRegistry.this._serviceMap.put(serviceIdentifier, service);
        ServiceRegistry.this._serviceOrdering.add(service);
      } else {
        throw new Ant4EclipseException(CoreExceptionCode.SERVICE_IDENTIFIER_IS_NOT_UNIQUE, serviceIdentifier);
      }
    }

    public final void registerService(final Object service, final String[] serviceIdentifier) {
      assertTrue(!ServiceRegistry.this._isInitialized,
          "Environment darf noch nicht initialisiert sein, wenn ein Service angemeldet wird!");
      parameterNotNull("service", service);
      parameterNotNull("serviceIdentifier", serviceIdentifier);
      assertTrue(serviceIdentifier.length > 0, "Länge des Parameters serviceIdentifier muss grösser als 0 sein!");

      for (int i = 0; i < serviceIdentifier.length; i++) {
        assertTrue(serviceIdentifier[i] != null, "Parameter serviceIdentifier[" + i + "] muss ungleich null sein!");
      }

      for (final String object : serviceIdentifier) {
        if (ServiceRegistry.this._serviceMap.containsKey(object)) {
          throw new Ant4EclipseException(CoreExceptionCode.SERVICE_IDENTIFIER_IS_NOT_UNIQUE, (Object) serviceIdentifier);
        }
      }

      for (final String object : serviceIdentifier) {
        ServiceRegistry.this._serviceMap.put(object, service);
        ServiceRegistry.this._serviceOrdering.add(service);
      }
    }
  }
}
