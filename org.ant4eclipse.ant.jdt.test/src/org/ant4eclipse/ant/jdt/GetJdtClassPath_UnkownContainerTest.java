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

import org.ant4eclipse.ant.jdt.ant.base.AbstractJdtClassPathTest;

import java.io.File;

/**
 * <p>
 * Tests, if the JDT classpath container
 * </p>
 * 
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 */
public class GetJdtClassPath_UnkownContainerTest extends AbstractJdtClassPathTest {

  /** - */
  private File _simpleProjectBinDir;

  @Override
  public void setUp() throws Exception {
    super.setUp();

    JdtProjectBuilder builder = JdtProjectBuilder.getPreConfiguredJdtBuilder("simpleproject")
        .withContainerClasspathEntry("UnkownContainer");

    this._simpleProjectBinDir = new File(builder.createIn(getTestWorkspaceDirectory()), "bin");
  }

  /**
   * <p>
   * </p>
   * 
   * @throws Exception
   */
  public void testSimple() throws Exception {
    try {
      String classpath = executeTestTarget("simpleproject", false, false);
      assertClasspath(classpath, this._simpleProjectBinDir);
    } catch (Exception e) {
      return;
    }
    fail();
  }
}
