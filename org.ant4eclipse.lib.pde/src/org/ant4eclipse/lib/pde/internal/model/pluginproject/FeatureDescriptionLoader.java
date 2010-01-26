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
package org.ant4eclipse.lib.pde.internal.model.pluginproject;


import org.ant4eclipse.lib.core.Assure;
import org.ant4eclipse.lib.core.logging.A4ELogging;
import org.ant4eclipse.lib.pde.PdeExceptionCode;
import org.ant4eclipse.lib.pde.internal.tools.FeatureDescription;
import org.ant4eclipse.lib.pde.model.featureproject.FeatureManifest;
import org.ant4eclipse.lib.pde.model.featureproject.FeatureManifestParser;
import org.ant4eclipse.lib.pde.model.pluginproject.Constants;

import java.io.File;
import java.io.FileInputStream;
import java.util.jar.JarFile;
import java.util.zip.ZipEntry;

/**
 * <p>
 * Helper class that loads a feature description from a given feature directory or feature jar file.
 * </p>
 * 
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 */
public class FeatureDescriptionLoader {

  /**
   * <p>
   * Parses the given feature file. If the file doesn't contain a feature manifest, <code>null</code> will be returned.
   * </p>
   * 
   * @param file
   *          the feature file
   * @return the feature description
   */
  public static FeatureDescription parseFeature(File file) {
    Assure.exists(file);

    if (file.isFile() && file.getName().endsWith(".jar")) {
      return parseFeatureJarFile(file);
    } else if (file.isDirectory()) {
      return parseFeatureDirectory(file);
    }

    if (A4ELogging.isDebuggingEnabled()) {
      // warn if feature description is null
      A4ELogging.debug(PdeExceptionCode.WARNING_FILE_DOES_NOT_CONTAIN_FEATURE_MANIFEST_FILE.getMessage(), file
          .getAbsoluteFile());
    }
    // return null
    return null;
  }

  /**
   * <p>
   * Parses a {@link FeatureDescription} from a given feature jar file.
   * </p>
   * 
   * @param file
   *          the given feature jar file.
   * @return the {@link FeatureDescription}
   */
  private static FeatureDescription parseFeatureJarFile(File file) {
    Assure.isFile(file);

    try {
      // create jar file
      JarFile jarFile = new JarFile(file);

      // get the feature manifest
      ZipEntry zipEntry = jarFile.getEntry(Constants.FEATURE_MANIFEST);

      // return null if no feature manifest
      if (zipEntry == null) {
        return null;
      }

      // parse the feature manifest
      FeatureManifest featureManifest = FeatureManifestParser.parseFeature(jarFile.getInputStream(zipEntry));

      // return the feature description
      return new FeatureDescription(file, featureManifest);

    } catch (Exception e) {
      // throw new RuntimeException();
      // TODO: handle exception
      return null;
    }
  }

  /**
   * <p>
   * Parses the feature description for the given (feature) directory. The directory must contain a valid
   * <code>feature.xml</code> file, otherwise this method returns <code>null</code>.
   * </p>
   * 
   * @param directory
   *          the feature directory
   * @return the {@link FeatureDescription} or <code>null</code>, if the directory doesn't contain a
   *         <code>feature.xml</code> file, the method returns <code>null</code>.
   */
  private static FeatureDescription parseFeatureDirectory(File directory) {
    Assure.isDirectory(directory);

    try {
      // create jar file
      File featureManifestFile = new File(directory, Constants.FEATURE_MANIFEST);

      // return null if no feature manifest
      if (!featureManifestFile.exists() || !featureManifestFile.isFile()) {
        return null;
      }

      // parse the feature manifest
      FeatureManifest featureManifest = FeatureManifestParser.parseFeature(new FileInputStream(featureManifestFile));

      // return the feature description
      return new FeatureDescription(directory, featureManifest);

    } catch (Exception e) {
      // throw new RuntimeException(e.getMessage(), e);
      // TODO: handle exception
      return null;
    }
  }
}
