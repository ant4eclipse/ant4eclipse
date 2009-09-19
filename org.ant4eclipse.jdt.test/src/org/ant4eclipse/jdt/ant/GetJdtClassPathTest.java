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
package org.ant4eclipse.jdt.ant;

import org.ant4eclipse.jdt.test.builder.JdtProjectBuilder;

import org.ant4eclipse.jdt.ant.base.AbstractJdtClassPathTest;

import java.io.File;

/**
 * <p>
 * Tests, if the JDT classpath container
 * </p>
 * 
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 */
public class GetJdtClassPathTest extends AbstractJdtClassPathTest {

  private File _simpleProjectBinDir;

  private File _projectBBinDir;

  @Override
  public void setUp() throws Exception {
    super.setUp();
    File simpleProjectDir = JdtProjectBuilder.getPreConfiguredJdtBuilder("simpleproject").createIn(
        getTestWorkspaceDirectory());
    this._simpleProjectBinDir = new File(simpleProjectDir, "bin");

    // projectB depends on simpleProject but doesn't re-export it
    File projectbDir = JdtProjectBuilder.getPreConfiguredJdtBuilder("projectb").withSrcClasspathEntry("/simpleproject",
        false).createIn(getTestWorkspaceDirectory());
    this._projectBBinDir = new File(projectbDir, "bin");
  }

  public void testSimple() throws Exception {
    String classpath = executeTestTarget("simpleproject", false, false);
    assertClasspath(classpath, this._simpleProjectBinDir);
  }

  public void testSimple_Relative() throws Exception {
    String classpath = executeTestTarget("simpleproject", false, true);
    assertClasspath(classpath, new File("simpleproject/bin"));
  }

  public void testSimple_TwoClassFolders() throws Exception {
    File projectcDir = JdtProjectBuilder.getPreConfiguredJdtBuilder("projectc").withSrcClasspathEntry("gen-src",
        "gen-classes", false).createIn(getTestWorkspaceDirectory());
    String classpath = executeTestTarget("projectc", false, false);

    // class path must contain 'bin' and 'gen-classes'
    assertClasspath(classpath, new File(projectcDir, "bin"), new File(projectcDir, "gen-classes"));
  }

  public void test_WithProjectReferences() throws Exception {
    String classpath = executeTestTarget("projectb", false, false);
    assertClasspath(classpath, this._projectBBinDir, this._simpleProjectBinDir);
  }

  public void test_WithMultipleReferences() throws Exception {

    // projectC references b. Since b doesn't reexport simple project,
    // simpleproject will be invisible
    File projectcDir = JdtProjectBuilder.getPreConfiguredJdtBuilder("projectc").withSrcClasspathEntry("/projectb",
        false).createIn(getTestWorkspaceDirectory());

    String classpath = executeTestTarget("projectc", false, false);
    File projectCBinDir = new File(projectcDir, "bin");
    assertClasspath(classpath, projectCBinDir, this._projectBBinDir);
  }

  public void test_WithMultipleReferencesRuntime() throws Exception {

    // projectC references b. Since b doesn't reexport simple project,
    // anyway: simpleproject will be visible
    // since we ask for runtime classpath
    File projectcDir = JdtProjectBuilder.getPreConfiguredJdtBuilder("projectc").withSrcClasspathEntry("/projectb",
        false).createIn(getTestWorkspaceDirectory());

    String classpath = executeTestTarget("projectc", true, false);
    File projectCBinDir = new File(projectcDir, "bin");
    assertClasspath(classpath, projectCBinDir, this._projectBBinDir, this._simpleProjectBinDir);
  }
}
