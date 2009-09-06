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

import java.io.File;

/**
 * Resolved record used to identify a path container.
 * 
 * @author Daniel Kasmeroglu (Daniel.Kasmeroglu@Kasisoft.net)
 */
public class ResolvedContainerEntry implements ResolvedPathEntry {

  private File[] _pathes;

  private String _owningproject;

  /**
   * Initialises this entry used to describe a path container.
   * 
   * @param owningproject
   *          The name of the related eclipse project. Neither <code>null</code> nor empty.
   * @param pathes
   *          The bundled pathes representing this container. Not <code>null</code>.
   */
  public ResolvedContainerEntry(final String owningproject, final File[] pathes) {
    Assert.notNull(pathes);
    Assert.nonEmpty(owningproject);
    _owningproject = owningproject;
    _pathes = pathes;
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
    return ReferenceKind.Container;
  }

  /**
   * Returns the pathes for this container.
   * 
   * @return The pathes for this container. Not <code>null</code>.
   */
  public File[] getPathes() {
    return _pathes;
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
    final ResolvedContainerEntry other = (ResolvedContainerEntry) object;
    if (!_owningproject.equals(other._owningproject)) {
      return false;
    }
    if (_pathes.length != other._pathes.length) {
      return false;
    }
    for (int i = 0; i < _pathes.length; i++) {
      if (!_pathes[i].equals(other._pathes[i])) {
        return false;
      }
    }
    return true;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public int hashCode() {
    int result = _owningproject.hashCode();
    for (int i = 0; i < _pathes.length; i++) {
      result = 31 * result + _pathes.hashCode();
    }
    return result;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String toString() {
    final StringBuffer buffer = new StringBuffer();
    buffer.append("[ResolvedContainerEntry:");
    buffer.append(" _owningproject: ");
    buffer.append(_owningproject);
    buffer.append(", _pathes: {");
    buffer.append(_pathes[0]);
    for (int i = 1; i < _pathes.length; i++) {
      buffer.append(", ");
      buffer.append(_pathes[i]);
    }
    buffer.append("}]");
    return buffer.toString();
  }

} /* ENDCLASS */
