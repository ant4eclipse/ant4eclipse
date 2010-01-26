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

import org.ant4eclipse.core.Assert;


import org.ant4eclipse.lib.jdt.ecj.ClassFile;
import org.ant4eclipse.lib.jdt.ecj.internal.tools.DefaultReferableType;
import org.eclipse.jdt.internal.compiler.classfmt.ClassFileReader;
import org.eclipse.jdt.internal.compiler.env.IBinaryType;

import java.io.File;

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
  public FileClassFileImpl(File classfile, String libraryLocation, byte libraryType) {
    super(libraryLocation, libraryType);

    Assert.exists(classfile);

    this._classfile = classfile;
  }

  /**
   * {@inheritDoc}
   */
  public final IBinaryType getBinaryType() {
    try {
      return ClassFileReader.read(this._classfile, true);
    } catch (Exception e) {
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
    StringBuffer buffer = new StringBuffer();
    buffer.append("[FileClassFileImpl:");
    buffer.append(" bundleLocation: ");
    buffer.append(getLibraryLocation());
    buffer.append(" bundleType: ");
    buffer.append(getLibraryType());
    buffer.append(" accessRestriction: ");
    buffer.append(getAccessRestriction());
    buffer.append(" classfile: ");
    buffer.append(this._classfile);
    buffer.append("]");
    return buffer.toString();
  }
}
