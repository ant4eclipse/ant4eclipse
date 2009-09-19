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
package org.ant4eclipse.jdt.ecj.internal.tools.loader;

import org.ant4eclipse.core.Assert;

import org.ant4eclipse.jdt.ecj.internal.tools.ModifiableClassFile;

import org.eclipse.jdt.internal.compiler.env.AccessRestriction;

/**
 * <p>
 * {@link AbstractClassFileImpl} is the base class of all the class file implementations.
 * </p>
 * 
 * @author Gerd Wuetherich (gerd@gerd-wuetherich.de)
 */
public abstract class AbstractClassFileImpl implements ModifiableClassFile {

  /** the library location */
  private String            _libraryLocation;

  /** the library type */
  private byte              _libraryType;

  /** an access restriction */
  private AccessRestriction _accessRestriction;

  /**
   * <p>
   * Creates a new instance of type {@link AbstractClassFileImpl}.
   * </p>
   * 
   * @param libraryLocation
   *          the library location
   * @param libraryType
   *          the library type
   */
  protected AbstractClassFileImpl(String libraryLocation, byte libraryType) {
    Assert.notNull(libraryLocation);

    this._libraryLocation = libraryLocation;
    this._libraryType = libraryType;
  }

  /**
   * {@inheritDoc}
   */
  public String getLibraryLocation() {
    return this._libraryLocation;
  }

  /**
   * {@inheritDoc}
   */
  public byte getLibraryType() {
    return this._libraryType;
  }

  /**
   * {@inheritDoc}
   */
  public final AccessRestriction getAccessRestriction() {
    return this._accessRestriction;
  }

  /**
   * {@inheritDoc}
   */
  public final boolean hasAccessRestriction() {
    return this._accessRestriction != null;
  }

  /**
   * {@inheritDoc}
   */
  public final void setAccessRestriction(AccessRestriction accessRestriction) {
    this._accessRestriction = accessRestriction;
  }
}
