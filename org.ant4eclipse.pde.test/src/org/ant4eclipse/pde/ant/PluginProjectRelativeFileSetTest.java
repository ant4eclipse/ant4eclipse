package org.ant4eclipse.pde.ant;

import org.ant4eclipse.pde.test.builder.PdeProjectBuilder;
import org.ant4eclipse.testframework.AbstractTestDirectoryBasedBuildFileTest;

public class PluginProjectRelativeFileSetTest extends AbstractTestDirectoryBasedBuildFileTest {

  public void setUp() {
    super.setUp();
    configureProject("src/org/ant4eclipse/pde/ant/PluginProjectRelativeFileSetTest.xml");
  }

  @Override
  protected void tearDown() {
  }

  public void testAbstractAnt4EclipseDataType() {
    getProject().setProperty("workspace", getTestDirectory().getRootDir().getAbsolutePath());
    getProject().setProperty("projectname", "simpleproject1");

    PdeProjectBuilder pdeProjectBuilder = PdeProjectBuilder.getPreConfiguredPdeProjectBuilder("simpleproject1");
    pdeProjectBuilder.withSourceClass("src", "de.gerd-wuetherich.test.Gerd");
    pdeProjectBuilder.withSourceClass("@dot", "de.gerd-wuetherich.test.Gerd");
    pdeProjectBuilder.createIn(getTestDirectoryRootDir());

    expectLog(
        "testMultipleDirectoriesFileSet",
        "C:\\DOKUME~1\\admin\\LOKALE~1\\Temp\\a4etest\\simpleproject1\\META-INF\\MANIFEST.MF  C:\\DOKUME~1\\admin\\LOKALE~1\\Temp\\a4etest\\simpleproject1\\@dot\\de\\gerd-wuetherich\\test\\Gerd.java");
  }
}
