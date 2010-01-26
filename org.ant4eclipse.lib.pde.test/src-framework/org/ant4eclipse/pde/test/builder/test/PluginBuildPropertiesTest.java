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
package org.ant4eclipse.pde.test.builder.test;

import org.ant4eclipse.pde.test.builder.PluginBuildProperties;

import junit.framework.TestCase;

public class PluginBuildPropertiesTest extends TestCase {

  public void test() {
    PluginBuildProperties properties = new PluginBuildProperties();
    properties.withLibrary(".").withSource("src").withSource("resource").withOutput("bin").finishLibrary();

    assertEquals("source.. = src/,\\\nresource/\noutput.. = bin/\nbin.includes = META-INF/,\\\n.", properties
        .toString());
  }
}
