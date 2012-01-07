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

import org.ant4eclipse.lib.core.logging.Ant4EclipseLogger;
import org.apache.tools.ant.BuildEvent;
import org.apache.tools.ant.BuildListener;

public class ProjectBuildListener implements BuildListener {

  private Ant4EclipseLogger logger;

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
