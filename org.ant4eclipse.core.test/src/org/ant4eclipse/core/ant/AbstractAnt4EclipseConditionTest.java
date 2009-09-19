/**********************************************************************
 * Copyright (c) 2005-2009 ant4eclipse project team.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Nils Hartmann, Daniel Kasmeroglu, Gerd Wuetherich
 **********************************************************************/
package org.ant4eclipse.core.ant;

import org.ant4eclipse.core.CoreExceptionCode;
import org.ant4eclipse.core.exception.Ant4EclipseException;

public class AbstractAnt4EclipseConditionTest extends AbstractAnt4EclipseBuildFileTest {

  @Override
  protected String getBuildFileName() {
    return "AbstractAnt4EclipseConditionTest.xml";
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
