package org.ant4eclipse.core.ant;

import org.apache.tools.ant.Project;

public class AbstractAnt4EclipseDataTypeTest extends AbstractAnt4EclipseBuildFileTest {

  @Override
  protected String getBuildFileName() {
    return "AbstractAnt4EclipseDataTypeTest.xml";
  }

  public void testAbstractAnt4EclipseDataType() {
    expectLog("testAbstractAnt4EclipseDataType", "setAttribute(myAttribute)");
  }

  public static class Ant4EclipseDataType extends AbstractAnt4EclipseDataType {

    public Ant4EclipseDataType(Project project) {
      super(project);
    }

    /**
     * Changes the user library configuration file.
     * 
     * @param userlib
     *          The new user library configuration file.
     */
    public void setAttribute(String attribute) {
      getProject().log("setAttribute(" + attribute + ")");
    }
  }
}
