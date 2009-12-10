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

import org.ant4eclipse.lib.core.Assure;

/**
 * Resolved record used to identify a source folder within a project.
 * 
 * @author Daniel Kasmeroglu (Daniel.Kasmeroglu@Kasisoft.net)
 */
public class ResolvedSourceEntry implements ResolvedPathEntry {

  private String _folder;

  private String _owningproject;

  /**
   * Sets up this entry with the relative path of the folder. The path is relative to the project.
   * 
   * @param owningproject
   *          The name of the related eclipse project. Neither <code>null</code> nor empty.
   * @param foldername
   *          The name of the folder. <code>null</code> or not empty.
   */
  public ResolvedSourceEntry(String owningproject, String foldername) {
    Assure.nonEmpty(owningproject);
    this._owningproject = owningproject;
    this._folder = foldername;
  }

  /**
   * {@inheritDoc}
   */
  public String getOwningProjectname() {
    return this._owningproject;
  }

  /**
   * {@inheritDoc}
   */
  public ReferenceKind getKind() {
    return ReferenceKind.Source;
  }

  /**
   * Returns the relative path of the folder within the project.
   * 
   * @return The relative path of the folder. <code>null</code> or not empty.
   */
  public String getFolder() {
    return this._folder;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean equals(Object object) {
    if (this == object) {
      return true;
    }
    if (object == null) {
      return false;
    }
    if (object.getClass() != getClass()) {
      return false;
    }
    ResolvedSourceEntry other = (ResolvedSourceEntry) object;
    if (!this._owningproject.equals(other._owningproject)) {
      return false;
    }
    if (this._folder == null) {
      return other._folder == null;
    }
    return this._folder.equals(other._folder);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public int hashCode() {
    int result = this._owningproject.hashCode();
    result = 31 * result + (this._folder != null ? this._folder.hashCode() : 0);
    return result;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String toString() {
    StringBuffer buffer = new StringBuffer();
    buffer.append("[ResolvedSourceEntry:");
    buffer.append(" _owningproject: ");
    buffer.append(this._owningproject);
    buffer.append(", _folder: ");
    buffer.append(this._folder);
    buffer.append("]");
    return buffer.toString();
  }

} /* ENDCLASS */
