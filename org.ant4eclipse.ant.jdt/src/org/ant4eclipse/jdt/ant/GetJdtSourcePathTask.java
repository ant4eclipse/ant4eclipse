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
package org.ant4eclipse.jdt.ant;

import org.ant4eclipse.platform.ant.core.task.AbstractGetProjectPathTask;

import org.ant4eclipse.lib.jdt.internal.model.project.JavaProjectRoleImpl;
import org.ant4eclipse.lib.jdt.model.project.JavaProjectRole;
import org.ant4eclipse.lib.platform.model.resource.EclipseProject;
import org.apache.tools.ant.BuildException;

import java.io.File;

/**
 * <p>
 * The {@link GetJdtSourcePathTask} can be used to resolve the source path of a given eclipse java project.
 * </p>
 * 
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 */
public class GetJdtSourcePathTask extends AbstractGetProjectPathTask {

  /** specifies if multiple source folders are supported */
  private boolean _allowMultipleFolders = false;

  /**
   * <p>
   * Returns true if multiple folders are supported.
   * </p>
   * 
   * @return <code>true</code> if multiple folders are supported.
   */
  public boolean isAllowMultipleFolders() {
    return this._allowMultipleFolders;
  }

  /**
   * <p>
   * Specifies if multiple folders are supported or not.
   * </p>
   * 
   * @param allowMultipleFolders
   *          if multiple folders are supported or not.
   */
  public void setAllowMultipleFolders(boolean allowMultipleFolders) {
    this._allowMultipleFolders = allowMultipleFolders;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected void preconditions() throws BuildException {
    super.preconditions();
    if (!getEclipseProject().hasRole(JavaProjectRoleImpl.class)) {
      throw new BuildException(String.format("The project '%s' must have the java project role!", getEclipseProject()
          .getSpecifiedName()));
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected File[] resolvePath() {

    // set relative flag
    EclipseProject.PathStyle relative = isRelative() ? EclipseProject.PathStyle.PROJECT_RELATIVE_WITHOUT_LEADING_PROJECT_NAME
        : EclipseProject.PathStyle.ABSOLUTE;

    // resolve the source path
    JavaProjectRole javaProjectRole = getEclipseProject().getRole(JavaProjectRole.class);
    String[] paths = javaProjectRole.getSourceFolders();
    File[] result = getEclipseProject().getChildren(paths, relative);

    if ((result.length > 1) && !isAllowMultipleFolders()) {
      StringBuffer buffer = new StringBuffer();
      buffer.append("Project '");
      buffer.append(getEclipseProject().getFolderName());
      buffer.append("' contains multiple SourceFolders! ");
      buffer.append("If you want to allow this, you have to");
      buffer.append(" set allowMultipleFolders='true'!");

      throw new RuntimeException(buffer.toString());
    }

    return result;
  }
}