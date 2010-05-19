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
package org.ant4eclipse.ant.core;

import org.ant4eclipse.lib.core.service.ServiceRegistryAccess;
import org.apache.tools.ant.Project;
import org.junit.Test;

public class AntConfiguratorTest {

  @Test
  public void projectConfiguration() {
    Project project = new Project();
    AntConfigurator.configureAnt4Eclipse(project);
    AntConfigurator.configureAnt4Eclipse(project);
    ServiceRegistryAccess.reset();
  }

} /* ENDCLASS */
