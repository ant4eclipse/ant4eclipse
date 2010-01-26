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

import org.apache.tools.ant.BuildException;

public class HasBuildCommandTest extends AbstractWorkspaceBasedBuildFileTest {

  @Override
  public void setUp() throws Exception {
    super.setUp();

    setupBuildFile("hasBuildCommand.xml");
  }

  public void testBuildCommand() {
    new EclipseProjectBuilder("simpleproject").withBuilder("myBuildCommand").createIn(getTestWorkspaceDirectory());
    expectLog("testHasBuildCommand", "OK");
  }

  public void testNoCommandSet() {
    new EclipseProjectBuilder("simpleproject").withBuilder("myBuildCommand").createIn(getTestWorkspaceDirectory());
    try {
      executeTarget("testNoCommandSet");
    } catch (BuildException e) {
      assertEquals("Attribute 'buildCommand' has to be set!", e.getMessage());
    }
  }
}
