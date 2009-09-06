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

import org.ant4eclipse.pydt.test.AbstractWorkspaceBasedTest;
import org.ant4eclipse.pydt.test.BuildResult;
import org.ant4eclipse.pydt.test.data.ProjectDescription;
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
    final ProjectDescription projectdescription = createEmptyProject(_pythonpathxml, 0);
    final BuildResult buildresult = execute(projectdescription.getPrimaryProjectname(), "get-python-path");
    final String[] content = buildresult.getTargetOutput("get-python-path");
    Assert.assertEquals(1, content.length);
    Assert.assertEquals("${workspacedir}" + File.separator + projectdescription.getPrimaryProjectname(), content[0]);
  }

  @Test
  public void emptyProjectRelative() {
    final ProjectDescription projectdescription = createEmptyProject(_pythonpathxml, 0);
    final BuildResult buildresult = execute(projectdescription.getPrimaryProjectname(), "get-python-path-relative");
    final String[] content = buildresult.getTargetOutput("get-python-path-relative");
    Assert.assertEquals(1, content.length);
    Assert.assertEquals(".", content[0]);
  }

  @Test
  public void emptyProjectDirseparator() {
    final ProjectDescription projectdescription = createEmptyProject(_pythonpathxml, 0);
    final BuildResult buildresult = execute(projectdescription.getPrimaryProjectname(), "get-python-path-dirseparator",
        "@");
    final String[] content = buildresult.getTargetOutput("get-python-path-dirseparator");
    Assert.assertEquals(1, content.length);
    Assert.assertEquals("${workspacedir}@" + projectdescription.getPrimaryProjectname(), content[0]);
  }

  @Test
  public void emptyProjectInternalLibs() {
    final ProjectDescription projectdescription = createEmptyProject(_pythonpathxml, KIND_INTERNALLIBRARYPRIMARY);
    final BuildResult buildresult = execute(projectdescription.getPrimaryProjectname(), "get-python-path");
    final String[] content = buildresult.getTargetOutput("get-python-path");
    Assert.assertEquals(1, content.length);
    Assert.assertEquals("${workspacedir}" + File.separator + projectdescription.getPrimaryProjectname()
        + File.pathSeparator + getInternalLibs(projectdescription, null, false), content[0]);
  }

  private String getInternalLibs(final ProjectDescription description, final String dirseparator,
      final boolean projectrelative) {
    String[] internallibs = description.getInternalLibs(dirseparator);
    StringBuffer buffer = new StringBuffer();
    for (int i = 0; i < internallibs.length; i++) {
      if (buffer.length() > 0) {
        buffer.append(File.pathSeparator);
      }
      if (projectrelative) {
        buffer.append(internallibs[i].substring(2 + description.getPrimaryProjectname().length()));
      } else {
        buffer.append("${workspacedir}");
        buffer.append(internallibs[i]);
      }
    }
    return buffer.toString();
  }

  @Test
  public void emptyProjectRelativeInternalLibs() {
    final ProjectDescription projectdescription = createEmptyProject(_pythonpathxml, KIND_INTERNALLIBRARYPRIMARY);
    final BuildResult buildresult = execute(projectdescription.getPrimaryProjectname(), "get-python-path-relative");
    final String[] content = buildresult.getTargetOutput("get-python-path-relative");
    Assert.assertEquals(1, content.length);
    Assert.assertEquals("." + File.pathSeparator + getInternalLibs(projectdescription, null, true), content[0]);
  }

  @Test
  public void emptyProjectDirseparatorInternalLibs() {
    final ProjectDescription projectdescription = createEmptyProject(_pythonpathxml, KIND_INTERNALLIBRARYPRIMARY);
    final BuildResult buildresult = execute(projectdescription.getPrimaryProjectname(), "get-python-path-dirseparator",
        "@");
    final String[] content = buildresult.getTargetOutput("get-python-path-dirseparator");
    Assert.assertEquals(1, content.length);
    Assert.assertEquals("${workspacedir}@" + projectdescription.getPrimaryProjectname() + File.pathSeparator
        + getInternalLibs(projectdescription, "@", false), content[0]);
  }

  @Test
  public void complexProject() {
    final ProjectDescription projectdescription = createComplexProject(_pythonpathxml, 0);
    final BuildResult buildresult = execute(projectdescription.getPrimaryProjectname(), "get-python-path");
    final String[] content = buildresult.getTargetOutput("get-python-path");
    Assert.assertEquals(1, content.length);
    Assert.assertEquals("${workspacedir}" + File.separator + projectdescription.getPrimaryProjectname()
        + File.pathSeparator + "${workspacedir}" + File.separator + projectdescription.getSecondaryProjectname(),
        content[0]);
  }

  @Test
  public void complexProjectRelative() {
    final ProjectDescription projectdescription = createComplexProject(_pythonpathxml, 0);
    final BuildResult buildresult = execute(projectdescription.getPrimaryProjectname(), "get-python-path-relative");
    final String[] content = buildresult.getTargetOutput("get-python-path-relative");
    Assert.assertEquals(1, content.length);
    Assert.assertEquals(".", content[0]);
  }

  @Test
  public void complexProjectDirseparator() {
    final ProjectDescription projectdescription = createComplexProject(_pythonpathxml, 0);
    final BuildResult buildresult = execute(projectdescription.getPrimaryProjectname(), "get-python-path-dirseparator",
        "@");
    final String[] content = buildresult.getTargetOutput("get-python-path-dirseparator");
    Assert.assertEquals(1, content.length);
    Assert.assertEquals("${workspacedir}@" + projectdescription.getPrimaryProjectname() + File.pathSeparator
        + "${workspacedir}@" + projectdescription.getSecondaryProjectname(), content[0]);
  }

  // @Test
  // public void complexProjectInternalLibs() {
  // final ProjectDescription projectdescription = createComplexProject(_pythonpathxml, KIND_INTERNALLIBRARYPRIMARY
  // | KIND_INTERNALLIBRARYSECONDARY);
  // final BuildResult buildresult = execute(projectdescription.getPrimaryProjectname(), "get-python-path");
  // final String[] content = buildresult.getTargetOutput("get-python-path");
  // Assert.assertEquals(1, content.length);
  // Assert.assertEquals("${workspacedir}" + File.separator + projectdescription.getPrimaryProjectname()
  // + File.pathSeparator + "${workspacedir}" + File.separator + projectdescription.getSecondaryProjectname()
  // + File.pathSeparator + getInternalLibs(projectdescription, null, false), content[0]);
  // }

  // @Test
  // public void complexProjectRelativeInternalLibs() {
  // final ProjectDescription projectdescription = createComplexProject(_pythonpathxml, KIND_INTERNALLIBRARYPRIMARY
  // | KIND_INTERNALLIBRARYSECONDARY);
  // final BuildResult buildresult = execute(projectdescription.getPrimaryProjectname(), "get-python-path-relative");
  // final String[] content = buildresult.getTargetOutput("get-python-path-relative");
  // Assert.assertEquals(1, content.length);
  // Assert.assertEquals("." + File.pathSeparator + getInternalLibs(projectdescription, null, true), content[0]);
  // }

  // @Test
  // public void complexProjectDirseparatorInternalLibs() {
  // final ProjectDescription projectdescription = createComplexProject(_pythonpathxml, KIND_INTERNALLIBRARYPRIMARY
  // | KIND_INTERNALLIBRARYSECONDARY);
  // final BuildResult buildresult = execute(projectdescription.getPrimaryProjectname(), "get-python-path-dirseparator",
  // "@");
  // final String[] content = buildresult.getTargetOutput("get-python-path-dirseparator");
  // Assert.assertEquals(1, content.length);
  // Assert.assertEquals("${workspacedir}@" + projectdescription.getPrimaryProjectname() + File.pathSeparator
  // + "${workspacedir}@" + projectdescription.getSecondaryProjectname() + File.pathSeparator
  // + getInternalLibs(projectdescription, "@", false), content[0]);
  // }

  @Test
  public void cyclicProject() {
    final ProjectDescription projectdescription = createCyclicProject(_pythonpathxml, 0);
    final BuildResult buildresult = execute(projectdescription.getPrimaryProjectname(), "get-python-path");
    final String[] content = buildresult.getTargetOutput("get-python-path");
    Assert.assertEquals(1, content.length);
    Assert.assertEquals("${workspacedir}" + File.separator + projectdescription.getPrimaryProjectname()
        + File.pathSeparator + "${workspacedir}" + File.separator + projectdescription.getSecondaryProjectname(),
        content[0]);
  }

  @Test
  public void cyclicProjectRelative() {
    final ProjectDescription projectdescription = createCyclicProject(_pythonpathxml, 0);
    final BuildResult buildresult = execute(projectdescription.getPrimaryProjectname(), "get-python-path-relative");
    final String[] content = buildresult.getTargetOutput("get-python-path-relative");
    Assert.assertEquals(1, content.length);
    Assert.assertEquals(".", content[0]);
  }

  @Test
  public void cyclicProjectDirseparator() {
    final ProjectDescription projectdescription = createCyclicProject(_pythonpathxml, 0);
    final BuildResult buildresult = execute(projectdescription.getPrimaryProjectname(), "get-python-path-dirseparator",
        "@");
    final String[] content = buildresult.getTargetOutput("get-python-path-dirseparator");
    Assert.assertEquals(1, content.length);
    Assert.assertEquals("${workspacedir}@" + projectdescription.getPrimaryProjectname() + File.pathSeparator
        + "${workspacedir}@" + projectdescription.getSecondaryProjectname(), content[0]);
  }

} /* ENDCLASS */
