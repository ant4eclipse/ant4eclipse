package org.ant4eclipse.core.service;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.ant4eclipse.core.Lifecycle;
import org.ant4eclipse.core.service.ServiceRegistryConfiguration.ConfigurationContext;
import org.ant4eclipse.core.util.MessageCreator;


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
 * TODO Make ServiceRegistry Keys Class-Objects
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
    notNull("Parameter 'configuration' has to be set!", configuration);
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

    // dispose instance if instance is initialized
    if (_instance.isInitialized()) {
      _instance.dispose();
    }

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
   * </p>
   * 
   * @param serviceIdentifier
   * @return
   * @throws ServiceNotAvailableException
   */
  public final Object getService(final String serviceIdentifier) throws ServiceNotAvailableException {
    final Object result = this._serviceMap.get(serviceIdentifier);

    if (result == null) {
      throw new ServiceNotAvailableException("Service '" + serviceIdentifier + "' is not available!");
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
          // TODO: FEHLERBEHANDLUNG!!!
          e.printStackTrace();
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
    assertTrue(isInitialized(), "dispose() darf nur aufgerufen werden, wenn isInitialized() == true !");

    final Iterator<Object> iterator = this._serviceOrdering.iterator();

    while (iterator.hasNext()) {
      final Object service = iterator.next();
      if (service instanceof Lifecycle) {
        try {
          ((Lifecycle) service).dispose();
        } catch (final Exception e) {
          // TODO: FEHLERBEHANDLUNG!!!
          e.printStackTrace();
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
  private static void notNull(final String message, final Object object) {
    if (object == null) {
      throw new RuntimeException("Precondition violated: " + message);
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
      final String errmsg = MessageCreator.createMessage("Precondition violated: %s", msg);
      throw new RuntimeException(errmsg);
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
      assertTrue(service != null, "Parameter service muss ungleich null sein!");
      assertTrue(serviceIdentifier != null, "Parameter serviceIdentifier muss ungleich null sein!");

      if (!ServiceRegistry.this._serviceMap.containsKey(serviceIdentifier)) {
        ServiceRegistry.this._serviceMap.put(serviceIdentifier, service);
        ServiceRegistry.this._serviceOrdering.add(service);
      } else {
        throw new NoUniqueIdentifierException("Identifier \"" + serviceIdentifier
            + "\" is not unique: it is already used!");
      }
    }

    public final void registerService(final Object service, final String[] serviceIdentifier) {
      assertTrue(!ServiceRegistry.this._isInitialized,
          "Environment darf noch nicht initialisiert sein, wenn ein Service angemeldet wird!");
      assertTrue(service != null, "Parameter service muss ungleich null sein!");
      assertTrue(serviceIdentifier != null, "Parameter serviceIdentifier muss ungleich null sein!");
      assertTrue(serviceIdentifier.length > 0, "Länge des Parameters serviceIdentifier muss grösser als 0 sein!");

      for (int i = 0; i < serviceIdentifier.length; i++) {
        assertTrue(serviceIdentifier[i] != null, "Parameter serviceIdentifier[" + i + "] muss ungleich null sein!");
      }

      for (int i = 0; i < serviceIdentifier.length; i++) {
        final Object object = serviceIdentifier[i];

        if (ServiceRegistry.this._serviceMap.containsKey(object)) {
          throw new NoUniqueIdentifierException("Identifier \"" + serviceIdentifier
              + "\" is not unique: it is already used!");
        }
      }

      for (int i = 0; i < serviceIdentifier.length; i++) {
        final String object = serviceIdentifier[i];
        ServiceRegistry.this._serviceMap.put(object, service);
        ServiceRegistry.this._serviceOrdering.add(service);
      }
    }
  }
}
