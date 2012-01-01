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
  public void nullService() {

    try {
      ServiceRegistryAccess.configure(new ServiceRegistryConfiguration() {
        @Override
        public void configure(ConfigurationContext context) {
          context.registerService(null, "null");
        }
      });
      Assert.fail();
    } catch (Ant4EclipseException ex) {
      Assert.assertEquals(CoreExceptionCode.PRECONDITION_VIOLATION, ex.getExceptionCode());
    }

    try {
      ServiceRegistryAccess.configure(new ServiceRegistryConfiguration() {
        @Override
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
      ServiceRegistryAccess.configure(new ServiceRegistryConfiguration() {
        @Override
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
      ServiceRegistryAccess.configure(new ServiceRegistryConfiguration() {
        @Override
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
      ServiceRegistryAccess.configure(new ServiceRegistryConfiguration() {
        @Override
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
    ServiceRegistryAccess.configure(new ServiceRegistryConfiguration() {
      @Override
      public void configure(ConfigurationContext context) {
        context.registerService(new NonDisposeDummyService(), "test1");
      }
    });
    ServiceRegistryAccess.reset();
  }

  @Test
  public void initializeAndDispose() {

    final DummyService dummyService = new DummyService();

    // 
    ServiceRegistryAccess.configure(new ServiceRegistryConfiguration() {

      @Override
      public void configure(ConfigurationContext context) {
        context.registerService(dummyService, DummyService.class.getName());
      }
    });

    Assert.assertTrue(ServiceRegistryAccess.instance().hasService(DummyService.class.getName()));
    DummyService service = ServiceRegistryAccess.instance().getService(DummyService.class);
    Assert.assertEquals(dummyService, service);

    Assert.assertTrue(ServiceRegistryAccess.instance().hasService(DummyService.class));
    service = ServiceRegistryAccess.instance().getService(DummyService.class);
    Assert.assertEquals(dummyService, service);

    ServiceRegistryAccess.reset();
    Assert.assertFalse(ServiceRegistryAccess.isConfigured());
    try {
      ServiceRegistryAccess.reset();
      Assert.fail();
    } catch (Ant4EclipseException ex) {
      Assert.assertEquals(CoreExceptionCode.PRECONDITION_VIOLATION, ex.getExceptionCode());
    }
  }

  /**
   */
  public class DummyService implements Lifecycle {

    @Override
    public void initialize() {
    }

  }

  public class NonInitialitationDummyService extends DummyService {
    @Override
    public void initialize() {
      throw new RuntimeException();
    }
  }

  public class NonDisposeDummyService extends DummyService {
  }
}
