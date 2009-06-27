package org.ant4eclipse.core.ant;

import org.ant4eclipse.core.service.ServiceRegistry;

import org.apache.tools.ant.BuildFileTest;
import org.apache.tools.ant.Project;

public class AbstractAnt4EclipseDataTypeTest extends BuildFileTest {

  public void setUp() {
    configureProject("src/org/ant4eclipse/core/ant/AbstractAnt4EclipseDataTypeTest.xml");
  }

  @Override
  protected void tearDown() throws Exception {
    super.tearDown();
    ServiceRegistry.reset();
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
    public void setAttribute(final String attribute) {
      getProject().log("setAttribute(" + attribute + ")");
    }
  }
}
