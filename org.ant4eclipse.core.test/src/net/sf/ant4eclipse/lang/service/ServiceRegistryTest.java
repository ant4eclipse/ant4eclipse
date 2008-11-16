package net.sf.ant4eclipse.lang.service;

import net.sf.ant4eclipse.core.Lifecycle;
import net.sf.ant4eclipse.core.service.ServiceNotAvailableException;
import net.sf.ant4eclipse.core.service.ServiceRegistry;
import net.sf.ant4eclipse.core.service.ServiceRegistryConfiguration;

import org.junit.Test;

import static org.junit.Assert.*;

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
    } catch (ServiceNotAvailableException e) {
      // 
    }

    ServiceRegistry.reset();
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
  }

  @Test
  public void testInitializeAndDispose() {

    final DummyService dummyService = new DummyService();

    // 
    ServiceRegistry.configure(new ServiceRegistryConfiguration() {

      public void configure(ConfigurationContext context) {

        context.registerService(dummyService, "test1");
      }
    });

    DummyService service = (DummyService) ServiceRegistry.instance().getService("test1");

    assertEquals(dummyService, service);
    assertTrue(dummyService.isInitialized());

    ServiceRegistry.reset();
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
}
