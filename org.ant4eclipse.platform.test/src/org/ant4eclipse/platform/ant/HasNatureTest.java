package org.ant4eclipse.platform.ant;

import org.ant4eclipse.platform.test.AbstractWorkspaceBasedBuildFileTest;
import org.ant4eclipse.platform.test.builder.EclipseProjectBuilder;

import org.apache.tools.ant.BuildException;

public class HasNatureTest extends AbstractWorkspaceBasedBuildFileTest {

  @Override
  public void setUp() throws Exception {
    super.setUp();

    setupBuildFile("hasNature.xml");

    new EclipseProjectBuilder("simpleproject").withNature("org.ant4eclipse.testnature").createIn(
        getTestWorkspaceDirectory());

    new EclipseProjectBuilder("gwtproject").withNature("com.google.gwt.eclipse.core.gwtNature").createIn(
        getTestWorkspaceDirectory());
  }

  public void testGwtNatureLong() {
    expectLog("testGwtNatureLong", "OK");
  }

  public void testGwtNatureShort() {
    expectLog("testGwtNatureShort", "OK");
  }

  public void testNonexistingNature() {
    expectLog("testNonexistingNature", "OK");
  }

  public void testExistingNature() {
    expectLog("testExistingNature", "OK");
  }

  public void testProjectAttribute() {
    try {
      expectLog("testProjectAttribute", "OK");
    } catch (BuildException e) {
      // ok
      return;
    }
    fail();
  }
}
