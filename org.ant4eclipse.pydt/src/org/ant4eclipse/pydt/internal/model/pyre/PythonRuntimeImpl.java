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
package org.ant4eclipse.pydt.internal.model.pyre;

import org.ant4eclipse.core.data.Version;

import org.ant4eclipse.pydt.model.PythonInterpreter;
import org.ant4eclipse.pydt.model.pyre.PythonRuntime;

import java.io.File;
import java.util.Arrays;

/**
 * Each runtime is a combination of a predefined set of types as provided with a python release.
 * 
 * @author Daniel Kasmeroglu (Daniel.Kasmeroglu@Kasisoft.net)
 */
class PythonRuntimeImpl implements PythonRuntime {

  private String            _id;

  private File              _location;

  private Version           _version;

  private File[]            _libs;

  private PythonInterpreter _interpreter;

  private File              _executable;

  /**
   * Initialises this runtime implementation.
   * 
   * @param id
   *          The id associated with this runtime. Neither <code>null</code> nor empty.
   * @param location
   *          The location within the filesystem. Not <code>null</code>.
   * @param version
   *          The version of this runtime. Not <code>null</code>.
   * @param libs
   *          The libraries associated with this runtime. Not <code>null</code>.
   * @param interpreter
   *          The interpreter associated with this runtime. Not <code>null</code>.
   */
  public PythonRuntimeImpl(final String id, final File location, final Version version, final File[] libs,
      final PythonInterpreter interpreter) {
    _id = id;
    _location = location;
    _version = version;
    _interpreter = interpreter;
    _executable = _interpreter.lookup(_location);
    _libs = libs;
    Arrays.sort(_libs);
  }

  /**
   * {@inheritDoc}
   */
  public String getId() {
    return _id;
  }

  /**
   * {@inheritDoc}
   */
  public File getLocation() {
    return _location;
  }

  /**
   * {@inheritDoc}
   */
  public Version getVersion() {
    return _version;
  }

  /**
   * {@inheritDoc}
   */
  public File[] getLibraries() {
    return _libs;
  }

  /**
   * {@inheritDoc}
   */
  public PythonInterpreter getInterpreter() {
    return _interpreter;
  }

  /**
   * {@inheritDoc}
   */
  public File getExecutable() {
    return _executable;
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
    final PythonRuntimeImpl other = (PythonRuntimeImpl) object;
    if (!_id.equals(other._id)) {
      return false;
    }
    if (!_location.equals(other._location)) {
      return false;
    }
    if (!_version.equals(other._version)) {
      return false;
    }
    if (_libs.length != other._libs.length) {
      return false;
    }
    if (!_interpreter.equals(other._interpreter)) {
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
    int result = _id.hashCode();
    result = 31 * result + _location.hashCode();
    result = 31 * result + _version.hashCode();
    result = 31 * result + _interpreter.hashCode();
    for (int i = 0; i < _libs.length; i++) {
      result = 31 * result + _libs[i].hashCode();
    }
    return result;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String toString() {
    final StringBuffer buffer = new StringBuffer();
    buffer.append("[PythonRuntimeImpl:");
    buffer.append(" _id: ");
    buffer.append(_id);
    buffer.append(", _location: ");
    buffer.append(_location);
    buffer.append(", _version: ");
    buffer.append(_version);
    buffer.append(", _interpreter: ");
    buffer.append(_interpreter);
    buffer.append(", _libs: {");
    buffer.append(_libs[0]);
    for (int i = 1; i < _libs.length; i++) {
      buffer.append(",");
      buffer.append(_libs[i]);
    }
    buffer.append("}");
    buffer.append("]");
    return buffer.toString();
  }

} /* ENDCLASS */