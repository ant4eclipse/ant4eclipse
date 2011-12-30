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
package org.ant4eclipse.lib.pde.internal.model.featureproject;

import org.ant4eclipse.lib.core.Assure;
import org.ant4eclipse.lib.pde.model.buildproperties.FeatureBuildProperties;
import org.ant4eclipse.lib.pde.model.featureproject.FeatureManifest;
import org.ant4eclipse.lib.pde.model.featureproject.FeatureProjectRole;
import org.ant4eclipse.lib.platform.model.resource.EclipseProject;
import org.ant4eclipse.lib.platform.model.resource.role.AbstractProjectRole;

import java.io.File;

/**
 * <p>
 * Implements the eclipse feature project role.
 * </p>
 * 
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 */
public class FeatureProjectRoleImpl extends AbstractProjectRole implements FeatureProjectRole {

  private EclipseProject         _project;

  /** the feature */
  private FeatureManifest        _feature;

  /** the build properties */
  private FeatureBuildProperties _buildProperties;

  /**
   * Returns the feature project role. If a feature project role is not set, an exception will be thrown.
   * 
   * @return Returns the feature project role.
   */
  public static FeatureProjectRoleImpl getFeatureProjectRole(EclipseProject eclipseProject) {
    Assure.assertTrue(hasFeatureProjectRole(eclipseProject), "Project \"" + eclipseProject.getFolderName()
        + "\" must have FeatureProjectRole!");

    return eclipseProject.getRole(FeatureProjectRoleImpl.class);
  }

  /**
   * Returns whether a feature project role is set or not.
   * 
   * @return Returns whether a feature project role is set or not.
   */
  public static boolean hasFeatureProjectRole(EclipseProject eclipseProject) {
    Assure.notNull("eclipseProject", eclipseProject);
    return eclipseProject.hasRole(FeatureProjectRoleImpl.class);
  }

  /**
   * <p>
   * Creates a new instance of type FeatureProjectRole.
   * </p>
   * 
   * @param eclipseProject
   *          the plugin project.
   */
  public FeatureProjectRoleImpl(EclipseProject eclipseProject) {
    super(FEATURE_PROJECT_ROLE_NAME, eclipseProject);
    Assure.notNull("eclipseProject", eclipseProject);
    this._project = eclipseProject;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public FeatureManifest getFeatureManifest() {
    return this._feature;
  }

  /**
   * <p>
   * Sets the feature manifest of feature project.
   * </p>
   * 
   * @param featuremanifest
   *          the feature manifest to set.
   */
  public void setFeature(FeatureManifest featuremanifest) {
    Assure.notNull("featuremanifest", featuremanifest);
    this._feature = featuremanifest;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean hasBuildProperties() {
    return this._buildProperties != null;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public FeatureBuildProperties getBuildProperties() {
    return this._buildProperties;
  }

  /**
   * <p>
   * Sets the build properties.
   * </p>
   */
  // TODO: should not be public to the rest of ant4eclipse!
  public void setBuildProperties(FeatureBuildProperties buildProperties) {
    this._buildProperties = buildProperties;
  }

  public File getFeatureXml() {
    return this._project.getChild("feature.xml");
  }

}
