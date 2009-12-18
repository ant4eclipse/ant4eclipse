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
package org.ant4eclipse.jdt.ant;


import org.ant4eclipse.jdt.ant.base.AbstractJdtClassPathTest;

import org.ant4eclipse.testframework.JdtProjectBuilder;

import java.io.File;

/**
 * <p>
 * </p>
 * 
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 */
public class ClasspathVariablesTest extends AbstractJdtClassPathTest {

  @Override
  public void setUp() throws Exception {
    super.setUp();

    // set up the build file
    setupBuildFile("classpathVariables.xml");
  }

  /**
   * <p>
   * </p>
   * 
   * @throws Exception
   */
  public void testClasspathVariables() throws Exception {
    // create simple project 'project' with a source directory 'src' and a output directory 'bin'
    JdtProjectBuilder.getPreConfiguredJdtBuilder("project").withVarClasspathEntry("BRUNO_WALTER")
        .withVarClasspathEntry("VAR2/test").createIn(getTestWorkspaceDirectory());

    // set the properties
    getProject().setProperty("projectName", "project");

    // execute target
    String classpath = executeTestTarget("project", true, true);
    assertClasspath(classpath, new File("project/bin"), new File(getTestWorkspaceDirectory(), "bruno_walter"),
        new File(getTestWorkspaceDirectory(), "var2/test"));
  }

}
