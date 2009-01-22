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
package org.ant4eclipse.pde.model.featureproject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import org.ant4eclipse.core.Assert;
import org.ant4eclipse.core.logging.A4ELogging;
import org.ant4eclipse.pde.model.buildproperties.BuildPropertiesParser;
import org.ant4eclipse.platform.model.resource.EclipseProject;
import org.ant4eclipse.platform.model.resource.role.ProjectRole;
import org.ant4eclipse.platform.model.resource.role.ProjectRoleIdentifier;

/**
 * <p>
 * Identifier for the feature project role.
 * </p>
 */
public class FeatureProjectRoleIdentifier implements ProjectRoleIdentifier {

  /**
   * {@inheritDoc}
   */
  public boolean isRoleSupported(final EclipseProject project) {
    return (project.hasNature(FeatureProjectRole.FEATURE_NATURE));
  }

  /**
   * {@inheritDoc}
   */
  public ProjectRole createRole(final EclipseProject project) {
    A4ELogging.debug("FeatureProjectRoleIdentifier.applyRole(%s)", project);
    Assert.notNull(project);
    final FeatureProjectRole featureProjectRole = new FeatureProjectRole(project);

    final File featureDescription = featureProjectRole.getFeatureXml();

    try {
      final FeatureManifest feature = FeatureManifestParser.parseFeature(new FileInputStream(featureDescription));
      featureProjectRole.setFeature(feature);
    } catch (final FileNotFoundException e) {
      // TODO A4eExcetpion
      throw new RuntimeException(e.getMessage(), e);
    }
    if (project.hasChild(BuildPropertiesParser.BUILD_PROPERTIES)) {
      BuildPropertiesParser.parseFeatureBuildProperties(featureProjectRole);
    }

    return featureProjectRole;
  }
} /* ENDCLASS */
