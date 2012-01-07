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
package org.ant4eclipse.lib.platform.internal.model.launcher;

import org.ant4eclipse.lib.core.A4ECore;
import org.ant4eclipse.lib.platform.model.launcher.LaunchConfiguration;
import org.ant4eclipse.lib.platform.model.launcher.LaunchConfigurationReader;
import org.ant4eclipse.testframework.ConfigurableAnt4EclipseTestCase;
import org.ant4eclipse.testframework.TestDirectory;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.InputStream;

public class LaunchConfigurationReaderImplTest extends ConfigurableAnt4EclipseTestCase {

  private TestDirectory _testWorkspace;

  @Before
  public void setup() {
    _testWorkspace = new TestDirectory();
  }

  @After
  public void dispose() {
    _testWorkspace.dispose();
    _testWorkspace = null;
  }

  @Test
  public void jdtLaunchConfig() throws Exception {
    LaunchConfigurationReader launchConfigurationReader = A4ECore.instance().getRequiredService(
        LaunchConfigurationReader.class );
    InputStream inputStream = LaunchConfigurationReaderImplTest.class.getResourceAsStream( "LocalJavaApplication.txt" );
    File launchConfigurationFile = _testWorkspace.createFile( "LocalJavaApplication.launch", inputStream );
    LaunchConfiguration launchConfiguration = launchConfigurationReader
        .readLaunchConfiguration( launchConfigurationFile );
    Assert.assertNotNull( launchConfiguration );
    Assert.assertEquals( "org.eclipse.jdt.launching.localJavaApplication", launchConfiguration.getType() );
    Assert.assertTrue( launchConfiguration.getBooleanAttribute( "myBoolean" ) );
    Assert.assertEquals( "true", launchConfiguration.getAttribute( "myBoolean" ) );
    Assert.assertFalse( launchConfiguration.getBooleanAttribute( "org.eclipse.jdt.launching.DEFAULT_CLASSPATH" ) );
    Assert.assertEquals( "/c/src/main/Main.java,/c/src/main/MainHelper.java",
        launchConfiguration.getAttribute( "org.eclipse.debug.core.MAPPED_RESOURCE_PATHS" ) );
    String[] elements = launchConfiguration.getListAttribute( "org.eclipse.debug.core.MAPPED_RESOURCE_PATHS" );
    Assert.assertNotNull( elements );
    Assert.assertEquals( 2, elements.length );
    Assert.assertEquals( "/c/src/main/Main.java", elements[0] );
    Assert.assertEquals( "/c/src/main/MainHelper.java", elements[1] );
  }

} /* ENDCLASS */
