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
package net.sf.ant4eclipse.ant;

import net.sf.ant4eclipse.core.logging.Ant4EclipseLogger;
import net.sf.ant4eclipse.core.service.PropertiesBasedServiceRegistryConfiguration;
import net.sf.ant4eclipse.core.service.ServiceRegistry;
import net.sf.ant4eclipse.core.service.ServiceRegistryConfiguration;

import org.apache.tools.ant.BuildEvent;
import org.apache.tools.ant.BuildListener;
import org.apache.tools.ant.Project;

public class Ant4EclipseConfiguration {

  public static void configureAnt4Eclipse(final Project project) {

    if (!ServiceRegistry.isConfigured()) {

      // create AntLogger
      final AntBasedLogger antLogger = new AntBasedLogger(project);

      // create BuildListener
      final BuildListener buildListener = new BuildListener() {

        public void taskStarted(final BuildEvent event) {
          antLogger.setContext(event.getTask());
        }

        public void taskFinished(final BuildEvent event) {
          antLogger.setContext(null);
        }

        public void targetStarted(final BuildEvent event) {
          antLogger.setContext(event.getTarget());
        }

        public void targetFinished(final BuildEvent event) {
          antLogger.setContext(null);
        }

        public void messageLogged(final BuildEvent event) {
          //
        }

        public void buildStarted(final BuildEvent event) {
          //
        }

        public void buildFinished(final BuildEvent event) {
          //
        }
      };

      project.addBuildListener(buildListener);

      // create service registry configuration
      final ServiceRegistryConfiguration configuration = new ServiceRegistryConfiguration() {

        public void configure(final ConfigurationContext context) {

          // 1st: add ant logger
          // TODO: could it be possible to configure this via properties file ?
          context.registerService(antLogger, Ant4EclipseLogger.class.getName());

          // 2. Configure services from properties files
          PropertiesBasedServiceRegistryConfiguration propertiesConfiguration = new PropertiesBasedServiceRegistryConfiguration();
          propertiesConfiguration.configure(context);
        }
      };

      // configure
      ServiceRegistry.configure(configuration);
    }
  }
}
