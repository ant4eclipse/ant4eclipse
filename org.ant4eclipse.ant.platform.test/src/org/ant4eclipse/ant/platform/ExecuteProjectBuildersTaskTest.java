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

  private static final String BUILDER_JAVA    = "org.eclipse.jdt.core.javabuilder";

  private static final String BUILDER_ANOTHER = "org.ant4eclipse.anotherbuilder";

  @Override
  public void setUp() throws Exception {
    super.setUp();
    EclipseProjectBuilder builder = new EclipseProjectBuilder("simpleproject");
    builder.withBuilder(BUILDER_JAVA);
    builder.withBuilder(BUILDER_ANOTHER);
    builder.createIn(getTestWorkspaceDirectory());
    setupBuildFile("executeProjectBuilders.xml");
  }

  public void testExecuteBuildCommands() {
    expectLog("executeProjectBuilders", String.format("%s~%s~", BUILDER_JAVA, BUILDER_ANOTHER));
  }

}
