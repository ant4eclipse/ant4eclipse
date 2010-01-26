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
package org.ant4eclipse.pde.test.builder;

import org.ant4eclipse.core.util.Utilities;

import org.ant4eclipse.lib.core.Assert;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.jar.JarEntry;
import java.util.jar.JarOutputStream;
import java.util.jar.Manifest;

public class JarBundleBuilder {
  public static int      BUFFER_SIZE = 10240;

  /** - */
  private BundleManifest _manifest;

  /** - */
  private String         _name;

  private String         _embeddedJarName;

  /**
   * <p>
   * Creates a new instance of type {@link JarBundleBuilder}.
   * </p>
   * 
   * @param name
   */
  public JarBundleBuilder(String name) {
    super();

    Assert.nonEmpty(name);

    // set the name
    this._name = name;

    // default manifest
    this._manifest = new BundleManifest(this._name);
  }

  /**
   * <p>
   * </p>
   * 
   * @return
   */
  public BundleManifest withBundleManifest() {
    return this._manifest;
  }

  public JarBundleBuilder withEmbeddedJar(String name) {
    this._embeddedJarName = name;
    this._manifest.withClassPath(".," + name + ".jar");
    return this;
  }

  /**
   * <p>
   * </p>
   * 
   * @param destinationDirectory
   * @return
   */
  public File createIn(File destinationDirectory) {

    if (!destinationDirectory.isDirectory()) {
      throw new RuntimeException("Directory '" + destinationDirectory + "' must be a directory.");
    }

    File jarFile = new File(destinationDirectory, this._name + ".jar");
    try {
      jarFile.createNewFile();
    } catch (IOException e) {
      new RuntimeException(e);
    }

    if (this._embeddedJarName == null) {
      createJarArchive(jarFile, this._manifest.getManifest());
    } else {
      File jarFile2 = new File(destinationDirectory, this._embeddedJarName + ".jar");
      try {
        jarFile2.createNewFile();
        createJarArchive(jarFile, this._manifest.getManifest(), new File[] { jarFile2 });
      } catch (IOException e) {
        new RuntimeException(e);
      }
      jarFile2.delete();
    }
    return jarFile;
  }

  /**
   * <p>
   * </p>
   * 
   * @param archiveFile
   */
  private void createJarArchive(File archiveFile, Manifest manifest) {
    createJarArchive(archiveFile, manifest, new File[0]);
  }

  /**
   * <p>
   * </p>
   * 
   * @param archiveFile
   * @param tobeJared
   */
  private void createJarArchive(File archiveFile, Manifest manifest, File[] tobeJared) {
    try {
      byte buffer[] = new byte[BUFFER_SIZE];
      // Open archive file
      FileOutputStream stream = new FileOutputStream(archiveFile);
      JarOutputStream out = new JarOutputStream(stream, manifest);

      for (int i = 0; i < tobeJared.length; i++) {
        if (tobeJared[i] == null || !tobeJared[i].exists() || tobeJared[i].isDirectory()) {
          continue; // Just in case...
        }
        System.out.println("Adding " + tobeJared[i].getName());

        // Add archive entry
        JarEntry jarAdd = new JarEntry(tobeJared[i].getName());
        jarAdd.setTime(tobeJared[i].lastModified());
        out.putNextEntry(jarAdd);

        // Write file to archive
        FileInputStream in = new FileInputStream(tobeJared[i]);
        Utilities.copy(in, out, buffer);
        Utilities.close(in);
      }
      Utilities.close(out);
      Utilities.close(stream);
      System.out.println("Adding completed OK");
    } catch (Exception ex) {
      ex.printStackTrace();
      System.out.println("Error: " + ex.getMessage());
    }
  }
}
