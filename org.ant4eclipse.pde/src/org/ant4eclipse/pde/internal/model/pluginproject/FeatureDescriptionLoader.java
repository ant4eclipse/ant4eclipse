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
package org.ant4eclipse.pde.internal.model.pluginproject;

import org.ant4eclipse.core.Assert;

import org.ant4eclipse.pde.internal.tools.FeatureDescription;
import org.ant4eclipse.pde.model.featureproject.FeatureManifest;
import org.ant4eclipse.pde.model.featureproject.FeatureManifestParser;
import org.ant4eclipse.pde.model.pluginproject.Constants;

import java.io.File;
import java.io.FileInputStream;
import java.util.jar.JarFile;
import java.util.zip.ZipEntry;

public class FeatureDescriptionLoader {

  /**
   * Parses the given feature (which might be a jar-file or a directory)
   * 
   * @param file
   * @return
   */
  public static FeatureDescription parseFeature(final File file) {
    Assert.exists(file);

    if (file.isFile() && file.getName().endsWith(".jar")) {
      return parseFeatureJarFile(file);
    } else if (file.isDirectory()) {
      return parseFeatureDirectory(file);
    }

    // TODO: Logging
    
    return null;
  }

  private static FeatureDescription parseFeatureJarFile(final File file) {
    Assert.isFile(file);

    try {
      // create jar file
      final JarFile jarFile = new JarFile(file);

      ZipEntry zipEntry = jarFile.getEntry(Constants.FEATURE_MANIFEST);

      FeatureManifest featureManifest = FeatureManifestParser.parseFeature(jarFile.getInputStream(zipEntry));
      return new FeatureDescription(file, featureManifest);

    } catch (Exception e) {
      throw new RuntimeException();
      // TODO: handle exception
    }
    // throw FileParserException since jar is no valid plugin jar
    // TODO: Konfigurierbar machen!!
    // throw new FileParserException("Could not parse plugin jar '" + file.getAbsolutePath()
    // + "' since it contains neither a Bundle-Manifest nor a plugin.xml!");
  }

  private static FeatureDescription parseFeatureDirectory(final File directory) {

    Assert.isDirectory(directory);

    try {
      // create jar file
      final File featureManifestFile = new File(directory, Constants.FEATURE_MANIFEST);

      FeatureManifest featureManifest = FeatureManifestParser.parseFeature(new FileInputStream(featureManifestFile));
      return new FeatureDescription(directory, featureManifest);

    } catch (Exception e) {
      throw new RuntimeException(e.getMessage(), e);
      // TODO: handle exception
    }
  }
}
