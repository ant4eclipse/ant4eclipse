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
  // Assure.assertTrue( hasFeatureProjectRole( eclipseProject ), String.format( "Project \"%s\" must have FeatureProjectRole!", eclipseProject.getFolderName() ) );
  public static FeatureProjectRoleImpl getFeatureProjectRole( EclipseProject eclipseProject ) {
    return eclipseProject.getRole( FeatureProjectRoleImpl.class );
  }

  /**
   * Returns whether a feature project role is set or not.
   * 
   * @return Returns whether a feature project role is set or not.
   */
  // Assure.notNull( "eclipseProject", eclipseProject );
  public static boolean hasFeatureProjectRole( EclipseProject eclipseProject ) {
    return eclipseProject.hasRole( FeatureProjectRoleImpl.class );
  }

  /**
   * <p>
   * Creates a new instance of type FeatureProjectRole.
   * </p>
   * 
   * @param eclipseProject
   *          the plugin project.
   */
  // Assure.notNull( "eclipseProject", eclipseProject );
  public FeatureProjectRoleImpl( EclipseProject eclipseProject ) {
    super( FEATURE_PROJECT_ROLE_NAME, eclipseProject );
    _project = eclipseProject;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public FeatureManifest getFeatureManifest() {
    return _feature;
  }

  /**
   * <p>
   * Sets the feature manifest of feature project.
   * </p>
   * 
   * @param featuremanifest
   *          the feature manifest to set.
   */
  // Assure.notNull( "featuremanifest", featuremanifest );
  public void setFeature( FeatureManifest featuremanifest ) {
    _feature = featuremanifest;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean hasBuildProperties() {
    return _buildProperties != null;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public FeatureBuildProperties getBuildProperties() {
    return _buildProperties;
  }

  /**
   * <p>
   * Sets the build properties.
   * </p>
   */
  // TODO: should not be public to the rest of ant4eclipse!
  public void setBuildProperties( FeatureBuildProperties buildProperties ) {
    _buildProperties = buildProperties;
  }

  public File getFeatureXml() {
    return _project.getChild( "feature.xml" );
  }

} /* ENDCLASS */
