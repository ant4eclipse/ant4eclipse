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
package org.ant4eclipse.platform.model.resource.internal.factory;

import java.io.File;
import java.util.Properties;

import org.ant4eclipse.core.Assert;
import org.ant4eclipse.core.logging.A4ELogging;
import org.ant4eclipse.core.util.ManifestHelper;
import org.ant4eclipse.core.util.ManifestHelper.ManifestHeaderElement;
import org.ant4eclipse.platform.model.resource.EclipseProject;
import org.ant4eclipse.platform.model.resource.internal.EclipseProjectImpl;
import org.ant4eclipse.platform.model.resource.internal.WorkspaceImpl;
import org.ant4eclipse.platform.model.resource.role.ProjectRoleIdentifier;


public class ProjectFactory {

  static {
    try {
      final Properties properties = new Properties();
      // TODO!!!
      properties.load(ProjectFactory.class.getClassLoader().getResources("/roleidentifier.properties"));

      // TODO
      Assert.assertTrue(properties.containsKey("roleidentifiers"),
          "Property 'roleidentifiers' has to be defined in property file 'roleidentifier.properties'!");

      final ManifestHeaderElement[] elements = ManifestHelper.getManifestHeaderElements(properties
          .getProperty("roleidentifiers"));

      for (int i = 0; i < elements.length; i++) {
        final ManifestHeaderElement manifestHeaderElement = elements[i];
        final String[] classNames = manifestHeaderElement.getValues();
        for (int j = 0; j < classNames.length; j++) {
          final String className = classNames[j];
          final Class<?> clazz = ProjectFactory.class.getClassLoader().loadClass(className);
          final Object instance = clazz.newInstance();
          // TODO ASSERT
          ProjectRoleIdentifierRegistry.addRoleIdentifier((ProjectRoleIdentifier) instance);
        }
      }

    } catch (final Exception e) {
      e.printStackTrace();
    }
  }

  public static EclipseProject readProjectFromWorkspace(final WorkspaceImpl workspace, final File projectDirectory) {

    A4ELogging.debug("ProjectFactory: readProjectFromWorkspace(%s, %s)", new Object[] { workspace,
        projectDirectory.getAbsolutePath() });

    Assert.notNull(workspace);
    Assert.isDirectory(projectDirectory);

    final EclipseProjectImpl project = new EclipseProjectImpl(workspace, projectDirectory);

    // parses the project description
    ProjectFileParser.parseProject(project);

    // apply role specific information
    ProjectRoleIdentifierRegistry.applyRoles(project);

    A4ELogging.debug("ProjectFactory: return '%s'", project);

    return project;

  }
}
