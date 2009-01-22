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
import org.ant4eclipse.core.logging.A4ELogging;
import org.ant4eclipse.pde.model.buildproperties.BuildPropertiesParser;
import org.ant4eclipse.platform.model.resource.EclipseProject;
import org.ant4eclipse.platform.model.resource.role.ProjectRole;
import org.ant4eclipse.platform.model.resource.role.ProjectRoleIdentifier;
import org.eclipse.osgi.service.resolver.BundleDescription;

/**
 * <p>
 * Identifier for plugin project roles.
 * </p>
 */
public class PluginProjectRoleIdentifier implements ProjectRoleIdentifier {

  /**
   * {@inheritDoc}
   */
  public boolean isRoleSupported(final EclipseProject project) {
    return (project.hasNature(PluginProjectRole.PLUGIN_NATURE));
  }

  /**
   * {@inheritDoc}
   */
  public ProjectRole createRole(final EclipseProject project) {
    A4ELogging.debug("PluginProjectRoleIdentifier.applyRole(%s)", project);
    Assert.notNull(project);

    BundleDescription description;
    try {
      description = BundleDescriptionLoader.loadFromPluginProject(project);
    } catch (final Exception e) {
      throw new RuntimeException(e.getMessage(), e);
    }

    final PluginProjectRole pluginProjectRole = new PluginProjectRole(project, description);

    // TODO: umbauen...
    if (project.hasChild(BuildPropertiesParser.BUILD_PROPERTIES)) {
      BuildPropertiesParser.parsePluginBuildProperties(pluginProjectRole);
    }

    return pluginProjectRole;
  }
} /* ENDCLASS */
