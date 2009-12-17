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

import org.ant4eclipse.pde.test.builder.PdeProjectBuilder;

import org.ant4eclipse.lib.core.Assure;
import org.ant4eclipse.testframework.AbstractTestDirectoryBasedTest;
import org.junit.Assert;

import java.io.File;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class PdeProjectBuilderTest extends AbstractTestDirectoryBasedTest {

  public void testPdeProjectFileSet_simple() {

    // create simple project
    PdeProjectBuilder pdeProjectBuilder = PdeProjectBuilder.getPreConfiguredPdeProjectBuilder("simpleproject1");
    pdeProjectBuilder.withSourceClass("@dot", "de.gerd-wuetherich.test.Gerd").finishClass();
    pdeProjectBuilder.withSourceClass("my/src", "de.gerd-wuetherich.test.Test").finishClass();
    pdeProjectBuilder.withSourceClass("my/src", "de.gerd-wuetherich.test.Test2").finishClass();
    pdeProjectBuilder.withSourceClass("my/src", "de.gerd-wuetherich.test.Test3").finishClass();
    pdeProjectBuilder.createIn(getTestDirectoryRootDir());

    File root = getTestDirectoryRootDir();

    // assert
    Assure.isDirectory(root);

    File simpleProjectDir = new File(root, "simpleproject1");

    Assure.isDirectory(simpleProjectDir);

    assertChildren(simpleProjectDir, ".classpath", ".project", "@dot", "META-INF", "my");

    assertChildren(new File(simpleProjectDir, "@dot"), "de");
    assertChildren(new File(simpleProjectDir, "my"), "src");
    assertChildren(new File(simpleProjectDir, "META-INF"), "MANIFEST.MF");
  }

  /**
   * @param parent
   * @param names
   */
  protected void assertChildren(File parent, String... names) {
    Assure.isDirectory(parent);

    File[] children = parent.listFiles();
    Assert.assertEquals(names.length, children.length);

    List<String> namesList = new LinkedList<String>(Arrays.asList(names));
    for (File file : children) {
      Assert.assertTrue(namesList.remove(file.getName()));
    }
  }

} /* ENDCLASS */
