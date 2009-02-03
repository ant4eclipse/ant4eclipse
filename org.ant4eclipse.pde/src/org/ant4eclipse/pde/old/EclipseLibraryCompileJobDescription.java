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
package org.ant4eclipse.pde.old;

import java.io.File;
import java.util.jar.Manifest;

import org.ant4eclipse.core.Assert;
import org.ant4eclipse.pde.model.pluginproject.BundleSource;
import org.ant4eclipse.pde.osgi.BundleLayoutResolver;
import org.ant4eclipse.pde.osgi.ExplodedBundleLayoutResolver;
import org.ant4eclipse.pde.osgi.JaredBundleLayoutResolver;
import org.ant4eclipse.pde.osgi.internal.ClassFileLoaderFactory;
import org.ant4eclipse.pde.osgi.internal.FilteredClasspathClassFileLoader;
import org.ant4eclipse.pde.tools.BundleClasspathResolver;
import org.ant4eclipse.pde.tools.PluginProjectLayoutResolver;
import org.ant4eclipse.pde.tools.TargetPlatform;
import org.ant4eclipse.platform.model.resource.EclipseProject;
import org.eclipse.osgi.service.resolver.BundleDescription;
import org.eclipse.osgi.service.resolver.ExportPackageDescription;
import org.eclipse.osgi.service.resolver.State;

/**
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 */
public class EclipseLibraryCompileJobDescription extends AbstractOsgiProjectBasedCompileJobDescription {

  /** the plug-in builder library context */
  private PluginLibraryBuilderContext _libraryBuilderContext;

  /**
   * @param eclipseProject
   */
  public EclipseLibraryCompileJobDescription(final EclipseProject eclipseProject) {
    super(eclipseProject);
  }

  /**
   * @param context
   */
  public void setContext(final PluginLibraryBuilderContext context) {
    this._libraryBuilderContext = context;
  }

  /**
   * @see net.sf.ant4eclipse.tools.core.ejc.CompileJobDescription#getOutputFolder()
   */
  public File getOutputFolder(final File sourceFolder) {
    return this._libraryBuilderContext.getClassesFolder();
  }

  /**
   * @see net.sf.ant4eclipse.tools.core.ejc.CompileJobDescription#getSourceFolders()
   */
  public File[] getSourceFolders() {
    return this._libraryBuilderContext.getSourceFolder();
  }

  /**
   * @see net.sf.ant4eclipse.tools.core.ejc.AbstractJdtProjectBasedCompileJobDescription#configureDependencies(net.sf.ant4eclipse.tools.core.ejc.CompileJobDescription.DependencyConfiguration)
   */
  public void configureDependencies(final DependencyConfiguration dependencyConfiguration) {

    final TargetPlatform targetPlatform = this._libraryBuilderContext.getTargetPlatform();
    final State state = targetPlatform.getState();

    // TODO: Clean...
    // TODO: DAS IST RICHTIGER MIST: DOPPELTER STATE!!!
    final BundleDescription bundleDescription = state.getBundle(this._libraryBuilderContext.getBundleDescription()
        .getSymbolicName(), this._libraryBuilderContext.getBundleDescription().getVersion());

    // final BundleProjectClassFileLoader bundleClassLoader = new BundleProjectClassFileLoader();

    Assert.notNull(bundleDescription);
    Assert.assertTrue(bundleDescription.isResolved(), "Bundle '" + bundleDescription.getName()
        + "' has to be resolved!");

    // add all packages that are IMPORTED to class loader...
    final ExportPackageDescription[] descriptions = bundleDescription.getResolvedImports();
    for (int i = 0; i < descriptions.length; i++) {
      dependencyConfiguration.addWiredPackage(descriptions[i].getName(), getClassFileLoader(descriptions[i]
          .getExporter(), true));
    }

    // add all packages that come from REQUIRED bundles...

    // // // OSGi Service Platform, Core Specification Release 4, Version 4.1, 3.13.1 Require-Bundle:
    // // //
    // // // "A bundle may both import packages (via Import-Package) and require one
    // // // or more bundles (via Require-Bundle), but if a package is imported via
    // // // Import-Package, it is not also visible via Require-Bundle: Import-Package
    // // // takes priority over Require-Bundle, and packages which are exported by a
    // // // required bundle and imported via Import-Package must not be treated as
    // // // split packages."

    final BundleDescription[] requiredBundles = BundleClasspathResolver.getAllRequiredBundles(bundleDescription);
    for (int i = 0; i < requiredBundles.length; i++) {

      final BundleDescription requiredBundle = requiredBundles[i];

      final ExportPackageDescription[] exportPackages = requiredBundle.getExportPackages();

      for (int j = 0; j < exportPackages.length; j++) {
        final ExportPackageDescription exportPackageDescription = exportPackages[j];

        dependencyConfiguration.addWiredPackage(exportPackageDescription.getName(), getClassFileLoader(
            exportPackageDescription.getExporter(), true));
      }

      if (requiredBundle.getHost() != null) {
        // required bundle is a fragment: add packages of host as well
        final ExportPackageDescription[] fragmentPackages = BundleClasspathResolver.getPackagesFromHost(requiredBundle
            .getHost());

        for (int j = 0; j < fragmentPackages.length; j++) {
          final ExportPackageDescription exportPackageDescription = fragmentPackages[j];
          dependencyConfiguration.addWiredPackage(exportPackageDescription.getName(), getClassFileLoader(
              exportPackageDescription.getExporter(), true));
        }
      }
    }

    // add self
    dependencyConfiguration.addBundleClassFileLoader(getClassFileLoader(bundleDescription, false));
  }

  /**
   * @param bundleDescription
   * @param filtered
   * @return
   */
  private ClassFileLoader getClassFileLoader(final BundleDescription bundleDescription, final boolean filtered) {
    Assert.assertTrue(bundleDescription.isResolved(), "<" + bundleDescription.getLocation()
        + ">.isResolved() has to be true!");

    BundleDescription host = null;
    BundleDescription[] fragments = null;

    // bundle description describes a fragment ->
    // choose host as main bundle description and add fragments
    if (bundleDescription.getHost() != null) {
      host = bundleDescription.getHost().getHosts()[0];
      fragments = host.getFragments();
    }
    // bundle description describes a host ->
    // just add fragments
    else {
      host = bundleDescription;
      fragments = bundleDescription.getFragments();
    }

    // class file loader still exists?
    final ClassFileLoader classFileLoader = getClassFileLoader(getLocation(host));

    if (classFileLoader != null) {
      return classFileLoader;
    } else {
      return createNewBundleFileLoader(host, fragments, filtered);
    }
  }

  /**
   * @param host
   * @param fragments
   * @param filtered
   * @return
   */
  private ClassFileLoader createNewBundleFileLoader(final BundleDescription host, final BundleDescription[] fragments,
      final boolean filtered) {

    // declare result
    ClassFileLoader result = null;

    // set bundle count
    final int bundleCount = fragments.length + 1;

    // get the resolvers
    final BundleLayoutResolver[] resolvers = new BundleLayoutResolver[bundleCount];
    resolvers[0] = getBundleLayoutResolver(host);
    for (int i = 0; i < fragments.length; i++) {
      resolvers[i + 1] = getBundleLayoutResolver(fragments[i]);
    }

    // create class path class file loaders
    final ClassFileLoader[] fileLoaders = new ClassFileLoader[bundleCount];
    for (int i = 0; i < bundleCount; i++) {
      fileLoaders[i] = ClassFileLoaderFactory.createClasspathClassFileLoader(resolvers[i]);
    }

    // create compound class file loader if necessary
    if (bundleCount > 1) {
      result = new CompoundClassFileLoaderImpl(fileLoaders);
    } else {
      result = fileLoaders[0];
    }

    // create filtering class file loader if necessary
    if (filtered) {

      // get manifests for all the bundles
      final Manifest[] manifests = new Manifest[bundleCount];
      for (int i = 0; i < bundleCount; i++) {
        manifests[i] = resolvers[i].getManifest();
      }

      // create filtered class path class file loader
      result = new FilteredClasspathClassFileLoader(manifests, result);
    }

    // store class file loader
    storeClassFileLoader(getLocation(host), result);

    // return result
    return result;
  }

  /**
   * <p>
   * Returns a {@link BundleLayoutResolver} for the given {@link BundleDescription}.
   * </p>
   *
   * @param bundleDescription
   *          the given {@link BundleDescription}.
   * @return a {@link BundleLayoutResolver} for the given {@link BundleDescription}.
   */
  private BundleLayoutResolver getBundleLayoutResolver(final BundleDescription bundleDescription) {

    // get the bundle source
    final BundleSource bundleSource = (BundleSource) bundleDescription.getUserObject();

    // get the location
    final File location = getLocation(bundleDescription);

    // eclipse project -> PluginProjectLayoutResolver
    if (bundleSource.isEclipseProject()) {
      return new PluginProjectLayoutResolver(bundleSource.getAsEclipseProject());
    }
    // directory -> ExplodedBundleLayoutResolver
    else if (location.isDirectory()) {
      return new ExplodedBundleLayoutResolver(location);
    }
    // jar -> JaredBundleLayoutResolver
    else {
      // TODO: EXPANSION DIR!!
      return new JaredBundleLayoutResolver(location, new File("c:\\temp"));
    }
  }

  /**
   * @param bundleDescription
   * @return
   */
  private File getLocation(final BundleDescription bundleDescription) {

    // get the bundle source
    final BundleSource bundleSource = (BundleSource) bundleDescription.getUserObject();

    // get the location
    final File result = bundleSource.isEclipseProject() ? bundleSource.getAsEclipseProject().getFolder() : bundleSource
        .getAsFile();

    // return result
    return result;
  }
}
