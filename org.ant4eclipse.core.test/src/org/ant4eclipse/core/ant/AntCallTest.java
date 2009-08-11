package org.ant4eclipse.core.ant;

import java.io.File;

public class AntCallTest extends AbstractAnt4EclipseBuildFileTest {

  @Override
  protected String getBuildFileName() {
    return "AntCallTest.xml";
  }

  public void setUp() throws Exception {
    super.setUp();
    String userDir = System.getProperty("user.dir");

    File workspaceDir = new File(userDir);

    getProject().setProperty("basedir", workspaceDir.getAbsolutePath() + File.separator + "src");
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
