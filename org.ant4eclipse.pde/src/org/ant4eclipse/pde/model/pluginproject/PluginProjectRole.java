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
package org.ant4eclipse.pde.model.pluginproject;

import org.ant4eclipse.core.Assert;
import org.ant4eclipse.pde.model.buildproperties.PluginBuildProperties;
import org.ant4eclipse.platform.model.resource.EclipseProject;
import org.ant4eclipse.platform.model.resource.role.AbstractProjectRole;
import org.eclipse.osgi.service.resolver.BundleDescription;

/**
 * <p>
 * Implements the eclipse plugin role. The plugin role contains the PluginDescriptor and the build properties.
 * </p>
 * 
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 */
public class PluginProjectRole extends AbstractProjectRole {

  /** PLUGIN_NATURE */
  public static final String      PLUGIN_NATURE            = "org.eclipse.pde.PluginNature";

  /** PLUGIN_PROJECT_ROLE_NAME */
  public static final String      PLUGIN_PROJECT_ROLE_NAME = "PluginProjectRole";

  /** the BundleDescription of the underlying plugin project */
  private final BundleDescription _bundleDescription;

  /** the build properties */
  private PluginBuildProperties   _buildProperties;

  /**
   * Returns the plugin project role. If a plugin project role is not set, an exception will be thrown.
   * 
   * @return the plugin project role.
   */
  public static final PluginProjectRole getPluginProjectRole(final EclipseProject eclipseProject) {
    Assert.assertTrue(hasPluginProjectRole(eclipseProject), "Project \"" + eclipseProject.getFolderName()
        + "\" must have PluginProjectRole!");

    return (PluginProjectRole) eclipseProject.getRole(PluginProjectRole.class);
  }

  /**
   * Returns whether a plugin project role is set or not.
   * 
   * @return whether a plugin project role is set or not.
   */
  public static final boolean hasPluginProjectRole(final EclipseProject eclipseProject) {
    Assert.notNull(eclipseProject);

    return eclipseProject.hasRole(PluginProjectRole.class);
  }

  /**
   * <p>
   * Creates a new instance of type PluginProjectRole.
   * </p>
   * 
   * @param eclipseProject
   *          the plugin project.
   */
  public PluginProjectRole(final EclipseProject eclipseProject, final BundleDescription description) {
    super(PLUGIN_PROJECT_ROLE_NAME, eclipseProject);

    Assert.notNull(eclipseProject);
    Assert.notNull(description);

    this._bundleDescription = description;
    this._buildProperties = null;
  }

  /**
   * <p>
   * Returns the unresolved BundleDescription.
   * </p>
   * 
   * @return the plugin descriptor.
   */
  public BundleDescription getBundleDescription() {
    return this._bundleDescription;
  }

  /**
   * Returns true if the build properties have been set.
   * 
   * @return true <=> The build properties have been set.
   */
  public boolean hasBuildProperties() {
    return this._buildProperties != null;
  }

  /**
   * @return Returns the buildProperties.
   */
  public PluginBuildProperties getBuildProperties() {
    return this._buildProperties;
  }

  /**
   * <p>
   * Sets the build properties.
   * </p>
   */
  public void setBuildProperties(final PluginBuildProperties buildProperties) {
    this._buildProperties = buildProperties;
  }
}
