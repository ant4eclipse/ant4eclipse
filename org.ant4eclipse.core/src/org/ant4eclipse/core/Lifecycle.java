/**********************************************************************
 * Copyright (c) 2005-2008 ant4eclipse project team.
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

/**
 * This interface must be implemented by services that are aware of a Lifecycle.
 */
public interface Lifecycle {

  /**
   * Returns <code>true</code> if the supplied service already has been initialised.
   * 
   * @return <code>true</code> <=> The service already has been initialised.
   */
  boolean isInitialized();

  /**
   * Initialises this service.
   */
  void initialize();

  /**
   * Marks this service instance as disposable which means that it's no longer used by application code thus allowing to
   * perform some cleanup operations here.
   */
  void dispose();

} /* ENDINTERFACE */
