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
package org.ant4eclipse.lib.core.util;

import org.ant4eclipse.lib.core.A4EService;

/**
 * A service to manage properties from various sources.
 */
public interface PropertyService extends A4EService {

  /**
   * Returns the property value for the given name, or null if the name if not found.
   * 
   * @param propertyName
   *          The property name to lookup.
   * @return The property value or null.
   */
  String getProperty(String propertyName);
}
