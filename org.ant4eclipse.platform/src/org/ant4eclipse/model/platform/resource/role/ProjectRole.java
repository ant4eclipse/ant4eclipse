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
package org.ant4eclipse.model.platform.resource.role;

import org.ant4eclipse.model.platform.resource.EclipseProject;

/**
 * <p>
 * Defines the common interface for project roles. A project role describes a specific role a project can have, e.g. the
 * java project.
 * </p>
 * 
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 */
public interface ProjectRole {

  /**
   * <p>
   * Returns the name of the {@link ProjectRole}.
   * </p>
   * 
   * @return the name of the {@link ProjectRole}.
   */
  public String getName();

  /**
   * <p>
   * Returns the {@link EclipseProject} this {@link ProjectRole} belongs to.
   * </p>
   * 
   * @return the {@link EclipseProject} this {@link ProjectRole} belongs to.
   */
  public EclipseProject getEclipseProject();
} /* ENDCLASS */