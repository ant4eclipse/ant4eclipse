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
package org.ant4eclipse.ant.pde;

import org.ant4eclipse.testframework.PdeProjectBuilder;

/**
 * Tests the functionality while using multiple taskdef calls (these are causing multiple reloads and thus
 * initialisation of class members).
 * 
 * @author Daniel Kasmeroglu (Daniel.Kasmeroglu@Kasisoft.net)
 */
public class MultipleTaskDefTest extends AbstractPdeBuildFileTest {

  @Override
  protected void setupDefaultBuildFile() throws Exception {
    // set up the build file
    setupBuildFile("MultipleTaskDefTest.xml");
  }

  @Override
  protected void tearDown() throws Exception {
  }

  /**
   * <p>
   * </p>
   */
  public void testAntCall() {

    // set some ant properties
    getProject().setProperty("projectname", "simpleproject1");

    // create simple project
    PdeProjectBuilder pdeProjectBuilder = PdeProjectBuilder.getPreConfiguredPdeProjectBuilder("simpleproject1");
    pdeProjectBuilder.withSourceClass("src", "de.simpleproject1.test.Test").finishClass();
    pdeProjectBuilder.withSourceClass("src", "de.simpleproject1.test.Test2").finishClass();
    pdeProjectBuilder.withSourceClass("src", "de.simpleproject1.test.Test3").finishClass();
    pdeProjectBuilder.withBundleManifest().withBundleVersion("1.0.0").withFragmentHost(
        "org.eclipse.osgi;bundle-version=\"[3.2.0,4.0.0)\"").withImportPackage("org.eclipse.ant.core");
    pdeProjectBuilder.withDefaultBuildProperties();
    pdeProjectBuilder.createIn(getTestWorkspaceDirectory());

    StringBuffer buffer = new StringBuffer();
    buffer.append("org.eclipse.equinox.registry_3.4.100.v20090520-1800");
    buffer.append("org.eclipse.ant.core_3.2.100.v20090520");
    buffer.append("org.eclipse.core.jobs_3.4.100.v20090429-1800");
    buffer.append("org.eclipse.core.runtime.compatibility.auth_3.2.100.v20090413");
    buffer.append("org.eclipse.equinox.app_1.2.0.v20090520-1800");
    buffer.append("org.eclipse.equinox.common_3.5.0.v20090520-1800");
    buffer.append("org.eclipse.core.contenttype_3.4.0.v20090429-1800");
    buffer.append("org.eclipse.core.runtime_3.5.0.v20090525");
    buffer.append("org.eclipse.osgi_3.5.0.v20090520");
    buffer.append("simpleproject1_1.0.0");
    buffer.append("org.eclipse.osgi.services_3.2.0.v20090520-1800");
    buffer.append("javax.servlet_2.5.0.v200806031605");
    buffer.append("org.eclipse.core.variables_3.2.200.v20090521");
    buffer.append("org.eclipse.equinox.preferences_3.2.300.v20090520-1800");

    expectLog("testAntCall", "beforedoExecuteafter");
  }

} /* ENDCLASS */
