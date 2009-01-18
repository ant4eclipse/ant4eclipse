package org.ant4eclipse.jdt.tools;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

import java.util.List;

import org.ant4eclipse.jdt.test.builder.JdtProjectBuilder;
import org.ant4eclipse.platform.model.resource.EclipseProject;
import org.ant4eclipse.platform.model.resource.Workspace;
import org.ant4eclipse.platform.model.resource.workspaceregistry.DefaultEclipseWorkspaceDefinition;
import org.ant4eclipse.platform.model.resource.workspaceregistry.WorkspaceRegistry;
import org.ant4eclipse.platform.test.TestWorkspace;
import org.ant4eclipse.testframework.ConfigurableAnt4EclipseTestCase;
import org.junit.Test;

public class BuildOrderResolverTest extends ConfigurableAnt4EclipseTestCase {

  private TestWorkspace _testWorkspace;

  @Override
  public void setup() {
    super.setup();

    _testWorkspace = new TestWorkspace();

    JdtProjectBuilder.getPreConfiguredJdtBuilder("simpleproject1").createIn(_testWorkspace.getRootDir());
    JdtProjectBuilder.getPreConfiguredJdtBuilder("simpleproject2").withClasspathEntry(
        "<classpathentry combineaccessrules=\"false\" kind=\"src\" path=\"/simpleproject1\"/>").createIn(
        _testWorkspace.getRootDir());
    JdtProjectBuilder.getPreConfiguredJdtBuilder("simpleproject3").withClasspathEntry(
        "<classpathentry combineaccessrules=\"false\" kind=\"src\" path=\"/simpleproject2\"/>").createIn(
        _testWorkspace.getRootDir());
  }

  @Override
  public void dispose() {
    _testWorkspace.dispose();

    super.dispose();
  }

  @Test
  public void buildOrder() {
    WorkspaceRegistry workspaceRegistry = WorkspaceRegistry.Helper.getRegistry();
    Workspace workspace = workspaceRegistry.registerWorkspace(_testWorkspace.getRootDir().getAbsolutePath(),
        new DefaultEclipseWorkspaceDefinition(_testWorkspace.getRootDir()));

    // List<EclipseProject> projects = ReferencedProjectsResolver.resolveReferencedProjects(workspace
    // .getProject("simpleproject3"), null);

    List<EclipseProject> projects = JdtResolver.resolveBuildOrder(workspace, new String[] { "simpleproject3",
        "simpleproject2" }, null);

    assertEquals(2, projects.size());
    assertSame(workspace.getProject("simpleproject2"), projects.get(0));
    assertSame(workspace.getProject("simpleproject3"), projects.get(1));
  }
}
