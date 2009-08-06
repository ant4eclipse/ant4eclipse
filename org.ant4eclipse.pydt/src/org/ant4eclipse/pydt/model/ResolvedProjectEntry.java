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
package org.ant4eclipse.pydt.model;

import org.ant4eclipse.core.Assert;

/**
 * Resolved record used to identify an eclipse project.
 * 
 * @author Daniel Kasmeroglu (Daniel.Kasmeroglu@Kasisoft.net)
 */
public class ResolvedProjectEntry implements ResolvedPathEntry {

  private String _projectname;

  /**
   * Sets up this entry with the name of the project.
   * 
   * @param name
   *          The name of the project. Neither <code>null</code> nor empty.
   */
  public ResolvedProjectEntry(final String name) {
    Assert.nonEmpty(name);
    _projectname = name;
  }

  /**
   * {@inheritDoc}
   */
  public ReferenceKind getKind() {
    return ReferenceKind.Project;
  }

  /**
   * Returns the specified name of the project.
   * 
   * @return The specified name of the project. Neither <code>null</code> nor empty.
   */
  public String getProjectname() {
    return _projectname;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean equals(final Object object) {
    if (this == object) {
      return true;
    }
    if (object == null) {
      return false;
    }
    if (object.getClass() != getClass()) {
      return false;
    }
    final ResolvedProjectEntry other = (ResolvedProjectEntry) object;
    return _projectname.equals(other._projectname);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public int hashCode() {
    return _projectname.hashCode();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String toString() {
    final StringBuffer buffer = new StringBuffer();
    buffer.append("[ResolvedProjectEntry:");
    buffer.append(" _projectname: ");
    buffer.append(_projectname);
    buffer.append("]");
    return buffer.toString();
  }

} /* ENDCLASS */
