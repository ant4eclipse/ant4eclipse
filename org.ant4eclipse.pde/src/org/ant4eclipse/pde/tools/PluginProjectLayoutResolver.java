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
package org.ant4eclipse.pde.tools;

import org.ant4eclipse.core.Assert;
import org.ant4eclipse.core.osgi.BundleLayoutResolver;
import org.ant4eclipse.core.util.ManifestHelper;

import org.ant4eclipse.pde.model.buildproperties.PluginBuildProperties;
import org.ant4eclipse.pde.model.pluginproject.PluginProjectRole;

import org.ant4eclipse.platform.model.resource.EclipseProject;

import java.io.File;
import java.io.FileInputStream;
import java.util.LinkedList;
import java.util.List;
import java.util.jar.Manifest;

/**
 * The {@link PluginProjectLayoutResolver} implements a {@link BundleLayoutResolver} for eclipse plug-in projects.
 * 
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 */
public class PluginProjectLayoutResolver implements BundleLayoutResolver {

  /** the eclipse project */
  private EclipseProject _eclipseProject;

  /** the manifest file */
  private Manifest       _manifest;

  /**
   * <p>
   * Creates a new instance of type {@link PluginProjectLayoutResolver}.
   * </p>
   * 
   * @param project
   *          the eclipse plug-in project that has to be resolved
   */
  public PluginProjectLayoutResolver(EclipseProject project) {
    Assert.notNull(project);
    Assert.assertTrue(project.hasRole(PluginProjectRole.class), "Project must have plugin project role!");

    // set the eclipse project
    this._eclipseProject = project;

    // retrieve the bundle manifest
    File manifestFile = this._eclipseProject.getChild("META-INF/MANIFEST.MF");
    try {
      this._manifest = new Manifest(new FileInputStream(manifestFile));
    } catch (Exception e) {
      // TODO:
      e.printStackTrace();
      throw new RuntimeException(e);
    }
  }

  /**
   * {@inheritDoc}
   */
  public byte getType() {
    return PROJECT;
  }

  /**
   * {@inheritDoc}
   */
  public Manifest getManifest() {
    return this._manifest;
  }

  /**
   * {@inheritDoc}
   */
  public File getLocation() {
    return this._eclipseProject.getFolder();
  }

  /**
   * {@inheritDoc}
   */
  public File[] resolveBundleClasspathEntries() {

    // declare result
    List<File> result = new LinkedList<File>();

    // resolve the bundle class path
    String bundleClasspath[] = ManifestHelper.getBundleClasspath(this._manifest);

    PluginProjectRole pluginProjectRole = (PluginProjectRole) this._eclipseProject.getRole(PluginProjectRole.class);

    PluginBuildProperties buildProperties = pluginProjectRole.getBuildProperties();
    File baseDir = this._eclipseProject.getFolder();

    for (String element : bundleClasspath) {
      if ((buildProperties != null) && buildProperties.hasLibrary(element)) {
        String[] libaries = buildProperties.getLibrary(element).getOutput();
        for (String libarie : libaries) {
          File file = new File(baseDir, libarie);
          if (!result.contains(file)) {
            result.add(file);
          }
        }
      } else {
        File file = new File(baseDir, element);
        result.add(file);
      }
    }
    if (buildProperties.hasLibrary(".")) {
      String[] libaries = buildProperties.getLibrary(".").getOutput();
      for (String libarie : libaries) {
        File file = new File(baseDir, libarie);
        if (!result.contains(file)) {
          result.add(file);
        }
      }
    }

    // return result
    return result.toArray(new File[result.size()]);
  }
}
