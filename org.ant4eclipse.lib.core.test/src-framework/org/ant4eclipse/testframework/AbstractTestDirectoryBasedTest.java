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
package org.ant4eclipse.testframework;

import org.ant4eclipse.lib.core.DefaultConfigurator;
import org.ant4eclipse.lib.core.service.ServiceRegistry;
import org.junit.After;
import org.junit.Before;

import java.io.File;

/**
 * Baseclass for all buildfile-based tests in the platform layer
 * 
 * @author Nils Hartmann (nils@nilshartmann.net)
 */
public abstract class AbstractTestDirectoryBasedTest {

  /** - */
  private TestDirectory _testWorkspace;

  /**
   * Creates the Test Environment before execution of a test case
   */
  @Before
  public void setUp() {
    DefaultConfigurator.configureAnt4Eclipse();
    this._testWorkspace = new TestDirectory();
  }

  /**
   * Disposes the test environment and resets the {@link ServiceRegistry}
   */
  @After
  public void tearDown() {
    this._testWorkspace.dispose();
    ServiceRegistry.reset();
    this._testWorkspace = null;
  }

  /**
   * Returns a {@link TestDirectory} for this test case.
   * 
   * @return A TestDirectory for this test case. Not <code>null</code> during a test.
   */
  protected TestDirectory getTestDirectory() {
    return this._testWorkspace;
  }

  /**
   * Returns the root directory of the workspace.
   * 
   * @return The root directory of the workspace. Not <code>null</code> during a test.
   */
  protected File getTestDirectoryRootDir() {
    return this._testWorkspace.getRootDir();
  }

} /* ENDCLASS */
