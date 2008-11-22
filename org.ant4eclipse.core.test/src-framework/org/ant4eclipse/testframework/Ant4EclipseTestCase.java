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
package org.ant4eclipse.testframework;

import java.util.Hashtable;
import java.util.Map;
import java.util.Properties;

import org.ant4eclipse.core.Ant4EclipseConfigurationProperties;
import org.ant4eclipse.core.logging.Ant4EclipseLogger;
import org.ant4eclipse.core.logging.DefaultAnt4EclipseLogger;
import org.ant4eclipse.core.service.ServiceRegistry;
import org.junit.After;
import org.junit.Before;

/**
 * Superclass for all Ant4Eclipse test cases
 * 
 * <p>
 * This testcases sets up the {@link Ant4EclipseConfigurationProperties} singleton and the {@link ServiceRegistry}
 * 
 * @author Nils Hartmann (nils@nilshartmann.net)
 */
public class Ant4EclipseTestCase {

  /**
   * This setup method sets up the {@link Ant4EclipseConfigurationProperties} and the {@link ServiceRegistry}.
   * 
   * <p>
   * The creation of both objects can be customized for a test by overwriting
   * {@link #setupAnt4EclipseConfigurationProperties()} resp. {@link #setupServiceRegistry()}
   */
  @Before
  public void setup() {
    setupAnt4EclipseConfigurationProperties();
    setupServiceRegistry();
  }

  @After
  public void dispose() {
    disposeServiceRegistry();
    disposeAnt4EclipseConfigurationProperties();
  }

  protected void disposeServiceRegistry() {
    try {
      if (ServiceRegistry.isConfigured()) {
        ServiceRegistry.reset();
      }
    } catch (Exception ex) {
      System.err.println("[Ant4EclipseTestCase] Could not reset ServiceRegistry: " + ex);
      ex.printStackTrace();
    }
  }

  protected void disposeAnt4EclipseConfigurationProperties() {
    try {
      Ant4EclipseConfigurationProperties.dispose();
    } catch (Exception ex) {
      System.err.println("[Ant4EclipseTestCase] Could not dispose Ant4EclipseConfigurationProperties: " + ex);
      ex.printStackTrace();
    }
  }

  /**
   * Initializes the {@link ServiceRegistry}.
   * 
   * <p>
   * The service registry is constructed with configuration items returned by {@link #getServiceRegistryProperties()}.
   * 
   * <p>
   * This method can be overwritten in subclasses to implement a test-specific behaviour
   * 
   */
  protected void setupServiceRegistry() {
    Map<String, Object> serviceRegistryProperties = new Hashtable<String, Object>();
    fillServiceRegistryProperties(serviceRegistryProperties);
    ServiceRegistryConfigurator.configureServiceRegistry(serviceRegistryProperties);
  }

  /**
   * Adds to the given Map test-specific configuration items to configure the Service Registry in
   * {@link #setupServiceRegistry()}
   * 
   * <p>
   * The map must contain service ids as keys and strings or objects as values that are registered as services at the
   * service registry
   * 
   * <p>
   * This implementation only add a logger to the service registry.
   * <p>
   * This method can be overwritten to add test-specific objects to the service registry
   * 
   * @param registryProperties
   *          The map that should be filled with test-specific properties for the service registry
   */
  protected void fillServiceRegistryProperties(Map<String, Object> registryProperties) {
    registryProperties.put(Ant4EclipseLogger.class.getName(), DefaultAnt4EclipseLogger.class.getName());
  }

  /**
   * Initializes the {@link Ant4EclipseConfigurationProperties} singleton for a test case.
   * 
   * <p>
   * This implementation configures the singleton with properties returned by
   * {@link #getAnt4EclipseConfigurationProperties()}
   * <p>
   * This method can be overwritten to implement test-specific behaviour
   */
  protected void setupAnt4EclipseConfigurationProperties() {
    Properties properties = new Properties();
    fillAnt4EclipseConfigurationProperties(properties);
    Ant4EclipseConfigurationProperties.initialize(properties);
  }

  /**
   * Adds test specific properties for the {@link Ant4EclipseConfigurationProperties} singleton to the given properties
   * object
   * <p>
   * This implementation doesn't add any properties. It can be overwritten to add test-specific properties.
   * 
   * @param properties
   *          the Properties object that takes the properties for Ant4EclipseConfigurationProperties
   */
  protected void fillAnt4EclipseConfigurationProperties(Properties properties) {
  }

}
