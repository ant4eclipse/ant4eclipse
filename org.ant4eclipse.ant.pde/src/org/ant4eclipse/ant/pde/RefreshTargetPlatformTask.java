/**********************************************************************
 * Copyright (c) 2005-2010 ant4eclipse project team.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Nils Hartmann, Daniel Kasmeroglu, Gerd Wuetherich
 **********************************************************************/
package org.ant4eclipse.ant.pde;

import org.ant4eclipse.ant.core.AbstractAnt4EclipseTask;
import org.ant4eclipse.lib.core.A4ECore;
import org.ant4eclipse.lib.pde.tools.TargetPlatformRegistry;

/**
 * <p>
 * Implements an ant task that allows the user to refresh target platforms. That can be useful if one would use the PDE
 * artifacts from one build step in a second build step (e.g. built plug-ins to build features or prodcuts).
 * </p>
 * 
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 */
public class RefreshTargetPlatformTask extends AbstractAnt4EclipseTask {

  /**
   * {@inheritDoc}
   */
  @Override
  protected void doExecute() {

    // get the target platform registry
    TargetPlatformRegistry targetPlatformRegistry = A4ECore.instance()
        .getRequiredService( TargetPlatformRegistry.class );

    // refresh all target platforms
    targetPlatformRegistry.refreshAll();

  }
  
} /* ENDCLASS */
