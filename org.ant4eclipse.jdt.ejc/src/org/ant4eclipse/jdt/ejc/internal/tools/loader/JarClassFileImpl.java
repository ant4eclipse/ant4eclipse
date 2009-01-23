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

import java.io.IOException;
import java.util.zip.ZipFile;

import org.ant4eclipse.core.Assert;
import org.eclipse.jdt.internal.compiler.classfmt.ClassFileReader;
import org.eclipse.jdt.internal.compiler.classfmt.ClassFormatException;
import org.eclipse.jdt.internal.compiler.env.IBinaryType;

/**
 * DefaultClassFile --
 * 
 * @author Gerd Wuetherich (gerd@gerd-wuetherich.de)
 */
public class JarClassFileImpl extends AbstractClassFileImpl {

  /** the zip file */
  private final ZipFile _zipFile;

  /** the zip entry name */
  private final String  _zipEntryName;

  /**
   * @param zipEntryName
   * @param zipFile
   */
  public JarClassFileImpl(final String zipEntryName, final ZipFile zipFile, final String libraryLocation,
      final byte libraryType) {

    super(libraryLocation, libraryType);

    Assert.nonEmpty(zipEntryName);
    Assert.notNull(zipFile);

    this._zipEntryName = zipEntryName;
    this._zipFile = zipFile;
  }

  /**
   * @see net.sf.ant4eclipse.tools.core.ejc.loader.ClassFile#getBinaryType()
   */
  public final IBinaryType getBinaryType() {
    try {
      return ClassFileReader.read(this._zipFile, this._zipEntryName, true);
    } catch (final ClassFormatException e) {
      // TODO
      e.printStackTrace();
      return null;
    } catch (final IOException e) {
      // TODO
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
    buffer.append("[JarClassFileImpl:");
    buffer.append(" bundleLocation: ");
    buffer.append(getLibraryLocation());
    buffer.append(" bundleType: ");
    buffer.append(getLibraryType());
    buffer.append(" accessRestriction: ");
    buffer.append(getAccessRestriction());
    buffer.append(" zipFile: ");
    buffer.append(this._zipFile);
    buffer.append(" zipEntryName: ");
    buffer.append(this._zipEntryName);
    buffer.append("]");
    return buffer.toString();
  }

}
