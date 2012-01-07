/**********************************************************************
 * Copyright (c) 2005-2009 ant4eclipse project team.
 * 
 * All rights reserved. This program and the accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: Nils Hartmann, Daniel Kasmeroglu, Gerd Wuetherich
 **********************************************************************/
package org.ant4eclipse.ant.platform;

import org.ant4eclipse.platform.test.AbstractWorkspaceBasedBuildFileTest;

import org.ant4eclipse.testframework.EclipseProjectBuilder;
import org.junit.Assert;

import java.io.File;
import java.io.IOException;

public class GetProjectDirecoryTest extends AbstractWorkspaceBasedBuildFileTest {

  @Override
  public void setUp() throws Exception {
    super.setUp();

    setupBuildFile( "getProjectDirectory.xml" );

    new EclipseProjectBuilder( "simpleproject" ).withNature( "org.ant4eclipse.testnature" ).createIn(
        getTestWorkspaceDirectory() );
  }

  public void testGetProjectDirectory() {
    try {
      expectLog( "testGetProjectDirectory", getTestWorkspaceDirectory().getCanonicalPath() + File.separator
          + "simpleproject" );
    } catch( IOException ex ) {
      Assert.fail( ex.getMessage() );
    }
  }
  
} /* ENDCLASS */
