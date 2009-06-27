package org.ant4eclipse.core.ant;

import org.ant4eclipse.core.CoreExceptionCode;
import org.ant4eclipse.core.exception.Ant4EclipseException;
import org.ant4eclipse.core.service.ServiceRegistry;

import org.apache.tools.ant.BuildFileTest;

public class AbstractAnt4EclipseConditionTest extends BuildFileTest {

  public void setUp() {
    configureProject("src/org/ant4eclipse/core/ant/AbstractAnt4EclipseConditionTest.xml");
  }

  @Override
  protected void tearDown() throws Exception {
    super.tearDown();
    ServiceRegistry.reset();
  }

  public void testAbstractAnt4EclipseTrueCondition() {
    assertPropertyUnset("test");
    expectLog("testAbstractAnt4EclipseTrueCondition", "doEval");
    assertPropertySet("test");
  }

  public void testAbstractAnt4EclipseFalseCondition() {
    assertPropertyUnset("test");
    expectLog("testAbstractAnt4EclipseFalseCondition", "doEval");
    assertPropertyUnset("test");
  }

  public void testAbstractAnt4EclipseFailureCondition() {
    assertPropertyUnset("test");
    expectSpecificBuildException("testAbstractAnt4EclipseFailureCondition", "Ant4EclipseException",
        "org.ant4eclipse.core.exception.Ant4EclipseException: Directory 'Bla' could not be created for an unkown reason");
    assertPropertyUnset("test");
  }

  public static class Ant4EclipseFalseCondition extends AbstractAnt4EclipseCondition {
    @Override
    protected boolean doEval() {
      getProject().log("doEval");
      return false;
    }
  }

  public static class Ant4EclipseTrueCondition extends AbstractAnt4EclipseCondition {
    @Override
    protected boolean doEval() {
      getProject().log("doEval");
      return true;
    }
  }

  public static class Ant4EclipseFailureCondition extends AbstractAnt4EclipseCondition {

    @Override
    protected boolean doEval() {
      throw new Ant4EclipseException(CoreExceptionCode.DIRECTORY_COULD_NOT_BE_CREATED, "Bla");
    }
  }
}
