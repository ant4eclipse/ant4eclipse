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
package org.ant4eclipse.model.platform.resource;

/**
 * <p>
 * Encapsulates a nature of a project. A project can have multiple natures.
 * </p>
 * 
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 */
public interface ProjectNature {

  /**
   * <p>
   * Returns the name of the {@link ProjectNature}.
   * </p>
   * 
   * @return the name of the {@link ProjectNature}.
   */
  public String getName();
} /* ENDCLASS */