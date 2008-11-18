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
package net.sf.ant4eclipse.core;

import java.util.Hashtable;
import java.util.Map;
import java.util.Properties;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class Ant4EclipseConfigurationPropertiesTest {

  private Ant4EclipseConfigurationProperties _configProperties;

  @Before
  public void initTestProperties() {

    Properties properties = new Properties();
    properties.setProperty("service.serviceA", "Service 1");
    properties.setProperty("service.serviceB", "Service 2");
    properties.setProperty("service.C", "Service 3");
    properties.setProperty("role.a", "Role A");
    _configProperties = new Ant4EclipseConfigurationProperties(properties);

  }

  @Test
  public void test_Properties() {

    Assert.assertTrue(_configProperties.hasProperty("service.C"));
    Assert.assertFalse(_configProperties.hasProperty("service.D"));
    Assert.assertEquals("Service 1", _configProperties.getProperty("service.serviceA"));

    Iterable<String[]> serviceProperties = _configProperties.getAllProperties("service");

    final Map<String, String> expectedValues = new Hashtable<String, String>();
    expectedValues.put("serviceA", "Service 1");
    expectedValues.put("serviceB", "Service 2");
    expectedValues.put("C", "Service 3");

    // Make sure each expected value was returned exactly one time
    for (String[] properties : serviceProperties) {
      String key = properties[0];
      String value = properties[1];

      String expectedValue = expectedValues.remove(key);
      Assert.assertNotNull(expectedValue);
      Assert.assertEquals(expectedValue, value);
    }

    Assert.assertTrue(expectedValues.isEmpty());

    Iterable<String[]> emptyProperties = _configProperties.getAllProperties("notfound");
    Assert.assertNotNull(emptyProperties);
    Assert.assertFalse(emptyProperties.iterator().hasNext());
  }

}
