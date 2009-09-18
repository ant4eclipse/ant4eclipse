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
import org.ant4eclipse.core.logging.A4ELogging;

import org.ant4eclipse.pde.PdeExceptionCode;
import org.ant4eclipse.pde.model.pluginproject.BundleSource;
import org.ant4eclipse.pde.model.pluginproject.Constants;

import org.ant4eclipse.platform.model.resource.EclipseProject;

import org.eclipse.osgi.service.resolver.BundleDescription;
import org.eclipse.osgi.service.resolver.StateObjectFactory;
import org.osgi.framework.BundleException;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Iterator;
import java.util.Properties;
import java.util.jar.Attributes;
import java.util.jar.JarFile;
import java.util.jar.Manifest;

public class BundleDescriptionLoader {
  /** - */
  public static StateObjectFactory _factory = StateObjectFactory.defaultFactory;

  /** - */
  // TODO: ueberarbeiten..
  private static long              COUNTER  = 1l;

  /**
   * <p>
   * </p>
   * 
   * @param project
   * @return
   * @throws IOException
   * @throws FileNotFoundException
   * @throws BundleException
   */
  public static BundleDescription loadFromPluginProject(EclipseProject project) throws FileNotFoundException,
      IOException, BundleException {
    Assert.notNull(project);

    File manifestFile = project.getChild(Constants.OSGI_BUNDLE_MANIFEST);
    // TODO: handle projects with plugin.xml that may not have a MANIFEST-file
    Manifest manifest = new Manifest(new FileInputStream(manifestFile));

    BundleDescription description = createBundleDescription(manifest, project.getFolder().getAbsolutePath(), project);
    BundleSource.getBundleSource(description);
    return description;
  }

  private static BundleDescription createBundleDescription(Manifest manifest, String path, Object source)
      throws BundleException {

    long counter = isSystemBundle(manifest) ? 0 : COUNTER++;

    Properties manifestProperties = convertManifest(manifest);
    BundleDescription bundleDescription = _factory.createBundleDescription(null, manifestProperties, path, counter);

    bundleDescription.setUserObject(new BundleSource(source, manifest));
    return bundleDescription;
  }

  private static boolean isSystemBundle(Manifest manifest) {
    String isSystemBundle = manifest.getMainAttributes().getValue("Eclipse-SystemBundle");
    return (isSystemBundle != null) && "true".equals(isSystemBundle);
  }

  /**
   * <p>
   * </p>
   * 
   * @param manifest
   * @return
   */
  @SuppressWarnings("unchecked")
  private static Properties convertManifest(Manifest manifest) {
    Attributes attributes = manifest.getMainAttributes();
    Iterator iter = attributes.keySet().iterator();
    Properties result = new Properties();
    while (iter.hasNext()) {
      Attributes.Name key = (Attributes.Name) iter.next();
      result.put(key.toString(), attributes.get(key));
    }
    return result;
  }

  /**
   * Parses the given plugin (which might be a jar-file or a directory) and returns its BundleDescription or null if the
   * Plugin doesn't contain a MANIFEST-File
   * 
   * @param file
   * @return
   * @throws FileParserException
   */
  public static BundleDescription parsePlugin(File file) {
    Assert.exists(file);
    BundleDescription description = null;
    try {
      if (file.isFile() && file.getName().endsWith(".jar")) {
        description = parsePluginJarFile(file);
      } else if (file.isDirectory()) {
        description = parsePluginDirectory(file);
      }
      if (description == null && A4ELogging.isDebuggingEnabled()) {
        A4ELogging.debug(PdeExceptionCode.WARNING_FILE_DOES_NOT_CONTAIN_BUNDLE_MANIFEST_FILE.getMessage(), file
            .getAbsoluteFile());
      }
    } catch (FileNotFoundException e) {
      throw new RuntimeException(e.getMessage(), e);
    } catch (IOException e) {
      throw new RuntimeException(e.getMessage(), e);
    } catch (BundleException e) {
      throw new RuntimeException(e.getMessage(), e);
    }

    return description;
  }

  private static BundleDescription parsePluginJarFile(File file) {
    Assert.isFile(file);

    try {
      // create jar file
      JarFile jarFile = new JarFile(file);

      // support for plugins based on the osgi bundle model
      Manifest manifest = jarFile.getManifest();
      if ((manifest != null) && isBundleManifest(manifest)) {
        return createBundleDescription(manifest, file.getAbsolutePath(), file);
      }
    } catch (Exception e) {
      throw new RuntimeException("Exception while parsing plugin jar '" + file.getName() + "'!", e);
    }

    // throw FileParserException since jar is no valid plugin jar
    // TODO: Konfigurierbar machen!!
    // throw new FileParserException("Could not parse plugin jar '" + file.getAbsolutePath()
    // + "' since it contains neither a Bundle-Manifest nor a plugin.xml!");
    return null;
  }

  /**
   * Returns the plugin descriptor for the given plugin directory.
   * 
   * @param directory
   *          the plugin directory.
   * @return the plugin descriptor for the given plugin directory.
   * @throws FileParserException
   * @throws IOException
   * @throws FileNotFoundException
   * @throws BundleException
   */
  private static BundleDescription parsePluginDirectory(File directory) throws FileNotFoundException, IOException,
      BundleException {

    Assert.isDirectory(directory);

    // support for plugins based on the osgi bundle model
    File bundleManifestFile = new File(directory, Constants.OSGI_BUNDLE_MANIFEST);
    if (bundleManifestFile.isFile()) {
      Manifest manifest = new Manifest(new FileInputStream(bundleManifestFile));

      if (isBundleManifest(manifest)) {
        return createBundleDescription(manifest, directory.getAbsolutePath(), directory);
      }
    }

    // throw FileParserException since directory is no valid plugin
    // directory
    // throw new FileParserException("Could not parse plugin directory '" + directory.getAbsolutePath()
    // + "' since it contains neither a Bundle-Manifest nor a plugin.xml!");
    return null;
  }

  /**
   * Returns whether or not the specified manifest is a bundle manifest.
   * 
   * @param manifest
   *          the manifest to test.
   * @return whether or not the specified manifest is a bundle manifest.
   */
  private static boolean isBundleManifest(Manifest manifest) {
    return manifest.getMainAttributes().getValue("Bundle-SymbolicName") != null;
  }
}
