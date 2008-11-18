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
package org.ant4eclipse.jdt.model.project;

import java.util.Map;

import org.ant4eclipse.core.Assert;
import org.ant4eclipse.jdt.model.jre.JavaProfile;
import org.ant4eclipse.jdt.model.jre.JavaRuntime;
import org.ant4eclipse.jdt.model.project.internal.JavaProjectRoleImpl;
import org.ant4eclipse.platform.model.resource.EclipseProject;
import org.ant4eclipse.platform.model.resource.role.ProjectRole;


/**
 * JavaProjectRole --
 * 
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 */
public interface JavaProjectRole extends ProjectRole {

  /**  */
  public static final String JAVA_NATURE = "org.eclipse.jdt.core.javanature";

  /**
   * Returns whether classpath entries are set or not.
   * 
   * @return Returns whether classpath entries are set or not.
   */
  public boolean hasRawClasspathEntries();

  /**
   * Returns the eclipse classpath entries.
   * 
   * @return returns the eclipse classpath entries.
   */
  public RawClasspathEntry[] getRawClasspathEntries();

  /**
   * Returns a list of EclipseClasspathEntry of a specifiy kind.
   * 
   * @param entrykind
   *          The kind of the desired entries.
   * 
   * @return A list of entries providing entries of the desired type.
   */
  public RawClasspathEntry[] getRawClasspathEntries(final int entrykind);

  /**
   * Returns the java runtime for this eclipse project or null if no runtime specified or if no such java runtime has
   * been registered.
   * 
   * @return
   */
  public JavaRuntime getJavaRuntime();

  /**
   * @return
   */
  public JavaProfile getJavaProfile();

  /**
   * @return
   */
  public Map getCompilerOptions();

  /**
   * @return
   */
  public String[] getSourceFolders();

  /**
   * @param allowMultipleFolder
   * @return
   */
  public String[] getAllOutputFolder();

  /**
   * @param sourceFolder
   * @return
   */
  public String getOutputFolderForSourceFolder(String sourceFolder);

  /**
   * @param resolveRelative
   * @return
   */
  public String getDefaultOutputFolder();

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
    public static final JavaProjectRole getJavaProjectRole(final EclipseProject eclipseProject) {
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
    public static final boolean hasJavaProjectRole(final EclipseProject eclipseProject) {
      Assert.notNull(eclipseProject);

      return eclipseProject.hasRole(JavaProjectRoleImpl.class);
    }
  }
}