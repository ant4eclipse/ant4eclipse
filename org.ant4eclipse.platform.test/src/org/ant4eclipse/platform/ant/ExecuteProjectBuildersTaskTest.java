package org.ant4eclipse.platform.ant;

import org.ant4eclipse.platform.test.AbstractWorkspaceBasedBuildFileTest;
import org.ant4eclipse.platform.test.builder.EclipseProjectBuilder;

public class ExecuteProjectBuildersTaskTest extends AbstractWorkspaceBasedBuildFileTest {

  @Override
  public void setUp() throws Exception {
    super.setUp();

    setupBuildFile("executeProjectSet.xml");
  }

  public void testExecuteProjectBuilders_1() {
    new EclipseProjectBuilder("simpleproject_1").createIn(getTestWorkspaceDirectory());
    new EclipseProjectBuilder("simpleproject_2").createIn(getTestWorkspaceDirectory());

    getTestWorkspace().createFile("projectSet.psf", createPsfContent());

    expectLog("executeProjectSet", "simpleproject_1simpleproject_2");
  }

  private String createPsfContent() {
    StringBuffer buffer = new StringBuffer();

    buffer.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
    buffer.append("<psf version=\"2.0\">");
    buffer.append("<provider id=\"org.tigris.subversion.subclipse.core.svnnature\">");
    buffer
        .append("<project reference=\"0.9.3,http://svn.javakontor.org/ant4eclipse/trunk/simpleproject_1,simpleproject_1\"/>");
    buffer
        .append("<project reference=\"0.9.3,http://svn.javakontor.org/ant4eclipse/trunk/simpleproject_2,simpleproject_2\"/>");
    buffer.append("</provider>");
    buffer.append("</psf>");

    return buffer.toString();
  }
}
