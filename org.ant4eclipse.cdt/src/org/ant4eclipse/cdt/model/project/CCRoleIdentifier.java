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
package org.ant4eclipse.cdt.model.project;

import org.ant4eclipse.core.Assert;
import org.ant4eclipse.core.logging.A4ELogging;

import org.ant4eclipse.platform.model.resource.EclipseProject;
import org.ant4eclipse.platform.model.resource.role.ProjectRole;
import org.ant4eclipse.platform.model.resource.role.ProjectRoleIdentifier;

import org.ant4eclipse.cdt.internal.model.project.CCProjectRoleImpl;

/**
 * <p>
 * {@link ProjectRoleIdentifier} for the c++ project role.
 * </p>
 * 
 * @author Daniel Kasmeroglu (Daniel.Kasmeroglu@Kasisoft.net)
 */
public final class CCRoleIdentifier implements ProjectRoleIdentifier {

  /**
   * <p>
   * Returns <code>true</code> is the given project has the nature <code>"org.eclipse.cdt.core.cnature"</code>.
   * </p>
   */
  public boolean isRoleSupported(EclipseProject project) {
    return (project.hasNature(CCProjectRole.CC_NATURE));
  }

  /**
   * <p>
   * Adds a {@link CProjectRole} to the given project and parses the pathes.
   * </p>
   */
  public ProjectRole createRole(EclipseProject project) {
    A4ELogging.trace("CRoleIdentifier.applyRole(%s)", project);
    Assert.notNull(project);
    final CCProjectRoleImpl result = new CCProjectRoleImpl(project);
    // ClasspathFileParser.parseClasspath(javaProjectRole);
    return result;
  }

  /**
   * {@inheritDoc}
   */
  public void postProcess(EclipseProject project) {
  }

} /* ENDCLASS */
