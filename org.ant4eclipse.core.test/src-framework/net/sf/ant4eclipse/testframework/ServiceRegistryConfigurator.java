package net.sf.ant4eclipse.testframework;

import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import net.sf.ant4eclipse.core.service.ServiceRegistry;
import net.sf.ant4eclipse.core.service.ServiceRegistryConfiguration;

public class ServiceRegistryConfigurator {

  public static void configureServiceRegistry(final Properties properties) {

    // get the entries
    final Set<Map.Entry<Object, Object>> entries = properties.entrySet();

    // setup the configuration
    ServiceRegistryConfiguration configuration = new ServiceRegistryConfiguration() {

      public void configure(ConfigurationContext context) {

        for (Iterator<Map.Entry<Object, Object>> iterator = entries.iterator(); iterator.hasNext();) {
          Map.Entry<Object, Object> entry = (Map.Entry<Object, Object>) iterator.next();

          Object service = null;

          if (entry.getValue() instanceof String) {
            try {
              Class<?> serviceclass = this.getClass().getClassLoader().loadClass((String) entry.getValue());
              service = serviceclass.newInstance();
            } catch (Exception e) {
              e.printStackTrace();
            }
          } else {
            service = entry.getValue();
          }

          context.registerService(service, entry.getKey().toString());
        }
      }
    };

    ServiceRegistry.configure(configuration);
  }
}
