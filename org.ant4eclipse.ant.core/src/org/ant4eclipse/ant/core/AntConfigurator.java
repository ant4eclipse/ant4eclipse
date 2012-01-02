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
package org.ant4eclipse.ant.core;

import org.ant4eclipse.lib.core.configuration.Ant4EclipseConfiguration;
import org.ant4eclipse.lib.core.configuration.Ant4EclipseConfigurationImpl;
import org.ant4eclipse.lib.core.logging.Ant4EclipseLogger;
import org.ant4eclipse.lib.core.service.DefaultServiceRegistryConfiguration;
import org.ant4eclipse.lib.core.service.ServiceRegistry;
import org.ant4eclipse.lib.core.service.ServiceRegistryAccess;
import org.apache.tools.ant.BuildEvent;
import org.apache.tools.ant.BuildListener;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.PropertyHelper;

/**
 * <p>
 * The class {@link AntConfigurator} provides static methods to configure Ant4Eclipse.
 * </p>
 * 
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 * @author Nils Hartmann (nils@nilshartmann.net)
 */
public class AntConfigurator {

  private static final String REF_REGISTRY = "org.ant4eclipse.SERVICEREGISTRY";

  /**
   * <p>
   * Configures Ant4Eclipse in a ant based environment (the standard case).
   * </p>
   * 
   * @param project
   *          the ant project
   */
  public static final void configureAnt4Eclipse(Project project) {

    if (!ServiceRegistryAccess.isConfigured()) {

      // set ant4eclipse property helper
      PropertyHelper.getPropertyHelper(project).setNext(new ThreadDispatchingPropertyHelper(project));

      Object existing = project.getReference(REF_REGISTRY);
      if ((existing != null) && (existing instanceof ServiceRegistry)) {

        // restore the previously existing registry
        ServiceRegistryAccess.restore((ServiceRegistry) existing);

      } else {

        // this project doesn't use a4e yet, so we need to set it up
        Ant4EclipseLogger logger = new AntBasedLogger();
        project.addBuildListener(new ProjectBuildListener(logger));
        Ant4EclipseConfiguration configuration = new Ant4EclipseConfigurationImpl();
        ServiceRegistryAccess.configure(new DefaultServiceRegistryConfiguration(logger, configuration));

        // configures all ant services
        Object[] services = ServiceRegistryAccess.instance().getAllServices();
        for (Object service : services) {
          if (service instanceof AntService) {
            ((AntService) service).configure(project);
          }
        }

        // backup the registry, so we can reuse it if necessary
        project.addReference(REF_REGISTRY, ServiceRegistryAccess.instance());

      }

    }
  }
  
  private static class ProjectBuildListener implements BuildListener {
    
    private Ant4EclipseLogger   logger;
    
    public ProjectBuildListener( Ant4EclipseLogger antlogger ) {
      logger = antlogger;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void taskStarted( BuildEvent event ) {
      logger.setContext( event.getTask() );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void taskFinished( BuildEvent event ) {
      logger.setContext( null );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void targetStarted( BuildEvent event ) {
      logger.setContext( event.getTarget() );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void targetFinished( BuildEvent event ) {
      logger.setContext( null );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void messageLogged( BuildEvent event ) {
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void buildStarted( BuildEvent event ) {
      logger.setContext( event.getProject() );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void buildFinished( BuildEvent event ) {
      logger.setContext( null );
    }

  } /* ENDCLASS */

} /* ENDCLASS */
