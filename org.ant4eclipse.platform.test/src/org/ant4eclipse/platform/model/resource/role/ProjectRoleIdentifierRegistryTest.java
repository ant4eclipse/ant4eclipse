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
package org.ant4eclipse.platform.model.resource.role;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.ant4eclipse.core.util.StringMap;

import org.ant4eclipse.platform.internal.model.resource.role.ProjectRoleIdentifierRegistry;
import org.ant4eclipse.platform.model.resource.EclipseProject;

import org.ant4eclipse.testframework.ConfigurableAnt4EclipseTestCase;
import org.junit.Test;

public class ProjectRoleIdentifierRegistryTest extends ConfigurableAnt4EclipseTestCase {

  @Override
  protected StringMap customAnt4EclipseConfiguration(StringMap properties) {

    // add TestProjectRoleIdentifier to ant4eclipse properties
    properties.put(ProjectRoleIdentifierRegistry.ROLEIDENTIFIER_PREFIX + ".dummyRole", TestProjectRoleIdentifier.class
        .getName());

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

    /**
     * {@inheritDoc}
     */
    public void postProcess(EclipseProject project) {
    }

  }

}
