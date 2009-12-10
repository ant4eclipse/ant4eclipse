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
package org.ant4eclipse.cdt.internal.tools;

import org.ant4eclipse.platform.model.resource.EclipseProject;

import org.ant4eclipse.cdt.model.project.CCProjectRole;
import org.ant4eclipse.cdt.model.project.CProjectRole;

import org.ant4eclipse.lib.core.Assert;

/**
 * <p>
 * Collection of Cdt related helper functions.
 * </p>
 * 
 * @author Daniel Kasmeroglu (Daniel.Kasmeroglu@Kasisoft.net)
 */
public class CdtUtilities {

  /**
   * Disable instantiation.
   */
  private CdtUtilities() {
  }

  /**
   * Returns <code>true</code> in case the supplied project has one of the supported c natures. This method is just a
   * convenience function which combines {@link #isCCProject(EclipseProject)} and {@link #isCProject(EclipseProject)}.
   * 
   * @param project
   *          The project that has to be examined. Not <code>null</code>.
   * 
   * @return <code>true</code> <=> The supplied project has one of the supported c natures.
   */
  public static final boolean isCRelatedProject(EclipseProject project) {
    Assert.notNull(project);
    return project.hasRole(CProjectRole.class) || project.hasRole(CCProjectRole.class);
  }

  /**
   * Returns <code>true</code> in case the supplied project has the simple c nature.
   * 
   * @param project
   *          The project that has to be examined. Not <code>null</code>.
   * 
   * @return <code>true</code> <=> The supplied project has the simple c nature.
   */
  public static final boolean isCProject(EclipseProject project) {
    Assert.notNull(project);
    return project.hasRole(CProjectRole.class);
  }

  /**
   * Returns <code>true</code> in case the supplied project has the c++ nature.
   * 
   * @param project
   *          The project that has to be examined. Not <code>null</code>.
   * 
   * @return <code>true</code> <=> The supplied project has the c++ nature.
   */
  public static final boolean isCCProject(EclipseProject project) {
    Assert.notNull(project);
    return project.hasRole(CCProjectRole.class);
  }

} /* ENDCLASS */
