package org.ant4eclipse.testframework;

import java.util.Map;
import java.util.Set;

import org.ant4eclipse.core.service.ServiceRegistry;
import org.ant4eclipse.core.service.ServiceRegistryConfiguration;

public class ServiceRegistryConfigurator {

  /**
   * <p>
   * entries:
   * <p>
   * key: id of the service to be registered.
   * <p>
   * value: either a String (that will be interpreted as a class name) or an object to be registered
   * 
   * @param entries
   */
  public static void configureServiceRegistry(final Map<String, Object> properties) {

    // get the entries
    final Set<Map.Entry<String, Object>> entries = properties.entrySet();

    // setup the configuration
    ServiceRegistryConfiguration configuration = new ServiceRegistryConfiguration() {

      public void configure(ConfigurationContext context) {

        for (Map.Entry<String, Object> entry : entries) {
          Object service = null;

          Object value = entry.getValue();
          if (value instanceof String) {
            try {
              Class<?> serviceclass = this.getClass().getClassLoader().loadClass((String) value);
              service = serviceclass.newInstance();
            } catch (Exception e) {
              e.printStackTrace();
            }
          } else {
            service = value;
          }

          context.registerService(service, entry.getKey());
        }
      }
    };

    ServiceRegistry.configure(configuration);
  }
}
