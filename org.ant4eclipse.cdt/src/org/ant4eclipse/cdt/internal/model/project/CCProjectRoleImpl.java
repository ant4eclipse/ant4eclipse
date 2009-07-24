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
package org.ant4eclipse.cdt.internal.model.project;

import org.ant4eclipse.platform.model.resource.EclipseProject;
import org.ant4eclipse.platform.model.resource.role.AbstractProjectRole;

import org.ant4eclipse.cdt.model.project.CProjectRole;

/**
 * <p>Implements the c++ project role.</p>
 * 
 * @author Daniel Kasmeroglu (Daniel.Kasmeroglu@Kasisoft.net)
 */
//@com.kasisoft.lgpl.tools.diagnostic.KDiagnostic(loggername="cdt")
public class CCProjectRoleImpl extends AbstractProjectRole implements CProjectRole {

  public static final String         NAME = "CCProjectRole";

  /**
   * <p>
   * Creates a new instance of type CCProjectRole.
   * </p>
   * 
   * @param eclipseProject
   *          the eclipse project
   */
  public CCProjectRoleImpl(final EclipseProject eclipseProject) {
    super(NAME, eclipseProject);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String toString() {
    final StringBuffer buffer = new StringBuffer();
    buffer.append("[CCProjectRole:");
    buffer.append(" NAME: ");
    buffer.append(NAME);
    buffer.append("]");
    return buffer.toString();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public int hashCode() {
    int hashCode = super.hashCode();
    return hashCode;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean equals(final Object o) {
    if (this == o) {
      return true;
    }
    if (!super.equals(o)) {
      return false;
    }
    if (o == null) {
      return false;
    }
    if (o.getClass() != getClass()) {
      return false;
    }
    // final CCProjectRoleImpl other = (CCProjectRoleImpl) o;
    return true;
  }

} /* ENDCLASS */