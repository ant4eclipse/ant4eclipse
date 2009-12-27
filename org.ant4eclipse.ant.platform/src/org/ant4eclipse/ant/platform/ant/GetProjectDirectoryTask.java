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
package org.ant4eclipse.ant.platform.ant;

import org.ant4eclipse.ant.platform.ant.core.task.AbstractGetProjectPathTask;

import java.io.File;

/**
 * <p>
 * Can be used to resolve the root diretory fo a given project.
 * </p>
 * 
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 */
public class GetProjectDirectoryTask extends AbstractGetProjectPathTask {

  /**
   * {@inheritDoc}
   */
  @Override
  protected File[] resolvePath() {
    return new File[] { getEclipseProject().getFolder() };
  }
}