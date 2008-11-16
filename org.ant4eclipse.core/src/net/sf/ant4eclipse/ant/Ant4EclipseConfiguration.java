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

          // add ant logger
          context.registerService(antLogger, Ant4EclipseLogger.class.getName());

//          // add the java runtime registry
//          context.registerService(new JavaRuntimeRegistryImpl(), JavaRuntimeRegistry.class.getName());
//
//          // add the workspace registry impl
//          context.registerService(new WorkspaceRegistryImpl(), WorkspaceRegistry.class.getName());
//
//          // add Dm Server registry
//          context.registerService(new DmServerRegistryImpl(), DmServerRegistry.class.getName());
//
//          // add S2 Application Platform registry
//          context.registerService(new EclipseVariableResolverImpl(), EclipseVariableResolver.class.getName());
//
//          // add PDE TargetPlatformRegistry registry
//          context.registerService(new TargetPlatformRegistryImpl(), TargetPlatformRegistry.class.getName());
        }
      };

      // configure
      ServiceRegistry.configure(configuration);
    }
  }
}
