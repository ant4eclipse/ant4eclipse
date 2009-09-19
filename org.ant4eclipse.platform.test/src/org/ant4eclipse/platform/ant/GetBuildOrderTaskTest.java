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
package org.ant4eclipse.platform.ant;

import org.ant4eclipse.platform.test.AbstractWorkspaceBasedBuildFileTest;
import org.ant4eclipse.platform.test.builder.EclipseProjectBuilder;

public class GetBuildOrderTaskTest extends AbstractWorkspaceBasedBuildFileTest {

  @Override
  public void setUp() throws Exception {
    super.setUp();

    setupBuildFile("getBuildOrder.xml");
  }

  public void testGetBuildOrder_1() {
    new EclipseProjectBuilder("simpleproject_1").createIn(getTestWorkspaceDirectory());
    new EclipseProjectBuilder("simpleproject_2").withProjectReference("simpleproject_1").createIn(
        getTestWorkspaceDirectory());

    getTestWorkspace().createFile("projectSet.psf", createPsfContent());

    expectLog("getBuildOrder", "simpleproject_1,simpleproject_2");
  }

  public void testGetBuildOrder_2() {
    new EclipseProjectBuilder("simpleproject_1").withProjectReference("simpleproject_2").createIn(
        getTestWorkspaceDirectory());
    new EclipseProjectBuilder("simpleproject_2").createIn(getTestWorkspaceDirectory());

    getTestWorkspace().createFile("projectSet.psf", createPsfContent());

    expectLog("getBuildOrder", "simpleproject_2,simpleproject_1");
  }

  private String createPsfContent() {
    StringBuffer buffer = new StringBuffer();

    buffer.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
    buffer.append("<psf version=\"2.0\">");
    buffer.append("<provider id=\"org.tigris.subversion.subclipse.core.svnnature\">");
    buffer
        .append("<project reference=\"0.9.3,http://svn.javakontor.org/ant4eclipse/trunk/simpleproject_1,simpleproject_1\"/>");
    buffer
        .append("<project reference=\"0.9.3,http://svn.javakontor.org/ant4eclipse/trunk/simpleproject_2,simpleproject_2\"/>");
    buffer.append("</provider>");
    buffer.append("</psf>");

    return buffer.toString();
  }
}
