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
  public AbstractSourcePathTest(final boolean dltk) {
    super(dltk);
  }

  /**
   * {@inheritDoc}
   */
  @Before
  public void setup() {
    super.setup();
    _sourcepathxml = getResource("/org/ant4eclipse/pydt/ant/sourcepath.xml");
  }

  /**
   * {@inheritDoc}
   */
  @After
  public void teardown() {
    super.teardown();
    _sourcepathxml = null;
  }

  @Test
  public void emptyProject() {
    final ProjectDescription projectdescription = createEmptyProject(_sourcepathxml, 0);
    final BuildResult buildresult = execute(projectdescription.getPrimaryProjectname(), "get-source-path");
    final String[] content = buildresult.getTargetOutput("get-source-path");
    Assert.assertEquals(1, content.length);
    Assert.assertEquals("${workspacedir}" + File.separator + projectdescription.getPrimaryProjectname(), content[0]);
  }

  @Test(expected = BuildException.class)
  public void emptyProjectMultipleFoldersFailure() {
    final ProjectDescription projectdescription = createEmptyProject(_sourcepathxml, KIND_MULTIPLESOURCEFOLDERSPRIMARY);
    execute(projectdescription.getPrimaryProjectname(), "get-source-path");
  }

  @Test
  public void emptyProjectMultipleFolders() {
    final ProjectDescription projectdescription = createEmptyProject(_sourcepathxml, KIND_MULTIPLESOURCEFOLDERSPRIMARY);
    final BuildResult buildresult = execute(projectdescription.getPrimaryProjectname(),
        "get-source-path-multiple-folders");
    final String[] content = buildresult.getTargetOutput("get-source-path-multiple-folders");
    Assert.assertEquals(1, content.length);
    Assert.assertEquals("${workspacedir}" + File.separator + projectdescription.getPrimaryProjectname()
        + File.pathSeparator + "${workspacedir}" + File.separator + projectdescription.getPrimaryProjectname()
        + File.separator + NAME_GENERATEDSOURCE, content[0]);
  }

  @Test
  public void emptyProjectRelative() {
    final ProjectDescription projectdescription = createEmptyProject(_sourcepathxml, 0);
    final BuildResult buildresult = execute(projectdescription.getPrimaryProjectname(), "get-source-path-relative");
    final String[] content = buildresult.getTargetOutput("get-source-path-relative");
    Assert.assertEquals(1, content.length);
    Assert.assertEquals(".", content[0]);
  }

  @Test(expected = BuildException.class)
  public void emptyProjectMultipleFoldersRelativeFailure() {
    final ProjectDescription projectdescription = createEmptyProject(_sourcepathxml, KIND_MULTIPLESOURCEFOLDERSPRIMARY);
    execute(projectdescription.getPrimaryProjectname(), "get-source-path-relative");
  }

  @Test
  public void emptyProjectMultipleFoldersRelative() {
    final ProjectDescription projectdescription = createEmptyProject(_sourcepathxml, KIND_MULTIPLESOURCEFOLDERSPRIMARY);
    final BuildResult buildresult = execute(projectdescription.getPrimaryProjectname(),
        "get-source-path-multiple-folders-relative");
    final String[] content = buildresult.getTargetOutput("get-source-path-multiple-folders-relative");
    Assert.assertEquals(1, content.length);
    Assert.assertEquals("." + File.pathSeparator + NAME_GENERATEDSOURCE, content[0]);
  }

  @Test
  public void emptyProjectDirseparator() {
    final ProjectDescription projectdescription = createEmptyProject(_sourcepathxml, 0);
    final BuildResult buildresult = execute(projectdescription.getPrimaryProjectname(), "get-source-path-dirseparator",
        "@");
    final String[] content = buildresult.getTargetOutput("get-source-path-dirseparator");
    Assert.assertEquals(1, content.length);
    Assert.assertEquals("${workspacedir}@" + projectdescription.getPrimaryProjectname(), content[0]);
  }

  @Test(expected = BuildException.class)
  public void emptyProjectMultipleFoldersFailureDirseparator() {
    final ProjectDescription projectdescription = createEmptyProject(_sourcepathxml, KIND_MULTIPLESOURCEFOLDERSPRIMARY);
    execute(projectdescription.getPrimaryProjectname(), "get-source-path-dirseparator", "@");
  }

  @Test
  public void emptyProjectMultipleFoldersDirseparator() {
    final ProjectDescription projectdescription = createEmptyProject(_sourcepathxml, KIND_MULTIPLESOURCEFOLDERSPRIMARY);
    final BuildResult buildresult = execute(projectdescription.getPrimaryProjectname(),
        "get-source-path-multiple-folders-dirseparator", "@");
    final String[] content = buildresult.getTargetOutput("get-source-path-multiple-folders-dirseparator");
    Assert.assertEquals(1, content.length);
    Assert.assertEquals("${workspacedir}@" + projectdescription.getPrimaryProjectname() + File.pathSeparator
        + "${workspacedir}@" + projectdescription.getPrimaryProjectname() + "@" + NAME_GENERATEDSOURCE, content[0]);
  }

  @Test
  public void complexProject() {
    final ProjectDescription projectdescription = createComplexProject(_sourcepathxml, 0);
    final BuildResult buildresult = execute(projectdescription.getPrimaryProjectname(), "get-source-path");
    final String[] content = buildresult.getTargetOutput("get-source-path");
    Assert.assertEquals(1, content.length);
    Assert.assertEquals("${workspacedir}" + File.separator + projectdescription.getPrimaryProjectname(), content[0]);
  }

  @Test(expected = BuildException.class)
  public void complexProjectMultipleFoldersFailure() {
    final ProjectDescription projectdescription = createComplexProject(_sourcepathxml,
        KIND_MULTIPLESOURCEFOLDERSPRIMARY);
    execute(projectdescription.getPrimaryProjectname(), "get-source-path");
  }

  @Test(expected = BuildException.class)
  public void complexProjectMultipleFoldersBothFailure() {
    final ProjectDescription projectdescription = createComplexProject(_sourcepathxml,
        KIND_MULTIPLESOURCEFOLDERSPRIMARY | KIND_MULTIPLESOURCEFOLDERSSECONDARY);
    execute(projectdescription.getPrimaryProjectname(), "get-source-path");
  }

  @Test
  public void complexProjectMultipleFolders() {
    final ProjectDescription projectdescription = createComplexProject(_sourcepathxml,
        KIND_MULTIPLESOURCEFOLDERSPRIMARY);
    final BuildResult buildresult = execute(projectdescription.getPrimaryProjectname(),
        "get-source-path-multiple-folders");
    final String[] content = buildresult.getTargetOutput("get-source-path-multiple-folders");
    Assert.assertEquals(1, content.length);
    Assert.assertEquals("${workspacedir}" + File.separator + projectdescription.getPrimaryProjectname()
        + File.pathSeparator + "${workspacedir}" + File.separator + projectdescription.getPrimaryProjectname()
        + File.separator + NAME_GENERATEDSOURCE, content[0]);
  }

  @Test
  public void complexProjectMultipleFoldersBoth() {
    final ProjectDescription projectdescription = createComplexProject(_sourcepathxml,
        KIND_MULTIPLESOURCEFOLDERSPRIMARY | KIND_MULTIPLESOURCEFOLDERSSECONDARY);
    final BuildResult buildresult = execute(projectdescription.getPrimaryProjectname(),
        "get-source-path-multiple-folders");
    final String[] content = buildresult.getTargetOutput("get-source-path-multiple-folders");
    Assert.assertEquals(1, content.length);
    Assert.assertEquals("${workspacedir}" + File.separator + projectdescription.getPrimaryProjectname()
        + File.pathSeparator + "${workspacedir}" + File.separator + projectdescription.getPrimaryProjectname()
        + File.separator + NAME_GENERATEDSOURCE, content[0]);
  }

  @Test
  public void complexProjectRelative() {
    final ProjectDescription projectdescription = createComplexProject(_sourcepathxml, 0);
    final BuildResult buildresult = execute(projectdescription.getPrimaryProjectname(), "get-source-path-relative");
    final String[] content = buildresult.getTargetOutput("get-source-path-relative");
    Assert.assertEquals(1, content.length);
    Assert.assertEquals(".", content[0]);
  }

  @Test(expected = BuildException.class)
  public void complexProjectMultipleFoldersRelativeFailure() {
    final ProjectDescription projectdescription = createComplexProject(_sourcepathxml,
        KIND_MULTIPLESOURCEFOLDERSPRIMARY);
    execute(projectdescription.getPrimaryProjectname(), "get-source-path-relative");
  }

  @Test(expected = BuildException.class)
  public void complexProjectMultipleFoldersRelativeBothFailure() {
    final ProjectDescription projectdescription = createComplexProject(_sourcepathxml,
        KIND_MULTIPLESOURCEFOLDERSPRIMARY | KIND_MULTIPLESOURCEFOLDERSSECONDARY);
    execute(projectdescription.getPrimaryProjectname(), "get-source-path-relative");
  }

  @Test
  public void complexProjectMultipleFoldersRelative() {
    final ProjectDescription projectdescription = createComplexProject(_sourcepathxml,
        KIND_MULTIPLESOURCEFOLDERSPRIMARY);
    final BuildResult buildresult = execute(projectdescription.getPrimaryProjectname(),
        "get-source-path-multiple-folders-relative");
    final String[] content = buildresult.getTargetOutput("get-source-path-multiple-folders-relative");
    Assert.assertEquals(1, content.length);
    Assert.assertEquals("." + File.pathSeparator + NAME_GENERATEDSOURCE, content[0]);
  }

  @Test
  public void complexProjectMultipleFoldersRelativeBoth() {
    final ProjectDescription projectdescription = createComplexProject(_sourcepathxml,
        KIND_MULTIPLESOURCEFOLDERSPRIMARY | KIND_MULTIPLESOURCEFOLDERSSECONDARY);
    final BuildResult buildresult = execute(projectdescription.getPrimaryProjectname(),
        "get-source-path-multiple-folders-relative");
    final String[] content = buildresult.getTargetOutput("get-source-path-multiple-folders-relative");
    Assert.assertEquals(1, content.length);
    Assert.assertEquals("." + File.pathSeparator + NAME_GENERATEDSOURCE, content[0]);
  }

  @Test
  public void complexProjectDirseparator() {
    final ProjectDescription projectdescription = createComplexProject(_sourcepathxml, 0);
    final BuildResult buildresult = execute(projectdescription.getPrimaryProjectname(), "get-source-path-dirseparator",
        "@");
    final String[] content = buildresult.getTargetOutput("get-source-path-dirseparator");
    Assert.assertEquals(1, content.length);
    Assert.assertEquals("${workspacedir}@" + projectdescription.getPrimaryProjectname(), content[0]);
  }

  @Test(expected = BuildException.class)
  public void complexProjectMultipleFoldersFailureDirseparator() {
    final ProjectDescription projectdescription = createComplexProject(_sourcepathxml,
        KIND_MULTIPLESOURCEFOLDERSPRIMARY);
    execute(projectdescription.getPrimaryProjectname(), "get-source-path-dirseparator", "@");
  }

  @Test(expected = BuildException.class)
  public void complexProjectMultipleFoldersBothFailureDirseparator() {
    final ProjectDescription projectdescription = createComplexProject(_sourcepathxml,
        KIND_MULTIPLESOURCEFOLDERSPRIMARY | KIND_MULTIPLESOURCEFOLDERSSECONDARY);
    execute(projectdescription.getPrimaryProjectname(), "get-source-path-dirseparator", "@");
  }

  @Test
  public void complexProjectMultipleFoldersDirseparator() {
    final ProjectDescription projectdescription = createComplexProject(_sourcepathxml,
        KIND_MULTIPLESOURCEFOLDERSPRIMARY);
    final BuildResult buildresult = execute(projectdescription.getPrimaryProjectname(),
        "get-source-path-multiple-folders-dirseparator", "@");
    final String[] content = buildresult.getTargetOutput("get-source-path-multiple-folders-dirseparator");
    Assert.assertEquals(1, content.length);
    Assert.assertEquals("${workspacedir}@" + projectdescription.getPrimaryProjectname() + File.pathSeparator
        + "${workspacedir}@" + projectdescription.getPrimaryProjectname() + "@" + NAME_GENERATEDSOURCE, content[0]);
  }

  @Test
  public void complexProjectMultipleFoldersBothDirseparator() {
    final ProjectDescription projectdescription = createComplexProject(_sourcepathxml,
        KIND_MULTIPLESOURCEFOLDERSPRIMARY | KIND_MULTIPLESOURCEFOLDERSSECONDARY);
    final BuildResult buildresult = execute(projectdescription.getPrimaryProjectname(),
        "get-source-path-multiple-folders-dirseparator", "@");
    final String[] content = buildresult.getTargetOutput("get-source-path-multiple-folders-dirseparator");
    Assert.assertEquals(1, content.length);
    Assert.assertEquals("${workspacedir}@" + projectdescription.getPrimaryProjectname() + File.pathSeparator
        + "${workspacedir}@" + projectdescription.getPrimaryProjectname() + "@" + NAME_GENERATEDSOURCE, content[0]);
  }

  @Test
  public void cyclicProject() {
    final ProjectDescription projectdescription = createCyclicProject(_sourcepathxml, 0);
    final BuildResult buildresult = execute(projectdescription.getPrimaryProjectname(), "get-source-path");
    final String[] content = buildresult.getTargetOutput("get-source-path");
    Assert.assertEquals(1, content.length);
    Assert.assertEquals("${workspacedir}" + File.separator + projectdescription.getPrimaryProjectname(), content[0]);
  }

  @Test(expected = BuildException.class)
  public void cyclicProjectMultipleFoldersFailure() {
    final ProjectDescription projectdescription = createCyclicProject(_sourcepathxml, KIND_MULTIPLESOURCEFOLDERSPRIMARY);
    execute(projectdescription.getPrimaryProjectname(), "get-source-path");
  }

  @Test(expected = BuildException.class)
  public void cyclicProjectMultipleFoldersBothFailure() {
    final ProjectDescription projectdescription = createCyclicProject(_sourcepathxml, KIND_MULTIPLESOURCEFOLDERSPRIMARY
        | KIND_MULTIPLESOURCEFOLDERSSECONDARY);
    execute(projectdescription.getPrimaryProjectname(), "get-source-path");
  }

  @Test
  public void cyclicProjectMultipleFolders() {
    final ProjectDescription projectdescription = createCyclicProject(_sourcepathxml, KIND_MULTIPLESOURCEFOLDERSPRIMARY);
    final BuildResult buildresult = execute(projectdescription.getPrimaryProjectname(),
        "get-source-path-multiple-folders");
    final String[] content = buildresult.getTargetOutput("get-source-path-multiple-folders");
    Assert.assertEquals(1, content.length);
    Assert.assertEquals("${workspacedir}" + File.separator + projectdescription.getPrimaryProjectname()
        + File.pathSeparator + "${workspacedir}" + File.separator + projectdescription.getPrimaryProjectname()
        + File.separator + NAME_GENERATEDSOURCE, content[0]);
  }

  @Test
  public void cyclicProjectMultipleFoldersBoth() {
    final ProjectDescription projectdescription = createCyclicProject(_sourcepathxml, KIND_MULTIPLESOURCEFOLDERSPRIMARY
        | KIND_MULTIPLESOURCEFOLDERSSECONDARY);
    final BuildResult buildresult = execute(projectdescription.getPrimaryProjectname(),
        "get-source-path-multiple-folders");
    final String[] content = buildresult.getTargetOutput("get-source-path-multiple-folders");
    Assert.assertEquals(1, content.length);
    Assert.assertEquals("${workspacedir}" + File.separator + projectdescription.getPrimaryProjectname()
        + File.pathSeparator + "${workspacedir}" + File.separator + projectdescription.getPrimaryProjectname()
        + File.separator + NAME_GENERATEDSOURCE, content[0]);
  }

  @Test
  public void cyclicProjectRelative() {
    final ProjectDescription projectdescription = createCyclicProject(_sourcepathxml, 0);
    final BuildResult buildresult = execute(projectdescription.getPrimaryProjectname(), "get-source-path-relative");
    final String[] content = buildresult.getTargetOutput("get-source-path-relative");
    Assert.assertEquals(1, content.length);
    Assert.assertEquals(".", content[0]);
  }

  @Test(expected = BuildException.class)
  public void cyclicProjectMultipleFoldersRelativeFailure() {
    final ProjectDescription projectdescription = createCyclicProject(_sourcepathxml, KIND_MULTIPLESOURCEFOLDERSPRIMARY);
    execute(projectdescription.getPrimaryProjectname(), "get-source-path-relative");
  }

  @Test(expected = BuildException.class)
  public void cyclicProjectMultipleFoldersRelativeBothFailure() {
    final ProjectDescription projectdescription = createCyclicProject(_sourcepathxml, KIND_MULTIPLESOURCEFOLDERSPRIMARY
        | KIND_MULTIPLESOURCEFOLDERSSECONDARY);
    execute(projectdescription.getPrimaryProjectname(), "get-source-path-relative");
  }

  @Test
  public void cyclicProjectMultipleFoldersRelative() {
    final ProjectDescription projectdescription = createCyclicProject(_sourcepathxml, KIND_MULTIPLESOURCEFOLDERSPRIMARY);
    final BuildResult buildresult = execute(projectdescription.getPrimaryProjectname(),
        "get-source-path-multiple-folders-relative");
    final String[] content = buildresult.getTargetOutput("get-source-path-multiple-folders-relative");
    Assert.assertEquals(1, content.length);
    Assert.assertEquals("." + File.pathSeparator + NAME_GENERATEDSOURCE, content[0]);
  }

  @Test
  public void cyclicProjectMultipleFoldersRelativeBoth() {
    final ProjectDescription projectdescription = createCyclicProject(_sourcepathxml, KIND_MULTIPLESOURCEFOLDERSPRIMARY
        | KIND_MULTIPLESOURCEFOLDERSSECONDARY);
    final BuildResult buildresult = execute(projectdescription.getPrimaryProjectname(),
        "get-source-path-multiple-folders-relative");
    final String[] content = buildresult.getTargetOutput("get-source-path-multiple-folders-relative");
    Assert.assertEquals(1, content.length);
    Assert.assertEquals("." + File.pathSeparator + NAME_GENERATEDSOURCE, content[0]);
  }

  @Test
  public void cyclicProjectDirseparator() {
    final ProjectDescription projectdescription = createCyclicProject(_sourcepathxml, 0);
    final BuildResult buildresult = execute(projectdescription.getPrimaryProjectname(), "get-source-path-dirseparator",
        "@");
    final String[] content = buildresult.getTargetOutput("get-source-path-dirseparator");
    Assert.assertEquals(1, content.length);
    Assert.assertEquals("${workspacedir}@" + projectdescription.getPrimaryProjectname(), content[0]);
  }

  @Test(expected = BuildException.class)
  public void cyclicProjectMultipleFoldersFailureDirseparator() {
    final ProjectDescription projectdescription = createCyclicProject(_sourcepathxml, KIND_MULTIPLESOURCEFOLDERSPRIMARY);
    execute(projectdescription.getPrimaryProjectname(), "get-source-path-dirseparator", "@");
  }

  @Test(expected = BuildException.class)
  public void cyclicProjectMultipleFoldersBothFailureDirseparator() {
    final ProjectDescription projectdescription = createCyclicProject(_sourcepathxml, KIND_MULTIPLESOURCEFOLDERSPRIMARY
        | KIND_MULTIPLESOURCEFOLDERSSECONDARY);
    execute(projectdescription.getPrimaryProjectname(), "get-source-path-dirseparator", "@");
  }

  @Test
  public void cyclicProjectMultipleFoldersDirseparator() {
    final ProjectDescription projectdescription = createCyclicProject(_sourcepathxml, KIND_MULTIPLESOURCEFOLDERSPRIMARY);
    final BuildResult buildresult = execute(projectdescription.getPrimaryProjectname(),
        "get-source-path-multiple-folders-dirseparator", "@");
    final String[] content = buildresult.getTargetOutput("get-source-path-multiple-folders-dirseparator");
    Assert.assertEquals(1, content.length);
    Assert.assertEquals("${workspacedir}@" + projectdescription.getPrimaryProjectname() + File.pathSeparator
        + "${workspacedir}@" + projectdescription.getPrimaryProjectname() + "@" + NAME_GENERATEDSOURCE, content[0]);
  }

  @Test
  public void cyclicProjectMultipleFoldersBothDirseparator() {
    final ProjectDescription projectdescription = createCyclicProject(_sourcepathxml, KIND_MULTIPLESOURCEFOLDERSPRIMARY
        | KIND_MULTIPLESOURCEFOLDERSSECONDARY);
    final BuildResult buildresult = execute(projectdescription.getPrimaryProjectname(),
        "get-source-path-multiple-folders-dirseparator", "@");
    final String[] content = buildresult.getTargetOutput("get-source-path-multiple-folders-dirseparator");
    Assert.assertEquals(1, content.length);
    Assert.assertEquals("${workspacedir}@" + projectdescription.getPrimaryProjectname() + File.pathSeparator
        + "${workspacedir}@" + projectdescription.getPrimaryProjectname() + "@" + NAME_GENERATEDSOURCE, content[0]);
  }

} /* ENDCLASS */
