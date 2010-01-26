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
package org.ant4eclipse.lib.pde.model.pluginproject;



import org.ant4eclipse.lib.pde.model.buildproperties.PluginBuildProperties;
import org.ant4eclipse.lib.platform.model.resource.role.ProjectRole;
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

}
