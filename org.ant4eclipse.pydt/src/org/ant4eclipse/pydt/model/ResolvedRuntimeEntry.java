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
import org.ant4eclipse.core.data.Version;

import java.io.File;

/**
 * Resolved record used to identify a python runtime.
 * 
 * @author Daniel Kasmeroglu (Daniel.Kasmeroglu@Kasisoft.net)
 */
public class ResolvedRuntimeEntry implements ResolvedPathEntry {

  private Version _version;

  private File[]  _libs;

  /**
   * Initialises this entry used to describe a runtime.
   * 
   * @param version
   *          The version of the runtime. Not <code>null</code>.
   * @param libs
   *          The bundled libraries representing the runtime. Not <code>null</code>.
   */
  public ResolvedRuntimeEntry(final Version version, final File[] libs) {
    Assert.notNull(version);
    Assert.notNull(libs);
    _version = version;
    _libs = libs;
  }

  /**
   * {@inheritDoc}
   */
  public ReferenceKind getKind() {
    return ReferenceKind.Runtime;
  }

  /**
   * Returns the version of the runtime.
   * 
   * @return The version of the runtime. Not <code>null</code>.
   */
  public Version getVersion() {
    return _version;
  }

  /**
   * Returns the libraries for this runtime.
   * 
   * @return The libraries for this runtime. Not <code>null</code>.
   */
  public File[] getLibraries() {
    return _libs;
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
    final ResolvedRuntimeEntry other = (ResolvedRuntimeEntry) object;
    if (_libs.length != other._libs.length) {
      return false;
    }
    if (!_version.equals(other._version)) {
      return false;
    }
    for (int i = 0; i < _libs.length; i++) {
      if (!_libs[i].equals(other._libs[i])) {
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
    int result = _version.hashCode();
    for (int i = 0; i < _libs.length; i++) {
      result = 31 * result + _libs.hashCode();
    }
    return result;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String toString() {
    final StringBuffer buffer = new StringBuffer();
    buffer.append("[ResolvedRuntimeEntry:");
    buffer.append(" _version: ");
    buffer.append(_version);
    buffer.append(", _libs: {");
    buffer.append(_libs[0]);
    for (int i = 1; i < _libs.length; i++) {
      buffer.append(", ");
      buffer.append(_libs[i]);
    }
    buffer.append("}]");
    return buffer.toString();
  }

} /* ENDCLASS */
