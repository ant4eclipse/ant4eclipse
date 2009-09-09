package org.ant4eclipse.pde.test.builder;

import junit.framework.TestCase;

public class PluginBuildPropertiesTest extends TestCase {

  public void test() {
    PluginBuildProperties properties = new PluginBuildProperties();
    properties.withLibrary(".").withSource("src").withSource("resource").withOutput("bin").finishLibrary();

    assertEquals("source.. = src/,\\\nresource/\noutput.. = bin/\nbin.includes = META-INF/,\\\n.", properties.toString());
  }
}
