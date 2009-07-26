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
package org.ant4eclipse.pde.internal.model.featureproject;

import java.io.File;

import org.ant4eclipse.core.Assert;
import org.ant4eclipse.pde.model.buildproperties.FeatureBuildProperties;
import org.ant4eclipse.pde.model.featureproject.FeatureManifest;
import org.ant4eclipse.pde.model.featureproject.FeatureProjectRole;

import org.ant4eclipse.platform.model.resource.EclipseProject;
import org.ant4eclipse.platform.model.resource.role.AbstractProjectRole;

/**
 * <p>
 * Implements the eclipse feature project role.
 * </p>
 * 
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 */
public class FeatureProjectRoleImpl extends AbstractProjectRole implements FeatureProjectRole {

  private final EclipseProject   _project;

  /** the feature */
  private FeatureManifest        _feature;

  /** the build properties */
  private FeatureBuildProperties _buildProperties;

  /**
   * Returns the feature project role. If a feature project role is not set, an exception will be thrown.
   * 
   * @return Returns the feature project role.
   */
  public static FeatureProjectRoleImpl getFeatureProjectRole(final EclipseProject eclipseProject) {
    Assert.assertTrue(hasFeatureProjectRole(eclipseProject), "Project \"" + eclipseProject.getFolderName()
        + "\" must have FeatureProjectRole!");

    return (FeatureProjectRoleImpl) eclipseProject.getRole(FeatureProjectRoleImpl.class);
  }

  /**
   * Returns whether a feature project role is set or not.
   * 
   * @return Returns whether a feature project role is set or not.
   */
  public static boolean hasFeatureProjectRole(final EclipseProject eclipseProject) {
    Assert.notNull(eclipseProject);

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
  public FeatureProjectRoleImpl(final EclipseProject eclipseProject) {
    super(FEATURE_PROJECT_ROLE_NAME, eclipseProject);
    Assert.notNull(eclipseProject);
    this._project = eclipseProject;
  }

  /**
   * <p>
   * Returns the feature manifest of the feature project.
   * </p>
   * 
   * @return the feature manifest.
   */
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
  public void setFeature(final FeatureManifest featuremanifest) {
    Assert.notNull(featuremanifest);

    this._feature = featuremanifest;
  }

  /**
   * @return
   */
  public boolean hasBuildProperties() {
    return this._buildProperties != null;
  }

  /**
   * @return Returns the buildProperties.
   */
  public FeatureBuildProperties getBuildProperties() {
    return this._buildProperties;
  }

  /**
   * <p>
   * Sets the build properties.
   * </p>
   */
  // TODO: should not be public to the rest of ant4eclipse!
  public void setBuildProperties(final FeatureBuildProperties buildProperties) {
    this._buildProperties = buildProperties;
  }

  public File getFeatureXml() {
    return this._project.getChild("feature.xml");
  }

}
