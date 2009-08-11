package org.ant4eclipse.core.ant;

import org.ant4eclipse.core.CoreExceptionCode;
import org.ant4eclipse.core.exception.Ant4EclipseException;

public class AbstractAnt4EclipseTaskTest extends AbstractAnt4EclipseBuildFileTest {

  @Override
  protected String getBuildFileName() {
    return "AbstractAnt4EclipseTaskTest.xml";
  }

  public void testAbstractAnt4EclipseTask() {
    expectLog("testAbstractAnt4EclipseTask", "doExecute");
  }

  public void testAbstractAnt4EclipseFailureTask() {
    expectSpecificBuildException("testAbstractAnt4EclipseFailureTask", "Ant4EclipseException",
        "org.ant4eclipse.core.exception.Ant4EclipseException: Directory 'Bla' could not be created for an unkown reason");
  }

  public static class Ant4EclipseTask extends AbstractAnt4EclipseTask {

    @Override
    protected void doExecute() throws Ant4EclipseException {
      getProject().log("doExecute");
    }
  }

  public static class Ant4EclipseFailureTask extends AbstractAnt4EclipseTask {

    @Override
    protected void doExecute() throws Ant4EclipseException {
      throw new Ant4EclipseException(CoreExceptionCode.DIRECTORY_COULD_NOT_BE_CREATED, "Bla");
    }
  }
}
