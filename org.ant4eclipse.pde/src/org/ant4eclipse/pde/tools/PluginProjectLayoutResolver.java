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
package org.ant4eclipse.pde.tools;

import java.io.File;
import java.io.FileInputStream;
import java.util.LinkedList;
import java.util.List;
import java.util.jar.Manifest;

import org.ant4eclipse.core.Assert;
import org.ant4eclipse.core.util.ManifestHelper;
import org.ant4eclipse.pde.model.buildproperties.PluginBuildProperties;
import org.ant4eclipse.pde.model.pluginproject.PluginProjectRole;
import org.ant4eclipse.platform.model.resource.EclipseProject;

/**
 * The {@link PluginProjectLayoutResolver} implements a {@link BundleLayoutResolver} for eclipse plug-in projects.
 * 
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 */
public class PluginProjectLayoutResolver implements BundleLayoutResolver {

  /** the eclipse project */
  private final EclipseProject _eclipseProject;

  /** the manifest file */
  private Manifest             _manifest;

  /**
   * <p>
   * Creates a new instance of type {@link PluginProjectLayoutResolver}.
   * </p>
   * 
   * @param project
   *          the eclipse plug-in project that has to be resolved
   */
  public PluginProjectLayoutResolver(final EclipseProject project) {
    Assert.notNull(project);
    Assert.assertTrue(project.hasRole(PluginProjectRole.class), "Project must have plugin project role!");

    // set the eclipse project
    this._eclipseProject = project;

    // retrieve the bundle manifest
    final File manifestFile = this._eclipseProject.getChild("META-INF/MANIFEST.MF");
    try {
      this._manifest = new Manifest(new FileInputStream(manifestFile));
    } catch (final Exception e) {
      // TODO:
      e.printStackTrace();
      throw new RuntimeException(e);
    }
  }

  /**
   * @see net.sf.ant4eclipse.tools.core.osgi.BundleLayoutResolver#getType()
   */
  public byte getType() {
    return PROJECT;
  }

  /**
   * @see net.sf.ant4eclipse.tools.core.osgi.BundleLayoutResolver#getManifest()
   */
  public Manifest getManifest() {
    return this._manifest;
  }

  /**
   * @see net.sf.ant4eclipse.tools.core.osgi.BundleLayoutResolver#getLocation()
   */
  public File getLocation() {
    return this._eclipseProject.getFolder();
  }

  /**
   * @see net.sf.ant4eclipse.tools.core.osgi.BundleLayoutResolver#resolveBundleClasspathEntries()
   */
  public File[] resolveBundleClasspathEntries() {

    // declare result
    final List<File> result = new LinkedList<File>();

    // resolve the bundle class path
    final String bundleClasspath[] = ManifestHelper.getBundleClasspath(this._manifest);

    final PluginProjectRole pluginProjectRole = (PluginProjectRole) this._eclipseProject
        .getRole(PluginProjectRole.class);

    final PluginBuildProperties buildProperties = pluginProjectRole.getBuildProperties();
    final File baseDir = this._eclipseProject.getFolder();

    for (int i = 0; i < bundleClasspath.length; i++) {
      if ((buildProperties != null) && buildProperties.hasLibrary(bundleClasspath[i])) {
        final String[] libaries = buildProperties.getLibrary(bundleClasspath[i]).getOutput();
        for (int j = 0; j < libaries.length; j++) {
          final File file = new File(baseDir, libaries[j]);
          if (!result.contains(file)) {
            result.add(file);
          }
        }
      } else {
        final File file = new File(baseDir, bundleClasspath[i]);
        result.add(file);
      }
    }
    if (buildProperties.hasLibrary(".")) {
      final String[] libaries = buildProperties.getLibrary(".").getOutput();
      for (int j = 0; j < libaries.length; j++) {
        final File file = new File(baseDir, libaries[j]);
        if (!result.contains(file)) {
          result.add(file);
        }
      }
    }

    // return result
    return (File[]) result.toArray(new File[result.size()]);
  }
}
