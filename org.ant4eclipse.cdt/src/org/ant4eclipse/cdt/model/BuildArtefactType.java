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
package org.ant4eclipse.cdt.model;

/**
 * Types supported by the CDT build system.
 * 
 * @author Daniel Kasmeroglu (Daniel.Kasmeroglu@Kasisoft.net)
 */
public enum BuildArtefactType {

  SharedLibrary("org.eclipse.cdt.build.core.buildArtefactType.sharedLib"), StaticLibrary(
      "org.eclipse.cdt.build.core.buildArtefactType.staticLib"), Executable(
      "org.eclipse.cdt.build.core.buildArtefactType.exe"), Unmanaged(null);

  private String _value;

  /**
   * Prepares this type with the corresponding value (used within the xml representation).
   * 
   * @param value
   *          The identifying value used within the xml representation. Maybe <code>null</code>.
   */
  BuildArtefactType(String value) {
    this._value = value;
  }

  /**
   * Returns the type associated with the supplied id. If the type cannot be identified it might be an unmanaged
   * project.
   * 
   * @param id
   *          The ID used to identify the type of project.
   * 
   * @return The artifact type or <code>null</code> if it could not be identified (in that case it might be an unmanaged
   *         project).
   */
  public static final BuildArtefactType valueByID(String id) {
    for (BuildArtefactType type : BuildArtefactType.values()) {
      if (type._value.equals(id)) {
        return type;
      }
    }
    return null;
  }

} /* ENDENUM */
