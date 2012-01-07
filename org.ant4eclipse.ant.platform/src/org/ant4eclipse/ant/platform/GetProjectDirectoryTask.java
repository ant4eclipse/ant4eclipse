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
package org.ant4eclipse.ant.platform;

import org.ant4eclipse.ant.platform.core.task.AbstractGetProjectPathTask;

import java.io.File;
import java.util.Arrays;
import java.util.List;

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
  protected List<File> resolvePath() {
    return Arrays.asList( getEclipseProject().getFolder() );
  }
  
} /* ENDCLASS */
