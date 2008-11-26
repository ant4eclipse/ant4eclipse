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
package org.ant4eclipse.jdt;

import java.io.File;

import org.ant4eclipse.jdt.test.builder.JdtEclipseProjectBuilder;
import org.ant4eclipse.platform.test.builder.TestEnvironment;
import org.ant4eclipse.testframework.Ant4EclipseTestCase;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * Abstract base class for JDT-related test cases
 * 
 * @author Nils Hartmann (nils@nilshartmann.net)
 */
public class AbstractJDTTest extends Ant4EclipseTestCase {

  /**
   * The TestEnvironment
   * 
   */
  private TestEnvironment _testEnvironment;

  @Before
  public void initTestEnvironment() throws Exception {
    _testEnvironment = new TestEnvironment();
  }

  @After
  public void disposeTestEnvironment() throws Exception {
    _testEnvironment.dispose();
    _testEnvironment = null;
  }

  /**
   * Returns a "pre-configured" {@link JdtEclipseProjectBuilder}, that already has set:
   * <ul>
   * <li>a java builder</li>
   * <li>the java nature
   * <li>
   * <li>the JRE container classpath entry</li>
   * <li>a source folder (<tt>src</tt>)</li>
   * <li>a default output folder (<tt>bin</tt>)</li>
   * </ul>
   * 
   * The builder returned can be used to further customize the project
   * 
   * @param projectName
   *          The name of the project
   * @return
   */
  protected JdtEclipseProjectBuilder getPreConfiguredJdtBuilder(String projectName) {
    return new JdtEclipseProjectBuilder(projectName).withJavaBuilder().withJavaNature()
        .withJreContainerClasspathEntry().withSrcClasspathEntry("src", false).withOutputClasspathEntry("bin");

  }

  // ---- TODO: remove the following (demonstration only) -----------------------------

  @Test
  public void test_JavaProject() throws Exception {
    File workspaceDir = _testEnvironment.createSubDirectory("workspace");
    new JdtEclipseProjectBuilder("my.project").withJavaNature().withJavaBuilder().withJreContainerClasspathEntry()
        .withSrcClasspathEntry("src", false).createIn(workspaceDir);
  }

  @Test
  public void test_JavaProjectSimple() throws Exception {
    File workspaceDir = _testEnvironment.createSubDirectory("workspace");
    getPreConfiguredJdtBuilder("my.simple.project").createIn(workspaceDir);
  }

}
