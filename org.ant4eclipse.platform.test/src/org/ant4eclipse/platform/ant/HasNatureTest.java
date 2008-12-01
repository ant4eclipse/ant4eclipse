package org.ant4eclipse.platform.ant;

import org.ant4eclipse.platform.test.AbstractPlatformBuildFileTest;
import org.ant4eclipse.platform.test.builder.builder.EclipseProjectBuilder;

public class HasNatureTest extends AbstractPlatformBuildFileTest {

  @Override
  public void setUp() throws Exception {
    super.setUp();

    setupBuildFile("hasNature.xml");
    new EclipseProjectBuilder("simpleproject").withNature("org.ant4eclipse.testnature").createIn(getWorkspaceDir());
  }

  public void testNonexistingNature() {
    expectLog("testNonexistingNature", "OK");
  }

  public void testExistingNature() {
    expectLog("testExistingNature", "OK");
  }
}
