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
package org.ant4eclipse.ant.pydt.ant;

import org.ant4eclipse.ant.pydt.test.AbstractWorkspaceBasedTest;
import org.ant4eclipse.ant.pydt.test.BuildResult;
import org.ant4eclipse.pydt.test.data.ProjectDescription;
import org.apache.tools.ant.BuildException;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.net.URL;

/**
 * Basic test implementation for the ant task: 'getPythonSourcePath'.
 * 
 * <ul>
 * <li>The <i>emptyXXX</i> tests do show the functionality of the task.</li>
 * <li>The <i>complexXXX</i> tests do show that dependencies don't alter the source pathes.</li>
 * <li>The <i>cyclicXXX</i> tests do show that dependencies don't alter the source pathes.</li>
 * </ul>
 * 
 * @author Daniel Kasmeroglu (Daniel.Kasmeroglu@Kasisoft.net)
 */
public class AbstractSourcePathTest extends AbstractWorkspaceBasedTest {

  private URL _sourcepathxml;

  /**
   * Initialises this set of tests.
   */
  public AbstractSourcePathTest(boolean dltk) {
    super(dltk);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  @Before
  public void setup() {
    super.setup();
    this._sourcepathxml = getResource("/org/ant4eclipse/pydt/ant/sourcepath.xml");
  }

  /**
   * {@inheritDoc}
   */
  @Override
  @After
  public void teardown() {
    super.teardown();
    this._sourcepathxml = null;
  }

  @Test
  public void emptyProject() {
    ProjectDescription projectdescription = createEmptyProject(this._sourcepathxml, 0);
    BuildResult buildresult = execute(projectdescription.getPrimaryProjectname(), "get-source-path");
    String[] content = buildresult.getTargetOutput("get-source-path");
    Assert.assertEquals(1, content.length);
    Assert.assertEquals("${workspacedir}" + File.separator + projectdescription.getPrimaryProjectname(), content[0]);
  }

  @Test(expected = BuildException.class)
  public void emptyProjectMultipleFoldersFailure() {
    ProjectDescription projectdescription = createEmptyProject(this._sourcepathxml, KIND_MULTIPLESOURCEFOLDERSPRIMARY);
    execute(projectdescription.getPrimaryProjectname(), "get-source-path");
  }

  @Test
  public void emptyProjectMultipleFolders() {
    ProjectDescription projectdescription = createEmptyProject(this._sourcepathxml, KIND_MULTIPLESOURCEFOLDERSPRIMARY);
    BuildResult buildresult = execute(projectdescription.getPrimaryProjectname(), "get-source-path-multiple-folders");
    String[] content = buildresult.getTargetOutput("get-source-path-multiple-folders");
    Assert.assertEquals(1, content.length);
    Assert.assertEquals("${workspacedir}" + File.separator + projectdescription.getPrimaryProjectname()
        + File.pathSeparator + "${workspacedir}" + projectdescription.getSourceFolders()[0], content[0]);
  }

  @Test
  public void emptyProjectRelative() {
    ProjectDescription projectdescription = createEmptyProject(this._sourcepathxml, 0);
    BuildResult buildresult = execute(projectdescription.getPrimaryProjectname(), "get-source-path-relative");
    String[] content = buildresult.getTargetOutput("get-source-path-relative");
    Assert.assertEquals(1, content.length);
    Assert.assertEquals(".", content[0]);
  }

  @Test(expected = BuildException.class)
  public void emptyProjectMultipleFoldersRelativeFailure() {
    ProjectDescription projectdescription = createEmptyProject(this._sourcepathxml, KIND_MULTIPLESOURCEFOLDERSPRIMARY);
    execute(projectdescription.getPrimaryProjectname(), "get-source-path-relative");
  }

  @Test
  public void emptyProjectMultipleFoldersRelative() {
    ProjectDescription projectdescription = createEmptyProject(this._sourcepathxml, KIND_MULTIPLESOURCEFOLDERSPRIMARY);
    BuildResult buildresult = execute(projectdescription.getPrimaryProjectname(),
        "get-source-path-multiple-folders-relative");
    String[] content = buildresult.getTargetOutput("get-source-path-multiple-folders-relative");
    Assert.assertEquals(1, content.length);
    String expectedpath = projectdescription.getSourceFolders()[0].substring(2 + projectdescription
        .getPrimaryProjectname().length());
    Assert.assertEquals("." + File.pathSeparator + expectedpath, content[0]);
  }

  @Test
  public void emptyProjectDirseparator() {
    ProjectDescription projectdescription = createEmptyProject(this._sourcepathxml, 0);
    BuildResult buildresult = execute(projectdescription.getPrimaryProjectname(), "get-source-path-dirseparator", "@");
    String[] content = buildresult.getTargetOutput("get-source-path-dirseparator");
    Assert.assertEquals(1, content.length);
    Assert.assertEquals("${workspacedir}@" + projectdescription.getPrimaryProjectname(), content[0]);
  }

  @Test(expected = BuildException.class)
  public void emptyProjectMultipleFoldersFailureDirseparator() {
    ProjectDescription projectdescription = createEmptyProject(this._sourcepathxml, KIND_MULTIPLESOURCEFOLDERSPRIMARY);
    execute(projectdescription.getPrimaryProjectname(), "get-source-path-dirseparator", "@");
  }

  @Test
  public void emptyProjectMultipleFoldersDirseparator() {
    ProjectDescription projectdescription = createEmptyProject(this._sourcepathxml, KIND_MULTIPLESOURCEFOLDERSPRIMARY);
    BuildResult buildresult = execute(projectdescription.getPrimaryProjectname(),
        "get-source-path-multiple-folders-dirseparator", "@");
    String[] content = buildresult.getTargetOutput("get-source-path-multiple-folders-dirseparator");
    Assert.assertEquals(1, content.length);
    Assert.assertEquals("${workspacedir}@" + projectdescription.getPrimaryProjectname() + File.pathSeparator
        + "${workspacedir}" + projectdescription.getSourceFolders("@")[0], content[0]);
  }

  @Test
  public void complexProject() {
    ProjectDescription projectdescription = createComplexProject(this._sourcepathxml, 0);
    BuildResult buildresult = execute(projectdescription.getPrimaryProjectname(), "get-source-path");
    String[] content = buildresult.getTargetOutput("get-source-path");
    Assert.assertEquals(1, content.length);
    Assert.assertEquals("${workspacedir}" + File.separator + projectdescription.getPrimaryProjectname(), content[0]);
  }

  @Test(expected = BuildException.class)
  public void complexProjectMultipleFoldersFailure() {
    ProjectDescription projectdescription = createComplexProject(this._sourcepathxml, KIND_MULTIPLESOURCEFOLDERSPRIMARY);
    execute(projectdescription.getPrimaryProjectname(), "get-source-path");
  }

  @Test(expected = BuildException.class)
  public void complexProjectMultipleFoldersBothFailure() {
    ProjectDescription projectdescription = createComplexProject(this._sourcepathxml, KIND_MULTIPLESOURCEFOLDERSPRIMARY
        | KIND_MULTIPLESOURCEFOLDERSSECONDARY);
    execute(projectdescription.getPrimaryProjectname(), "get-source-path");
  }

  @Test
  public void complexProjectMultipleFolders() {
    ProjectDescription projectdescription = createComplexProject(this._sourcepathxml, KIND_MULTIPLESOURCEFOLDERSPRIMARY);
    BuildResult buildresult = execute(projectdescription.getPrimaryProjectname(), "get-source-path-multiple-folders");
    String[] content = buildresult.getTargetOutput("get-source-path-multiple-folders");
    Assert.assertEquals(1, content.length);
    Assert.assertEquals("${workspacedir}" + File.separator + projectdescription.getPrimaryProjectname()
        + File.pathSeparator + "${workspacedir}" + projectdescription.getSourceFolders()[0], content[0]);
  }

  @Test
  public void complexProjectMultipleFoldersBoth() {
    ProjectDescription projectdescription = createComplexProject(this._sourcepathxml, KIND_MULTIPLESOURCEFOLDERSPRIMARY
        | KIND_MULTIPLESOURCEFOLDERSSECONDARY);
    BuildResult buildresult = execute(projectdescription.getPrimaryProjectname(), "get-source-path-multiple-folders");
    String[] content = buildresult.getTargetOutput("get-source-path-multiple-folders");
    Assert.assertEquals(1, content.length);
    Assert.assertEquals("${workspacedir}" + File.separator + projectdescription.getPrimaryProjectname()
        + File.pathSeparator + "${workspacedir}" + projectdescription.getSourceFolders()[0], content[0]);
  }

  @Test
  public void complexProjectRelative() {
    ProjectDescription projectdescription = createComplexProject(this._sourcepathxml, 0);
    BuildResult buildresult = execute(projectdescription.getPrimaryProjectname(), "get-source-path-relative");
    String[] content = buildresult.getTargetOutput("get-source-path-relative");
    Assert.assertEquals(1, content.length);
    Assert.assertEquals(".", content[0]);
  }

  @Test(expected = BuildException.class)
  public void complexProjectMultipleFoldersRelativeFailure() {
    ProjectDescription projectdescription = createComplexProject(this._sourcepathxml, KIND_MULTIPLESOURCEFOLDERSPRIMARY);
    execute(projectdescription.getPrimaryProjectname(), "get-source-path-relative");
  }

  @Test(expected = BuildException.class)
  public void complexProjectMultipleFoldersRelativeBothFailure() {
    ProjectDescription projectdescription = createComplexProject(this._sourcepathxml, KIND_MULTIPLESOURCEFOLDERSPRIMARY
        | KIND_MULTIPLESOURCEFOLDERSSECONDARY);
    execute(projectdescription.getPrimaryProjectname(), "get-source-path-relative");
  }

  @Test
  public void complexProjectMultipleFoldersRelative() {
    ProjectDescription projectdescription = createComplexProject(this._sourcepathxml, KIND_MULTIPLESOURCEFOLDERSPRIMARY);
    BuildResult buildresult = execute(projectdescription.getPrimaryProjectname(),
        "get-source-path-multiple-folders-relative");
    String[] content = buildresult.getTargetOutput("get-source-path-multiple-folders-relative");
    Assert.assertEquals(1, content.length);
    String expectedpath = projectdescription.getSourceFolders()[0].substring(2 + projectdescription
        .getPrimaryProjectname().length());
    Assert.assertEquals("." + File.pathSeparator + expectedpath, content[0]);
  }

  @Test
  public void complexProjectMultipleFoldersRelativeBoth() {
    ProjectDescription projectdescription = createComplexProject(this._sourcepathxml, KIND_MULTIPLESOURCEFOLDERSPRIMARY
        | KIND_MULTIPLESOURCEFOLDERSSECONDARY);
    BuildResult buildresult = execute(projectdescription.getPrimaryProjectname(),
        "get-source-path-multiple-folders-relative");
    String[] content = buildresult.getTargetOutput("get-source-path-multiple-folders-relative");
    Assert.assertEquals(1, content.length);
    String expectedpath = projectdescription.getSourceFolders()[0].substring(2 + projectdescription
        .getPrimaryProjectname().length());
    Assert.assertEquals("." + File.pathSeparator + expectedpath, content[0]);
  }

  @Test
  public void complexProjectDirseparator() {
    ProjectDescription projectdescription = createComplexProject(this._sourcepathxml, 0);
    BuildResult buildresult = execute(projectdescription.getPrimaryProjectname(), "get-source-path-dirseparator", "@");
    String[] content = buildresult.getTargetOutput("get-source-path-dirseparator");
    Assert.assertEquals(1, content.length);
    Assert.assertEquals("${workspacedir}@" + projectdescription.getPrimaryProjectname(), content[0]);
  }

  @Test(expected = BuildException.class)
  public void complexProjectMultipleFoldersFailureDirseparator() {
    ProjectDescription projectdescription = createComplexProject(this._sourcepathxml, KIND_MULTIPLESOURCEFOLDERSPRIMARY);
    execute(projectdescription.getPrimaryProjectname(), "get-source-path-dirseparator", "@");
  }

  @Test(expected = BuildException.class)
  public void complexProjectMultipleFoldersBothFailureDirseparator() {
    ProjectDescription projectdescription = createComplexProject(this._sourcepathxml, KIND_MULTIPLESOURCEFOLDERSPRIMARY
        | KIND_MULTIPLESOURCEFOLDERSSECONDARY);
    execute(projectdescription.getPrimaryProjectname(), "get-source-path-dirseparator", "@");
  }

  @Test
  public void complexProjectMultipleFoldersDirseparator() {
    ProjectDescription projectdescription = createComplexProject(this._sourcepathxml, KIND_MULTIPLESOURCEFOLDERSPRIMARY);
    BuildResult buildresult = execute(projectdescription.getPrimaryProjectname(),
        "get-source-path-multiple-folders-dirseparator", "@");
    String[] content = buildresult.getTargetOutput("get-source-path-multiple-folders-dirseparator");
    Assert.assertEquals(1, content.length);
    Assert.assertEquals("${workspacedir}@" + projectdescription.getPrimaryProjectname() + File.pathSeparator
        + "${workspacedir}" + projectdescription.getSourceFolders("@")[0], content[0]);
  }

  @Test
  public void complexProjectMultipleFoldersBothDirseparator() {
    ProjectDescription projectdescription = createComplexProject(this._sourcepathxml, KIND_MULTIPLESOURCEFOLDERSPRIMARY
        | KIND_MULTIPLESOURCEFOLDERSSECONDARY);
    BuildResult buildresult = execute(projectdescription.getPrimaryProjectname(),
        "get-source-path-multiple-folders-dirseparator", "@");
    String[] content = buildresult.getTargetOutput("get-source-path-multiple-folders-dirseparator");
    Assert.assertEquals(1, content.length);
    Assert.assertEquals("${workspacedir}@" + projectdescription.getPrimaryProjectname() + File.pathSeparator
        + "${workspacedir}" + projectdescription.getSourceFolders("@")[0], content[0]);
  }

  @Test
  public void cyclicProject() {
    ProjectDescription projectdescription = createCyclicProject(this._sourcepathxml, 0);
    BuildResult buildresult = execute(projectdescription.getPrimaryProjectname(), "get-source-path");
    String[] content = buildresult.getTargetOutput("get-source-path");
    Assert.assertEquals(1, content.length);
    Assert.assertEquals("${workspacedir}" + File.separator + projectdescription.getPrimaryProjectname(), content[0]);
  }

  @Test(expected = BuildException.class)
  public void cyclicProjectMultipleFoldersFailure() {
    ProjectDescription projectdescription = createCyclicProject(this._sourcepathxml, KIND_MULTIPLESOURCEFOLDERSPRIMARY);
    execute(projectdescription.getPrimaryProjectname(), "get-source-path");
  }

  @Test(expected = BuildException.class)
  public void cyclicProjectMultipleFoldersBothFailure() {
    ProjectDescription projectdescription = createCyclicProject(this._sourcepathxml, KIND_MULTIPLESOURCEFOLDERSPRIMARY
        | KIND_MULTIPLESOURCEFOLDERSSECONDARY);
    execute(projectdescription.getPrimaryProjectname(), "get-source-path");
  }

  @Test
  public void cyclicProjectMultipleFolders() {
    ProjectDescription projectdescription = createCyclicProject(this._sourcepathxml, KIND_MULTIPLESOURCEFOLDERSPRIMARY);
    BuildResult buildresult = execute(projectdescription.getPrimaryProjectname(), "get-source-path-multiple-folders");
    String[] content = buildresult.getTargetOutput("get-source-path-multiple-folders");
    Assert.assertEquals(1, content.length);
    Assert.assertEquals("${workspacedir}" + File.separator + projectdescription.getPrimaryProjectname()
        + File.pathSeparator + "${workspacedir}" + projectdescription.getSourceFolders()[0], content[0]);
  }

  @Test
  public void cyclicProjectMultipleFoldersBoth() {
    ProjectDescription projectdescription = createCyclicProject(this._sourcepathxml, KIND_MULTIPLESOURCEFOLDERSPRIMARY
        | KIND_MULTIPLESOURCEFOLDERSSECONDARY);
    BuildResult buildresult = execute(projectdescription.getPrimaryProjectname(), "get-source-path-multiple-folders");
    String[] content = buildresult.getTargetOutput("get-source-path-multiple-folders");
    Assert.assertEquals(1, content.length);
    Assert.assertEquals("${workspacedir}" + File.separator + projectdescription.getPrimaryProjectname()
        + File.pathSeparator + "${workspacedir}" + projectdescription.getSourceFolders()[0], content[0]);
  }

  @Test
  public void cyclicProjectRelative() {
    ProjectDescription projectdescription = createCyclicProject(this._sourcepathxml, 0);
    BuildResult buildresult = execute(projectdescription.getPrimaryProjectname(), "get-source-path-relative");
    String[] content = buildresult.getTargetOutput("get-source-path-relative");
    Assert.assertEquals(1, content.length);
    Assert.assertEquals(".", content[0]);
  }

  @Test(expected = BuildException.class)
  public void cyclicProjectMultipleFoldersRelativeFailure() {
    ProjectDescription projectdescription = createCyclicProject(this._sourcepathxml, KIND_MULTIPLESOURCEFOLDERSPRIMARY);
    execute(projectdescription.getPrimaryProjectname(), "get-source-path-relative");
  }

  @Test(expected = BuildException.class)
  public void cyclicProjectMultipleFoldersRelativeBothFailure() {
    ProjectDescription projectdescription = createCyclicProject(this._sourcepathxml, KIND_MULTIPLESOURCEFOLDERSPRIMARY
        | KIND_MULTIPLESOURCEFOLDERSSECONDARY);
    execute(projectdescription.getPrimaryProjectname(), "get-source-path-relative");
  }

  @Test
  public void cyclicProjectMultipleFoldersRelative() {
    ProjectDescription projectdescription = createCyclicProject(this._sourcepathxml, KIND_MULTIPLESOURCEFOLDERSPRIMARY);
    BuildResult buildresult = execute(projectdescription.getPrimaryProjectname(),
        "get-source-path-multiple-folders-relative");
    String[] content = buildresult.getTargetOutput("get-source-path-multiple-folders-relative");
    Assert.assertEquals(1, content.length);
    String expectedpath = projectdescription.getSourceFolders()[0].substring(2 + projectdescription
        .getPrimaryProjectname().length());
    Assert.assertEquals("." + File.pathSeparator + expectedpath, content[0]);
  }

  @Test
  public void cyclicProjectMultipleFoldersRelativeBoth() {
    ProjectDescription projectdescription = createCyclicProject(this._sourcepathxml, KIND_MULTIPLESOURCEFOLDERSPRIMARY
        | KIND_MULTIPLESOURCEFOLDERSSECONDARY);
    BuildResult buildresult = execute(projectdescription.getPrimaryProjectname(),
        "get-source-path-multiple-folders-relative");
    String[] content = buildresult.getTargetOutput("get-source-path-multiple-folders-relative");
    Assert.assertEquals(1, content.length);
    String expectedpath = projectdescription.getSourceFolders()[0].substring(2 + projectdescription
        .getPrimaryProjectname().length());
    Assert.assertEquals("." + File.pathSeparator + expectedpath, content[0]);
  }

  @Test
  public void cyclicProjectDirseparator() {
    ProjectDescription projectdescription = createCyclicProject(this._sourcepathxml, 0);
    BuildResult buildresult = execute(projectdescription.getPrimaryProjectname(), "get-source-path-dirseparator", "@");
    String[] content = buildresult.getTargetOutput("get-source-path-dirseparator");
    Assert.assertEquals(1, content.length);
    Assert.assertEquals("${workspacedir}@" + projectdescription.getPrimaryProjectname(), content[0]);
  }

  @Test(expected = BuildException.class)
  public void cyclicProjectMultipleFoldersFailureDirseparator() {
    ProjectDescription projectdescription = createCyclicProject(this._sourcepathxml, KIND_MULTIPLESOURCEFOLDERSPRIMARY);
    execute(projectdescription.getPrimaryProjectname(), "get-source-path-dirseparator", "@");
  }

  @Test(expected = BuildException.class)
  public void cyclicProjectMultipleFoldersBothFailureDirseparator() {
    ProjectDescription projectdescription = createCyclicProject(this._sourcepathxml, KIND_MULTIPLESOURCEFOLDERSPRIMARY
        | KIND_MULTIPLESOURCEFOLDERSSECONDARY);
    execute(projectdescription.getPrimaryProjectname(), "get-source-path-dirseparator", "@");
  }

  @Test
  public void cyclicProjectMultipleFoldersDirseparator() {
    ProjectDescription projectdescription = createCyclicProject(this._sourcepathxml, KIND_MULTIPLESOURCEFOLDERSPRIMARY);
    BuildResult buildresult = execute(projectdescription.getPrimaryProjectname(),
        "get-source-path-multiple-folders-dirseparator", "@");
    String[] content = buildresult.getTargetOutput("get-source-path-multiple-folders-dirseparator");
    Assert.assertEquals(1, content.length);
    Assert.assertEquals("${workspacedir}@" + projectdescription.getPrimaryProjectname() + File.pathSeparator
        + "${workspacedir}" + projectdescription.getSourceFolders("@")[0], content[0]);
  }

  @Test
  public void cyclicProjectMultipleFoldersBothDirseparator() {
    ProjectDescription projectdescription = createCyclicProject(this._sourcepathxml, KIND_MULTIPLESOURCEFOLDERSPRIMARY
        | KIND_MULTIPLESOURCEFOLDERSSECONDARY);
    BuildResult buildresult = execute(projectdescription.getPrimaryProjectname(),
        "get-source-path-multiple-folders-dirseparator", "@");
    String[] content = buildresult.getTargetOutput("get-source-path-multiple-folders-dirseparator");
    Assert.assertEquals(1, content.length);
    Assert.assertEquals("${workspacedir}@" + projectdescription.getPrimaryProjectname() + File.pathSeparator
        + "${workspacedir}" + projectdescription.getSourceFolders("@")[0], content[0]);
  }

} /* ENDCLASS */
