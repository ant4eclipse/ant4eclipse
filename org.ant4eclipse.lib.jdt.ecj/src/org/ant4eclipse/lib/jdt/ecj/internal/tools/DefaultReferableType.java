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
package org.ant4eclipse.lib.jdt.ecj.internal.tools;


import org.ant4eclipse.lib.core.Assure;
import org.ant4eclipse.lib.jdt.ecj.ReferableType;
import org.eclipse.jdt.internal.compiler.env.AccessRestriction;

/**
 * <p>
 * {@link DefaultReferableType} is the base class of all referable types.
 * </p>
 * 
 * @author Gerd Wuetherich (gerd@gerd-wuetherich.de)
 */
public class DefaultReferableType implements ReferableType {

  /** the library location */
  private String            _libraryLocation;

  /** the library type */
  private byte              _libraryType;

  /** an access restriction */
  private AccessRestriction _accessRestriction;

  /**
   * <p>
   * Creates a new instance of type DefaultReferableType.
   * </p>
   */
  public DefaultReferableType() {
    //
  }

  /**
   * <p>
   * Creates a new instance of type {@link DefaultReferableType}.
   * </p>
   * 
   * @param libraryLocation
   *          the library location
   * @param libraryType
   *          the library type
   */
  protected DefaultReferableType(String libraryLocation, byte libraryType) {
    Assure.notNull("libraryLocation", libraryLocation);
    this._libraryLocation = libraryLocation;
    this._libraryType = libraryType;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getLibraryLocation() {
    return this._libraryLocation;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public byte getLibraryType() {
    return this._libraryType;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public final AccessRestriction getAccessRestriction() {
    return this._accessRestriction;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public final boolean hasAccessRestriction() {
    return this._accessRestriction != null;
  }

  /**
   * {@inheritDoc}
   */
  public final void setAccessRestriction(AccessRestriction accessRestriction) {
    this._accessRestriction = accessRestriction;
  }

  /**
   * <p>
   * </p>
   * 
   * @param libraryLocation
   *          the libraryLocation to set
   */
  public void setLibraryLocation(String libraryLocation) {
    this._libraryLocation = libraryLocation;
  }

  /**
   * <p>
   * </p>
   * 
   * @param libraryType
   *          the libraryType to set
   */
  public void setLibraryType(byte libraryType) {
    this._libraryType = libraryType;
  }
}
