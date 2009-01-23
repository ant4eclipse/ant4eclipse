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
package org.ant4eclipse.jdt.ejc.internal.tools.loader;

import org.ant4eclipse.core.Assert;
import org.ant4eclipse.jdt.ejc.internal.tools.ModifiableClassFile;
import org.eclipse.jdt.internal.compiler.env.AccessRestriction;

/**
 * {@link AbstractClassFileImpl} is the base class of all the class file implementations.
 * 
 * @author Gerd Wuetherich (gerd@gerd-wuetherich.de)
 */
public abstract class AbstractClassFileImpl implements ModifiableClassFile {

  /** the library location */
  private final String      _libraryLocation;

  /** the library type */
  private final byte        _libraryType;

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
  protected AbstractClassFileImpl(final String libraryLocation, final byte libraryType) {
    Assert.notNull(libraryLocation);

    this._libraryLocation = libraryLocation;
    this._libraryType = libraryType;
  }

  /**
   * @see net.sf.ant4eclipse.tools.core.ejc.loader.ClassFile#getLibraryLocation()
   */
  public String getLibraryLocation() {
    return this._libraryLocation;
  }

  /**
   * @see net.sf.ant4eclipse.tools.core.ejc.loader.ClassFile#getLibraryType()
   */
  public byte getLibraryType() {
    return this._libraryType;
  }

  /**
   * @see net.sf.ant4eclipse.tools.core.ejc.loader.ClassFile#getAccessRestriction()
   */
  public final AccessRestriction getAccessRestriction() {
    return this._accessRestriction;
  }

  /**
   * @see net.sf.ant4eclipse.tools.core.ejc.loader.ClassFile#hasAccessRestriction()
   */
  public final boolean hasAccessRestriction() {
    return this._accessRestriction != null;
  }

  /**
   * @see net.sf.ant4eclipse.tools.core.ejc.loader.ModifiableClassFile#setAccessRestriction(org.eclipse.jdt.internal.compiler.env.AccessRestriction)
   */
  public final void setAccessRestriction(final AccessRestriction accessRestriction) {
    this._accessRestriction = accessRestriction;
  }
}
