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
package org.ant4eclipse.ant;

import org.apache.tools.ant.Project;
import org.apache.tools.ant.Task;

/**
 * Baseclass for all ant4eclipse task.
 * 
 * <p>
 * This tasks configures the ant4eclipse logging when the Ant project has been set.
 * 
 * @author Nils Hartmann
 */
public class Ant4EclipseTask extends Task {

  public Ant4EclipseTask() {
    super();
  }

  public void setProject(final Project project) {
    super.setProject(project);

    Ant4EclipseConfiguration.configureAnt4Eclipse(getProject());
  }
}
