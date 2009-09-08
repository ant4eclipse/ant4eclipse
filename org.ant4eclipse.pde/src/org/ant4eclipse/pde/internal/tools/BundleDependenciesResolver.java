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
import org.ant4eclipse.core.osgi.BundleLayoutResolver;
import org.ant4eclipse.core.osgi.ExplodedBundleLayoutResolver;
import org.ant4eclipse.core.osgi.JaredBundleLayoutResolver;
import org.ant4eclipse.jdt.tools.ResolvedClasspathEntry;
import org.ant4eclipse.jdt.tools.ResolvedClasspathEntry.AccessRestrictions;
import org.ant4eclipse.pde.model.pluginproject.BundleSource;
import org.ant4eclipse.pde.tools.PluginProjectLayoutResolver;
import org.ant4eclipse.platform.model.resource.EclipseProject;
import org.eclipse.osgi.service.resolver.BundleDescription;
import org.eclipse.osgi.service.resolver.BundleSpecification;
import org.eclipse.osgi.service.resolver.ExportPackageDescription;

/**
 * <p>
 * </p>
 * 
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 * @author Nils Hartmann (nils@nilshartmann.net)
 */
public class BundleDependenciesResolver {

  /** the map of all resolved bundles */
  private Map<BundleDescription, BundleDependency> _resolvedBundles;

  /** - */
  private Set<String>                              _allImportedPackages;

  /**
   * <p>
   * 
   * </p>
   */
  public BundleDependenciesResolver() {
    _resolvedBundles = new HashMap<BundleDescription, BundleDependency>();
    _allImportedPackages = new LinkedHashSet<String>();
  }

  /**
   * <p>
   * </p>
   * 
   * @param bundleDescription
   * @return
   */
  public List<BundleDependency> resolveBundleClasspath(final BundleDescription bundleDescription) {

    Assert.notNull(bundleDescription);

    if (!bundleDescription.isResolved()) {
      String resolverErrors = TargetPlatformImpl.dumpResolverErrors(bundleDescription);
      String bundleInfo = TargetPlatformImpl.getBundleInfo(bundleDescription);
      throw new RuntimeException(String.format("Bundle '%s' is not resolved. Reason:\n%s", bundleInfo, resolverErrors));
    }

    // Step 1: add all packages that are imported...
    for (ExportPackageDescription exportPackageDescription : bundleDescription.getResolvedImports()) {

      addImportedPackage(exportPackageDescription);
    }

    // Step 2: add all packages that come from required bundles...
    for (BundleDescription requiredBundle : bundleDescription.getResolvedRequires()) {

      addRequiredBundle(requiredBundle);

      // add all re-exported bundles also...
      for (BundleDescription reexportedBundle : getReexportedBundles(requiredBundle)) {

        addRequiredBundle(reexportedBundle);
      }
    }

    // Step 3: add the bundle itself to the class path
    // addBundleToClasspath(context, bundleDescription);

    return new LinkedList<BundleDependency>(_resolvedBundles.values());
  }

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

    // TODO
    // // OSGi Service Platform, Core Specification Release 4, Version 4.1, 3.13.1 Require-Bundle:
    // // //
    // // // "A bundle may both import packages (via Import-Package) and require one
    // // // or more bundles (via Require-Bundle), but if a package is imported via
    // // // Import-Package, it is not also visible via Require-Bundle: Import-Package
    // // // takes priority over Require-Bundle, and packages which are exported by a
    // // // required bundle and imported via Import-Package must not be treated as
    // // // split packages."

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
      _resolvedBundles.put(host, new BundleDependency(host, _allImportedPackages));
    }

    // return BundleDependency
    return _resolvedBundles.get(host);
  }

  /**
   * <p>
   * Returns the host for the given {@link BundleDescription}.
   * </p>
   * 
   * @param bundleDescription
   * @return
   */
  private BundleDescription getHost(BundleDescription bundleDescription) {

    // bundle description describes a fragment ->
    // choose host as main bundle description
    if (bundleDescription.getHost() != null) {
      return bundleDescription.getHost().getHosts()[0];
    }
    // bundle description describes a host ->
    // return bundle description
    else {
      return bundleDescription;
    }
  }

  /**
   * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
   */
  public class BundleDependency {

    /** the host */
    private BundleDescription _host;

    /** list of imported packages for this bundle dependency */
    private Set<String>       _importedPackages;

    /** list of all imported packages */
    private Set<String>       _allImportedPackages;

    /** - */
    private boolean           _isRequiredBundle;

    /**
     *
     */
    public BundleDependency(BundleDescription host, Set<String> allImportedPackages) {
      Assert.notNull(host);

      _host = host;
      _isRequiredBundle = false;
      _importedPackages = new LinkedHashSet<String>();
      _allImportedPackages = allImportedPackages;
    }

    public List<EclipseProject> getReferencedPluginProjects() {

      List<EclipseProject> result = new LinkedList<EclipseProject>();

      // get the bundle source
      final BundleSource bundleSource = (BundleSource) _host.getUserObject();

      if (bundleSource.isEclipseProject()) {
        result.add(bundleSource.getAsEclipseProject());
      }

      for (BundleDescription fragment : _host.getFragments()) {
        final BundleSource fragmentSource = (BundleSource) fragment.getUserObject();

        if (fragmentSource.isEclipseProject()) {
          result.add(fragmentSource.getAsEclipseProject());
        }
      }

      return result;
    }

    /**
     * @return
     */
    public boolean isRequiredBundle() {
      return _isRequiredBundle;
    }

    /**
     * @param isRequiredBundle
     */
    public void setRequiredBundle(boolean isRequiredBundle) {
      _isRequiredBundle = isRequiredBundle;
    }

    /**
     * @param packageName
     */
    public void addImportedPackage(String packageName) {
      _importedPackages.add(packageName);
    }

    /**
     * <p>
     * Returns the {@link ResolvedClasspathEntry}.
     * </p>
     * 
     * @return
     */
    public ResolvedClasspathEntry getResolvedClasspathEntry() {

      // System.err.println(toString());

      List<File> files = new LinkedList<File>();
      AccessRestrictions accessRestrictions = new AccessRestrictions();

      for (String importedPackage : _importedPackages) {
        accessRestrictions.addPublicPackage(importedPackage);
      }

      BundleLayoutResolver layoutResolver = getBundleLayoutResolver(_host);
      files.addAll(Arrays.asList(layoutResolver.resolveBundleClasspathEntries()));
      if (_isRequiredBundle) {
        addAllExportedPackages(accessRestrictions, _host);
      }

      for (BundleDescription fragment : _host.getFragments()) {
        layoutResolver = getBundleLayoutResolver(fragment);
        files.addAll(Arrays.asList(layoutResolver.resolveBundleClasspathEntries()));
        if (_isRequiredBundle) {
          addAllExportedPackages(accessRestrictions, fragment);
        }
      }

      // System.err.println(accessRestrictions);

      return new ResolvedClasspathEntry(files.toArray(new File[0]), accessRestrictions);
    }

    private void addAllExportedPackages(AccessRestrictions accessRestrictions, BundleDescription bundleDescription) {

      // TODO getSelectedExports()?
      ExportPackageDescription[] exportPackageDescriptions = bundleDescription.getSelectedExports();

      for (ExportPackageDescription exportPackageDescription : exportPackageDescriptions) {
        if (!_allImportedPackages.contains(exportPackageDescription.getName())) {
          accessRestrictions.addPublicPackage(exportPackageDescription.getName());
        }
      }
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
     * <p>
     * Returns the location for the given {@link BundleDescription}.
     * </p>
     * 
     * @param bundleDescription
     *          the {@link BundleDescription}
     * @return the location for the given {@link BundleDescription}.
     */
    private File getLocation(final BundleDescription bundleDescription) {

      // get the bundle source
      final BundleSource bundleSource = (BundleSource) bundleDescription.getUserObject();

      // get the location
      final File result = bundleSource.isEclipseProject() ? bundleSource.getAsEclipseProject().getFolder()
          : bundleSource.getAsFile();

      // return result
      return result;
    }

    /**
     * @see java.lang.Object#toString()
     */
    public String toString() {
      StringBuffer buffer = new StringBuffer();
      buffer.append("[BundleDependency:");
      buffer.append(" _host: ");
      buffer.append(_host);
      buffer.append(" _accessiblePackages: ");
      buffer.append(_importedPackages);
      buffer.append(" _isRequiredBundle: ");
      buffer.append(_isRequiredBundle);
      buffer.append("]");
      return buffer.toString();
    }
  }
}
