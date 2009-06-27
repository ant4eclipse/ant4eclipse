package org.ant4eclipse.platform.ant;

import org.ant4eclipse.platform.test.AbstractWorkspaceBasedBuildFileTest;
import org.ant4eclipse.platform.test.builder.EclipseProjectBuilder;

import java.io.File;

public class GetProjectDirecoryTest extends AbstractWorkspaceBasedBuildFileTest {

  @Override
  public void setUp() throws Exception {
    super.setUp();

    setupBuildFile("getProjectDirectory.xml");

    new EclipseProjectBuilder("simpleproject").withNature("org.ant4eclipse.testnature").createIn(
        getTestWorkspaceDirectory());
  }

  public void testGetProjectDirectory() {
    expectLog("testGetProjectDirectory", getTestWorkspaceDirectory().getAbsolutePath() + File.separator
        + "simpleproject");
  }
}
