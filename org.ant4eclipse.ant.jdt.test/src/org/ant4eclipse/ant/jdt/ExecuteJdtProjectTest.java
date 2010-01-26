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
package org.ant4eclipse.ant.jdt;

import org.ant4eclipse.jdt.test.builder.JdtProjectBuilder;

import org.ant4eclipse.ant.jdt.ant.base.AbstractJdtTest;

import java.io.File;

/**
 * <p>
 * </p>
 * 
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 */
public class ExecuteJdtProjectTest extends AbstractJdtTest {

  @Override
  public void setUp() throws Exception {
    super.setUp();

    // set up the build file
    setupBuildFile("executeJdtProject.xml");
  }

  /**
   * <p>
   * </p>
   */
  public void testExecuteJdtProject() {
    // create simple project 'project' with a source directory 'src' and a output directory 'bin'
    JdtProjectBuilder.getPreConfiguredJdtBuilder("project").createIn(getTestWorkspaceDirectory());

    // set the properties
    getProject().setProperty("projectName", "project");

    // execute target
    executeTarget("executeJdtProject_forEachSourceDirectory");

    // equals
    String[] logEntries = getLog().split("!");

    assertEquals(9, logEntries.length);

    // ${executeJdtProject.classpath.relative.runtime}
    assertClasspath(logEntries[0], new File("project/bin"));

    // ${executeJdtProject.classpath.absolute.runtime}
    assertClasspath(logEntries[1], new File(getTestWorkspaceDirectory(), "project/bin"));

    // ${executeJdtProject.classpath.relative.compiletime}
    assertClasspath(logEntries[2], new File("project/bin"));

    // ${executeJdtProject.classpath.absolute.compiletime}
    assertClasspath(logEntries[3], new File(getTestWorkspaceDirectory(), "project/bin"));

    // ${executeJdtProject.default.output.directory}
    assertClasspath(logEntries[4], new File(getTestWorkspaceDirectory(), "project/bin"));

    // ${executeJdtProject.default.output.directory.name}
    assertClasspath(logEntries[5], new File("bin"));

    // ${executeJdtProject.source.directory}
    assertClasspath(logEntries[6], new File(getTestWorkspaceDirectory(), "project/src"));

    // ${executeJdtProject.output.directory}
    assertClasspath(logEntries[7], new File(getTestWorkspaceDirectory(), "project/bin"));

    // ${executeJdtProject.boot.classpath}
    // How should we test this?
    // assertClasspath(logEntries[8], new File(getTestWorkspaceDirectory(), "project/bin"));
  }
}
