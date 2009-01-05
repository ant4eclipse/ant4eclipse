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

import java.util.Properties;

import org.ant4eclipse.core.Ant4EclipseConfigurator;
import org.ant4eclipse.core.service.ServiceRegistry;
import org.junit.After;
import org.junit.Before;

/**
 * Superclass for all Ant4Eclipse test cases
 * 
 * <p>
 * This test case sets up the {@link Ant4EclipseConfigurationProperties} singleton and the {@link ServiceRegistry}
 * 
 * @author Nils Hartmann (nils@nilshartmann.net)
 */
public class ConfigurableAnt4EclipseTestCase {

  /**
   * This setup method sets up the {@link Ant4EclipseConfigurationProperties} and the {@link ServiceRegistry}.
   * 
   * <p>
   * The creation of both objects can be customized for a test by overwriting
   * {@link #setupAnt4EclipseConfigurationProperties()} resp. {@link #setupServiceRegistry()}
   */
  @Before
  public void setup() {
    Properties properties = customAnt4EclipseConfiguration(new Properties());
    if (properties == null) {
      Ant4EclipseConfigurator.configureAnt4Eclipse();
    } else {
      Ant4EclipseConfigurator.configureAnt4Eclipse(properties);
    }
  }

  @After
  public void dispose() {
    try {
      if (ServiceRegistry.isConfigured()) {
        ServiceRegistry.reset();
      }
    } catch (Exception ex) {
      System.err.println("[Ant4EclipseTestCase] Could not reset ServiceRegistry: " + ex);
      ex.printStackTrace();
    }
  }

  /**
   * @param properties
   * @return
   */
  protected Properties customAnt4EclipseConfiguration(Properties properties) {
    return null;
  }

}
