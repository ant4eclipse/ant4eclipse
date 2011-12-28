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
package org.ant4eclipse.ant.core;

import java.io.File;

public class AntCallTest extends AbstractAnt4EclipseBuildFileTest {

  @Override
  protected String getBuildFileName() {
    return "AntCallTest.xml";
  }

  @Override
  public void setUp() throws Exception {
    super.setUp();
    getTestDirectory().createFile("AntCallTest-callback.xml", getResource("AntCallTest-callback.xml").openStream());
    String userDir = System.getProperty("user.dir");
    File workspaceDir = new File(userDir);
    getProject().setProperty("basedir", workspaceDir.getAbsolutePath() + File.separator + "src");
  }

  /**
   * <p>
   * </p>
   */
  public void testAntCall() {
    expectLog("testAntCall", "beforedoExecuteafter");
  }

  public void testFileNotFoundAntCall() {
    expectBuildException("testFileNotFoundAntCall", "cause");
  }
}
