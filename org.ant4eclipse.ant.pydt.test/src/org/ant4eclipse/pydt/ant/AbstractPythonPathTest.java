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

import org.ant4eclipse.ant.pydt.test.AbstractWorkspaceBasedTest;
import org.ant4eclipse.ant.pydt.test.BuildResult;
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
  public AbstractPythonPathTest(boolean dltk) {
    super(dltk);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  @Before
  public void setup() {
    super.setup();
    this._pythonpathxml = getResource("/org/ant4eclipse/pydt/ant/pythonpath.xml");
  }

  /**
   * {@inheritDoc}
   */
  @Override
  @After
  public void teardown() {
    super.teardown();
    this._pythonpathxml = null;
  }

  @Test
  public void emptyProject() {
    ProjectDescription projectdescription = createEmptyProject(this._pythonpathxml, 0);
    BuildResult buildresult = execute(projectdescription.getPrimaryProjectname(), "get-python-path");
    String[] content = buildresult.getTargetOutput("get-python-path");
    Assert.assertEquals(1, content.length);
    Assert.assertEquals("${workspacedir}" + File.separator + projectdescription.getPrimaryProjectname(), content[0]);
  }

  @Test
  public void emptyProjectRelative() {
    ProjectDescription projectdescription = createEmptyProject(this._pythonpathxml, 0);
    BuildResult buildresult = execute(projectdescription.getPrimaryProjectname(), "get-python-path-relative");
    String[] content = buildresult.getTargetOutput("get-python-path-relative");
    Assert.assertEquals(1, content.length);
    Assert.assertEquals(".", content[0]);
  }

  @Test
  public void emptyProjectDirseparator() {
    ProjectDescription projectdescription = createEmptyProject(this._pythonpathxml, 0);
    BuildResult buildresult = execute(projectdescription.getPrimaryProjectname(), "get-python-path-dirseparator", "@");
    String[] content = buildresult.getTargetOutput("get-python-path-dirseparator");
    Assert.assertEquals(1, content.length);
    Assert.assertEquals("${workspacedir}@" + projectdescription.getPrimaryProjectname(), content[0]);
  }

  @Test
  public void emptyProjectInternalLibs() {
    ProjectDescription projectdescription = createEmptyProject(this._pythonpathxml, KIND_INTERNALLIBRARYPRIMARY);
    BuildResult buildresult = execute(projectdescription.getPrimaryProjectname(), "get-python-path");
    String[] content = buildresult.getTargetOutput("get-python-path");
    Assert.assertEquals(1, content.length);
    Assert.assertEquals("${workspacedir}" + File.separator + projectdescription.getPrimaryProjectname()
        + File.pathSeparator + getInternalLibs(projectdescription, null, false, null), content[0]);
  }

  private String getInternalLibs(ProjectDescription description, String dirseparator, boolean projectrelative,
      Boolean primary) {
    String[] internallibs = description.getInternalLibs(dirseparator, primary);
    StringBuffer buffer = new StringBuffer();
    if (dirseparator == null) {
      dirseparator = File.separator;
    }
    String primaryprefix = dirseparator + description.getPrimaryProjectname();
    for (String internallib : internallibs) {
      if (buffer.length() > 0) {
        buffer.append(File.pathSeparator);
      }
      if (projectrelative) {
        if (internallib.startsWith(primaryprefix)) {
          buffer.append(internallib.substring(1 + primaryprefix.length()));
        } else {
          buffer.append(".." + internallib);
        }
      } else {
        buffer.append("${workspacedir}");
        buffer.append(internallib);
      }
    }
    return buffer.toString();
  }

  @Test
  public void emptyProjectRelativeInternalLibs() {
    ProjectDescription projectdescription = createEmptyProject(this._pythonpathxml, KIND_INTERNALLIBRARYPRIMARY);
    BuildResult buildresult = execute(projectdescription.getPrimaryProjectname(), "get-python-path-relative");
    String[] content = buildresult.getTargetOutput("get-python-path-relative");
    Assert.assertEquals(1, content.length);
    Assert.assertEquals("." + File.pathSeparator + getInternalLibs(projectdescription, null, true, null), content[0]);
  }

  @Test
  public void emptyProjectDirseparatorInternalLibs() {
    ProjectDescription projectdescription = createEmptyProject(this._pythonpathxml, KIND_INTERNALLIBRARYPRIMARY);
    BuildResult buildresult = execute(projectdescription.getPrimaryProjectname(), "get-python-path-dirseparator", "@");
    String[] content = buildresult.getTargetOutput("get-python-path-dirseparator");
    Assert.assertEquals(1, content.length);
    Assert.assertEquals("${workspacedir}@" + projectdescription.getPrimaryProjectname() + File.pathSeparator
        + getInternalLibs(projectdescription, "@", false, null), content[0]);
  }

  @Test
  public void complexProject() {
    ProjectDescription projectdescription = createComplexProject(this._pythonpathxml, 0);
    BuildResult buildresult = execute(projectdescription.getPrimaryProjectname(), "get-python-path");
    String[] content = buildresult.getTargetOutput("get-python-path");
    Assert.assertEquals(1, content.length);
    Assert.assertEquals("${workspacedir}" + File.separator + projectdescription.getPrimaryProjectname()
        + File.pathSeparator + "${workspacedir}" + File.separator + projectdescription.getSecondaryProjectname(),
        content[0]);
  }

  @Test
  public void complexProjectRelative() {
    ProjectDescription projectdescription = createComplexProject(this._pythonpathxml, 0);
    BuildResult buildresult = execute(projectdescription.getPrimaryProjectname(), "get-python-path-relative");
    String[] content = buildresult.getTargetOutput("get-python-path-relative");
    Assert.assertEquals(1, content.length);
    Assert.assertEquals(
        "." + File.pathSeparator + ".." + File.separator + projectdescription.getSecondaryProjectname(), content[0]);
  }

  @Test
  public void complexProjectDirseparator() {
    ProjectDescription projectdescription = createComplexProject(this._pythonpathxml, 0);
    BuildResult buildresult = execute(projectdescription.getPrimaryProjectname(), "get-python-path-dirseparator", "@");
    String[] content = buildresult.getTargetOutput("get-python-path-dirseparator");
    Assert.assertEquals(1, content.length);
    Assert.assertEquals("${workspacedir}@" + projectdescription.getPrimaryProjectname() + File.pathSeparator
        + "${workspacedir}@" + projectdescription.getSecondaryProjectname(), content[0]);
  }

  @Test
  public void complexProjectInternalLibs() {
    ProjectDescription projectdescription = createComplexProject(this._pythonpathxml, KIND_INTERNALLIBRARYPRIMARY
        | KIND_INTERNALLIBRARYSECONDARY);
    BuildResult buildresult = execute(projectdescription.getPrimaryProjectname(), "get-python-path");
    String[] content = buildresult.getTargetOutput("get-python-path");
    Assert.assertEquals(1, content.length);
    String internalprimary = getInternalLibs(projectdescription, null, false, Boolean.TRUE);
    String internalsecondary = getInternalLibs(projectdescription, null, false, Boolean.FALSE);
    Assert.assertEquals("${workspacedir}" + File.separator + projectdescription.getPrimaryProjectname()
        + File.pathSeparator + internalprimary + File.pathSeparator + "${workspacedir}" + File.separator
        + projectdescription.getSecondaryProjectname() + File.pathSeparator + internalsecondary, content[0]);
  }

  @Test
  public void complexProjectRelativeInternalLibs() {
    ProjectDescription projectdescription = createComplexProject(this._pythonpathxml, KIND_INTERNALLIBRARYPRIMARY
        | KIND_INTERNALLIBRARYSECONDARY);
    BuildResult buildresult = execute(projectdescription.getPrimaryProjectname(), "get-python-path-relative");
    String[] content = buildresult.getTargetOutput("get-python-path-relative");
    Assert.assertEquals(1, content.length);
    Assert.assertEquals("." + File.pathSeparator + getInternalLibs(projectdescription, null, true, Boolean.TRUE)
        + File.pathSeparator + ".." + File.separator + projectdescription.getSecondaryProjectname()
        + File.pathSeparator + getInternalLibs(projectdescription, null, true, Boolean.FALSE), content[0]);
  }

  @Test
  public void complexProjectDirseparatorInternalLibs() {
    ProjectDescription projectdescription = createComplexProject(this._pythonpathxml, KIND_INTERNALLIBRARYPRIMARY
        | KIND_INTERNALLIBRARYSECONDARY);
    BuildResult buildresult = execute(projectdescription.getPrimaryProjectname(), "get-python-path-dirseparator", "@");
    String[] content = buildresult.getTargetOutput("get-python-path-dirseparator");
    Assert.assertEquals(1, content.length);
    String internalprimary = getInternalLibs(projectdescription, "@", false, Boolean.TRUE);
    String internalsecondary = getInternalLibs(projectdescription, "@", false, Boolean.FALSE);
    Assert.assertEquals("${workspacedir}@" + projectdescription.getPrimaryProjectname() + File.pathSeparator
        + internalprimary + File.pathSeparator + "${workspacedir}@" + projectdescription.getSecondaryProjectname()
        + File.pathSeparator + internalsecondary, content[0]);
  }

  @Test
  public void cyclicProject() {
    ProjectDescription projectdescription = createCyclicProject(this._pythonpathxml, 0);
    BuildResult buildresult = execute(projectdescription.getPrimaryProjectname(), "get-python-path");
    String[] content = buildresult.getTargetOutput("get-python-path");
    Assert.assertEquals(1, content.length);
    Assert.assertEquals("${workspacedir}" + File.separator + projectdescription.getPrimaryProjectname()
        + File.pathSeparator + "${workspacedir}" + File.separator + projectdescription.getSecondaryProjectname(),
        content[0]);
  }

  @Test
  public void cyclicProjectRelative() {
    ProjectDescription projectdescription = createCyclicProject(this._pythonpathxml, 0);
    BuildResult buildresult = execute(projectdescription.getPrimaryProjectname(), "get-python-path-relative");
    String[] content = buildresult.getTargetOutput("get-python-path-relative");
    Assert.assertEquals(1, content.length);
    Assert.assertEquals(
        "." + File.pathSeparator + ".." + File.separator + projectdescription.getSecondaryProjectname(), content[0]);
  }

  @Test
  public void cyclicProjectDirseparator() {
    ProjectDescription projectdescription = createCyclicProject(this._pythonpathxml, 0);
    BuildResult buildresult = execute(projectdescription.getPrimaryProjectname(), "get-python-path-dirseparator", "@");
    String[] content = buildresult.getTargetOutput("get-python-path-dirseparator");
    Assert.assertEquals(1, content.length);
    Assert.assertEquals("${workspacedir}@" + projectdescription.getPrimaryProjectname() + File.pathSeparator
        + "${workspacedir}@" + projectdescription.getSecondaryProjectname(), content[0]);
  }

} /* ENDCLASS */
