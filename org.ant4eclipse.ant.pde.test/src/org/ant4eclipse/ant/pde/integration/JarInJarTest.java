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
package org.ant4eclipse.ant.pde.integration;

import org.ant4eclipse.pde.test.builder.JarBundleBuilder;
import org.ant4eclipse.pde.test.builder.PdeProjectBuilder;

import org.ant4eclipse.ant.pde.AbstractPdeBuildFileTest;

public class JarInJarTest extends AbstractPdeBuildFileTest {

  public void testJarInJar() {

    try {
      JarBundleBuilder jarBundleBuilder = new JarBundleBuilder("test");
      jarBundleBuilder.withBundleManifest().withBundleVersion("1.1.2").withExportPackage("test");
      jarBundleBuilder.withEmbeddedJar("hurz");
      jarBundleBuilder.createIn(getAdditionalTargetPlatformDirectory());

      // create simple project
      PdeProjectBuilder pdeProjectBuilder = PdeProjectBuilder.getPreConfiguredPdeProjectBuilder("simpleproject1");
      pdeProjectBuilder.withSourceClass("src", "de.simpleproject1.test.Test").finishClass();
      pdeProjectBuilder.withSourceClass("src", "de.simpleproject1.test.Test2").finishClass();
      pdeProjectBuilder.withSourceClass("src", "de.simpleproject1.test.Test3").finishClass();
      pdeProjectBuilder.withBundleManifest().withBundleVersion("1.0.0").withFragmentHost(
          "org.eclipse.osgi;bundle-version=\"[3.2.0,4.0.0)\"").withImportPackage("test");
      pdeProjectBuilder.createIn(getTestWorkspaceDirectory());

      String regexp2 = "projectname: 'simpleproject1'R:\\\\software\\\\ide\\\\eclipse35\\\\plugins\\\\org.eclipse.osgi_3.5.0.v20090520.jar;simpleproject1\\\\bin";

      // execute test
      expectLogMatches("buildWorkspace", getExpectedTargetPlatformLog() + regexp2);
    } catch (Exception e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }

}
