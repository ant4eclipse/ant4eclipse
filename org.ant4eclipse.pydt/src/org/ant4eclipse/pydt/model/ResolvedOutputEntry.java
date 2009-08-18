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


/**
 * Resolved record used to identify an output folder within a project.
 * 
 * @author Daniel Kasmeroglu (Daniel.Kasmeroglu@Kasisoft.net)
 */
public class ResolvedOutputEntry implements ResolvedPathEntry {

  private String _folder;

  /**
   * Sets up this entry with the relative path of the folder. The path is relative to the project.
   * 
   * @param foldername
   *          The name of the folder. <code>null</code> or not empty.
   */
  public ResolvedOutputEntry(final String foldername) {
    _folder = foldername;
  }

  /**
   * {@inheritDoc}
   */
  public ReferenceKind getKind() {
    return ReferenceKind.Output;
  }

  /**
   * Returns the relative path of the folder within the project.
   * 
   * @return The relative path of the folder. <code>null</code> or not empty.
   */
  public String getFolder() {
    return _folder;
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
    final ResolvedOutputEntry other = (ResolvedOutputEntry) object;
    return _folder.equals(other._folder);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public int hashCode() {
    return _folder.hashCode();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String toString() {
    final StringBuffer buffer = new StringBuffer();
    buffer.append("[ResolvedOutputEntry:");
    buffer.append(" _folder: ");
    buffer.append(_folder);
    buffer.append("]");
    return buffer.toString();
  }

} /* ENDCLASS */
