/**********************************************************************
 * Copyright (c) 2005-2008 ant4eclipse project team.
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

import org.ant4eclipse.jdt.test.AbstractJDTBuildFileTest;
import org.ant4eclipse.jdt.test.builder.JdtEclipseProjectBuilder;

public class GetEclipseClasspathTest extends AbstractJDTBuildFileTest {

  @Override
  public void setUp() throws Exception {
    super.setUp();

    setupBuildFile("getEclipseClasspath.xml");
    JdtEclipseProjectBuilder.getPreConfiguredJdtBuilder("simpleproject").createIn(getWorkspaceDir());
  }

  public void testSimple() throws Exception {
    executeTarget("test-simple");
    String classpath = getProject().getProperty("classpath");
    System.out.println(classpath);
    assertNotNull(classpath);

  }

}
