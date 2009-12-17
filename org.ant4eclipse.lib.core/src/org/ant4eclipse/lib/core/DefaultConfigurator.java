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
package org.ant4eclipse.lib.core;

import org.ant4eclipse.lib.core.configuration.Ant4EclipseConfiguration;
import org.ant4eclipse.lib.core.configuration.Ant4EclipseConfigurationImpl;
import org.ant4eclipse.lib.core.logging.Ant4EclipseLogger;
import org.ant4eclipse.lib.core.logging.DefaultAnt4EclipseLogger;
import org.ant4eclipse.lib.core.service.DefaultServiceRegistryConfiguration;
import org.ant4eclipse.lib.core.service.ServiceRegistry;
import org.ant4eclipse.lib.core.util.StringMap;

/**
 * <p>
 * The class {@link DefaultConfigurator} provides static methods to configure Ant4Eclipse.
 * </p>
 * 
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 * @author Nils Hartmann (nils@nilshartmann.net)
 */
public class DefaultConfigurator {

  /**
   * <p>
   * Configures Ant4Eclipse in a non-ant based environment.
   * </p>
   */
  public static final void configureAnt4Eclipse() {
    if (!ServiceRegistry.isConfigured()) {
      Ant4EclipseLogger logger = new DefaultAnt4EclipseLogger();
      Ant4EclipseConfiguration configuration = new Ant4EclipseConfigurationImpl();
      ServiceRegistry.configure(new DefaultServiceRegistryConfiguration(logger, configuration));
    }
  }

  /**
   * <p>
   * Configures Ant4Eclipse in a non-ant based environment.
   * </p>
   * 
   * @param properties
   *          the configuration properties
   */
  public static final void configureAnt4Eclipse(StringMap properties) {
    if (!ServiceRegistry.isConfigured()) {
      if (properties == null) {
        properties = new StringMap();
      }
      Ant4EclipseLogger logger = new DefaultAnt4EclipseLogger();
      Ant4EclipseConfiguration configuration = new Ant4EclipseConfigurationImpl(properties);
      ServiceRegistry.configure(new DefaultServiceRegistryConfiguration(logger, configuration));
    }
  }

} /* ENDCLASS */
