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
package org.ant4eclipse.core;

import org.ant4eclipse.lib.core.DefaultConfigurator;
import org.ant4eclipse.lib.core.service.ServiceRegistry;
import org.ant4eclipse.lib.core.util.StringMap;
import org.apache.tools.ant.Project;
import org.junit.Test;

public class Ant4EclipseConfiguratorTest {

  @Test
  public void testAnt4EclipseConfigurator() {

    DefaultConfigurator.configureAnt4Eclipse();
    DefaultConfigurator.configureAnt4Eclipse();

    ServiceRegistry.reset();
  }

  @Test
  public void testAnt4EclipseConfigurator_2() {
    Project project = new Project();
    AntConfigurator.configureAnt4Eclipse(project);
    AntConfigurator.configureAnt4Eclipse(project);

    ServiceRegistry.reset();
  }

  @Test
  public void testAnt4EclipseConfigurator_3() {
    DefaultConfigurator.configureAnt4Eclipse(new StringMap());
    DefaultConfigurator.configureAnt4Eclipse(new StringMap());

    ServiceRegistry.reset();
  }

  @Test
  public void testAnt4EclipseConfigurator_4() {
    DefaultConfigurator.configureAnt4Eclipse((StringMap) null);
    DefaultConfigurator.configureAnt4Eclipse((StringMap) null);

    ServiceRegistry.reset();
  }
}
