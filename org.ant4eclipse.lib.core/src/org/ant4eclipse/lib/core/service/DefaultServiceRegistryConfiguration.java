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

import org.ant4eclipse.lib.core.Assure;
import org.ant4eclipse.lib.core.configuration.Ant4EclipseConfiguration;
import org.ant4eclipse.lib.core.logging.Ant4EclipseLogger;

/**
 * <p>
 * Default implementation of a ServiceRegistryConfiguration
 * </p>
 * 
 * @author Daniel Kasmeroglu (Daniel.Kasmeroglu@Kasisoft.net)
 */
public class DefaultServiceRegistryConfiguration implements ServiceRegistryConfiguration {

  private Ant4EclipseLogger        logger;

  private Ant4EclipseConfiguration config;

  /**
   * Initialises this registry with a valid logger and an apropriate configuration.
   * 
   * @param loggerobj
   *          The logger to be used. Not <code>null</code>.
   * @param configobj
   *          The configuration to be used. Not <code>null</code>.
   */
  public DefaultServiceRegistryConfiguration(Ant4EclipseLogger loggerobj, Ant4EclipseConfiguration configobj) {
    Assure.notNull("loggerobj", loggerobj);
    Assure.notNull("configobj", configobj);
    this.logger = loggerobj;
    this.config = configobj;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void configure(ConfigurationContext context) {

    // 1. add Ant4EclipseLogger
    context.registerService(this.logger, Ant4EclipseLogger.class.getName());

    // 2. add Ant4EclipseConfiguration
    context.registerService(this.config, Ant4EclipseConfiguration.class.getName());

    // 3. Configure services from properties
    PropertiesBasedServiceRegistryConfiguration configuration = new PropertiesBasedServiceRegistryConfiguration(
        this.config);
    configuration.configure(context);

  }

} /* ENDCLASS */
