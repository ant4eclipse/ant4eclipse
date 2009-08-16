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
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.net.URL;

/**
 * @author Daniel Kasmeroglu (Daniel.Kasmeroglu@Kasisoft.net)
 */
public class SourcePathDLTKTest extends AbstractWorkspaceBasedTest {

  private URL _sourcepathxml;

  /**
   * Initialises this set of tests.
   */
  public SourcePathDLTKTest() {
    super(true);
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
  public void emptyProjectDLTK() {
    final String projectname = createEmptyProject(_sourcepathxml);
    final BuildResult buildresult = execute(projectname, "get-source-path");
    final String[] content = buildresult.getTargetOutput("get-source-path");
    Assert.assertEquals(1, content.length);
    Assert.assertEquals("${workspacedir}" + File.separator + projectname, content[0]);
  }

  @Test
  public void emptyProjectPyDev() {
    final String projectname = createEmptyProject(_sourcepathxml);
    final BuildResult buildresult = execute(projectname, "get-source-path");
    final String[] content = buildresult.getTargetOutput("get-source-path");
    Assert.assertEquals(1, content.length);
    Assert.assertEquals("${workspacedir}" + File.separator + projectname + File.separator + "src", content[0]);
  }

} /* ENDCLASS */
