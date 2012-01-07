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
package org.ant4eclipse.lib.pde.model.launcher;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

import org.junit.Before;
import org.junit.Test;

public class SelectedLaunchConfigurationBundleParserTest {

  private SelectedLaunchConfigurationBundleParser _parser;

  @Before
  public void createParser() {
    _parser = new SelectedLaunchConfigurationBundleParser();
  }

  @Test
  public void symbolicNameOnly() {
    String info = "org.ant4eclipse";

    SelectedLaunchConfigurationBundle bundleInfo = _parser.parseLaunchConfigurationBundleInfo( info );
    assertThat( bundleInfo, notNullValue() );

    assertThat( bundleInfo.getBundleSymbolicName(), equalTo( info ) );
    assertThat( bundleInfo.getVersion(), nullValue() );
    assertThat( bundleInfo.getStartLevel(), equalTo( "default" ) );
    assertThat( bundleInfo.getAutoStart(), equalTo( "default" ) );
  }

  @Test
  public void allInformations() {
    String info = "org.ant4eclipse*1.0.0_RELEASE@14:true";

    SelectedLaunchConfigurationBundle bundleInfo = _parser.parseLaunchConfigurationBundleInfo( info );
    assertThat( bundleInfo, notNullValue() );

    assertThat( bundleInfo.getBundleSymbolicName(), equalTo( "org.ant4eclipse" ) );
    assertThat( bundleInfo.getVersion(), equalTo( "1.0.0_RELEASE" ) );
    assertThat( bundleInfo.getStartLevel(), equalTo( "14" ) );
    assertThat( bundleInfo.getAutoStart(), equalTo( "true" ) );
  }

  @Test
  public void startOnly() {
    String info = "org.ant4eclipse@3:false";

    SelectedLaunchConfigurationBundle bundleInfo = _parser.parseLaunchConfigurationBundleInfo( info );
    assertThat( bundleInfo, notNullValue() );

    assertThat( bundleInfo.getBundleSymbolicName(), equalTo( "org.ant4eclipse" ) );
    assertThat( bundleInfo.getVersion(), nullValue() );
    assertThat( bundleInfo.getStartLevel(), equalTo( "3" ) );
    assertThat( bundleInfo.getAutoStart(), equalTo( "false" ) );
  }
  
} /* ENDCLASS */
