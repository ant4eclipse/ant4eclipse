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
package org.ant4eclipse.lib.jdt.ecj.internal.tools;


import org.ant4eclipse.lib.core.Assure;
import org.ant4eclipse.lib.jdt.ecj.ReferableSourceFile;
import org.eclipse.jdt.internal.compiler.env.AccessRestriction;

import java.io.File;

/**
 * <p>
 * </p>
 * 
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 */
public class ReferableSourceFileImpl extends SourceFileImpl implements ReferableSourceFile {

  private DefaultReferableType _referableType = new DefaultReferableType();

  /**
   * <p>
   * Creates a new instance of type {@link ReferableSourceFileImpl}.
   * </p>
   * 
   * @param sourceFolder
   * @param sourceFileName
   */
  public ReferableSourceFileImpl( File sourceFolder, String sourceFileName, String libraryLocation, byte libraryType ) {
    super( sourceFolder, sourceFileName );
    Assure.notNull( "libraryLocation", libraryLocation );
    _referableType.setLibraryLocation( libraryLocation );
    _referableType.setLibraryType( libraryType );
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public final AccessRestriction getAccessRestriction() {
    return _referableType.getAccessRestriction();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getLibraryLocation() {
    return _referableType.getLibraryLocation();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public byte getLibraryType() {
    return _referableType.getLibraryType();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public final boolean hasAccessRestriction() {
    return _referableType.hasAccessRestriction();
  }

  /**
   * {@inheritDoc}
   */
  public final void setAccessRestriction( AccessRestriction accessRestriction ) {
    _referableType.setAccessRestriction( accessRestriction );
  }
  
} /* ENDCLASS */
