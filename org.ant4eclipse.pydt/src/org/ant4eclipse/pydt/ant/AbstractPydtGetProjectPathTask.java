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
package org.ant4eclipse.pydt.ant;

import org.ant4eclipse.platform.ant.core.task.AbstractGetProjectPathTask;

import org.ant4eclipse.lib.core.exception.Ant4EclipseException;
import org.ant4eclipse.lib.platform.model.resource.EclipseProject;
import org.ant4eclipse.pydt.PydtExceptionCode;
import org.ant4eclipse.pydt.internal.tools.PythonUtilities;
import org.apache.tools.ant.BuildException;

/**
 * Abstract path based task for the python support.
 * 
 * @author Daniel Kasmeroglu (Daniel.Kasmeroglu@Kasisoft.net)
 */
abstract class AbstractPydtGetProjectPathTask extends AbstractGetProjectPathTask {

  /**
   * {@inheritDoc}
   */
  @Override
  protected void preconditions() throws BuildException {
    super.preconditions();
    if (!PythonUtilities.isPythonRelatedProject(getEclipseProject())) {
      throw new Ant4EclipseException(PydtExceptionCode.MISSING_PYTHON_ROLE, getEclipseProject().getSpecifiedName());
    }
  }

  /**
   * Returns the PathStyle to be used for the path calculations.
   * 
   * @return The PathStyle to be used for the path calculations.
   */
  protected EclipseProject.PathStyle getPathStyle() {
    if (isRelative()) {
      return EclipseProject.PathStyle.PROJECT_RELATIVE_WITHOUT_LEADING_PROJECT_NAME;
    } else {
      return EclipseProject.PathStyle.ABSOLUTE;
    }
  }

} /* ENDCLASS */
