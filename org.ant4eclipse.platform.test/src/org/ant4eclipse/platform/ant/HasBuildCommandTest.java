package org.ant4eclipse.platform.ant;

import org.ant4eclipse.platform.test.AbstractWorkspaceBasedBuildFileTest;
import org.ant4eclipse.platform.test.builder.EclipseProjectBuilder;

import org.apache.tools.ant.BuildException;

public class HasBuildCommandTest extends AbstractWorkspaceBasedBuildFileTest {

  @Override
  public void setUp() throws Exception {
    super.setUp();

    setupBuildFile("hasBuildCommand.xml");
  }

  public void testBuildCommand() {
    new EclipseProjectBuilder("simpleproject").withBuilder("myBuildCommand").createIn(getTestWorkspaceDirectory());
    expectLog("testHasBuildCommand", "OK");
  }

  public void testNoCommandSet() {
    new EclipseProjectBuilder("simpleproject").withBuilder("myBuildCommand").createIn(getTestWorkspaceDirectory());
    try {
      executeTarget("testNoCommandSet");
    } catch (BuildException e) {
      assertEquals("Attribute 'buildCommand' has to be set!", e.getMessage());
    }
  }
}
