package org.ant4eclipse.core.logging;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.ant4eclipse.core.exception.Ant4EclipseException;
import org.ant4eclipse.core.service.ServiceRegistry;
import org.ant4eclipse.core.service.ServiceRegistryConfiguration;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class A4ELogging_FailureTest {

  private static final Class<?> SERVICE_TYPE = Ant4EclipseLogger.class;

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
    } catch (Ant4EclipseException exception) {
      assertEquals(String.format("Service '%s' is not available.", SERVICE_TYPE.getName()), exception.getMessage());
    }
  }
}
