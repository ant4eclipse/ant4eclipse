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

/**
 * Common interface used for A4E related services.
 */
public interface A4EService {

  /**
   * Returns the priority associated with this service. Negative values are reserved for 
   * 
   * @return   The priority associated with this service. Maybe <code>null</code>.
   */
  Integer getPriority();
  
  /**
   * Will be invoked when a service is requested to drop all it's internal data. This function is
   * only used in conjunction with tests.
   */
  void reset();
  
} /* ENDINTERFACE */
