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

import org.ant4eclipse.core.Assert;
import org.ant4eclipse.core.exception.Ant4EclipseException;
import org.ant4eclipse.core.logging.A4ELogging;

import org.ant4eclipse.pde.PdeExceptionCode;
import org.ant4eclipse.pde.internal.model.featureproject.FeatureProjectRoleImpl;
import org.ant4eclipse.pde.model.buildproperties.BuildPropertiesParser;

import org.ant4eclipse.platform.model.resource.EclipseProject;
import org.ant4eclipse.platform.model.resource.role.ProjectRole;
import org.ant4eclipse.platform.model.resource.role.ProjectRoleIdentifier;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

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

    final FeatureProjectRoleImpl featureProjectRole = new FeatureProjectRoleImpl(project);
    final File featureDescription = featureProjectRole.getFeatureXml();

    try {
      final FeatureManifest feature = FeatureManifestParser.parseFeature(new FileInputStream(featureDescription));
      featureProjectRole.setFeature(feature);
    } catch (final FileNotFoundException e) {
      throw new Ant4EclipseException(PdeExceptionCode.FEATURE_MANIFEST_FILE_NOT_FOUND, project.getFolder()
          .getAbsolutePath());
    }

    // parse build properties
    if (project.hasChild(BuildPropertiesParser.BUILD_PROPERTIES)) {
      BuildPropertiesParser.parseFeatureBuildProperties(featureProjectRole);
    }

    return featureProjectRole;
  }

  /**
   * {@inheritDoc}
   */
  public void postProcess(final EclipseProject project) {
  }

} /* ENDCLASS */
