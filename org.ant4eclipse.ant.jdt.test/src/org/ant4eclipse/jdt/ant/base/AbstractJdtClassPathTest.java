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
package org.ant4eclipse.jdt.ant.base;

/**
 * <p>
 * </p>
 * 
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 */
public abstract class AbstractJdtClassPathTest extends AbstractJdtTest {

  @Override
  public void setUp() throws Exception {
    super.setUp();

    // set up the build file
    setupBuildFile("getJdtClassPath.xml");
  }

  /**
   * @param projectName
   * @param runtimeClasspath
   * @param relative
   * 
   * @return
   * @throws Exception
   */
  protected String executeTestTarget(String projectName, boolean runtimeClasspath, boolean relative) throws Exception {
    return executeTestTarget(projectName, runtimeClasspath, relative, TEST_PATH_SEPARATOR);
  }

  /**
   * @param projectName
   * @param runtimeClasspath
   * @param relative
   * @param pathSeparator
   * 
   * @return
   * @throws Exception
   */
  protected String executeTestTarget(String projectName, boolean runtimeClasspath, boolean relative,
      String pathSeparator) throws Exception {

    assertNotNull(projectName);
    assertNotNull(pathSeparator);

    // set the properties
    getProject().setProperty("projectName", projectName);
    getProject().setProperty("runtimeClasspath", Boolean.toString(runtimeClasspath));
    getProject().setProperty("relative", Boolean.toString(relative));

    // execute target
    executeTarget("getJdtClassPath");

    // return the class path
    String classpath = getProject().getProperty("classpath");
    assertNotNull(classpath);
    return classpath;
  }
}
