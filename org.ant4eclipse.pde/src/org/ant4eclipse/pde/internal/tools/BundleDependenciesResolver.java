package org.ant4eclipse.pde.internal.tools;

import java.io.File;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.ant4eclipse.core.Assert;
import org.ant4eclipse.core.logging.A4ELogging;
import org.ant4eclipse.core.osgi.BundleLayoutResolver;
import org.ant4eclipse.core.osgi.ExplodedBundleLayoutResolver;
import org.ant4eclipse.core.osgi.JaredBundleLayoutResolver;
import org.ant4eclipse.jdt.tools.ResolvedClasspathEntry;
import org.ant4eclipse.jdt.tools.ResolvedClasspathEntry.AccessRestrictions;
import org.ant4eclipse.pde.model.pluginproject.BundleSource;
import org.ant4eclipse.pde.tools.PluginProjectLayoutResolver;
import org.ant4eclipse.platform.model.resource.EclipseProject;

import org.eclipse.osgi.service.resolver.BaseDescription;
import org.eclipse.osgi.service.resolver.BundleDescription;
import org.eclipse.osgi.service.resolver.BundleSpecification;
import org.eclipse.osgi.service.resolver.ExportPackageDescription;
import org.eclipse.osgi.service.resolver.ImportPackageSpecification;

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
    _resolvedBundles = new HashMap<BundleDescription, BundleDependency>();
    _allImportedPackages = new LinkedHashSet<String>();
  }

  /**
   * <p>
   * </p>
   * 
   * @param description
   * @return
   */
  public List<BundleDependency> resolveBundleClasspath(final BundleDescription description) {
    return resolveBundleClasspath(description, true);
  }

  /**
   * <p>
   * 
   * </p>
   * 
   * @param description
   *          the bundle description
   * @return
   */
  public List<BundleDependency> resolveBundleClasspath(final BundleDescription description,
      boolean includeOptionalDependencies) {

    Assert.notNull(description);

    // Step 1: throw exception if bundle description is not resolved
    if (!description.isResolved()) {
      throw new RuntimeException(String.format("Bundle '%s' is not resolved. Reason:\n%s", TargetPlatformImpl
          .getBundleInfo(description), TargetPlatformImpl.dumpResolverErrors(description)));
    }

    // ImportPackageSpecification[] importPackageSpecifications = bundleDescription.getImportPackages();
    // for (ImportPackageSpecification importPackageSpecification : importPackageSpecifications) {
    // BaseDescription baseDescription = importPackageSpecification.getSupplier();
    // A4ELogging.info(baseDescription.getSupplier().toString());
    // }

    // Step 2: add all packages that are imported...
    for (ExportPackageDescription exportPackageDescription : description.getResolvedImports()) {
      addImportedPackage(exportPackageDescription);
    }

    // Step 3: add all packages that come from required bundles...
    for (BundleDescription requiredBundle : description.getResolvedRequires()) {
      // add the required bundles...
      addRequiredBundle(requiredBundle);

      // add all re-exported bundles also...
      for (BundleDescription reexportedBundle : getReexportedBundles(requiredBundle)) {
        addRequiredBundle(reexportedBundle);
      }
    }

    // Step 4: if the bundle is a fragment, we have to add the dependencies of the host as well
    if (isFragment(description)) {
      BundleDescription hostDescription = getHost(description);
      resolveBundleClasspath(hostDescription);

      // create BundleDependency if necessary
      if (!_resolvedBundles.containsKey(hostDescription)) {
        _resolvedBundles.put(hostDescription, new BundleDependency(hostDescription, true));
      } else {
        BundleDependency bundleDependency = _resolvedBundles.get(_resolvedBundles);
        bundleDependency._isHostForRootBundle = true;
      }
    }

    return new LinkedList<BundleDependency>(_resolvedBundles.values());
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
  public static BundleLayoutResolver getBundleLayoutResolver(final BundleDescription bundleDescription) {

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
  public static File getLocation(final BundleDescription bundleDescription) {

    // get the bundle source
    final BundleSource bundleSource = (BundleSource) bundleDescription.getUserObject();

    // get the location
    final File result = bundleSource.isEclipseProject() ? bundleSource.getAsEclipseProject().getFolder() : bundleSource
        .getAsFile();

    // return result
    return result;
  }

  /**
   * @param bundleDescription
   * @return
   */
  private BundleDescription[] getReexportedBundles(final BundleDescription bundleDescription) {
    Assert.notNull(bundleDescription);

    if (!bundleDescription.isResolved()) {
      String resolverErrors = TargetPlatformImpl.dumpResolverErrors(bundleDescription);
      String bundleInfo = TargetPlatformImpl.getBundleInfo(bundleDescription);
      throw new RuntimeException(String.format("Bundle '%s' is not resolved. Reason:\n%s", bundleInfo, resolverErrors));
    }

    // define the result set
    final Set<BundleDescription> resultSet = new LinkedHashSet<BundleDescription>();

    // iterate over all required bundles
    for (BundleSpecification specification : bundleDescription.getRequiredBundles()) {

      // only add exported entries
      if (specification.isExported()) {

        // get the bundle description
        final BundleDescription reexportedBundle = (BundleDescription) specification.getSupplier();

        // add the bundle description
        resultSet.add(reexportedBundle);

        // add all re-exported bundle description recursively
        for (BundleDescription rereexportedBundle : getReexportedBundles(reexportedBundle)) {
          resultSet.add(rereexportedBundle);
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
      for (BundleDescription reexportedBundle : getReexportedBundles(fragment)) {
        resultSet.add(reexportedBundle);
      }
    }

    // return the result
    return resultSet.toArray(new BundleDescription[resultSet.size()]);
  }

  /**
   * @param context
   * @param bundleDescription
   */
  private void addRequiredBundle(final BundleDescription bundleDescription) {

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

    _allImportedPackages.add(exportPackageDescription.getName());
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
    Assert.notNull(bundleDescription);

    // get host
    BundleDescription host = getHost(bundleDescription);

    // create BundleDependency if necessary
    if (!_resolvedBundles.containsKey(host)) {
      _resolvedBundles.put(host, new BundleDependency(host));
    }

    // return BundleDependency
    return _resolvedBundles.get(host);
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
      Assert.notNull(host);

      _host = host;
      _isRequiredBundle = false;
      _importedPackages = new LinkedHashSet<String>();
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
      Assert.notNull(host);

      _host = host;
      _isRequiredBundle = false;
      _isHostForRootBundle = isHostForRootBundle;
      _importedPackages = new LinkedHashSet<String>();
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
      Assert.notNull(host);

      _host = host;
      _fragment = fragment;
      _isRequiredBundle = false;
      _importedPackages = new LinkedHashSet<String>();
    }

    /**
     * <p>
     * Returns the host (never <code>null</code>).
     * </p>
     * 
     * @return the host (never <code>null</code>).
     */
    public BundleDescription getHost() {
      return _host;
    }

    /**
     * <p>
     * Returns the fragment (maybe <code>null</code>).
     * </p>
     * 
     * @return the fragment (maybe <code>null</code>).
     */
    public BundleDescription getFragment() {
      return _fragment;
    }

    /**
     * <p>
     * Returns <code>true</code> if a fragment is set.
     * </p>
     * 
     * @return <code>true</code> if a fragment is set.
     */
    public boolean hasFragment() {
      return _fragment != null;
    }

    /**
     * <p>
     * Returns the {@link ResolvedClasspathEntry}.
     * </p>
     * 
     * @return the {@link ResolvedClasspathEntry}.
     */
    public ResolvedClasspathEntry getResolvedClasspathEntry() {

      // create the list of all contained files
      List<File> files = new LinkedList<File>();

      // if this dependency is the host bundle for the root bundle, everything is visible for the root bundle
      if (_isHostForRootBundle) {

        // get the layout resolver
        BundleLayoutResolver layoutResolver = getBundleLayoutResolver(_host);

        // add the resolved bundle class path entries
        files.addAll(Arrays.asList(layoutResolver.resolveBundleClasspathEntries()));

        // return the result
        return new ResolvedClasspathEntry(files.toArray(new File[0]));
      }

      // create the access restrictions
      AccessRestrictions accessRestrictions = new AccessRestrictions();

      // add the imported packages
      for (String importedPackage : _importedPackages) {
        accessRestrictions.addPublicPackage(importedPackage);
      }

      // resolve the class path of the host
      BundleLayoutResolver layoutResolver = getBundleLayoutResolver(_host);
      files.addAll(Arrays.asList(layoutResolver.resolveBundleClasspathEntries()));
      if (_isRequiredBundle) {
        addAllExportedPackages(_host, accessRestrictions);
      }

      // resolve the class path of the fragment
      if (hasFragment()) {
        layoutResolver = getBundleLayoutResolver(getFragment());
        files.addAll(Arrays.asList(layoutResolver.resolveBundleClasspathEntries()));
        if (_isRequiredBundle) {
          addAllExportedPackages(getFragment(), accessRestrictions);
        }
      }

      // return the result
      return new ResolvedClasspathEntry(files.toArray(new File[0]), accessRestrictions);
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
      final BundleSource bundleSource = (BundleSource) _host.getUserObject();

      if (bundleSource.isEclipseProject()) {
        result.add(bundleSource.getAsEclipseProject());
      }

      // add the fragment if it is an eclipse project
      if (hasFragment()) {
        final BundleSource fragmentSource = (BundleSource) getFragment().getUserObject();

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
      return "BundleDependency [_host=" + _host + ", _fragment=" + _fragment + ", _importedPackages="
          + _importedPackages + ", _isHostForRootBundle=" + _isHostForRootBundle + ", _isRequiredBundle="
          + _isRequiredBundle + "]";
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
      _isRequiredBundle = isRequiredBundle;
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
      _importedPackages.add(packageName);
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
      ExportPackageDescription[] exportPackageDescriptions = bundleDescription.getSelectedExports();

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
        if (!_allImportedPackages.contains(exportPackageDescription.getName())) {
          accessRestrictions.addPublicPackage(exportPackageDescription.getName());
        }
      }
    }
  }
}
