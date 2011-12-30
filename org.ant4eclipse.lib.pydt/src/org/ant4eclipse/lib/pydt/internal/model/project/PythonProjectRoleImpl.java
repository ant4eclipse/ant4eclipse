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
package org.ant4eclipse.lib.pydt.internal.model.project;


import org.ant4eclipse.lib.platform.model.resource.EclipseProject;
import org.ant4eclipse.lib.platform.model.resource.role.AbstractProjectRole;
import org.ant4eclipse.lib.pydt.model.RawPathEntry;
import org.ant4eclipse.lib.pydt.model.ReferenceKind;
import org.ant4eclipse.lib.pydt.model.project.DLTKProjectRole;
import org.ant4eclipse.lib.pydt.model.project.PyDevProjectRole;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * Implements the python project role.
 * </p>
 * 
 * @author Daniel Kasmeroglu (Daniel.Kasmeroglu@Kasisoft.net)
 */
public class PythonProjectRoleImpl extends AbstractProjectRole implements DLTKProjectRole, PyDevProjectRole {

  public static final String NAME = "PythonProjectRole";

  private List<RawPathEntry> _rawpathentries;

  private boolean            _isdltk;

  /**
   * <p>
   * Creates a new instance of type PythonProjectRole.
   * </p>
   * 
   * @param eclipseProject
   *          The eclipse project. Not <code>null</code>.
   */
  public PythonProjectRoleImpl(EclipseProject eclipseProject, boolean dltk) {
    super(NAME, eclipseProject);
    this._rawpathentries = new ArrayList<RawPathEntry>();
    this._isdltk = dltk;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isDLTK() {
    return this._isdltk;
  }

  /**
   * Adds the supplied raw (unresolved) path entry to this role implementation.
   * 
   * @param rawPathEntry
   *          The raw path information associated with the current eclipse project.
   */
  public void addRawPathEntry(RawPathEntry rawpathentry) {
    this._rawpathentries.add(rawpathentry);
  }

  /**
   * Removes the supplied raw (unresolved) path entry from this role implementation.
   * 
   * @param entry
   */
  public void removeRawPathEntry(RawPathEntry entry) {
    this._rawpathentries.remove(entry);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public RawPathEntry[] getRawPathEntries() {
    return this._rawpathentries.toArray(new RawPathEntry[this._rawpathentries.size()]);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public RawPathEntry[] getRawPathEntries(ReferenceKind kind) {
    List<RawPathEntry> result = new ArrayList<RawPathEntry>();
    for (RawPathEntry entry : this._rawpathentries) {
      if (kind == entry.getKind()) {
        result.add(entry);
      }
    }
    return result.toArray(new RawPathEntry[result.size()]);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String toString() {
    StringBuffer buffer = new StringBuffer();
    buffer.append("[PythonProjectRole:");
    buffer.append(" NAME: ");
    buffer.append(NAME);
    buffer.append(", _isdltk: ");
    buffer.append(this._isdltk);
    buffer.append(", _rawpathentries; {");
    if (!this._rawpathentries.isEmpty()) {
      buffer.append(this._rawpathentries.get(0));
      for (int i = 1; i < this._rawpathentries.size(); i++) {
        buffer.append(",");
        buffer.append(this._rawpathentries.get(i));
      }
    }
    buffer.append("}");
    buffer.append("]");
    return buffer.toString();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public int hashCode() {
    int result = super.hashCode();
    for (int i = 0; i < this._rawpathentries.size(); i++) {
      result = result * 31 + this._rawpathentries.get(i).hashCode();
    }
    result = result * 31 + (this._isdltk ? 1 : 0);
    return result;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean equals(Object object) {
    if (this == object) {
      return true;
    }
    if (!super.equals(object)) {
      return false;
    }
    if (object == null) {
      return false;
    }
    if (object.getClass() != getClass()) {
      return false;
    }
    PythonProjectRoleImpl other = (PythonProjectRoleImpl) object;
    if (this._isdltk != other._isdltk) {
      return false;
    }
    if (this._rawpathentries.size() != other._rawpathentries.size()) {
      return false;
    }
    for (int i = 0; i < this._rawpathentries.size(); i++) {
      if (!this._rawpathentries.get(i).equals(other._rawpathentries.get(i))) {
        return false;
      }
    }
    return true;
  }

} /* ENDCLASS */