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
    final String projectname = createEmptyProject(_sourcepathxml, false);
    final BuildResult buildresult = execute(projectname, "get-source-path");
    final String[] content = buildresult.getTargetOutput("get-source-path");
    Assert.assertEquals(1, content.length);
    Assert.assertEquals("${workspacedir}" + File.separator + projectname, content[0]);
  }

  @Test(expected = BuildException.class)
  public void emptyProjectMultipleFoldersFailure() {
    final String projectname = createEmptyProject(_sourcepathxml, true);
    execute(projectname, "get-source-path");
  }

} /* ENDCLASS */
