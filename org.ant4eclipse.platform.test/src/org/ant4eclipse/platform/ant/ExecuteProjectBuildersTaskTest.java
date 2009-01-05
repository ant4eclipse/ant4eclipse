package org.ant4eclipse.platform.ant;

import org.ant4eclipse.platform.test.AbstractTestWorkspaceBuildFileTest;
import org.ant4eclipse.platform.test.builder.EclipseProjectBuilder;

public class ExecuteProjectBuildersTaskTest extends AbstractTestWorkspaceBuildFileTest {

  @Override
  public void setUp() throws Exception {
    super.setUp();

    setupBuildFile("executeProjectBuilders.xml");

    new EclipseProjectBuilder("simpleproject").withNature("org.ant4eclipse.testnature").withBuilder(
        "org.eclipse.jdt.core.javabuilder").withBuilder("org.ant4eclipse.anotherbuilder").createIn(
        getTestWorkspaceDirectory());
  }

  public void testNonexistingNature() {
    expectLog("executeProjectBuilders", "org.eclipse.jdt.core.javabuilderorg.ant4eclipse.anotherbuilder");
  }

}
