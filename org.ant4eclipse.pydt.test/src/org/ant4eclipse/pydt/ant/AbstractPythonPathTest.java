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
package org.ant4eclipse.pydt.ant;

import org.ant4eclipse.core.util.Pair;

import org.ant4eclipse.pydt.test.AbstractWorkspaceBasedTest;
import org.ant4eclipse.pydt.test.BuildResult;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.net.URL;

/**
 * Basic test implementation for the ant task: 'getPythonPath'.
 * 
 * <ul>
 * <li>The <i>emptyXXX</i> tests do show the functionality of the task.</li>
 * <li>The <i>complexXXX</i> tests do show that dependencies don't alter the python pathes.</li>
 * <li>The <i>cyclicXXX</i> tests do show that dependencies don't alter the python pathes.</li>
 * </ul>
 * 
 * @todo [18-Aug-2009:KASI] Support of internal libraries, external libraries, external folders and runtimes.
 * 
 * @author Daniel Kasmeroglu (Daniel.Kasmeroglu@Kasisoft.net)
 */
public class AbstractPythonPathTest extends AbstractWorkspaceBasedTest {

  private URL _pythonpathxml;

  /**
   * Initialises this set of tests.
   */
  public AbstractPythonPathTest(final boolean dltk) {
    super(dltk);
  }

  /**
   * {@inheritDoc}
   */
  @Before
  public void setup() {
    super.setup();
    _pythonpathxml = getResource("/org/ant4eclipse/pydt/ant/pythonpath.xml");
  }

  /**
   * {@inheritDoc}
   */
  @After
  public void teardown() {
    super.teardown();
    _pythonpathxml = null;
  }

  @Test
  public void emptyProject() {
    final String projectname = createEmptyProject(_pythonpathxml, false);
    final BuildResult buildresult = execute(projectname, "get-python-path");
    final String[] content = buildresult.getTargetOutput("get-python-path");
    Assert.assertEquals(1, content.length);
    Assert.assertEquals("${workspacedir}" + File.separator + projectname, content[0]);
  }

  @Test
  public void emptyProjectRelative() {
    final String projectname = createEmptyProject(_pythonpathxml, false);
    final BuildResult buildresult = execute(projectname, "get-python-path-relative");
    final String[] content = buildresult.getTargetOutput("get-python-path-relative");
    Assert.assertEquals(1, content.length);
    Assert.assertEquals(".", content[0]);
  }

  @Test
  public void emptyProjectDirseparator() {
    final String projectname = createEmptyProject(_pythonpathxml, false);
    final BuildResult buildresult = execute(projectname, "get-python-path-dirseparator", "@");
    final String[] content = buildresult.getTargetOutput("get-python-path-dirseparator");
    Assert.assertEquals(1, content.length);
    Assert.assertEquals("${workspacedir}@" + projectname, content[0]);
  }

  @Test
  public void complexProject() {
    final Pair<String, String> projectname = createComplexProject(_pythonpathxml, false, false);
    final BuildResult buildresult = execute(projectname.getFirst(), "get-python-path");
    final String[] content = buildresult.getTargetOutput("get-python-path");
    Assert.assertEquals(1, content.length);
    Assert.assertEquals("${workspacedir}" + File.separator + projectname.getFirst() + File.pathSeparator
        + "${workspacedir}" + File.separator + projectname.getSecond(), content[0]);
  }

  @Test
  public void complexProjectRelative() {
    final Pair<String, String> projectname = createComplexProject(_pythonpathxml, false, false);
    final BuildResult buildresult = execute(projectname.getFirst(), "get-python-path-relative");
    final String[] content = buildresult.getTargetOutput("get-python-path-relative");
    Assert.assertEquals(1, content.length);
    Assert.assertEquals(".", content[0]);
  }

  @Test
  public void complexProjectDirseparator() {
    final Pair<String, String> projectname = createComplexProject(_pythonpathxml, false, false);
    final BuildResult buildresult = execute(projectname.getFirst(), "get-python-path-dirseparator", "@");
    final String[] content = buildresult.getTargetOutput("get-python-path-dirseparator");
    Assert.assertEquals(1, content.length);
    Assert.assertEquals("${workspacedir}@" + projectname.getFirst() + File.pathSeparator + "${workspacedir}@"
        + projectname.getSecond(), content[0]);
  }

  @Test
  public void cyclicProject() {
    final Pair<String, String> projectname = createCyclicProject(_pythonpathxml, false, false);
    final BuildResult buildresult = execute(projectname.getFirst(), "get-python-path");
    final String[] content = buildresult.getTargetOutput("get-python-path");
    Assert.assertEquals(1, content.length);
    Assert.assertEquals("${workspacedir}" + File.separator + projectname.getFirst() + File.pathSeparator
        + "${workspacedir}" + File.separator + projectname.getSecond(), content[0]);
  }

  @Test
  public void cyclicProjectRelative() {
    final Pair<String, String> projectname = createCyclicProject(_pythonpathxml, false, false);
    final BuildResult buildresult = execute(projectname.getFirst(), "get-python-path-relative");
    final String[] content = buildresult.getTargetOutput("get-python-path-relative");
    Assert.assertEquals(1, content.length);
    Assert.assertEquals(".", content[0]);
  }

  @Test
  public void cyclicProjectDirseparator() {
    final Pair<String, String> projectname = createCyclicProject(_pythonpathxml, false, false);
    final BuildResult buildresult = execute(projectname.getFirst(), "get-python-path-dirseparator", "@");
    final String[] content = buildresult.getTargetOutput("get-python-path-dirseparator");
    Assert.assertEquals(1, content.length);
    Assert.assertEquals("${workspacedir}@" + projectname.getFirst() + File.pathSeparator + "${workspacedir}@"
        + projectname.getSecond(), content[0]);
  }

} /* ENDCLASS */