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
package org.ant4eclipse.lib.platform.internal.model.resource.variable;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

import java.util.Vector;

import org.junit.Before;
import org.junit.Test;

public class PropertyParserTest {

  Vector<String> _fragments;

  Vector<String> _propertyRefs;

  Vector<String> _propertyArgs;

  PropertyParser _propertyParser;

  @Before
  public void createParserAndArguments() {
    _fragments = new Vector<String>();
    _propertyRefs = new Vector<String>();
    _propertyArgs = new Vector<String>();

    _propertyParser = new PropertyParser();

  }

  @Test
  public void stringOnlyWithProperty() {
    String value = "${project_loc}";
    _propertyParser.parsePropertyString( value, _fragments, _propertyRefs, _propertyArgs );

    assertThat( _fragments.size(), is( 1 ) );
    assertThat( _fragments.get( 0 ), is( nullValue() ) );
    assertThat( _propertyRefs.size(), is( 1 ) );
    assertThat( _propertyRefs.get( 0 ), is( equalTo( "project_loc" ) ) );
    assertThat( _propertyArgs.size(), is( 1 ) );
    assertThat( _propertyArgs.get( 0 ), is( nullValue() ) );
  }

  @Test
  public void stringWithTwoDifferentProperties() {
    String value = "${project_loc} ${workspace_loc}";
    _propertyParser.parsePropertyString( value, _fragments, _propertyRefs, _propertyArgs );

    assertThat( _fragments.size(), is( 3 ) );
    assertThat( _fragments.get( 0 ), is( nullValue() ) );
    assertThat( _fragments.get( 1 ), is( equalTo( " " ) ) );
    assertThat( _fragments.get( 2 ), is( nullValue() ) );
    assertThat( _propertyRefs.size(), is( 2 ) );
    assertThat( _propertyRefs.get( 0 ), is( equalTo( "project_loc" ) ) );
    assertThat( _propertyRefs.get( 1 ), is( equalTo( "workspace_loc" ) ) );
    assertThat( _propertyArgs.size(), is( 2 ) );
    assertThat( _propertyArgs.get( 0 ), is( nullValue() ) );
    assertThat( _propertyArgs.get( 1 ), is( nullValue() ) );
  }

  @Test
  public void stringWithTwoDifferentPropertiesAndArguments() {
    String value = "${project_loc:myProject} ${workspace_loc:myWorkspace}";
    _propertyParser.parsePropertyString( value, _fragments, _propertyRefs, _propertyArgs );

    assertThat( _fragments.size(), is( 3 ) );
    assertThat( _fragments.get( 0 ), is( nullValue() ) );
    assertThat( _fragments.get( 1 ), is( equalTo( " " ) ) );
    assertThat( _fragments.get( 2 ), is( nullValue() ) );
    assertThat( _propertyRefs.size(), is( 2 ) );
    assertThat( _propertyRefs.get( 0 ), is( equalTo( "project_loc" ) ) );
    assertThat( _propertyRefs.get( 1 ), is( equalTo( "workspace_loc" ) ) );
    assertThat( _propertyArgs.size(), is( 2 ) );
    assertThat( _propertyArgs.get( 0 ), is( equalTo( "myProject" ) ) );
    assertThat( _propertyArgs.get( 1 ), is( equalTo( "myWorkspace" ) ) );
  }

  @Test
  public void stringWithPropertyAndArgument() {
    String value = "${project_loc:abc}";
    _propertyParser.parsePropertyString( value, _fragments, _propertyRefs, _propertyArgs );

    assertThat( _fragments.size(), is( 1 ) );
    assertThat( _fragments.get( 0 ), is( nullValue() ) );
    assertThat( _propertyRefs.size(), is( 1 ) );
    assertThat( _propertyRefs.get( 0 ), is( equalTo( "project_loc" ) ) );
    assertThat( _propertyArgs.size(), is( 1 ) );
    assertThat( _propertyArgs.get( 0 ), is( "abc" ) );

  }
  
} /* ENDCLASS */
