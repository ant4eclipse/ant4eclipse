package org.ant4eclipse.core.ant;

import org.ant4eclipse.core.service.ServiceRegistry;

import org.apache.tools.ant.BuildFileTest;

import java.io.File;

public class AntCallTest extends BuildFileTest {

  public void setUp() {
    configureProject("src/org/ant4eclipse/core/ant/AntCallTest.xml");

    String userDir = System.getProperty("user.dir");

    File workspaceDir = new File(userDir);

    getProject().setProperty("basedir", workspaceDir.getAbsolutePath() + File.separator + "src");
  }

  @Override
  protected void tearDown() throws Exception {
    super.tearDown();
    ServiceRegistry.reset();
  }

  /**
   * <p>
   * </p>
   */
  public void testAntCall() {
    expectLog("testAntCall", "beforedoExecuteafter");
  }

  public void testFileNotFoundAntCall() {
    expectBuildException("testFileNotFoundAntCall", "cause");
  }
}
