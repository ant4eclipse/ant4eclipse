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
package org.ant4eclipse.lib.core.logging;

import org.ant4eclipse.lib.core.exception.Ant4EclipseException;
import org.ant4eclipse.lib.core.logging.A4ELogging;
import org.ant4eclipse.lib.core.logging.Ant4EclipseLogger;
import org.ant4eclipse.lib.core.service.ConfigurationContext;
import org.ant4eclipse.lib.core.service.ServiceRegistry;
import org.ant4eclipse.lib.core.service.ServiceRegistryConfiguration;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class Failuretest {

  private static final Class<?> SERVICE_TYPE = Ant4EclipseLogger.class;

  @Before
  public void configureServiceRegistry() {
    ServiceRegistryConfiguration configuration = new ServiceRegistryConfiguration() {
      public void configure(ConfigurationContext context) {
      }
    };
    ServiceRegistry.configure(configuration);
  }

  @After
  public void disposeServiceRegistry() {
    ServiceRegistry.reset();
  }

  @Test
  public void logging() {
    try {
      A4ELogging.debug("Test");
      Assert.fail();
    } catch (Ant4EclipseException exception) {
      Assert.assertEquals(String.format("Service '%s' is not available.", SERVICE_TYPE.getName()), exception
          .getMessage());
    }
  }

} /* ENDCLASS */
