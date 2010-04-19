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
package org.ant4eclipse.lib.pde.internal.tools;

import org.ant4eclipse.lib.core.Assure;
import org.ant4eclipse.lib.core.osgi.BundleLayoutResolver;
import org.ant4eclipse.lib.core.osgi.ExplodedBundleLayoutResolver;
import org.ant4eclipse.lib.core.osgi.JaredBundleLayoutResolver;
import org.ant4eclipse.lib.core.util.ManifestHelper;
import org.ant4eclipse.lib.jdt.tools.ResolvedClasspathEntry;
import org.ant4eclipse.lib.jdt.tools.ResolvedClasspathEntry.AccessRestrictions;
import org.ant4eclipse.lib.pde.model.pluginproject.BundleSource;
import org.ant4eclipse.lib.pde.tools.PluginProjectLayoutResolver;
import org.ant4eclipse.lib.platform.model.resource.EclipseProject;
import org.eclipse.osgi.service.resolver.BundleDescription;
import org.eclipse.osgi.service.resolver.BundleSpecification;
import org.eclipse.osgi.service.resolver.ExportPackageDescription;

import java.io.File;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.jar.Manifest;

/**
 * <p>
 * The {@link BundleDependenciesResolver} is a helper class that can be used to resolve the dependencies of a given
 * bundle.
 * </p>
 * 
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 * @author Nils Hartmann (nils@nilshartmann.net)
 */
public class BundleDependenciesResolver {

  /** ECLIPSE_EXTENSIBLE_API */
  private static final String                      ECLIPSE_EXTENSIBLE_API = "Eclipse-ExtensibleAPI";

  /** the map of all resolved bundles */
  private Map<BundleDescription, BundleDependency> _resolvedBundles;

  /** the map of all imported packages */
  private Set<String>                              _allImportedPackages;

  /**
   * <p>
   * Creates a new instance of type {@link BundleDependenciesResolver}.
   * </p>
   */
  public BundleDependenciesResolver() {

    // create instance variables
    this._resolvedBundles = new HashMap<BundleDescription, BundleDependency>();
    this._allImportedPackages = new LinkedHashSet<String>();
  }

  /**
   * <p>
   * </p>
   * 
   * @param description
   * @return
   * @throws UnresolvedBundleException
   */
  public List<BundleDependency> resolveBundleClasspath(BundleDescription description) throws UnresolvedBundleException {
    return resolveBundleClasspath(description, true);
  }

  /**
   * <p>
   * </p>
   * 
   * @param description
   *          the bundle description
   * @param includeOptionalDependencies
   * @return
   * @throws UnresolvedBundleException
   */
  public List<BundleDependency> resolveBundleClasspath(BundleDescription description,
      boolean includeOptionalDependencies) throws UnresolvedBundleException {

    Assure.notNull("description", description);

    // A4ELogging.info("resolveBundleClasspath(%s)", TargetPlatformImpl.getBundleInfo(description));
    // new RuntimeException().printStackTrace();

    // Step 1: throw exception if bundle description is not resolved
    if (!description.isResolved()) {
      throw new UnresolvedBundleException(description);
    }

    // Step 2: add all packages that are imported...
    for (ExportPackageDescription exportPackageDescription : description.getResolvedImports()) {
      addImportedPackage(exportPackageDescription);
    }

    // Step 3: add all packages that come from required bundles...
    for (BundleDescription requiredBundle : description.getResolvedRequires()) {

      // add the required bundles...
      addRequiredBundle(requiredBundle);
    }

    // Step 4: add all re-exported bundles also...
    for (BundleDescription reexportedBundle : getReexportedBundles(description)) {
      addRequiredBundle(reexportedBundle);
    }

    // Step 5: if the bundle is a fragment, we have to add the dependencies of the host as well
    if (isFragment(description)) {
      BundleDescription hostDescription = getHost(description);
      resolveBundleClasspath(hostDescription);

      // create BundleDependency if necessary
      if (!this._resolvedBundles.containsKey(hostDescription)) {
        this._resolvedBundles.put(hostDescription, new BundleDependency(hostDescription, true));
      } else {
        BundleDependency bundleDependency = this._resolvedBundles.get(hostDescription);
        bundleDependency._isHostForRootBundle = true;
      }
    }

    return new LinkedList<BundleDependency>(this._resolvedBundles.values());
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
  public static BundleLayoutResolver getBundleLayoutResolver(BundleDescription bundleDescription) {

    // get the bundle source
    BundleSource bundleSource = (BundleSource) bundleDescription.getUserObject();

    // get the location
    File location = getLocation(bundleDescription);

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
      String tempdir = System.getProperty("java.io.tmpdir");

      // TODO: EXPANSION DIR!!
      return new JaredBundleLayoutResolver(location, new File(tempdir + File.separatorChar + "a4e_expand_dir"));
    }
  }

  /**
   * <p>
   * Returns <code>true</code> if the given <code>bundleDescription</code> is a fragment.
   * </p>
   * 
   * @param bundleDescription
   *          the {@link BundleDescription}
   * @return <code>true</code> if the given <code>bundleDescription</code> is a fragment.
   */
  public static boolean isFragment(BundleDescription bundleDescription) {

    // bundle description describes a fragment if bundleDescription.getHost() != null
    return bundleDescription.getHost() != null;
  }

  /**
   * <p>
   * Returns the host for the given {@link BundleDescription}.
   * </p>
   * 
   * @param bundleDescription
   *          the {@link BundleDescription}
   * @return the host for the given {@link BundleDescription}.
   */
  public static BundleDescription getHost(BundleDescription bundleDescription) {

    if (!bundleDescription.isResolved()) {
      throw new RuntimeException();
    }

    // bundle description describes a fragment ->
    // choose host as main bundle description
    if (isFragment(bundleDescription)) {
      return bundleDescription.getHost().getHosts()[0];
    }
    // bundle description describes a host ->
    // return bundle description
    else {
      return bundleDescription;
    }
  }

  /**
   * <p>
   * Returns the location for the given {@link BundleDescription}.
   * </p>
   * 
   * @param bundleDescription
   *          the {@link BundleDescription}
   * @return the location for the given {@link BundleDescription}.
   */
  public static File getLocation(BundleDescription bundleDescription) {

    // get the bundle source
    BundleSource bundleSource = (BundleSource) bundleDescription.getUserObject();

    // get the location
    File result = bundleSource.isEclipseProject() ? bundleSource.getAsEclipseProject().getFolder() : bundleSource
        .getAsFile();

    // return result
    return result;
  }

  /**
   * @param bundleDescription
   * @return
   */
  private Set<BundleDescription> getReexportedBundles(BundleDescription root) {

    // TODO: AE-171
    // A4ELogging
    // .info("Resolving reexported bundles for bundle '%s'", TargetPlatformImpl.getBundleInfo(bundleDescription));

    List<BundleDescription> resolvedDescriptions = new LinkedList<BundleDescription>();
    resolvedDescriptions.add(root);
    for (BundleDescription requiredBundle : root.getResolvedRequires()) {
      resolvedDescriptions.add(requiredBundle);
    }

    Set<BundleDescription> result = new HashSet<BundleDescription>();
    for (BundleDescription requiredBundle : root.getResolvedRequires()) {
      result.addAll(getReexportedBundles(requiredBundle, resolvedDescriptions));
    }
    return result;
  }

  /**
   * @param bundleDescription
   * @return
   */
  private Set<BundleDescription> getReexportedBundles(BundleDescription bundleDescription,
      List<BundleDescription> resolvedDescriptions) {

    Assure.notNull("bundleDescription", bundleDescription);

    // return if bundle already is resolved
    if (resolvedDescriptions.contains(bundleDescription)) {

      // // compute cycle list...
      // List<BundleDescription> cycleList = resolvedDescriptions.subList( /*
      // * resolvedDescriptions.indexOf(bundleDescription
      // * )
      // */0, resolvedDescriptions.size());
      //
      // // create the cycle list...
      // StringBuilder builder = new StringBuilder();
      // for (BundleDescription description : cycleList) {
      // builder.append(description.getSymbolicName() + "_" + description.getVersion());
      // builder.append(" -> ");
      // }
      // builder.append(bundleDescription.getSymbolicName() + "_" + bundleDescription.getVersion());
      //
      // // create warning...
      // A4ELogging.warn("****************************");
      // A4ELogging
      // .warn(
      // "Attention! The specified bundles contain cyclic dependencies via requiredBundle/reexport definitions (e.g. '%s').",
      // builder.toString());
      // A4ELogging.warn("****************************");

      return new HashSet<BundleDescription>();
    } else {
      resolvedDescriptions.add(bundleDescription);
    }

    // TODO: AE-171
    // A4ELogging.info("-> Resolving reexported bundles for bundle '%s'", TargetPlatformImpl
    // .getBundleInfo(bundleDescription));

    if (!bundleDescription.isResolved()) {
      String resolverErrors = TargetPlatformImpl.dumpResolverErrors(bundleDescription, true);
      String bundleInfo = TargetPlatformImpl.getBundleInfo(bundleDescription);
      throw new RuntimeException(String.format("Bundle '%s' is not resolved. Reason:\n%s", bundleInfo, resolverErrors));
    }

    // define the result set
    Set<BundleDescription> resultSet = new LinkedHashSet<BundleDescription>();

    // iterate over all required bundles
    for (BundleSpecification specification : bundleDescription.getRequiredBundles()) {

      // only add exported entries
      if (specification.isExported()) {

        // get the bundle description
        BundleDescription reexportedBundle = (BundleDescription) specification.getSupplier();

        // only add the re-exported bundle if it is not null
        if (reexportedBundle != null) {

          // add the bundle description
          resultSet.add(reexportedBundle);

          // add all re-exported bundle description recursively
          for (BundleDescription rereexportedBundle : getReexportedBundles(reexportedBundle, resolvedDescriptions)) {
            resultSet.add(rereexportedBundle);
          }

        }

      }
    }

    // iterate over all attached fragments
    for (BundleDescription fragment : bundleDescription.getFragments()) {

      // add the fragment
      // TODO should we really add the fragment here ?
      // -> fragments are added 'automatically' in BundleDependency.getResolvedClasspathEntry()
      resultSet.add(fragment);

      // add all re-exported bundles
      for (BundleDescription reexportedBundle : getReexportedBundles(fragment, resolvedDescriptions)) {
        resultSet.add(reexportedBundle);
      }
    }

    resolvedDescriptions.remove(bundleDescription);

    // return the result
    return resultSet;
  }

  /**
   * @param context
   * @param bundleDescription
   */
  private void addRequiredBundle(BundleDescription bundleDescription) {

    // get bundle dependency
    BundleDependency bundleDependency = getBundleDependency(bundleDescription);

    // set the 'required bundle' flag
    bundleDependency.setRequiredBundle(true);
  }

  /**
   * @param exportPackageDescription
   */
  private void addImportedPackage(ExportPackageDescription exportPackageDescription) {

    // get bundle dependency
    BundleDependency bundleDependency = getBundleDependency(exportPackageDescription.getSupplier());

    // add the imported package
    bundleDependency.addImportedPackage(exportPackageDescription.getName());

    this._allImportedPackages.add(exportPackageDescription.getName());
  }

  /**
   * <p>
   * Returns the {@link BundleDependency}.
   * </p>
   * 
   * @param bundleDescription
   * @return
   */
  private BundleDependency getBundleDependency(BundleDescription bundleDescription) {
    Assure.notNull("bundleDescription", bundleDescription);

    // get host
    BundleDescription host = getHost(bundleDescription);

    // create BundleDependency if necessary
    if (!this._resolvedBundles.containsKey(host)) {
      this._resolvedBundles.put(host, new BundleDependency(host));
    }

    // return BundleDependency
    return this._resolvedBundles.get(host);
  }

  /**
   * <p>
   * The class {@link BundleDependency} represents a dependency to another bundle. It also contains a set of all
   * packages that are imported by this dependency.
   * </p>
   * 
   * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
   */
  public class BundleDependency {

    /** the host */
    private BundleDescription _host;

    /** the fragment */
    private BundleDescription _fragment;

    /** list of imported packages for this bundle dependency */
    private Set<String>       _importedPackages;

    /** indicates that this bundle is referenced via Require-Bundle */
    private boolean           _isRequiredBundle;

    /** indicates that this bundle is the host for the root bundle */
    private boolean           _isHostForRootBundle;

    /**
     * <p>
     * Creates a new instance of type {@link BundleDependency}.
     * </p>
     * 
     * @param host
     *          the host
     */
    public BundleDependency(BundleDescription host) {
      Assure.notNull("host", host);

      this._host = host;
      this._isRequiredBundle = false;
      this._importedPackages = new LinkedHashSet<String>();
    }

    /**
     * <p>
     * Creates a new instance of type {@link BundleDependency}.
     * </p>
     * 
     * @param host
     *          the host
     * @param isHostForRootBundle
     *          indicates that this bundle is the host for the root bundle
     */
    public BundleDependency(BundleDescription host, boolean isHostForRootBundle) {
      Assure.notNull("host", host);

      this._host = host;
      this._isRequiredBundle = false;
      this._isHostForRootBundle = isHostForRootBundle;
      this._importedPackages = new LinkedHashSet<String>();
    }

    /**
     * <p>
     * Creates a new instance of type {@link BundleDependency}.
     * </p>
     * 
     * @param host
     *          the host
     * @param fragment
     *          the fragment (maybe <code>null</code>)
     */
    public BundleDependency(BundleDescription host, BundleDescription fragment) {
      Assure.notNull("host", host);

      this._host = host;
      this._fragment = fragment;
      this._isRequiredBundle = false;
      this._importedPackages = new LinkedHashSet<String>();
    }

    /**
     * <p>
     * Returns the host (never <code>null</code>).
     * </p>
     * 
     * @return the host (never <code>null</code>).
     */
    public BundleDescription getHost() {
      return this._host;
    }

    /**
     * <p>
     * Returns the fragments (never <code>null</code>).
     * </p>
     * 
     * @return the fragment (never <code>null</code>).
     */
    public BundleDescription[] getFragments() {

      // is single fragment set?
      if (this._fragment != null) {
        return new BundleDescription[] { this._fragment };
      }

      // if the host contains the eclipse extensible API header, return all known fragments
      else if (isEclipseExtensibleAPI()) {
        return this._host.getFragments();
      }

      // return empty array
      else {
        return new BundleDescription[0];
      }
    }

    /**
     * <p>
     * Returns <code>true</code> if a fragment is set.
     * </p>
     * 
     * @return <code>true</code> if a fragment is set.
     */
    public boolean hasFragments() {
      return (this._fragment != null)
          || (isEclipseExtensibleAPI() && (this._host.getFragments() != null) && (this._host.getFragments().length > 0));
    }

    /**
     * <p>
     * Returns the {@link ResolvedClasspathEntry}.
     * </p>
     * 
     * @return the {@link ResolvedClasspathEntry}.
     */
    public ResolvedClasspathEntry getResolvedClasspathEntry() {

      // if this dependency is the host bundle for the root bundle, everything is visible for the root bundle
      if (this._isHostForRootBundle) {

        // get the layout resolver
        BundleLayoutResolver layoutResolver = getBundleLayoutResolver(this._host);

        // return the result
        if (layoutResolver instanceof PluginProjectLayoutResolver) {
          return new ResolvedClasspathEntry(layoutResolver.resolveBundleClasspathEntries(),
              ((PluginProjectLayoutResolver) layoutResolver).getPluginProjectSourceFolders());
        } else {
          return new ResolvedClasspathEntry(layoutResolver.resolveBundleClasspathEntries());
        }
      }

      // create the list of all contained files
      List<File> classfiles = new LinkedList<File>();
      List<File> sourcefiles = new LinkedList<File>();

      // create the access restrictions
      AccessRestrictions accessRestrictions = new AccessRestrictions();

      // add the imported packages
      for (String importedPackage : this._importedPackages) {
        accessRestrictions.addPublicPackage(importedPackage);
      }

      // resolve the class path of the host
      BundleLayoutResolver layoutResolver = getBundleLayoutResolver(this._host);
      classfiles.addAll(Arrays.asList(layoutResolver.resolveBundleClasspathEntries()));
      if (layoutResolver instanceof PluginProjectLayoutResolver) {
        sourcefiles.addAll(Arrays
            .asList(((PluginProjectLayoutResolver) layoutResolver).getPluginProjectSourceFolders()));
      }
      if (this._isRequiredBundle) {
        addAllExportedPackages(this._host, accessRestrictions);
      }

      // resolve the class path of the fragment
      for (BundleDescription fragment : getFragments()) {
        layoutResolver = getBundleLayoutResolver(fragment);
        classfiles.addAll(Arrays.asList(layoutResolver.resolveBundleClasspathEntries()));
        if (layoutResolver instanceof PluginProjectLayoutResolver) {
          sourcefiles.addAll(Arrays.asList(((PluginProjectLayoutResolver) layoutResolver)
              .getPluginProjectSourceFolders()));
        }
        if (this._isRequiredBundle) {
          addAllExportedPackages(fragment, accessRestrictions);
        }
      }

      // return the result
      return new ResolvedClasspathEntry(classfiles.toArray(new File[0]), accessRestrictions, sourcefiles
          .toArray(new File[0]));
    }

    /**
     * <p>
     * Returns all referenced eclipse plug-in projects. This information is required to compute the build order between
     * eclipse plug-in projects.
     * </p>
     * 
     * @return a list with referenced eclipse plug-in projects
     */
    public List<EclipseProject> getReferencedPluginProjects() {

      // create the result list
      List<EclipseProject> result = new LinkedList<EclipseProject>();

      // add the host if it is an eclipse project
      BundleSource bundleSource = (BundleSource) this._host.getUserObject();

      if (bundleSource.isEclipseProject()) {
        result.add(bundleSource.getAsEclipseProject());
      }

      // add the fragment if it is an eclipse project
      for (BundleDescription fragment : getFragments()) {
        BundleSource fragmentSource = (BundleSource) fragment.getUserObject();

        if (fragmentSource.isEclipseProject()) {
          result.add(fragmentSource.getAsEclipseProject());
        }
      }

      // return the result
      return result;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
      return "BundleDependency [_host=" + this._host + ", _fragment=" + this._fragment + ", _importedPackages="
          + this._importedPackages + ", _isHostForRootBundle=" + this._isHostForRootBundle + ", _isRequiredBundle="
          + this._isRequiredBundle + "]";
    }

    /**
     * <p>
     * Marks this dependency as a 'Require-Bundle'-Dependency. In this case all exported packages of the referenced
     * bundles are accessible.
     * </p>
     * 
     * @param isRequiredBundle
     *          the required bundle flag
     */
    private void setRequiredBundle(boolean isRequiredBundle) {
      this._isRequiredBundle = isRequiredBundle;
    }

    /**
     * <p>
     * Adds the package with the specified name as an imported packages.
     * </p>
     * 
     * @param packageName
     *          the package name
     */
    private void addImportedPackage(String packageName) {
      this._importedPackages.add(packageName);
    }

    /**
     * <p>
     * Adds all exported packages of the given bundle description to the {@link AccessRestrictions}.
     * </p>
     * 
     * @param bundleDescription
     *          the bundle description
     * @param accessRestrictions
     *          the access restrictions
     */
    private void addAllExportedPackages(BundleDescription bundleDescription, AccessRestrictions accessRestrictions) {

      // get all exported packages of the given bundle description
      List<ExportPackageDescription> exportPackageDescriptions = new LinkedList<ExportPackageDescription>();

      // TODO: We should resolve and add the bundle of the selected exporter as well...
      exportPackageDescriptions.addAll(Arrays.asList(bundleDescription.getSelectedExports()));
      exportPackageDescriptions.addAll(Arrays.asList(bundleDescription.getSubstitutedExports()));

      // System.err.println(bundleDescription.getSymbolicName() + " (Exports) : "
      // + Arrays.asList(bundleDescription.getExportPackages()));
      // System.err.println(bundleDescription.getSymbolicName() + " (SelectedExports) : "
      // + Arrays.asList(bundleDescription.getSelectedExports()));
      // System.err.println(bundleDescription.getSymbolicName() + " (SubstitutedExports) : "
      // + Arrays.asList(bundleDescription.getSubstitutedExports()));

      // add all exported packages to the list of public (=visible) packages
      for (ExportPackageDescription exportPackageDescription : exportPackageDescriptions) {

        // If the package already has been imported via 'Import-Package',
        // the package must not be added from the required bundle!
        //
        // OSGi Service Platform, Core Specification Release 4, Version 4.1, 3.13.1 Require-Bundle:
        // "A bundle may both import packages (via Import-Package) and require one
        // or more bundles (via Require-Bundle), but if a package is imported via
        // Import-Package, it is not also visible via Require-Bundle: Import-Package
        // takes priority over Require-Bundle, and packages which are exported by a
        // required bundle and imported via Import-Package must not be treated as
        // split packages."
        if (!BundleDependenciesResolver.this._allImportedPackages.contains(exportPackageDescription.getName())) {
          accessRestrictions.addPublicPackage(exportPackageDescription.getName());
        }
      }
    }

    /**
     * <p>
     * </p>
     * 
     * @return
     */
    private boolean isEclipseExtensibleAPI() {
      BundleLayoutResolver resolver = getBundleLayoutResolver(this._host);
      Manifest manifest = resolver.getManifest();
      String eclipseExtensibleHeader = ManifestHelper.getManifestHeader(manifest, ECLIPSE_EXTENSIBLE_API);
      boolean isEclipseExtensibleHeaderAPI = (eclipseExtensibleHeader != null)
          && Boolean.parseBoolean(eclipseExtensibleHeader);
      return isEclipseExtensibleHeaderAPI;
    }

  }
}
