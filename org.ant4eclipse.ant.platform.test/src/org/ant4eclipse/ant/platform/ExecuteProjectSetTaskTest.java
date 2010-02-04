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

import java.io.File;

public class ExecuteProjectSetTaskTest extends AbstractWorkspaceBasedBuildFileTest {

  private static final String[] PROJECTNAMES_VALID = new String[] { "org.ant4eclipse.lib.core",
      "org.ant4eclipse.lib.core.test", "org.ant4eclipse.external", "org.ant4eclipse.lib.jdt",
      "org.ant4eclipse.lib.jdt.ecj", "org.ant4eclipse.lib.jdt.test", "org.ant4eclipse.lib.platform",
      "org.ant4eclipse.lib.platform.test", "org.ant4eclipse.lib.pde", "org.ant4eclipse.lib.pde.test",
      "org.ant4eclipse.lib.pydt", "org.ant4eclipse.lib.pydt.test" };

  /**
   * {@inheritDoc}
   */
  @Override
  public void setUp() throws Exception {

    super.setUp();

    boolean first = true;
    for (String projectname : PROJECTNAMES_VALID) {
      EclipseProjectBuilder builder = new EclipseProjectBuilder(projectname);
      if (first) {
        File input = new File("input");
        File file1 = new File(input, "valid-projectset.psf");
        File file2 = new File(input, "invalid-projectset.psf");
        builder.withResource(file1);
        builder.withResource(file2);
        first = false;
      }
      builder.createIn(getTestWorkspaceDirectory());
    }

    setupBuildFile("executeProjectSet.xml");

  }

  /**
   * @note [04-Feb-2010:KASI] This snippet was laying around within the ExecuteProjectBuildersTaskTest. It's obviously
   *       meant to be part of this test, so make use of it ;-)
   */
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

  /**
   * {@inheritDoc}
   */
  @Override
  public void tearDown() throws Exception {
  }

  public void testExecuteProjectSet() {
    StringBuffer expected = new StringBuffer();
    for (String projectname : PROJECTNAMES_VALID) {
      expected.append(String.format("%s%s%s~", getTestWorkspaceDirectory().getAbsolutePath(), File.separator,
          projectname));
    }
    expectLog("executeProjectSet", expected.toString());
  }

  public void testExecuteProjectSetFiltered() {
    StringBuffer expected = new StringBuffer();
    for (String projectname : PROJECTNAMES_VALID) {
      if (!projectname.endsWith(".test")) {
        expected.append(String.format("%s%s%s~", getTestWorkspaceDirectory().getAbsolutePath(), File.separator,
            projectname));
      }
    }
    expectLog("executeProjectSetFiltered", expected.toString());
  }

  public void testExecuteInvalidProjectSet() {
    StringBuffer expected = new StringBuffer();
    for (String projectname : PROJECTNAMES_VALID) {
      if (projectname.indexOf(".jdt") == -1) {
        expected.append(String.format("%s%s%s~", getTestWorkspaceDirectory().getAbsolutePath(), File.separator,
            projectname));
      }
    }
    expectLog("executeInvalidProjectSet", expected.toString());
  }

  public void testExecuteInvalidProjectSetFiltered() {
    StringBuffer expected = new StringBuffer();
    for (String projectname : PROJECTNAMES_VALID) {
      if (!projectname.endsWith(".test")) {
        if (projectname.indexOf(".jdt") == -1) {
          expected.append(String.format("%s%s%s~", getTestWorkspaceDirectory().getAbsolutePath(), File.separator,
              projectname));
        }
      }
    }
    expectLog("executeInvalidProjectSetFiltered", expected.toString());
  }

}
