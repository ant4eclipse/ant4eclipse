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
import org.junit.Assert;
import org.junit.Test;

public class ServiceRegistryTest {

  @Test
  public void serviceRegistry() {

    final Object object1 = new Object();
    final Object object2 = new Object();

    // 
    ServiceRegistry.configure(new ServiceRegistryConfiguration() {

      public void configure(ConfigurationContext context) {
        context.registerService(object1, "test1");
        context.registerService(object2, new String[] { "test2", "test3" });
      }
    });

    Assert.assertTrue(ServiceRegistry.instance().hasService("test1"));
    Assert.assertTrue(ServiceRegistry.instance().hasService("test2"));
    Assert.assertTrue(ServiceRegistry.instance().hasService("test3"));

    Assert.assertEquals(ServiceRegistry.instance().getService("test1"), object1);
    Assert.assertEquals(ServiceRegistry.instance().getService("test2"), object2);
    Assert.assertEquals(ServiceRegistry.instance().getService("test3"), object2);

    // 
    try {
      ServiceRegistry.instance().getService("not there");
      Assert.fail();
    } catch (Ant4EclipseException e) {
      Assert.assertEquals("Service 'not there' is not available.", e.getMessage());
    }

    ServiceRegistry.reset();
  }

  @Test
  public void nullService() {

    try {
      ServiceRegistry.configure(new ServiceRegistryConfiguration() {
        public void configure(ConfigurationContext context) {
          context.registerService(null, "null");
        }
      });
      Assert.fail();
    } catch (Ant4EclipseException ex) {
      Assert.assertEquals(CoreExceptionCode.PRECONDITION_VIOLATION, ex.getExceptionCode());
    }

    try {
      ServiceRegistry.configure(new ServiceRegistryConfiguration() {
        public void configure(ConfigurationContext context) {
          context.registerService("null", (String) null);
        }
      });
      Assert.fail();
    } catch (Ant4EclipseException ex) {
      Assert.assertEquals(CoreExceptionCode.PRECONDITION_VIOLATION, ex.getExceptionCode());
    }

  }

  @Test
  public void duplicateIdentifierException() {
    try {
      // 
      ServiceRegistry.configure(new ServiceRegistryConfiguration() {
        public void configure(ConfigurationContext context) {
          context.registerService(new Object(), "test1");
          context.registerService(new Object(), new String[] { "test1", "test3" });
        }
      });
      Assert.fail();
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
      Assert.fail();
    } catch (Exception e) {
      //
    }
  }

  @Test
  public void initialisationException() {
    try {
      // 
      ServiceRegistry.configure(new ServiceRegistryConfiguration() {
        public void configure(ConfigurationContext context) {
          context.registerService(new NonInitialitationDummyService(), "test1");
        }
      });
      Assert.fail();
    } catch (Ant4EclipseException ex) {
      Assert.assertEquals(CoreExceptionCode.SERVICE_COULD_NOT_BE_INITIALIZED, ex.getExceptionCode());
    }
  }

  @Test
  public void disposeException() {
    ServiceRegistry.configure(new ServiceRegistryConfiguration() {
      public void configure(ConfigurationContext context) {
        context.registerService(new NonDisposeDummyService(), "test1");
      }
    });
    ServiceRegistry.reset();
  }

  @Test
  public void initializeAndDispose() {

    final DummyService dummyService = new DummyService();

    // 
    ServiceRegistry.configure(new ServiceRegistryConfiguration() {

      public void configure(ConfigurationContext context) {
        context.registerService(dummyService, DummyService.class.getName());
      }
    });

    Assert.assertTrue(ServiceRegistry.instance().hasService(DummyService.class.getName()));
    DummyService service = (DummyService) ServiceRegistry.instance().getService(DummyService.class.getName());
    Assert.assertEquals(dummyService, service);
    Assert.assertTrue(dummyService.isInitialized());

    Assert.assertTrue(ServiceRegistry.instance().hasService(DummyService.class));
    service = ServiceRegistry.instance().getService(DummyService.class);
    Assert.assertEquals(dummyService, service);
    Assert.assertTrue(dummyService.isInitialized());

    ServiceRegistry.reset();
    Assert.assertFalse(ServiceRegistry.isConfigured());
    try {
      ServiceRegistry.reset();
      Assert.fail();
    } catch (Ant4EclipseException ex) {
      Assert.assertEquals(CoreExceptionCode.PRECONDITION_VIOLATION, ex.getExceptionCode());
    }
  }

  /**
   */
  public class DummyService implements Lifecycle {

    private boolean _initialized = false;

    public void initialize() {
      this._initialized = true;
    }

    public void dispose() {
      this._initialized = false;
    }

    public boolean isInitialized() {
      return this._initialized;
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
