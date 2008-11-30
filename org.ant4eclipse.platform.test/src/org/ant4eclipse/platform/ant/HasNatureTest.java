package org.ant4eclipse.platform.ant;

import java.io.File;

import org.ant4eclipse.platform.test.AbstractPlatformBuildFileTest;
import org.ant4eclipse.platform.test.builder.builder.EclipseProjectBuilder;

public class HasNatureTest extends AbstractPlatformBuildFileTest {

  private File _workspaceDir;

  @Override
  public void setUp() throws Exception {
    super.setUp();

    setupBuildFile("hasNature.xml");
    _workspaceDir = getTestEnvironment().createSubDirectory("workspace");
    getProject().setProperty("workspaceDir", _workspaceDir.getAbsolutePath());

    new EclipseProjectBuilder("simpleproject").withNature("org.ant4eclipse.testnature").createIn(_workspaceDir);
  }

  public void testNonexistingNature() {
    expectLog("testNonexistingNature", "OK");
  }

  public void testExistingNature() {
    expectLog("testExistingNature", "OK");
  }
}
