package org.ant4eclipse.core.logging;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.ant4eclipse.core.service.ServiceNotAvailableException;
import org.ant4eclipse.core.service.ServiceRegistry;
import org.ant4eclipse.core.service.ServiceRegistryConfiguration;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class A4ELogging_FailureTest {

  @Before
  public void configureServiceRegistry() {
    ServiceRegistryConfiguration configuration = new ServiceRegistryConfiguration() {

      public void configure(ConfigurationContext context) {
        // do nothing
      }
    };

    ServiceRegistry.configure(configuration);
  }

  @After
  public void disposeServiceRegistry() {
    ServiceRegistry.reset();
  }

  @Test
  public void testA4ELogging() {
    try {
      A4ELogging.debug("Test");
      fail();
    } catch (ServiceNotAvailableException exception) {
      assertEquals("Service 'org.ant4eclipse.core.logging.Ant4EclipseLogger' is not available!", exception.getMessage());
    }
  }
}
