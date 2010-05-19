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
package org.ant4eclipse.lib.jdt.tools;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

import org.ant4eclipse.lib.core.service.ServiceRegistryAccess;
import org.ant4eclipse.lib.platform.model.resource.EclipseProject;
import org.ant4eclipse.lib.platform.model.resource.Workspace;
import org.ant4eclipse.lib.platform.model.resource.workspaceregistry.DefaultEclipseWorkspaceDefinition;
import org.ant4eclipse.lib.platform.model.resource.workspaceregistry.WorkspaceRegistry;
import org.ant4eclipse.lib.platform.tools.BuildOrderResolver;
import org.ant4eclipse.testframework.ConfigurableAnt4EclipseTestCase;
import org.ant4eclipse.testframework.JdtProjectBuilder;
import org.ant4eclipse.testframework.TestDirectory;
import org.junit.Test;

import java.util.List;

public class BuildOrderResolverTest extends ConfigurableAnt4EclipseTestCase {

  private TestDirectory _testWorkspace;

  @Override
  public void setup() {
    super.setup();

    this._testWorkspace = new TestDirectory();

    JdtProjectBuilder.getPreConfiguredJdtBuilder("simpleproject1").createIn(this._testWorkspace.getRootDir());
    JdtProjectBuilder.getPreConfiguredJdtBuilder("simpleproject2").withClasspathEntry(
        "<classpathentry combineaccessrules=\"false\" kind=\"src\" path=\"/simpleproject1\"/>").createIn(
        this._testWorkspace.getRootDir());
    JdtProjectBuilder.getPreConfiguredJdtBuilder("simpleproject3").withClasspathEntry(
        "<classpathentry combineaccessrules=\"false\" kind=\"src\" path=\"/simpleproject2\"/>").createIn(
        this._testWorkspace.getRootDir());
  }

  @Override
  public void dispose() {
    this._testWorkspace.dispose();

    super.dispose();
  }

  @Test
  public void buildOrder() {
    WorkspaceRegistry workspaceRegistry = ServiceRegistryAccess.instance().getService(WorkspaceRegistry.class);
    Workspace workspace = workspaceRegistry.registerWorkspace(this._testWorkspace.getRootDir().getAbsolutePath(),
        new DefaultEclipseWorkspaceDefinition(this._testWorkspace.getRootDir()));

    // List<EclipseProject> projects = ReferencedProjectsResolver.resolveReferencedProjects(workspace
    // .getProject("simpleproject3"), null);

    List<EclipseProject> projects = BuildOrderResolver.resolveBuildOrder(workspace, new String[] { "simpleproject3",
        "simpleproject2" }, null, null);

    assertEquals(2, projects.size());
    assertSame(workspace.getProject("simpleproject2"), projects.get(0));
    assertSame(workspace.getProject("simpleproject3"), projects.get(1));
  }
}
