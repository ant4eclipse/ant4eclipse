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
package org.ant4eclipse.ant.platform;

import org.ant4eclipse.platform.test.AbstractWorkspaceBasedBuildFileTest;

import org.ant4eclipse.testframework.EclipseProjectBuilder;

public class ExecuteProjectBuildersTaskTest extends AbstractWorkspaceBasedBuildFileTest {

  @Override
  public void setUp() throws Exception {
    super.setUp();

    setupBuildFile("executeProjectSet.xml");
  }

  public void testExecuteProjectBuilders_1() {
    new EclipseProjectBuilder("simpleproject_1").createIn(getTestWorkspaceDirectory());
    new EclipseProjectBuilder("simpleproject_2").createIn(getTestWorkspaceDirectory());

    getTestWorkspace().createFile("projectSet.psf", createPsfContent(false));

    expectLog("executeProjectSet", "simpleproject_1simpleproject_2");
  }

  public void testExecuteProjectBuilders_2() {

    // this variety also makes use of the filter functionality
    new EclipseProjectBuilder("simpleproject_1").createIn(getTestWorkspaceDirectory());
    new EclipseProjectBuilder("simpleproject_2.test").createIn(getTestWorkspaceDirectory());

    getTestWorkspace().createFile("projectSet.psf", createPsfContent(true));

    expectLog("executeProjectSetFiltered", "simpleproject_1");
  }

  private String createPsfContent(boolean test) {
    StringBuffer buffer = new StringBuffer();
    String part = test ? ".test" : "";
    buffer.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
    buffer.append("<psf version=\"2.0\">");
    buffer.append("<provider id=\"org.tigris.subversion.subclipse.core.svnnature\">");
    buffer
        .append("<project reference=\"0.9.3,http://svn.javakontor.org/ant4eclipse/trunk/simpleproject_1,simpleproject_1\"/>");
    buffer
        .append(String
            .format(
                "<project reference=\"0.9.3,http://svn.javakontor.org/ant4eclipse/trunk/simpleproject_2%s,simpleproject_2%s\"/>",
                part, part));
    buffer.append("</provider>");
    buffer.append("</psf>");

    return buffer.toString();
  }
}
