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
package org.ant4eclipse.lib.cdt.model.project;

import org.ant4eclipse.lib.cdt.internal.model.project.CProjectRoleImpl;
import org.ant4eclipse.lib.core.Assure;
import org.ant4eclipse.lib.core.logging.A4ELogging;
import org.ant4eclipse.lib.platform.model.resource.EclipseProject;
import org.ant4eclipse.lib.platform.model.resource.ProjectNature;
import org.ant4eclipse.lib.platform.model.resource.role.ProjectRole;
import org.ant4eclipse.lib.platform.model.resource.role.ProjectRoleIdentifier;

import java.util.Set;

/**
 * <p>
 * {@link ProjectRoleIdentifier} for the c project role.
 * </p>
 * 
 * @author Daniel Kasmeroglu (Daniel.Kasmeroglu@Kasisoft.net)
 */
public final class CRoleIdentifier implements ProjectRoleIdentifier {

  /**
   * <p>
   * Returns <code>true</code> is the given project has the nature <code>"org.eclipse.cdt.core.cnature"</code>.
   * </p>
   */
  @Override
  public boolean isRoleSupported(EclipseProject project) {
    return project.hasNature(CProjectRole.C_NATURE);
  }

  /**
   * <p>
   * Adds a {@link CProjectRole} to the given project and parses the pathes.
   * </p>
   */
  @Override
  public ProjectRole createRole(EclipseProject project) {
    A4ELogging.trace("CRoleIdentifier.applyRole(%s)", project);
    Assure.notNull("project", project);
    final CProjectRoleImpl result = new CProjectRoleImpl(project);
    // ClasspathFileParser.parseClasspath(javaProjectRole);
    return result;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void postProcess(EclipseProject project) {
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Integer getPriority() {
    return null;
  }
  
  /**
   * {@inheritDoc}
   */
  @Override
  public void reset() {
  }
  
  /**
   * {@inheritDoc}
   */
  @Override
  public Set<ProjectNature> getNatures() {
    return ProjectNature.createNatures( CProjectRole.C_NATURE );
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String[] getNatureNicknames() {
    return new String[] { "c" };
  }
  
} /* ENDCLASS */
