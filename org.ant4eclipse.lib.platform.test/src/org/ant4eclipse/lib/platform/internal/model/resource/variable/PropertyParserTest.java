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
    this._fragments = new Vector<String>();
    this._propertyRefs = new Vector<String>();
    this._propertyArgs = new Vector<String>();

    this._propertyParser = new PropertyParser();

  }

  @Test
  public void stringOnlyWithProperty() {
    String value = "${project_loc}";
    this._propertyParser.parsePropertyString(value, this._fragments, this._propertyRefs, this._propertyArgs);

    assertThat(this._fragments.size(), is(1));
    assertThat(this._fragments.get(0), is(nullValue()));
    assertThat(this._propertyRefs.size(), is(1));
    assertThat(this._propertyRefs.get(0), is(equalTo("project_loc")));
    assertThat(this._propertyArgs.size(), is(1));
    assertThat(this._propertyArgs.get(0), is(nullValue()));
  }

  @Test
  public void stringWithTwoDifferentProperties() {
    String value = "${project_loc} ${workspace_loc}";
    this._propertyParser.parsePropertyString(value, this._fragments, this._propertyRefs, this._propertyArgs);

    assertThat(this._fragments.size(), is(3));
    assertThat(this._fragments.get(0), is(nullValue()));
    assertThat(this._fragments.get(1), is(equalTo(" ")));
    assertThat(this._fragments.get(2), is(nullValue()));
    assertThat(this._propertyRefs.size(), is(2));
    assertThat(this._propertyRefs.get(0), is(equalTo("project_loc")));
    assertThat(this._propertyRefs.get(1), is(equalTo("workspace_loc")));
    assertThat(this._propertyArgs.size(), is(2));
    assertThat(this._propertyArgs.get(0), is(nullValue()));
    assertThat(this._propertyArgs.get(1), is(nullValue()));
  }

  @Test
  public void stringWithTwoDifferentPropertiesAndArguments() {
    String value = "${project_loc:myProject} ${workspace_loc:myWorkspace}";
    this._propertyParser.parsePropertyString(value, this._fragments, this._propertyRefs, this._propertyArgs);

    assertThat(this._fragments.size(), is(3));
    assertThat(this._fragments.get(0), is(nullValue()));
    assertThat(this._fragments.get(1), is(equalTo(" ")));
    assertThat(this._fragments.get(2), is(nullValue()));
    assertThat(this._propertyRefs.size(), is(2));
    assertThat(this._propertyRefs.get(0), is(equalTo("project_loc")));
    assertThat(this._propertyRefs.get(1), is(equalTo("workspace_loc")));
    assertThat(this._propertyArgs.size(), is(2));
    assertThat(this._propertyArgs.get(0), is(equalTo("myProject")));
    assertThat(this._propertyArgs.get(1), is(equalTo("myWorkspace")));
  }

  public void stringWithPropertyAndArgument() {
    String value = "${project_loc:abc}";
    this._propertyParser.parsePropertyString(value, this._fragments, this._propertyRefs, this._propertyArgs);

    assertThat(this._fragments.size(), is(1));
    assertThat(this._fragments.get(0), is(nullValue()));
    assertThat(this._propertyRefs.size(), is(1));
    assertThat(this._propertyRefs.get(0), is(equalTo("project_loc")));
    assertThat(this._propertyArgs.size(), is(1));
    assertThat(this._propertyArgs.get(1), is("abc"));

  }
}
