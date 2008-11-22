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

import org.ant4eclipse.platform.model.resource.EclipseProject;
import org.ant4eclipse.testframework.Ant4EclipseTestCase;
import org.junit.Test;

public class ProjectRoleIdentifierRegistryTest extends Ant4EclipseTestCase {

  @Override
  protected void fillAnt4EclipseConfigurationProperties(Properties properties) {
    super.fillAnt4EclipseConfigurationProperties(properties);

    // add TestProjectRoleIdentifier to ant4eclipse properties
    properties.setProperty(ProjectRoleIdentifierRegistry.ROLEIDENTIFIER_PREFIX + ".dummyRole",
        TestProjectRoleIdentifier.class.getName());
  }

  @Test
  public void test_init() {

    ProjectRoleIdentifierRegistry registry = new ProjectRoleIdentifierRegistry();
    Iterable<ProjectRoleIdentifier> projectRoleIdentifiers = registry.getProjectRoleIdentifiers();
    assertNotNull(projectRoleIdentifiers);
    // check at least one role
    assertTrue(projectRoleIdentifiers.iterator().hasNext());
    assertTrue(projectRoleIdentifiers.iterator().next() instanceof TestProjectRoleIdentifier);
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
