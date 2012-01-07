/**********************************************************************
 * Copyright (c) 2005-2009 ant4eclipse project team.
 * 
 * All rights reserved. This program and the accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: Nils Hartmann, Daniel Kasmeroglu, Gerd Wuetherich
 **********************************************************************/
package org.ant4eclipse.ant.pde;

import org.ant4eclipse.testframework.AbstractTestDirectoryBasedBuildFileTest;

public class ExecutePluginProjectTaskTest extends AbstractTestDirectoryBasedBuildFileTest {

  @Override
  public void setUp() {
    super.setUp();
    configureProject( "src/org/ant4eclipse/ant/pde/ExecutePluginProjectTaskTest.xml" );
  }

  public void testPdeProjectFileSet_simple() {
  }
  
} /* ENDCLASS */
