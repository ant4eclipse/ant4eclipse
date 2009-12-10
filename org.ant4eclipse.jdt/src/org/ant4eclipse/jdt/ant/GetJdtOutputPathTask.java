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

import org.ant4eclipse.jdt.model.project.JavaProjectRole;

import org.ant4eclipse.platform.ant.core.task.AbstractGetProjectPathTask;
import org.ant4eclipse.platform.model.resource.EclipseProject;

import org.ant4eclipse.lib.core.Assure;
import org.apache.tools.ant.BuildException;

import java.io.File;

/**
 * <p>
 * The {@link GetJdtOutputPathTask} can be used to resolve the output path of a given eclipse java project.
 * </p>
 * 
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 */
public class GetJdtOutputPathTask extends AbstractGetProjectPathTask {

  /** the constant for 'DEFAULT_FOLDER' */
  private static final String DEFAULT_FOLDER        = "defaultFolder";

  /** the constant for 'ALL' */
  private static final String ALL                   = "all";

  /** the constant for 'FOR_SOURCE_FOLDER' */
  private static final String FOR_SOURCE_FOLDER     = "forSourceFolder";

  /** defaultFolder, all, forSourceFolder */
  private String              _resolve              = DEFAULT_FOLDER;

  /** sourceFolder */
  private String              _sourceFolder;

  /** indicates if multiple output folders should be allowed or not */
  private boolean             _allowMultipleFolders = false;

  /**
   * <p>
   * Returns <code>true</code> if multiple folders are allowed.
   * </p>
   * 
   * @return <code>true</code> if multiple folders are allowed.
   */
  public boolean isAllowMultipleFolders() {
    return this._allowMultipleFolders;
  }

  /**
   * <p>
   * Sets if multiple folders are allowed.
   * </p>
   * 
   * @param allowMultipleFolders
   */
  public void setAllowMultipleFolders(boolean allowMultipleFolders) {
    this._allowMultipleFolders = allowMultipleFolders;
  }

  /**
   * <p>
   * Sets the resolution scope.
   * </p>
   * 
   * @param resolve
   *          the resolution scope.
   */
  public final void setResolve(String resolve) {
    if (DEFAULT_FOLDER.equals(resolve) || FOR_SOURCE_FOLDER.equals(resolve) || ALL.equals(resolve)) {
      this._resolve = resolve;
    } else {
      // TODO: NLS
      throw new BuildException("Attribute resolve must have one of the following values: '" + FOR_SOURCE_FOLDER
          + "', '" + ALL + "' or '" + DEFAULT_FOLDER + "'!");
    }

  }

  /**
   * <p>
   * Returns the source folder.
   * </p>
   * 
   * @return the source folder.
   */
  public final String getSourceFolder() {
    return this._sourceFolder;
  }

  /**
   * <p>
   * Sets the source folder.
   * </p>
   * 
   * @param sourceFolder
   *          the source folder.
   */
  public final void setSourceFolder(String sourceFolder) {
    this._sourceFolder = sourceFolder;
  }

  /**
   * <p>
   * Returns <code>true</code> if the source folder is set.
   * </p>
   * 
   * @return <code>true</code> if the source folder is set.
   */
  public final boolean isSourceFolderSet() {
    return this._sourceFolder != null;
  }

  /**
   * <p>
   * Requires that the source folder is set.
   * </p>
   */
  protected final void requireSourceFolderSet() {
    if (!isSourceFolderSet()) {
      // TODO: NLS
      throw new BuildException("Attribute 'sourceFolder' has to be set if resolve='forSourceFolder'!");
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public File[] resolvePath() {
    EclipseProject.PathStyle relative = isRelative() ? EclipseProject.PathStyle.PROJECT_RELATIVE_WITHOUT_LEADING_PROJECT_NAME
        : EclipseProject.PathStyle.ABSOLUTE;

    // resolve output folder for source folder
    if (FOR_SOURCE_FOLDER.equals(this._resolve)) {
      requireSourceFolderSet();

      JavaProjectRole javaProjectRole = (JavaProjectRole) getEclipseProject().getRole(JavaProjectRole.class);

      String pathName = javaProjectRole.getOutputFolderForSourceFolder(getSourceFolder());
      File resolvedPathEntry = getEclipseProject().getChild(pathName, relative);
      return new File[] { resolvedPathEntry };

    }
    // resolve all output folder
    else if (ALL.equals(this._resolve)) {
      JavaProjectRole javaProjectRole = (JavaProjectRole) getEclipseProject().getRole(JavaProjectRole.class);
      String[] pathNames = javaProjectRole.getAllOutputFolders();

      // TODO: NLS
      if (pathNames.length > 1 && !isAllowMultipleFolders()) {
        StringBuffer buffer = new StringBuffer();
        buffer.append("Project '");
        buffer.append(getEclipseProject().getSpecifiedName());
        buffer.append("' contains multiple output folder! ");
        buffer.append("If you want to allow this, ");
        buffer.append(" set allowMultipleFolder='true'!");
        throw new BuildException(buffer.toString());
      }

      return getEclipseProject().getChildren(pathNames, relative);
    } else
    // resolve default folder
    {
      Assure.assertTrue(DEFAULT_FOLDER.equals(this._resolve), "Illegal value for attribute resolve!");

      JavaProjectRole javaProjectRole = (JavaProjectRole) getEclipseProject().getRole(JavaProjectRole.class);
      String path = javaProjectRole.getDefaultOutputFolder();
      File resolvedPathEntry = getEclipseProject().getChild(path, relative);
      return new File[] { resolvedPathEntry };
    }
  }
}
