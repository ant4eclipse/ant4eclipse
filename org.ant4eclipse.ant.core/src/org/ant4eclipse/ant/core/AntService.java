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

import org.apache.tools.ant.Project;

/**
 * <p>
 * Each implementor is a service capable to be launched within an ant environment.
 * </p>
 * 
 * @author Daniel Kasmeroglu (Daniel.Kasmeroglu@Kasisoft.net)
 */
public interface AntService {

  /**
   * Will be called whenever an ant service will be registered.
   * 
   * @param project
   *          The ant project used for this service. Not <code>null</code>.
   */
  void configure(Project project);

} /* ENDINTERFACE */
