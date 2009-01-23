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
package org.ant4eclipse.jdt.internal.tools.ejc.loader;

import java.io.File;

import org.ant4eclipse.core.Assert;
import org.ant4eclipse.jdt.tools.ejc.loader.ClassFile;
import org.eclipse.jdt.internal.compiler.classfmt.ClassFileReader;
import org.eclipse.jdt.internal.compiler.env.IBinaryType;

/**
 * DefaultClassFile --
 * 
 * @author Gerd Wuetherich (gerd@gerd-wuetherich.de)
 */
public class FileClassFileImpl extends AbstractClassFileImpl implements ClassFile {

  /** the class file */
  private final File _classfile;

  /**
   * @param classfile
   */
  public FileClassFileImpl(final File classfile, final String libraryLocation, final byte libraryType) {
    super(libraryLocation, libraryType);

    Assert.exists(classfile);

    this._classfile = classfile;
  }

  /**
   * @see net.sf.ant4eclipse.tools.core.ejc.loader.ClassFile#getBinaryType()
   */
  public final IBinaryType getBinaryType() {
    try {
      return ClassFileReader.read(this._classfile, true);
    } catch (final Exception e) {
      // return null if an exception occurs
      e.printStackTrace();
      return null;
    }
  }

  /**
   * @see net.sf.ant4eclipse.tools.core.ejc.internal.loader.AbstractClassFileImpl#toString()
   */
  @Override
  public String toString() {
    final StringBuffer buffer = new StringBuffer();
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
