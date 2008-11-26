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
package org.ant4eclipse.platform.model.resource.role;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Properties;

import org.ant4eclipse.platform.model.internal.resource.role.ProjectRoleIdentifierRegistry;
import org.ant4eclipse.platform.model.resource.EclipseProject;
import org.ant4eclipse.testframework.Ant4EclipseTestCase;
import org.junit.Test;

public class ProjectRoleIdentifierRegistryTest extends Ant4EclipseTestCase {

  @Override
  protected Properties customAnt4EclipseConfiguration(Properties properties) {

    // add TestProjectRoleIdentifier to ant4eclipse properties
    properties.setProperty(ProjectRoleIdentifierRegistry.ROLEIDENTIFIER_PREFIX + ".dummyRole",
        TestProjectRoleIdentifier.class.getName());

    return properties;
  }

  @Test
  public void test_init() {

    ProjectRoleIdentifierRegistry registry = new ProjectRoleIdentifierRegistry();
    Iterable<ProjectRoleIdentifier> projectRoleIdentifiers = registry.getProjectRoleIdentifiers();
    assertNotNull(projectRoleIdentifiers);
    // check at least one role
    assertTrue(projectRoleIdentifiers.iterator().hasNext());
    Object roleIdentifier = projectRoleIdentifiers.iterator().next();
    assertTrue(roleIdentifier.getClass().getName(), roleIdentifier instanceof TestProjectRoleIdentifier);
  }

  public static class TestProjectRoleIdentifier implements ProjectRoleIdentifier {

    public ProjectRole createRole(EclipseProject project) {
      return null;
    }

    public boolean isRoleSupported(EclipseProject project) {
      return false;
    }
  }

}
