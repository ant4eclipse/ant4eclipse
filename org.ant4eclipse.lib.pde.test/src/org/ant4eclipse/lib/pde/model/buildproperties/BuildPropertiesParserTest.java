/**********************************************************************
 * Copyright (c) 2011 Nils Hartmann and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Nils Hartmann initial implementation
 **********************************************************************/
package org.ant4eclipse.lib.pde.model.buildproperties;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

import java.io.InputStream;

import org.ant4eclipse.lib.core.util.StringMap;
import org.ant4eclipse.lib.pde.model.buildproperties.PluginBuildProperties.Library;
import org.junit.Test;

/**
 * @author Nils Hartmann (nils@nilshartmann.net)
 * 
 */
public class BuildPropertiesParserTest {

  @Test
  public void test_BuildPropertiesParser() throws Exception {
    String resourceName = getClass().getPackage().getName().replace('.', '/')
        + "/buildproperties-parser-test.properties";
    InputStream inputStream = getClass().getClassLoader().getResourceAsStream(resourceName);
    assertThat(inputStream, is(notNullValue()));
    StringMap stringMap = new StringMap(inputStream);

    PluginBuildProperties buildProperties = BuildPropertiesParser.initializePluginBuildProperties(stringMap);

    assertThat(buildProperties.getAdditionalBundles(),
        is(equalTo(new String[] { "org.eclipse.osgi", "org.apache.ant" })));

    Library library = buildProperties.getLibrary(".");
    assertThat(library, is(notNullValue()));

    assertThat(library.getSource(), is(equalTo(new String[] { "src" })));
    assertThat(library.getOutput(), is(equalTo(new String[] { "bin" })));
    assertThat(buildProperties.getBinaryIncludes(), is(equalTo(new String[] { "META-INF", "." })));
  }
}
