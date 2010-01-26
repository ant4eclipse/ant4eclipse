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
package org.ant4eclipse.lib.pde.model.featureproject;


import org.ant4eclipse.lib.pde.model.buildproperties.FeatureBuildProperties;
import org.ant4eclipse.lib.platform.model.resource.role.ProjectRole;

/**
 * <p>
 * Implements the eclipse feature project role.
 * </p>
 * 
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 */
public interface FeatureProjectRole extends ProjectRole {

  /** PLUGIN_NATURE */
  String FEATURE_NATURE            = "org.eclipse.pde.FeatureNature";

  /** PLUGIN_PROJECT_ROLE_NAME */
  String FEATURE_PROJECT_ROLE_NAME = "FeatureProjectRole";

  /**
   * <p>
   * Returns the feature manifest of the feature project.
   * </p>
   * 
   * @return the feature manifest.
   */
  FeatureManifest getFeatureManifest();

  /**
   * <p>
   * Returns <code>true</code> if the feature project has build properties, <code>false</code> otherwise.
   * </p>
   * 
   * @return <code>true</code> if the feature project has build properties, <code>false</code> otherwise.
   */
  boolean hasBuildProperties();

  /**
   * <p>
   * Returns the {@link FeatureBuildProperties}.
   * </p>
   * 
   * @return the {@link FeatureBuildProperties}.
   */
  FeatureBuildProperties getBuildProperties();

}
