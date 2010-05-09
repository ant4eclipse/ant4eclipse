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
import org.apache.tools.ant.BuildException;

public class HasNatureTest extends AbstractWorkspaceBasedBuildFileTest {

  @Override
  public void setUp() throws Exception {
    super.setUp();
    setupBuildFile("hasNature.xml");
    EclipseProjectBuilder builder = new EclipseProjectBuilder("simpleproject");
    builder.withNature("org.ant4eclipse.testnature");
    builder.createIn(getTestWorkspaceDirectory());
  }

  public void testNonexistingNature() {
    expectLog("testNonexistingNature", "OK");
  }

  public void testExistingNature() {
    expectLog("testExistingNature", "OK");
  }

  public void testProjectAttribute() {
    try {
      expectLog("testProjectAttribute", "OK");
    } catch (BuildException e) {
      // ok
      return;
    }
    fail();
  }
}
