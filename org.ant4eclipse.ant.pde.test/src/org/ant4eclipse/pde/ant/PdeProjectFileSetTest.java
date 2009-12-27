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
package org.ant4eclipse.pde.ant;


import org.ant4eclipse.testframework.AbstractTestDirectoryBasedBuildFileTest;
import org.ant4eclipse.testframework.PdeProjectBuilder;

public class PdeProjectFileSetTest extends AbstractTestDirectoryBasedBuildFileTest {

  @Override
  public void setUp() {
    super.setUp();
    configureProject("src/org/ant4eclipse/pde/ant/PdeProjectFileSetTest.xml");
  }

  public void testPdeProjectFileSet_simple() {

    // set some ant properties
    getProject().setProperty("workspace", getTestDirectory().getRootDir().getAbsolutePath());
    getProject().setProperty("projectname", "simpleproject1");
    getProject().setProperty("projectname", "simpleproject1");
    getProject().setProperty("bin.includes", "META-INF/,.,my/src/");
    getProject().setProperty("bin.excludes", "my/src/de/gerd-wuetherich/test/Test.java,**/Test3.java");

    // create simple project
    PdeProjectBuilder pdeProjectBuilder = PdeProjectBuilder.getPreConfiguredPdeProjectBuilder("simpleproject1");
    pdeProjectBuilder.withSourceClass("@dot", "de.gerd-wuetherich.test.Gerd").finishClass();
    pdeProjectBuilder.withSourceClass("my/src", "de.gerd-wuetherich.test.Test").finishClass();
    pdeProjectBuilder.withSourceClass("my/src", "de.gerd-wuetherich.test.Test2").finishClass();
    pdeProjectBuilder.withSourceClass("my/src", "de.gerd-wuetherich.test.Test3").finishClass();
    pdeProjectBuilder.createIn(getTestDirectoryRootDir());

    // expected log
    String rootPath = getTestDirectoryRootDir().getAbsolutePath();
    String expectedLog = normalize("%s\\simpleproject1\\META-INF\\MANIFEST.MF  %s\\simpleproject1\\@dot\\de\\gerd-wuetherich\\test\\Gerd.java  %s\\simpleproject1\\my\\src\\de\\gerd-wuetherich\\test\\Test2.java");

    // execute test
    expectLog("testMultipleDirectoriesFileSet", String.format(expectedLog, rootPath, rootPath, rootPath));
  }
}
