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
import org.ant4eclipse.lib.core.data.Version;

import java.io.File;

/**
 * Resolved record used to identify a python runtime.
 * 
 * @author Daniel Kasmeroglu (Daniel.Kasmeroglu@Kasisoft.net)
 */
public class ResolvedRuntimeEntry implements ResolvedPathEntry {

  private Version _version;

  private File[]  _libs;

  private String  _owningproject;

  /**
   * Initialises this entry used to describe a runtime.
   * 
   * @param owningproject
   *          The name of the related eclipse project. Neither <code>null</code> nor empty.
   * @param version
   *          The version of the runtime. Not <code>null</code>.
   * @param libs
   *          The bundled libraries representing the runtime. Not <code>null</code>.
   */
  public ResolvedRuntimeEntry(String owningproject, Version version, File[] libs) {
    Assure.notNull(version);
    Assure.notNull(libs);
    Assure.nonEmpty(owningproject);
    this._owningproject = owningproject;
    this._version = version;
    this._libs = libs;
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
    return ReferenceKind.Runtime;
  }

  /**
   * Returns the version of the runtime.
   * 
   * @return The version of the runtime. Not <code>null</code>.
   */
  public Version getVersion() {
    return this._version;
  }

  /**
   * Returns the libraries for this runtime.
   * 
   * @return The libraries for this runtime. Not <code>null</code>.
   */
  public File[] getLibraries() {
    return this._libs;
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
    ResolvedRuntimeEntry other = (ResolvedRuntimeEntry) object;
    if (!this._owningproject.equals(other._owningproject)) {
      return false;
    }
    if (this._libs.length != other._libs.length) {
      return false;
    }
    if (!this._version.equals(other._version)) {
      return false;
    }
    for (int i = 0; i < this._libs.length; i++) {
      if (!this._libs[i].equals(other._libs[i])) {
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
    int result = this._owningproject.hashCode();
    result = 31 * result + this._version.hashCode();
    for (File lib : this._libs) {
      result = 31 * result + lib.hashCode();
    }
    return result;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String toString() {
    StringBuffer buffer = new StringBuffer();
    buffer.append("[ResolvedRuntimeEntry:");
    buffer.append(" _owningproject: ");
    buffer.append(this._owningproject);
    buffer.append(", _version: ");
    buffer.append(this._version);
    buffer.append(", _libs: {");
    buffer.append(this._libs[0]);
    for (int i = 1; i < this._libs.length; i++) {
      buffer.append(", ");
      buffer.append(this._libs[i]);
    }
    buffer.append("}]");
    return buffer.toString();
  }

} /* ENDCLASS */
