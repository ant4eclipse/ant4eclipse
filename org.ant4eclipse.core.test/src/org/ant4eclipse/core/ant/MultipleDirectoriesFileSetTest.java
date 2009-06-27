package org.ant4eclipse.core.ant;

import org.ant4eclipse.core.service.ServiceRegistry;

import org.apache.tools.ant.BuildFileTest;

public class MultipleDirectoriesFileSetTest extends BuildFileTest {

  public void setUp() {
    configureProject("src/org/ant4eclipse/core/ant/MultipleDirectoriesFileSetTest.xml");
  }

  @Override
  protected void tearDown() throws Exception {
    super.tearDown();
    ServiceRegistry.reset();
  }

  public void testAbstractAnt4EclipseDataType() {
    expectLog("testMultipleDirectoriesFileSet", "test");
  }
}
