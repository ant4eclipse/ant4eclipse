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
package org.ant4eclipse.pde.model.pluginproject;

import org.ant4eclipse.core.Assert;

import org.ant4eclipse.pde.model.buildproperties.PluginBuildProperties;

import org.ant4eclipse.platform.model.resource.EclipseProject;
import org.ant4eclipse.platform.model.resource.role.ProjectRole;

import org.eclipse.osgi.service.resolver.BundleDescription;

/**
 * <p>
 * Implements the eclipse plug-in project role. The plug-in project role contains the BundleDescription and the build
 * properties.
 * </p>
 * 
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 */
public interface PluginProjectRole extends ProjectRole {

  /** PLUGIN_NATURE */
  String PLUGIN_NATURE            = "org.eclipse.pde.PluginNature";

  /** PLUGIN_PROJECT_ROLE_NAME */
  String PLUGIN_PROJECT_ROLE_NAME = "PluginProjectRole";

  /**
   * <p>
   * Returns the BundleDescription.
   * </p>
   * 
   * @return BundleDescription.
   */
  BundleDescription getBundleDescription();

  /**
   * <p>
   * Returns <code>true</code> if the build properties have been set.
   * </p>
   * 
   * @return <code>true</code> if the build properties have been set.
   */
  boolean hasBuildProperties();

  /**
   * <p>
   * Returns the build properties.
   * </p>
   * 
   * @return Returns the buildProperties.
   */
  PluginBuildProperties getBuildProperties();

  /**
   * <p>
   * Sets the build properties.
   * </p>
   */
  void setBuildProperties(PluginBuildProperties buildProperties);

  /**
   * <p>
   * Helper class that provides methods for retrieving the {@link PluginProjectRole} from a given {@link EclipseProject}
   * .
   * </p>
   * 
   * 
   * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
   */
  public static class Helper {

    /**
     * <p>
     * Returns the {@link PluginProjectRole}. If a {@link PluginProjectRole} is not set, an exception will be thrown.
     * </p>
     * 
     * @return the plugin project role.
     */
    public static PluginProjectRole getPluginProjectRole(EclipseProject eclipseProject) {
      Assert.assertTrue(hasPluginProjectRole(eclipseProject), "Project \"" + eclipseProject.getFolderName()
          + "\" must have PluginProjectRole!");

      return (PluginProjectRole) eclipseProject.getRole(PluginProjectRole.class);
    }

    /**
     * <p>
     * Returns whether a {@link PluginProjectRole} is set or not.
     * </p>
     * 
     * @return Returns whether a {@link PluginProjectRole} is set or not.
     */
    public static final boolean hasPluginProjectRole(EclipseProject eclipseProject) {
      Assert.notNull(eclipseProject);

      return eclipseProject.hasRole(PluginProjectRole.class);
    }
  }
}
