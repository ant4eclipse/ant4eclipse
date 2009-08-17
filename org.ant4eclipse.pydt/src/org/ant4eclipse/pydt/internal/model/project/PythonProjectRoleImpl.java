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
package org.ant4eclipse.pydt.internal.model.project;

import org.ant4eclipse.platform.model.resource.EclipseProject;
import org.ant4eclipse.platform.model.resource.role.AbstractProjectRole;

import org.ant4eclipse.pydt.model.RawPathEntry;
import org.ant4eclipse.pydt.model.ReferenceKind;
import org.ant4eclipse.pydt.model.project.DLTKProjectRole;
import org.ant4eclipse.pydt.model.project.PyDevProjectRole;

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
  public PythonProjectRoleImpl(final EclipseProject eclipseProject, boolean dltk) {
    super(NAME, eclipseProject);
    _rawpathentries = new ArrayList<RawPathEntry>();
    _isdltk = dltk;
  }

  /**
   * {@inheritDoc}
   */
  public boolean isDLTK() {
    return _isdltk;
  }

  /**
   * Adds the supplied raw (unresolved) path entry to this role implementation.
   * 
   * @param rawPathEntry
   *          The raw path information associated with the current eclipse project.
   */
  public void addRawPathEntry(final RawPathEntry rawpathentry) {
    _rawpathentries.add(rawpathentry);
  }

  /**
   * {@inheritDoc}
   */
  public RawPathEntry[] getRawPathEntries() {
    return _rawpathentries.toArray(new RawPathEntry[_rawpathentries.size()]);
  }

  /**
   * {@inheritDoc}
   */
  public RawPathEntry[] getRawPathEntries(final ReferenceKind kind) {
    final List<RawPathEntry> result = new ArrayList<RawPathEntry>();
    for (RawPathEntry entry : _rawpathentries) {
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
    final StringBuffer buffer = new StringBuffer();
    buffer.append("[PythonProjectRole:");
    buffer.append(" NAME: ");
    buffer.append(NAME);
    buffer.append(", _isdltk: ");
    buffer.append(_isdltk);
    buffer.append(", _rawpathentries; {");
    if (!_rawpathentries.isEmpty()) {
      buffer.append(_rawpathentries.get(0));
      for (int i = 1; i < _rawpathentries.size(); i++) {
        buffer.append(",");
        buffer.append(_rawpathentries.get(i));
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
    for (int i = 0; i < _rawpathentries.size(); i++) {
      result = result * 31 + _rawpathentries.get(i).hashCode();
    }
    result = result * 31 + (_isdltk ? 1 : 0);
    return result;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean equals(final Object object) {
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
    final PythonProjectRoleImpl other = (PythonProjectRoleImpl) object;
    if (_isdltk != other._isdltk) {
      return false;
    }
    if (_rawpathentries.size() != other._rawpathentries.size()) {
      return false;
    }
    for (int i = 0; i < _rawpathentries.size(); i++) {
      if (!_rawpathentries.get(i).equals(other._rawpathentries.get(i))) {
        return false;
      }
    }
    return true;
  }

} /* ENDCLASS */