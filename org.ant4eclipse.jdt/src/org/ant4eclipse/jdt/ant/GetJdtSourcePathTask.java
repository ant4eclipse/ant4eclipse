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
package org.ant4eclipse.jdt.ant;

import org.ant4eclipse.jdt.internal.model.project.JavaProjectRoleImpl;
import org.ant4eclipse.jdt.model.project.JavaProjectRole;

import org.ant4eclipse.platform.ant.core.task.AbstractGetProjectPathTask;
import org.ant4eclipse.platform.model.resource.EclipseProject;

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
  public void setAllowMultipleFolders(final boolean allowMultipleFolders) {
    this._allowMultipleFolders = allowMultipleFolders;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected File[] resolvePath() {

    // set relative flag
    final int relative = isRelative() ? EclipseProject.PROJECT_RELATIVE_WITHOUT_LEADING_PROJECT_NAME
        : EclipseProject.ABSOLUTE;

    // the 'default' result
    File[] result = new File[0];

    // resolve the source path
    if (getEclipseProject().hasRole(JavaProjectRoleImpl.class)) {

      final JavaProjectRole javaProjectRole = JavaProjectRole.Helper.getJavaProjectRole(getEclipseProject());
      final String[] paths = javaProjectRole.getSourceFolders();
      result = getEclipseProject().getChildren(paths, relative);

      if ((result.length > 1) && !isAllowMultipleFolders()) {
        final StringBuffer buffer = new StringBuffer();
        buffer.append("Project '");
        buffer.append(getEclipseProject().getFolderName());
        buffer.append("' contains multiple SourceFolders! ");
        buffer.append("If you want to allow this, you have to");
        buffer.append(" set allowMultipleFolders='true'!");

        throw new RuntimeException(buffer.toString());
      }
    }

    return (result);
  }
}