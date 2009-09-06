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

  private String _owningproject;

  /**
   * Sets up this entry with the name of the project.
   * 
   * @param owningproject
   *          The name of the related eclipse project. Neither <code>null</code> nor empty.
   * @param name
   *          The name of the project. Neither <code>null</code> nor empty.
   */
  public ResolvedProjectEntry(final String owningproject, final String name) {
    Assert.nonEmpty(owningproject);
    Assert.nonEmpty(name);
    _owningproject = owningproject;
    _projectname = name;
  }

  /**
   * {@inheritDoc}
   */
  public String getOwningProjectname() {
    return _owningproject;
  }

  /**
   * {@inheritDoc}
   */
  public ReferenceKind getKind() {
    return ReferenceKind.Project;
  }

  /**
   * Returns the name of the referred project.
   * 
   * @return The name of the referred project. Neither <code>null</code> nor empty.
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
    if (!_owningproject.equals(other._owningproject)) {
      return false;
    }
    return _projectname.equals(other._projectname);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public int hashCode() {
    int result = _owningproject.hashCode();
    result = 31 * result + _projectname.hashCode();
    return result;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String toString() {
    final StringBuffer buffer = new StringBuffer();
    buffer.append("[ResolvedProjectEntry:");
    buffer.append(" _owningproject: ");
    buffer.append(_owningproject);
    buffer.append(", _projectname: ");
    buffer.append(_projectname);
    buffer.append("]");
    return buffer.toString();
  }

} /* ENDCLASS */
