/**********************************************************************
 * Copyright (c) 2005-2008 ant4eclipse project team.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Nils Hartmann, Daniel Kasmeroglu, Gerd Wuetherich
 **********************************************************************/
package org.ant4eclipse.core.service;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.ant4eclipse.core.configuration.Ant4EclipseConfiguration;
import org.ant4eclipse.core.configuration.Ant4EclipseConfigurationImpl;
import org.ant4eclipse.core.service.ServiceRegistryConfiguration.ConfigurationContext;
import org.ant4eclipse.core.util.ExtendedProperties;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Hashtable;
import java.util.Map;

public class PropertiesBasedServiceRegistryConfigurationTest {

  private Ant4EclipseConfiguration _ant4EclipseConfiguration;

  @Before
  public void setupTestProperties() {
    // some dummy properties
    ExtendedProperties properties = new ExtendedProperties();
    properties.put("service.ant4eclipse.TestServiceA", TestServiceAImpl.class.getName());
    properties.put("service.TestServiceB", TestServiceBImpl.class.getName());
    properties.put("noservice.TestServiceC", "Not a service");
    this._ant4EclipseConfiguration = new Ant4EclipseConfigurationImpl(properties);
  }

  @After
  public void dispose() {
    this._ant4EclipseConfiguration = null;
  }

  @Test
  public void test_configure() {

    DummyContext context = new DummyContext();

    // Execute configure
    PropertiesBasedServiceRegistryConfiguration config = new PropertiesBasedServiceRegistryConfiguration(
        this._ant4EclipseConfiguration);
    config.configure(context);

    // Make sure only given services have been registered
    Object serviceA = context._registeredServices.remove("ant4eclipse.TestServiceA");
    assertTrue(serviceA instanceof TestServiceAImpl);

    Object serviceB = context._registeredServices.remove("TestServiceB");
    assertTrue(serviceB instanceof TestServiceBImpl);

    assertTrue(context._registeredServices.isEmpty());

  }

  class DummyContext implements ConfigurationContext {

    Map<String, Object> _registeredServices = new Hashtable<String, Object>();

    public void registerService(Object service, String serviceIdentifier) {
      this._registeredServices.put(serviceIdentifier, service);

    }

    public void registerService(Object service, String[] serviceIdentifiers) {
      fail("registerService should not be called");

    }

  }

}
