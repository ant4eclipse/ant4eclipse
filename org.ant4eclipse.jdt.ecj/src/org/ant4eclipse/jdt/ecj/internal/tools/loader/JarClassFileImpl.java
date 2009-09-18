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
package org.ant4eclipse.jdt.ecj.internal.tools.loader;

import org.ant4eclipse.core.Assert;

import org.eclipse.jdt.internal.compiler.classfmt.ClassFileReader;
import org.eclipse.jdt.internal.compiler.classfmt.ClassFormatException;
import org.eclipse.jdt.internal.compiler.env.IBinaryType;

import java.io.IOException;
import java.util.zip.ZipFile;

/**
 * DefaultClassFile --
 * 
 * @author Gerd Wuetherich (gerd@gerd-wuetherich.de)
 */
public class JarClassFileImpl extends AbstractClassFileImpl {

  /** the zip file */
  private ZipFile _zipFile;

  /** the zip entry name */
  private String  _zipEntryName;

  /**
   * @param zipEntryName
   * @param zipFile
   */
  public JarClassFileImpl(String zipEntryName, ZipFile zipFile, String libraryLocation, byte libraryType) {

    super(libraryLocation, libraryType);

    Assert.nonEmpty(zipEntryName);
    Assert.notNull(zipFile);

    this._zipEntryName = zipEntryName;
    this._zipFile = zipFile;
  }

  /**
   * {@inheritDoc}
   */
  public final IBinaryType getBinaryType() {
    try {
      return ClassFileReader.read(this._zipFile, this._zipEntryName, true);
    } catch (ClassFormatException e) {
      // TODO
      e.printStackTrace();
      return null;
    } catch (IOException e) {
      // TODO
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
