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
package org.ant4eclipse.ant.jdt.ant;

import org.ant4eclipse.jdt.test.builder.JdtProjectBuilder;

import org.ant4eclipse.ant.jdt.ant.base.AbstractJdtClassPathTest;

import java.io.File;

/**
 * <p>
 * </p>
 * 
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 */
public class UserLibrariesTest extends AbstractJdtClassPathTest {

  @Override
  public void setUp() throws Exception {
    super.setUp();

    // set up the build file
    setupBuildFile("userLibraries.xml");
  }

  /**
   * <p>
   * </p>
   * 
   * @throws Exception
   */
  public void testClasspathVariables() throws Exception {
    // create simple project 'project' with a source directory 'src' and a output directory 'bin'
    JdtProjectBuilder.getPreConfiguredJdtBuilder("project").withContainerClasspathEntry(
        "org.eclipse.jdt.USER_LIBRARY/testLibrary").createIn(getTestWorkspaceDirectory());

    //
    getTestWorkspace().createFile("myUserLibraries.xml", getContent());

    // set the properties
    getProject().setProperty("projectName", "project");

    // execute target
    String classpath = executeTestTarget("project", true, true);
    System.err.println(classpath);
    assertClasspath(classpath, new File("project/bin"), new File(getTestWorkspaceDirectory(), "haensel"));
  }

  private String getContent() {

    String pathDir = getTestWorkspaceDirectory().getAbsolutePath() + File.separatorChar + "haensel";

    getTestWorkspace().createSubDirectory("haensel");

    StringBuffer buffer = new StringBuffer();
    buffer.append("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>");
    buffer.append("<eclipse-userlibraries version=\"2\">");
    buffer.append("<library name=\"testLibrary\" systemlibrary=\"true\">");
    buffer.append("<archive path=\"");
    buffer.append(pathDir);
    buffer.append("\"/>");
    buffer.append("</library>");
    buffer.append("</eclipse-userlibraries>");

    return buffer.toString();
  }
}
