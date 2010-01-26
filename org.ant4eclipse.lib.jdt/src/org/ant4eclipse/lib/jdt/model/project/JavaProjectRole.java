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
package org.ant4eclipse.lib.jdt.model.project;



import org.ant4eclipse.platform.model.resource.EclipseProject;
import org.ant4eclipse.platform.model.resource.role.ProjectRole;

import org.ant4eclipse.lib.core.Assert;
import org.ant4eclipse.lib.core.util.StringMap;
import org.ant4eclipse.lib.jdt.internal.model.project.JavaProjectRoleImpl;
import org.ant4eclipse.lib.jdt.model.jre.JavaProfile;
import org.ant4eclipse.lib.jdt.model.jre.JavaRuntime;

/**
 * <p>
 * Implements the java project role.
 * </p>
 * 
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 */
public interface JavaProjectRole extends ProjectRole {

  /** the java nature id */
  String JAVA_NATURE = "org.eclipse.jdt.core.javanature";

  /**
   * <p>
   * Returns whether class path entries are set or not.
   * </p>
   * 
   * @return Returns whether class path entries are set or not.
   */
  boolean hasRawClasspathEntries();

  /**
   * <p>
   * Returns the eclipse class path entries.
   * </p>
   * 
   * @return returns the eclipse class path entries.
   */
  RawClasspathEntry[] getRawClasspathEntries();

  /**
   * <p>
   * Returns a list of EclipseClasspathEntry of a specific kind.
   * </p>
   * 
   * @param entrykind
   *          The kind of the desired entries.
   * 
   * @return A list of entries providing entries of the desired type.
   */
  RawClasspathEntry[] getRawClasspathEntries(int entrykind);

  /**
   * <p>
   * Returns the java runtime for this eclipse project or null if no runtime specified or if no such java runtime has
   * been registered.
   * </p>
   * 
   * @return the java runtime for this eclipse project or null if no runtime specified or if no such java runtime has
   *         been registered.
   */
  JavaRuntime getJavaRuntime();

  /**
   * <p>
   * Returns the {@link JavaProfile}.
   * </p>
   * 
   * @return the {@link JavaProfile}.
   */
  JavaProfile getJavaProfile();

  /**
   * <p>
   * Returns a map with the compiler options or <code>null</code> if no such options exist.
   * </p>
   * 
   * @return a map with the compiler options or <code>null</code> if no such options exist.
   */
  StringMap getCompilerOptions();

  /**
   * <p>
   * Returns the source folders for this project.
   * </p>
   * 
   * @return the source folders for this project.
   */
  String[] getSourceFolders();

  /**
   * <p>
   * Returns the output folders for this project.
   * </p>
   * 
   * @return the output folders for this project.
   */
  String[] getAllOutputFolders();

  /**
   * <p>
   * Returns the output folder for a specific source folder.
   * </p>
   * 
   * @param sourceFolder
   *          the source folder
   * @return the output folder for a specific source folder.
   */
  String getOutputFolderForSourceFolder(String sourceFolder);

  /**
   * <p>
   * Returns the default output folder.
   * </p>
   * 
   * @return the default output folder
   */
  String getDefaultOutputFolder();

  /**
   * <p>
   * Helper class that provides methods for retrieving the {@link JavaProjectRole} from a given {@link EclipseProject}.
   * </p>
   * 
   * 
   * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
   */
  public class Helper {

    /**
     * <p>
     * Returns the {@link JavaProjectRole}. If a {@link JavaProjectRole} is not set, an exception will be thrown.
     * </p>
     * 
     * @return the java project role.
     */
    public static final JavaProjectRole getJavaProjectRole(EclipseProject eclipseProject) {
      Assert.assertTrue(hasJavaProjectRole(eclipseProject), "Project \"" + eclipseProject.getFolderName()
          + "\" must have JavaProjectRole!");

      return (JavaProjectRole) eclipseProject.getRole(JavaProjectRoleImpl.class);
    }

    /**
     * <p>
     * Returns whether a {@link JavaProjectRole} is set or not.
     * </p>
     * 
     * @return Returns whether a {@link JavaProjectRole} is set or not.
     */
    public static final boolean hasJavaProjectRole(EclipseProject eclipseProject) {
      Assert.notNull(eclipseProject);

      return eclipseProject.hasRole(JavaProjectRoleImpl.class);
    }
  }
}