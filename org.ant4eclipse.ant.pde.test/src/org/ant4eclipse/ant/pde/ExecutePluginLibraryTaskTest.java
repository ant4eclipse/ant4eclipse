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
package org.ant4eclipse.ant.pde;

import org.ant4eclipse.pde.test.builder.PdeProjectBuilder;

import org.ant4eclipse.testframework.AbstractTestDirectoryBasedBuildFileTest;

public class ExecutePluginLibraryTaskTest extends AbstractTestDirectoryBasedBuildFileTest {

  @Override
  public void setUp() {
    super.setUp();

    configureProject("src/org/ant4eclipse/pde/ant/ExecutePluginLibraryTaskTest.xml");
  }

  public void testPdeProjectFileSet_simple() {

    // set some ant properties
    getProject().setProperty("workspaceDirectory", getTestDirectory().getRootDir().getAbsolutePath());
    getProject().setProperty("project.name", "simpleproject1");
    getProject().setProperty("library.name", ".");

    // create simple project
    PdeProjectBuilder pdeProjectBuilder = PdeProjectBuilder.getPreConfiguredPdeProjectBuilder("simpleproject1");
    pdeProjectBuilder.withSourceClass("src", "de.gerd-wuetherich.test.Test").finishClass();
    pdeProjectBuilder.withSourceClass("src", "de.gerd-wuetherich.test.Test2").finishClass();
    pdeProjectBuilder.withBuildProperties().withLibrary(".").withSource("src").withSource("resource").withOutput("bin")
        .finishLibrary();
    pdeProjectBuilder.createIn(getTestDirectoryRootDir());

    // expected log
    String rootPath = getTestDirectoryRootDir().getAbsolutePath();
    String expectedLog = normalize("- %1s\\simpleproject1\\bin -- bin -- %1s\\simpleproject1\\src -- src -- %1s\\simpleproject1\\resource -- resource -");

    // execute test
    expectLog("testExecutePluginLibrary", String.format(expectedLog, rootPath, rootPath, rootPath));
  }

  public void testPdeProjectFileSet_libraryDoesNotExist() {

    // set some ant properties
    getProject().setProperty("workspaceDirectory", getTestDirectory().getRootDir().getAbsolutePath());
    getProject().setProperty("project.name", "simpleproject1");
    getProject().setProperty("library.name", "hurz");

    // create simple project
    PdeProjectBuilder pdeProjectBuilder = PdeProjectBuilder.getPreConfiguredPdeProjectBuilder("simpleproject1");
    pdeProjectBuilder.withSourceClass("src", "de.gerd-wuetherich.test.Test").finishClass();
    pdeProjectBuilder.withSourceClass("src", "de.gerd-wuetherich.test.Test2").finishClass();
    pdeProjectBuilder.withBuildProperties().withLibrary(".").withSource("src").withSource("resource").withOutput("bin")
        .finishLibrary();
    pdeProjectBuilder.createIn(getTestDirectoryRootDir());

    // execute test
    expectSpecificBuildException("testExecutePluginLibrary", "libraryDoesNotExist",
        "org.ant4eclipse.core.exception.Ant4EclipseException: Library 'hurz' doesn't exist in project 'simpleproject1'.");
  }

  public void testPdeProjectFileSet_libraryWithoutSource() {

    // set some ant properties
    getProject().setProperty("workspaceDirectory", getTestDirectory().getRootDir().getAbsolutePath());
    getProject().setProperty("project.name", "simpleproject1");
    getProject().setProperty("library.name", ".");

    // create simple project
    PdeProjectBuilder pdeProjectBuilder = PdeProjectBuilder.getPreConfiguredPdeProjectBuilder("simpleproject1");
    pdeProjectBuilder.withSourceClass("src", "de.gerd-wuetherich.test.Test").finishClass();
    pdeProjectBuilder.withSourceClass("src", "de.gerd-wuetherich.test.Test2").finishClass();
    pdeProjectBuilder.withBuildProperties().withLibrary(".").withOutput("bin").finishLibrary();
    pdeProjectBuilder.createIn(getTestDirectoryRootDir());

    // execute test
    expectSpecificBuildException("testExecutePluginLibrary", "libraryDoesNotExist",
        "org.ant4eclipse.core.exception.Ant4EclipseException: Library '.' doesn't exist in project 'simpleproject1'.");
  }
}
