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
package org.ant4eclipse.core.ant;

import org.ant4eclipse.lib.core.service.ServiceRegistry;
import org.ant4eclipse.testframework.TestDirectory;
import org.apache.tools.ant.BuildFileTest;

import java.io.File;

/**
 * Base-class for all tests that deals with 'real' build files
 * 
 * <p>
 * The build files are copied from the classpath to a (temp) test directory before executing the test
 * 
 * @author Nils Hartmann (nils@nilshartmann.net)
 */
public abstract class AbstractAnt4EclipseBuildFileTest extends BuildFileTest {

  private TestDirectory _testDirectory;

  @Override
  public void setUp() throws Exception {
    this._testDirectory = new TestDirectory(true);

    File buildFile = this._testDirectory.createFile(getBuildFileName(), getResource(getBuildFileName()).openStream());

    configureProject(buildFile.getAbsolutePath());
  }

  @Override
  protected void tearDown() throws Exception {
    super.tearDown();
    this._testDirectory.dispose();
    ServiceRegistry.reset();
  }

  /**
   * Returns the build file name.
   * 
   * <p>
   * The name must be relative to this class. The file must be available on classpath
   * 
   * @return
   */
  protected abstract String getBuildFileName();

}
