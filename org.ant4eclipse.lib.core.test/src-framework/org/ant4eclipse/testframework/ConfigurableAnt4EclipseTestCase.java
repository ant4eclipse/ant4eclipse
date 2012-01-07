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
package org.ant4eclipse.testframework;

import org.ant4eclipse.lib.core.util.StringMap;

import javax.imageio.spi.ServiceRegistry;

/**
 * <p>
 * Base class for all AntEclipse test cases that require a configured {@link ServiceRegistry} and are not executed
 * within an Ant environment.
 * </p>
 * 
 * @author Nils Hartmann (nils@nilshartmann.net)
 */
public class ConfigurableAnt4EclipseTestCase {

  /**
   * Provides a set of properties used for the configuration. The supplied set is supposed to be altered and returned.
   * If a <code>null</code> value is returned the default configuration takes place.
   * 
   * @param properties
   *          The current set of properties which is supposed to be altered. Not <code>null</code>.
   * 
   * @return The altered properties or <code>null</code>.
   */
  protected StringMap customAnt4EclipseConfiguration( StringMap properties ) {
    return null;
  }

} /* ENDCLASS */
