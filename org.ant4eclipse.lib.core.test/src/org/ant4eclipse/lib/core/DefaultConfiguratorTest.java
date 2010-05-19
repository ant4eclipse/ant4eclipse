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
package org.ant4eclipse.lib.core;

import org.ant4eclipse.lib.core.service.ServiceRegistryAccess;
import org.ant4eclipse.lib.core.util.StringMap;
import org.junit.Test;

public class DefaultConfiguratorTest {

  @Test
  public void simpleConfigurator() {
    DefaultConfigurator.configureAnt4Eclipse();
    DefaultConfigurator.configureAnt4Eclipse();
    ServiceRegistryAccess.reset();
  }

  @Test
  public void configurationWithEmptySettings() {
    DefaultConfigurator.configureAnt4Eclipse(new StringMap());
    DefaultConfigurator.configureAnt4Eclipse(new StringMap());
    ServiceRegistryAccess.reset();
  }

  @Test
  public void configurationWithoutSettings() {
    DefaultConfigurator.configureAnt4Eclipse(null);
    DefaultConfigurator.configureAnt4Eclipse(null);
    ServiceRegistryAccess.reset();
  }

} /* ENDCLASS */
