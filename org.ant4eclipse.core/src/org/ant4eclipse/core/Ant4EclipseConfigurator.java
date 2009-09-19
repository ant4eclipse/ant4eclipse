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
package org.ant4eclipse.core;

import org.ant4eclipse.core.ant.AntBasedLogger;
import org.ant4eclipse.core.configuration.Ant4EclipseConfiguration;
import org.ant4eclipse.core.configuration.Ant4EclipseConfigurationImpl;
import org.ant4eclipse.core.logging.Ant4EclipseLogger;
import org.ant4eclipse.core.logging.DefaultAnt4EclipseLogger;
import org.ant4eclipse.core.service.PropertiesBasedServiceRegistryConfiguration;
import org.ant4eclipse.core.service.ServiceRegistry;
import org.ant4eclipse.core.service.ServiceRegistryConfiguration;
import org.ant4eclipse.core.util.StringMap;

import org.apache.tools.ant.Project;

/**
 * <p>
 * The class {@link Ant4EclipseConfigurator} provides static methods to configure Ant4Eclipse.
 * </p>
 * 
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 * @author Nils Hartmann (nils@nilshartmann.net)
 */
public class Ant4EclipseConfigurator {

  /**
   * <p>
   * Configures Ant4Eclipse in a ant based environment (the standard case).
   * </p>
   * 
   * @param project
   *          the ant project
   */
  public static void configureAnt4Eclipse(Project project) {
    if (!ServiceRegistry.isConfigured()) {
      configureAnt4Eclipse(new AntBasedLogger(project), new Ant4EclipseConfigurationImpl());
    }
  }

  /**
   * <p>
   * Configures Ant4Eclipse in a non-ant based environment.
   * </p>
   */
  public static void configureAnt4Eclipse() {
    if (!ServiceRegistry.isConfigured()) {
      configureAnt4Eclipse(new DefaultAnt4EclipseLogger(), new Ant4EclipseConfigurationImpl());
    }
  }

  /**
   * <p>
   * Configures Ant4Eclipse in a non-ant based environment.
   * </p>
   * 
   * @param ant4EclipseConfigurationProperties
   *          the configuration properties
   */
  public static void configureAnt4Eclipse(StringMap ant4EclipseConfigurationProperties) {
    if (!ServiceRegistry.isConfigured()) {
      configureAnt4Eclipse(new DefaultAnt4EclipseLogger(),
          ant4EclipseConfigurationProperties != null ? new Ant4EclipseConfigurationImpl(
              ant4EclipseConfigurationProperties) : new Ant4EclipseConfigurationImpl(new StringMap()));
    }
  }

  /**
   * <p>
   * Configures Ant4Eclipse.
   * </p>
   * 
   * @param logger
   *          the {@link Ant4EclipseLogger}
   * @param configuration
   *          the {@link Ant4EclipseConfiguration}
   */
  private static void configureAnt4Eclipse(final Ant4EclipseLogger logger, final Ant4EclipseConfiguration configuration) {

    // configure
    ServiceRegistry.configure(new ServiceRegistryConfiguration() {

      public void configure(ConfigurationContext context) {

        // 1. add Ant4EclipseLogger
        context.registerService(logger, Ant4EclipseLogger.class.getName());

        // 2. add Ant4EclipseConfiguration
        context.registerService(configuration, Ant4EclipseConfiguration.class.getName());

        // 3. Configure services from properties
        PropertiesBasedServiceRegistryConfiguration propertiesConfiguration = new PropertiesBasedServiceRegistryConfiguration(
            configuration);
        propertiesConfiguration.configure(context);
      }
    });
  }
}
