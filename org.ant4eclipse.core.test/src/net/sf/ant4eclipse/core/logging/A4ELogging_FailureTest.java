package net.sf.ant4eclipse.core.logging;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import net.sf.ant4eclipse.core.logging.A4ELogging;
import net.sf.ant4eclipse.core.service.ServiceNotAvailableException;
import net.sf.ant4eclipse.core.service.ServiceRegistry;
import net.sf.ant4eclipse.core.service.ServiceRegistryConfiguration;

import static org.junit.Assert.*;

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
      assertEquals("Service 'net.sf.ant4eclipse.core.logging.Ant4EclipseLogger' is not available!", exception
          .getMessage());
    }
  }
}
