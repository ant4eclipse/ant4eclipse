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
package org.ant4eclipse.lib.jdt.ecj.internal.tools.loader;

import java.io.File;

import org.ant4eclipse.lib.core.Assure;
import org.ant4eclipse.lib.jdt.ecj.ClassFile;
import org.ant4eclipse.lib.jdt.ecj.internal.tools.DefaultReferableType;
import org.eclipse.jdt.internal.compiler.classfmt.ClassFileReader;
import org.eclipse.jdt.internal.compiler.env.IBinaryType;
import org.eclipse.jdt.internal.compiler.util.Util;

/**
 * FileClassFileImpl --
 * 
 * @author Gerd Wuetherich (gerd@gerd-wuetherich.de)
 */
public class FileClassFileImpl extends DefaultReferableType implements ClassFile {

  /** the class file */
  private File _classfile;

  /**
   * @param classfile
   */
  public FileClassFileImpl( File classfile, String libraryLocation, byte libraryType ) {
    super( libraryLocation, libraryType );
    Assure.exists( "classfile", classfile );
    _classfile = classfile;
  }

  @Override
  public byte[] getBytes() {
    try {
      return Util.getFileByteContent( _classfile );
    } catch( Exception e ) {
      throw new RuntimeException( e.getMessage(), e );
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public final IBinaryType getBinaryType() {
    try {
      return ClassFileReader.read( _classfile, true );
    } catch( Exception e ) {
      // return null if an exception occurs
      e.printStackTrace();
      return null;
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String toString() {
    return String.format( "[FileClassFileImpl: bundleLocation: %s bundleType: %s accessRestriction: %s classfile: %s]",
        getLibraryLocation(), getLibraryType(), getAccessRestriction(), _classfile
    );
  }
  
} /* ENDCLASS */
