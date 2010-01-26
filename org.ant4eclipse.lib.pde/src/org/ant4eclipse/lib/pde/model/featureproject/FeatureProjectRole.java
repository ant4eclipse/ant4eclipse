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




import org.ant4eclipse.lib.core.Assure;
import org.ant4eclipse.lib.pde.model.buildproperties.FeatureBuildProperties;
import org.ant4eclipse.lib.platform.model.resource.EclipseProject;
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
  public static final String FEATURE_NATURE            = "org.eclipse.pde.FeatureNature";

  /** PLUGIN_PROJECT_ROLE_NAME */
  public static final String FEATURE_PROJECT_ROLE_NAME = "FeatureProjectRole";

  /**
   * <p>
   * Returns the feature manifest of the feature project.
   * </p>
   * 
   * @return the feature manifest.
   */
  public FeatureManifest getFeatureManifest();

  /**
   * <p>
   * Returns <code>true</code> if the feature project has build properties, <code>false</code> otherwise.
   * </p>
   * 
   * @return <code>true</code> if the feature project has build properties, <code>false</code> otherwise.
   */
  public boolean hasBuildProperties();

  /**
   * <p>
   * Returns the {@link FeatureBuildProperties}.
   * </p>
   * 
   * @return the {@link FeatureBuildProperties}.
   */
  public FeatureBuildProperties getBuildProperties();

  /**
   * <p>
   * Helper class to access the {@link FeatureProjectRole}.
   * </p>
   * 
   * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
   */
  public static class Helper {

    /**
     * <p>
     * Returns the {@link FeatureProjectRole}.
     * </p>
     * 
     * @param eclipseProject
     *          the eclipse project
     * @return the {@link FeatureProjectRole}
     */
    public static final FeatureProjectRole getFeatureProjectRole(EclipseProject eclipseProject) {
      Assure.assertTrue(hasFeatureProjectRole(eclipseProject), "Project \"" + eclipseProject.getFolderName()
          + "\" must have FeatureProjectRole!");

      // returns the feature project role
      return (FeatureProjectRole) eclipseProject.getRole(FeatureProjectRole.class);
    }

    /**
     * <p>
     * Returns <code>true</code>, if the eclipse project contains a feature project role.
     * </p>
     * 
     * @param eclipseProject
     *          the eclipse project
     * @return <code>true</code> if the eclipse project contains a feature project role.
     */
    public static final boolean hasFeatureProjectRole(EclipseProject eclipseProject) {
      Assure.notNull(eclipseProject);

      return eclipseProject.hasRole(FeatureProjectRole.class);
    }
  }
}
