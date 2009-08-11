package org.ant4eclipse.core.ant;


public class MultipleDirectoriesFileSetTest extends AbstractAnt4EclipseBuildFileTest {

  @Override
  protected String getBuildFileName() {
    return "MultipleDirectoriesFileSetTest.xml";
  }

  public void testAbstractAnt4EclipseDataType() {
    expectLog("testMultipleDirectoriesFileSet", "test");
  }
}
