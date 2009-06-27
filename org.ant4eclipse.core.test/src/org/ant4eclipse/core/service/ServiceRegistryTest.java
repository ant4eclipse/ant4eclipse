package org.ant4eclipse.core.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.ant4eclipse.core.Lifecycle;
import org.ant4eclipse.core.exception.Ant4EclipseException;

import org.junit.Test;

public class ServiceRegistryTest {

  @Test
  public void testServiceRegistry() {

    final Object object1 = new Object();
    final Object object2 = new Object();

    // 
    ServiceRegistry.configure(new ServiceRegistryConfiguration() {

      public void configure(ConfigurationContext context) {
        context.registerService(object1, "test1");
        context.registerService(object2, new String[] { "test2", "test3" });
      }
    });

    assertTrue(ServiceRegistry.instance().hasService("test1"));
    assertTrue(ServiceRegistry.instance().hasService("test2"));
    assertTrue(ServiceRegistry.instance().hasService("test3"));

    assertEquals(ServiceRegistry.instance().getService("test1"), object1);
    assertEquals(ServiceRegistry.instance().getService("test2"), object2);
    assertEquals(ServiceRegistry.instance().getService("test3"), object2);

    // 
    try {
      ServiceRegistry.instance().getService("not there");
      fail();
    } catch (Ant4EclipseException e) {
      assertEquals("Service 'not there' is not available.", e.getMessage());
    }

    ServiceRegistry.reset();
  }

  @Test
  public void testNullService() {
    try {
      // 
      ServiceRegistry.configure(new ServiceRegistryConfiguration() {
        public void configure(ConfigurationContext context) {
          context.registerService(null, "null");
        }
      });
      fail();
    } catch (Exception e) {
      assertEquals(e.getMessage(), "Precondition violated: Parameter 'service' has to be set.");
    }

    try {
      // 
      ServiceRegistry.configure(new ServiceRegistryConfiguration() {
        public void configure(ConfigurationContext context) {
          context.registerService("null", (String) null);
        }
      });
      fail();
    } catch (Exception e) {
      assertEquals(e.getMessage(), "Precondition violated: Parameter 'serviceIdentifier' has to be set.");
    }
  }

  @Test
  public void testNoUniqueIdentifierException() {
    try {
      // 
      ServiceRegistry.configure(new ServiceRegistryConfiguration() {
        public void configure(ConfigurationContext context) {
          context.registerService(new Object(), "test1");
          context.registerService(new Object(), new String[] { "test1", "test3" });
        }
      });
      fail();
    } catch (Exception e) {
      //
    }
    try {
      // 
      ServiceRegistry.configure(new ServiceRegistryConfiguration() {
        public void configure(ConfigurationContext context) {
          context.registerService(new Object(), "test1");
          context.registerService(new Object(), "test1");
        }
      });
      fail();
    } catch (Exception e) {
      //
    }
  }

  @Test
  public void testInitialitationException() {
    try {
      // 
      ServiceRegistry.configure(new ServiceRegistryConfiguration() {
        public void configure(ConfigurationContext context) {
          context.registerService(new NonInitialitationDummyService(), "test1");
        }
      });
      fail();
    } catch (Exception e) {
      assertEquals(
          "Service 'org.ant4eclipse.core.service.ServiceRegistryTest$NonInitialitationDummyService' could not be initialized.",
          e.getMessage());
    }
  }

  @Test
  public void testDisposeException() {
    ServiceRegistry.configure(new ServiceRegistryConfiguration() {
      public void configure(ConfigurationContext context) {
        context.registerService(new NonDisposeDummyService(), "test1");
      }
    });
    ServiceRegistry.reset();
  }

  @Test
  public void testInitializeAndDispose() {

    final DummyService dummyService = new DummyService();

    // 
    ServiceRegistry.configure(new ServiceRegistryConfiguration() {

      public void configure(ConfigurationContext context) {
        context.registerService(dummyService, DummyService.class.getName());
      }
    });

    assertTrue(ServiceRegistry.instance().hasService(DummyService.class.getName()));
    DummyService service = (DummyService) ServiceRegistry.instance().getService(DummyService.class.getName());
    assertEquals(dummyService, service);
    assertTrue(dummyService.isInitialized());

    assertTrue(ServiceRegistry.instance().hasService(DummyService.class));
    service = ServiceRegistry.instance().getService(DummyService.class);
    assertEquals(dummyService, service);
    assertTrue(dummyService.isInitialized());

    ServiceRegistry.reset();
    assertFalse(ServiceRegistry.isConfigured());
    try {
      ServiceRegistry.reset();
      fail();
    } catch (Exception e) {
      assertEquals("Precondition violated: ServiceRegistry has to be configured.", e.getMessage());
    }
  }

  /**
   */
  public class DummyService implements Lifecycle {

    private boolean _initialized = false;

    public void initialize() {
      _initialized = true;
    }

    public void dispose() {
      _initialized = false;
    }

    public boolean isInitialized() {
      return _initialized;
    }
  }

  public class NonInitialitationDummyService extends DummyService {
    public void initialize() {
      throw new RuntimeException();
    }
  }

  public class NonDisposeDummyService extends DummyService {
    public void dispose() {
      throw new RuntimeException();
    }
  }
}
